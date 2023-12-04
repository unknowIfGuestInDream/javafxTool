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

import com.tlcsdm.core.javafx.factory.BaseStage;
import com.tlcsdm.core.javafx.factory.SingletonFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/2/2 20:42
 */
public class BubbleCursorStage extends BaseStage {
    private static BubbleCursorStage instance = null;
    private Stage mainStage;

    public static BubbleCursorStage getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(BubbleCursorStage.class);
        }
        return instance;
    }

    private void start() throws Exception {
        Stage stage = new Stage();
        mainStage = stage;
        stage.initOwner(BaseStage.getStage());
        stage.initStyle(StageStyle.TRANSPARENT);
        URL url = BubbleCursorStage.class.getResource("/fxml/bubbleCursor.fxml");

        String urlStr = java.net.URLDecoder.decode(String.valueOf(url), "utf-8");
        url = new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();

        // TODO
        //Scene scene=new Scene(root, HardwareUtil.getScreenWeight(),HardwareUtil.getScreenHeight());
        Scene scene = new Scene(root, 500, 500);
        scene.setFill(null);
        stage.setX(0);
        stage.setY(0);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void close() {
        if (mainStage != null) {
            mainStage.close();
        }
    }

    @Override
    public void show() {
        if (mainStage == null) {
            try {
                getInstance().start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return;
        }
        if (!mainStage.isShowing()) {
            mainStage.show();
        }
    }
}
