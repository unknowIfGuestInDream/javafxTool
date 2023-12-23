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
import com.tlcsdm.cg.CgSample;
import com.tlcsdm.cg.util.CgConstant;
import com.tlcsdm.cg.util.I18nUtils;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.frame.service.VersionCheckerService;

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
        result = getReleaseResult(CgConstant.PROJECT_VERSION_CHECK_URL, "");
        if (result.isEmpty()) {
            return;
        }
        var list = parseReleaseResult(CgConstant.PROJECT_VERSION_CHECK_URL, result);
        for (var map : list) {
            boolean isPrerelease = (boolean) map.get("isPrerelease");
            if (!isPrerelease) {
                String tag = String.valueOf(map.get("tagName"));
                if (tag.endsWith(CgConstant.PROJECT_TAG_SUBFIX)) {
                    String version = tag.substring(1, tag.length() - CgConstant.PROJECT_TAG_SUBFIX.length());
                    int compare = VersionComparator.INSTANCE.compare(version, CgSample.PROJECT_INFO.getVersion());
                    if (compare > 0) {
                        String content = new StringBuilder().append(I18nUtils.get("cg.versionCheck.versionNum"))
                            .append(": ").append(version).append("\r\n")
                            .append(I18nUtils.get("cg.versionCheck.body")).append(": \n").append(map.get("body"))
                            .append("\r\n").append("\r\n").append(I18nUtils.get("cg.versionCheck.desc"))
                            .append("\r\n").append(I18nUtils.get("cg.versionCheck.desc.other")).append("\n")
                            .toString();
                        CgConstant.PROJECT_RELEASE_URL = String.valueOf(map.get("releaseUrl"));
                        FxApp.runLater(() -> FxNotifications.defaultNotify().title(I18nUtils.get("cg.versionCheck.title"))
                            .graphic(LayoutHelper.iconView(
                                LayoutHelper.class.getResource("/com/tlcsdm/core/static/icon/release.png"),
                                48.0D))
                            .text(content).show());
                    }
                    break;
                }
            }
        }
    }
}
