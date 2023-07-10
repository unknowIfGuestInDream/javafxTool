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

package com.tlcsdm.core.javafx.service;

import com.tlcsdm.core.javafx.controller.PathWatchToolController;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件夹监控工具所用的文件监听器
 *
 * @author unknowIfGuestInDream
 */
public class PathWatchListener extends FileAlterationListenerAdaptor {

    private final PathWatchToolController pathWatchToolController;
    private final Notifications notification = FxNotifications.alwaysNotify();

    public PathWatchListener(PathWatchToolController pathWatchToolController) {
        this.pathWatchToolController = pathWatchToolController;
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
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
        System.out.println("onStart");
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("新建：" + directory.getAbsolutePath());
        System.out.println("相对路径?");
        pathWatchToolController.getWatchLogTextArea().appendText("新建：");
    }

    @Override
    public void onDirectoryChange(File directory) {
        System.out.println("修改：" + directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("删除：" + directory.getAbsolutePath());
    }

    @Override
    public void onFileCreate(File file) {
        String compressedPath = file.getAbsolutePath();
        System.out.println("新建：" + compressedPath);
        if (file.canRead()) {
            // TODO 读取或重新加载文件内容
            System.out.println("文件变更，进行处理");
        }
    }

    @Override
    public void onFileChange(File file) {
        String compressedPath = file.getAbsolutePath();
        System.out.println("修改：" + compressedPath);
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("删除：" + file.getAbsolutePath());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
        System.out.println("onStop");
    }

    private boolean ifMatchText(String fileName, String csText, String ncsText, boolean sRegex, Pattern csPattern,
                                Pattern ncsPattern) {
        boolean match = true;
        String lFileName = fileName.toLowerCase();
        String lcsText = csText.toLowerCase();
        String lncsText = ncsText.toLowerCase();
        if (sRegex) {
            if (csText.length() != 0) {
                Matcher m = csPattern.matcher(fileName);
                match = m.find();
            }
            if (match && ncsText.length() != 0) {
                Matcher m = ncsPattern.matcher(fileName);
                match = !m.find();
            }
        } else {
            if (csText.length() != 0) {
                match = lFileName.contains(lcsText);
            }
            if (match && ncsText.length() != 0) {
                match = !lFileName.contains(lncsText);
            }
        }
        return match;
    }
}
