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

/**
 * Copyright (c) 2019, 2021, ControlsFX
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.tlcsdm.core.javafx.control.skin;

import com.tlcsdm.core.javafx.control.DecorationComboBox;
import com.tlcsdm.core.javafx.control.Severity;
import javafx.beans.InvalidationListener;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Objects;

/**
 * @author unknowIfGuestInDream
 */
public class DecorationComboBoxSkin<T> extends ComboBoxListViewSkin<T> {

    private static final Image errorImage = new Image(Objects.requireNonNull(
        DecorationTextfieldSkin.class.getResource("/com/tlcsdm/core/static/graphic/error_ov.png")).toExternalForm());
    private static final Image warningImage = new Image(Objects.requireNonNull(
        DecorationTextfieldSkin.class.getResource("/com/tlcsdm/core/static/graphic/warning_ov.png")).toExternalForm());
    private static final Image infoImage = new Image(Objects.requireNonNull(
            DecorationTextfieldSkin.class.getResource("/com/tlcsdm/core/static/graphic/message_info.png"))
        .toExternalForm());

    private final DecorationComboBox<T> control;
    private ImageView decoration;
    private Tooltip tooltip;
    private StackPane arrowButton;

    public DecorationComboBoxSkin(DecorationComboBox<T> comboBox) {
        super(comboBox);
        this.control = comboBox;
        init();
        getChildren().add(decoration);
        registerChangeListener(control.offsetXProperty(), e -> getSkinnable().requestLayout());
        registerChangeListener(control.offsetYProperty(), e -> getSkinnable().requestLayout());
        decoration.setImage(getGraphicBySeverity(control.getSeverity()));
        refreshStyle(control.getSeverity());
        registerChangeListener(control.severityProperty(), e -> {
            decoration.setImage(getGraphicBySeverity(control.getSeverity()));
            refreshStyle(control.getSeverity());
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
        arrowButton = (StackPane) getChildren().get(0);
    }

    private final InvalidationListener tooltipListener = o -> {
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

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        final double fullHeight = h + snappedTopInset() + snappedBottomInset();
        final double rightWidth = getSkinnable().snapSizeX(decoration.getFitWidth());
        final double arrowWidth = getSkinnable().snapSizeX(arrowButton.getWidth());
        final double rightStartX = w + 5 - rightWidth + snappedLeftInset() - arrowWidth + control.offsetXProperty()
            .get();
        final double rightStartY = 3 + control.offsetYProperty().get();
        decoration.resizeRelocate(rightStartX, rightStartY, rightWidth, fullHeight);
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
                control.setStyle("-fx-text-inner-color: red;");
                tooltip.setStyle("-fx-background: FBEFEF;-fx-background-color: FBEFEF;-fx-text-fill: cc0033;");
                break;
            case WARNING:
                control.setStyle("-fx-text-inner-color: red;");
                tooltip.setStyle("-fx-background: FFFFCC;-fx-background-color: FFFFCC; -fx-text-fill: CC9900;");
                break;
            case INFO:
                control.setStyle("-fx-text-inner-color: black;");
                tooltip.setStyle("-fx-background: c4d0ef;-fx-background-color: c4d0ef; -fx-text-fill: black;");
                break;
            default:
                control.setStyle("-fx-text-inner-color: black;");
        }
    }

    @Override
    public void dispose() {
        tooltip.textProperty().removeListener(tooltipListener);
        unregisterChangeListeners(control.tooltipMsgProperty());
        unregisterChangeListeners(control.severityProperty());
        unregisterChangeListeners(control.offsetXProperty());
        unregisterChangeListeners(control.offsetYProperty());
        tooltip = null;
        decoration = null;
        super.dispose();
    }

}
