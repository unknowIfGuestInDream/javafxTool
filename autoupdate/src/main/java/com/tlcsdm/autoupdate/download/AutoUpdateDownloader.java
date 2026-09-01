/*
 * Copyright (c) 2026 unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.autoupdate.download;

import com.tlcsdm.autoupdate.model.UpdateInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;

/**
 * 更新包下载器.
 *
 * <p>基于 JDK 内置 {@link HttpClient} 实现, 支持重定向 (GitHub release 资源会跳转到
 * 对象存储), 下载进度回调以及 SHA-256 校验. 该类不依赖 JavaFX, 可独立进行单元测试.</p>
 *
 * @author unknowIfGuestInDream
 */
public class AutoUpdateDownloader {

    private static final int BUFFER_SIZE = 8192;
    private static final String USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50";

    private final HttpClient client;
    private final Duration requestTimeout;

    /**
     * 使用默认配置创建下载器.
     */
    public AutoUpdateDownloader() {
        this(HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(15)).build(), Duration.ofMinutes(30));
    }

    /**
     * 使用自定义 {@link HttpClient} 创建下载器, 便于测试.
     *
     * @param client         HTTP 客户端
     * @param requestTimeout 单次请求超时时间
     */
    public AutoUpdateDownloader(HttpClient client, Duration requestTimeout) {
        this.client = client;
        this.requestTimeout = requestTimeout;
    }

    /**
     * 下载更新包到指定目录.
     *
     * @param info      更新信息
     * @param targetDir 保存目录, 不存在时自动创建
     * @param listener  进度回调, 可为 {@code null}
     * @return 下载完成的文件路径
     * @throws IOException          下载失败或校验不通过时抛出
     * @throws InterruptedException 下载被中断时抛出
     */
    public Path download(UpdateInfo info, Path targetDir, DownloadProgressListener listener)
        throws IOException, InterruptedException {
        Files.createDirectories(targetDir);
        Path base = targetDir.toAbsolutePath().normalize();
        Path target = base.resolve(info.getFileName()).normalize();
        // 防止文件名包含路径分隔符或 ".." 导致写出目标目录之外
        if (!base.equals(target.getParent())) {
            throw new IOException("Illegal download file name: " + info.getFileName());
        }
        Path temp = base.resolve(target.getFileName().toString() + ".part");

        HttpRequest request = HttpRequest.newBuilder(URI.create(info.getDownloadUrl())).GET()
            .timeout(requestTimeout).header("User-Agent", USER_AGENT).build();
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() != 200) {
            closeQuietly(response.body());
            throw new IOException("Unexpected HTTP status " + response.statusCode() + " for " + info.getDownloadUrl());
        }

        long total = contentLength(response, info);
        try (InputStream in = response.body();
             OutputStream out = Files.newOutputStream(temp)) {
            copy(in, out, total, listener);
        }

        if (info.hasChecksum()) {
            String actual = sha256(temp);
            if (!actual.equalsIgnoreCase(info.getSha256())) {
                Files.deleteIfExists(temp);
                throw new IOException("Checksum mismatch, expected " + info.getSha256() + " but got " + actual);
            }
        }
        Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }

    private void copy(InputStream in, OutputStream out, long total, DownloadProgressListener listener)
        throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        long read = 0;
        int len;
        if (listener != null) {
            listener.onProgress(0, total);
        }
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
            read += len;
            if (listener != null) {
                listener.onProgress(read, total);
            }
        }
        out.flush();
    }

    private static long contentLength(HttpResponse<?> response, UpdateInfo info) {
        long headerLength = response.headers().firstValueAsLong("Content-Length").orElse(-1L);
        if (headerLength > 0) {
            return headerLength;
        }
        return info.getSize() > 0 ? info.getSize() : -1L;
    }

    /**
     * 计算文件的 SHA-256 校验值.
     *
     * @param file 目标文件
     * @return 小写十六进制的 SHA-256 校验值
     * @throws IOException 读取文件失败时抛出
     */
    public static String sha256(Path file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream in = Files.newInputStream(file)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    digest.update(buffer, 0, len);
                }
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("SHA-256 algorithm not available", e);
        }
    }

    private static void closeQuietly(InputStream in) {
        if (in == null) {
            return;
        }
        try {
            in.close();
        } catch (IOException ignored) {
            // ignore close failure on the error path
        }
    }
}
