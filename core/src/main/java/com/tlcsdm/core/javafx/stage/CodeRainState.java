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
import com.tlcsdm.core.javafx.util.OSUtil;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.OptionalInt;
import java.util.Random;

/**
 * 代码雨.
 *
 * @author unknowIfGuestInDream
 */
public class CodeRainState extends BaseStage {

    private static CodeRainState instance = null;
    private static Stage mainStage;
    private final String title = "coderain-desktop";
    private AnimationTimer timer;
    public static String color = "#00ff00";
    private Dimension screenSize;

    /**
     * 调用单例工厂.
     */
    public static CodeRainState getInstance() {
        if (instance == null) {
            instance = SingletonFactory.getWeakInstace(CodeRainState.class);
        }
        return instance;
    }

    public void start() {
        Stage stage = getStage();
        mainStage = new Stage();
        mainStage.setTitle(title);
        mainStage.initOwner(stage);
        //透明窗口
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setX(0);
        mainStage.setY(0);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(screenSize.getWidth(), screenSize.getHeight());
        // bind the width and height properties when screen is resized.
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        init(canvas);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        mainStage.setScene(scene);
        //关闭自由调整大小
        mainStage.setResizable(false);
        mainStage.setAlwaysOnTop(false);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> show());
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> close());
        mainStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                this.close();
            }
        });
        OSUtil.setWinIconAfter(title);
    }

    private void init(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int fontSize = 25; // font size in pixels

        // Animate the matrix effect
        timer = new AnimationTimer() {
            long lastTimerCall = 0;
            final long NANOS_PER_MILLI = 1000000; //nanoseconds in a millisecond
            final long ANIMATION_DELAY = 50 * NANOS_PER_MILLI; // convert 50 ms to ns

            // Capture current dimensions of the Canvas
            int prevWidth = (int) canvas.getWidth();
            int prevHeight = (int) canvas.getHeight();

            // Keeps track of each column's y coordinate for next iteration to draw a character.
            int[] ypos = resize(gc, fontSize);

            // Random generator for characters and symbols
            final Random random = new Random();

            @Override
            public void handle(long now) {
                // elapsed time occurred so let's begin drawing on the canvas.
                if (now > lastTimerCall + ANIMATION_DELAY) {
                    lastTimerCall = now;

                    int w = (int) canvas.getWidth();
                    int h = (int) canvas.getHeight();
                    // did resize occur?
                    if (w != prevWidth || h != prevHeight) {
                        // clear canvas and recalculate ypos array.
                        ypos = resize(gc, fontSize);
                        prevWidth = w;
                        prevHeight = h;
                    }
                    // Each frame add a small amount of transparent black to the canvas essentially
                    // creating a fade effect.
                    gc.setFill(Color.web("#0001"));
                    gc.fillRect(0, 0, w, h);
                    // Set an opaque color for the drawn characters.
                    gc.setFill(Color.web(color));
                    gc.setFont(new Font("monospace", fontSize - 5));
                    // Based on the stored y coordinate allows us to draw the character next (beneath the previous)
                    for (int i = 0; i < ypos.length; i++) {
                        // pick a random character (using unicode)
                        //char ch = (char) random.ints(12353, 12380) // Japanese
                        //char ch = (char) random.ints(12100, 12200) // Chinese
                        OptionalInt opt = random.ints(932, 960) // Greek
                            .findFirst();
                        char ch = (char) (opt.isPresent() ? opt.getAsInt() : 940);
                        String text = Character.toString(ch);
                        // x coordinate to draw from left to right (each column).
                        double x = i * fontSize;
                        // y coordinate is based on the value previously stored.
                        int y = ypos[i];
                        // draw a character with an opaque color
                        gc.fillText(text, x, ypos[i]);
                        // The effect similar to dripping paint from the top (y = 0).
                        // If the current y is greater than the random length then reset the y position to zero.
                        if (y > 100 + Math.random() * 10000) {
                            // (restart the drip process from the top)
                            ypos[i] = 0;
                        } else {
                            // otherwise, in the next iteration draw the character beneath this character (continue to drip).
                            ypos[i] = y + fontSize;
                        }
                    }
                }
            }
        };
    }

    /**
     * Fill the entire canvas area with the color black. Returns a resized array of y positions
     * that keeps track of each column's y coordinate position.
     */
    private int[] resize(GraphicsContext gc, int fontSize) {
        // clear by filling the background with black.
        Canvas canvas = gc.getCanvas();
        gc.setFill(Color.web("#000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // resize ypos array based on the width of the canvas
        int cols = (int) Math.floor(canvas.getWidth() / fontSize) + 1;
        int[] ypos = new int[cols];
        return ypos;
    }

    @Override
    public void close() {
        if (mainStage != null) {
            //隐藏就停止动画，节省性能
            timer.stop();
            mainStage.close();
        }
    }

    @Override
    public void show() {
        init();
        if (!mainStage.isShowing()) {
            timer.start();
            mainStage.show();
            OSUtil.setWinIconAfter(title);

        }
    }

    @Override
    public void init() {
        if (mainStage == null) {
            getInstance().start();
            OSUtil.setWinIconAfter(title);
            mainStage.requestFocus();
        }
    }

}
