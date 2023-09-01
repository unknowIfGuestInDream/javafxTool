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

package com.tlcsdm.smc.codeDev;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.bind.MultiTextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.bind.TextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据DTS的trigger source文档生成xml数据文件，协助CD开发
 *
 * @author unknowIfGuestInDream
 * @date 2022/12/8 23:12
 */
public class DtsTriggerSourceXml extends SmcSample {

    private TextField excelField;
    private FileChooser excelFileChooser;
    private TextField outputField;
    private DirectoryChooser outputChooser;
    private TextField groupField;
    private TextArea xmlFileNameAndStartColField;
    private TextField sheetNameField;
    private NumberTextField startRowField;
    private NumberTextField endRowField;
    private TextField xmlNameTemplateField;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action openOutDir = FxAction.openOutDir(actionEvent -> {
        String outPath = outputField.getText();
        if (StrUtil.isEmpty(outPath)) {
            notificationBuilder.text(I18nUtils.get("smc.tool.button.openOutDir.warnMsg"));
            notificationBuilder.showWarning();
            return;
        }
        String path = outPath + File.separator + "triggerSource";
        if (!FileUtil.exist(path)) {
            path = outPath;
        }
        JavaFxSystemUtil.openDirectory(path);
    });

    private final Action generate = FxAction.generate(actionEvent -> {
        StaticLog.info("Extracting data...");
        // 输入值获取
        String parentDirectoryPath = FileUtil.getParent(excelField.getText(), 1);
        List<String> groups = StrUtil.splitTrim(groupField.getText(), ",");
        String excelName = FileUtil.getName(excelField.getText());
        String outputPath = outputField.getText();
        int groupNum = groups.size();
        String xmlFileNameAndStartCol = xmlFileNameAndStartColField.getText();
        String sheetName = sheetNameField.getText();
        int startRow = Integer.parseInt(startRowField.getText());
        int endRow = Integer.parseInt(endRowField.getText());
        String xmlNameTemplate = xmlNameTemplateField.getText();

        List<String> xmlFileNames = new ArrayList<>();
        List<String> startCols = new ArrayList<>();
        parseXmlConfig(xmlFileNameAndStartCol, xmlFileNames, startCols);

        String resultPath = outputPath + File.separator + "triggerSource";
        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        StaticLog.info("Processing data...");
        // 处理数据
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName), sheetName);
        for (int i = 0; i < xmlFileNames.size(); i++) {
            File file = FileUtil
                .newFile(resultPath + File.separator + StrUtil.format(xmlNameTemplate, xmlFileNames.get(i)));
            if (file.exists()) {
                FileUtil.del(file);
            }
            List<String> contentsList = new ArrayList<>();
            contentsList.add("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            contentsList.add("<!DOCTYPE xml>");
            contentsList.add("<!-- this file was auto-generated. Do not modify it manually -->");
            contentsList.add("<DTCTriggerSource>");
            contentsList.add("    <Dependence Dependence=\"\" />");

            int startCol = ExcelUtil.colNameToIndex(startCols.get(i));
            for (int j = startRow; j <= endRow; j++) {
                contentsList.add("    <TriggerSource Channel=\"" + (j - startRow) + "\"");
                for (int k = 0; k < groupNum; k++) {
                    String getGroupLine = ExcelUtil.indexToColName(startCol + k);
                    String content = "        Group" + k + "TriggerInfo=\""
                        + getXmlGroupValue(reader, getGroupLine + j, groups.get(k) + j) + "\"";
                    if (k == groupNum - 1) {
                        content += " />";
                    }
                    contentsList.add(content);
                }
            }

            contentsList.add("</DTCTriggerSource>");
            contentsList.add("");
            FileUtil.appendUtf8Lines(contentsList, file);
        }
        reader.close();
        StaticLog.info("Generate result...");
        notificationBuilder.text(I18nUtils.get("smc.tool.button.generate.success"));
        notificationBuilder.showInformation();
        bindUserData();
    });

    private final Collection<? extends Action> actions = List.of(generate, openOutDir);

    @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel file", "*.xlsx");

        Label excelLabel = new Label(I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.excel") + ": ");
        excelField = new TextField();
        excelField.setMaxWidth(Double.MAX_VALUE);
        excelFileChooser = new FileChooser();
        excelFileChooser.getExtensionFilters().add(extFilter);

        Button excelButton = FxButton.choose();
        excelField.setEditable(false);
        excelButton.setOnAction(arg0 -> {
            File file = excelFileChooser.showOpenDialog(stage);
            if (file != null) {
                excelField.setText(file.getPath());
                excelFileChooser.setInitialDirectory(file.getParentFile());
            }
        });

        Label outputLabel = new Label(I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.output") + ": ");
        outputField = new TextField();
        outputField.setMaxWidth(Double.MAX_VALUE);
        outputChooser = new DirectoryChooser();
        Button outputButton = FxButton.choose();
        outputField.setEditable(false);
        outputButton.setOnAction(arg0 -> {
            File file = outputChooser.showDialog(stage);
            if (file != null) {
                outputField.setText(file.getPath());
                outputChooser.setInitialDirectory(file);
            }
        });

        Label groupLabel = new Label(I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.group") + ": ");
        groupField = new TextField();
        groupField.setPrefWidth(Double.MAX_VALUE);
        groupField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label xmlFileNameAndStartColLabel = new Label(
            I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.xmlFileNameAndStartCol") + ": ");
        xmlFileNameAndStartColField = new TextArea();

        Label sheetNameLabel = new Label(I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.sheetName") + ": ");
        sheetNameField = new TextField();

        Label startRowLabel = new Label(I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.startRow") + ": ");
        startRowField = new NumberTextField();

        Label endRowLabel = new Label(I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.endRow") + ": ");
        endRowField = new NumberTextField();

        Label xmlNameTemplateLabel = new Label(
            I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.xmlNameTemplate") + ": ");
        xmlNameTemplateField = new TextField();

        sheetNameField.setText("DTS trigger");
        startRowField.setNumber(BigDecimal.valueOf(5));
        endRowField.setNumber(BigDecimal.valueOf(132));
        xmlFileNameAndStartColField
            .setPromptText(I18nUtils.get("smc.tool.dtsTriggerSourceXml.textfield.xmlNameTemplate.promptText"));
        xmlNameTemplateField.setText("DTS{}TriggerSource.xml");

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(excelLabel, 0, 1);
        grid.add(excelButton, 1, 1);
        grid.add(excelField, 2, 1);
        grid.add(outputLabel, 0, 2);
        grid.add(outputButton, 1, 2);
        grid.add(outputField, 2, 2);
        grid.add(groupLabel, 0, 3);
        grid.add(groupField, 1, 3, 2, 1);
        grid.add(xmlFileNameAndStartColLabel, 0, 4);
        grid.add(xmlFileNameAndStartColField, 1, 4, 2, 1);
        grid.add(sheetNameLabel, 0, 5);
        grid.add(sheetNameField, 1, 5, 2, 1);
        grid.add(startRowLabel, 0, 6);
        grid.add(startRowField, 1, 6, 2, 1);
        grid.add(endRowLabel, 0, 7);
        grid.add(endRowField, 1, 7, 2, 1);
        grid.add(xmlNameTemplateLabel, 0, 8);
        grid.add(xmlNameTemplateField, 1, 8, 2, 1);

        return grid;
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        BooleanBinding outputValidation = new TextInputControlEmptyBinding(outputField).build();
        BooleanBinding emptyValidation = new MultiTextInputControlEmptyBinding(excelField, outputField, groupField,
            xmlFileNameAndStartColField, sheetNameField, startRowField, endRowField, xmlNameTemplateField).build();

        generate.disabledProperty().bind(emptyValidation);
        openOutDir.disabledProperty().bind(outputValidation);
        FileChooserUtil.setOnDrag(excelField, FileChooserUtil.FileType.FILE);
        FileChooserUtil.setOnDrag(outputField, FileChooserUtil.FileType.FOLDER);
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        userData.put("excel", excelField);
        userData.put("excelFileChooser", excelFileChooser);
        userData.put("output", outputField);
        userData.put("outputChooser", outputChooser);
        userData.put("group", groupField);
        userData.put("xmlFileNameAndStartCol", xmlFileNameAndStartColField);
        userData.put("sheetName", sheetNameField);
        userData.put("startRow", startRowField);
        userData.put("endRow", endRowField);
        userData.put("xmlNameTemplate", xmlNameTemplateField);
    }

    @Override
    public Node getControlPanel() {
        String content = """
            {excelLabel}: {excelDesc}
            {groupLabel}: {groupDesc}
            {xmlFileNameAndStartColLabel}: {xmlFileNameAndStartColDesc}
            eg: C8292;Q
            {xmlNameTemplateLabel}: {xmlNameTemplateDesc}
            """;

        Map<String, String> map = new HashMap<>(16);
        map.put("excelLabel", I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.excel"));
        map.put("excelDesc", "eg: DTS_Transfer_request_Table.xlsx");
        map.put("groupLabel", I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.group"));
        map.put("groupDesc", I18nUtils.get("smc.tool.dtsTriggerSourceXml.control.groupDesc"));
        map.put("xmlFileNameAndStartColLabel",
            I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.xmlFileNameAndStartCol"));
        map.put("xmlFileNameAndStartColDesc",
            I18nUtils.get("smc.tool.dtsTriggerSourceXml.control.xmlFileNameAndStartColDesc"));
        map.put("xmlNameTemplateLabel", I18nUtils.get("smc.tool.dtsTriggerSourceXml.label.xmlNameTemplate"));
        map.put("xmlNameTemplateDesc", I18nUtils.get("smc.tool.dtsTriggerSourceXml.control.xmlNameTemplateDesc"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "dtsTriggerSourceXml";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.dtsTriggerSourceXml");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/xml.png"));
    }

    @Override
    public String getOrderKey() {
        return "DtsTriggerSourceXml";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.dtsTriggerSourceXml.description");
    }

    /**
     * 解析 xmlFileNameAndStartCol 的配置，获取xml文件名和读取的列信息
     */
    private void parseXmlConfig(String xmlFileNameAndStartCol, List<String> xmlFileNames, List<String> startCols) {
        List<String> xmlConfigs = StrUtil.splitTrim(xmlFileNameAndStartCol, "\n");
        for (String xmlConfig : xmlConfigs) {
            List<String> l = StrUtil.split(xmlConfig, ";");
            xmlFileNames.add(l.get(0));
            startCols.add(l.get(1));
        }
        if (xmlFileNames.size() != startCols.size()) {
            FxAlerts.exception(new UnExpectedResultException());
        }
    }

    /**
     * 读取单元格的值
     */
    private String getXmlGroupValue(ExcelReader reader, String groupLineCell, String groupValueLineCell) {
        if ("-".equals(reader.getCell(groupLineCell).getStringCellValue())) {
            return "Reserved";
        }
        return reader.getCell(groupValueLineCell).getStringCellValue();
    }

}
