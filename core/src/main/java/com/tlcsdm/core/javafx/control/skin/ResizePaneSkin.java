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

import com.tlcsdm.core.javafx.control.ResizePane;
import javafx.geometry.HPos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * @author unknowIfGuestInDream
 */
public class ResizePaneSkin extends SkinBase<ResizePane> {

    private Content content;
    private Handle handle;
    // package-private to avoid synthetic accessor
    @SuppressWarnings("WeakerAccess")
    Side side;

    public ResizePaneSkin(final ResizePane resizePane) {
        super(resizePane);

        side = resizePane.getSide();

        handle = new Handle();
        content = new Content(resizePane.getContent());
        getChildren().setAll(content, handle);

        initHandleMouseHandlers();
        initChangeListeners(resizePane);
    }

    private void initHandleMouseHandlers() {
        handle.setOnMousePressed(event -> {
            handle.setInitialPos(content.getSize());
            handle.setPressPos(side.isHorizontal() ? event.getSceneY() : event.getSceneX());
            event.consume();
        });

        handle.setOnMouseDragged(event -> {
            final double delta = (side.isHorizontal() ? event.getSceneY() : event.getSceneX()) - handle.getPressPos();
            switch (side) {
                case BOTTOM:
                case RIGHT:
                    content.setSize(handle.getInitialPos() + delta);
                    getSkinnable().requestLayout();
                    break;
                case TOP:
                case LEFT:
                    content.setSize(handle.getInitialPos() - delta);
                    getSkinnable().requestLayout();
                    break;
                default:
                    throw new IllegalStateException("Unsupported side: " + side);
            }
            event.consume();
        });
    }

    private void initChangeListeners(final ResizePane resizePane) {
        registerChangeListener(resizePane.contentProperty(), value -> {
            content = new Content(getSkinnable().getContent());
            getChildren().setAll(content, handle);
            getSkinnable().requestLayout();
        });

        registerChangeListener(resizePane.sideProperty(), value -> {
            side = getSkinnable().getSide();
            handle.setGrabberStyle(side);
            getSkinnable().requestLayout();
        });

        registerChangeListener(resizePane.widthProperty(), value -> getSkinnable().requestLayout());
        registerChangeListener(resizePane.heightProperty(), value -> getSkinnable().requestLayout());
    }

    @Override
    protected void layoutChildren(final double contentX, final double contentY,
        final double width, final double height) {

        final double paddingX = snappedLeftInset();
        final double paddingY = snappedTopInset();
        final double handleSize = handle.prefWidth(-1);
        final double spacing = getSkinnable().getSpacing();

        if (side.isHorizontal()) {
            handle.resize(width, handleSize);
        } else {
            handle.resize(handleSize, height);
        }

        switch (side) {
            case BOTTOM:
                getSkinnable().resize(width, content.getSize() + handleSize);
                content.setClipSize(width, content.getSize());
                layoutInArea(content, paddingX, paddingY, width, content.getSize());
                positionInArea(handle, paddingX, paddingY + content.getSize() + spacing, width, handleSize);
                break;
            case TOP:
                content.setClipSize(width, height - handleSize);
                layoutInArea(content, paddingX, paddingY + handleSize + spacing, width, content.getSize());
                positionInArea(handle, paddingX, paddingY, width, handleSize);
                break;
            case LEFT:
                content.setClipSize(width - handleSize, height);
                layoutInArea(content, paddingX + handleSize + spacing, paddingY, content.getSize(), height);
                positionInArea(handle, paddingX, paddingY, handleSize, height);
                break;
            case RIGHT:
                content.setClipSize(width - handleSize, height);
                layoutInArea(content, paddingX, paddingY, content.getSize(), height);
                positionInArea(handle, paddingX + content.getSize() + spacing, paddingY, handleSize, height);
                break;
            default:
                throw new IllegalStateException("Unsupported side: " + side);
        }
    }

    private void positionInArea(final Node child, final double areaX, final double areaY,
        final double areaWidth, final double areaHeight) {
        positionInArea(child, areaX, areaY, areaWidth, areaHeight, 0, HPos.LEFT, VPos.TOP);
    }

    private void layoutInArea(final Node child, final double areaX, final double areaY,
        final double areaWidth, final double areaHeight) {
        layoutInArea(child, areaX, areaY, areaWidth, areaHeight, 0, HPos.LEFT, VPos.TOP);
    }

    @Override
    protected double computeMinWidth(final double height, final double topInset, final double rightInset,
        final double bottomInset, final double leftInset) {
        return leftInset + rightInset + (side.isVertical()
            ? content.getSize() + handle.prefWidth(-1) + getSkinnable().getSpacing()
            : content.minWidth(-1));
    }

    @Override
    protected double computeMinHeight(final double width, final double topInset, final double rightInset,
        final double bottomInset, final double leftInset) {
        return topInset + bottomInset + (side.isHorizontal()
            ? content.getSize() + handle.prefWidth(-1) + getSkinnable().getSpacing()
            : content.minHeight(-1));
    }

    @Override
    protected double computePrefWidth(final double height, final double topInset, final double rightInset,
        final double bottomInset, final double leftInset) {
        return leftInset + rightInset + (side.isVertical()
            ? content.getSize() + handle.prefWidth(-1) + getSkinnable().getSpacing()
            : content.prefWidth(-1));
    }

