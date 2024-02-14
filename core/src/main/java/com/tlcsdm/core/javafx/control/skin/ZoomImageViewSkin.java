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
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author unknowIfGuestInDream
 */
public class ZoomImageViewSkin extends SkinBase<ZoomImageView> {

    private final MainAreaScrollPane mainArea;

    public ZoomImageViewSkin(ZoomImageView zoomView) {
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

        // zoom slider
        Slider zoomSlider = new Slider();
        zoomSlider.minProperty().bind(view.minZoomFactorProperty());
        zoomSlider.maxProperty().bind(view.maxZoomFactorProperty());
        zoomSlider.valueProperty().bindBidirectional(view.zoomFactorProperty());
        zoomSlider.disableProperty().bind(view.showAllProperty());

        Label zoomLabel = new Label("Zoom");
        zoomLabel.disableProperty().bind(view.showAllProperty());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // toolbar
        return new ToolBar(
            showAll,
            new Separator(Orientation.VERTICAL),
            zoomLabel,
            zoomSlider,
            new Separator(Orientation.VERTICAL),
            rotateLeft,
            rotateRight,
            spacer
        );
    }

    class MainAreaScrollPane extends ScrollPane {

        private final StackPane wrapper;
        private final Pane pane;
        private final Group group;
        private ImageView imageView;

        public MainAreaScrollPane() {
            ZoomImageView zoomView = getSkinnable();
            imageView = new ImageView();
            imageView.imageProperty().bind(zoomView.imageProperty());
            imageView.setPreserveRatio(true);
            setFitToWidth(true);
            setFitToHeight(true);
            setPannable(true);

            pane = new
                Pane() {
                    @Override
                    protected void layoutChildren() {
                        wrapper.resizeRelocate((getWidth() - wrapper.prefWidth(-1)) / 2,
                            (getHeight() - wrapper.prefHeight(-1)) / 2, wrapper.prefWidth(-1), wrapper.prefHeight(-1));
                    }
                };

            wrapper = new StackPane();
            wrapper.getStyleClass().add("image-view-wrapper");
            wrapper.setMaxWidth(Region.USE_PREF_SIZE);
            wrapper.setMaxHeight(Region.USE_PREF_SIZE);
            wrapper.rotateProperty().bind(zoomView.pageRotationProperty());
            wrapper.addEventHandler(ScrollEvent.SCROLL, evt -> {
                if (evt.isShortcutDown()) {
                    if (evt.getDeltaY() > 0) {
                        increaseZoomFactor(0.5);
                    } else {
                        decreaseZoomFactor(0.5);
                    }
                    evt.consume();
                }
            });
            group = new Group(wrapper);
            pane.getChildren().addAll(group);
            viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
                pane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                pane.setMinWidth(Region.USE_COMPUTED_SIZE);
                pane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                pane.setMinHeight(Region.USE_COMPUTED_SIZE);

                if (isPortrait()) {
                    double prefWidth = newBounds.getWidth() * zoomView.getZoomFactor() - 5;
                    pane.setPrefWidth(prefWidth);
                    pane.setMinWidth(prefWidth);

                    if (zoomView.isShowAll()) {
                        pane.setPrefHeight(newBounds.getHeight() - 5);
                    } else {
                        Image image = zoomView.getImage();
                        if (image != null) {
                            double scale = newBounds.getWidth() / image.getWidth();
                            double scaledImageHeight = image.getHeight() * scale;
                            double prefHeight = scaledImageHeight * zoomView.getZoomFactor();
                            pane.setPrefHeight(prefHeight);
                            pane.setMinHeight(prefHeight);
                        }
                    }
                } else {
                    /*
                     * Image has been rotated.
                     */
                    double prefHeight = newBounds.getHeight() * zoomView.getZoomFactor() - 5;
                    pane.setPrefHeight(prefHeight);
                    pane.setMinHeight(prefHeight);
                    if (zoomView.isShowAll()) {
                        pane.setPrefWidth(newBounds.getWidth() - 5);
                    } else {
                        Image image = zoomView.getImage();
                        if (image != null) {
                            double scale = newBounds.getHeight() / image.getWidth();
                            double scaledImageHeight = image.getHeight() * scale;
                            double prefWidth = scaledImageHeight * zoomView.getZoomFactor();
                            pane.setPrefWidth(prefWidth);
                            pane.setMinWidth(prefWidth);
                        }
                    }
                }
            });

            setContent(pane);
            zoomView.showAllProperty().addListener(it -> {
                updateScrollbarPolicies();
                layoutImage();
                requestLayout();
            });
            zoomView.pageRotationProperty().addListener(it -> {
                updateScrollbarPolicies();
                layoutImage();
            });
            zoomView.zoomFactorProperty().addListener(it -> {
                updateScrollbarPolicies();
                requestLayout();
            });
            updateScrollbarPolicies();
            layoutImage();
        }

        protected void layoutImage() {
            wrapper.getChildren().setAll(imageView);
            requestLayout();
            if (getSkinnable().isShowAll()) {
                fitAll(imageView);
            } else {
                fitWidth(imageView);
            }
        }

        private void fitWidth(ImageView imageView) {
            if (isPortrait()) {
                imageView.fitWidthProperty().bind(pane.widthProperty().subtract(40));
                imageView.fitHeightProperty().unbind();
            } else {
                imageView.fitWidthProperty().bind(pane.heightProperty().subtract(40));
                imageView.fitHeightProperty().unbind();
            }
        }

        private void fitAll(ImageView imageView) {
            if (isPortrait()) {
                imageView.fitWidthProperty().bind(pane.widthProperty().subtract(40));
                imageView.fitHeightProperty().bind(pane.heightProperty().subtract(40));
            } else {
                imageView.fitWidthProperty().bind(pane.heightProperty().subtract(40));
                imageView.fitHeightProperty().bind(pane.widthProperty().subtract(40));
            }
        }

        private void updateScrollbarPolicies() {
            if (getSkinnable().isShowAll()) {
                setVbarPolicy(ScrollBarPolicy.NEVER);
                setHbarPolicy(ScrollBarPolicy.NEVER);
            } else {
                if (getSkinnable().getZoomFactor() > 1) {
                    setVbarPolicy(ScrollBarPolicy.ALWAYS);
                    setHbarPolicy(ScrollBarPolicy.ALWAYS);
                } else {
                    if (isPortrait()) {
                        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
                        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
                    } else {
                        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
                        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
                    }
                }
            }
        }

        private boolean isPortrait() {
            return getSkinnable().getPageRotation() % 180 == 0;
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
        if (!zoomView.isShowAll()) {
            zoomView.setZoomFactor(Math.max(zoomView.getMinZoomFactor(), currentZoomFactor - delta));
        }
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
        if (!zoomView.isShowAll()) {
            zoomView.setZoomFactor(Math.min(zoomView.getMaxZoomFactor(), currentZoomFactor + delta));
        }
        return currentZoomFactor != zoomView.getZoomFactor();
    }
}
