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

package com.tlcsdm.core.javafx.stage;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.Keys;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.core.javafx.util.PaintConvertUtil;
import com.tlcsdm.core.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

/**
 * 颜色提取器
 *
 * @author LeeWyatt
 */
public class ScreenColorPickerStage extends Stage {
    private final Pane rootPane;

    private final double screenScaleX;
    private final double screenScaleY;
    private final Pane controlsBox;
    private final WritableImage fxScreenImage;
    private final Robot robot;
    private final double fxScreenWidth;
    private final double fxScreenHeight;
    private ImageView previewImg;

    private final int previewSize = 60;
    private final double previewScale = 2;
    private Rectangle previewRect;
    private Label colorLabel;

    private final boolean hideMainStage;

    public ScreenColorPickerStage() {
        this.initOwner(FxApp.primaryStage);
        this.hideMainStage = Config.getBoolean(Keys.ScreenColorPickerHideWindow, true);
        if (hideMainStage) {
            // 如果设置TRANSPARENT样式时 {@code stage.initStyle(StageStyle.TRANSPARENT);}
            // 可以通过 FxApp.primaryStage.setIconified(true); 来隐藏窗口
            FxApp.primaryStage.setOpacity(0);
        }
        screenScaleX = Screen.getPrimary().getOutputScaleX();
        screenScaleY = Screen.getPrimary().getOutputScaleY();
        fxScreenWidth = Screen.getPrimary().getBounds().getWidth();
        fxScreenHeight = Screen.getPrimary().getBounds().getHeight();
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        int screenW = (int) (bounds.getWidth() * screenScaleX);
        int screenH = (int) (bounds.getHeight() * screenScaleY);

        robot = new Robot();
        fxScreenImage = robot.getScreenCapture(null, new Rectangle2D(0, 0, screenW, screenH), false);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(screenW);
        imageView.setFitHeight(screenH);
        imageView.setImage(fxScreenImage);

        this.setAlwaysOnTop(true);
        this.setFullScreen(true);
        this.setFullScreenExitHint("");

        controlsBox = createPreviewPane();
        rootPane = new Pane(imageView, controlsBox);
        rootPane.setMouseTransparent(false);

        showPreviewPane(robot.getMouseX(), robot.getMouseY());
        controlsBox.setVisible(true);
        rootPane.setOnMouseMoved(event -> {
            showPreviewPane(event.getX(), event.getY());
            event.consume();
        });
        rootPane.setOnMouseClicked(event -> {
            Color color = fxScreenImage.getPixelReader().getColor((int) (event.getX() * screenScaleX),
                (int) (event.getY() * screenScaleY));
            OSUtil.writeToClipboard(PaintConvertUtil.convertPaintToCss(color));
            endPickColor();
        });

        Scene scene = new Scene(rootPane, screenW, screenH);
        scene.getStylesheets().add(Objects.requireNonNull(
            getClass().getResource("/com/tlcsdm/core/static/javafx/stage/screenshot-stage.css")).toExternalForm());
        this.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        // scene.setCursor(new ImageCursor(new
        // javafx.scene.image.Image(getClass().getResource("/images/color-cursor.png").toExternalForm())));
        this.initStyle(StageStyle.TRANSPARENT);
        // rootPane.setVisible(false);

        // 退出全屏, 结束截屏
        this.fullScreenProperty().addListener((ob, ov, nv) -> {
            if (!nv) {
                endPickColor();
            }
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                endPickColor();
            }
        });

        // 失去焦点,结束截屏
        this.focusedProperty().addListener((ob, ov, nv) -> {
            if (!nv) {
                endPickColor();
            }
        });

    }

    private Pane createPreviewPane() {
        previewImg = new ImageView();
        previewImg.setImage(fxScreenImage);
        previewImg.setFitHeight(previewSize);
        previewImg.setFitWidth(previewSize);
        Line hr = new Line(0, previewSize / 2.0, previewSize, previewSize / 2.0);
        Line vr = new Line(previewSize / 2.0, 0, previewSize / 2.0, previewSize);
        StackPane stackPane = new StackPane(previewImg, hr, vr);
        stackPane.getStyleClass().add("preview-pane");
        BorderPane bp = new BorderPane();

        colorLabel = new Label("#000000");
        colorLabel.setTextFill(Color.WHITE);
        colorLabel.setGraphicTextGap(6);
        previewRect = new Rectangle(8, 8);
        previewRect.setStroke(Color.BLACK);
        previewRect.setStrokeWidth(0.5);
        colorLabel.setGraphic(previewRect);
        colorLabel.setPrefSize(previewSize + 4, 10);
        colorLabel.setAlignment(Pos.CENTER_LEFT);
        colorLabel.setPadding(new Insets(0, 0, 0, 8));
        colorLabel.setStyle("-fx-font-size: 6;");
        Label tipLabel = new Label(I18nUtils.get("core.menubar.setting.colorPicker.tips"));
        tipLabel.setTextFill(colorLabel.getTextFill());
        tipLabel.setFont(colorLabel.getFont());
        tipLabel.setStyle("-fx-font-size: 6;");
        tipLabel.setAlignment(Pos.CENTER);
        tipLabel.setPrefSize(previewSize + 4, 18);
        tipLabel.setWrapText(true);
        VBox box = new VBox(colorLabel, tipLabel);
        box.setAlignment(Pos.CENTER);
        box.setBackground(new Background(new BackgroundFill(Color.web("#333333"), null, null)));
        BorderPane.setAlignment(box, Pos.CENTER);
        bp.setCenter(stackPane);
        bp.setBottom(box);
        bp.setScaleX(previewScale);
        bp.setScaleY(previewScale);
        bp.setVisible(false);
        return bp;
    }

    private void endPickColor() {
        rootPane.setVisible(false);
        if (hideMainStage) {
            FxApp.primaryStage.setOpacity(1);
        }
        setAlwaysOnTop(false);
        this.hide();
    }

    public void showStage() {
        this.show();
        this.toFront();
        rootPane.setVisible(true);
    }

    private void showPreviewPane(double x, double y) {
        Color color = fxScreenImage.getPixelReader().getColor((int) (x * screenScaleX), (int) (y * screenScaleY));
        colorLabel.setText(PaintConvertUtil.convertPaintToCss(color).toUpperCase());
        previewRect.setFill(color);
        previewImg.setViewport(new Rectangle2D(x * screenScaleX - previewSize / 2.0,
            y * screenScaleY - previewSize / 2.0, previewSize, previewSize));
        double boxW = controlsBox.getWidth() * previewScale;
        double layoutX = x + 50;
        double boxH = controlsBox.getHeight() * previewScale;
        double layoutY = y + 70;
        if (layoutX > fxScreenWidth - boxW) {
            layoutX = x - 100;
        }
        if (layoutY > fxScreenHeight - boxH) {
            layoutY = y - 130;
        }
        controlsBox.setLayoutX(layoutX);
        controlsBox.setLayoutY(layoutY);
    }
}
