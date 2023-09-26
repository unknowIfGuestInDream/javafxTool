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

package com.tlcsdm.core.javafx.control;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 加载组件
 *
 * <p><b>Example:</b></p>
 * <pre><code>
 *         ProgressStage ps=ProgressStage.of();
 *         ps.show();
 *
 *         ThreadPoolTaskExecutor.get().execute(() -> {
 *             ThreadUtil.safeSleep(5000);
 *             ps.close();
 *         });
 * </code></pre>
 *
 * @author unknowIfGuestInDream
 * @date 2023/3/4 21:05
 */
public class ProgressStage {
    private Stage stage;
    private Stage parentStage;

    private ProgressStage() {
    }

    /**
     * 创建加载动画
     */
    public static ProgressStage of(Stage parent, String msg) {
        ProgressStage ps = new ProgressStage();
        ps.initUI(parent, msg);
        return ps;
    }

    public static ProgressStage of(String msg) {
        return ProgressStage.of(FxApp.primaryStage, msg);
    }

    public static ProgressStage of() {
        return ProgressStage.of(FxApp.primaryStage, I18nUtils.get("core.control.progressStage.msg"));
    }

    /**
     * 显示
     */
    public void show() {
        parentStage.getScene().getRoot().setEffect(new GaussianBlur());
        stage.show();
    }

    public void close() {
        FxApp.runLater(() -> {
            if (stage.isShowing()) {
                stage.close();
                parentStage.getScene().getRoot().setEffect(null);
            }
        });
    }

    private void initUI(Stage parent, String msg) {
        this.parentStage = parent;
        stage = new Stage();
        stage.initOwner(parent);
        // style
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);

        // message
        Label adLbl = new Label(msg);
        adLbl.setTextFill(Color.BLUE);

        // progress
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setProgress(-1);
        // pack
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setBackground(Background.EMPTY);
        vBox.getChildren().addAll(indicator, adLbl);

        // scene
        Scene scene = new Scene(vBox);
        scene.setFill(null);
        stage.setScene(scene);
        stage.setWidth(msg.length() * 8 + 10);
        stage.setHeight(100);

        // show center of parent
        double x = parent.getX() + (parent.getWidth() - stage.getWidth()) / 2;
        double y = parent.getY() + (parent.getHeight() - stage.getHeight()) / 2;
        stage.setX(x);
        stage.setY(y);
    }
}
