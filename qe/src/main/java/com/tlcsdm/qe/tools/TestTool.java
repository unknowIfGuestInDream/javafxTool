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

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.control.CustomTextField;
import com.tlcsdm.core.javafx.control.DecorationTextfield;
import com.tlcsdm.core.javafx.control.DecorationTextfield2;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.RangeSlider;
import com.tlcsdm.core.javafx.control.Severity;
import com.tlcsdm.core.javafx.control.Switch;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.helper.ImageViewHelper;
import com.tlcsdm.core.javafx.util.FxXmlUtil;
import com.tlcsdm.core.logging.logback.ConsoleLogAppender;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.qe.QeSample;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.control.PropertySheet.Mode;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.action.ActionUtils.ActionTextBehavior;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 测试用，发布时设置可见性为false.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.0
 */
public class TestTool extends QeSample {

    private TextField originalField;
    private TextField compareField;
    private TextField outputField;
    private CustomTextField customTextField1;
    private DecorationTextfield decorationTextfield1;

    private static final Map<String, Object> customDataMap = new LinkedHashMap<>();

    static {
        customDataMap.put("1. Name#First Name", "Jonathan");
        customDataMap.put("1. Name#Last Name", "Giles");
        customDataMap.put("1. Name#Birthday", LocalDate.of(1985, Month.JANUARY, 12));
        customDataMap.put("2. Billing Address#Address 1", "");
        customDataMap.put("2. Billing Address#Address 2", "");
        customDataMap.put("2. Billing Address#City", "");
        customDataMap.put("2. Billing Address#State", "");
        customDataMap.put("2. Billing Address#Zip", "");
        customDataMap.put("3. Phone#Home", "123-123-1234");
        customDataMap.put("3. Phone#Mobile", "234-234-2345");
        customDataMap.put("3. Phone#Work", "");
    }

    private final PropertySheet propertySheet = new PropertySheet();

    private final Action generate = FxAction.generate(actionEvent -> {
        customTextField1.setOffsetX(17);
        decorationTextfield1.setDecoration(Severity.ERROR, "error");
        //        TooltipUtil.showToast("title", "message");
        //        ProgressStage ps = ProgressStage.of();
        //        ps.show();
        //
        //        ThreadPoolTaskExecutor.get().execute(() -> {
        //            ThreadUtil.safeSleep(5000);
        //            ps.close();
        //        });

        //        StaticLog.info("hello log");
        //        StaticLog.error("hello log");
        //        StaticLog.warn("hello log");

        //        GroovyUtil.invokeMethod("test.groovy", "hello");

        //        Map<String, Object> map = new HashMap<>();
        //        map.put("args", new String[] { "8000", "E:\\javaWorkSpace\\javafxTool\\docs", "docs" });
        //        GroovyUtil.run("SimpleHttpServer.groovy", map);

        //        GroovyUtil.simpleHttpServer(8000, CoreUtil.getRootPath() + File.separator + "docs", "docs");

        //        GroovyUtil.run("test.groovy");
    });

    private final Collection<? extends Action> actions = List.of(generate);

    @Override
    public boolean isVisible() {
        String value = System.getProperty(CoreConstant.JVM_WORKENV);
        return CoreConstant.JVM_WORKENV_TEST.equals(value);
    }

