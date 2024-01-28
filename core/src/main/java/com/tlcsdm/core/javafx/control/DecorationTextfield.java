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

import com.tlcsdm.core.javafx.control.skin.CustomTextFieldSkin;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Objects;

/**
 * Use {@link CustomTextField} and overwrite layoutChildren#rightStartX(double, double, double, double) in {@link CustomTextFieldSkin}
 *
 * @author unknowIfGuestInDream
 */
public class DecorationTextfield extends StackPane {

    private TextField textField;
    private ImageView decoration;
    private Tooltip tooltip;
    private Pos pos;
    private double xOffset;
    private double yOffset;
    /**
     * Because {@link #decoration} is unmanaged, we need this listener
     * to detect when its Parent lays out so that we can lay out {@code decoration}.
     */
    private ChangeListener<Boolean> targetNeedsLayoutListener;

    private static final Image errorImage = new Image(
        Objects.requireNonNull(DecorationTextfield.class.getResource("/com/tlcsdm/core/static/graphic/error_ov.png"))
            .toExternalForm());
    private static final Image warningImage = new Image(
        Objects.requireNonNull(DecorationTextfield.class.getResource("/com/tlcsdm/core/static/graphic/warning_ov.png"))
            .toExternalForm());
    private static final Image infoImage = new Image(Objects.requireNonNull(
        DecorationTextfield.class.getResource("/com/tlcsdm/core/static/graphic/message_info.png")).toExternalForm());

    public DecorationTextfield() {
        init();
        getStyleClass().setAll("decoration-text-field");
        registerListeners();
        layoutChildren();
    }

    private void init() {
        textField = new TextField();
        textField.setFocusTraversable(false);
        decoration = new ImageView();
        decoration.setFocusTraversable(false);
        decoration.setFitHeight(16);
        decoration.setPreserveRatio(true);
        decoration.setManaged(false);
        decoration.setImage(null);
        decoration.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        this.pos = Pos.CENTER_RIGHT;
        this.xOffset = 10;
        this.yOffset = 0;
        targetNeedsLayoutListener = (__, ___, ____) -> layoutGraphic();
        textField.needsLayoutProperty().addListener(targetNeedsLayoutListener);
        tooltip = new Tooltip();
        tooltip.setOpacity(0.9);
        tooltip.setAutoFix(true);
        Tooltip.install(decoration, tooltip);
        // Make DecorationPane transparent
        setBackground(null);
        getChildren().addAll(textField, decoration);
        layoutChildren();
    }

    private void registerListeners() {
        //button.setOnMousePressed(e -> handleControlPropertyChanged("BUTTON_PRESSED"));
    }

    public TextField getTextField() {
        return textField;
    }

    public ImageView getDecoration() {
        return decoration;
    }

    private void layoutGraphic() {
        // Because we made decoration unmanaged, we are responsible for sizing it:
        this.decoration.autosize();
        // Now get decoration's layout Bounds and use for its position computations:
        Bounds decorationNodeLayoutBounds = this.decoration.getLayoutBounds();
        double decorationNodeWidth = decorationNodeLayoutBounds.getWidth();
        double decorationNodeHeight = decorationNodeLayoutBounds.getHeight();
        Bounds targetBounds = textField.getLayoutBounds();
        double x = targetBounds.getMinX();
        double y = targetBounds.getMinY();
        double targetWidth = targetBounds.getWidth();
        if (targetWidth <= 0.0) {
            targetWidth = textField.prefWidth(-1.0);
        }

        double targetHeight = targetBounds.getHeight();
        if (targetHeight <= 0.0) {
            targetHeight = textField.prefHeight(-1.0);
        }

        switch (this.pos.getHpos()) {
            case CENTER:
                x += targetWidth / 2.0 - decorationNodeWidth / 2.0;
                break;
            case LEFT:
                x -= decorationNodeWidth / 2.0;
                break;
            case RIGHT:
                x += targetWidth - decorationNodeWidth / 2.0;
        }

        switch (this.pos.getVpos()) {
            case CENTER:
                y += targetHeight / 2.0 - decorationNodeHeight / 2.0;
                break;
            case TOP:
                y -= decorationNodeHeight / 2.0;
                break;
            case BOTTOM:
                y += targetHeight - decorationNodeHeight / 2.0;
                break;
            case BASELINE:
                y += textField.getBaselineOffset() - this.decoration.getBaselineOffset() - decorationNodeHeight / 2.0;
        }

        this.decoration.setLayoutX(x + this.xOffset);
        this.decoration.setLayoutY(y + this.yOffset);
    }

    /**
     * Defines severity of messages.
     */
    public enum Severity {
        OK, INFO, WARNING, ERROR
    }

    public void setDecoration(Severity severity, String message) {
        decoration.setImage(getGraphicBySeverity(severity));
        tooltip.setText(message);
    }

    private Image getGraphicBySeverity(Severity severity) {
        return switch (severity) {
            case ERROR -> errorImage;
            case WARNING -> warningImage;
            case INFO -> infoImage;
            default -> null;
        };
    }
}
