/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.ImageView;

public class FxImageView extends Control {

    public FxImageView(ImageView view) {
        super();
        getStyleClass().add("fx-image-view");
        setFocusTraversable(false);
        setImageView(view);
        zoomFactorProperty().addListener(it -> {
            if (getZoomFactor() < 1) {
                throw new IllegalArgumentException("zoom factor can not be smaller than 1");
            } else if (getZoomFactor() > getMaxZoomFactor()) {
                throw new IllegalArgumentException("zoom factor can not be larger than max zoom factor, but " + getZoomFactor() + " > " + getMaxZoomFactor());
            }
        });

        getStylesheets().add(getUserAgentStylesheet());
    }

    /**
     * Sets the upper bounds for zoom operations. The default value is "4".
     */
    private final DoubleProperty maxZoomFactor = new SimpleDoubleProperty(this, "maxZoomFactor", 4);

    public final double getMaxZoomFactor() {
        return maxZoomFactor.get();
    }

    public final DoubleProperty maxZoomFactorProperty() {
        return maxZoomFactor;
    }

    public final void setMaxZoomFactor(double maxZoomFactor) {
        this.maxZoomFactor.set(maxZoomFactor);
    }

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

    /**
     * The resolution / scale at which the thumbnails will be rendered. The default value is "1".
     */
    private final FloatProperty thumbnailPageScale = new SimpleFloatProperty(this, "thumbnailScale", 1f);

    public final float getThumbnailPageScale() {
        return thumbnailPageScale.get();
    }

    public final FloatProperty thumbnailPageScaleProperty() {
        return thumbnailPageScale;
    }

    public final void setThumbnailPageScale(float thumbnailPageScale) {
        this.thumbnailPageScale.set(thumbnailPageScale);
    }

    /**
     * The resolution / scale at which the main page will be rendered. The default value is "4".
     * The value has direct impact on the size of the images being generated and the memory requirements.
     * Keep low on low powered / low resolution systems and high on large systems with hires displays.
     */
    private final FloatProperty pageScale = new SimpleFloatProperty(this, "pageScale", 4f);

    public final float getPageScale() {
        return pageScale.get();
    }

    public final FloatProperty pageScaleProperty() {
        return pageScale;
    }

    public final void setPageScale(float pageScale) {
        this.pageScale.set(pageScale);
    }

    /**
     * The size used for the images displayed in the thumbnail view. The default value is "200".
     */
    private final DoubleProperty thumbnailSize = new SimpleDoubleProperty(this, "thumbnailSize", 200d);

    public final double getThumbnailSize() {
        return thumbnailSize.get();
    }

    public final DoubleProperty thumbnailSizeProperty() {
        return thumbnailSize;
    }

    public final void setThumbnailSize(double thumbnailSize) {
        this.thumbnailSize.set(thumbnailSize);
    }

    /**
     * The currently loaded and displayed PDF document.
     */
    private final ObjectProperty<ImageView> imageView = new SimpleObjectProperty<>(this, "imageView");

    public final ObjectProperty<ImageView> imageViewProperty() {
        return imageView;
    }

    public final ImageView getImageView() {
        return imageView.get();
    }

    public final void setImageView(ImageView image) {
        imageView.set(image);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FxImageViewSkin(this);
    }
//
//    @Override
//    public String getUserAgentStylesheet() {
//        return Objects.requireNonNull(PDFView.class.getResource("pdf-view.css")).toExternalForm();
//    }
//

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
     * Un-loads currently loaded document.
     */
    public final void reset() {
//        setDocument(null);
//        setSearchText(null);
//        setZoomFactor(1);
//        setRotate(0);
    }

}
