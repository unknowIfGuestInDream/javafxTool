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

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.stream.IntStream;

/**
 * A JavaFX Demo of Image Zoom, Drag & Rotation.
 */
public final class PlutoExplorerExtended extends Application {

    private static final double HALF = 0.5d;
    /**
     * This is the number of Zoom-In operations required to
     * <b><i>almost exactly</i></b>
     * halve the size of the Viewport.
     */
    private static final int ZOOM_N = 9; // TODO try.: 1 <= ZOOM_N <= 20"-ish"
    /**
     * This factor guarantees that after
     * {@link  #ZOOM_N}
     * times Zoom-In, the Viewport-size halves
     * <b><i>almost exactly</i></b>.<br>
     * (HALF was chosen to - perhaps? - avoid excessive Image degradation when zooming)<br>
     * For ZOOM_N = 9 the factor value is approximately 93%
     */
    private static final double ZOOM_IN_SCALE = Math.pow(HALF, 1.0d / ZOOM_N);

    /**
     * Enumeration of the possible Zoom actions.
     */
    private static enum Zoom {
        /**
         * If Shift was pressed while
         * Scrolling, the action
         * will be ignored
         */
        ZOOM_NONE(0, Double.NaN),
        /**
         * = Mouse Scroll Up
         */
        ZOOM_IN(1, ZOOM_IN_SCALE),
        /**
         * = Mouse Scroll Down
         */
        ZOOM_OUT(-1, 1.0d / ZOOM_IN_SCALE);

        /**
         * 0, 1 or -1
         */
        private final int zoomLevelDelta;
        /**
         * Zoom-Factor
         */
        private final double scale;

        private Zoom(final int zoomLevelDelta, final double scale) {
            this.zoomLevelDelta = zoomLevelDelta;
            this.scale = scale;
        }

        private static Zoom of(final ScrollEvent scrollEvent) {

            final double scrollAmount = scrollEvent.getDeltaY();

            if (scrollAmount == 0) {
                return ZOOM_NONE;
            }
            if (scrollAmount > 0) {
                return ZOOM_IN;
            } else {
                return ZOOM_OUT;
            }
        }
    }

    /**
     * @see ZoomDragPane#ZoomDragPane(Image, double, double)
     */
    private static final class ZoomDragPane extends Pane {

        private static final double MIN_PX = 10;

        private int zoomLevel = 0;
        private final ImageView view;
        private final double imageWidth;
        private final double imageHeight;
        private final double rotation90scale;

        /**
         * Create a
         * {@link  Pane}
         * container for an
         * {@link  ImageView}
         * which encapsulates all the Zoom, Drag & Rotation logic for an Image.
         *
         * @param image      the Image to be displayed
         * @param prefWidth  the desired width  of the Pane
         * @param prefHeight the desired height of the Pane
         */
        private ZoomDragPane(final Image image, final double prefWidth, final double prefHeight) {

            this.setPrefSize(prefWidth, prefHeight);
            this.setStyle("-fx-background-color: LIGHTGREY");

            this.view = new ImageView(image);
            this.view.setPreserveRatio(true);
            this.view.setSmooth(true);
            this.view.setCache(true);

            this.getChildren().add(this.view);

            this.imageWidth = image.getWidth();
            this.imageHeight = image.getHeight();

            this.view.setViewport(new Rectangle2D(0, 0, this.imageWidth, this.imageHeight));

            /*
             * Unless its square, the Image must be scaled when rotated through 90 (or 270) degrees...
             */
            this.rotation90scale = Math.min(this.imageWidth, this.imageHeight) / Math.max(this.imageWidth,
                this.imageHeight);

            this.view.fitWidthProperty().bind(this.widthProperty());
            this.view.fitHeightProperty().bind(this.heightProperty());

            setMouseDraggedEventHandler();

            this.view.setOnScroll(e -> zoom(e));
        }

