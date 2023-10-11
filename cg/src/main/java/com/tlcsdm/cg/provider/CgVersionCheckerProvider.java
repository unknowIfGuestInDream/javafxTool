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

package com.tlcsdm.cg.provider;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.net.SSLContextBuilder;
import cn.hutool.log.StaticLog;
import com.tlcsdm.cg.CgSample;
import com.tlcsdm.cg.util.CgConstant;
import com.tlcsdm.cg.util.I18nUtils;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.util.JacksonUtil;
import com.tlcsdm.frame.service.VersionCheckerService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author unknowIfGuestInDream
 */
public class CgVersionCheckerProvider implements VersionCheckerService {

    private static volatile String result = "";

    @Override
    public void checkNewVersion() {
        if (!result.isEmpty()) {
            return;
        }
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL).sslContext(SSLContextBuilder.create().build())
            .connectTimeout(Duration.ofMillis(2000)).build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(CgConstant.PROJECT_VERSION_CHECK_URL)).GET().headers(
            "Content-Type", "application/json", "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50",
            "accept", "application/vnd.github+json").build();
        var future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
            if (response.statusCode() != 200) {
                throw new UnExpectedResultException(response.body());
            }
            return response.body();
        }).thenAccept(body -> result = body);
        try {
            future.get(3, TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException | UnExpectedResultException e) {
            StaticLog.error("Failed to check for updates.");
            return;
        } catch (InterruptedException e) {
            StaticLog.error(e);
            Thread.currentThread().interrupt();
        }

        var list = JacksonUtil.json2List(result, Map.class);
        if (list == null || list.isEmpty()) {
            return;
        }
        for (var map : list) {
            boolean isDraft = (boolean) map.get("draft");
            boolean isPrerelease = (boolean) map.get("prerelease");
            if (!isDraft && !isPrerelease) {
                String tag = String.valueOf(map.get("tag_name"));
                if (tag.endsWith(CgConstant.PROJECT_TAG_SUBFIX)) {
                    String version = tag.substring(1, tag.length() - CgConstant.PROJECT_TAG_SUBFIX.length());
                    int compare = VersionComparator.INSTANCE.compare(version, CgSample.PROJECT_INFO.getVersion());
                    if (compare > 0) {
                        String content = new StringBuilder().append(I18nUtils.get("cg.versionCheck.versionNum"))
                            .append(": ").append(version).append("\r\n").append(I18nUtils.get("cg.versionCheck.body"))
                            .append(": \n").append(map.get("body")).append("\r\n").append("\r\n")
                            .append(I18nUtils.get("cg.versionCheck.desc")).append("\r\n")
                            .append(I18nUtils.get("cg.versionCheck.desc.other")).append("\n").toString();
                        CgConstant.PROJECT_RELEASE_URL = String.valueOf(map.get("html_url"));
                        FxApp.runLater(() -> {
                            FxNotifications.defaultNotify().title(I18nUtils.get("cg.versionCheck.title"))
                                .graphic(LayoutHelper
                                    .iconView(getClass().getResource("/com/tlcsdm/core/static/icon/release.png"), 48.0D))
                                .text(content).show();
                        });
                    }
                    break;
                }
            }
        }
    }
}
