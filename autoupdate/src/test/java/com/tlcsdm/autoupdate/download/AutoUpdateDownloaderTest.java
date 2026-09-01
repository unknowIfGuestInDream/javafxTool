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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link AutoUpdateDownloader} 的单元测试, 使用本地回环 HTTP 服务模拟下载.
 *
 * @author unknowIfGuestInDream
 */
public class AutoUpdateDownloaderTest {

    @Test
    public void downloadsFileAndReportsProgress(@TempDir Path dir) throws Exception {
        byte[] body = "javafxTool auto update payload".getBytes(StandardCharsets.UTF_8);
        try (TinyHttpServer server = new TinyHttpServer(200, body)) {
            UpdateInfo info = UpdateInfo.builder().downloadUrl(server.baseUrl() + "/app.zip").build();
            List<long[]> events = new ArrayList<>();
            Path file = newDownloader().download(info, dir, (read, total) -> events.add(new long[] {read, total}));

            assertTrue(Files.exists(file));
            assertEquals("app.zip", file.getFileName().toString());
            assertArrayEquals(body, Files.readAllBytes(file));
            assertFalse(events.isEmpty());
            long[] last = events.get(events.size() - 1);
            assertEquals(body.length, last[0]);
            assertEquals(body.length, last[1]);
            long previous = -1;
            for (long[] event : events) {
                assertTrue(event[0] >= previous);
                previous = event[0];
            }
            assertFalse(Files.exists(dir.resolve("app.zip.part")));
        }
    }

    @Test
    public void verifiesChecksumAndCleansUpOnMismatch(@TempDir Path dir) throws Exception {
        byte[] body = "checksum me".getBytes(StandardCharsets.UTF_8);
        try (TinyHttpServer server = new TinyHttpServer(200, body)) {
            UpdateInfo good = UpdateInfo.builder().downloadUrl(server.baseUrl() + "/ok.zip")
                .sha256(sha256Hex(body)).build();
            assertTrue(Files.exists(newDownloader().download(good, dir, null)));

            UpdateInfo bad = UpdateInfo.builder().downloadUrl(server.baseUrl() + "/bad.zip")
                .sha256("00deadbeef").build();
            assertThrows(IOException.class, () -> newDownloader().download(bad, dir, null));
            assertFalse(Files.exists(dir.resolve("bad.zip")));
            assertFalse(Files.exists(dir.resolve("bad.zip.part")));
        }
    }

    @Test
    public void nonOkStatusThrows(@TempDir Path dir) throws Exception {
        try (TinyHttpServer server = new TinyHttpServer(404, "nope".getBytes(StandardCharsets.UTF_8))) {
            UpdateInfo info = UpdateInfo.builder().downloadUrl(server.baseUrl() + "/missing.zip").build();
            assertThrows(IOException.class, () -> newDownloader().download(info, dir, null));
            assertFalse(Files.exists(dir.resolve("missing.zip")));
        }
    }

    @Test
    public void sha256MatchesKnownValue(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("hello.txt");
        Files.writeString(file, "hello");
        assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824",
            AutoUpdateDownloader.sha256(file));
    }

    private static AutoUpdateDownloader newDownloader() {
        return new AutoUpdateDownloader(
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build(), Duration.ofSeconds(30));
    }

    private static String sha256Hex(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return HexFormat.of().formatHex(digest.digest(data));
    }

    /**
     * 极简的 HTTP/1.1 回环服务, 对任意请求返回固定状态码与响应体.
     */
    private static final class TinyHttpServer implements AutoCloseable {

        private final ServerSocket serverSocket;
        private volatile boolean running = true;

        TinyHttpServer(int status, byte[] body) throws IOException {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 0));
            Thread thread = new Thread(() -> serveLoop(status, body), "tiny-http");
            thread.setDaemon(true);
            thread.start();
        }

        String baseUrl() {
            return "http://127.0.0.1:" + serverSocket.getLocalPort();
        }

        private void serveLoop(int status, byte[] body) {
            while (running) {
                try (Socket socket = serverSocket.accept()) {
                    consumeRequest(socket.getInputStream());
                    writeResponse(socket.getOutputStream(), status, body);
                } catch (IOException e) {
                    return;
                }
            }
        }

        private static void consumeRequest(InputStream in) throws IOException {
            int state = 0;
            int read;
            while ((read = in.read()) != -1) {
                if ((state == 0 || state == 2) && read == '\r') {
                    state++;
                } else if ((state == 1 || state == 3) && read == '\n') {
                    state++;
                } else {
                    state = 0;
                }
                if (state == 4) {
                    return;
                }
            }
        }

        private static void writeResponse(OutputStream out, int status, byte[] body) throws IOException {
            String reason = status == 200 ? "OK" : "Not Found";
            String header = "HTTP/1.1 " + status + " " + reason + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "Connection: close\r\n\r\n";
            out.write(header.getBytes(StandardCharsets.US_ASCII));
            out.write(body);
            out.flush();
        }

        @Override
        public void close() throws IOException {
            running = false;
            serverSocket.close();
        }
    }
}
