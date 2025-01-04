/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.util;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.net.SSLContextBuilder;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.tlcsdm.core.exception.UnExpectedResultException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

/**
 * gitlab api 测试 用于检查更新功能.
 *
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class GitlabApiTest {

    private String gitlabWeb = "http://scgitlab.rdb.renesas.com:8080/";
    private String token = "";
    private String result = "";
    private final String aesKey = "1gd0v4s3525ssfzm3e4f0a0183d85ssx";
    private final String encryptStr = "e0b0b409e5f1393ea65f67d0f41d12e912953d7b8bd9143806808264ce4163d3";

    @Test
    void release() {
        HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.NORMAL)
            .sslContext(SSLContextBuilder.create().build()).connectTimeout(Duration.ofMillis(1000)).build();
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, aesKey.getBytes());
        token = aes.decryptStr(encryptStr);
        HttpRequest request = HttpRequest.newBuilder(URI.create(gitlabWeb + "api/v4/projects/10/releases")).GET()
            .headers("Content-Type", "application/json", "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50",
                "PRIVATE-TOKEN", token)
            .build();
        var future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
            if (response.statusCode() != 200) {
                throw new UnExpectedResultException(response.body());
            }
            return response.body();
        }).thenAccept(body -> {
            result = body;
        });
        try {
            future.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException | UnExpectedResultException e) {
            e.printStackTrace();
        }
        var list = JacksonUtil.json2List(result, Map.class);
        if (list == null || list.isEmpty()) {
            return;
        }
        for (var map : list) {
            boolean isPrerelease = (boolean) map.get("upcoming_release");
            if (!isPrerelease) {
                String tag = String.valueOf(map.get("tag_name"));
                if (tag.endsWith("-smc")) {
                    String version = tag.substring(1, tag.length() - 4);
                    int compare = VersionComparator.INSTANCE.compare(version, "1.0.8");
                    if (compare > 0) {
                        String content = new StringBuilder().append("Version Number: ").append(": ").append(version)
                            .append("\r\n").append("body:").append(": \n").append(map.get("description"))
                            .append("\r\n").toString();
                        System.out.println(content);
                        // http://scgitlab.rdb.renesas.com:8080/liangtang/javafxTool/-/releases/v1.0.0-qe
                        var links = (Map<String, String>) map.get("_links");
                        System.out.println(links.get("self"));
                        // map.get("_links").get("self")
                        // SmcConstant.PROJECT_RELEASE_URL = String.valueOf(map.get("html_url"));
                    }
                    break;
                }
            }
        }
    }

}
