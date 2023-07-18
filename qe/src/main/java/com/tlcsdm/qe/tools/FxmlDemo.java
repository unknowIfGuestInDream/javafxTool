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

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 测试用，测试fxml集成
 *
 * @author unknowIfGuestInDream
 */
public class FxmlDemo extends QeSample implements Initializable {

    @FXML
    private LineChart<Integer, Double> lineChart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Slider sliderLight;
    @FXML
    private ComboBox<Integer> cmbDimmingCurve;
    @FXML
    private Rectangle rectLighting;
    @FXML
    private SVGPath svgLighting;
    @FXML
    private SVGPath svgBase;
    @FXML
    private Label lblControl;
    @FXML
    private Label lblActualLevel;
    @FXML
    private Button btnOff;
    @FXML
    private Button btnDimmingSet;
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
    @FXML
    private SplitPane splitPane;
    @FXML
    private TreeView<String> treeView;

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(getClass().getResource("/com/tlcsdm/qe/fxml/light.fxml"),
            ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public Node getControlPanel() {
        String content = """
            """;
        Map<String, String> map = new HashMap<>();
        return FxTextInput.textArea(StrUtil.format(content, map));
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
            if (level < 26) {
                return 0.1;
            }
            return level / 256;
        }, sliderLight.valueProperty()));
        lblActualLevel.textProperty().bind(Bindings.createStringBinding(() -> {
            double level = daliDimmingCurve((int) sliderLight.getValue());
            return String.format("%.2f", level) + "%";
        }, sliderLight.valueProperty()));
        Font font = Font.loadFont(getClass().getResourceAsStream("/com/tlcsdm/qe/fxml/digiface.ttf"),
            lblActualLevel.getFont().getSize());
        lblActualLevel.setFont(font);
        cmbDimmingCurve.getItems().addAll(0, 1);
        cmbDimmingCurve.getSelectionModel().select(0);
        lineChart.getData().add(createSeries());

        sliderLight.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderLight.getValue() - sliderLight.getMin())
                / (sliderLight.getMax() - sliderLight.getMin()) * 100.0;
            return String.format("-slider-track-color: linear-gradient(to top, -slider-filled-track-color 0%%, "
                + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);", percentage, percentage);
        }, sliderLight.valueProperty(), sliderLight.minProperty(), sliderLight.maxProperty()));
    }

    public void initializeUI() {
        initializeSvg();
    }

    @FXML
    public void minLevelAction(ActionEvent event) {
        sliderLight.setValue(sliderLight.getMin());
    }

    @FXML
    public void maxLevelAction(ActionEvent event) {
        sliderLight.setValue(sliderLight.getMax());
    }

    @FXML
    public void upLevelAction(ActionEvent event) {
        int level = (int) sliderLight.getValue();
        if (level < sliderLight.getMax()) {
            sliderLight.setValue(level + 1);
        }
    }

    @FXML
    public void downLevelAction(ActionEvent event) {
        int level = (int) sliderLight.getValue();
        if (level > sliderLight.getMin()) {
            sliderLight.setValue(level - 1);
        }
    }

    @FXML
    public void directLevelAction(ActionEvent event) {
        sliderLight.setValue(Integer.parseInt(txtDirectLevel.getText()));
    }

    @FXML
    public void directLevelEnterAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            directLevelAction(null);
        }
    }

    private void initializeSvg() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(getClass().getResourceAsStream("/com/tlcsdm/qe/static/icon/lighting.svg"));
            org.w3c.dom.Node nodeLighting = doc.getElementsByTagName("path").item(0);
            org.w3c.dom.Node nodeBase = doc.getElementsByTagName("path").item(1);
            parseXmlToSvg(svgLighting, nodeLighting);
            parseXmlToSvg(svgBase, nodeBase);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            StaticLog.error(e, "parse lighting.svg failed!");
        }
    }

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
    }

    private Series<Integer, Double> createSeries() {
        Series<Integer, Double> series = new Series<>();
        for (int i = 0; i < 256; i++) {
            series.getData().add(new Data<>(i, daliDimmingCurve(i)));
        }
        return series;
    }

    private double daliDimmingCurve(int x) {
        if (x == 255) {
            return 100;
        } else if (x == 0) {
            return 0;
        } else {
            return Math.pow(10, (double) (x - 1) * 3 / 253 - 1);
        }
    }
}
