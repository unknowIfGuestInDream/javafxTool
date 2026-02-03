/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

package com.tlcsdm.qe.tools;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.chart.LineChartWithMarkers;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 测试用，测试fxml集成.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.0
 */
public class FxmlDemo extends QeSample implements Initializable {

    @FXML
    private TreeView<String> treeView;
    @FXML
    private Label lblControl;
    // ReadOnly Data
    @FXML
    private Label lblPhm, lblMinLevel, lblMaxLevel, lblPowerOnLevel, lblSystemFailLevel;
    // Power Control
    @FXML
    private Button btnPowerControl, btnUp, btnDown, btnStepUp, btnStepDown, btnMax, btnMin, btnDirectLevel;
    @FXML
    private Rectangle rectLighting;
    @FXML
    private SVGPath svgLighting, svgOutline, svgRay;
    @FXML
    private Label lblActualLevel;
    @FXML
    private Slider sliderLight;
    @FXML
    private TextField txtDirectLevel;
    // Dimming Curve
    @FXML
    private VBox boxChart;
    // Fade Setting
    @FXML
    private ComboBox<FadeData> cmbFadeTime, cmbFadeRate;
    @FXML
    private Button btnFadeTime, btnFadeRate;
    // Level Setting
    @FXML
    private ToggleGroup levelGroup;
    @FXML
    private ComboBox<String> cmbLevelStore;
    @FXML
    private RadioButton radioActual, radioDirect;
    @FXML
    private TextField txtDirect;
    @FXML
    private Button btnLevelSet;

    private LineChartWithMarkers<Integer, Double> lineChart;
    private NumberAxis xAxis;
    private NumberAxis yAxis;

    SimpleIntegerProperty phmProperty;
    SimpleIntegerProperty minProperty;
    SimpleIntegerProperty maxProperty;
    SimpleIntegerProperty powerProperty;
    SimpleIntegerProperty systemProperty;

    private final HashMap<Integer, Double> daliDimmingCurveMap = new HashMap<>(512);
    private final LinkedList<Integer> discretePointXList = new LinkedList<>();

