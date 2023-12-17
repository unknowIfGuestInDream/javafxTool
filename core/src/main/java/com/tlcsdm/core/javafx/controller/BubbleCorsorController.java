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

package com.tlcsdm.core.javafx.controller;

import com.tlcsdm.core.javafx.stage.BubbleCursorStage;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.MouseInfo;
import java.awt.Point;

/**
 * 鼠标彩色痕迹.
 *
 * @author unknowIfGuestInDream
 */
public class BubbleCorsorController extends BaseController {

    @FXML
    Group root;

    private static BubbleCorsorController instance;

    private Stage stage;

    private Timeline timeline;

    /**
     * 鼠标前一次移动的坐标位置.
     */
    private int preX = -1;
    private int preY = -1;

    /**
     * 淡入淡出结束事件.
     */
    private EventHandler<ActionEvent> fadeFinishAction = event -> {
        FadeTransition t = (FadeTransition) event.getSource();
        Circle c = (Circle) t.getNode();
        root.getChildren().remove(c);
    };

    public static BubbleCorsorController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        Platform.runLater(() -> {
            stage = (Stage) root.getScene().getWindow();
            stage.setAlwaysOnTop(true);
            stage.toFront();
        });
        root.setStyle("-fx-fill: null;-fx-background-color: rgba(0,0,0,0)");

        //时间轴动画
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60.0), e -> {

            Point point = MouseInfo.getPointerInfo().getLocation();
            if (point.x != preX && point.y != preY) {
                preX = point.x;
                preY = point.y;
                renderFrame(point.x, point.y);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    protected final void renderFrame(double x, double y) {
        //随机色
        Circle circle = new Circle(1, Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)).brighter());
        circle.setTranslateX(x + 10);
        circle.setTranslateY(y + 10);
        root.getChildren().add(circle);
        //平移过度
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), circle);
//        translateTransition.setFromX(20);
        translateTransition.setToX(x + 50);
        translateTransition.setToY(y + 50);

//        translateTransition.setCycleCount(Timeline.INDEFINITE);
        translateTransition.setAutoReverse(false);
        translateTransition.play();
        //动画结束，粒子死亡
        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //粒子死亡
                TranslateTransition t = (TranslateTransition) (event.getSource());
                Circle c = (Circle) t.getNode();
                //淡入淡出
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), c);
                //起始透明度为1
                fadeTransition.setFromValue(1);
                //终止透明度为0.1
                fadeTransition.setToValue(0);
                fadeTransition.play();
                fadeTransition.setOnFinished(fadeFinishAction);
            }
        });
        //缩放过度
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), circle);
        scaleTransition.setToX(2);
        scaleTransition.setToY(2);
        scaleTransition.play();
    }

    @Override
    public void show() {
        if (!stage.isShowing()) {
            BubbleCursorStage.getInstance().show();
            startTimer();
        }
    }

    @Override
    public void close() {
        BubbleCursorStage.getInstance().close();
        //关闭时间轴
        stopTimer();
    }

    protected void startTimer() {
        if (timeline.getStatus() != Animation.Status.RUNNING) {
            timeline.play();
        }
    }

    protected void stopTimer() {
        if (timeline.getStatus() != Animation.Status.STOPPED) {
            timeline.stop();
        }
    }

}
