/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.controller;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.stage.WebWallpaperStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * 网页壁纸.
 *
 * @author unknowIfGuestInDream
 */
public class WebWallpaperController extends BaseController {
    private Dimension screenSize;
    private static WebWallpaperController instance;
    private Stage stage;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private WebView webView;

    public static WebWallpaperController getInstance() {
        return instance;
    }

    @FXML
    private void initialize() {
        instance = this;
        Platform.runLater(() -> {
            stage = (Stage) rootAnchorPane.getScene().getWindow();
        });
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        initWebView();
    }

    private void initWebView() {
        webView.setMinSize(screenSize.getWidth(), screenSize.getHeight());
        webView.setMaxSize(screenSize.getWidth(), screenSize.getHeight());
        //设置最大历史记录数0,不记录任何历史记录
        WebHistory webHistory = webView.getEngine().getHistory();
        webHistory.setMaxSize(0);
    }

    public void setWeb(String path) {
        WebEngine webEngine = webView.getEngine();
        //alert调试
        webEngine.setOnAlert((WebEvent<String> wEvent) -> {
            StaticLog.info("JS alert() message: " + wEvent.getData());
        });
        //先跳转到空页面，立即释放部分内存
        webEngine.load(null);
        webEngine.load(path);
    }

    @Override
    public void show() {
        if (!stage.isShowing()) {
            WebWallpaperStage.getInstance().show();
        }
    }

    @Override
    public void close() {
        WebWallpaperStage.getInstance().close();
        //释放网页资源
        webView.getEngine().load(null);
    }

}