    @Override
    protected double computePrefHeight(final double width, final double topInset, final double rightInset,
        final double bottomInset, final double leftInset) {
        return topInset + bottomInset + (side.isHorizontal()
            ? content.getSize() + handle.prefWidth(-1) + getSkinnable().getSpacing()
            : content.prefHeight(-1));
    }

    @Override
    protected double computeMaxWidth(final double height, final double topInset, final double rightInset,
        final double bottomInset, final double leftInset) {
        return leftInset + rightInset + content.maxWidth(-1) +
            (side.isVertical() ? handle.prefWidth(-1) + getSkinnable().getSpacing() : 0);
    }

    @Override
    protected double computeMaxHeight(final double width, final double topInset, final double rightInset,
        final double bottomInset, final double leftInset) {
        return topInset + bottomInset + content.maxHeight(-1) +
            (side.isHorizontal() ? handle.prefWidth(-1) + getSkinnable().getSpacing() : 0);
    }

    class Handle extends StackPane {

        private double initialPos;
        private double pressPos;
        private StackPane grabber;

        Handle() {
            getStyleClass().setAll("resize-pane-handle");

            grabber = new StackPane() {

                @Override
                protected double computeMinWidth(final double height) {
                    return computePrefWidth(height);
                }

                @Override
                protected double computeMinHeight(final double width) {
                    return computePrefHeight(width);
                }

                @Override
                protected double computePrefWidth(final double height) {
                    return snappedLeftInset() + snappedRightInset();
                }

                @Override
                protected double computePrefHeight(final double width) {
                    return snappedTopInset() + snappedBottomInset();
                }

                @Override
                protected double computeMaxWidth(final double height) {
                    return computePrefWidth(height);
                }

                @Override
                protected double computeMaxHeight(final double width) {
                    return computePrefHeight(width);
                }
            };
            grabber.getStyleClass().setAll("resize-pane-grabber");
            setGrabberStyle(side);
            getChildren().add(grabber);
        }

        final void setGrabberStyle(final Side side) {
            if (side.isHorizontal()) {
                setCursor(Cursor.V_RESIZE);
            } else {
                setCursor(Cursor.H_RESIZE);
            }
        }

        @Override
        protected double computeMinWidth(final double height) {
            return computePrefWidth(height);
        }

        @Override
        protected double computeMinHeight(final double width) {
            return computePrefHeight(width);
        }

        @Override
        protected double computePrefWidth(final double height) {
            return snappedLeftInset() + snappedRightInset();
        }

        @Override
        protected double computePrefHeight(final double width) {
            return snappedTopInset() + snappedBottomInset();
        }

        @Override
        protected double computeMaxWidth(final double height) {
            return computePrefWidth(height);
        }

        @Override
        protected double computeMaxHeight(final double width) {
            return computePrefHeight(width);
        }

        @Override
        protected void layoutChildren() {
            final double grabberWidth = grabber.prefWidth(-1);
            final double grabberHeight = grabber.prefHeight(-1);
            grabber.resize(grabberWidth, grabberHeight);

            switch (side) {
                case BOTTOM:
                    positionInArea(grabber, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.TOP);
                    break;
                case TOP:
                    positionInArea(grabber, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.BOTTOM);
                    break;
                case LEFT:
                    positionInArea(grabber, 0, 0, getWidth(), getHeight(), 0, HPos.RIGHT, VPos.CENTER);
                    break;
                case RIGHT:
                    positionInArea(grabber, 0, 0, getWidth(), getHeight(), 0, HPos.LEFT, VPos.CENTER);
                    break;
            }
        }

        double getInitialPos() {
            return initialPos;
        }

        void setInitialPos(final double initialPos) {
            this.initialPos = initialPos;
        }

        double getPressPos() {
            return pressPos;
        }

        void setPressPos(final double pressPos) {
            this.pressPos = pressPos;
        }
    }

    class Content extends StackPane {

        private Node content;
        private Rectangle clipRect;
        private double size = 0;

        Content(final Node node) {
            if (node != null) {
                this.content = node;
                this.size = side.isVertical()
                    ? Math.max(content.prefWidth(-1), content.minWidth(-1))
                    : Math.max(content.prefHeight(-1), content.minHeight(-1));
                getChildren().add(node);
            }
            this.clipRect = new Rectangle();
            setClip(clipRect);
        }

        void setClipSize(final double w, final double h) {
            clipRect.setWidth(w);
            clipRect.setHeight(h);
        }

        double getSize() {
            return size;
        }

        void setSize(final double size) {
            this.size = Math.min(Math.max(size, getMinSize()), getMaxSize());
        }

        double getMinSize() {
            return content != null ? (side.isVertical() ? content.minWidth(-1) : content.minHeight(-1)) : 0;
        }

        double getMaxSize() {
            return content != null ? (side.isVertical() ? content.maxWidth(-1) : content.maxHeight(-1)) : 0;
        }

        @Override
        protected double computeMaxWidth(final double height) {
            return content != null ? snapSizeX(content.maxWidth(height)) : Double.MAX_VALUE;
        }

        @Override
        protected double computeMaxHeight(final double width) {
            return content != null ? snapSizeY(content.maxHeight(width)) : Double.MAX_VALUE;
        }
    }
}
