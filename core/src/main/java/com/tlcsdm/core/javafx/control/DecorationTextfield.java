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

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;

/**
 * The DecorationTextfield control is simply a JavaFX {@link javafx.scene.control.TextField} control
 * with support for decoration.It supports the display of error, warning and
 * info images.
 *
 * <p>
 * The image is on the right side by default and is not displayed initially.
 * When {@link DecorationTextfield#setDecoration(Severity, String)} is called
 * and the parameter is {@link Severity#ERROR}, {@link Severity#WARNING} or
 * {@link Severity#INFO}, the image is displayed.
 *
 * <h3>Code Samples</h3>
 * <p>
 * If you want the image to be displayed outside the text box, you can use
 * {@link DecorationTextfield#setOffsetX} to achieve it.
 *
 * <pre>{@code
 * final DecorationTextfield text = new DecorationTextfield();
 * text.setOffsetX(25);
 * }</pre>
 *
 * @author unknowIfGuestInDream
 * @see CustomTextField
 * @see javafx.scene.control.TextField
 */
public class DecorationTextfield extends CustomTextField {
    private ImageView decoration;
    private Tooltip tooltip;

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
        getStyleClass().add("decoration-text-field");
    }

    private void init() {
        decoration = new ImageView();
        decoration.setFocusTraversable(false);
        decoration.setFitWidth(16);
        decoration.setFitHeight(16);
        decoration.setPreserveRatio(true);
        decoration.getStyleClass().add("decoration");
        tooltip = new Tooltip();
        tooltip.setAutoFix(true);
        tooltip.setShowDelay(new Duration(200.0D));
        tooltip.setOpacity(0.9);
        tooltip.setWrapText(true);
        tooltip.setShowDuration(new Duration(8000.0D));
        tooltip.getStyleClass().add("decoration-tooltip");
        tooltip.textProperty().addListener(o -> {
            boolean enableTooltip = decoration.getProperties().containsKey("javafx.scene.control.Tooltip");
            if (tooltip.getText() == null || tooltip.getText().isEmpty()) {
                if (enableTooltip) {
                    Tooltip.uninstall(decoration, tooltip);
                    decoration.setCursor(Cursor.DEFAULT);
                }
            } else {
                if (!enableTooltip) {
                    Tooltip.install(decoration, tooltip);
                    decoration.setCursor(Cursor.HAND);
                }
            }
        });
        setRight(decoration);
    }

    public void setDecoration(Severity severity, String message) {
        tooltip.setText(message);
        decoration.setImage(getGraphicBySeverity(severity));
        refreshStyle(severity);
    }

    public void setDecoration(Severity severity) {
        setDecoration(severity, null);
    }

    private Image getGraphicBySeverity(Severity severity) {
        return switch (severity) {
            case ERROR -> errorImage;
            case WARNING -> warningImage;
            case INFO -> infoImage;
            default -> null;
        };
    }

    private void refreshStyle(Severity severity) {
        switch (severity) {
            case ERROR:
                setStyle("-fx-text-inner-color: red;");
                tooltip.setStyle("-fx-font-size: 12;-fx-background-color: FBEFEF;-fx-text-fill: cc0033;");
                break;
            case WARNING:
                setStyle("-fx-text-inner-color: red;");
                tooltip.setStyle("-fx-font-size: 12;-fx-background-color: FFFFCC; -fx-text-fill: CC9900;");
                break;
            case INFO:
                setStyle("-fx-text-inner-color: black;");
                tooltip.setStyle("-fx-font-size: 12;-fx-background-color: c4d0ef; -fx-text-fill: FFFFFF;");
                break;
            default:
                setStyle("-fx-text-inner-color: black;");
        }
    }
}
