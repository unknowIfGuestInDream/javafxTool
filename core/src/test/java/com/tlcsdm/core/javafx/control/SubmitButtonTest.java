/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author unknowIfGuestInDream
 */
public class SubmitButtonTest extends Application {
    private SubmitButton button;
    private double progress;
    private long lastTimerCall;
    private boolean toggle;
    private AnimationTimer timer;

    @Override
    public void init() {
        button = new SubmitButton();
        button.setColor(Color.web("#34495e"));
        button.setOnMousePressed(e -> timer.start());
        button.statusProperty().addListener(o -> System.out.println(button.getStatus()));

        progress = 0;
        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastTimerCall + 50_000_000L) {
                    progress += 0.005;
                    button.setProgress(progress);
                    lastTimerCall = now;
                    if (toggle) {
                        if (progress > 0.75) {
                            progress = 0;
                            button.setFailed();
                            timer.stop();
                            toggle ^= true;
                        }
                    } else {
                        if (progress > 1) {
                            progress = 0;
                            timer.stop();
                            toggle ^= true;
                        }
                    }
                }
            }
        };
    }

    @Override
    public void start(Stage stage) {
        StackPane pane = new StackPane(button);
        pane.setPadding(new Insets(20));

        Scene scene = new Scene(pane);

        stage.setTitle("Submit Button");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