        /**
         * Drag the Viewport as the Mouse is moved.
         */
        private void setMouseDraggedEventHandler() {

            final ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();
            /*
             * Remember where the Mouse was in the Image when it was pressed...
             */
            this.view.setOnMousePressed(e -> mouseDown.set(imageViewToImage(e.getX(), e.getY())));
            /*
             * Using the above, work out how far the Mouse has been dragged & adjust the Viewport...
             */
            this.view.setOnMouseDragged(e -> {
                final Point2D dragPoint = imageViewToImage(e.getX(), e.getY());
                final Point2D dragDelta = dragPoint.subtract(mouseDown.get());

                final Rectangle2D viewport = this.view.getViewport();

                final double newX = viewport.getMinX() - dragDelta.getX();
                final double newY = viewport.getMinY() - dragDelta.getY();

                setImageViewport(newX, newY, viewport.getWidth(), viewport.getHeight());
            });
        }

        /**
         * Zoom Event-Handler. Zooms In or Out exactly 1 Level (if at all).
         * <p>
         * Note.: the X-/Y-Coordinates returned by the ScrollEvent are relative-to-the-ImageView
         * and need to be normalised to relative-to-the-Image for the Zoom & Viewport calculations.
         *
         * @param scrollEvent
         */
        private void zoom(final ScrollEvent scrollEvent) {

            final Zoom zoom = Zoom.of(scrollEvent);
            final int zoomLevelTry = this.zoomLevel + zoom.zoomLevelDelta;
            /*
             * Zoomed out too far or no Zoom at all? Then there's nothing to do...
             */
            if (zoomLevelTry < 0
                || zoom == Zoom.ZOOM_NONE) {
                return;
            }
            /*
             * Calculate the Viewport Size for the desired Zoom-Level...
             */
            final Dimension2D newSize = zoomCalculateViewportSize(zoomLevelTry);
            /*
             * If the maximum Zoom-Level has been exceeded there's nothing to do...
             */
            if (Math.min(newSize.getWidth(), newSize.getHeight()) < MIN_PX) {
                return;
            }
            /* --------------------------------------------------------------
             *
             * OK, the new Zoom-Level is valid:
             * -> calculate the new Viewport X-/Y-coordinates & update the Viewport...
             * (we Zoom in or out centred around the Pixel at the Mouse position)
             */
            this.zoomLevel = zoomLevelTry;

            final Point2D mouseInImage = imageViewToImage(scrollEvent.getX(), scrollEvent.getY());

            final Point2D newLocation = zoomCalculateNewViewportXY(mouseInImage, zoom.scale);
            /*
             * Store the new Coordinates & Size in the Viewport...
             */
            setImageViewport(newLocation.getX(), newLocation.getY(), newSize.getWidth(), newSize.getHeight());
        }

        /**
         * To fix the Pixel @ the Mouse X-coordinate, the following is true:
         * <br>
         * {@code  (x - newViewportMinX) / (x - currentViewportMinX) = scale}
         * <p>
         * The new Viewport X-coordinate is therefore given by:
         * <br>
         * {@code  newViewportMinX = x - (x - currentViewportMinX) * scale}
         * <p>
         * The new Viewport Y-coordinate is calculated similarly.
         *
         * @param imageMouse the Mouse coordinates relative to the Image
         * @param scale      the Zoom-factor
         * @return X-/Y-coordinate of the new Viewport<br>
         * (which
         * {@link  #setImageViewport(double, double, double, double)}
         * will bring into Range if necessary)
         */
        private Point2D zoomCalculateNewViewportXY(final Point2D imageMouse, final double scale) {

            final Rectangle2D oldViewport = this.view.getViewport();

            final double mouseX = imageMouse.getX();
            final double mouseY = imageMouse.getY();

            final double newX = mouseX - (mouseX - oldViewport.getMinX()) * scale;
            final double newY = mouseY - (mouseY - oldViewport.getMinY()) * scale;

            return new Point2D(newX, newY);
        }

        /**
         * Calculate the Viewport size for a particular
         * {@code  zoomLevel}.
         *
         * @param zoomLevel the Zoom Level
         * @return
         */
        private Dimension2D zoomCalculateViewportSize(final int zoomLevel) {

            final double zoomScale = Math.pow(ZOOM_IN_SCALE, zoomLevel);

            final double newWidth = this.imageWidth * zoomScale;
            final double newHeight = this.imageHeight * zoomScale;

            return new Dimension2D(newWidth, newHeight);
        }

