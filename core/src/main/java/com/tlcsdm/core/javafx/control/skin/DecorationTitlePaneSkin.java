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
import javafx.css.PseudoClass;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.TitledPaneSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Objects;

/**
 * @author unknowIfGuestInDream
 */
public class DecorationTitlePaneSkin extends TitledPaneSkin {

    private static final PseudoClass HAS_NO_SIDE_NODE = PseudoClass.getPseudoClass("no-side-nodes");
    private static final PseudoClass HAS_LEFT_NODE = PseudoClass.getPseudoClass("left-node-visible");
    private static final PseudoClass HAS_RIGHT_NODE = PseudoClass.getPseudoClass("right-node-visible");

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

    private Node left;
    private StackPane leftPane;
    private Node right;
    private StackPane rightPane;
    private ImageView decoration;
    private Tooltip tooltip;
    private DecorationTitlePane control;

    public DecorationTitlePaneSkin(final DecorationTitlePane control) {
        super(control);
        this.control = control;
        init();
        //updateChildren();
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
        // control.setRight(decoration);
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

    //    private void updateChildren() {
    //        Node newLeft = control.leftProperty().get();
    //        // Remove leftPane in any case
    //        getChildren().remove(leftPane);
    //        if (newLeft != null) {
    //            leftPane = new StackPane(newLeft);
    //            leftPane.setManaged(false);
    //            leftPane.setAlignment(Pos.CENTER_LEFT);
    //            leftPane.getStyleClass().add("left-pane");
    //            getChildren().add(leftPane);
    //            left = newLeft;
    //        } else {
    //            leftPane = null;
    //            left = null;
    //        }
    //
    //        Node newRight = control.rightProperty().get();
    //        // Remove rightPane in anycase
    //        getChildren().remove(rightPane);
    //        if (newRight != null) {
    //            rightPane = new StackPane(newRight);
    //            rightPane.setManaged(false);
    //            rightPane.setAlignment(Pos.CENTER_RIGHT);
    //            rightPane.getStyleClass().add("right-pane");
    //            getChildren().add(rightPane);
    //            right = newRight;
    //        } else {
    //            rightPane = null;
    //            right = null;
    //        }
    //
    //        control.pseudoClassStateChanged(HAS_LEFT_NODE, left != null);
    //        control.pseudoClassStateChanged(HAS_RIGHT_NODE, right != null);
    //        control.pseudoClassStateChanged(HAS_NO_SIDE_NODE, left == null && right == null);
    //    }

    //    @Override
    //    protected void layoutChildren(double x, double y, double w, double h) {
    //        final double fullHeight = h + snappedTopInset() + snappedBottomInset();
    //
    //        final double leftWidth = leftPane == null ? 0.0 : getSkinnable().snapSizeX(leftPane.prefWidth(fullHeight));
    //        final double rightWidth = rightPane == null ? 0.0 : getSkinnable().snapSizeX(rightPane.prefWidth(fullHeight));
    //
    //        final double textFieldStartX = getSkinnable().snapPositionX(x) + getSkinnable().snapSizeX(leftWidth);
    //        final double textFieldWidth = w - getSkinnable().snapSizeX(leftWidth) - getSkinnable().snapSizeX(rightWidth);
    //        super.layoutChildren(textFieldStartX, 0, textFieldWidth, fullHeight);
    //
    //        if (leftPane != null) {
    //            final double leftStartX = 0 + control.offsetXProperty().get();
    //            final double leftStartY = 0 + control.offsetYProperty().get();
    //            leftPane.resizeRelocate(leftStartX, leftStartY, leftWidth, fullHeight);
    //        }
    //
    //        if (rightPane != null) {
    //            final double rightStartX = w - rightWidth + snappedLeftInset() + control.offsetXProperty().get();
    //            final double rightStartY = 0 + control.offsetYProperty().get();
    //            rightPane.resizeRelocate(rightStartX, rightStartY, rightWidth, fullHeight);
    //        }
    //    }

    @Override
    protected double computePrefWidth(double h, double topInset, double rightInset, double bottomInset,
        double leftInset) {
        final double pw = super.computePrefWidth(h, topInset, rightInset, bottomInset, leftInset);
        final double leftWidth = leftPane == null ? 0.0 : snapSizeX(leftPane.prefWidth(h));
        final double rightWidth = rightPane == null ? 0.0 : snapSizeX(rightPane.prefWidth(h));

        return pw + leftWidth + rightWidth;
    }

    @Override
    protected double computePrefHeight(double w, double topInset, double rightInset, double bottomInset,
        double leftInset) {
        final double ph = super.computePrefHeight(w, topInset, rightInset, bottomInset, leftInset);
        final double leftHeight = leftPane == null ? 0.0 : snapSizeX(leftPane.prefHeight(-1));
        final double rightHeight = rightPane == null ? 0.0 : snapSizeX(rightPane.prefHeight(-1));

        return Math.max(ph, Math.max(leftHeight, rightHeight));
    }

    @Override
    protected double computeMinWidth(double h, double topInset, double rightInset, double bottomInset,
        double leftInset) {
        final double mw = super.computeMinWidth(h, topInset, rightInset, bottomInset, leftInset);
        final double leftWidth = leftPane == null ? 0.0 : snapSizeX(leftPane.minWidth(h));
        final double rightWidth = rightPane == null ? 0.0 : snapSizeX(rightPane.minWidth(h));

        return mw + leftWidth + rightWidth;
    }

    @Override
    protected double computeMinHeight(double w, double topInset, double rightInset, double bottomInset,
        double leftInset) {
        final double mh = super.computeMinHeight(w, topInset, rightInset, bottomInset, leftInset);
        final double leftHeight = leftPane == null ? 0.0 : snapSizeX(leftPane.minHeight(-1));
        final double rightHeight = rightPane == null ? 0.0 : snapSizeX(rightPane.minHeight(-1));

        return Math.max(mh, Math.max(leftHeight, rightHeight));
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
                tooltip.setStyle("-fx-font-size: 12;-fx-background-color: FBEFEF;-fx-text-fill: cc0033;");
                break;
            case WARNING:
                control.setStyle("-fx-text-inner-color: red;");
                tooltip.setStyle("-fx-font-size: 12;-fx-background-color: FFFFCC; -fx-text-fill: CC9900;");
                break;
            case INFO:
                control.setStyle("-fx-text-inner-color: black;");
                tooltip.setStyle("-fx-font-size: 12;-fx-background-color: c4d0ef; -fx-text-fill: FFFFFF;");
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
        tooltip = null;
        decoration = null;
        control = null;
        getChildren().clear();
        super.dispose();
    }
}
