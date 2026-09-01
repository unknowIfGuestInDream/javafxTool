/*
 * Copyright (c) 2026 unknowIfGuestInDream.
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

package com.tlcsdm.autoupdate;

import com.tlcsdm.autoupdate.download.AutoUpdateDownloader;
import com.tlcsdm.autoupdate.install.UpdateApplier;
import com.tlcsdm.autoupdate.model.UpdateInfo;
import com.tlcsdm.autoupdate.util.I18nUtils;
import com.tlcsdm.core.factory.config.ThreadPoolTaskExecutor;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 自动更新的门面类, 应用模块只需要构造 {@link UpdateInfo} 并调用
 * {@link #promptAndUpdate(UpdateInfo)} 即可完成 "确认 - 下载 - 定位/安装" 的完整流程.
 *
 * <p>该类是集成自动更新能力的唯一入口, 所有 UI 交互都会切换到 JavaFX 应用线程执行,
 * 下载动作则在后台线程池中完成, 不会阻塞界面.</p>
 *
 * @author unknowIfGuestInDream
 */
public final class AutoUpdate {

    private AutoUpdate() {
    }

    /**
     * 判断 {@code latest} 版本是否比 {@code current} 版本更新.
     *
     * @param latest  最新版本号, 例如 {@code 1.2.0}
     * @param current 当前版本号, 例如 {@code 1.1.0}
     * @return 当 {@code latest} 严格大于 {@code current} 时返回 {@code true}
     */
    public static boolean isNewer(final String latest, final String current) {
        return compareVersion(latest, current) > 0;
    }

    /**
     * 使用默认配置执行自动更新流程.
     *
     * @param info 更新信息
     */
    public static void promptAndUpdate(final UpdateInfo info) {
        promptAndUpdate(info, AutoUpdateOptions.defaults());
    }

    /**
     * 执行自动更新流程: 弹窗确认 -&gt; 后台下载(带进度) -&gt; 校验 -&gt; 定位或应用更新.
     *
     * @param info    更新信息
     * @param options 行为配置
     */
    public static void promptAndUpdate(final UpdateInfo info, final AutoUpdateOptions options) {
        if (info == null) {
            return;
        }
        AutoUpdateOptions opts = options == null ? AutoUpdateOptions.defaults() : options;
        boolean confirmed = FxAlerts.confirmYesNo(I18nUtils.get("autoupdate.confirm.title"),
            I18nUtils.get("autoupdate.confirm.message", info.getVersion(), info.getCurrentVersion()));
        if (!confirmed) {
            return;
        }
        ProgressHolder holder = showProgress(info);
        ThreadPoolTaskExecutor.get().execute(() -> runDownload(info, opts, holder));
    }

    private static void runDownload(final UpdateInfo info, final AutoUpdateOptions opts, final ProgressHolder holder) {
        AutoUpdateDownloader downloader = new AutoUpdateDownloader();
        try {
            Path file = downloader.download(info, opts.getTargetDir(),
                (bytesRead, totalBytes) -> FxApp.runLater(() -> updateProgress(holder, bytesRead, totalBytes)));
            FxApp.runLater(() -> onDownloaded(file, opts));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            FxApp.runLater(() -> onDownloadFailed(holder));
        } catch (IOException e) {
            FxApp.runLater(() -> onDownloadFailed(holder));
        }
    }

    private static void onDownloadFailed(final ProgressHolder holder) {
        closeQuietly(holder);
        FxAlerts.error(I18nUtils.get("autoupdate.failed.title"), I18nUtils.get("autoupdate.failed.message"));
    }

    private static void onDownloaded(final Path file, final AutoUpdateOptions opts) {
        UpdateApplier applier = new UpdateApplier();
        if (opts.isAutoApply() && opts.getInstallDir() != null) {
            boolean restart = FxAlerts.confirmYesNo(I18nUtils.get("autoupdate.restart.title"),
                I18nUtils.get("autoupdate.restart.message"));
            if (restart) {
                try {
                    applier.applyAndRestart(file, opts.getInstallDir(), opts.getRelaunchCommand());
                    return;
                } catch (IOException e) {
                    FxAlerts.error(I18nUtils.get("autoupdate.failed.title"),
                        I18nUtils.get("autoupdate.failed.message"));
                }
            }
        }
        FxAlerts.info(I18nUtils.get("autoupdate.ready.title"),
            I18nUtils.get("autoupdate.ready.message", file.toString()));
        applier.reveal(file);
    }

    private static ProgressHolder showProgress(final UpdateInfo info) {
        ProgressHolder holder = new ProgressHolder();
        Stage stage = new Stage();
        if (FxApp.primaryStage != null) {
            stage.initOwner(FxApp.primaryStage);
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(I18nUtils.get("autoupdate.progress.title"));
        Label label = new Label(I18nUtils.get("autoupdate.progress.message", info.getFileName()));
        ProgressBar bar = new ProgressBar();
        bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        bar.setPrefWidth(320);
        VBox box = new VBox(12, label, bar);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(20));
        stage.setScene(new Scene(box));
        FxApp.setupIcon(stage);
        holder.stage = stage;
        holder.bar = bar;
        holder.label = label;
        stage.show();
        return holder;
    }

    private static void updateProgress(final ProgressHolder holder, final long bytesRead, final long totalBytes) {
        if (holder.bar == null) {
            return;
        }
        if (totalBytes > 0) {
            double progress = (double) bytesRead / (double) totalBytes;
            holder.bar.setProgress(progress);
            long percent = Math.round(progress * 100);
            holder.label.setText(I18nUtils.get("autoupdate.progress.percent", percent));
            if (bytesRead >= totalBytes) {
                closeQuietly(holder);
            }
        } else {
            holder.bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        }
    }

    private static void closeQuietly(final ProgressHolder holder) {
        if (holder != null && holder.stage != null) {
            holder.stage.close();
        }
    }

    /**
     * 比较两个点分版本号.
     *
     * <p>仅比较数字段, 例如 {@code 1.10.0} 大于 {@code 1.9.0}. 非数字后缀(如
     * {@code -beta}) 会被忽略, 调用方应在传入前过滤预发布版本.</p>
     *
     * @param v1 版本号 1
     * @param v2 版本号 2
     * @return 正数表示 v1 更大, 负数表示 v2 更大, 0 表示相等
     */
    static int compareVersion(final String v1, final String v2) {
        String left = v1 == null ? "" : v1.trim();
        String right = v2 == null ? "" : v2.trim();
        if (left.startsWith("v") || left.startsWith("V")) {
            left = left.substring(1);
        }
        if (right.startsWith("v") || right.startsWith("V")) {
            right = right.substring(1);
        }
        String[] a = left.split("\\.");
        String[] b = right.split("\\.");
        int len = Math.max(a.length, b.length);
        for (int i = 0; i < len; i++) {
            int x = i < a.length ? parseSegment(a[i]) : 0;
            int y = i < b.length ? parseSegment(b[i]) : 0;
            if (x != y) {
                return Integer.compare(x, y);
            }
        }
        return 0;
    }

    private static int parseSegment(final String segment) {
        int end = 0;
        while (end < segment.length() && Character.isDigit(segment.charAt(end))) {
            end++;
        }
        if (end == 0) {
            return 0;
        }
        try {
            return Integer.parseInt(segment.substring(0, end));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 进度对话框内部持有的组件引用.
     */
    private static final class ProgressHolder {
        private Stage stage;
        private ProgressBar bar;
        private Label label;
    }
}