    @Override
    public boolean isVisible() {
        String value = System.getProperty(CoreConstant.JVM_WORKENV);
        return CoreConstant.JVM_WORKENV_TEST.equals(value);
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(getClass().getResource("/com/tlcsdm/qe/fxml/light.fxml"),
            ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "fxmlDemo";
    }

    @Override
    public String getSampleName() {
        return "FXML示例";
    }

    @Override
    public String getOrderKey() {
        return "fxmlDemo";
    }

    @Override
    public String getSampleDescription() {
        return "此组件测试用";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0-Beta";
    }

    @Override
    public boolean hasControlPanel() {
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeOption();
        initializeUI();
    }

    public void initializeOption() {
        initializeTreeview();
        initializeReadOnlyData();
        initializePowerControl();
        initializeFade();
        initializeLevel();
    }

    public void initializeUI() {
        initializeSvg();
        initializeChart();
        // Get the font from the Font cache to avoid loading the font every time.
        Font font = Font.font("tlcsdm", lblActualLevel.getFont().getSize());
        if (font == null || !"tlcsdm".equals(font.getFamily())) {
            font = Font.loadFont(getClass().getResourceAsStream("/com/tlcsdm/qe/fxml/digital.ttf"),
                lblActualLevel.getFont().getSize());
        }
        lblActualLevel.setFont(font);
        lblPhm.setFont(Font.font("tlcsdm", lblPhm.getFont().getSize()));
        lblMaxLevel.setFont(Font.font("tlcsdm", lblMaxLevel.getFont().getSize()));
        lblMinLevel.setFont(Font.font("tlcsdm", lblMinLevel.getFont().getSize()));
        lblPowerOnLevel.setFont(Font.font("tlcsdm", lblPowerOnLevel.getFont().getSize()));
        lblSystemFailLevel.setFont(Font.font("tlcsdm", lblSystemFailLevel.getFont().getSize()));
    }

    private void initializeSvg() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(getClass().getResourceAsStream("/com/tlcsdm/qe/static/icon/lighting.svg"));
            org.w3c.dom.Node nodeLighting = doc.getElementsByTagName("path").item(0);
            org.w3c.dom.Node nodeOutline = doc.getElementsByTagName("path").item(1);
            org.w3c.dom.Node nodeRay = doc.getElementsByTagName("path").item(2);
            parseXmlToSvg(svgLighting, nodeLighting);
            parseXmlToSvg(svgOutline, nodeOutline);
            parseXmlToSvg(svgRay, nodeRay);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            StaticLog.error(e, "Parse svg failed!");
        }
    }

    @SuppressWarnings("rawtypes")
    private void initializeChart() {
        xAxis = new NumberAxis();
        xAxis.setLabel("Control level");
        xAxis.setUpperBound(254);
        xAxis.setSide(Side.BOTTOM);
        xAxis.setAutoRanging(false);

        yAxis = new NumberAxis();
        yAxis.setLabel("Lighting level (%)");
        yAxis.setSide(Side.LEFT);
        yAxis.setAutoRanging(false);
        lineChart = new LineChartWithMarkers(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setPadding(new Insets(0, 10, 0, 0));
        boxChart.getChildren().add(lineChart);

        lineChart.getData().add(createSeries());
        lineChart.getData().get(0).getData().forEach(data -> {
            Tooltip.install(data.getNode(), new Tooltip(
                "X:" + data.getXValue().toString() + "\n" + "Y:" + String.format("%.2f", data.getYValue())));
            if (data.getXValue() == (int) sliderLight.getValue()) {
                XYChart.Data<Integer, Double> d1 = new XYChart.Data<>(data.getXValue(), data.getYValue());
                XYChart.Data<Integer, Double> d2 = new XYChart.Data<>(data.getXValue(), data.getYValue());
                lineChart.addHorizontalValueMarker(d1);
                lineChart.addVerticalValueMarker(d2);
            }
        });

        sliderLight.valueProperty().addListener((observable, oldValue, newValue) -> {
            lineChart.clearHorizontalValueMarker();
            lineChart.clearVerticalValueMarker();
            int level = newValue.intValue();
            double curve = daliDimmingCurve(level);
            Data<Integer, Double> d1 = new Data<>(level, curve);
            Data<Integer, Double> d2 = new Data<>(level, curve);
            lineChart.addHorizontalValueMarker(d1);
            lineChart.addVerticalValueMarker(d2);
            buildDiscretePoint();
        });

        // chart style
        for (int i = 0; i < 255; i++) {
            final int index = i;
            lineChart.lookup(".data" + index).styleProperty().bind(Bindings.createStringBinding(() -> {
                if (index > maxProperty.get() || index < minProperty.get()) {
                    return "-fx-background-color: gray, white;";
                }
                return "";
            }, minProperty, maxProperty));
        }

        lineChart.lookup(".default-color0.chart-series-line").styleProperty().bind(Bindings.createStringBinding(() -> {
            return "-fx-stroke: linear-gradient(to top, gray 0%, blue " + daliDimmingCurve(maxProperty.get())
                + "%, gray " + daliDimmingCurve(minProperty.get()) + "%);";
        }, minProperty, maxProperty));
        buildDiscretePoint();
    }

    private void initializeTreeview() {
        TreeItem<String> root = new TreeItem<>("QE for Lighting");
        root.setExpanded(true);
        TreeItem<String> group1 = new TreeItem<>("Group 1");
        group1.setExpanded(true);
        group1.getChildren().add(new TreeItem<>("Address 1"));
        TreeItem<String> group5 = new TreeItem<>("Group 5");
        group5.setExpanded(true);
        group5.getChildren().add(new TreeItem<>("Address 1"));
        TreeItem<String> assign = new TreeItem<>("Not Assigned");
        assign.setExpanded(true);
        assign.getChildren().add(new TreeItem<>("Address 0"));
        assign.getChildren().add(new TreeItem<>("Address 2"));
        root.getChildren().addAll(new TreeItem<>("Boardcast"), group1, group5, assign);
        treeView.setRoot(root);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            lblControl.setText(newValue.getValue());
        });
        treeView.getSelectionModel().select(treeView.getRoot());
    }

    private void initializeReadOnlyData() {
        phmProperty = new SimpleIntegerProperty(26);
        maxProperty = new SimpleIntegerProperty(240);
        minProperty = new SimpleIntegerProperty(170);
        powerProperty = new SimpleIntegerProperty(254);
        systemProperty = new SimpleIntegerProperty(254);
        lblPhm.textProperty().bind(phmProperty.asString());
        lblMaxLevel.textProperty().bind(maxProperty.asString());
        lblMinLevel.textProperty().bind(minProperty.asString());
        lblPowerOnLevel.textProperty().bind(powerProperty.asString());
        lblSystemFailLevel.textProperty().bind(systemProperty.asString());
    }

    private void initializeFade() {
        cmbFadeTime.getItems().addAll(new FadeData(0, "no fade", "0"), new FadeData(1, "0.7sec", "0.707"),
            new FadeData(2, "1.0sec", "1.000"), new FadeData(3, "1.4sec", "1.414"), new FadeData(4, "2.0sec", "2.000"),
            new FadeData(5, "2.8sec", "2.828"), new FadeData(6, "4.0sec", "4.000"), new FadeData(7, "5.7sec", "5.657"),
            new FadeData(8, "8.0sec", "8.000"), new FadeData(9, "11.3sec", "11.314"),
            new FadeData(10, "16.0sec", "16.000"), new FadeData(11, "22.6sec", "22.627"),
            new FadeData(12, "32.0sec", "32.000"), new FadeData(13, "45.3sec", "45.255"),
            new FadeData(14, "64.0sec", "64.000"), new FadeData(15, "90.5sec", "90.510"));
        cmbFadeTime.getSelectionModel().select(0);
        cmbFadeRate.getItems().addAll(new FadeData(1, "357.8steps/sec", "357.796"),
            new FadeData(2, "253steps/sec", "253.000"), new FadeData(3, "179steps/sec", "178.898"),
            new FadeData(4, "127steps/sec", "126.500"), new FadeData(5, "89.4steps/sec", "89.449"),
            new FadeData(6, "63.3steps/sec", "63.250"), new FadeData(7, "44.7steps/sec", "44.725"),
            new FadeData(8, "31.6steps/sec", "31.625"), new FadeData(9, "22.4steps/sec", "22.362"),
            new FadeData(10, "15.8steps/sec", "15.813"), new FadeData(11, "11.2steps/sec", "11.181"),
            new FadeData(12, "7.9steps/sec", "7.906"), new FadeData(13, "5.6steps/sec", "5.591"),
            new FadeData(14, "4.0steps/sec", "3.953"), new FadeData(15, "2.8steps/sec", "2.795"));
        cmbFadeRate.getSelectionModel().select(6);

        txtDirect.disableProperty().bind(radioDirect.selectedProperty().not());
        btnLevelSet.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            if (!radioDirect.isSelected()) {
                return false;
            }
            String directString = txtDirect.getText();
            if (isInteger(directString)) {
                int direct = Integer.parseInt(directString);
                return direct < 0 || direct > 254;
            } else {
                return true;
            }
        }, txtDirect.textProperty(), levelGroup.selectedToggleProperty()));
    }

    private void initializeLevel() {
        cmbLevelStore.getItems().addAll("Maximum Level", "Minimum Level", "Power-On Level", "System Failure Level");
        cmbLevelStore.getSelectionModel().select(0);
    }

    private void initializePowerControl() {
        svgLighting.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
            int level = (int) sliderLight.getValue();
            return daliDimmingCurve(level) / 100.0;
        }, sliderLight.valueProperty()));

        svgRay.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
            int level = (int) sliderLight.getValue();
            return daliDimmingCurve(level) / 100.0;
        }, sliderLight.valueProperty()));

        svgOutline.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
            int level = (int) sliderLight.getValue();
            return 1.0 - daliDimmingCurve(level) / 100.0;
        }, sliderLight.valueProperty()));

        lblActualLevel.textProperty().bind(Bindings.createStringBinding(() -> {
            double level = daliDimmingCurve((int) sliderLight.getValue());
            return String.format("%.2f", level) + "%";
        }, sliderLight.valueProperty()));
        lblActualLevel.textFillProperty().bind(Bindings.createObjectBinding(() -> {
            int level = (int) sliderLight.getValue();
            if (level < minProperty.get() || level > maxProperty.get()) {
                return Color.RED;
            }
            return Color.BLACK;
        }, minProperty, maxProperty, sliderLight.valueProperty()));
        // Gradient color settings for the slide bar, need to be used together with the
        // settings in css
        sliderLight.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderLight.getValue() - sliderLight.getMin())
                / (sliderLight.getMax() - sliderLight.getMin()) * 100.0;
            return String.format("-slider-track-color: linear-gradient(to top, -slider-filled-track-color 0%%, "
                + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);", percentage, percentage);
        }, sliderLight.valueProperty(), sliderLight.minProperty(), sliderLight.maxProperty()));

        btnDirectLevel.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            String directString = txtDirectLevel.getText();
            if (isInteger(directString)) {
                int direct = Integer.parseInt(directString);
                return direct < 0 || direct > 254;
            } else {
                return true;
            }
        }, txtDirectLevel.textProperty()));
    }

    /**
     * Svg path converted to SVGPath object
     */
    private void parseXmlToSvg(SVGPath svg, org.w3c.dom.Node node) {
        String content = node.getAttributes().getNamedItem("d").getNodeValue();
        svg.setContent(content);
        if (node.getAttributes().getNamedItem("fill") != null) {
            String colorString = node.getAttributes().getNamedItem("fill").getNodeValue();
            svg.setFill(Color.web(colorString, 1));
        }
        if (node.getAttributes().getNamedItem("stroke") != null) {
            String stroke = node.getAttributes().getNamedItem("stroke").getNodeValue();
            svg.setStroke(Color.web(stroke, 1));
        }
        if (node.getAttributes().getNamedItem("stroke-width") != null) {
            String strokeWidth = node.getAttributes().getNamedItem("stroke-width").getNodeValue();
            svg.setStrokeWidth(Double.parseDouble(strokeWidth));
        }
        if (node.getAttributes().getNamedItem("stroke-miterlimit") != null) {
            String miterlimit = node.getAttributes().getNamedItem("stroke-miterlimit").getNodeValue();
            svg.setStrokeMiterLimit(Double.parseDouble(miterlimit));
        }
        if (node.getAttributes().getNamedItem("id") != null) {
            String id = node.getAttributes().getNamedItem("id").getNodeValue();
            svg.getStyleClass().add(id);
        }
    }

    /**
     * DALI dimming curve formula
     */
    private Series<Integer, Double> createSeries() {
        Series<Integer, Double> series = new Series<>();
        for (int i = 0; i < 255; i++) {
            series.getData().add(new Data<>(i, daliDimmingCurve(i)));
        }
        return series;
    }

    /**
     * Get lighting level based on control level
     */
    private double daliDimmingCurve(int level) {
        if (daliDimmingCurveMap.containsKey(level)) {
            return daliDimmingCurveMap.get(level);
        }
        double result;
        if (level == 0) {
            result = 0.0;
        } else {
            result = Math.pow(10, (double) (level - 1) * 3 / 253 - 1);
        }
        daliDimmingCurveMap.put(level, result);
        return result;
    }

    @FXML
    public void powerControlAction() {
        sliderLight.setValue(sliderLight.getMin());
        resetDataTextFill();
    }

    @FXML
    public void minLevelAction() {
        sliderLight.setValue(minProperty.get());
        resetDataTextFill();
    }

    @FXML
    public void maxLevelAction() {
        sliderLight.setValue(maxProperty.get());
        resetDataTextFill();
    }

    @FXML
    public void upLevelAction() {
        int level = (int) sliderLight.getValue();
        if (level == discretePointXList.getLast()) {
            return;
        }
        int index = discretePointXList.indexOf(level);
        if (index == -1) {
            return;
        }
        sliderLight.setValue(discretePointXList.get(index + 1));
        resetDataTextFill();
    }

    @FXML
    public void downLevelAction() {
        int level = (int) sliderLight.getValue();
        if (level == discretePointXList.getFirst()) {
            return;
        }
        int index = discretePointXList.indexOf(level);
        if (index == -1) {
            return;
        }
        sliderLight.setValue(discretePointXList.get(index - 1));
        resetDataTextFill();
    }

    @FXML
    public void stepUpAction() {
        int level = (int) sliderLight.getValue();
        if (level < sliderLight.getMax()) {
            sliderLight.setValue(level + 1);
            resetDataTextFill();
        }
    }

    @FXML
    public void stepDownAction() {
        int level = (int) sliderLight.getValue();
        if (level > sliderLight.getMin()) {
            sliderLight.setValue(level - 1);
            resetDataTextFill();
        }
    }

    @FXML
    public void directLevelAction() {
        sliderLight.setValue(Integer.parseInt(txtDirectLevel.getText()));
        resetDataTextFill();
    }

    @FXML
    public void directLevelEnterAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnDirectLevel.fire();
        }
    }

    @FXML
    public void levelSettingAction() {
        String storeType = cmbLevelStore.getValue();
        boolean isActual = radioActual.isSelected();
        int level = (int) sliderLight.getValue();
        String dircetString = txtDirect.getText();
        int dircet;
        try {
            dircet = Integer.parseInt(dircetString);
        } catch (NumberFormatException e) {
            dircet = 0;
        }
        int result = isActual ? level : dircet;
        if ("Maximum Level".equals(storeType)) {
            if (result == maxProperty.get())
                return;
            maxProperty.set(result);
            resetDataTextFill();
            lblMaxLevel.setTextFill(Color.BLUE);
        } else if ("Minimum Level".equals(storeType)) {
            if (result == minProperty.get())
                return;
            minProperty.set(result);
            resetDataTextFill();
            lblMinLevel.setTextFill(Color.BLUE);
        } else if ("Power-On Level".equals(storeType)) {
            if (result == powerProperty.get())
                return;
            powerProperty.set(result);
            resetDataTextFill();
            lblPowerOnLevel.setTextFill(Color.BLUE);
        } else if ("System Failure Level".equals(storeType)) {
            if (result == systemProperty.get())
                return;
            systemProperty.set(result);
            resetDataTextFill();
            lblSystemFailLevel.setTextFill(Color.BLUE);
        } else {
            // Do nothing
        }
    }

    private void resetDataTextFill() {
        lblMaxLevel.setTextFill(Color.BLACK);
        lblMinLevel.setTextFill(Color.BLACK);
        lblPowerOnLevel.setTextFill(Color.BLACK);
        lblSystemFailLevel.setTextFill(Color.BLACK);
    }

    @FXML
    public void fadeTimeAction() {
        // Do nothing
    }

    @FXML
    public void fadeRateAction() {
        buildDiscretePoint();
    }

    /**
     * Build discrete point data styles
     */
    private void buildDiscretePoint() {
        String rate = cmbFadeRate.getValue().value;
        int step = new BigDecimal(rate).multiply(new BigDecimal("0.2")).setScale(0, RoundingMode.HALF_UP).intValue();
        int level = (int) sliderLight.getValue();
        discretePointXList.clear();
        for (int i = level; i <= 254; i = i + step) {
            discretePointXList.add(i);
        }
        for (int i = level - step; i >= 0; i = i - step) {
            discretePointXList.addFirst(i);
        }
        lineChart.getData().get(0).getData().forEach(data -> {
            if (!discretePointXList.contains(data.getXValue())) {
                data.getNode().getStyleClass().removeAll("discreteFirstPoint", "discretePoint");
            } else if (data.getXValue() == level) {
                if (!data.getNode().getStyleClass().contains("discreteFirstPoint")) {
                    data.getNode().getStyleClass().add("discreteFirstPoint");
                }
                data.getNode().getStyleClass().remove("discretePoint");
            } else {
                if (!data.getNode().getStyleClass().contains("discretePoint")) {
                    data.getNode().getStyleClass().add("discretePoint");
                }
                data.getNode().getStyleClass().remove("discreteFirstPoint");
            }
        });
        lineChart.requestChartLayout();
    }

    private ImageView iconView(URL resource) {
        return new ImageView(new Image(resource.toExternalForm()));
    }

    /**
     * Returns true if the argument is a integer
     */
    private boolean isInteger(String str) {
        if (str.isEmpty()) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Fade data model
     */
    record FadeData(int setting, String desc, String value) {

        @Override
        public String toString() {
            return setting + "(" + desc + ")";

        }
    }
}
