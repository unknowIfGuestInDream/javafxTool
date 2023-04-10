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
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.util.FxXmlUtil;
import com.tlcsdm.core.javafx.util.TooltipUtil;
import com.tlcsdm.core.logging.logback.ConsoleLogAppender;
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
import javafx.stage.Stage;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.control.PropertySheet.Mode;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.action.ActionUtils.ActionTextBehavior;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * 测试用，发布时设置可见性为false
 *
 * @author unknowIfGuestInDream
 */
public class TestTool extends QeSample {

    private TextField originalField;
    private TextField compareField;
    private TextField outputField;

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
        TooltipUtil.showToast("message");
//        ProgressStage ps = ProgressStage.of();
//        ps.show();
//
//        ThreadPoolTaskExecutor.get().execute(() -> {
//            ThreadUtil.safeSleep(5000);
//            ps.close();
//        });

        StaticLog.info("hello log");
        StaticLog.error("hello log");
        StaticLog.warn("hello log");
    });

    private final Collection<? extends Action> actions = List.of(generate);

    @Override
    public boolean isVisible() {
        return true;
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
//        grid.add(originalField, 1, 1);
//        grid.add(compareLabel, 0, 2);
//        grid.add(compareField, 1, 2);
//        grid.add(outputLabel, 0, 3);
//        grid.add(outputField, 1, 3);

        return grid;
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

    class CustomPropertyItem implements Item {

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
        public Optional<ObservableValue<? extends Object>> getObservableValue() {
            return Optional.empty();
        }

    }

    class VideoConvertWork extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            ThreadUtil.safeSleep(5000);
            return null;
        }
    }

}