        /**
         * Zoom in to the requested
         * {@code  zoomLevel}.<br>
         * (the Viewport will be centred within the Image)
         *
         * @param zoomLevel
         */
        private void zoomInCentredToLevel(final int zoomLevel) {

            this.zoomLevel = zoomLevel;
            /*
             * Calculate the Viewport Size for the desired Zoom-Level...
             */
            final Dimension2D newSize = zoomCalculateViewportSize(zoomLevel);

            final double newX = (this.imageWidth - newSize.getWidth()) / 2;
            final double newY = (this.imageHeight - newSize.getHeight()) / 2;

            setImageViewport(newX, newY, newSize.getWidth(), newSize.getHeight());
        }

        /**
         * Calculate Mouse coordinates within the Image based on coordinates within the ImageView.
         *
         * @param viewX X-coordinate of the Mouse within the ImageView
         * @param viewY Y-coordinate of the Mouse within the ImageView
         * @return Coordinates of the Mouse within the Image
         */
        private Point2D imageViewToImage(final double viewX, final double viewY) {

            final Bounds boundsLocal = this.view.getBoundsInLocal();

            final double xProportion = viewX / boundsLocal.getWidth();
            final double yProportion = viewY / boundsLocal.getHeight();

            final Rectangle2D viewport = this.view.getViewport();

            final double imageX = viewport.getMinX() + xProportion * viewport.getWidth();
            final double imageY = viewport.getMinY() + yProportion * viewport.getHeight();

            return new Point2D(imageX, imageY);
        }

        /**
         * Store the new Coordinates & Size in the Viewport.<br>
         * (making sure that the Viewport remains within the Image)
         *
         * @param x
         * @param y
         * @param width
         * @param height
         * @see ImageView#setViewport(Rectangle2D)
         */
        private void setImageViewport(final double x, final double y, final double width, final double height) {

            final double xMax = this.imageWidth - width;
            final double yMax = this.imageHeight - height;

            final double xClamp = Math.max(0, Math.min(x, xMax)); // 0 <= x <= xMax
            final double yClamp = Math.max(0, Math.min(y, yMax)); // 0 <= y <= yMax

            this.view.setViewport(new Rectangle2D(xClamp, yClamp, width, height));
        }

        /**
         * The Image will either be rotated or the rotation will be reset to zero.
         * <p>
         * Note:
         * this rotation logic was conceived for multiples of 90 degrees.<br>
         * It can, however, handle any angle, but the rotation90scale logic would need a touch of Pythagoras.
         *
         * @param relativeRotation the rotation angle, in Degrees (0 = reset to zero)<br>
         */
        private void rotateOrReset(final double relativeRotation) {

            final double rotatePrevious = this.view.getRotate();

            if (relativeRotation == 0) {
                if (rotatePrevious != 0) {
                    rotateAndScale(rotatePrevious, 0);
                }
            } else {
                rotateAndScale(rotatePrevious, rotatePrevious + relativeRotation);
            }
        }

        private void rotateAndScale(final double previousRotation, final double absoluteRotation) {

            this.view.setRotate(absoluteRotation);

            if (this.imageWidth == this.imageHeight) {
                return;
            }

            final boolean multiple180previous = previousRotation % 180 == 0;
            final boolean multiple180new = absoluteRotation % 180 == 0;

            if (multiple180new == multiple180previous) {
                return;
            }
            if (multiple180new) {
                this.view.setScaleX(1.0d);
                this.view.setScaleY(1.0d);
            } else {
                this.view.setScaleX(rotation90scale);
                this.view.setScaleY(rotation90scale);
            }
        }
    }

    private static final String TITLE = "Pluto Explorer";
    private static final String IMAGE_CREDIT_URL = "http://www.nasa.gov/image-feature/global-mosaic-of-pluto-in-true-color";
    private static final String IMAGE_URL = "https://www.nasa.gov/wp-content/uploads/2023/03/global-mosaic-of-pluto-in-true-color.jpg";

