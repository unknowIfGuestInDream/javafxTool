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

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * 特效stage运行demo.
 *
 * @author unknowIfGuestInDream
 */
public class SnowDemo extends Application {

    static final Image FLASK_IMG = new Image(Objects.requireNonNull(
        SnowDemo.class.getResource("/com/tlcsdm/core/static/graphic/flake.png")).toExternalForm(), 5, 5, true, true);

    private final ArrayList<Flake> flakes = new ArrayList<>(2000);
    private final int flaskNum = 1600;
    private final int w = 500;
    private final int h = 500;
    private Pane root;
    private final Random random = new Random();
    private Text textFps;

    /**
     * 雪花
     */
    static class Flake extends ImageView {
        private double verSpeed;
        private double horSpeed;

        public Flake() {
            super(FLASK_IMG);
        }

        public double getVerSpeed() {
            return verSpeed;
        }

        public void setVerSpeed(double verSpeed) {
            this.verSpeed = verSpeed;
        }

        public double getHorSpeed() {
            return horSpeed;
        }

        public void setHorSpeed(double horSpeed) {
            this.horSpeed = horSpeed;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new Pane();
        textFps = new Text("FPS: 0");
        textFps.setFill(Color.RED);
        textFps.setTextOrigin(VPos.TOP);
        textFps.setFont(Font.font(35));
        root.getChildren().add(textFps);
        String bgUrl = Objects.requireNonNull(
            getClass().getResource("/com/tlcsdm/core/static/graphic/snow_bg.jpg")).toExternalForm();
        root.setStyle("-fx-background-image: url('" + bgUrl + "')");
        initSnow();
        SnowState.getInstance().setStage(primaryStage);
        SnowState.getInstance().show();
        SakuraState.getInstance().setStage(primaryStage);
        SakuraState.getInstance().show();
//        BubbleCursorStage.getInstance().setStage(primaryStage);
//        BubbleCursorStage.getInstance().show();
        primaryStage.setScene(new Scene(root, w, h));
        primaryStage.setTitle("Snow");
        primaryStage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            int times;
            long lastTime = System.nanoTime();
            long ONE_SECOND = 1_000_000_000;
            int fps;
            double duration;

            @Override
            public void handle(long now) {
                fps++;
                duration += now - lastTime;
                if (duration >= ONE_SECOND) {
                    textFps.setText("FPS: " + fps);
                    fps = 0;
                    duration -= ONE_SECOND;
                }
                lastTime = now;

                times++;
                for (Flake flake : flakes) {
                    flake.setRotate((flake.getRotate() + 3) % 360);
                    flake.setY(flake.getY() + flake.getVerSpeed());
                    flake.setX(flake.getX() + flake.getHorSpeed());
                    if (flake.getY() > h) {
                        flake.setX(random.nextDouble() * w);
                        flake.setY(0);
                        flake.setVerSpeed(random.nextDouble() * 2.5 + 0.5);
                        flake.setOpacity(1 - flake.getY() / h);
                        flake.setScaleX(random.nextDouble() * 0.5 + 0.6);
                        flake.setScaleY(flake.getScaleX());
                    }
                }
            }
        };
        animationTimer.start();
    }

    private void initSnow() {
        for (int i = 0; i < flaskNum; i++) {
            Flake flake = new Flake();
            flake.setX(random.nextDouble() * w);
            flake.setY(random.nextDouble() * h);
            flake.setVerSpeed(random.nextDouble() * 2.5 + 0.5);
            flake.setHorSpeed(random.nextDouble() - 0.5);
            flake.setOpacity(1 - flake.getY() / h);
            flake.setScaleX(random.nextDouble() * 0.5 + 0.6);
            flake.setScaleY(flake.getScaleX());
            flake.setRotate(random.nextDouble() * 360);
            flakes.add(flake);
        }
        root.getChildren().addAll(flakes);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
