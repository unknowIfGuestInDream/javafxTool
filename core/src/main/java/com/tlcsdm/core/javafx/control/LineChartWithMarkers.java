package com.tlcsdm.core.javafx.control;

import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.shape.Line;

/**
 * LineChart that can add horizontal and vertical axis auxiliary lines
 */
public class LineChartWithMarkers<X, Y> extends LineChart<X, Y> {

    private final ObservableList<Data<X, Y>> horizontalMarkers;
    private final ObservableList<Data<X, Y>> verticalMarkers;

    public LineChartWithMarkers(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        super(xAxis, yAxis);
        horizontalMarkers = FXCollections.observableArrayList(data -> new Observable[]{data.YValueProperty()});
        horizontalMarkers.addListener((InvalidationListener) observable -> layoutPlotChildren());
        verticalMarkers = FXCollections.observableArrayList(data -> new Observable[]{data.XValueProperty()});
        verticalMarkers.addListener((InvalidationListener) observable -> layoutPlotChildren());
    }

    public void addHorizontalValueMarker(Data<X, Y> marker) {
        if (horizontalMarkers.contains(marker)) {
            return;
        }
        Line line = new Line();
        line.setStyle("-fx-stroke:red;-fx-stroke-width:1px;-fx-stroke-dash-array: 5px, 10px;");
        marker.setNode(line);
        getPlotChildren().add(line);
        horizontalMarkers.add(marker);
    }

    public void removeHorizontalValueMarker(Data<X, Y> marker) {
        if (marker.getNode() != null) {
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        horizontalMarkers.remove(marker);
    }

    public void clearHorizontalValueMarker() {
        horizontalMarkers.forEach(marker -> {
            if (marker.getNode() != null) {
                getPlotChildren().remove(marker.getNode());
                marker.setNode(null);
            }
        });
        horizontalMarkers.clear();
    }

    public void addVerticalValueMarker(Data<X, Y> marker) {
        if (verticalMarkers.contains(marker)) {
            return;
        }
        Line line = new Line();
        line.setStyle("-fx-stroke:red;-fx-stroke-width:1px;-fx-stroke-dash-array: 5px, 10px;");
        marker.setNode(line);
        getPlotChildren().add(line);
        verticalMarkers.add(marker);
    }

    public void removeVerticalValueMarker(Data<X, Y> marker) {
        if (marker.getNode() != null) {
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        verticalMarkers.remove(marker);
    }

    public void clearVerticalValueMarker() {
        verticalMarkers.forEach(marker -> {
            if (marker.getNode() != null) {
                getPlotChildren().remove(marker.getNode());
                marker.setNode(null);
            }
        });
        verticalMarkers.clear();
    }

    public ObservableList<Node> plotChildren() {
        return getPlotChildren();
    }

    @Override
    protected void layoutPlotChildren() {
        horizontalMarkers.forEach(this::drawHorizontalMarker);
        verticalMarkers.forEach(this::drawVerticalMarker);
        super.layoutPlotChildren();
    }

    private void drawHorizontalMarker(Data<X, Y> horizontalMarker) {
        Line line = (Line) horizontalMarker.getNode();
        line.setStartX(0);
        line.setEndX(getBoundsInLocal().getWidth());
        line.setStartY(getYAxis().getDisplayPosition(horizontalMarker.getYValue()) + 0.5); // 0.5 for crispness
        line.setEndY(line.getStartY());
        line.toFront();
    }

    private void drawVerticalMarker(Data<X, Y> verticalMarker) {
        Line line = (Line) verticalMarker.getNode();
        line.setStartX(getXAxis().getDisplayPosition(verticalMarker.getXValue()) + 0.5); // 0.5 for crispness
        line.setEndX(line.getStartX());
        line.setStartY(0d);
        line.setEndY(getBoundsInLocal().getHeight());
        line.toFront();
    }

}
