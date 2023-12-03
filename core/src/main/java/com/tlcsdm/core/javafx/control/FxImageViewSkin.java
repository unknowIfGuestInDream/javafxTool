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

import com.dlsc.pdfviewfx.PDFView;
import com.dlsc.pdfviewfx.skins.PDFViewSkin;
import javafx.animation.ParallelTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FxImageViewSkin extends SkinBase<FxImageView> {

    // Access to PDF document must be single threaded (see Apache PdfBox website FAQs)
    private final Executor EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, PDFView.class.getSimpleName() + " Thread");
        thread.setDaemon(true);
        return thread;
    });

    private final ObservableList<Integer> pdfFilePages = FXCollections.observableArrayList();

    private final ListView<Integer> thumbnailListView = new ListView<>();

    private final ListView<PDFViewSkin.PageSearchResult> searchResultListView = new ListView<>();

    private final ImageView imageView;

    private final Map<Integer, Image> imageCache = new HashMap<>();

    protected FxImageViewSkin(FxImageView view) {
        super(view);
        this.imageView = view.getImageView();

//        view.imageViewProperty().addListener(it -> updatePagesList());
//        updatePagesList();
//
        ToolBar toolBar = createToolBar(view);
        toolBar.visibleProperty().bind(view.showToolBarProperty());
        toolBar.managedProperty().bind(view.showToolBarProperty());
//
//        HBox searchNavigator = createSearchNavigator();
//
//        PDFViewSkin.MainAreaScrollPane mainArea = new PDFViewSkin.MainAreaScrollPane();
//        VBox.setVgrow(mainArea, Priority.ALWAYS);
//
//        VBox rightSide = new VBox(searchNavigator, mainArea);
//        rightSide.getStyleClass().add("main-area");
//        rightSide.setFillWidth(true);
//
//        StackPane leftSide = new StackPane(thumbnailListView, searchResultListView);
//        leftSide.getStyleClass().add("tray");
//        leftSide.visibleProperty().bind(view.showThumbnailsProperty());
//        leftSide.managedProperty().bind(view.showThumbnailsProperty());
//
//        BorderPane borderPane = new BorderPane();
//        borderPane.setTop(toolBar);
//        borderPane.setLeft(leftSide);
//        borderPane.setCenter(rightSide);
//        borderPane.setFocusTraversable(false);
//
//        getChildren().add(borderPane);
//
//        view.documentProperty().addListener(it -> {
//            mainArea.setImage(null);
//            imageCache.clear();
//            view.setPage(-1);
//            view.setPage(0);
//        });
//
//        view.searchTextProperty().addListener(it -> search());
    }

    //    private <T> void maybeScrollTo(ListView<T> listView, T item) {
