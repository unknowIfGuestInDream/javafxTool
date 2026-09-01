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

package com.tlcsdm.smc.provider;

import cn.hutool.core.comparator.VersionComparator;
import com.tlcsdm.autoupdate.AutoUpdate;
import com.tlcsdm.autoupdate.model.UpdateInfo;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.frame.service.VersionCheckerService;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import com.tlcsdm.smc.util.SmcConstant;
import org.controlsfx.control.Notifications;

import java.util.List;
import java.util.Map;

/**
 * @author unknowIfGuestInDream
 * @date 2023/3/31 21:06
 */
public class SmcVersionCheckerProvider implements VersionCheckerService {

    private static volatile String result = "";

    @Override
    public void checkNewVersion() {
        // 防止重启带来的重复检查
        if (!result.isEmpty()) {
            return;
        }
        result = getReleaseResult(SmcConstant.PROJECT_VERSION_CHECK_URL, "");
        if (result.isEmpty()) {
            return;
        }
        var list = parseReleaseResult(SmcConstant.PROJECT_VERSION_CHECK_URL, result);
        for (var map : list) {
            boolean isPrerelease = (boolean) map.get("isPrerelease");
            if (!isPrerelease) {
                String tag = String.valueOf(map.get("tagName"));
                if (tag.endsWith(SmcConstant.PROJECT_TAG_SUBFIX)) {
                    String version = tag.substring(1, tag.length() - SmcConstant.PROJECT_TAG_SUBFIX.length());
                    int compare = VersionComparator.INSTANCE.compare(version, SmcSample.PROJECT_INFO.getVersion());
                    if (compare > 0) {
                        SmcConstant.PROJECT_RELEASE_URL = String.valueOf(map.get("releaseUrl"));
                        UpdateInfo updateInfo = buildUpdateInfo(version, map);
                        notifyNewVersion(version, map, updateInfo);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 展示新版本通知. 若能定位到当前平台的安装包, 点击通知即可通过自动更新模块下载安装.
     */
    private void notifyNewVersion(String version, Map<String, Object> map, UpdateInfo updateInfo) {
        StringBuilder content = new StringBuilder().append(I18nUtils.get("smc.versionCheck.versionNum"))
            .append(": ").append(version).append("\r\n").append(I18nUtils.get("smc.versionCheck.body"))
            .append(": \n").append(map.get("body")).append("\r\n").append("\r\n")
            .append(I18nUtils.get("smc.versionCheck.desc")).append("\r\n")
            .append(I18nUtils.get("smc.versionCheck.desc.other")).append("\n");
        if (updateInfo != null) {
            content.append(I18nUtils.get("smc.versionCheck.autoUpdate")).append("\n");
        }
        String text = content.toString();
        FxApp.runLater(() -> {
            Notifications notifications = FxNotifications.defaultNotify()
                .title(I18nUtils.get("smc.versionCheck.title")).graphic(LayoutHelper.iconView(
                    LayoutHelper.class.getResource("/com/tlcsdm/core/static/icon/release.png"), 48.0D))
                .text(text);
            if (updateInfo != null) {
                notifications.onAction(e -> AutoUpdate.promptAndUpdate(updateInfo));
            }
            notifications.show();
        });
    }

    /**
     * 根据当前操作系统从release附件中挑选安装包, 构建自动更新所需的 {@link UpdateInfo}.
     *
     * @return 找到匹配的安装包时返回更新信息, 否则返回 {@code null}
     */
    private UpdateInfo buildUpdateInfo(String version, Map<String, Object> map) {
        String platform = switch (OSUtil.getOS()) {
            case WINDOWS -> "win";
            case MAC -> "mac";
            case LINUX -> "linux";
            default -> null;
        };
        if (platform == null || !(map.get("assets") instanceof List<?> assets)) {
            return null;
        }
        for (Object item : assets) {
            if (!(item instanceof Map<?, ?> asset)) {
                continue;
            }
            String name = String.valueOf(asset.get("name"));
            if (name.endsWith(".zip") && name.contains("-" + platform + "-")) {
                UpdateInfo.Builder builder = UpdateInfo.builder().version(version)
                    .currentVersion(SmcSample.PROJECT_INFO.getVersion())
                    .downloadUrl(String.valueOf(asset.get("downloadUrl"))).fileName(name)
                    .releaseNotes(String.valueOf(map.get("body"))).releaseUrl(SmcConstant.PROJECT_RELEASE_URL);
                if (asset.get("size") instanceof Number size) {
                    builder.size(size.longValue());
                }
                if (asset.get("sha256") != null) {
                    builder.sha256(String.valueOf(asset.get("sha256")));
                }
                return builder.build();
            }
        }
        return null;
    }
}
