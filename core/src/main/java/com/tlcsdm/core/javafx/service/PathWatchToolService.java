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
import com.tlcsdm.core.javafx.controller.PathWatchToolController;
import com.tlcsdm.core.javafx.util.TooltipUtil;
import javafx.geometry.Pos;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

/**
 * 文件夹监控工具
 *
 * @author unknowIfGuestInDream
 */
public class PathWatchToolService {
    private final PathWatchToolController pathWatchToolController;
    Thread thread = null;

    public PathWatchToolService(PathWatchToolController pathWatchToolController) {
        this.pathWatchToolController = pathWatchToolController;
    }

    public void watchAction() {
        String watchPath = pathWatchToolController.getWatchPathTextField().getText();
        if (StringUtils.isEmpty(watchPath)) {
            TooltipUtil.showToast("监控目录不能为空！");
            return;
        }
        Path path = Paths.get(watchPath);
        if (!Files.exists(path)) {
            TooltipUtil.showToast("监控目录不存在！");
            return;
        } else if (!Files.isDirectory(path)) {
            TooltipUtil.showToast("只能监控文件夹！");
            return;
        }
        if (thread != null) {
            thread.stop();
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
        thread = new Thread(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                //给path路径加上文件观察服务
//                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                        if (DirectoryTreeUtil.ifMatchText(dir.toString(), folderPathCsText, folderPathNCsText, folderPathSRegex, folderPathCsPattern, folderPathNCsPattern)) {
//                            dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
//                        } else {
//                            StaticLog.info("跳过监听：" + dir.toString());
//                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
                while (true) {
                    final WatchKey key = watchService.take();
                    for (WatchEvent<?> watchEvent : key.pollEvents()) {
                        StringBuffer stringBuffer = new StringBuffer();
                        final WatchEvent.Kind<?> kind = watchEvent.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }
                        // get the filename for the event
                        final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                        final Path filename = watchEventPath.context();
                        Path watchable = ((Path) key.watchable()).resolve(filename);
                        if (Files.isDirectory(watchable)) {
//                            if (!DirectoryTreeUtil.ifMatchText(filename.toString(), folderPathCsText, folderPathNCsText, folderPathSRegex, folderPathCsPattern, folderPathNCsPattern)) {
//                                StaticLog.info("跳过文件夹监听：" + watchable);
//                                continue;
//                            }
                        }
                        if (Files.isRegularFile(watchable)) {
//                            if (!DirectoryTreeUtil.ifMatchText(filename.toString(), fileNameContains, fileNameNotContains, fileNameSRegex, fileNameCsPattern, fileNameNCsPattern)) {
//                                StaticLog.info("跳过文件监听：" + watchable);
//                                continue;
//                            }
                        }
                        //创建事件
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            stringBuffer.append("新建：");
                            pathWatchToolController.getWatchLogTextArea().appendText("新建：");
                            if (Files.isDirectory(watchable)) {
                                watchable.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
                            }
                        }
                        //修改事件
                        if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            stringBuffer.append("修改：");
                            pathWatchToolController.getWatchLogTextArea().appendText("修改：");
                        }
                        //删除事件
                        if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                            stringBuffer.append("删除：");
                            pathWatchToolController.getWatchLogTextArea().appendText("删除：");
                        }
                        stringBuffer.append(kind + " -> " + filename + "\n");
                        StaticLog.info(stringBuffer.toString());
                        pathWatchToolController.getWatchLogTextArea().appendText(stringBuffer.toString());
                        if (pathWatchToolController.getIsShowNotificationCheckBox().isSelected()) {
                            TooltipUtil.showToast("文件夹发送变化", stringBuffer.toString(), Pos.BOTTOM_RIGHT);
                        }
                    }
                    boolean valid = key.reset();
//                    if (!valid) {
//                        break;
//                    }
                }
            } catch (IOException | InterruptedException ex) {
                StaticLog.error("获取监听异常：", ex);
            }
        });
        thread.start();
    }

    public void stopWatchAction() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }
}
