package com.tlcsdm.core.util;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.net.SSLContextBuilder;
import com.tlcsdm.core.exception.UnExpectedResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

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
                        String content = new StringBuilder().append("Version Number: ")
                            .append(": ").append(version).append("\r\n").append("body:")
                            .append(": \n").append(map.get("description")).append("\r\n").toString();
                        System.out.println(content);
                        //http://scgitlab.rdb.renesas.com:8080/liangtang/javafxTool/-/releases/v1.0.0-qe
                        //map.get("_links").get("self")
                        //SmcConstant.PROJECT_RELEASE_URL = String.valueOf(map.get("html_url"));
                    }
                    break;
                }
            }
        }
    }

}
