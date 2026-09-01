/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

package com.tlcsdm.frame.service;

import cn.hutool.core.net.SSLContextBuilder;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.util.JacksonUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 检查更新(支持github和gitlab).
 *
 * @author unknowIfGuestInDream
 */
public interface VersionCheckerService {

    /**
     * 检查更新
     */
    void checkNewVersion();

    /**
     * aes加密密钥.
     */
    default String getAesKey() {
        return "1gd0v4s3525ssfzm3e4f0a0183d85ssx";
    }

    /**
     * 获取releases结果.
     *
     * @param url        releases接口地址
     * @param encryptStr 加密token密文
     */
    default String getReleaseResult(String url, String encryptStr) {
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL).sslContext(SSLContextBuilder.create().build())
            .connectTimeout(Duration.ofMillis(2000)).build();
        HttpRequest request;
        if (isGithub(url)) {
            request = HttpRequest.newBuilder(URI.create(url)).GET().headers("Content-Type", "application/json",
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50",
                "accept", "application/vnd.github+json").build();
        } else {
            String aesKey = getAesKey();
            AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, aesKey.getBytes());
            String token = aes.decryptStr(encryptStr);
            request = HttpRequest.newBuilder(URI.create(url)).GET().headers("Content-Type", "application/json",
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50",
                "PRIVATE-TOKEN", token).build();
        }
        var future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
            if (response.statusCode() != 200) {
                throw new UnExpectedResultException(response.body());
            }
            return response.body();
        });
        try {
            return future.get(3, TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException | UnExpectedResultException e) {
            StaticLog.error("Failed to check for updates.");
        } catch (InterruptedException e) {
            StaticLog.error(e);
            Thread.currentThread().interrupt();
        }
        return "";
    }

    /**
     * 解析release结果, 获取需要的数据.
     */
    default List<Map<String, Object>> parseReleaseResult(String url, String result) {
        List<Map<String, Object>> releaseList = new ArrayList<>();
        var list = JacksonUtil.json2List(result, Map.class);
        if (list == null || list.isEmpty()) {
            return releaseList;
        }
        for (var map : list) {
            Map<String, Object> releaseMap = new HashMap<>();
            if (isGithub(url)) {
                boolean isDraft = (boolean) map.get("draft");
                boolean isPrerelease = (boolean) map.get("prerelease");
                releaseMap.put("isPrerelease", isDraft || isPrerelease);
                releaseMap.put("tagName", map.get("tag_name"));
                releaseMap.put("body", map.get("body"));
                releaseMap.put("releaseUrl", map.get("html_url"));
            } else {
                releaseMap.put("isPrerelease", map.get("upcoming_release"));
                releaseMap.put("tagName", map.get("tag_name"));
                releaseMap.put("body", map.get("description"));
                releaseMap.put("releaseUrl", ((Map<String, Object>) map.get("_links")).get("self"));
            }
            releaseMap.put("assets", parseReleaseAssets(url, map));
            releaseList.add(releaseMap);
        }
        return releaseList;
    }

    /**
     * 解析release中的附件资源, 供自动更新等场景使用.
     *
     * <p>每个附件包含 {@code name}(文件名)、{@code downloadUrl}(下载地址)、
     * {@code size}(字节大小)以及可选的 {@code sha256}(校验和)。</p>
     *
     * @param url release接口地址
     * @param map 单条release的原始数据
     * @return 附件列表, 无附件时返回空列表
     */
    default List<Map<String, Object>> parseReleaseAssets(String url, Map<String, Object> map) {
        List<Map<String, Object>> assetList = new ArrayList<>();
        if (isGithub(url)) {
            Object assets = map.get("assets");
            if (assets instanceof List<?> list) {
                for (Object item : list) {
                    if (!(item instanceof Map<?, ?> asset)) {
                        continue;
                    }
                    Map<String, Object> assetMap = new HashMap<>();
                    assetMap.put("name", asset.get("name"));
                    assetMap.put("downloadUrl", asset.get("browser_download_url"));
                    assetMap.put("size", asset.get("size"));
                    Object digest = asset.get("digest");
                    if (digest != null) {
                        String digestStr = String.valueOf(digest);
                        assetMap.put("sha256",
                            digestStr.startsWith("sha256:") ? digestStr.substring("sha256:".length()) : digestStr);
                    }
                    assetList.add(assetMap);
                }
            }
        } else if (map.get("assets") instanceof Map<?, ?> assets && assets.get("links") instanceof List<?> links) {
            for (Object item : links) {
                if (!(item instanceof Map<?, ?> link)) {
                    continue;
                }
                Map<String, Object> assetMap = new HashMap<>();
                assetMap.put("name", link.get("name"));
                Object direct = link.get("direct_asset_url");
                assetMap.put("downloadUrl", direct != null ? direct : link.get("url"));
                assetList.add(assetMap);
            }
        }
        return assetList;
    }

    /**
     * 根据接口地址判断是否是github.
     */
    default boolean isGithub(String url) {
        return url.contains("github.com");
    }
}
