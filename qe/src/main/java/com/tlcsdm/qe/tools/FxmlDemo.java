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
import com.tlcsdm.core.javafx.control.LineChartWithMarkers;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 测试用，测试fxml集成
 *
 * @author unknowIfGuestInDream
 */
public class FxmlDemo extends QeSample implements Initializable {

    @FXML
    private SplitPane splitPane;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private Label lblControl;
    // Power Control
    @FXML
    private Button btnPowerControl;
    @FXML
    private Rectangle rectLighting;
    @FXML
    private SVGPath svgLighting;
    @FXML
    private SVGPath svgBase;
    @FXML
    private SVGPath svgOutline;
    @FXML
    private SVGPath svgRay;
    @FXML
    private Label lblActualLevel;
    @FXML
    private Slider sliderLight;
    @FXML
    private Button btnUp;
    @FXML
    private Button btnDown;
    @FXML
    private Button btnStepUp;
    @FXML
    private Button btnStepDown;
    @FXML
    private Button btnMax;
    @FXML
    private Button btnMin;
    @FXML
    private Button btnDirectLevel;
    @FXML
    private TextField txtDirectLevel;
    // Dimming Curve
    @FXML
    private VBox boxChart;
    @FXML
    private ComboBox<Integer> cmbDimmingCurve;
    @FXML
    private Button btnDimmingSet;
    // Fade Setting
    @FXML
    private TextField txtFadeTime;
    @FXML
    private TextField txtFadeRate;
    @FXML
    private TextField txtFadeTimeBase;
    @FXML
    private TextField txtFadeTimeMulti;
    @FXML
    private TextField txtFastFadeTime;
    @FXML
    private Button btnFadeTime;
    @FXML
    private Button btnFadeRate;
    @FXML
    private Button btnExtendedFadeTime;
    @FXML
    private Button btnFastFadeTime;
    // Level Setting
    @FXML
    private ToggleGroup levelGroup;
    @FXML
    private ComboBox<String> cmbLevelStore;
    @FXML
    private RadioButton radioActual;
    @FXML
    private RadioButton radioDirect;
    @FXML
    private TextField txtDirect;
    @FXML
    private Button btnLevelSet;

