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

import com.tlcsdm.core.javafx.control.skin.ZoomImageViewSkin;
import com.tlcsdm.core.javafx.control.skin.ZoomImageViewSkin2;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;

import java.util.Objects;

/**
 * @author unknowIfGuestInDream
 */
@DefaultProperty("image")
public class ZoomImageView extends Control {

    public ZoomImageView() {
        super();
        getStyleClass().add("zoom-view");
        setFocusTraversable(false);
        getStylesheets().add(getUserAgentStylesheet());
    }

    public ZoomImageView(Image image) {
        this();
        setImage(image);
    }

    /**
     * A flag used to control whether the view will include a toolbar with zoom, search, rotation
     * controls.
     */
    private final BooleanProperty showToolBar = new SimpleBooleanProperty(this, "showToolBar", true);

    public final boolean isShowToolBar() {
        return showToolBar.get();
    }

    public final BooleanProperty showToolBarProperty() {
        return showToolBar;
    }

    public final void setShowToolBar(boolean showToolBar) {
        this.showToolBar.set(showToolBar);
    }

    /**
     * Sets the upper bounds for zoom operations. The default value is "4".
     */
    private final DoubleProperty maxZoomFactor = new SimpleDoubleProperty(this, "maxZoomFactor", 20);

    public final double getMaxZoomFactor() {
        return maxZoomFactor.get();
    }

    public final DoubleProperty maxZoomFactorProperty() {
        return maxZoomFactor;
    }

    public final void setMaxZoomFactor(double maxZoomFactor) {
        this.maxZoomFactor.set(maxZoomFactor);
    }

    /**
     * Sets the upper bounds for zoom operations. The default value is "4".
     */
    private final DoubleProperty minZoomFactor = new SimpleDoubleProperty(this, "minZoomFactor", 0.5);

    public final double getMinZoomFactor() {
        return minZoomFactor.get();
    }

    public final DoubleProperty minZoomFactorProperty() {
        return minZoomFactor;
    }

    public final void setMinZoomFactor(double minZoomFactor) {
        this.minZoomFactor.set(minZoomFactor);
    }

    /**
     * The current zoom factor. The default value is "1".
     */
    private final DoubleProperty zoomFactor = new SimpleDoubleProperty(this, "zoomFactor", 1);

    public final double getZoomFactor() {
        return zoomFactor.get();
    }

    public final DoubleProperty zoomFactorProperty() {
        return zoomFactor;
    }

    public final void setZoomFactor(double zoomFactor) {
        this.zoomFactor.set(zoomFactor);
    }

    private final ObjectProperty<Image> image = new SimpleObjectProperty<>(this, "image");

    public final ObjectProperty<Image> imageProperty() {
        return image;
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    public Image getImage() {
        return image.get();
    }

    /**
     * A flag that controls whether we always want to show the entire page. If "true" then the page
     * will be constantly resized to fit the viewport of the scroll pane in which it is showing. In
     * this mode zooming is not possible.
     */
    private final BooleanProperty showAll = new SimpleBooleanProperty(this, "showAll", false);

    public final boolean isShowAll() {
        return showAll.get();
    }

    public final BooleanProperty showAllProperty() {
        return showAll;
    }

    public final void setShowAll(boolean showAll) {
        this.showAll.set(showAll);
    }

    /**
     * The page rotation in degrees. Supported values are only "0", "90", "180", "270", "360", ...
     * multiples of "90".
     */
    private final DoubleProperty pageRotation = new SimpleDoubleProperty(this, "pageRotation", 0) {
        @Override
        public void set(double newValue) {
            super.set(newValue % 360d);
        }
    };

    public final double getPageRotation() {
        return pageRotation.get();
    }

    public final DoubleProperty pageRotationProperty() {
        return pageRotation;
    }

    public final void setPageRotation(double pageRotation) {
        this.pageRotation.set(pageRotation);
    }

    /**
     * Convenience method to rotate the generated image by -90 degrees.
     */
    public final void rotateLeft() {
        setPageRotation(getPageRotation() - 90);
    }

    /**
     * Convenience method to rotate the generated image by +90 degrees.
     */
    public final void rotateRight() {
        setPageRotation(getPageRotation() + 90);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ZoomImageViewSkin2(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(
                DecorationTitlePane.class.getResource("/com/tlcsdm/core/static/javafx/control/zoomimageview.css"))
            .toExternalForm();
    }
}
