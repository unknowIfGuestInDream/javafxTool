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

package com.tlcsdm.smc.tool.tool;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import com.tlcsdm.core.exception.UnExpectedResultException;

import cn.hutool.core.net.SSLContextBuilder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

/**
 * github api 测试
 * 用于检查更新功能
 *
 * @author os_tangliang
 */
public class GithubApiTest {

    static volatile String result = "";

    @Test
    public void release() {
        HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.NORMAL)
                .sslContext(SSLContextBuilder.create().build()).connectTimeout(Duration.ofMillis(1000)).build();

        HttpRequest request = HttpRequest
                .newBuilder(URI.create("https://api.github.com/repos/unknowIfGuestInDream/javafxTool/releases")).GET()
                .headers("Content-Type", "application/json", "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50",
                        "accept", "application/vnd.github+json")
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

        JSONArray array = JSONUtil.parseArray(result);
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.getByPath("[" + i + "].draft"));
            System.out.println(array.getByPath("[" + i + "].prerelease"));
            System.out.println(array.getByPath("[" + i + "].tag_name"));
            System.out.println(array.getByPath("[" + i + "].name"));
            System.out.println(array.getByPath("[" + i + "].html_url"));
            JSONArray assets = JSONUtil.parseArray(array.getByPath("[" + i + "].assets"));
            System.out.println(assets.size());
            System.out.println(assets.getByPath("[0].name"));
            System.out.println(array.getByPath("[" + i + "].assets[0].name"));
            System.out.println(array.getByPath("[" + i + "].assets[0].browser_download_url"));
        }
    }

}
