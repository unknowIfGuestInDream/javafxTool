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

package com.tlcsdm.smc.unitDesign;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.cell.CellLocation;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 根据DTS的trigger source文档生成相应的UD文档，协助UD开发
 *
 * @author: unknowIfGuestInDream
 * @date: 2022/12/8 23:12
 */
public class DtsTriggerSourceDoc extends SmcSample {

    private TextField excelField;
    private FileChooser excelFileChooser;
    private TextField outputField;
    private DirectoryChooser outputChooser;
    private TextField groupField;
    private TextArea deviceNameAndStartColField;
    private TextField templateField;
    private FileChooser templateChooser;
    private TextField sheetNameField;
    private TextField conditionColField;
    private NumberTextField startRowField;
    private NumberTextField endRowField;
    private NumberTextField beginWriteRowNumField;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();
    private final String defaultTemplateName = "DTS_request_table.xlsx";
    private final String defaultTemplatePath = "com/tlcsdm/smc/static/templates/DTS_request_table.xlsx";

    @Override
    public boolean isVisible() {
        return true;
    }

    private final Action download = FxAction.download("下载模板", actionEvent -> {
        InputStream templateFile = ResourceUtil.getStream(defaultTemplatePath);
        FileUtil.writeFromStream(templateFile, "E:\\testPlace\\result\\DTS_request_table.xlsx");
        notificationBuilder.text("General successfully.");
        notificationBuilder.showInformation();
    });

