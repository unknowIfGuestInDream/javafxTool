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

package com.tlcsdm.qe.provider;

import com.tlcsdm.core.eventbus.EventBus;
import com.tlcsdm.frame.event.SplashAnimFinishedEvent;
import com.tlcsdm.frame.service.SplashScreen;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

/**
 * 闪屏图片
 *
 * @author unknowIfGuestInDream
 * @date 2023/3/3 22:47
 */
public class QeSplashProvider implements SplashScreen {

    @Override
    public boolean supportAnimation() {
        return true;
    }

    @Override
    public Parent getParent() {
        Pane pane = new Pane();
        int w = 316;
        int h = 660;
        int radius = 64;
        pane.setPrefSize(w, h);
        SVGPath icon = new SVGPath();
        icon.setScaleX(2);
        icon.setScaleY(2);
        icon.setContent(
            "M10.3,7.3L8.2,5.2l0,0L7.8,4.5L7,3.9L6.1,4.8l0.6,0.8L7.4,6l0,0l2.1,2.1L9.2,8.4l1.4,1.4  c0.3,0.3,0.7,0.3,1,0L12,9.5c0.3-0.3,0.3-0.7,0-1l-1.4-1.4C10.6,7,10.3,7.3,10.3,7.3zM11.8,6.3c0.5-0.5,0.5-1.2,0.3-1.7l-0.9,1L10.6,5l0.9-1C11,3.7,10.3,3.8,9.8,4.3C9.4,4.6,9.2,5.2,9.4,5.7  L6.2,8.8C6.1,9,6.1,9.3,6.2,9.5l0,0l0.4,0.3c0.2,0.2,0.5,0.2,0.7,0l3.2-3.1C10.9,6.8,11.4,6.7,11.8,6.3zM1.4,2.4h1.4v7.5H1.4V2.4z M1.9,2.4h4.4v1.3H1.9C1.9,3.8,1.9,2.4,1.9,2.4z M1.9,5.6h3.8v1.3H1.9V5.6zM10.3,13H2.7C1.2,13,0,11.8,0,10.3V2.7C0,1.2,1.2,0,2.7,0h7.6C11.8,0,13,1.2,13,2.7v7.6C13,11.8,11.8,13,10.3,13z M2.7,0.3  c-1.3,0-2.4,1.1-2.4,2.4v7.6c0,1.3,1.1,2.4,2.4,2.4h7.6c1.3,0,2.4-1.1,2.4-2.4V2.7c0-1.3-1.1-2.4-2.4-2.4C10.3,0.3,2.7,0.3,2.7,0.3z");
        icon.setFill(new LinearGradient(0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, new Color(0.11, 0.52, 0.93, 1.0)), new Stop(1.0, new Color(0.68, 0.05, 0.93, 1.0))));
        StackPane iconPane = new StackPane(icon);
        iconPane.setPrefSize(radius, radius);
        iconPane.setStyle("-fx-background-color: #2b2b2b;-fx-background-radius: " + radius / 2.0 + "px");
        iconPane.setEffect(new DropShadow());
        iconPane.setLayoutX((w - radius) / 2.0);
        iconPane.setLayoutY((h - radius) / 2.0);
        pane.setStyle("-fx-background-color: null;");
        pane.getChildren().add(iconPane);
        iconPane.setOpacity(0.2);
        Circle circle = new Circle(70);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), iconPane);
        fadeTransition.setFromValue(0.2);
        fadeTransition.setToValue(1.0);
        PathTransition pathTransition = new PathTransition(Duration.seconds(2), circle, iconPane);
        ScaleTransition scaleTransition = scaleTransition(1.2, iconPane, 2, 2);
        SequentialTransition animation = new SequentialTransition(fadeTransition, pathTransition, scaleTransition);
        animation.setInterpolator(Interpolator.EASE_IN);
        animation.setOnFinished(event -> {
            EventBus.getDefault().post(new SplashAnimFinishedEvent());
        });
        animation.play();

        return pane;
    }

    private ScaleTransition scaleTransition(double duration, Node node, double byX, double byY) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(duration), node);
        scaleTransition.setByX(byX);
        scaleTransition.setByY(byY);
        return scaleTransition;
    }
}
