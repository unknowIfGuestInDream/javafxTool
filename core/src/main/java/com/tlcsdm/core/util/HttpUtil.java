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

import cn.hutool.core.net.SSLContextBuilder;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.CoreException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author unknowIfGuestInDream
 */
public class HttpUtil {

    /**
     * get请求.
     */
    public static String doGet(String url, Map<String, String> header) {
        var builder = HttpRequest.newBuilder().uri(URI.create(url)).GET();
        buildHeader(header, builder);
        return execute(builder, StandardCharsets.UTF_8);
    }

    /**
     * post请求.
     */
    public static String doPost(String url, Map<String, String> header, String body) {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8);
        var builder = HttpRequest.newBuilder().uri(URI.create(url)).POST(bodyPublisher);
        buildHeader(header, builder);
        return execute(builder, StandardCharsets.UTF_8);
    }

    /**
     * form表单.
     */
    public static String doPostForm(String url, Map<String, String> header, Map<String, String> pd, Charset charset) {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        StringBuilder sb = new StringBuilder();
        if (pd != null) {
            pd.forEach((k, v) -> {
                sb.append(k);
                sb.append("=");
                sb.append(v);
                sb.append("&");
            });
        }
        sb.append("tmp=tmp");
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(sb.toString(), charset);
        var builder = HttpRequest.newBuilder().uri(URI.create(url)).POST(bodyPublisher);
        buildHeader(header, builder);
        builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
        return execute(builder, charset);
    }

    private static void buildHeader(Map<String, String> header, HttpRequest.Builder builder) {
        if (header != null && !header.isEmpty()) {
            for (String key : header.keySet()) {
                builder.setHeader(key, header.get(key));
            }
        }
        builder.setHeader("Content-Type", "application/json");
        builder.setHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50");
    }

    private static String execute(HttpRequest.Builder builder, Charset charset) {
        var request = builder.build();
        try {
            var client = HttpClient.newBuilder().sslContext(SSLContextBuilder.create().build()).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(charset));
            String result;
            if (response.statusCode() == 200) {
                result = response.body();
            } else {
                throw new CoreException("The request failed");
            }
            return result;
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return null;
    }

}
