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

package com.tlcsdm.core.javafx.chart;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Variation of the base class {@link Chart} that provides a simpler implementation with a tighter
 * layout destined to plot multiple charts in a single window.
 *
 * @author unknowIfGuestInDream
 */
public abstract class DynamicChart extends Region {
    private static final int MIN_WIDTH_TO_LEAVE_FOR_CHART_CONTENT = 100;
    private static final int MIN_HEIGHT_TO_LEAVE_FOR_CHART_CONTENT = 50;

    /**
     * This is the Pane that Chart subclasses use to contain the chart content, It is sized to be inside
     * the chart area leaving space for the title and legend.
     */
    private final Pane chartContent = new Pane() {
        @Override
        protected void layoutChildren() {
            final double top = snappedTopInset();
            final double left = snappedLeftInset();
            final double bottom = snappedBottomInset();
            final double right = snappedRightInset();
            final double width = getWidth();
            final double height = getHeight();
            final double contentWidth = snapSizeX(width - (left + right));
            final double contentHeight = snapSizeY(height - (top + bottom));
            layoutChartChildren(snapPositionY(top), snapPositionX(left), contentWidth, contentHeight);
        }
    };

    /**
     * The node to display as the Legend. Subclasses can set a node here to be displayed on a side as
     * the legend. If no legend is wanted then this can be set to null
     */
    private final ObjectProperty<Node> legend = new ObjectPropertyBase<Node>() {
        private Node old = null;

        @Override
        protected void invalidated() {
            Node newLegend = get();
            if (old != null)
                getChildren().remove(old);
            if (newLegend != null) {
                getChildren().add(newLegend);
                updateLegendSizeBinding(newLegend);
                newLegend.setVisible(true);
            }
            old = newLegend;
        }

        @Override
        public Object getBean() {
            return DynamicChart.this;
        }

        @Override
        public String getName() {
            return "legend";
        }
    };

    protected final Node getLegend() {
        return legend.getValue();
    }

    protected final void setLegend(Node value) {
        legend.setValue(value);
    }

    protected final ObjectProperty<Node> legendProperty() {
        return legend;
    }

    protected void updateLegendSizeBinding(Node legend) {
        if (legend instanceof FlowPane legendFlowPane) {
            legendFlowPane.prefWrapLengthProperty().bind(widthProperty());
        } else if (legend instanceof Region legendTilePane) {
            legendTilePane.prefWidthProperty().bind(widthProperty());
        }
    }

    /**
     * Modifiable and observable list of all content in the chart. This is where implementations of
     * Chart should add any nodes they use to draw their chart. This excludes the legend and title which
     * are looked after by this class.
     *
     * @return Observable list of plot children
     */
    protected ObservableList<Node> getChartChildren() {
        return chartContent.getChildren();
    }

    // -------------- CONSTRUCTOR --------------------------------------------------------------------------------------

    /**
     * Creates a new default Chart instance.
     */
    public DynamicChart() {
        getChildren().addAll(chartContent);
        getStyleClass().add("chart");
        chartContent.getStyleClass().add("chart-content");
        // mark chartContent as unmanaged because any changes to its preferred size shouldn't cause a relayout
        chartContent.setManaged(false);
        chartContent.setPadding(Insets.EMPTY);
        setPadding(Insets.EMPTY);
    }

    /**
     * Call this when you know something has changed that needs the chart to be relayed out.
     */
    protected void requestChartLayout() {
        chartContent.requestLayout();
    }

    /**
     * Called to update and layout the chart children available from getChartChildren()
     *
     * @param top    The top offset from the origin to account for any padding on the chart content
     * @param left   The left offset from the origin to account for any padding on the chart content
     * @param width  The width of the area to layout the chart within
     * @param height The height of the area to layout the chart within
     */
    protected abstract void layoutChartChildren(double top, double left, double width, double height);

    /**
     * Invoked during the layout pass to layout this chart and all its content.
     */
    @Override
    protected void layoutChildren() {
        double top = snappedTopInset();
        double left = snappedLeftInset();
        double bottom = snappedBottomInset();
        double right = snappedRightInset();
        double width = getWidth();
        double height = getHeight();
        // layout legend
        Node legend = getLegend();
        if (legend != null) {
            boolean shouldShowLegend = true;
            double legendHeight = snapSizeY(legend.prefHeight(width - left - right));
            double legendWidth = boundedSize(snapSizeX(legend.prefWidth(legendHeight)), 0, width - left - right);
            double legendLeft = left + (width - left - right - legendWidth) / 2;
            double legendTop = height - bottom - legendHeight;
            legend.resizeRelocate(legendLeft, legendTop, legendWidth, legendHeight);
            if (height - bottom - top - legendHeight < MIN_HEIGHT_TO_LEAVE_FOR_CHART_CONTENT)
                shouldShowLegend = false;
            else
                bottom += legendHeight;
            legend.setVisible(shouldShowLegend);
        }
        // whats left is for the chart content
        chartContent.resizeRelocate(left, top, width - left - right, height - top - bottom);
    }

    /**
     * Charts are sized outside in, user tells chart how much space it has and chart draws inside that.
     * So minimum height is a constant 150.
     */
    @Override
    protected double computeMinHeight(double width) {
        return MIN_HEIGHT_TO_LEAVE_FOR_CHART_CONTENT;
    }

    /**
     * Charts are sized outside in, user tells chart how much space it has and chart draws inside that.
     * So minimum width is a constant 200.
     */
    @Override
    protected double computeMinWidth(double height) {
        return MIN_WIDTH_TO_LEAVE_FOR_CHART_CONTENT;
    }

    /**
     * Charts are sized outside in, user tells chart how much space it has and chart draws inside that.
     * So preferred width is a constant 500.
     */
    @Override
    protected double computePrefWidth(double height) {
        return 500.0;
    }

    /**
     * Charts are sized outside in, user tells chart how much space it has and chart draws inside that.
     * So preferred height is a constant 400.
     */
    @Override
    protected double computePrefHeight(double width) {
        return 400.0;
    }

    private double boundedSize(double var0, double var2, double var4) {
        return Math.min(Math.max(var0, var2), Math.max(var2, var4));
    }
}
