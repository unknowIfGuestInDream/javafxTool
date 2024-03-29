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

package com.tlcsdm.core.javafx.stage;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.controller.WebWallpaperController;
import com.tlcsdm.core.javafx.factory.SingletonFactory;
import com.tlcsdm.core.javafx.util.OSUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 网络资源.
 *
 * @author unknowIfGuestInDream
 */
public class WebWallpaperStage extends BaseStage {
    private static WebWallpaperStage instance;
    private Dimension screenSize;
    private Stage mainStage;
    private final String title = "web-wallpaper-desktop";

    private static String webWallpaperPath;

    public WebWallpaperStage() {
    }

    /**
     * 调用单例工厂.
     */
    public static WebWallpaperStage getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(WebWallpaperStage.class);
        }
        return instance;
    }

    public void start() throws IOException {
        Stage stage = getStage();
        mainStage = new Stage();
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainStage.initOwner(stage);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> show());
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> close());
        URL url = WebWallpaperStage.class.getResource("/com/tlcsdm/core/fxml/stage/webWallpaper.fxml");
        String urlStr = java.net.URLDecoder.decode(String.valueOf(url), StandardCharsets.UTF_8);
        url = new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        WebWallpaperController webWallpaperController = fxmlLoader.getController();
        webWallpaperController.setWeb(webWallpaperPath);
        root.setMouseTransparent(true);
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        mainStage.setX(0);
        mainStage.setY(0);
        mainStage.setTitle(title);
        mainStage.setScene(scene);
        OSUtil.setWinIconAfter(title);
    }

    @Override
    public void close() {
        mainStage.close();
    }

    @Override
    public void show() {
        init();
        if (!mainStage.isShowing()) {
            mainStage.show();
            OSUtil.setWinIconAfter(title);
        }
    }

    @Override
    public void init() {
        if (mainStage == null) {
            if (StringUtils.isEmpty(webWallpaperPath)) {
                throw new IllegalArgumentException("Please initialize webWallpaperPath!");
            }
            try {
                getInstance().start();
            } catch (IOException e) {
                StaticLog.error(e);
            }
        }
    }

    public static String getWebWallpaperPath() {
        return webWallpaperPath;
    }

    public static void setWebWallpaperPath(String webWallpaperPath) {
        WebWallpaperStage.webWallpaperPath = webWallpaperPath;
    }
}
