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

import com.tlcsdm.core.javafx.factory.SingletonFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebWallpaperStage extends BaseStage {
    private static WebWallpaperStage instance = null;
    private Dimension screenSize;
    private Stage mainStage;

    public WebWallpaperStage() {

    }

    //调用单例工厂
    public static WebWallpaperStage getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(WebWallpaperStage.class);
        }
        return instance;
    }

    public void start() throws Exception {
        Stage stage = new Stage();
        mainStage = stage;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕
        stage.initOwner(getStage());
        stage.initStyle(StageStyle.TRANSPARENT);
        URL url = WebWallpaperStage.class.getResource("/com/tlcsdm/core/fxml/stage/webWallpaper.fxml");

        String urlStr = java.net.URLDecoder.decode(String.valueOf(url), StandardCharsets.UTF_8);
        url = new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        root.setMouseTransparent(true);
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setX(0);
        stage.setY(0);
        stage.setTitle("web-wallpaper-desktop");
        stage.setScene(scene);
        stage.show();
        // OsUtil.setWinIconAfter(StageTitleConst.WEBWALLPAPERTITLE);
    }

    @Override
    public void close() {
        mainStage.close();
    }

    @Override
    public void show() {
        if (mainStage != null) {
            mainStage.show();
            // OsUtil.setWinIconAfter(StageTitleConst.WEBWALLPAPERTITLE);
        } else {
            try {
                getInstance().start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
