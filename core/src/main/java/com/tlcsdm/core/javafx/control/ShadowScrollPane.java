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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

/**
 * Add a drop shadow to the top of the scroll pane.
 *
 * @author unknowIfGuestInDream
 * @date 2023/8/27 17:35
 */
public class ShadowScrollPane extends ScrollPane {

    private final Region shadow = new Region();

    public ShadowScrollPane() {
        super();
        init();
    }

    public ShadowScrollPane(Node content) {
        super(content);
        init();
    }

    private void init() {
        skinProperty().addListener(it -> getChildren().addAll(shadow));

        setFitToWidth(true);
        setVbarPolicy(ScrollBarPolicy.NEVER);
        setHbarPolicy(ScrollBarPolicy.NEVER);

        shadow.setManaged(false);
        shadow.setStyle("-fx-pref-height: 10;" +
            "-fx-background-color: black;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, .75), 20, 0.19, 0, 6);");
        shadow.getStyleClass().add("shadow");
        shadow.visibleProperty().bind(showShadowProperty());
        shadow.setMouseTransparent(true);
        shadow.visibleProperty().bind(vvalueProperty().greaterThan(0));

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

        vvalueProperty().addListener(it -> {
            if (lastOffset != computeOffset()) {
                requestLayout();
            }
        });
        showShadowProperty().addListener(it -> requestLayout());
    }

    private final BooleanProperty showShadow = new SimpleBooleanProperty(this, "showShadow", true);

    public final BooleanProperty showShadowProperty() {
        return showShadow;
    }

    public final boolean isShowShadow() {
        return showShadow.get();
    }

    public final void setShowShadow(boolean show) {
        showShadow.set(show);
    }

    private final int SHADOW_HEIGHT = 30;

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        if (isShowShadow()) {
            Insets insets = getInsets();
            double w = getWidth();
            double offset = computeOffset();
            shadow.resizeRelocate(-10, insets.getTop() - shadow.prefHeight(-1) - SHADOW_HEIGHT + offset, w + 20, shadow.prefHeight(-1) - 1);
            lastOffset = offset;
        }
    }

    private double lastOffset;

    private double computeOffset() {
        if (getContent() != null) {
            return Math.min(getVvalue() * getContent().prefHeight(-1), SHADOW_HEIGHT);
        }

        return 0;
    }
}
