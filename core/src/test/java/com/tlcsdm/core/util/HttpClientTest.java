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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlcsdm.core.exception.UnExpectedResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * HttpClient测试.
 *
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class HttpClientTest {

    @Test
    void get() {
        AtomicReference<String> result = new AtomicReference<>("");
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(
                HttpClient.Redirect.NORMAL)
            .sslContext(SSLContextBuilder.create().build()).connectTimeout(Duration.ofMillis(1000)).build();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("https://www.tlcsdm.com/#/README")).GET()
            .headers("Content-Type", "application/json", "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50")
            .build();
        var future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
            if (response.statusCode() != 200) {
                throw new UnExpectedResultException(response.body());
            }
            return response.body();
        }).thenAccept(body -> {
            result.set(body);
        });
        try {
            future.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException | UnExpectedResultException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    @Test
    void post() throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("name", "John Doe");
            put("occupation", "gardener");
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
            .writeValueAsString(values);

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(
                HttpClient.Redirect.NORMAL)
            .sslContext(SSLContextBuilder.create().build()).connectTimeout(Duration.ofMillis(3000)).build();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/post"))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();
    }

    @Test
    void put() throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("name", "John Doe");
            put("occupation", "gardener");
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
            .writeValueAsString(values);
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
            .sslContext(SSLContextBuilder.create().build()).connectTimeout(Duration.ofMillis(3000)).build();
        HttpRequest request = HttpRequest.newBuilder()
            .headers("Content-Type", "application/json")
            .uri(URI.create("http://localhost:3010/put"))
            .PUT(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
            .build();
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();
    }

    //HttpURLConnection的 Java HTTP GET 请求
    @Test
    void connGet() {
        HttpURLConnection con = null;
        var url = "http://webcode.me";
        try {
            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();
            con.setRequestMethod("GET");
            StringBuilder content;
            try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            System.out.println(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            con.disconnect();
        }
    }

    @Test
    void connPost() {
        HttpURLConnection con = null;
        var url = "https://httpbin.org/post";
        var urlParameters = "name=Jack&occupation=programmer";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {
            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }
            StringBuilder content;
            try (var br = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            System.out.println(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            con.disconnect();
        }
    }
}
