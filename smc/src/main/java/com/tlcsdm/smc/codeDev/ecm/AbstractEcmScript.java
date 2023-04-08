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
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.multi.ListValueMap;
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
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.util.FreemarkerUtil;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

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
    protected TextField sheetNameField;
    protected NumberTextField startRowField;
    protected TextField categorySheetNameField;
    protected NumberTextField categoryStartRowField;
    protected TextArea categoryConfigField;
    protected TextField errorSourceIdColField;
    protected TextField categoryIdColField;
    protected TextField errorSourceNumberColField;
    protected TextField errorSourceEnNameColField;
    protected Label errorSourceDescColLabel;
    protected TextField errorSourceDescColField;
    protected TextField errorSourceJpNameColField;
    protected TextArea functionConfigField;
    protected TextArea productConfigField;

    private final String defaultTemplateName = "ecm.zip";
    private final FileChooser downloadChooser = new FileChooser();

    protected String outParentFolder = File.separator + "ecm";
    protected Notifications notificationBuilder = FxNotifications.defaultNotify();

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
                    String path = "com/tlcsdm/smc/static/templates/smc/ecm";
                    ZipUtil.zip(file, Charset.defaultCharset(),
                            new ClassPathResource(path + File.separator + "u2a.ftl", getClass().getClassLoader()),
                            new ClassPathResource(path + File.separator + "c1m.ftl", getClass().getClassLoader()));

                    notificationBuilder.text(I18nUtils.get("smc.tool.button.download.success"));
                    notificationBuilder.showInformation();
                }
            });

    private final Action generate = FxAction.generate(actionEvent -> {
        dealData();

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

        Label excelLabel = new Label(I18nUtils.get("smc.tool.ecm.label.excel") + ": ");
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

        Label outputLabel = new Label(I18nUtils.get("smc.tool.ecm.label.output") + ": ");
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

        Label sheetNameLabel = new Label(I18nUtils.get("smc.tool.ecm.label.sheetName") + ": ");
        sheetNameField = new TextField();
        sheetNameField.setPrefWidth(Double.MAX_VALUE);

        Label startRowLabel = new Label(I18nUtils.get("smc.tool.ecm.label.startRow") + ": ");
        startRowField = new NumberTextField();

        TitledPane errorSourcePane = createErrorSourceControl();
        TitledPane categoryPane = createCategoryControl();
        // 折叠面板
        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(categoryPane, errorSourcePane);
        accordion.setExpandedPane(categoryPane);

        initDefaultValue();

        userData.put("excel", excelField);
        userData.put("excelFileChooser", excelFileChooser);
        userData.put("output", outputField);
        userData.put("outputChooser", outputChooser);
        userData.put("sheetName", sheetNameField);
        userData.put("startRow", startRowField);
        userData.put("categorySheetName", categorySheetNameField);
        userData.put("categoryConfig", categoryConfigField);
        userData.put("errorSourceIdCol", errorSourceIdColField);
        userData.put("categoryIdCol", categoryIdColField);
        userData.put("errorSourceNumberCol", errorSourceNumberColField);
        userData.put("errorSourceEnNameCol", errorSourceEnNameColField);
        userData.put("errorSourceDesc", errorSourceDescColField);
        userData.put("errorSourceJpNameCol", errorSourceJpNameColField);
        userData.put("functionConfig", functionConfigField);
        userData.put("productConfig", productConfigField);

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(excelLabel, 0, 1);
        grid.add(excelButton, 1, 1);
        grid.add(excelField, 2, 1);
        grid.add(outputLabel, 0, 2);
        grid.add(outputButton, 1, 2);
        grid.add(outputField, 2, 2);
        grid.add(sheetNameLabel, 0, 3);
        grid.add(sheetNameField, 1, 3, 2, 1);
        grid.add(startRowLabel, 0, 4);
        grid.add(startRowField, 1, 4, 2, 1);
        grid.add(accordion, 0, 5, 3, 1);

        return grid;
    }

    /**
     * ErrorSource面板
     */
    protected TitledPane createErrorSourceControl() {
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(5));

        Label errorSourceIdColLabel = new Label(I18nUtils.get("smc.tool.ecm.label.errorSourceIdCol") + ": ");
        errorSourceIdColField = new TextField();
        errorSourceIdColLabel.setMinWidth(130);
        errorSourceIdColField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(errorSourceIdColField, Priority.ALWAYS);

        Label categoryIdColLabel = new Label(I18nUtils.get("smc.tool.ecm.label.categoryIdCol") + ": ");
        categoryIdColField = new TextField();

        Label errorSourceNumberColLabel = new Label(I18nUtils.get("smc.tool.ecm.label.errorSourceNumberCol") + ": ");
        errorSourceNumberColField = new TextField();

        Label errorSourceEnNameColLabel = new Label(I18nUtils.get("smc.tool.ecm.label.errorSourceEnNameCol") + ": ");
        errorSourceEnNameColField = new TextField();

        errorSourceDescColLabel = new Label(I18nUtils.get("smc.tool.ecm.label.errorSourceDescCol") + ": ");
        errorSourceDescColField = new TextField();

        Label errorSourceJpNameColLabel = new Label(I18nUtils.get("smc.tool.ecm.label.errorSourceJpNameCol") + ": ");
        errorSourceJpNameColField = new TextField();

        Label functionConfigLabel = new Label(I18nUtils.get("smc.tool.ecm.label.functionConfig") + ": ");
        functionConfigField = new TextArea();
        functionConfigField.setMinHeight(130);

        Label productConfigLabel = new Label(I18nUtils.get("smc.tool.ecm.label.productConfig") + ": ");
        productConfigField = new TextArea();
        productConfigField.setMinHeight(150);

        grid.add(errorSourceIdColLabel, 0, 0);
        grid.add(errorSourceIdColField, 1, 0);
        grid.add(categoryIdColLabel, 0, 1);
        grid.add(categoryIdColField, 1, 1);
        grid.add(errorSourceNumberColLabel, 0, 2);
        grid.add(errorSourceNumberColField, 1, 2);
        grid.add(errorSourceEnNameColLabel, 0, 3);
        grid.add(errorSourceEnNameColField, 1, 3);
        grid.add(errorSourceDescColLabel, 0, 4);
        grid.add(errorSourceDescColField, 1, 4);
        grid.add(errorSourceJpNameColLabel, 0, 5);
        grid.add(errorSourceJpNameColField, 1, 5);
        grid.add(functionConfigLabel, 0, 6);
        grid.add(functionConfigField, 1, 6);
        grid.add(productConfigLabel, 0, 7);
        grid.add(productConfigField, 1, 7);

        return new TitledPane(I18nUtils.get("smc.tool.ecm.title.errorSource"), grid);
    }

    /**
     * Category 面板
     */
    protected TitledPane createCategoryControl() {
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(5));

        Label categorySheetNameLabel = new Label(I18nUtils.get("smc.tool.ecm.label.categorySheetName") + ": ");
        categorySheetNameField = new TextField();
        categorySheetNameField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(categorySheetNameField, Priority.ALWAYS);

        Label categoryStartRowLabel = new Label(I18nUtils.get("smc.tool.ecm.label.categoryStartRow") + ": ");
        categoryStartRowField = new NumberTextField();

        Label categoryConfigLabel = new Label(I18nUtils.get("smc.tool.ecm.label.categoryConfig") + ": ");
        categoryConfigField = new TextArea();
        categoryConfigField.setMinHeight(60);
        categoryConfigField.setPrefHeight(80);

        grid.add(categorySheetNameLabel, 0, 0);
        grid.add(categorySheetNameField, 1, 0);
        grid.add(categoryStartRowLabel, 0, 1);
        grid.add(categoryStartRowField, 1, 1);
        grid.add(categoryConfigLabel, 0, 2);
        grid.add(categoryConfigField, 1, 2);

        return new TitledPane(I18nUtils.get("smc.tool.ecm.title.category"), grid);
    }

    /**
     * 设置默认值
     */
    protected void initDefaultValue() {
        startRowField.setNumber(BigDecimal.valueOf(3));
        categoryConfigField
                .setPromptText(I18nUtils.get("smc.tool.dmaTriggerSourceCode.textfield.deviceAndStartCol.promptText"));
        functionConfigField
                .setPromptText(I18nUtils.get("smc.tool.dmaTriggerSourceCode.textfield.deviceAndStartCol.promptText"));
        productConfigField
                .setPromptText(I18nUtils.get("smc.tool.dmaTriggerSourceCode.textfield.deviceAndStartCol.promptText"));
        categoryStartRowField.setNumber(BigDecimal.valueOf(3));
        categorySheetNameField.setText("Category");
    }

    /**
     * 数据处理
     */
    protected void dealData() {
        // 输入值获取
        String excel = excelField.getText();
        String outputPath = outputField.getText();
        String deviceSheetName = sheetNameField.getText();
        int deviceStartRow = Integer.parseInt(startRowField.getText());
        // Category
        String categorySheetName = categorySheetNameField.getText();
        int categoryStartRow = Integer.parseInt(categoryStartRowField.getText());
        String categorys = categoryConfigField.getText();
        // Error Source
        String functions = functionConfigField.getText();
        String errorSourceIdCol = errorSourceIdColField.getText();
        String categoryIdCol = categoryIdColField.getText();
        String errorSourceNumberCol = errorSourceNumberColField.getText();
        String errorSourceEnNameCol = errorSourceEnNameColField.getText();
        String errorSourceDescCol = errorSourceDescColField.getText();
        String errorSourceJpNameCol = errorSourceJpNameColField.getText();
        String products = productConfigField.getText();

        String resultPath = outputPath + outParentFolder;

        int optErrortIndex = 0;
        LinkedHashMap<String, String> operationMap = new LinkedHashMap<>();
        List<String> operationConfigs = StrUtil.splitTrim(functions, "\n");
        for (int i = 0; i < operationConfigs.size(); i++) {
            String operationConfig = operationConfigs.get(i);
            List<String> l = StrUtil.split(operationConfig, ";");
            operationMap.put(l.get(0), l.get(1));
            if ("optErrort".equals(l.get(0))) {
                optErrortIndex = i;
            }
        }
        LinkedHashMap<String, String> productMap = new LinkedHashMap<>();
        List<String> productConfigs = StrUtil.splitTrim(products, "\n");
        ListValueMap<String, String> productsInfo = new ListValueMap<>();
        for (String productConfig : productConfigs) {
            List<String> l = StrUtil.split(productConfig, ";");
            productMap.put(l.get(0) + "_" + l.get(1), l.get(2));
            productsInfo.putValue(l.get(0), l.get(1));
        }
        // category 配置数据
        LinkedHashMap<String, String> categoryMap = new LinkedHashMap<>();
        List<String> categoryConfigs = StrUtil.splitTrim(categorys, "\n");
        for (String categoryConf : categoryConfigs) {
            List<String> l = StrUtil.split(categoryConf, ";");
            categoryMap.put(l.get(0), l.get(1));
        }
        // category 数据处理
        ExcelReader categoryReader = ExcelUtil.getReader(FileUtil.file(excel), categorySheetName);
        int categoryEndRow = categoryReader.getRowCount();
        List<Map<String, Object>> categoryInfos = new ArrayList<>();
        for (int i = categoryStartRow; i <= categoryEndRow; i++) {
            Map<String, Object> categoryInfo = new HashMap<>();
            for (String key : categoryMap.keySet()) {
                Cell cell = categoryReader.getCell(categoryMap.get(key) + i);
                if (cell == null) {
                    continue;
                }
                String value = cell.getStringCellValue();
                if (StrUtil.isBlank(value)) {
                    continue;
                }
                categoryInfo.put(key, value);
            }
            if (categoryInfo.isEmpty()) {
                continue;
            }
            categoryInfos.add(categoryInfo);
        }
        categoryReader.close();

        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(excel), deviceSheetName);
        int endRow = reader.getRowCount();
        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        // 遍历productMap
        for (String key : productMap.keySet()) {
            List<Map<String, Object>> ErrorSourceInfos = new ArrayList<>();
            String productCol = productMap.get(key);
            // 遍历excel sheet数据
            for (int i = deviceStartRow; i <= endRow; i++) {
                Cell cell = reader.getCell(errorSourceIdCol + i);
                if (cell == null) {
                    continue;
                }
                if (StrUtil.isBlank(cell.getStringCellValue())) {
                    continue;
                }
                if (!"-".equals(productCol)) {
                    String productCondition = reader.getCell(productCol + i).getStringCellValue();
                    if (productCondition.contains("—") || productCondition.contains("-")) {
                        continue;
                    }
                }
                String errorSourceId = reader.getCell(errorSourceIdCol + i).getStringCellValue();
                String categoryId = reader.getCell(categoryIdCol + i).getStringCellValue();
                int errorSourceNum = (int) reader.getCell(errorSourceNumberCol + i).getNumericCellValue();
                String errorSourceNumber = String.valueOf(errorSourceNum);
                String errorSourceEnName = reader.getCell(errorSourceEnNameCol + i).getStringCellValue();
                String errorSourceJpName = reader.getCell(errorSourceJpNameCol + i).getStringCellValue();
                String errorSourceDesc = reader.getCell(errorSourceDescCol + i).getStringCellValue();
                // 特殊处理 24-29添加description信息
                if (errorSourceNum < 24 || errorSourceNum > 29) {
                    errorSourceDesc = "";
                }
                List<Map<String, Object>> function = new ArrayList<>();
                boolean optMaskintStatus = false;
                for (String funcId : operationMap.keySet()) {
                    String funcCol = operationMap.get(funcId);
                    String funcSupCondition = reader.getCell(funcCol + i).getStringCellValue();
                    // support 向下判断
                    boolean support = !(funcSupCondition.contains("—") || funcSupCondition.contains("-"));
                    if ("opMaskint".equals(funcId)) {
                        optMaskintStatus = support;
                    }
                    Map<String, Object> operation = new HashMap<>();
                    operation.put("funcId", funcId);
                    operation.put("support", String.valueOf(support));
                    operation.put("errorNote", "");
                    // 数据后置处理
                    handlerOperationSupport(operation, funcSupCondition, optMaskintStatus);
                    function.add(operation);
                }
                Map<String, Object> errorSource = new HashMap<>();
                errorSource.put("errorSourceId", errorSourceId);
                errorSource.put("categoryId", categoryId);
                errorSource.put("errorSourceNumber", errorSourceNumber);
                errorSource.put("errorSourceEnName", errorSourceEnName);
                errorSource.put("errorSourceJpName", errorSourceJpName);
                errorSource.put("errorSourceDesc", errorSourceDesc.replaceAll("\n", " "));
                errorSource.put("function", function);
                handlerErrorSourceMap(errorSource, key, optErrortIndex);
                ErrorSourceInfos.add(errorSource);
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("categoryInfos", categoryInfos);
            paramMap.put("errorSourceInfos", ErrorSourceInfos);
            File result = FileUtil.newFile(resultPath + "\\" + key + ".xml");
            FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(paramMap, getFtlPath()), result);
        }
        reader.close();

        // 后续文件合并
        // 待删除文件
        List<String> delFileNames = new ArrayList<>();
        for (String device : productsInfo.keySet()) {
            List<String> list = productsInfo.get(device);
            if (list.size() == 1) {
                FileUtil.rename(FileUtil.file(resultPath, device + "_" + list.get(0) + ".xml"), device + ".xml", true);
                continue;
            }
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    String orgName = device + "_" + list.get(i) + ".xml";
                    String comName = device + "_" + list.get(j) + ".xml";
                    boolean b = FileUtil.contentEquals(FileUtil.file(resultPath, orgName),
                            FileUtil.file(resultPath, comName));
                    if (b) {
                        if (!delFileNames.contains(orgName) && !delFileNames.contains(comName)) {
                            String deviceName = device + ".xml";
                            if (FileUtil.file(resultPath, deviceName).exists()) {
                                deviceName = device + "-" + UUID.fastUUID() + ".xml";
                            }
                            FileUtil.copyFile(FileUtil.file(resultPath, orgName),
                                    FileUtil.file(resultPath, deviceName));
                        }
                        if (!delFileNames.contains(orgName)) {
                            delFileNames.add(orgName);
                        }
                        if (!delFileNames.contains(comName)) {
                            delFileNames.add(comName);
                        }
                    }
                }
            }
        }
        for (String fileName : delFileNames) {
            FileUtil.del(FileUtil.file(resultPath, fileName));
        }
    }

    /**
     * errorSource 数据后续处理
     */
    private void handlerErrorSourceMap(Map<String, Object> errorSource, String product, int optErrortIndex) {
        String errorSourceenName = (String) errorSource.get("errorSourceEnName");
        String errorSourcejpName = (String) errorSource.get("errorSourceJpName");
        errorSourceenName = cleanErrorSourceData(errorSourceenName);
        errorSourcejpName = cleanErrorSourceData(errorSourcejpName);
        if (errorSourceenName.endsWith("*7")) {
            errorSourceenName = StrUtil.replaceLast(errorSourceenName, "*7", "(For debug purpose only)");
            if (errorSourcejpName.endsWith("*7")) {
                errorSourcejpName = StrUtil.replaceLast(errorSourcejpName, "*7", "(デバッグのみを目的とする)");
            } else {
                errorSourcejpName += "(デバッグのみを目的とする)";
            }
        }
        errorSource.put("errorSourceEnName", errorSourceenName);
        errorSource.put("errorSourceJpName", errorSourcejpName);

        List<Map<String, Object>> function = (List<Map<String, Object>>) errorSource.get("function");
        List<Map<String, Object>> extraFunc = new ArrayList<>();
        for (Map<String, Object> map : function) {
            String funcId = map.get("funcId").toString();
            if ("optErrort".equals(funcId)) {
                String support = map.get("support").toString();
                String errorNote = map.get("errorNote").toString();
                int size = 0;
                if (product.startsWith("RH850U2A16")) {
                    size = 4;
                    generateErrort(size, support, errorNote, extraFunc, function);
                    function.remove(map);
                } else if (product.startsWith("RH850U2A8") || product.startsWith("RH850U2A6")) {
                    size = 2;
                    generateErrort(size, support, errorNote, extraFunc, function);
                    function.remove(map);
                } else {
                    break;
                }
            }
        }
        function.addAll(optErrortIndex, extraFunc);
    }

    private void generateErrort(int size, String support, String errorNote, List<Map<String, Object>> extraFunc,
            List<Map<String, Object>> function) {
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("funcId", "optErrort" + i);
            map.put("support", support);
            map.put("errorNote", errorNote);
            extraFunc.add(map);
        }
    }

    /**
     * 处理使能条件的 * 信息, 默认是support = true下的
     */
    private void handlerOperationSupport(Map<String, Object> operation, String funcSupCondition,
            boolean optMaskintStatus) {
        if (funcSupCondition.contains("*")) {
            String mesNum = StrUtil.subAfter(funcSupCondition, "*", true);
            if ("1".equals(mesNum) || "2".equals(mesNum)) {
                operation.put("errorNote", mesNum);
            }
            if ("5".equals(mesNum)) {
                String funcId = operation.get("funcId").toString();
                if ("optDCLS".equals(funcId)) {
                    operation.put("support", String.valueOf(optMaskintStatus));
                }
                if ("optIntg".equals(funcId)) {
                    operation.put("support", "false");
                }
            }
        } else {
            String funcId = operation.get("funcId").toString();
            if ("optDCLS".equals(funcId)) {
                operation.put("support", "false");
            }
            if ("optIntg".equals(funcId)) {
                operation.put("support", String.valueOf(optMaskintStatus));
            }
        }

    }

    /**
     * 清洗ErrorSource数据
     */
    private String cleanErrorSourceData(String data) {
        data = data.replaceAll("  ", " ");
        if (data.contains(" (")) {
            data = StrUtil.replace(data, " (", "(");
        }
        if (data.contains("\n")) {
            List<String> list = StrUtil.split(data, "\n");
            data = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                data += " ";
                data += list.get(i).replaceFirst("- ", "-");
            }
        }
        data = data.replaceAll("-", " - ");
        data = data.replaceAll("  ", " ");
        if (data.contains(" - bit")) {
            data = data.replaceAll(" - bit", "-bit");
        }
        if (data.contains("P - Bus")) {
            data = data.replaceAll("P - Bus", "P-Bus");
        }
        if (data.contains("I - Bus")) {
            data = data.replaceAll("I - Bus", "I-Bus");
        }
        if (data.contains("H - Bus")) {
            data = data.replaceAll("H - Bus", "H-Bus");
        }
        return data;
    }

    /**
     * 获取freemarker模板路径
     */
    protected abstract String getFtlPath();

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.ecm.description");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.2";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/xml.png"));
    }

    @Override
    public Node getControlPanel() {
        String content = """
                {templateDesc}

                {categoryConfigLabel}: key;colName
                eg: categoryId;F
                {functionConfigLabel}: key;colName
                eg: optMaskint;G
                {productConfigLabel}: device;pin;colName
                {productConfigDesc}
                eg: RH850U2A16;516;N
                """;

        Map<String, String> map = new HashMap<>(8);
        map.put("categoryConfigLabel", I18nUtils.get("smc.tool.ecm.label.categoryConfig"));
        map.put("functionConfigLabel", I18nUtils.get("smc.tool.ecm.label.functionConfig"));
        map.put("productConfigLabel", I18nUtils.get("smc.tool.ecm.label.productConfig"));
        map.put("productConfigDesc", I18nUtils.get("smc.tool.ecm.control.productConfig"));
        map.put("templateDesc", I18nUtils.get("smc.tool.ecm.control.templateDesc"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }
}
