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

import com.tlcsdm.core.javafx.control.ZoomImageView;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * @author unknowIfGuestInDream
 */
public class ZoomImageViewSkin2 extends SkinBase<ZoomImageView> {

    private final MainAreaScrollPane mainArea;

    public ZoomImageViewSkin2(ZoomImageView zoomView) {
        super(zoomView);

        ToolBar toolBar = createToolBar(zoomView);
        toolBar.visibleProperty().bind(zoomView.showToolBarProperty());
        toolBar.managedProperty().bind(zoomView.showToolBarProperty());
        mainArea = new MainAreaScrollPane();
        VBox.setVgrow(mainArea, Priority.ALWAYS);

        VBox rightSide = new VBox(mainArea);
        rightSide.getStyleClass().add("main-area");
        rightSide.setFillWidth(true);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolBar);
        borderPane.setCenter(rightSide);
        borderPane.setFocusTraversable(false);

        getChildren().add(borderPane);
    }

    private ToolBar createToolBar(ZoomImageView zoomView) {
        ZoomImageView view = getSkinnable();

        // show all
        ToggleButton showAll = new ToggleButton();
        showAll.setGraphic(
            LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/core/static/graphic/fullscreen.png")));
        showAll.getStyleClass().addAll("tool-bar-button", "show-all-button");
        showAll.setTooltip(new Tooltip("Show all / whole page"));
        showAll.selectedProperty().bindBidirectional(zoomView.showAllProperty());

        // rotate buttons
        Button rotateLeft = new Button();
        rotateLeft.getStyleClass().addAll("tool-bar-button", "rotate-left");
        rotateLeft.setTooltip(new Tooltip("Rotate page left"));
        rotateLeft.setGraphic(
            LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/core/static/graphic/rotate-left.png")));
        rotateLeft.setOnAction(evt -> view.rotateLeft());

        Button rotateRight = new Button();
        rotateRight.getStyleClass().addAll("tool-bar-button", "rotate-right");
        rotateRight.setTooltip(new Tooltip("Rotate page right"));
        rotateRight.setGraphic(
            LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/core/static/graphic/rotate-right.png")));
        rotateRight.setOnAction(evt -> view.rotateRight());

        Button zoomIn = new Button();
        zoomIn.getStyleClass().addAll("tool-bar-button", "zoom-in");
        zoomIn.setTooltip(new Tooltip("Zoom in"));
        zoomIn.setGraphic(LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/core/static/graphic/zoom-in.png")));
        zoomIn.setOnAction(evt -> increaseZoomFactor(0.5));

        Button zoomOut = new Button();
        zoomOut.getStyleClass().addAll("tool-bar-button", "zoom-out");
        zoomOut.setTooltip(new Tooltip("Zoom out"));
        zoomOut.setGraphic(
            LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/core/static/graphic/zoom-out.png")));
        zoomOut.setOnAction(evt -> decreaseZoomFactor(0.5));

        Label zoomLabel = new Label("Zoom");
        zoomLabel.disableProperty().bind(view.showAllProperty());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // toolbar
        return new ToolBar(showAll, new Separator(Orientation.VERTICAL), zoomLabel, zoomIn, zoomOut,
            new Separator(Orientation.VERTICAL), rotateLeft, rotateRight, spacer);
    }

    class MainAreaScrollPane extends ScrollPane {

        private ImageView imageView;
        private ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        public MainAreaScrollPane() {
            AnchorPane.setLeftAnchor(this, 0.0);
            AnchorPane.setRightAnchor(this, 0.0);
            AnchorPane.setTopAnchor(this, 0.0);
            AnchorPane.setBottomAnchor(this, 0.0);

            ZoomImageView zoomView = getSkinnable();
            imageView = new ImageView();
            imageView.imageProperty().bind(zoomView.imageProperty());
            imageView.setPreserveRatio(true);
            imageView.setViewport(new Rectangle2D(0, 0, imageView.getImage().getWidth(), imageView.getImage().getHeight()));
            setFitToWidth(true);
            setFitToHeight(true);
            setPannable(true);
            this.setContent(imageView);
            imageView.rotateProperty().bind(zoomView.pageRotationProperty());
            imageView.addEventHandler(ScrollEvent.ANY, evt -> {
                if (evt.isShortcutDown()) {
                    if (evt.getDeltaY() > 0) {
                        increaseZoomFactor(0.5);
                    } else {
                        decreaseZoomFactor(0.5);
                    }
                    evt.consume();
                }
            });

            imageView.setOnMousePressed(e -> {

                Point2D mousePress = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
                mouseDown.set(mousePress);
            });

            imageView.setOnMouseDragged(e -> {
                Point2D dragPoint = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
                shift(imageView, dragPoint.subtract(mouseDown.get()));
                mouseDown.set(imageViewToImage(imageView, new Point2D(e.getX(), e.getY())));
            });


            zoomView.showAllProperty().addListener(it -> {
                requestLayout();
            });
            zoomView.pageRotationProperty().addListener(it -> {
            });
            zoomView.zoomFactorProperty().addListener(it -> {
                imageView.setFitHeight(imageView.getImage().getHeight() * (zoomView.zoomFactorProperty().get()));
                //updateScrollbarPolicies();
                requestLayout();
            });

            init();
        }

        private void init() {
            double width = imageView.getImage().getWidth();
            double height = imageView.getImage().getHeight();
            double imageRatio = width / height;
            double parentRatio = this.getWidth() / this.getHeight();

            if (imageRatio > parentRatio && width > this.getWidth()) {
                imageView.setFitWidth(this.getWidth());
            } else if (imageRatio < parentRatio && height > this.getHeight()) {
                imageView.setFitHeight(this.getHeight());
            }

        }
    }

    /**
     * Method to decrease the zoom factor for value specified as {@code delta}.
     *
     * @param delta zoom factor (decrease) delta
     * @return true if the operation actually did cause a zoom change
     */
    private boolean decreaseZoomFactor(double delta) {
        ZoomImageView zoomView = getSkinnable();
        double currentZoomFactor = zoomView.getZoomFactor();
        //if (!zoomView.isShowAll()) {
            zoomView.setZoomFactor(Math.max(zoomView.getMinZoomFactor(), currentZoomFactor - delta));
        //}
        return currentZoomFactor != zoomView.getZoomFactor();
    }

    /**
     * Method to increase the zoom factor for value specified as {@code delta}.
     *
     * @param delta zoom factor (increase) delta
     * @return true if the operation actually did cause a zoom change
     */
    private boolean increaseZoomFactor(double delta) {
        ZoomImageView zoomView = getSkinnable();
        double currentZoomFactor = zoomView.getZoomFactor();
        //if (!zoomView.isShowAll()) {
            zoomView.setZoomFactor(Math.min(zoomView.getMaxZoomFactor(), currentZoomFactor + delta));
       // }
        return currentZoomFactor != zoomView.getZoomFactor();
    }

    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(double value, double min, double max) {

        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
            viewport.getMinX() + xProportion * viewport.getWidth(),
            viewport.getMinY() + yProportion * viewport.getHeight());
    }
}