    @Override
    public void start(final Stage primaryStage) {

        final Image image = new Image(IMAGE_URL);
        final double paneWidth = image.getWidth();

        final ZoomDragPane pane = new ZoomDragPane(image, paneWidth, paneWidth * image.getHeight() / image.getWidth());

        final Hyperlink link = new Hyperlink("Image Credit: NASA/JHUAPL/SwRI");
        ;
        link.setOnAction(e -> getHostServices().showDocument(IMAGE_CREDIT_URL));
        ;
        link.setPadding(new Insets(10));
        ;
        link.setTooltip(new Tooltip(IMAGE_CREDIT_URL));

        final HBox buttons = createButtons(pane);

        final VBox root = new VBox(link, pane, buttons);
        ;
        root.setFillWidth(true);

        VBox.setVgrow(pane, Priority.ALWAYS);

        installTooltip(pane);

        pane.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                final Alert alert = new Alert(AlertType.INFORMATION);
                ;
                alert.setTitle(TITLE);
                ;
                alert.setHeaderText("Double-Click Dialogue");
                ;
                alert.setContentText("Press OK or Enter/Return");
                ;
                alert.showAndWait();
            }
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle(TITLE);
        primaryStage.show();
    }

    /**
     * Installs a once-only Tooltip centred over the Image.
     *
     * @param zoomDragPane
     */
    private static void installTooltip(final ZoomDragPane zoomDragPane) {
        /*
         * Implementation note.:
         * hijacking the setOnShowing(...) method & performing a show(...) there
         * seems to be the only way of accurately positioning a Tooltip.
         * Even though the Tooltip is only displayed once, the setOnShowing(...) method
         * is invoked 3 times, each time with a different Tooltip size,
         * the last of which seems to be final & is (apart from the 18 Pixel discrepancy) correct.
         */
        final Tooltip toolTip = new Tooltip("Scroll to zoom, drag to pan");
        ;
        toolTip.setOpacity(0.75);
        ;
        toolTip.setShowDelay(Duration.seconds(0.5));
        ;
        toolTip.setShowDuration(Duration.seconds(9));
        ;
        toolTip.setOnShown(e -> Tooltip.uninstall(zoomDragPane, toolTip));
        ;
        toolTip.setOnShowing(e -> {
            final Node view = zoomDragPane.view;
            final Bounds viewBoundsLocal = view.getBoundsInLocal();
            final Bounds viewBounds = view.localToScreen(viewBoundsLocal);

            final int toolTipFudge = 18; // Fudge because Tooltip claims to be larger than it really is

            final double toolTipWidth = toolTip.getWidth() - toolTipFudge;
            final double toolTipHeight = toolTip.getHeight() - toolTipFudge;

            final double toolTipOffsetX = (viewBounds.getWidth() - toolTipWidth) / 2;
            final double toolTipOffsetY = (viewBounds.getHeight() - toolTipHeight) / 2;

            toolTip.show(zoomDragPane, viewBounds.getMinX() + toolTipOffsetX, viewBounds.getMinY() + toolTipOffsetY);
        });
        Tooltip.install(zoomDragPane, toolTip);
    }

    /**
     * Creates a Bar with Zoom Buttons & Rotation CheckBox.
     *
     * @param zoomDragPane
     * @return
     */
    private static HBox createButtons(final ZoomDragPane zoomDragPane) {

        final char DEGREE = '\u00B0';

        final CheckBox rotate = new CheckBox("Rotate 90" + DEGREE + " (Toggle)");
        ;
        rotate.setOnAction(e -> zoomDragPane.rotateOrReset(rotate.isSelected() ? 90 : 0));
        ;
        rotate.setPadding(new Insets(0, 0, 0, 50));

        final HBox toolBox = new HBox(10);
        ;
        toolBox.setAlignment(Pos.CENTER);
        ;
        toolBox.setPadding(new Insets(10));

        IntStream.range(0, 3).forEach(n -> {
            final Button zoom = new Button("Zoom " + ((int) Math.pow(2, n)) + "00%");
            ;
            zoom.setOnAction(e -> zoomDragPane.zoomInCentredToLevel(ZOOM_N * n));
            toolBox.getChildren().add(zoom);
        });
        toolBox.getChildren().add(rotate);

        return toolBox;
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
