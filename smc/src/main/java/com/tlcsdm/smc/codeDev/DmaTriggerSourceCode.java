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
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.multi.ListValueMap;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.util.FreemarkerUtil;
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
import java.nio.charset.Charset;
import java.util.*;

/**
 * 根据DMA的trigger source文档生成setting, binding, h代码
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/3/10 23:12
 */
public class DmaTriggerSourceCode extends SmcSample {

    private TextField excelField;
    private FileChooser excelFileChooser;
    private TextField outputField;
    private DirectoryChooser outputChooser;
    private TextField groupField;
    private TextArea deviceAndStartColField;
    private TextField sheetNameField;
    private NumberTextField startRowField;
    private NumberTextField endRowField;
    private NumberTextField offsetField;
    private NumberTextField defineLengthField;
    private TextField macroTemplateField;

    private final String defaultTemplateName = "dmaTemplate.zip";
    // 结果输出到 dmaCode 文件夹下
    private final String outParentFolder = "\\dmaCode";
    private final FileChooser downloadChooser = new FileChooser();
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final String templateBindingPath = "smc/dmaTriggerSourceCode/binding.ftl";
    private final String templateSettingPath = "smc/dmaTriggerSourceCode/setting.ftl";
    private final String templateCgdmaPath = "smc/dmaTriggerSourceCode/cgdma.ftl";

