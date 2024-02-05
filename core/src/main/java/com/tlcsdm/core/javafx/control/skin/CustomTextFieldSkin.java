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
 * Copyright (c) 2013, 2019 ControlsFX
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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.layout.StackPane;
import javafx.scene.text.HitInfo;

public abstract class CustomTextFieldSkin extends TextFieldSkin {

    private static final PseudoClass HAS_NO_SIDE_NODE = PseudoClass.getPseudoClass("no-side-nodes");
    private static final PseudoClass HAS_LEFT_NODE = PseudoClass.getPseudoClass("left-node-visible");
    private static final PseudoClass HAS_RIGHT_NODE = PseudoClass.getPseudoClass("right-node-visible");

    private Node left;
    private StackPane leftPane;
    private Node right;
    private StackPane rightPane;

    private final TextField control;

    public CustomTextFieldSkin(final TextField control) {
        super(control);

        this.control = control;
        updateChildren();

        registerChangeListener(leftProperty(), e -> updateChildren());
        registerChangeListener(rightProperty(), e -> updateChildren());
        registerChangeListener(offsetXProperty(), e -> getSkinnable().requestLayout());
        registerChangeListener(offsetYProperty(), e -> getSkinnable().requestLayout());
    }

    public abstract ObjectProperty<Node> leftProperty();

    public abstract ObjectProperty<Node> rightProperty();

    public abstract DoubleProperty offsetXProperty();

    public abstract DoubleProperty offsetYProperty();

    private void updateChildren() {
        Node newLeft = leftProperty().get();
        //Remove leftPane in any case
        getChildren().remove(leftPane);
        if (newLeft != null) {
            leftPane = new StackPane(newLeft);
            leftPane.setManaged(false);
            leftPane.setAlignment(Pos.CENTER_LEFT);
            leftPane.getStyleClass().add("left-pane");
            getChildren().add(leftPane);
            left = newLeft;
        } else {
            leftPane = null;
            left = null;
        }

        Node newRight = rightProperty().get();
        //Remove rightPane in anycase
        getChildren().remove(rightPane);
        if (newRight != null) {
            rightPane = new StackPane(newRight);
            rightPane.setManaged(false);
            rightPane.setAlignment(Pos.CENTER_RIGHT);
            rightPane.getStyleClass().add("right-pane");
            getChildren().add(rightPane);
            right = newRight;
        } else {
            rightPane = null;
            right = null;
        }

        control.pseudoClassStateChanged(HAS_LEFT_NODE, left != null);
        control.pseudoClassStateChanged(HAS_RIGHT_NODE, right != null);
        control.pseudoClassStateChanged(HAS_NO_SIDE_NODE, left == null && right == null);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        final double fullHeight = h + snappedTopInset() + snappedBottomInset();

        final double leftWidth = leftPane == null ? 0.0 : getSkinnable().snapSizeX(leftPane.prefWidth(fullHeight));
        final double rightWidth = rightPane == null ? 0.0 : getSkinnable().snapSizeX(rightPane.prefWidth(fullHeight));

        final double textFieldStartX = getSkinnable().snapPositionX(x) + getSkinnable().snapSizeX(leftWidth);
        final double textFieldWidth = w - getSkinnable().snapSizeX(leftWidth) - getSkinnable().snapSizeX(rightWidth);

        super.layoutChildren(textFieldStartX, 0, textFieldWidth, fullHeight);

        if (leftPane != null) {
            final double leftStartX = 0 + offsetXProperty().get();
            final double leftStartY = 0 + offsetYProperty().get();
            leftPane.resizeRelocate(leftStartX, leftStartY, leftWidth, fullHeight);
        }

        if (rightPane != null) {
            final double rightStartX = w - rightWidth + snappedLeftInset() + offsetXProperty().get();
            final double rightStartY = 0 + offsetYProperty().get();
            rightPane.resizeRelocate(rightStartX, rightStartY, rightWidth, fullHeight);
        }
    }

    @Override
    public HitInfo getIndex(double x, double y) {
        /*
          This resolves https://bitbucket.org/controlsfx/controlsfx/issue/476
          when we have a left Node and the click point is badly returned
          because we weren't considering the shift induced by the leftPane.
         */
        final double leftWidth = leftPane == null ? 0.0 : snapSizeX(leftPane.prefWidth(getSkinnable().getHeight()));
        return super.getIndex(x - leftWidth, y);
    }

    @Override
    protected double computePrefWidth(double h, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double pw = super.computePrefWidth(h, topInset, rightInset, bottomInset, leftInset);
        final double leftWidth = leftPane == null ? 0.0 : snapSizeX(leftPane.prefWidth(h));
        final double rightWidth = rightPane == null ? 0.0 : snapSizeX(rightPane.prefWidth(h));

        return pw + leftWidth + rightWidth;
    }

    @Override
    protected double computePrefHeight(double w, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double ph = super.computePrefHeight(w, topInset, rightInset, bottomInset, leftInset);
        final double leftHeight = leftPane == null ? 0.0 : snapSizeX(leftPane.prefHeight(-1));
        final double rightHeight = rightPane == null ? 0.0 : snapSizeX(rightPane.prefHeight(-1));

        return Math.max(ph, Math.max(leftHeight, rightHeight));
    }

    @Override
    protected double computeMinWidth(double h, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double mw = super.computeMinWidth(h, topInset, rightInset, bottomInset, leftInset);
        final double leftWidth = leftPane == null ? 0.0 : snapSizeX(leftPane.minWidth(h));
        final double rightWidth = rightPane == null ? 0.0 : snapSizeX(rightPane.minWidth(h));

        return mw + leftWidth + rightWidth;
    }

    @Override
    protected double computeMinHeight(double w, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double mh = super.computeMinHeight(w, topInset, rightInset, bottomInset, leftInset);
        final double leftHeight = leftPane == null ? 0.0 : snapSizeX(leftPane.minHeight(-1));
        final double rightHeight = rightPane == null ? 0.0 : snapSizeX(rightPane.minHeight(-1));

        return Math.max(mh, Math.max(leftHeight, rightHeight));
    }

    @Override
    public void dispose() {
        unregisterChangeListeners(leftProperty());
        unregisterChangeListeners(rightProperty());
        unregisterChangeListeners(offsetXProperty());
        unregisterChangeListeners(offsetYProperty());
        getChildren().clear();
        super.dispose();
    }
}