    @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(2);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));
        //
        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);
        //
        //        // original
        //        Label originalLabel = new Label(I18nUtils.get("smc.tool.fileDiff.label.original") + ": ");
        //        originalField = new TextField();
        //        originalField.setMaxWidth(Double.MAX_VALUE);
        //
        //        // compare
        //        Label compareLabel = new Label(I18nUtils.get("smc.tool.fileDiff.label.compare") + ": ");
        //        compareField = new TextField();
        //        compareField.setMaxWidth(Double.MAX_VALUE);
        //
        //        // output
        //        Label outputLabel = new Label(I18nUtils.get("smc.tool.fileDiff.label.output") + ": ");
        //        outputField = new TextField();
        //        outputField.setMaxWidth(Double.MAX_VALUE);
        //
        //        userData.put("original", originalField);
        //        userData.put("compare", compareField);
        //        userData.put("output", outputField);
        //
        TextArea textArea = new TextArea();
        textArea.setFocusTraversable(true);
        ConsoleLogAppender.textAreaList.add(textArea);
        propertySheet.getItems().setAll(getCustomModelProperties());
        propertySheet.setMode(Mode.CATEGORY);

        grid.add(toolBar, 0, 0, 2, 1);
        grid.add(propertySheet, 0, 1, 2, 1);
        grid.add(textArea, 0, 2, 2, 1);

        Region horizontalRangeSlider = createHorizontalSlider();
        grid.add(horizontalRangeSlider, 0, 3, 2, 1);

        customTextField1 = new CustomTextField();
        customTextField1.setStyle("-fx-custom-offsetX: 25;");
        customTextField1.setRight(ImageViewHelper.get("folder"));
        grid.add(customTextField1, 0, 4, 2, 1);

        DecorationTextfield2 decorationTextfield = new DecorationTextfield2();
        decorationTextfield.setDecoration(Severity.ERROR);
        decorationTextfield.setStyle("-fx-custom-offsetX: 25;");
        grid.add(decorationTextfield, 0, 5, 2, 1);

        decorationTextfield1 = new DecorationTextfield("DecorationTextfield");
        grid.add(decorationTextfield1, 0, 6, 2, 1);
        decorationTextfield1.setDecoration(Severity.WARNING, "warn");

        ObservableList<String> stringList = FXCollections.observableArrayList("1111", "2222", "Aaaaa", "Abbbb", "Abccc",
            "Abcdd", "Abcde", "Bbbb", "bbbb", "Cccc", "Dddd", "Eeee", "Ffff", "gggg", "hhhh", "3333");
        SearchableComboBox<String> searchableStringBox = new SearchableComboBox<>();
        searchableStringBox.setItems(stringList);
        searchableStringBox.setMaxWidth(Double.MAX_VALUE);
        grid.add(searchableStringBox, 0, 7, 2, 1);

        Switch sw = new Switch("switch");
        grid.add(sw, 0, 8, 2, 1);
        //        grid.add(originalField, 1, 1);
        //        grid.add(compareLabel, 0, 2);
        //        grid.add(compareField, 1, 2);
        //        grid.add(outputLabel, 0, 3);
        //        grid.add(outputField, 1, 3);

        return grid;
    }

    Region createHorizontalSlider() {
        final TextField minField = new TextField();
        minField.setPrefColumnCount(5);
        final TextField maxField = new TextField();
        maxField.setPrefColumnCount(5);

        final RangeSlider hSlider = new RangeSlider(0, 100, 10, 90);
        hSlider.setShowTickMarks(true);
        hSlider.setShowTickLabels(true);
        hSlider.setBlockIncrement(10);
        hSlider.setPrefWidth(200);

        minField.setText("" + hSlider.getLowValue());
        maxField.setText("" + hSlider.getHighValue());

        minField.setEditable(false);
        minField.setPromptText("Min");

        maxField.setEditable(false);
        maxField.setPromptText("Max");

        minField.textProperty().bind(hSlider.lowValueProperty().asString("%.2f"));
        maxField.textProperty().bind(hSlider.highValueProperty().asString("%.2f"));

        HBox box = new HBox(10);
        box.getChildren().addAll(minField, hSlider, maxField);
        box.setPadding(new Insets(20, 0, 0, 20));
        box.setFillHeight(false);

        return box;
    }

    @Override
    protected void updateForVersionUpgrade() {
        FxXmlUtil.del(getSampleXmlPrefix(), "original");
        FxXmlUtil.del(getSampleXmlPrefix(), "compare");
        FxXmlUtil.del(getSampleXmlPrefix(), "output");
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
        return "testTool";
    }

    @Override
    public String getSampleName() {
        return "测试组件";
    }

    @Override
    public String getOrderKey() {
        return "testTool";
    }

    @Override
    public String getSampleDescription() {
        return "此组件测试用";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0-Beta";
    }

    private ObservableList<Item> getCustomModelProperties() {
        ObservableList<Item> list = FXCollections.observableArrayList();
        for (String key : customDataMap.keySet()) {
            list.add(new CustomPropertyItem(key));
        }
        return list;
    }

    static class CustomPropertyItem implements Item {

        private final String key;
        private final String category;
        private final String name;

        public CustomPropertyItem(String key) {
            this.key = key;
            String[] skey = key.split("#");
            category = skey[0];
            name = skey[1];
        }

        @Override
        public Class<?> getType() {
            return customDataMap.get(key).getClass();
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public Object getValue() {
            return customDataMap.get(key);
        }

        @Override
        public void setValue(Object value) {
            customDataMap.put(key, value);
        }

        @Override
        public Optional<ObservableValue<?>> getObservableValue() {
            return Optional.empty();
        }

    }

    @SuppressWarnings("All")
    class VideoConvertWork extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            ThreadUtil.safeSleep(5000);
            return null;
        }
    }

    public SVGPath loadPathToMM(String pathName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        SVGPath path = new SVGPath();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 禁止DTD验证,防止网络阻塞
            builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
            Document d = builder.parse(getClass().getResourceAsStream(pathName));
            org.w3c.dom.Node node = d.getElementsByTagName("path").item(0);
            String content = node.getAttributes().getNamedItem("d").getNodeValue();

            path.setContent(content);

        } catch (Exception e) {
            StaticLog.error(e);
        }
        return path;
    }

}
