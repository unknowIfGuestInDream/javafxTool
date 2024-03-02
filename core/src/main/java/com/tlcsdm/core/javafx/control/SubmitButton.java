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

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.DefaultProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author unknowIfGuestInDream
 */
@DefaultProperty("children")
public class SubmitButton extends Region {
    public enum Status {NONE, IN_PROGRESS, SUCCESS, FAIL, FINISHED}

    private static final double PREFERRED_WIDTH = 400;
    private static final double PREFERRED_HEIGHT = 100;
    private static final double MINIMUM_WIDTH = 40;
    private static final double MINIMUM_HEIGHT = 10;
    private static final double MAXIMUM_WIDTH = 1024;
    private static final double MAXIMUM_HEIGHT = 1024;
    private static double aspectRatio = PREFERRED_HEIGHT / PREFERRED_WIDTH;
    private boolean keepAspect;
    private double size;
    private double width;
    private double height;
    private Rectangle frame;
    private Rectangle buttonArea;
    private Text text;
    private Pane progressPane;
    private Arc progressBar;
    private StackPane iconPane;
    private Region iconWrap;
    private SVGPath icon;
    private StackPane pane;
    private ObjectProperty<Color> color;
    private ObjectProperty<Color> frameColor;
    private ObjectProperty<Color> buttonColor;
    private ObjectProperty<Color> textColor;
    private ObjectProperty<Color> progressBarColor;
    private ObjectProperty<Color> iconColor;
    private DoubleProperty progress;
    private ObjectProperty<Status> status;
    private Color formerColor;
    private Status lastStatus;
    private Paint backgroundPaint;
    private Paint borderPaint;
    private double borderWidth;
    private Timeline timeline;

    private final String checkPath = "M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z";
    private final String failPath = "M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7 86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256 41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3 297.4 406.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256 342.6 150.6z";

    private final String defColor = "#00ca94";

    public SubmitButton() {
        this("Submit");
    }

    public SubmitButton(final String text) {
        keepAspect = true;
        color = new ObjectPropertyBase<>(Color.web(defColor)) {
            @Override
            protected void invalidated() {
                super.invalidated();
                if (null == get()) {
                    set(Color.web("00ca94"));
                }
                frameColor.set(get());
                textColor.set(get());
                redraw();
            }

            @Override
            public Object getBean() {
                return SubmitButton.this;
            }

            @Override
            public String getName() {
                return "color";
            }
        };
        frameColor = new ObjectPropertyBase<>(Color.web(defColor)) {
            @Override
            public Object getBean() {
                return SubmitButton.this;
            }

            @Override
            public String getName() {
                return "frameColor";
            }
        };
        buttonColor = new ObjectPropertyBase<>(Color.WHITE) {
            @Override
            public Object getBean() {
                return SubmitButton.this;
            }

            @Override
            public String getName() {
                return "buttonColor";
            }
        };
        textColor = new ObjectPropertyBase<>(Color.web(defColor)) {
            @Override
            public Object getBean() {
                return SubmitButton.this;
            }

            @Override
            public String getName() {
                return "textColor";
            }
        };
        progressBarColor = new ObjectPropertyBase<>(Color.TRANSPARENT) {
            @Override
            public Object getBean() {
                return SubmitButton.this;
            }

            @Override
            public String getName() {
                return "progressBarColor";
            }
        };
        iconColor = new ObjectPropertyBase<>(Color.WHITE) {
            @Override
            protected void invalidated() {
                super.invalidated();
                if (null == get()) {
                    set(Color.WHITE);
                }
            }

            @Override
            public Object getBean() {
                return SubmitButton.this;
            }

            @Override
            public String getName() {
                return "iconColor";
            }
        };
        progress = new DoublePropertyBase(0) {
            @Override
            protected void invalidated() {
                set(clamp(0, 1, get()));
                progressBar.setLength(-360.0 * get());
                if (Double.compare(get(), 1.0) >= 0) {
                    animateFromProgressSuccess();
                }
            }

            @Override
            public Object getBean() {
                return SubmitButton.this;
            }

            @Override
            public String getName() {
                return "progress";
            }
        };
        status = new ObjectPropertyBase<>(Status.NONE) {
            @Override
            protected void invalidated() {
                switch (get()) {
                    case IN_PROGRESS:
                        break;
                    case SUCCESS:
                        icon.setContent(checkPath);
                        break;
                    case FAIL:
                        icon.setContent(failPath);
                        formerColor = getColor();
                        color.set(Color.CRIMSON);
                        animateFromProgressFail();
                        break;
                    case FINISHED:
                        progress.set(0);
                        break;
                    case NONE:
                    default:
                        break;
                }
            }

            @Override
            public Object getBean() {
                return SubmitButton.this;
            }

            @Override
            public String getName() {
                return "status";
            }
        };
        formerColor = getColor();
        backgroundPaint = Color.TRANSPARENT;
        borderPaint = Color.TRANSPARENT;
        borderWidth = 0d;
        timeline = new Timeline();
        init();
        initGraphics(text);
        registerListeners();
    }