//        /*
//         * We want to make sure that the selected result will be visible within the list view,
//         * but we do not want to scroll every time the selected search result changes. We really
//         * only want to perform a scrolling if the newly selected search result is not within the
//         * currently visible rows of the list view.
//         */
//        VirtualFlow virtualFlow = (VirtualFlow) listView.lookup("VirtualFlow");
//        if (virtualFlow != null) {
//
//            IndexedCell firstVisibleCell = virtualFlow.getFirstVisibleCell();
//            IndexedCell lastVisibleCell = virtualFlow.getLastVisibleCell();
//
//            if (firstVisibleCell != null && lastVisibleCell != null) {
//
//                /*
//                 * Adding 1 to start and subtracting 1 from the end as the calculations of the
//                 * currently visible cells doesn't seem to work perfectly. Also, if only a fraction
//                 * of a cell is visible then it requires scrolling, too.
//                 */
//                int start = Math.max(0, firstVisibleCell.getIndex() + 1);
//                int end = Math.max(1, lastVisibleCell.getIndex() - 1);
//                int index = listView.getItems().indexOf(item);
//
//                if (index < start || index > end) {
//                    listView.scrollTo(item);
//                }
//            }
//        }
//    }
//
//    private PDFViewSkin.SearchService searchService;
//
//    private void search() {
//        if (searchService == null) {
//            searchService = new PDFViewSkin.SearchService();
//            searchService.setOnSucceeded(evt -> {
//                getSkinnable().getSearchResults().setAll(searchService.getValue());
//
//                if (!searchService.getValue().isEmpty()) {
//                    getSkinnable().setSelectedSearchResult(searchService.getValue().get(0));
//                } else {
//                    getSkinnable().setSelectedSearchResult(null);
//                }
//            });
//        }
//
//        searchService.restart();
//    }
//
//    class SearchService extends Service<List<PDFView.SearchResult>> {
//        @Override
//        protected Task<List<PDFView.SearchResult>> createTask() {
//            PDFView.Document document = getSkinnable().getDocument();
//
//            if (document instanceof PDFView.SearchableDocument) {
//                return new PDFViewSkin.SearchTask((PDFView.SearchableDocument) document, getSkinnable().getSearchText());
//            } else {
//                throw new PDFViewSkin.SearchService.SearchException("Document is not searchable.");
//            }
//        }
//
//        private class SearchException extends RuntimeException {
//            public SearchException(String message) {
//                super(message);
//            }
//        }
//    }
//
//    static class SearchTask extends Task<List<PDFView.SearchResult>> {
//
//        private final PDFView.SearchableDocument document;
//        private final String searchText;
//
//        public SearchTask(PDFView.SearchableDocument document, String searchText) {
//            this.document = document;
//            this.searchText = searchText;
//        }
//
//        @Override
//        protected List<PDFView.SearchResult> call() throws Exception {
//            if (StringUtils.isBlank(searchText)) {
//                return Collections.emptyList();
//            }
//
//            Thread.sleep(300);
//
//            if (isCancelled()) {
//                return Collections.emptyList();
//            }
//
//            return document.getSearchResults(searchText);
//        }
//    }
//
//    private final DoubleProperty requestedVValue = new SimpleDoubleProperty(-1);
//
    private ToolBar createToolBar(FxImageView fxImageView) {
        FxImageView view = getSkinnable();

        // show all
        ToggleButton showAll = new ToggleButton();
        //showAll.setGraphic(new FontIcon(MaterialDesign.MDI_FULLSCREEN));
        showAll.getStyleClass().addAll("tool-bar-button", "show-all-button");
        showAll.setTooltip(new Tooltip("Show all / whole page"));
        //showAll.selectedProperty().bindBidirectional(fxImageView.showAllProperty());

        // paging
        Button goLeft = new Button();
        //goLeft.setGraphic(new FontIcon(MaterialDesign.MDI_CHEVRON_LEFT));
        goLeft.setTooltip(new Tooltip("Show previous page"));
        //goLeft.setOnAction(evt -> view.gotoPreviousPage());
        goLeft.getStyleClass().addAll("tool-bar-button", "previous-page-button");
        //goLeft.disableProperty().bind(Bindings.createBooleanBinding(() -> view.getPage() <= 0, view.pageProperty(), view.documentProperty()));
        goLeft.setMaxHeight(Double.MAX_VALUE);

        Button goRight = new Button();
        //goRight.setGraphic(new FontIcon(MaterialDesign.MDI_CHEVRON_RIGHT));
        goRight.setTooltip(new Tooltip("Show next page"));
        //goRight.setOnAction(evt -> view.gotoNextPage());
        goRight.getStyleClass().addAll("tool-bar-button", "next-page-button");
        //goRight.disableProperty().bind(Bindings.createBooleanBinding(() -> view.getDocument() == null || view.getDocument().getNumberOfPages() <= view.getPage() + 1, view.pageProperty(), view.documentProperty()));
        goRight.setMaxHeight(Double.MAX_VALUE);

        // rotate buttons
        Button rotateLeft = new Button();
        rotateLeft.getStyleClass().addAll("tool-bar-button", "rotate-left");
        rotateLeft.setTooltip(new Tooltip("Rotate page left"));
        //rotateLeft.setGraphic(new FontIcon(MaterialDesign.MDI_ROTATE_LEFT));
        rotateLeft.setOnAction(evt -> view.rotateLeft());

        Button rotateRight = new Button();
        rotateRight.getStyleClass().addAll("tool-bar-button", "rotate-right");
        rotateRight.setTooltip(new Tooltip("Rotate page right"));
        //rotateRight.setGraphic(new FontIcon(MaterialDesign.MDI_ROTATE_RIGHT));
        rotateRight.setOnAction(evt -> view.rotateRight());

        // zoom slider
        Slider zoomSlider = new Slider();
        zoomSlider.setMin(1);
        zoomSlider.maxProperty().bind(view.maxZoomFactorProperty());
        zoomSlider.valueProperty().bindBidirectional(view.zoomFactorProperty());
        // zoomSlider.disableProperty().bind(view.showAllProperty());

        Label zoomLabel = new Label("Zoom");
        //zoomLabel.disableProperty().bind(view.showAllProperty());

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

    /**
     * Method to decrease the zoom factor for value specified as {@code delta}.
     *
     * @param delta zoom factor (decrease) delta
     * @return true if the operation actually did cause a zoom change
     */
    private boolean decreaseZoomFactor(double delta) {
        FxImageView pdfView = getSkinnable();
        double currentZoomFactor = pdfView.getZoomFactor();
//        if (!pdfView.isShowAll()) {
//            pdfView.setZoomFactor(Math.max(1, currentZoomFactor - delta));
//        }
        return currentZoomFactor != pdfView.getZoomFactor();
    }

    /**
     * Method to increase the zoom factor for value specified as {@code delta}.
     *
     * @param delta zoom factor (increase) delta
     * @return true if the operation actually did cause a zoom change
     */
    private boolean increaseZoomFactor(double delta) {
        FxImageView pdfView = getSkinnable();
        double currentZoomFactor = pdfView.getZoomFactor();
//        if (!pdfView.isShowAll()) {
//            pdfView.setZoomFactor(Math.min(pdfView.getMaxZoomFactor(), currentZoomFactor + delta));
//        }
        return currentZoomFactor != pdfView.getZoomFactor();
    }

//    class PagerService extends Service<Void> {
//        private boolean up;
//
//        public void setUp(boolean up) {
//            this.up = up;
//        }
//
//        @Override
//        protected Task<Void> createTask() {
//            return new PDFViewSkin.PagerTask(up);
//        }
//    }
//
//    class PagerTask extends Task<Void> {
//        private final boolean up;
//
//        public PagerTask(boolean up) {
//            this.up = up;
//        }
//
//        @Override
//        protected Void call() throws Exception {
//            Thread.sleep(100);
//            Platform.runLater(() -> {
//                if (up) {
//                    getSkinnable().gotoPreviousPage();
//                    requestedVValue.set(1);
//                } else {
//                    getSkinnable().gotoNextPage();
//                    requestedVValue.set(0);
//                }
//            });
//
//            return null;
//        }
//    }
//
//    private final PDFViewSkin.PagerService pagerService = new PDFViewSkin.PagerService();
//
//    class MainAreaScrollPane extends ScrollPane {
//
//        private final StackPane wrapper;
//        private final Pane pane;
//        private final Group group;
//        private final PDFViewSkin.RenderService mainAreaRenderService = new PDFViewSkin.RenderService(false);
//        private final Rectangle bouncer = new Rectangle();
//        private ImageView imageView;
//
//        public MainAreaScrollPane() {
//
//            FxImageView fxImageView = getSkinnable();
//
//            bouncer.getStyleClass().add("bouncer");
//            bouncer.setManaged(false);
//            bouncer.fillProperty().bind(fxImageView.searchResultColorProperty());
//            bouncer.visibleProperty().bind(fxImageView.selectedSearchResultProperty().isNotNull());
//
//            fxImageView.selectedSearchResultProperty().addListener(it -> bounceSearchResult());
//            fxImageView.getSearchResults().addListener((Observable it) -> mainAreaRenderService.restart());
//
//            mainAreaRenderService.setOnSucceeded(evt -> {
//                double vValue = requestedVValue.get();
//                if (vValue != -1) {
//                    setVvalue(vValue);
//                    requestedVValue.set(-1);
//                }
//            });
//
//            addEventHandler(KeyEvent.KEY_PRESSED, evt -> {
//                switch (evt.getCode()) {
//                    case UP:
//                    case LEFT:
//                    case PAGE_UP:
//                    case HOME:
//                        if (getVvalue() == 0 || fxImageView.isShowAll() || evt.getCode() == KeyCode.LEFT) {
//                            requestedVValue.set(1);
//                            fxImageView.gotoPreviousPage();
//                        }
//                        break;
//                    case DOWN:
//                    case RIGHT:
//                    case PAGE_DOWN:
//                    case END:
//                        if (getVvalue() == 1 || pdfView.isShowAll() || evt.getCode() == KeyCode.RIGHT) {
//                            requestedVValue.set(0);
//                            pdfView.gotoNextPage();
//                        }
//                        break;
//                }
//            });
//
//            addEventHandler(ScrollEvent.SCROLL, evt -> {
//                if (evt.isInertia()) {
//                    return;
//                }
//
//                boolean success;
//
//                if (evt.getDeltaY() > 0) {
//                    success = pdfView.getPage() > 0;
//                    pagerService.setUp(true);
//                } else {
//                    success = pdfView.getPage() < pdfView.getDocument().getNumberOfPages() - 1;
//                    pagerService.setUp(false);
//                }
//
//                if (success) {
//                    pagerService.restart();
//                    evt.consume();
//                }
//
//            });
//
//            setFitToWidth(true);
//            setFitToHeight(true);
//            setPannable(true);
//
//            pane = new
//                Pane() {
//                    @Override
//                    protected void layoutChildren() {
//                        wrapper.resizeRelocate((getWidth() - wrapper.prefWidth(-1)) / 2, (getHeight() - wrapper.prefHeight(-1)) / 2, wrapper.prefWidth(-1), wrapper.prefHeight(-1));
//                    }
//                };
//
//            wrapper = new StackPane() {
//                @Override
//                protected void layoutChildren() {
//                    super.layoutChildren();
//
//                    PDFView.SearchResult result = pdfView.getSelectedSearchResult();
//                    if (result != null) {
//
//                        Rectangle2D marker = result.getScaledMarker(pdfView.getPageScale() * pdfView.getZoomFactor());
//                        double scale = getWidth() / imageView.getImage().getWidth();
//
//                        if (marker != null) {
//                            bouncer.setLayoutX(marker.getMinX() * scale);
//                            bouncer.setLayoutY(marker.getMinY() * scale);
//                            bouncer.setWidth(marker.getWidth() * scale);
//                            bouncer.setHeight(marker.getHeight() * scale);
//                        }
//                    }
//
//                    Platform.runLater(() -> ensureVisible(bouncer));
//                }
//
//                private void ensureVisible(Node node) {
//                    Bounds viewport = getViewportBounds();
//
//                    double contentHeight = getContent().localToScene(getContent().getBoundsInLocal()).getHeight();
//                    double nodeMinY = node.localToScene(node.getBoundsInLocal()).getMinY();
//                    double nodeMaxY = node.localToScene(node.getBoundsInLocal()).getMaxY();
//
//                    double vValueCurrent = getVvalue();
//
//                    double vValueDelta = 0;
//
//                    if (nodeMaxY < Math.abs(viewport.getMinY())) {
//                        vValueDelta = (nodeMinY - viewport.getHeight()) / contentHeight;
//                    } else if (nodeMinY > viewport.getHeight() + viewport.getMinY()) {
//                        vValueDelta = (nodeMinY + viewport.getHeight()) / contentHeight;
//                    }
//
//                    setVvalue(vValueCurrent + vValueDelta);
//                }
//            };
//
//            fxImageView.selectedSearchResultProperty().addListener(it -> wrapper.requestLayout());
//
//            wrapper.getStyleClass().add("image-view-wrapper");
//            wrapper.setMaxWidth(Region.USE_PREF_SIZE);
//            wrapper.setMaxHeight(Region.USE_PREF_SIZE);
//            wrapper.rotateProperty().bind(fxImageView.pageRotationProperty());
//            wrapper.addEventHandler(ScrollEvent.SCROLL, evt -> {
//                if (evt.isShortcutDown()) {
//                    if (evt.getDeltaY() > 0) {
//                        increaseZoomFactor(0.5);
//                    } else {
//                        decreaseZoomFactor(0.5);
//                    }
//                    evt.consume();
//                }
//            });
//
//            group = new Group(wrapper);
//            pane.getChildren().addAll(group);
//
//            viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
//                pane.setPrefWidth(Region.USE_COMPUTED_SIZE);
//                pane.setMinWidth(Region.USE_COMPUTED_SIZE);
//
//                pane.setPrefHeight(Region.USE_COMPUTED_SIZE);
//                pane.setMinHeight(Region.USE_COMPUTED_SIZE);
//
//                if (isPortrait()) {
//
//                    double prefWidth = newBounds.getWidth() * fxImageView.getZoomFactor() - 5;
//                    pane.setPrefWidth(prefWidth);
//                    pane.setMinWidth(prefWidth);
//
//                    if (fxImageView.isShowAll()) {
//                        pane.setPrefHeight(newBounds.getHeight() - 5);
//                    } else {
//                        Image image = getImage();
//                        if (image != null) {
//                            double scale = newBounds.getWidth() / image.getWidth();
//                            double scaledImageHeight = image.getHeight() * scale;
//                            double prefHeight = scaledImageHeight * fxImageView.getZoomFactor();
//                            pane.setPrefHeight(prefHeight);
//                            pane.setMinHeight(prefHeight);
//                        }
//                    }
//
//                } else {
//
//                    /*
//                     * Image has been rotated.
//                     */
//
//                    double prefHeight = newBounds.getHeight() * fxImageView.getZoomFactor() - 5;
//                    pane.setPrefHeight(prefHeight);
//                    pane.setMinHeight(prefHeight);
//
//                    if (fxImageView.isShowAll()) {
//                        pane.setPrefWidth(newBounds.getWidth() - 5);
//                    } else {
//                        Image image = getImage();
//                        if (image != null) {
//                            double scale = newBounds.getHeight() / image.getWidth();
//                            double scaledImageHeight = image.getHeight() * scale;
//                            double prefWidth = scaledImageHeight * fxImageView.getZoomFactor();
//                            pane.setPrefWidth(prefWidth);
//                            pane.setMinWidth(prefWidth);
//                        }
//                    }
//
//                }
//            });
//
//            setContent(pane);
//
//            mainAreaRenderService.setExecutor(EXECUTOR);
//            mainAreaRenderService.scaleProperty().bind(pdfView.pageScaleProperty().multiply(pdfView.zoomFactorProperty()));
//            mainAreaRenderService.pageProperty().bind(pdfView.pageProperty());
//            mainAreaRenderService.valueProperty().addListener(it -> {
//                Image image = mainAreaRenderService.getValue();
//                if (image != null) {
//                    setImage(image);
//                }
//                wrapper.requestLayout(); // bouncer needs layout now
//            });
//
//            fxImageView.showAllProperty().addListener(it -> {
//                updateScrollbarPolicies();
//                layoutImage();
//                requestLayout();
//            });
//
//            fxImageView.pageRotationProperty().addListener(it -> {
//                updateScrollbarPolicies();
//                layoutImage();
//            });
//
//            fxImageView.zoomFactorProperty().addListener(it -> {
//                updateScrollbarPolicies();
//                requestLayout();
//            });
//
//            updateScrollbarPolicies();
//
//            layoutImage();
//        }

    private ParallelTransition parallel;

//        private void bounceSearchResult() {
//            if (parallel != null) {
//                parallel.stop();
//            }
//
////            PDFView.SearchResult selectedSearchResult = getSkinnable().getSelectedSearchResult();
////
////            if (selectedSearchResult == null) {
////                return;
////            }
//
//            final int SCALE_FACTOR = 3;
//            final int DURATION = 150;
//
//            ScaleTransition scaleUp = new ScaleTransition();
//            scaleUp.setDuration(Duration.millis(DURATION));
//            scaleUp.setFromX(1);
//            scaleUp.setFromY(1);
//            scaleUp.setToX(SCALE_FACTOR);
//            scaleUp.setToY(SCALE_FACTOR);
//            scaleUp.setNode(bouncer);
//
//            FadeTransition fadeIn = new FadeTransition();
//            fadeIn.setDuration(Duration.millis(DURATION));
//            fadeIn.setFromValue(0);
//            fadeIn.setToValue(0.7);
//            fadeIn.setNode(bouncer);
//
//            parallel = new ParallelTransition(scaleUp, fadeIn);
//            parallel.setOnFinished(evt -> {
//                ScaleTransition scaleDown = new ScaleTransition();
//                scaleDown.setDuration(Duration.millis(DURATION));
//                scaleDown.setFromX(SCALE_FACTOR);
//                scaleDown.setFromY(SCALE_FACTOR);
//                scaleDown.setToX(1);
//                scaleDown.setToY(1);
//                scaleDown.setNode(bouncer);
//
//                FadeTransition fadeOut = new FadeTransition();
//                fadeOut.setDuration(Duration.millis(DURATION));
//                fadeOut.setFromValue(0.7);
//                fadeOut.setToValue(0);
//                fadeOut.setNode(bouncer);
//
//                ParallelTransition parallel2 = new ParallelTransition(scaleDown, fadeOut);
//                parallel2.play();
//            });
//
//            parallel.play();
//        }

//
//        private final ObjectProperty<Image> image = new SimpleObjectProperty<>(this, "image");
//
//        private void setImage(Image image) {
//            this.image.set(image);
//        }
//
//        private Image getImage() {
//            return image.get();
//        }
//
//        protected void layoutImage() {
//            imageView = new ImageView();
//            imageView.imageProperty().bind(image);
//            imageView.setPreserveRatio(true);
//            wrapper.getChildren().setAll(imageView, bouncer);
//
//            requestLayout();
//
//            if (getSkinnable().isShowAll()) {
//                fitAll(imageView);
//            } else {
//                fitWidth(imageView);
//            }
//        }
//
//        private void fitWidth(ImageView imageView) {
//            if (isPortrait()) {
//                imageView.fitWidthProperty().bind(pane.widthProperty().subtract(40));
//                imageView.fitHeightProperty().unbind();
//            } else {
//                imageView.fitWidthProperty().bind(pane.heightProperty().subtract(40));
//                imageView.fitHeightProperty().unbind();
//            }
//        }
//
//        private void fitAll(ImageView imageView) {
//            if (isPortrait()) {
//                imageView.fitWidthProperty().bind(pane.widthProperty().subtract(40));
//                imageView.fitHeightProperty().bind(pane.heightProperty().subtract(40));
//            } else {
//                imageView.fitWidthProperty().bind(pane.heightProperty().subtract(40));
//                imageView.fitHeightProperty().bind(pane.widthProperty().subtract(40));
//            }
//        }
//
//        private void updateScrollbarPolicies() {
//            if (getSkinnable().isShowAll()) {
//                setVbarPolicy(ScrollBarPolicy.NEVER);
//                setHbarPolicy(ScrollBarPolicy.NEVER);
//            } else {
//                if (getSkinnable().getZoomFactor() > 1) {
//                    setVbarPolicy(ScrollBarPolicy.ALWAYS);
//                    setHbarPolicy(ScrollBarPolicy.ALWAYS);
//                } else {
//                    if (isPortrait()) {
//                        setVbarPolicy(ScrollBarPolicy.ALWAYS);
//                        setHbarPolicy(ScrollBarPolicy.NEVER);
//                    } else {
//                        setVbarPolicy(ScrollBarPolicy.NEVER);
//                        setHbarPolicy(ScrollBarPolicy.ALWAYS);
//                    }
//                }
//            }
//        }
//
//        private boolean isPortrait() {
//            return getSkinnable().getPageRotation() % 180 == 0;
//        }
//    }
//
//    private class RenderService extends Service<Image> {
//
//        private final boolean thumbnailRenderer;
//
//        public RenderService(boolean thumbnailRenderer) {
//            this.thumbnailRenderer = thumbnailRenderer;
//
//            setExecutor(EXECUTOR);
//
//            InvalidationListener restartListener = it -> restart();
//            page.addListener(restartListener);
//            scale.addListener(restartListener);
//        }
//
//        private final FloatProperty scale = new SimpleFloatProperty();
//
//        private float getScale() {
//            return scale.get();
//        }
//
//        FloatProperty scaleProperty() {
//            return scale;
//        }
//
//        // initialize with -1 to ensure property fires
//        private final IntegerProperty page = new SimpleIntegerProperty(-1);
//
//        private final void setPage(int page) {
//            this.page.set(page);
//        }
//
//        private int getPage() {
//            return page.get();
//        }
//
//        IntegerProperty pageProperty() {
//            return page;
//        }
//
//        @Override
//        protected Task<Image> createTask() {
//            return new PDFViewSkin.RenderTask(thumbnailRenderer, getPage(), getScale());
//        }
//    }
//
//    private class RenderTask extends Task<Image> {
//
//        private final int page;
//        private final float scale;
//        private final boolean thumbnail;
//
//        public RenderTask(boolean thumbnail, int page, float scale) {
//            this.thumbnail = thumbnail;
//            this.page = page;
//            this.scale = scale;
//        }
//
//        @Override
//        protected Image call() {
//            if (page >= 0 && page < getSkinnable().getDocument().getNumberOfPages()) {
//                if (!isCancelled()) {
//                    Image renderedImage = renderPDFPage(page, scale);
//                    if (getSkinnable().isCacheThumbnails() && thumbnail) {
//                        imageCache.put(page, renderedImage);
//                    }
//                    return renderedImage;
//                }
//            }
//
//            return null;
//        }
//
//        private Image renderPDFPage(int pageNumber, float scale) {
//            BufferedImage bufferedImage = getSkinnable().getDocument().renderPage(pageNumber, scale);
//
//            // only highlight search results in the main view (for performance reasons)
//            if (!thumbnail) {
//                highlightSearchResults(pageNumber, scale, bufferedImage);
//            }
//
//            return SwingFXUtils.toFXImage(bufferedImage, null);
//        }
//
//        private void highlightSearchResults(int pageNumber, float scale, BufferedImage bufferedImage) {
//            List<PDFView.SearchResult> searchResults = getSkinnable().getSearchResults().stream()
//                .filter(result -> result.getPageNumber() == pageNumber)
//                .collect(Collectors.toList());
//
//            if (!searchResults.isEmpty()) {
//                Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
//                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
//
//                Color searchResultColor = getSkinnable().getSearchResultColor();
//
//                graphics.setStroke(new BasicStroke(8));
//                graphics.setColor(new java.awt.Color((int) (255 * searchResultColor.getRed()), (int) (255 * searchResultColor.getGreen()), (int) (255 * searchResultColor.getBlue())));
//
//                searchResults.forEach(result -> {
//                    Rectangle2D highlightMarker = result.getScaledMarker(scale);
//                    graphics.fillRect(
//                        (int) highlightMarker.getMinX(),
//                        (int) highlightMarker.getMinY(),
//                        (int) highlightMarker.getWidth(),
//                        (int) highlightMarker.getHeight());
//                });
//            }
//        }
//    }
//
//    private void updatePagesList() {
//        PDFView.Document document = getSkinnable().getDocument();
//        pdfFilePages.clear();
//        if (document != null) {
//            for (int i = 0; i < document.getNumberOfPages(); i++) {
//                pdfFilePages.add(i);
//            }
//        }
//    }
//
//    class SearchResultListCell extends ListCell<PDFViewSkin.PageSearchResult> {
//
//        private final Label pageLabel = new Label();
//        private final Label matchesLabel = new Label();
//        private final Label summaryLabel = new Label();
//        private final ImageView imageView = new ImageView();
//        private final PDFViewSkin.RenderService renderService = new PDFViewSkin.RenderService(true);
//
//        public SearchResultListCell() {
//            pageLabel.setMaxWidth(Double.MAX_VALUE);
//            HBox.setHgrow(pageLabel, Priority.ALWAYS);
//
//            HBox header = new HBox(pageLabel, matchesLabel);
//            header.setFillHeight(true);
//            header.setAlignment(Pos.TOP_LEFT);
//
//            VBox.setVgrow(summaryLabel, Priority.ALWAYS);
//            VBox box = new VBox(5, header, summaryLabel);
//            box.setFillWidth(true);
//
//            imageView.setPreserveRatio(true);
//
//            StackPane stackPane = new StackPane(imageView);
//            stackPane.getStyleClass().add("image-view-wrapper");
//            stackPane.setMaxWidth(Region.USE_PREF_SIZE);
//            stackPane.visibleProperty().bind(imageView.imageProperty().isNotNull());
//
//            pageLabel.getStyleClass().add("page-label");
//            matchesLabel.getStyleClass().add("matches-label");
//            summaryLabel.getStyleClass().add("summary-label");
//            summaryLabel.setWrapText(true);
//
//            HBox.setHgrow(box, Priority.ALWAYS);
//            HBox finalBox = new HBox(10, stackPane, box);
//            finalBox.setFillHeight(false);
//            finalBox.setAlignment(Pos.TOP_LEFT);
//
//            finalBox.visibleProperty().bind(itemProperty().isNotNull());
//
//            setGraphic(finalBox);
//            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//
//            renderService.scaleProperty().bind(getSkinnable().thumbnailPageScaleProperty());
//            renderService.valueProperty().addListener(it -> imageView.setImage(renderService.getValue()));
//
//            itemProperty().addListener(it -> {
//                PDFViewSkin.PageSearchResult item = getItem();
//                if (item != null) {
//                    Image image = imageCache.get(item.getPageNumber());
//                    if (getSkinnable().isCacheThumbnails() && image != null) {
//                        renderService.cancel();
//                        imageView.setImage(image);
//                    } else {
//                        renderService.setPage(item.getPageNumber());
//                    }
//                } else {
//                    imageView.setImage(null);
//                }
//            });
//
//            setPrefWidth(0);
//        }
//
//        @Override
//        protected void updateItem(PDFViewSkin.PageSearchResult item, boolean empty) {
//            super.updateItem(item, empty);
//
//            if (item != null && !empty) {
//
//                pageLabel.setText("Page " + (item.getPageNumber() + 1));
//
//                int matchCount = item.getItems().size();
//                if (matchCount == 1) {
//                    matchesLabel.setText("1 match");
//                } else {
//                    matchesLabel.setText(matchCount + " matches");
//                }
//
//                String text = item.getItems().stream()
//                    .filter(searchResult -> searchResult.getTextSnippet() != null)
//                    .map(PDFView.SearchResult::getTextSnippet)
//                    .collect(Collectors.joining("... "));
//
//                summaryLabel.setText(text.substring(0, Math.min(120, text.length())));
//
//                PDFView.Document document = getSkinnable().getDocument();
//                if (document.isLandscape(item.pageNumber)) {
//                    imageView.fitWidthProperty().bind(getSkinnable().thumbnailSizeProperty().divide(3));
//                    imageView.fitHeightProperty().unbind();
//                } else {
//                    imageView.fitWidthProperty().unbind();
//                    imageView.fitHeightProperty().bind(getSkinnable().thumbnailSizeProperty().divide(3));
//                }
//            }
//        }
//    }
//
//    class PdfPageListCell extends ListCell<Integer> {
//
//        private final ImageView imageView = new ImageView();
//        private final Label pageNumberLabel = new Label();
//        private final PDFViewSkin.RenderService renderService = new PDFViewSkin.RenderService(true);
//
//        public PdfPageListCell() {
//            StackPane stackPane = new StackPane(imageView);
//            stackPane.getStyleClass().add("image-view-wrapper");
//            stackPane.setMaxWidth(Region.USE_PREF_SIZE);
//            stackPane.visibleProperty().bind(imageView.imageProperty().isNotNull());
//
//            pageNumberLabel.getStyleClass().add("page-number-label");
//            pageNumberLabel.visibleProperty().bind(imageView.imageProperty().isNotNull());
//
//            VBox vBox = new VBox(5, stackPane, pageNumberLabel);
//            vBox.setAlignment(Pos.CENTER);
//            vBox.setFillWidth(true);
//            vBox.visibleProperty().bind(emptyProperty().not());
//            setGraphic(vBox);
//
//            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//
//            imageView.setPreserveRatio(true);
//
//            setAlignment(Pos.CENTER);
//            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//            setMinSize(0, 0);
//
//            InvalidationListener invalidationListener = it -> {
//                Image image = imageCache.get(getItem());
//                if (getSkinnable().isCacheThumbnails() && image != null) {
//                    renderService.cancel();
//                    imageView.setImage(image);
//                } else {
//                    renderService.setPage(getIndex());
//                }
//            };
//
//
//            itemProperty().addListener(invalidationListener);
//
//            renderService.scaleProperty().bind(getSkinnable().thumbnailPageScaleProperty());
//            renderService.valueProperty().addListener(it -> imageView.setImage(renderService.getValue()));
//        }
//
//        @Override
//        protected void updateItem(Integer pageNumber, boolean empty) {
//            super.updateItem(pageNumber, empty);
//
//            if (pageNumber != null && !empty) {
//                if (getSkinnable().getDocument().isLandscape(pageNumber)) {
//                    imageView.fitWidthProperty().bind(getSkinnable().thumbnailSizeProperty());
//                    imageView.fitHeightProperty().unbind();
//                } else {
//                    imageView.fitWidthProperty().unbind();
//                    imageView.fitHeightProperty().bind(getSkinnable().thumbnailSizeProperty());
//                }
//
//                pageNumberLabel.setText(Integer.toString(getIndex() + 1));
//            }
//        }
//    }
//
//    private final ObservableList<PDFViewSkin.PageSearchResult> pageSearchResults = FXCollections.observableArrayList();
//
//    public static class PageSearchResult implements Comparable<PDFViewSkin.PageSearchResult> {
//
//        private final int pageNumber;
//        private final String searchText;
//        private final List<PDFView.SearchResult> items = new ArrayList<>();
//
//        public PageSearchResult(int pageNumber, String searchText) {
//            this.pageNumber = pageNumber;
//            this.searchText = searchText;
//        }
//
//        public String getSearchText() {
//            return searchText;
//        }
//
//        public int getPageNumber() {
//            return pageNumber;
//        }
//
//        public List<PDFView.SearchResult> getItems() {
//            return items;
//        }
//
//        @Override
//        public int compareTo(PDFViewSkin.PageSearchResult o) {
//            return getPageNumber() - o.getPageNumber();
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o)
//                return true;
//            if (o == null || getClass() != o.getClass())
//                return false;
//            PDFViewSkin.PageSearchResult that = (PDFViewSkin.PageSearchResult) o;
//            return pageNumber == that.pageNumber && Objects.equals(searchText, that.searchText);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(pageNumber, searchText);
//        }
//    }
}
