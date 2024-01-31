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

import java.util.Objects;

/**
 * @author unknowIfGuestInDream
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
        decoration.setFitHeight(16);
        decoration.setPreserveRatio(true);
        decoration.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        decoration.setCursor(Cursor.HAND);
        tooltip = new Tooltip();
        tooltip.setOpacity(0.9);
        tooltip.setAutoFix(true);
        // setOffsetX(25);
        Tooltip.install(decoration, tooltip);
        setRight(decoration);
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