    private final Action openOutDir = FxAction.openOutDir(actionEvent -> {
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

    private final Action download = FxAction.download(I18nUtils.get("smc.tool.dmaTriggerSourceCode.button.download"),
            actionEvent -> {
                downloadChooser.setInitialFileName(defaultTemplateName);
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("zip", "*.zip");
                downloadChooser.getExtensionFilters().add(extFilter);
                File file = downloadChooser.showSaveDialog(FxApp.primaryStage);
                if (file != null) {
                    if (!StrUtil.endWith(file.getName(), ".zip")) {
                        notificationBuilder
                                .text(I18nUtils.get("smc.tool.dmaTriggerSourceCode.button.download.warn.message"));
                        notificationBuilder.showWarning();
                        return;
                    }
                    if (file.exists()) {
                        FileUtil.del(file);
                    }
                    ZipUtil.zip(file, Charset.defaultCharset(),
                            new ClassPathResource(
                                    "com/tlcsdm/smc/static/templates/smc/dmaTriggerSourceCode/binding.ftl",
                                    getClass().getClassLoader()),
                            new ClassPathResource("com/tlcsdm/smc/static/templates/smc/dmaTriggerSourceCode/cgdma.ftl",
                                    getClass().getClassLoader()),
                            new ClassPathResource(
                                    "com/tlcsdm/smc/static/templates/smc/dmaTriggerSourceCode/setting.ftl",
                                    getClass().getClassLoader()));

                    notificationBuilder
                            .text(I18nUtils.get("smc.tool.button.download.success"));
                    notificationBuilder.showInformation();
                }
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
        List<TransferRequest> transferRequests = new ArrayList<>(xmlConfigs.size());
        for (String xmlConfig : xmlConfigs) {
            List<String> l = StrUtil.split(xmlConfig, ";");
            TransferRequest transferRequest = new TransferRequest(l.get(0), l.get(1), l.get(2));
            transferRequests.add(transferRequest);
        }

        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        // 处理数据
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(excel), sheetName);
        // 文件内容获取
        int groupSize = groups.size();
        int factorSize = endRow - startRow + 1;
        int initCapacity = groupSize * factorSize;
        List<Map<String, Object>> bindingContent = new ArrayList<>(initCapacity);
        List<Map<String, Object>> cgdmaContent = new ArrayList<>(initCapacity);
        List<Map<String, Object>> groupList = new ArrayList<>(groupSize);

        Map<String, Object> map = new HashMap<>();
        map.put("offset", offsetString);
        map.put("groups", groupList);
        map.put("bindingContent", bindingContent);
        map.put("cgdmaContent", cgdmaContent);
        int groupNum = 0;
        for (String group : groups) {
            Map<String, Object> g = new HashMap<>();
            List<Map<String, Object>> settingContent = new ArrayList<>(factorSize);
            g.put("groupNum", groupNum);
            g.put("settingContent", settingContent);

            String defaultSelection = "";
            // 模板赋值使用
            Map<String, String> paramMap = MapUtil.builder("offset", offsetString)
                    .put("groupNum", String.valueOf(groupNum)).build();
            for (int i = startRow; i <= endRow; i++) {
                Map<String, Object> setting = new HashMap<>();
                Map<String, Object> cgdma = new HashMap<>();
                Map<String, Object> binding = new HashMap<>();
                String factor = reader.getCell(group + i).getStringCellValue();
                if ("Reserve".equals(factor)) {
                    continue;
                }
                if (defaultSelection.length() == 0) {
                    defaultSelection = factor;
                }
                paramMap.put("factor", factor);
                String macro = StrUtil.format(macroTemplate, paramMap);
                // setting
                setting.put("factor", factor);
                boolean hasCondition = false;
                ListValueMap<String, String> entries = new ListValueMap<>();
                for (TransferRequest transferRequest : transferRequests) {
                    int x = groupNum + ExcelUtil.colNameToIndex(transferRequest.startCol);
                    if ("-".equals(reader.getCell(x, i - 1).getStringCellValue())) {
                        hasCondition = true;
                    } else {
                        entries.putValue(transferRequest.device, transferRequest.pins);
                    }
                }
                setting.put("hasCondition", hasCondition);
                if (hasCondition) {
                    StringJoiner parameter = new StringJoiner("||");
                    for (String key : entries.keySet()) {
                        List<String> pinList = entries.get(key);
                        StringJoiner parameterMeta = new StringJoiner(";", key + "##", "");
                        for (String pin : pinList) {
                            parameterMeta.add(pin);
                        }
                        parameter.add(parameterMeta.toString());
                    }
                    setting.put("parameter", parameter.toString());
                }
                // binding
                binding.put("factor", factor);
                binding.put("groupNum", groupNum);
                binding.put("macro", macro);
                // cgdma
                cgdma.put("factor", factor);
                cgdma.put("groupNum", groupNum);
                cgdma.put("macro", macro);
                cgdma.put("hex", "0x" + String.format("%08x", i - startRow).toUpperCase() + "UL");
                cgdma.put("offset", " ");
                if (macro.length() < defineLength - 8) {
                    cgdma.put("offset", CharSequenceUtil.repeat(" ", defineLength - macro.length() - 8));
                }
                cgdmaContent.add(cgdma);
                bindingContent.add(binding);
                settingContent.add(setting);
            }
            g.put("defaultSelection", defaultSelection);
            // 后置处理
            groupList.add(g);
            // 当前循环结束，开始下一次循环
            groupNum++;
        }

        File setting = FileUtil.newFile(resultPath + "\\setting.xml");
        FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(map, templateSettingPath), setting);
        File binding = FileUtil.newFile(resultPath + "\\binding.xml");
        FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(map, templateBindingPath), binding);
        File cgdma = FileUtil.newFile(resultPath + "\\r_cg_dma.h");
        FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(map, templateCgdmaPath), cgdma);
        reader.close();

        notificationBuilder.text(I18nUtils.get("smc.tool.dtsTriggerSourceXml.button.generate.success"));
        notificationBuilder.showInformation();
        bindUserData();
    });

    private final Collection<? extends Action> actions = List.of(generate, download, openOutDir);

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

    /**
     * Template 设置
     */
    private TitledPane createTemplateControl() {
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

    @Override
    public Node getControlPanel() {
        String content = """
                {templateDesc}

                {deviceAndStartColLabel}: {deviceAndStartColDesc}
                """;

        Map<String, String> map = new HashMap<>(8);
        map.put("deviceAndStartColLabel", I18nUtils.get("smc.tool.dmaTriggerSourceCode.label.deviceAndStartCol"));
        map.put("deviceAndStartColDesc", I18nUtils.get("smc.tool.dmaTriggerSourceCode.control.deviceAndStartColDesc"));
        map.put("templateDesc", I18nUtils.get("smc.tool.dmaTriggerSourceCode.control.templateDesc"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "dmaTriggerSourceCode";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.dmaTriggerSourceCode");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public String getOrderKey() {
        return "DmaTriggerSourceCode";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.dmaTriggerSourceCode.description");
    }

    /**
     * device 相关信息
     */
    record TransferRequest(String device, String pins, String startCol) {
    }

}
