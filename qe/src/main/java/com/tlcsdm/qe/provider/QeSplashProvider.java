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
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.Keys;
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
        icon.setScaleX(2.5);
        icon.setScaleY(2.5);
        icon.setContent("""
            M8.7,6.8c0-1.1-0.4-2.1-0.9-2.7C7,3.3,6.2,3,5.1,3s-2,0.3-2.6,1S1.5,5.6,1.5,6.8s0.4,2.1,0.9,2.7
            	c0.6,0.7,1.5,1,2.6,1c0.2,0,0.6,0,0.8-0.1c0,0,0.4,0.5,0.7,0.7c0.5,0.2,1.3,0,1.3,0l-0.1-0.8c-0.1,0-0.4,0-0.5-0.1
            	c-0.1,0-0.2-0.1-0.4-0.2C8.1,9.3,8.7,8.2,8.7,6.8z M6.4,8.6L6.4,8.6C6.3,8.8,6.1,8.4,5.6,8.1C5.2,8,4.9,8,4.5,8.1v0.6
            	c0.2,0,0.5,0.1,0.7,0.2C5.5,9,5.6,9.3,5.5,9.3c-0.1,0-0.4,0.1-0.5,0C4.4,9.3,3.9,9,3.7,8.6C3.3,8.1,3.2,7.4,3.2,6.6s0.1-1.5,0.5-1.9
            	C4.2,4,5.2,3.8,5.9,4.2c0.2,0.1,0.4,0.2,0.5,0.5c0.4,0.5,0.5,1.1,0.5,1.9S6.8,8.1,6.4,8.6z M14.5,8.7L14.5,8.7L14.5,8.7L14.5,8.7
            	c0,0.1-0.1,0.2-0.2,0.2h-3.9c-0.1,0-0.2-0.1-0.2-0.2s0.1-0.2,0.2-0.2H14c0-0.1,0-0.2-0.1-0.3c-0.4-1-1.5-1.7-2.7-1.4
            	C10,7.1,9.3,8.2,9.6,9.3c0.4,1,1.7,1.7,2.8,1.4c0.6-0.1,1.1-0.6,1.3-1l0,0c0,0,0-0.1,0.1-0.1c0.1-0.1,0.2,0,0.4,0.1
            	c0,0.1,0,0.2,0,0.2l0,0c-0.4,0.6-0.9,1-1.7,1.3c-1.4,0.3-3-0.3-3.4-1.7C8.8,8,9.6,6.6,11,6.3s3,0.3,3.4,1.7
            	C14.5,8.2,14.5,8.5,14.5,8.7L14.5,8.7z M12.2,14.9h-9c-1.8,0-3.2-1.4-3.2-3.1V3.1C0,1.4,1.4,0,3.2,0h9c1.8,0,3.2,1.4,3.2,3.1v8.7
            	C15.4,13.5,14,14.9,12.2,14.9z M3.2,0.3c-1.5,0-2.8,1.3-2.8,2.7v8.7c0,1.5,1.3,2.7,2.8,2.7h9c1.5,0,2.8-1.3,2.8-2.7V3.1
            	c0-1.5-1.3-2.7-2.8-2.7C12.2,0.3,3.2,0.3,3.2,0.3z
            """);
        icon.setFill(new LinearGradient(0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE, new Stop(0.0, new Color(0.11, 0.90, 0.13, 1.0)), new Stop(1.0, new Color(0.68, 0.05, 0.93, 1.0))));
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
        animation.setOnFinished(event -> EventBus.getDefault().post(new SplashAnimFinishedEvent()));
        if (Config.getBoolean(Keys.SkipBootAnimation, false)) {
            animation.setRate(10);
        }
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
