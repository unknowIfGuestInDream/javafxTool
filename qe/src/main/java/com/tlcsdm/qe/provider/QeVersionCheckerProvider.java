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

package com.tlcsdm.qe.provider;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.net.SSLContextBuilder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.frame.service.VersionCheckerService;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import com.tlcsdm.qe.util.QeConstant;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author unknowIfGuestInDream
 * @date 2023/3/31 21:06
 */
public class QeVersionCheckerProvider implements VersionCheckerService {

    private static volatile String result = "";

    @Override
    public void checkNewVersion() {
        if (result.length() > 0) {
            return;
        }
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL).sslContext(SSLContextBuilder.create().build())
            .connectTimeout(Duration.ofMillis(2000)).build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(QeConstant.PROJECT_VERSION_CHECK_URL)).GET().headers(
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
        JSONArray array = JSONUtil.parseArray(result);
        for (int i = 0; i < array.size(); i++) {
            boolean isDraft = (boolean) array.getByPath("[" + i + "].draft");
            boolean isPrerelease = (boolean) array.getByPath("[" + i + "].prerelease");
            if (!isDraft && !isPrerelease) {
                String tag = String.valueOf(array.getByPath("[" + i + "].tag_name"));
                if (tag.endsWith(QeConstant.PROJECT_TAG_SUBFIX)) {
                    String version = tag.substring(1, tag.length() - QeConstant.PROJECT_TAG_SUBFIX.length());
                    int compare = VersionComparator.INSTANCE.compare(version, QeSample.PROJECT_INFO.getVersion());
                    if (compare > 0) {
                        String content = new StringBuilder().append(I18nUtils.get("qe.versionCheck.versionNum"))
                            .append(": ").append(version).append("\r\n").append(I18nUtils.get("qe.versionCheck.body"))
                            .append(": \n").append(array.getByPath("[" + i + "].body")).append("\r\n").append("\r\n")
                            .append(I18nUtils.get("qe.versionCheck.desc")).append("\r\n")
                            .append(I18nUtils.get("qe.versionCheck.desc.other")).append("\n").toString();

                        QeConstant.PROJECT_RELEASE_URL = String.valueOf(array.getByPath("[" + i + "].html_url"));
                        FxApp.runLater(() -> {
                            FxNotifications.defaultNotify().title(I18nUtils.get("qe.versionCheck.title"))
                                .graphic(LayoutHelper
                                    .iconView(getClass().getResource("/com/tlcsdm/qe/static/icon/release.png"), 48.0D))
                                .text(content).show();
                        });
                    }
                    break;
                }
            }
        }
    }
}
