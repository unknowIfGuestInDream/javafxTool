/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.javafx.service;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.factory.config.ThreadPoolTaskExecutor;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.controller.PathWatchToolController;
import com.tlcsdm.core.javafx.dialog.ExceptionDialog;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.util.I18nUtils;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Folder monitoring tool
 *
 * @author unknowIfGuestInDream
 */
public class PathWatchToolService {
    private final PathWatchToolController pathWatchToolController;
    private FileAlterationMonitor monitor;
    private final Notifications infoNotify = FxNotifications.notifications(Duration.seconds(20), Pos.BOTTOM_RIGHT);
    private final Notifications warningNotify = FxNotifications.defaultNotify();

    public PathWatchToolService(PathWatchToolController pathWatchToolController) {
        this.pathWatchToolController = pathWatchToolController;
    }

    public boolean watchAction() {
        String watchPath = pathWatchToolController.getWatchPathTextField().getText();
        if (StringUtils.isEmpty(watchPath)) {
            warningNotify.text(I18nUtils.get("core.menubar.setting.pathWatch.message.empty"));
            warningNotify.showWarning();
            return false;
        }
        Path path = Paths.get(watchPath);
        if (!Files.exists(path)) {
            warningNotify.text(I18nUtils.get("core.menubar.setting.pathWatch.message.exist"));
            warningNotify.showWarning();
            return false;
        } else if (!Files.isDirectory(path)) {
            warningNotify.text(I18nUtils.get("core.menubar.setting.pathWatch.message.directory"));
            warningNotify.showWarning();
            return false;
        }

        boolean fileNameSRegex = pathWatchToolController.getFileNameSupportRegexCheckBox().isSelected();
        String fileNameContains = pathWatchToolController.getFileNameContainsTextField().getText();
        String fileNameNotContains = pathWatchToolController.getFileNameNotContainsTextField().getText();
        Pattern fileNameCsPattern = Pattern.compile(fileNameContains, Pattern.CASE_INSENSITIVE);
        Pattern fileNameNCsPattern = Pattern.compile(fileNameNotContains, Pattern.CASE_INSENSITIVE);

        String folderPathCsText = pathWatchToolController.getFolderPathContainsTextField().getText();
        String folderPathNCsText = pathWatchToolController.getFolderPathNotContainsTextField().getText();
        boolean folderPathSRegex = pathWatchToolController.getFolderPathSupportRegexCheckBox().isSelected();
        Pattern folderPathCsPattern = Pattern.compile(folderPathCsText, Pattern.CASE_INSENSITIVE);
        Pattern folderPathNCsPattern = Pattern.compile(folderPathNCsText, Pattern.CASE_INSENSITIVE);

        FileAlterationObserver observer = new FileAlterationObserver(new File(watchPath), pathname -> {
            Path filepath = pathname.toPath();
            if (Files.isDirectory(filepath) && (!ifMatchText(filepath.getFileName().toString(), folderPathCsText,
                folderPathNCsText, folderPathSRegex, folderPathCsPattern, folderPathNCsPattern))) {
                return false;
            } else if (Files.isRegularFile(filepath) && (!ifMatchText(filepath.getFileName().toString(),
                fileNameContains, fileNameNotContains, fileNameSRegex, fileNameCsPattern, fileNameNCsPattern))) {
                return false;
            } else {
                return true;
            }
        });
        observer.addListener(new FileAlterationListenerAdaptor() {

            @Override
            public void onDirectoryCreate(File directory) {
                showMonitorInfo(I18nUtils.get("core.menubar.setting.pathWatch.message.directoryCreate"), directory);
            }

            @Override
            public void onDirectoryChange(File directory) {
                showMonitorInfo(I18nUtils.get("core.menubar.setting.pathWatch.message.directoryChange"), directory);
            }

            @Override
            public void onDirectoryDelete(File directory) {
                showMonitorInfo(I18nUtils.get("core.menubar.setting.pathWatch.message.directoryDelete"), directory);
            }

            @Override
            public void onFileCreate(File file) {
                showMonitorInfo(I18nUtils.get("core.menubar.setting.pathWatch.message.fileCreate"), file);
            }

            @Override
            public void onFileChange(File file) {
                showMonitorInfo(I18nUtils.get("core.menubar.setting.pathWatch.message.fileChange"), file);
            }

            @Override
            public void onFileDelete(File file) {
                showMonitorInfo(I18nUtils.get("core.menubar.setting.pathWatch.message.fileDelete"), file);
            }
        });

        monitor = new FileAlterationMonitor(10_000, observer);
        monitor
            .setThreadFactory(ThreadPoolTaskExecutor.hasInitialized() ? ThreadPoolTaskExecutor.get().getThreadFactory()
                : new BasicThreadFactory.Builder().namingPattern("pathWatch").daemon(true)
                .uncaughtExceptionHandler((t, e) -> StaticLog.error(e)).build());
        try {
            monitor.start();
        } catch (Exception e) {
            ExceptionDialog exceptionDialog = new ExceptionDialog(e);
            exceptionDialog.show();
        }
        return true;
    }

    public void stopWatchAction() {
        try {
            monitor.stop();
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    /**
     * Monitoring information output
     */
    private void showMonitorInfo(String message, File file) {
        StringBuilder buffer = new StringBuilder(
            "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "]");
        buffer.append(" " + message + " ");
        buffer.append(file.getAbsolutePath() + "\n");
        pathWatchToolController.getWatchLogTextArea().appendText(buffer.toString());
        if (pathWatchToolController.getIsShowNotificationCheckBox().isSelected()) {
            FxApp.runLater(() -> {
                infoNotify.title(I18nUtils.get("core.menubar.setting.pathWatch.title.infoNotify"));
                infoNotify.text(message + "\n" + file.getAbsolutePath());
                infoNotify.showInformation();
            });
        }
    }

    /**
     * File or folder regular match
     */
    private boolean ifMatchText(String fileName, String csText, String ncsText, boolean sRegex, Pattern csPattern,
                                Pattern ncsPattern) {
        boolean match = true;
        String lFileName = fileName.toLowerCase();
        String lcsText = csText.toLowerCase();
        String lncsText = ncsText.toLowerCase();
        if (sRegex) {
            if (!csText.isEmpty()) {
                Matcher m = csPattern.matcher(fileName);
                match = m.find();
            }
            if (match && !ncsText.isEmpty()) {
                Matcher m = ncsPattern.matcher(fileName);
                match = !m.find();
            }
        } else {
            if (!csText.isEmpty()) {
                match = lFileName.contains(lcsText);
            }
            if (match && !ncsText.isEmpty()) {
                match = !lFileName.contains(lncsText);
            }
        }
        return match;
    }

}