    private final Action generate = FxAction.generate(actionEvent -> {
        // 输入值获取
        String parentDirectoryPath = FileUtil.getParent(excelField.getText(), 1);
        List<String> groups = StrUtil.splitTrim(groupField.getText(), ",");
        String excelName = FileUtil.getName(excelField.getText());
        String excel = excelField.getText();
        String outputPath = outputField.getText();
        int groupNum = groups.size();
        String deviceNameAndStartCol = deviceNameAndStartColField.getText();
        String sheetName = sheetNameField.getText();
        String conditionCol = conditionColField.getText();
        int startRow = Integer.parseInt(startRowField.getText());
        int endRow = Integer.parseInt(endRowField.getText());
        int beginWriteRowNum = Integer.parseInt(beginWriteRowNumField.getText());
        String templatePath = templateField.getText();
        //所需变量赋值
        List<String> deviceNames = new ArrayList<>();
        List<String> startCols = new ArrayList<>();
        String resultFileName = defaultTemplateName;
        parseXmlConfig(deviceNameAndStartCol, deviceNames, startCols);
        //数据读取列
        List<ArrayList<String>> groupLines = new ArrayList<>();
        buildGroupLines(groupLines, startCols, groupNum);
        // trigger factor信息
        List<Map<Integer, String>> triggerFactorList = new ArrayList<>(128);
        List<Integer> triggerFactorRowNumList = new ArrayList<>(128);
        List<List<Map<Integer, String>>> conditionList = new ArrayList<>(128);
        //文件模板
        InputStream templateFile;
        if (StrUtil.isEmpty(templatePath)) {
            templateFile = ResourceUtil.getStream(defaultTemplatePath);
        } else {
            templateFile = FileUtil.getInputStream(templatePath);
            resultFileName = FileUtil.getName(templatePath);
        }
        CellLocation cellLocation = ExcelUtil.toLocation(conditionCol + beginWriteRowNum);
        int startConditionX = cellLocation.getX();
        // 处理数据
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(excel), sheetName);
        int initialCapacity = CoreUtil.newHashMapWithExpectedSize(groupNum);
        for (int i = startRow; i <= endRow; i++) {
            Map<Integer, String> map = new HashMap<>(initialCapacity);
            for (int j = 0; j < groupNum; j++) {
                String groupValue = reader.getCell(groups.get(j) + i).getStringCellValue();
                map.put(j, groupValue);
            }
            triggerFactorRowNumList.add(groupNum);
            triggerFactorList.add(map);
        }
        for (ArrayList<String> arrayList : groupLines) {
            List<Map<Integer, String>> list = new ArrayList<>();
            for (int i = startRow; i <= endRow; i++) {
                Map<Integer, String> map = new HashMap<>(initialCapacity);
                for (int j = 0; j < groupNum; j++) {
                    map.put(j, reader.getCell(arrayList.get(j) + i).getStringCellValue());
                }
                list.add(map);
            }
            conditionList.add(list);
        }
        reader.close();
        //数据写入

        notificationBuilder.text("General successfully.");
        notificationBuilder.showInformation();
        bindUserData();
    });

    private final Collection<? extends Action> actions = List.of(generate, download);

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

        Label excelLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.excel") + ": ");
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

        Label outputLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.output") + ": ");
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

        Label groupLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.startCell") + ": ");
        groupField = new TextField();
        groupField.setPrefWidth(Double.MAX_VALUE);
        groupField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label deviceNameAndStartColLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.startCell") + ": ");
        deviceNameAndStartColField = new TextArea();

        Label templateLabel = new Label("template: ");
        templateField = new TextField();
        templateChooser = new FileChooser();
        templateChooser.getExtensionFilters().add(extFilter);

        Button templateButton = FxButton.choose();
        templateField.setEditable(false);
        templateButton.setOnAction(arg0 -> {
            File file = templateChooser.showOpenDialog(stage);
            if (file != null) {
                templateField.setText(file.getPath());
                templateChooser.setInitialDirectory(file.getParentFile());
            }
        });

        Label sheetNameLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.startCell") + ": ");
        sheetNameField = new TextField();

        Label conditionColLabel = new Label("conditionCol: ");
        conditionColField = new TextField();

        Label startRowLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.macroLength") + ": ");
        startRowField = new NumberTextField();

        Label endRowLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.macroLength") + ": ");
        endRowField = new NumberTextField();

        Label beginWriteRowNumLabel = new Label("beginWriteRowNumField: ");
        beginWriteRowNumField = new NumberTextField();

        sheetNameField.setText("DTS trigger");
        conditionColField.setText("CL");
        startRowField.setNumber(BigDecimal.valueOf(5));
        endRowField.setNumber(BigDecimal.valueOf(132));
        beginWriteRowNumField.setNumber(BigDecimal.valueOf(3));
        deviceNameAndStartColField.setPromptText("多个实例请换行");

        userData.put("excel", excelField);
        userData.put("excelFileChooser", excelFileChooser);
        userData.put("output", outputField);
        userData.put("outputChooser", outputChooser);
        userData.put("group", groupField);
        userData.put("deviceNameAndStartCol", deviceNameAndStartColField);
        userData.put("template", templateField);
        userData.put("templateChooser", templateChooser);
        userData.put("sheetName", sheetNameField);
        userData.put("conditionCol", conditionColField);
        userData.put("startRow", startRowField);
        userData.put("endRow", endRowField);
        userData.put("beginWriteRowNum", beginWriteRowNumField);

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(excelLabel, 0, 1);
        grid.add(excelButton, 1, 1);
        grid.add(excelField, 2, 1);
        grid.add(outputLabel, 0, 2);
        grid.add(outputButton, 1, 2);
        grid.add(outputField, 2, 2);
        grid.add(groupLabel, 0, 3);
        grid.add(groupField, 1, 3, 2, 1);
        grid.add(deviceNameAndStartColLabel, 0, 4);
        grid.add(deviceNameAndStartColField, 1, 4, 2, 1);
        grid.add(templateLabel, 0, 5);
        grid.add(templateButton, 1, 5);
        grid.add(templateField, 2, 5);
        grid.add(sheetNameLabel, 0, 6);
        grid.add(sheetNameField, 1, 6, 2, 1);
        grid.add(conditionColLabel, 0, 7);
        grid.add(conditionColField, 1, 7, 2, 1);
        grid.add(startRowLabel, 0, 8);
        grid.add(startRowField, 1, 8, 2, 1);
        grid.add(endRowLabel, 0, 9);
        grid.add(endRowField, 1, 9, 2, 1);
        grid.add(beginWriteRowNumLabel, 0, 10);
        grid.add(beginWriteRowNumField, 1, 10, 2, 1);

        return grid;
    }

    @Override
    public Node getControlPanel() {
        String content = """
                {diffButton}:
                {diffDesc}

                {excelLabel}: {excelDesc}
                {generalLabel}: {generalDesc}
                {macroLengthLabel}: {macroLengthDesc}
                {markSheetLabel}: {markSheetDesc}

                {note}
                {noteDesc}
                """;

        Map<String, String> map = new HashMap<>(32);
        map.put("diffButton", generate.getText());
        map.put("diffDesc", I18nUtils.get("smc.tool.specGeneralTest.button.diff.desc"));
        map.put("Required", I18nUtils.get("smc.tool.control.required"));
        map.put("excelLabel", I18nUtils.get("smc.tool.specGeneralTest.label.excel"));
        map.put("excelDesc", "eg: TestSpec_General_RH850U2A.xlsx");
        map.put("generalLabel", I18nUtils.get("smc.tool.specGeneralTest.label.general"));
        map.put("generalDesc", "eg: {user.dir}\\src\\smc_gen\\general");
        map.put("macroLengthLabel", I18nUtils.get("smc.tool.specGeneralTest.label.macroLength"));
        map.put("macroLengthDesc", I18nUtils.get("smc.tool.specGeneralTest.control.macroLengthDesc"));
        map.put("markSheetLabel", I18nUtils.get("smc.tool.specGeneralTest.label.markSheet"));
        map.put("markSheetDesc", I18nUtils.get("smc.tool.specGeneralTest.control.markSheetDesc"));
        map.put("note", I18nUtils.get("smc.tool.control.note"));
        map.put("noteDesc", I18nUtils.get("smc.tool.specGeneralTest.control.noteDesc"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "dtsTriggerSourceDoc";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.dtsTriggerSourceDoc");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public String getOrderKey() {
        return "DtsTriggerSourceDoc";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.dtsTriggerSourceDoc.description");
    }

    /**
     * 解析 device和StartCol 的配置，获取device名和读取的开始列信息
     */
    private void parseXmlConfig(String deviceNameAndStartCol, List<String> deviceNames, List<String> startCols) {
        List<String> xmlConfigs = StrUtil.splitTrim(deviceNameAndStartCol, "\n");
        for (String xmlConfig : xmlConfigs) {
            List<String> l = StrUtil.split(xmlConfig, ";");
            deviceNames.add(l.get(0));
            startCols.add(l.get(1));
        }
        if (deviceNames.size() != startCols.size()) {
            FxAlerts.exception(new UnExpectedResultException());
        }
    }

    /**
     * groupLines 数据列明配置
     */
    private void buildGroupLines(List<ArrayList<String>> groupLines, List<String> startCols, int groupNum) {
        for (String col : startCols) {
            ArrayList<String> l = new ArrayList<>();
            l.add(col);
            int startCol = ExcelUtil.colNameToIndex(col);
            for (int j = 1; j < groupNum; j++) {
                String getGroupLine = ExcelUtil.indexToColName(startCol + j);
                l.add(getGroupLine);
            }
            groupLines.add(l);
        }
    }

}
