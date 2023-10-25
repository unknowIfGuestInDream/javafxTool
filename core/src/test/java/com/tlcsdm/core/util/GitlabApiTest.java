package com.tlcsdm.core.util;

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
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import com.tlcsdm.core.exception.UnExpectedResultException;

import cn.hutool.core.net.SSLContextBuilder;

/**
 * gitlab api 测试 用于检查更新功能.
 *
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class GitlabApiTest {

    private String gitlabWeb = "";
    private String token = "";
    private String result = "";

    @Test
    void release() {
        HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.NORMAL)
                .sslContext(SSLContextBuilder.create().build()).connectTimeout(Duration.ofMillis(1000)).build();

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
        System.out.println(result);
    }

}