    private void init() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 ||
            Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        if (Double.compare(getMinWidth(), 0.0) <= 0 || Double.compare(getMinHeight(), 0.0) <= 0) {
            setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
        }

        if (Double.compare(getMaxWidth(), 0.0) <= 0 || Double.compare(getMaxHeight(), 0.0) <= 0) {
            setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
        }
    }

    private void initGraphics(final String txt) {
        frame = new Rectangle(PREFERRED_WIDTH, PREFERRED_HEIGHT, frameColor.get());
        frame.setArcWidth(PREFERRED_HEIGHT);
        frame.setArcHeight(PREFERRED_HEIGHT);

        buttonArea = new Rectangle(5, 5, PREFERRED_WIDTH - 10, PREFERRED_HEIGHT - 10);
        buttonArea.setFill(buttonColor.get());
        buttonArea.setArcWidth(PREFERRED_HEIGHT);
        buttonArea.setArcHeight(PREFERRED_HEIGHT);

        text = new Text(txt);
        text.setFont(Font.font(48));
        text.setFill(textColor.get());
        text.setMouseTransparent(true);

        progressBar = new Arc(PREFERRED_WIDTH * 0.5, PREFERRED_HEIGHT * 0.5, PREFERRED_HEIGHT * 0.5,
            PREFERRED_HEIGHT * 0.5, 90, 0);
        progressBar.setType(ArcType.OPEN);
        progressBar.setFill(Color.TRANSPARENT);
        progressBar.setStroke(progressBarColor.get());

        progressPane = new Pane(progressBar);
        progressPane.setMouseTransparent(true);

        icon = new SVGPath();
        icon.setContent(checkPath);
        icon.setFill(iconColor.get());
        icon.setOpacity(0);
        iconWrap = new Region();
        iconWrap.setShape(icon);
        iconWrap.opacityProperty().bind(icon.opacityProperty());
        iconWrap.backgroundProperty().bind(
            Bindings.createObjectBinding(() -> Background.fill(icon.getFill()), icon.fillProperty()));

        iconPane = new StackPane(iconWrap);
        iconPane.setMouseTransparent(true);

        pane = new StackPane(frame, buttonArea, text, progressPane, iconPane);
        pane.setBackground(new Background(new BackgroundFill(backgroundPaint, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setBorder(new Border(
            new BorderStroke(borderPaint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(borderWidth))));

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
        buttonArea.setOnMousePressed(e -> animateUpToProgress());
        status.addListener((o, ov, nv) -> lastStatus = ov);
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(final Color col) {
        color.set(col);
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public double getProgress() {
        return progress.get();
    }

    public void setProgress(final double PROGRESS) {
        progress.set(PROGRESS);
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public Status getStatus() {
        return status.get();
    }

    private void setStatus(final Status STATUS) {
        status.set(STATUS);
    }

    public ReadOnlyObjectProperty<Status> statusProperty() {
        return status;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setFailed() {
        setStatus(Status.FAIL);
    }

    private void animateUpToProgress() {
        KeyValue kvButtonAreaFill0 = new KeyValue(buttonArea.fillProperty(), Color.WHITE, Interpolator.EASE_BOTH);
        KeyValue kvTextFill0 = new KeyValue(text.fillProperty(), getColor(), Interpolator.EASE_BOTH);

        KeyValue kvFrameWidth0 = new KeyValue(frame.widthProperty(), frame.getWidth(), Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaWidth0 = new KeyValue(buttonArea.widthProperty(), buttonArea.getWidth(),
            Interpolator.EASE_BOTH);
        KeyValue kvTextOpacity0 = new KeyValue(text.opacityProperty(), 1, Interpolator.EASE_BOTH);

        KeyValue kvTextSize0 = new KeyValue(text.fontProperty(), Font.font(height * 0.48), Interpolator.EASE_BOTH);
        KeyValue kvTextSize1 = new KeyValue(text.fontProperty(), Font.font(height * 0.45), Interpolator.EASE_BOTH);

        KeyValue kvButtonAreaFill1 = new KeyValue(buttonArea.fillProperty(), getColor(), Interpolator.EASE_BOTH);
        KeyValue kvTextFill1 = new KeyValue(text.fillProperty(), Color.WHITE, Interpolator.EASE_BOTH);
        KeyValue kvFrameFill0 = new KeyValue(frame.fillProperty(), getColor(), Interpolator.EASE_BOTH);

        KeyValue kvFrameWidth1 = new KeyValue(frame.widthProperty(), height, Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaWidth1 = new KeyValue(buttonArea.widthProperty(), height * 0.9, Interpolator.EASE_BOTH);
        KeyValue kvTextOpacity1 = new KeyValue(text.opacityProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue kvFrameFill1 = new KeyValue(frame.fillProperty(), Color.web("#b9b9b9"), Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaFill2 = new KeyValue(buttonArea.fillProperty(), Color.WHITE, Interpolator.EASE_BOTH);
        KeyValue kvProgressBarColor0 = new KeyValue(progressBar.strokeProperty(), getColor(), Interpolator.EASE_BOTH);

        KeyValue kvProgressBarValue0 = new KeyValue(progressBar.lengthProperty(), 0, Interpolator.EASE_BOTH);

        // KeyFrames to progress
        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kvButtonAreaFill0, kvTextFill0);
        KeyFrame kf1 = new KeyFrame(Duration.millis(300), kvButtonAreaFill1, kvTextFill1, kvTextSize0);
        KeyFrame kf2 = new KeyFrame(Duration.millis(400), kvTextSize1);
        KeyFrame kf3 = new KeyFrame(Duration.millis(500), kvTextSize0);
        KeyFrame kf4 = new KeyFrame(Duration.millis(700), kvButtonAreaFill1, kvTextFill1, kvTextOpacity0, kvFrameWidth0,
            kvButtonAreaWidth0, kvFrameFill0);
        KeyFrame kf5 = new KeyFrame(Duration.millis(1000), kvFrameWidth1, kvButtonAreaWidth1, kvTextOpacity1,
            kvButtonAreaFill2, kvFrameFill1, kvProgressBarColor0, kvProgressBarValue0);

        // Play timeline to progress
        timeline.getKeyFrames().setAll(kf0, kf1, kf2, kf3, kf4, kf5);
        timeline.setOnFinished(e -> setStatus(Status.IN_PROGRESS));
        timeline.play();
    }

    private void animateFromProgressSuccess() {
        setStatus(Status.SUCCESS);

        KeyValue kvFrameWidth1 = new KeyValue(frame.widthProperty(), height, Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaWidth1 = new KeyValue(buttonArea.widthProperty(), height * 0.9, Interpolator.EASE_BOTH);
        KeyValue kvTextOpacity1 = new KeyValue(text.opacityProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaFill2 = new KeyValue(buttonArea.fillProperty(), Color.WHITE, Interpolator.EASE_BOTH);
        KeyValue kvProgressBarColor0 = new KeyValue(progressBar.strokeProperty(), getColor(), Interpolator.EASE_BOTH);

        KeyValue kvProgressBarValue0 = new KeyValue(progressBar.lengthProperty(), 0, Interpolator.EASE_BOTH);

        KeyValue kvProgressBarColor1 = new KeyValue(progressBar.fillProperty(), Color.TRANSPARENT,
            Interpolator.DISCRETE);
        KeyValue kvFrameFill2 = new KeyValue(frame.fillProperty(), getColor(), Interpolator.DISCRETE);
        KeyValue kvIconOpacity0 = new KeyValue(icon.opacityProperty(), 0, Interpolator.EASE_BOTH);

        KeyValue kvFrameWidth2 = new KeyValue(frame.widthProperty(), width, Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaWidth2 = new KeyValue(buttonArea.widthProperty(), width - height * 0.1,
            Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaFill3 = new KeyValue(buttonArea.fillProperty(), getColor(), Interpolator.EASE_BOTH);
        KeyValue kvIconOpacity1 = new KeyValue(icon.opacityProperty(), 1, Interpolator.EASE_BOTH);

        KeyValue kvButtonAreaFill4 = new KeyValue(buttonArea.fillProperty(), Color.WHITE, Interpolator.EASE_BOTH);
        KeyValue kvTextOpacity2 = new KeyValue(text.opacityProperty(), 1, Interpolator.EASE_BOTH);
        KeyValue kvTextFill2 = new KeyValue(text.fillProperty(), getColor(), Interpolator.DISCRETE);
        KeyValue kvIconOpacity2 = new KeyValue(icon.opacityProperty(), 0, Interpolator.DISCRETE);

        // KeyFrames from progress
        KeyFrame kf7 = new KeyFrame(Duration.ZERO, kvFrameWidth1, kvButtonAreaWidth1, kvProgressBarColor0,
            kvButtonAreaFill2);

        KeyFrame kf8 = new KeyFrame(Duration.millis(1), kvFrameFill2, kvProgressBarValue0, kvProgressBarColor1,
            kvIconOpacity0);
        KeyFrame kf9 = new KeyFrame(Duration.millis(200), kvFrameWidth2, kvButtonAreaWidth2, kvButtonAreaFill3,
            kvIconOpacity1, kvTextFill2);
        KeyFrame kf10 = new KeyFrame(Duration.millis(600), kvFrameWidth2, kvButtonAreaWidth2, kvButtonAreaFill3,
            kvIconOpacity1, kvTextOpacity1);
        KeyFrame kf11 = new KeyFrame(Duration.millis(900), kvFrameWidth2, kvButtonAreaWidth2, kvButtonAreaFill3,
            kvIconOpacity2, kvTextOpacity1);
        KeyFrame kf12 = new KeyFrame(Duration.millis(1400), kvButtonAreaFill4, kvTextOpacity2);

        // Play timeline from progress
        timeline.getKeyFrames().setAll(kf7, kf8, kf9, kf10, kf11, kf12);
        timeline.setOnFinished(e -> setStatus(Status.FINISHED));
        timeline.play();
    }

    private void animateFromProgressFail() {
        setStatus(Status.FAIL);

        KeyValue kvFrameWidth1 = new KeyValue(frame.widthProperty(), height, Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaWidth1 = new KeyValue(buttonArea.widthProperty(), height * 0.9, Interpolator.EASE_BOTH);
        KeyValue kvTextOpacity1 = new KeyValue(text.opacityProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaFill2 = new KeyValue(buttonArea.fillProperty(), Color.WHITE, Interpolator.EASE_BOTH);
        KeyValue kvProgressBarColor0 = new KeyValue(progressBar.strokeProperty(), getColor(), Interpolator.EASE_BOTH);

        KeyValue kvProgressBarValue0 = new KeyValue(progressBar.lengthProperty(), 0, Interpolator.EASE_BOTH);

        KeyValue kvProgressBarColor1 = new KeyValue(progressBar.fillProperty(), Color.TRANSPARENT,
            Interpolator.DISCRETE);
        KeyValue kvFrameFill2 = new KeyValue(frame.fillProperty(), getColor(), Interpolator.DISCRETE);
        KeyValue kvIconOpacity0 = new KeyValue(icon.opacityProperty(), 0, Interpolator.EASE_BOTH);

        KeyValue kvFrameWidth2 = new KeyValue(frame.widthProperty(), width, Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaWidth2 = new KeyValue(buttonArea.widthProperty(), width - height * 0.1,
            Interpolator.EASE_BOTH);
        KeyValue kvButtonAreaFill3 = new KeyValue(buttonArea.fillProperty(), getColor(), Interpolator.EASE_BOTH);
        KeyValue kvIconOpacity1 = new KeyValue(icon.opacityProperty(), 1, Interpolator.EASE_BOTH);

        KeyValue kvButtonAreaFill4 = new KeyValue(buttonArea.fillProperty(), Color.WHITE, Interpolator.EASE_BOTH);
        KeyValue kvTextOpacity2 = new KeyValue(text.opacityProperty(), 1, Interpolator.EASE_BOTH);
        KeyValue kvTextFill2 = new KeyValue(text.fillProperty(), getColor(), Interpolator.DISCRETE);
        KeyValue kvIconOpacity2 = new KeyValue(icon.opacityProperty(), 0, Interpolator.DISCRETE);

        // KeyFrames from progress
        KeyFrame kf7 = new KeyFrame(Duration.ZERO, kvFrameWidth1, kvButtonAreaWidth1, kvProgressBarColor0,
            kvButtonAreaFill2);
        KeyFrame kf8 = new KeyFrame(Duration.millis(1), kvFrameFill2, kvProgressBarValue0, kvProgressBarColor1,
            kvIconOpacity0);
        KeyFrame kf9 = new KeyFrame(Duration.millis(200), kvFrameWidth2, kvButtonAreaWidth2, kvButtonAreaFill3,
            kvIconOpacity1, kvTextFill2);
        KeyFrame kf10 = new KeyFrame(Duration.millis(600), kvFrameWidth2, kvButtonAreaWidth2, kvButtonAreaFill3,
            kvIconOpacity1, kvTextOpacity1);
        KeyFrame kf11 = new KeyFrame(Duration.millis(900), kvFrameWidth2, kvButtonAreaWidth2, kvButtonAreaFill3,
            kvIconOpacity2, kvTextOpacity1);
        KeyFrame kf12 = new KeyFrame(Duration.millis(1400), kvButtonAreaFill4, kvTextOpacity2);

        // Play timeline from progress
        timeline.getKeyFrames().setAll(kf7, kf8, kf9, kf10, kf11, kf12);
        timeline.setOnFinished(e -> {
            setColor(formerColor);
            progressBarColor.set(Color.TRANSPARENT);
            setStatus(Status.FINISHED);
        });
        timeline.play();
    }

    private double clamp(final double min, final double max, final double value) {
        if (Double.compare(value, min) < 0) {
            return min;
        }
        if (Double.compare(value, max) > 0) {
            return max;
        }
        return value;
    }

    // ******************** Resizing ******************************************
    private void resize() {
        width = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size = Math.min(width, height);

        if (keepAspect) {
            if (aspectRatio * width > height) {
                width = 1 / (aspectRatio / height);
            } else if (1 / (aspectRatio / height) > width) {
                height = aspectRatio * width;
            }
        }

        if (width > 0 && height > 0) {
            // Use for rectangular controls width != height
            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            frame.setWidth(width);
            frame.setHeight(height);
            frame.setArcWidth(height);
            frame.setArcHeight(height);

            buttonArea.setWidth(width - height * 0.1);
            buttonArea.setHeight(height * 0.9);
            buttonArea.setFill(buttonColor.get());
            buttonArea.setArcWidth(height);
            buttonArea.setArcHeight(height);

            text.setFont(Font.font(height * 0.48));

            progressPane.setPrefSize(height, height);

            progressBar.setCenterX(width * 0.5);
            progressBar.setCenterY(height * 0.5);
            progressBar.setRadiusX(height * 0.475);
            progressBar.setRadiusY(height * 0.475);
            progressBar.setStrokeWidth(height * 0.05);

            iconPane.setPrefSize(height, height);
            iconWrap.setPrefSize((int) (height * 0.6), (int) (height * 0.6));
            iconWrap.setMaxSize((int) (height * 0.6), (int) (height * 0.6));
            redraw();
        }
    }

    private void redraw() {
        frame.setFill(frameColor.get());
        buttonArea.setFill(buttonColor.get());
        text.setFill(textColor.get());
        progressBar.setStroke(progressBarColor.get());
        icon.setFill(iconColor.get());
        pane.setBackground(new Background(new BackgroundFill(backgroundPaint, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setBorder(new Border(new BorderStroke(borderPaint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
            new BorderWidths(borderWidth / PREFERRED_WIDTH * size))));
    }
}