    private LineChartWithMarkers<Integer, Double> lineChart;
    private NumberAxis xAxis;
    private NumberAxis yAxis;

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(getClass().getResource("/com/tlcsdm/qe/fxml/light.fxml"), ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale));
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
    public void initialize(URL location, ResourceBundle resources) {
        initializeOption();
        initializeUI();
    }

    public void initializeOption() {
        svgLighting.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
            double level = sliderLight.getValue();
            if (level == 0) {
                return 0.0;
            }
            if (level < 26) {
                return 0.1;
            }
            return level / 256;
        }, sliderLight.valueProperty()));

        svgRay.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
            double level = sliderLight.getValue();
            return level / 256;
        }, sliderLight.valueProperty()));

        svgOutline.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
            double level = sliderLight.getValue();
            return 1.0 - level / 256;
        }, sliderLight.valueProperty()));

        lblActualLevel.textProperty().bind(Bindings.createStringBinding(() -> {
            double level = daliDimmingCurve((int) sliderLight.getValue());
            return String.format("%.2f", level) + "%";
        }, sliderLight.valueProperty()));
        // Get the font from the Font cache to avoid loading the font every time.
        Font font = Font.font("tlcsdm", lblActualLevel.getFont().getSize());
        if (font == null || !"tlcsdm".equals(font.getFamily())) {
            font = Font.loadFont(getClass().getResourceAsStream("/com/tlcsdm/qe/fxml/digital.ttf"), lblActualLevel.getFont().getSize());
        }
        lblActualLevel.setFont(font);

        cmbDimmingCurve.getItems().addAll(0, 1);
        cmbDimmingCurve.getSelectionModel().select(0);

        sliderLight.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderLight.getValue() - sliderLight.getMin()) / (sliderLight.getMax() - sliderLight.getMin()) * 100.0;
            return String.format("-slider-track-color: linear-gradient(to top, -slider-filled-track-color 0%%, " + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);", percentage, percentage);
        }, sliderLight.valueProperty(), sliderLight.minProperty(), sliderLight.maxProperty()));

        initializeTreeview();
    }

    public void initializeUI() {
        initializeSvg();
        initializeChart();
    }

    private void initializeSvg() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(getClass().getResourceAsStream("/com/tlcsdm/qe/static/icon/lighting.svg"));
            org.w3c.dom.Node nodeLighting = doc.getElementsByTagName("path").item(0);
            org.w3c.dom.Node nodeBase = doc.getElementsByTagName("path").item(1);
            org.w3c.dom.Node nodeOutline = doc.getElementsByTagName("path").item(2);
            org.w3c.dom.Node nodeRay = doc.getElementsByTagName("path").item(3);
            parseXmlToSvg(svgLighting, nodeLighting);
            parseXmlToSvg(svgBase, nodeBase);
            parseXmlToSvg(svgOutline, nodeOutline);
            parseXmlToSvg(svgRay, nodeRay);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            StaticLog.error(e, "Parse svg failed!");
        }
    }

    private void initializeChart() {
        xAxis = new NumberAxis();
        xAxis.setLabel("Control level");
        xAxis.setUpperBound(256.0);
        xAxis.setSide(Side.BOTTOM);

        yAxis = new NumberAxis();
        yAxis.setLabel("Lighting level (%)");
        yAxis.setSide(Side.LEFT);
        lineChart = new LineChartWithMarkers(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setPadding(new Insets(0, 10, 0, 0));
        boxChart.getChildren().add(lineChart);

        lineChart.getData().add(createSeries());
        lineChart.getData().get(0).getData().forEach(data -> {
            Tooltip.install(data.getNode(), new Tooltip("X轴:" + data.getXValue().toString() + "\n" + "Y轴:" + String.format("%.2f", data.getYValue())));
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
        });
    }

    private void initializeTreeview() {
        TreeItem<String> root = new TreeItem<>("QE for Lighting");
        root.setExpanded(true);
        TreeItem<String> group1 = new TreeItem<>("Group 1");
        group1.setExpanded(true);
        group1.getChildren().add(new TreeItem<>("Addrest 1"));
        TreeItem<String> group5 = new TreeItem<>("Group 5");
        group5.setExpanded(true);
        group5.getChildren().add(new TreeItem<>("Addrest 1"));
        TreeItem<String> assign = new TreeItem<>("Not Assigned");
        assign.setExpanded(true);
        assign.getChildren().add(new TreeItem<>("Addrest 0"));
        assign.getChildren().add(new TreeItem<>("Addrest 2"));
        root.getChildren().addAll(new TreeItem<>("Boardcast"), group1, group5, assign);
        treeView.setRoot(root);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            lblControl.setText(newValue.getValue());
        });
        treeView.getSelectionModel().select(treeView.getRoot());
    }

    /**
     * Svg path converted to SVGPath object
     */
    private void parseXmlToSvg(SVGPath svg, org.w3c.dom.Node node) {
        String content = node.getAttributes().getNamedItem("d").getNodeValue();
        svg.setContent(content);
        String colorString = node.getAttributes().getNamedItem("fill").getNodeValue();
        svg.setFill(Color.web(colorString, 1));
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
        for (int i = 0; i < 256; i++) {
            series.getData().add(new Data<>(i, daliDimmingCurve(i)));
        }
        return series;
    }

    /**
     * Get lighting level based on control level
     */
    private double daliDimmingCurve(int x) {
        if (x == 255) {
            return 100;
        } else if (x == 0) {
            return 0;
        } else {
            return Math.pow(10, (double) (x - 1) * 3 / 253 - 1);
        }
    }

    @FXML
    public void powerControlAction() {
        // Do nothing
    }

    @FXML
    public void minLevelAction() {
        sliderLight.setValue(sliderLight.getMin());
    }

    @FXML
    public void maxLevelAction() {
        sliderLight.setValue(sliderLight.getMax());
    }

    @FXML
    public void upLevelAction() {
        int level = (int) sliderLight.getValue();
        if (level < sliderLight.getMax()) {
            sliderLight.setValue(level + 1);
        }
    }

    @FXML
    public void downLevelAction() {
        int level = (int) sliderLight.getValue();
        if (level > sliderLight.getMin()) {
            sliderLight.setValue(level - 1);
        }
    }

    @FXML
    public void stepUpAction() {
        // Do nothing
    }

    @FXML
    public void stepDownAction() {
        // Do nothing
    }

    @FXML
    public void directLevelAction() {
        sliderLight.setValue(Integer.parseInt(txtDirectLevel.getText()));
    }

    @FXML
    public void directLevelEnterAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnDirectLevel.fire();
        }
    }

    @FXML
    public void dimmingSetAction() {
        // Do nothing
    }

    @FXML
    public void levelSettingAction() {
        // Do nothing
    }

    @FXML
    public void fadeTimeAction() {
        // Do nothing
    }

    @FXML
    public void fadeRateAction() {
        // Do nothing
    }

    @FXML
    public void extendedFadeTimeAction() {
        // Do nothing
    }

    @FXML
    public void fastFadeTimeAction() {
        // Do nothing
    }
}
