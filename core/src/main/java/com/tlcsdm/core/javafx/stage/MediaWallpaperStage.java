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
import com.tlcsdm.core.javafx.controller.MediaWallpaperController;
import com.tlcsdm.core.javafx.factory.SingletonFactory;
import com.tlcsdm.core.javafx.util.OSUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 视频壁纸.
 *
 * @author unknowIfGuestInDream
 */
public class MediaWallpaperStage extends BaseStage {

    private static MediaWallpaperStage instance = null;
    private Dimension screenSize;
    private Stage mainStage;
    private final String title = "media-wallpaper-desktop";

    private static String mediaWallpaperPath;

    public MediaWallpaperStage() {
    }

    /**
     * 调用单例工厂.
     */
    public static MediaWallpaperStage getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(MediaWallpaperStage.class);
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
        URL url = MediaWallpaperStage.class.getResource("/com/tlcsdm/core/fxml/stage/mediaWallpaper.fxml");
        String urlStr = java.net.URLDecoder.decode(String.valueOf(url), StandardCharsets.UTF_8);
        url = new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        MediaWallpaperController mediaWallpaperController = fxmlLoader.getController();
        mediaWallpaperController.setMedia(mediaWallpaperPath);
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
            if (StringUtils.isEmpty(mediaWallpaperPath)) {
                throw new IllegalArgumentException("Please initialize mediaWallpaperPath!");
            }
            try {
                getInstance().start();
            } catch (IOException e) {
                StaticLog.error(e);
            }
        }
    }

    public static String getMediaWallpaperPath() {
        return mediaWallpaperPath;
    }

    public static void setMediaWallpaperPath(String mediaWallpaperPath) {
        MediaWallpaperStage.mediaWallpaperPath = mediaWallpaperPath;
    }

}
