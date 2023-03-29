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

package com.tlcsdm.smc.codeDev.ecm;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * EcmScript脚本超类
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/3/26 21:13
 */
public abstract class AbstractEcmScript extends SmcSample {

    protected TextField excelField;
    protected FileChooser excelFileChooser;
    protected TextField outputField;
    protected DirectoryChooser outputChooser;
    protected TextField groupField;
    protected TextArea deviceAndStartColField;
    protected TextField sheetNameField;
    protected NumberTextField startRowField;
    protected NumberTextField endRowField;
    protected NumberTextField offsetField;
    protected NumberTextField defineLengthField;
    protected TextField macroTemplateField;

    protected final String outParentFolder = "\\ecm";
    protected final FileChooser downloadChooser = new FileChooser();
    protected final Notifications notificationBuilder = FxNotifications.defaultNotify();

    protected final Action openOutDir = FxAction.openOutDir(actionEvent -> {
        String outPath = outputField.getText();
        if (StrUtil.isEmpty(outPath)) {
            notificationBuilder.text(I18nUtils.get("smc.tool.button.openOutDir.warnMsg"));
            notificationBuilder.showWarning();
            return;
        }
        String path = outPath + outParentFolder;
        if (!FileUtil.exist(path)) {
            path = outPath;
        }
        JavaFxSystemUtil.openDirectory(path);
    });

    private final Action generate = FxAction.generate(actionEvent -> {
        // 输入值获取
        String excel = excelField.getText();
        List<String> groups = StrUtil.splitTrim(groupField.getText(), ",");
        String outputPath = outputField.getText();
        String deviceAndStartCol = deviceAndStartColField.getText();
        String sheetName = sheetNameField.getText();
        int startRow = Integer.parseInt(startRowField.getText());
        int endRow = Integer.parseInt(endRowField.getText());
        int offset = Integer.parseInt(offsetField.getText());
        int defineLength = Integer.parseInt(defineLengthField.getText());
        String macroTemplate = macroTemplateField.getText();

        String resultPath = outputPath + outParentFolder;
        String offsetString = CharSequenceUtil.repeat(" ", offset);
        List<String> xmlConfigs = StrUtil.splitTrim(deviceAndStartCol, "\n");

        //Category
        String categorySheetName = "Category";
        int categoryStartRow = 3;
        String categorys = """
                categoryId;F
                categoryEnName;G
                categoryJpName;H
                """;
        // device
        String deviceSheetName = "U2A";
        int deviceStartRow = 3;
        String functions = """
                optMaskint;G
                optIntg;G
                optDCLS;G
                optIntrg;I
                optErroroutput;J
                optErrort;K
                optDelayt;L
                """;
        String errorSourceIdCol = "A";
        String categoryIdCol = "B";
        String errorSourceNumberCol = "C";
        String errorSourceenNameCol = "D";
        String errorSourceDescCol = "E";
        String errorSourcejpNameCol = "W";

        String products = """
                RH850U2A16;516;N
                RH850U2A16;373;O
                RH850U2A16;292;P
                RH850U2A8;373;R
                RH850U2A8;292;S
                RH850U2A6;292;T
                RH850U2A6;176;T
                RH850U2A6;156;U
                RH850U2A6;144;V
                """;

        notificationBuilder.text(I18nUtils.get("smc.tool.dtsTriggerSourceXml.button.generate.success"));
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

        Label excelLabel = new Label(I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.excel") + ": ");
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

        Label outputLabel = new Label(I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.output") + ": ");
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

        Label groupLabel = new Label(I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.group") + ": ");
        groupField = new TextField();
        groupField.setPrefWidth(Double.MAX_VALUE);
        groupField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label deviceAndStartColLabel = new Label(
                I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.deviceAndStartCol") + ": ");
        deviceAndStartColField = new TextArea();

        Label sheetNameLabel = new Label(I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.sheetName") + ": ");
        sheetNameField = new TextField();

        Label startRowLabel = new Label(I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.startRow") + ": ");
        startRowField = new NumberTextField();

        Label endRowLabel = new Label(I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.endRow") + ": ");
        endRowField = new NumberTextField();

        TitledPane templatePane = createTemplateControl();
        // 折叠面板
        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(templatePane);
        accordion.setExpandedPane(templatePane);

        sheetNameField.setText("sDMAC transfer request");
        startRowField.setNumber(BigDecimal.valueOf(5));
        endRowField.setNumber(BigDecimal.valueOf(260));
        deviceAndStartColField
                .setPromptText(I18nUtils.get("smc.tool.dmaTriggerSourceCode.textfield.deviceAndStartCol.promptText"));
        offsetField.setNumber(BigDecimal.valueOf(4));
        defineLengthField.setNumber(BigDecimal.valueOf(60));
        macroTemplateField.setText("_DMAC_GRP{groupNum}_REQUEST_{factor}");

        userData.put("excel", excelField);
        userData.put("excelFileChooser", excelFileChooser);
        userData.put("output", outputField);
        userData.put("outputChooser", outputChooser);
        userData.put("group", groupField);
        userData.put("deviceAndStartCol", deviceAndStartColField);
        userData.put("sheetName", sheetNameField);
        userData.put("startRow", startRowField);
        userData.put("endRow", endRowField);
        userData.put("offset", offsetField);
        userData.put("defineLength", defineLengthField);
        userData.put("macroTemplate", macroTemplateField);

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(excelLabel, 0, 1);
        grid.add(excelButton, 1, 1);
        grid.add(excelField, 2, 1);
        grid.add(outputLabel, 0, 2);
        grid.add(outputButton, 1, 2);
        grid.add(outputField, 2, 2);
        grid.add(groupLabel, 0, 3);
        grid.add(groupField, 1, 3, 2, 1);
        grid.add(deviceAndStartColLabel, 0, 4);
        grid.add(deviceAndStartColField, 1, 4, 2, 1);
        grid.add(sheetNameLabel, 0, 5);
        grid.add(sheetNameField, 1, 5, 2, 1);
        grid.add(startRowLabel, 0, 6);
        grid.add(startRowField, 1, 6, 2, 1);
        grid.add(endRowLabel, 0, 7);
        grid.add(endRowField, 1, 7, 2, 1);
        grid.add(accordion, 0, 8, 3, 1);

        return grid;
    }

    protected TitledPane createTemplateControl() {
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(5));

        Label offsetLabel = new Label(I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.offset") + ": ");
        offsetField = new NumberTextField();
        offsetField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(offsetField, Priority.ALWAYS);

        Label defineLengthLabel = new Label(I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.defineLength") + ": ");
        defineLengthField = new NumberTextField();
        defineLengthField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(defineLengthField, Priority.ALWAYS);

        Label macroTemplateLabel = new Label(I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.macroTemplate") + ": ");
        macroTemplateField = new TextField();

        grid.add(offsetLabel, 0, 0);
        grid.add(offsetField, 1, 0);
        grid.add(defineLengthLabel, 0, 1);
        grid.add(defineLengthField, 1, 1);
        grid.add(macroTemplateLabel, 0, 2);
        grid.add(macroTemplateField, 1, 2);

        return new TitledPane(I18nUtils.get("smc.tool.dmaTriggerSourceCode.title.template"), grid);
    }

    protected abstract String getFtlPath();
}
