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
import com.tlcsdm.core.javafx.factory.SingletonFactory;
import com.tlcsdm.core.javafx.util.ScreenUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 彩色鼠标痕迹.
 *
 * @author unknowIfGuestInDream
 */
public final class BubbleCursorStage extends BaseStage {
    private static BubbleCursorStage instance = null;
    private Stage mainStage;

    /**
     * 调用单例工厂.
     */
    public static BubbleCursorStage getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(BubbleCursorStage.class);
        }
        return instance;
    }

    private void start() throws IOException {
        Stage stage = getStage();
        mainStage = new Stage();
        mainStage.initOwner(getStage());
        mainStage.initStyle(StageStyle.TRANSPARENT);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> {
            show();
        });
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            close();
        });
        URL url = BubbleCursorStage.class.getResource("/com/tlcsdm/core/fxml/stage/bubbleCursor.fxml");
        String urlStr = java.net.URLDecoder.decode(String.valueOf(url), StandardCharsets.UTF_8);
        url = new URL(urlStr);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, ScreenUtil.getScreenWeight(), ScreenUtil.getScreenHeight());
        scene.setFill(null);
        mainStage.setX(0);
        mainStage.setY(0);
        mainStage.setScene(scene);
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
            } catch (IOException e) {
                StaticLog.error(e);
            }
            return;
        }
        if (!mainStage.isShowing()) {
            mainStage.show();
        }
    }
}
