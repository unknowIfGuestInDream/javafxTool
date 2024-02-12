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

package com.tlcsdm.core.javafx.control.skin;

import com.tlcsdm.core.javafx.control.DecorationTitlePane;
import com.tlcsdm.core.javafx.control.Severity;
import javafx.beans.InvalidationListener;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.TitledPaneSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Objects;

/**
 * @author unknowIfGuestInDream
 */
public class DecorationTitlePaneSkin extends TitledPaneSkin {

    private static final Image errorImage = new Image(
        Objects.requireNonNull(
                DecorationTitlePaneSkin.class.getResource("/com/tlcsdm/core/static/graphic/error_ov.png"))
            .toExternalForm());
    private static final Image warningImage = new Image(
        Objects.requireNonNull(
                DecorationTitlePaneSkin.class.getResource("/com/tlcsdm/core/static/graphic/warning_ov.png"))
            .toExternalForm());
    private static final Image infoImage = new Image(Objects.requireNonNull(
            DecorationTitlePaneSkin.class.getResource("/com/tlcsdm/core/static/graphic/message_info.png"))
        .toExternalForm());

    private ImageView decoration;
    private Tooltip tooltip;
    private DecorationTitlePane control;

    public DecorationTitlePaneSkin(final DecorationTitlePane control) {
        super(control);
        this.control = control;
        init();
        decoration.setImage(getGraphicBySeverity(control.getSeverity()));
        registerChangeListener(control.severityProperty(), e -> {
            decoration.setImage(getGraphicBySeverity(control.getSeverity()));
        });
        tooltip.setText(control.getTooltipMsg());
        registerChangeListener(control.tooltipMsgProperty(), e -> {
            tooltip.setText(control.getTooltipMsg());
        });
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
        tooltip.textProperty().addListener(tooltipListener);
        tooltip.setFont(Font.font(12));
        control.setGraphic(decoration);
        control.setContentDisplay(ContentDisplay.RIGHT);
    }

    private InvalidationListener tooltipListener = o -> {
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
    };

    private Image getGraphicBySeverity(Severity severity) {
        return switch (severity) {
            case ERROR -> errorImage;
            case WARNING -> warningImage;
            case INFO -> infoImage;
            default -> null;
        };
    }

    @Override
    public void dispose() {
        tooltip.textProperty().removeListener(tooltipListener);
        unregisterChangeListeners(control.tooltipMsgProperty());
        unregisterChangeListeners(control.severityProperty());
        tooltip = null;
        decoration = null;
        control = null;
        getChildren().clear();
        super.dispose();
    }
}
