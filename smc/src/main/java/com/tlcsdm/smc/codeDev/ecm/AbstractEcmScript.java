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
import com.tlcsdm.core.javafx.bind.MultiTextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.bind.TextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxButtonType;
import com.tlcsdm.core.javafx.dialog.FxDialog;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.richtext.GroovyCodeArea;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.core.util.FreemarkerUtil;
import com.tlcsdm.core.util.GroovyUtil;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * EcmScript脚本超类
 *
 * @author unknowIfGuestInDream
 * @date 2023/3/26 21:13
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
    protected Label tagConfigLabel;
    protected TextArea tagConfigField;

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
                String ftlPath = "com/tlcsdm/smc/static/templates/";
                String groovyPath = "com/tlcsdm/smc/static/groovy/";
                ZipUtil.zip(file, Charset.defaultCharset(),
                    new ClassPathResource(ftlPath + getFtlPath(), getClass().getClassLoader()),
                    new ClassPathResource(groovyPath + getGroovyPath(), getClass().getClassLoader()));

                notificationBuilder.text(I18nUtils.get("smc.tool.button.download.success"));
                notificationBuilder.showInformation();
            }
        });

    private final Action viewGroovyScript = FxAction
        .view(I18nUtils.get("smc.tool.dmaTriggerSourceCode.button.scriptContent"), actionEvent -> {
            VBox vbox = new VBox();
            GroovyCodeArea area = new GroovyCodeArea();
            area.setEditable(false);
            area.appendText(GroovyUtil.getScriptContent(getGroovyPath()));
            area.showParagraphAtTop(0);
            VirtualizedScrollPane<GroovyCodeArea> pane = new VirtualizedScrollPane<>(area);
            vbox.getChildren().addAll(pane);
            VBox.setVgrow(pane, Priority.ALWAYS);
            FxDialog<VBox> dialog = new FxDialog<VBox>()
                .setTitle(I18nUtils.get("smc.tool.dmaTriggerSourceCode.button.scriptContent"))
                .setOwner(FxApp.primaryStage).setPrefSize(1000, 800).setResizable(true).setBody(vbox)
                .setButtonTypes(FxButtonType.COPY, FxButtonType.CLOSE);
            dialog.setButtonHandler(FxButtonType.COPY, (e, s) -> {
                OSUtil.writeToClipboard(area.getText());
            }).setButtonHandler(FxButtonType.CLOSE, (e, s) -> s.close());
            dialog.show();
        });

    private final Action generate = FxAction.generate(actionEvent -> {
        dealData();
        notificationBuilder.text(I18nUtils.get("smc.tool.button.generate.success"));
        notificationBuilder.showInformation();
        bindUserData();
    });

    private final Collection<? extends Action> actions = List.of(generate, download, viewGroovyScript, openOutDir);

    @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);
        FileChooser.ExtensionFilter extFilter = FileChooserUtil.xlsxFilter();

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

        tagConfigLabel = new Label(I18nUtils.get("smc.tool.ecm.label.tagConfig") + ": ");
        tagConfigField = new TextArea();
        tagConfigField.setMinHeight(60);
        tagConfigField.setPrefHeight(80);

        grid.addRow(0, errorSourceIdColLabel, errorSourceIdColField);
        grid.addRow(1, categoryIdColLabel, categoryIdColField);
        grid.addRow(2, errorSourceNumberColLabel, errorSourceNumberColField);
        grid.addRow(3, errorSourceEnNameColLabel, errorSourceEnNameColField);
        grid.addRow(4, errorSourceDescColLabel, errorSourceDescColField);
        grid.addRow(5, errorSourceJpNameColLabel, errorSourceJpNameColField);
        grid.addRow(6, functionConfigLabel, functionConfigField);
        grid.addRow(7, productConfigLabel, productConfigField);
        grid.addRow(8, tagConfigLabel, tagConfigField);

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
        categorySheetNameLabel.setMinWidth(90);
        categorySheetNameField = new TextField();
        categorySheetNameField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(categorySheetNameField, Priority.ALWAYS);

        Label categoryStartRowLabel = new Label(I18nUtils.get("smc.tool.ecm.label.categoryStartRow") + ": ");
        categoryStartRowField = new NumberTextField();

        Label categoryConfigLabel = new Label(I18nUtils.get("smc.tool.ecm.label.categoryConfig") + ": ");
        categoryConfigField = new TextArea();
        categoryConfigField.setMinHeight(60);
        categoryConfigField.setPrefHeight(80);

        grid.addRow(0, categorySheetNameLabel, categorySheetNameField);
        grid.addRow(1, categoryStartRowLabel, categoryStartRowField);
        grid.addRow(2, categoryConfigLabel, categoryConfigField);

        return new TitledPane(I18nUtils.get("smc.tool.ecm.title.category"), grid);
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        BooleanBinding outputValidation = new TextInputControlEmptyBinding(outputField).build();
        BooleanBinding emptyValidation = new MultiTextInputControlEmptyBinding(excelField, outputField, sheetNameField,
            startRowField, categorySheetNameField, categoryStartRowField, categoryConfigField, errorSourceIdColField,
            categoryIdColField, errorSourceNumberColField, errorSourceEnNameColField, errorSourceDescColField,
            errorSourceJpNameColField, functionConfigField, productConfigField, tagConfigField).build();
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
        userData.put("tagConfig", tagConfigField);
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
        String resultPath = outputField.getText() + outParentFolder;
        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        // 处理数据
        dealProductData();
        // 后续文件合并
        postMergeResult();
    }

    /**
     * 创建Function配置数据
     */
    protected LinkedHashMap<String, String> createOperationMap() {
        LinkedHashMap<String, String> operationMap = new LinkedHashMap<>();
        String functions = functionConfigField.getText();
        List<String> operationConfigs = StrUtil.splitTrim(functions, "\n");
        for (String operationConfig : operationConfigs) {
            List<String> l = StrUtil.split(operationConfig, ";");
            operationMap.put(l.get(0), l.get(1));
        }
        return operationMap;
    }

    /**
     * 创建product配置数据
     */
    protected LinkedHashMap<String, String> createProductMap() {
        String products = productConfigField.getText();
        LinkedHashMap<String, String> productMap = new LinkedHashMap<>();
        List<String> productConfigs = StrUtil.splitTrim(products, "\n");
        for (String productConfig : productConfigs) {
            List<String> l = StrUtil.split(productConfig, ";");
            productMap.put(l.get(0) + "_" + l.get(1), l.get(2));
        }
        return productMap;
    }

    /**
     * 创建productInfo数据
     * 用于文件合并功能
     */
    protected ListValueMap<String, String> createProductInfo() {
        String products = productConfigField.getText();
        List<String> productConfigs = StrUtil.splitTrim(products, "\n");
        ListValueMap<String, String> productsInfo = new ListValueMap<>();
        for (String productConfig : productConfigs) {
            List<String> l = StrUtil.split(productConfig, ";");
            productsInfo.putValue(l.get(0), l.get(1));
        }
        return productsInfo;
    }

    /**
     * 创建category配置数据
     */
    protected LinkedHashMap<String, String> createCategoryMap() {
        String categorys = categoryConfigField.getText();
        LinkedHashMap<String, String> categoryMap = new LinkedHashMap<>();
        List<String> categoryConfigs = StrUtil.splitTrim(categorys, "\n");
        for (String categoryConf : categoryConfigs) {
            List<String> l = StrUtil.split(categoryConf, ";");
            categoryMap.put(l.get(0), l.get(1));
        }
        return categoryMap;
    }

    /**
     * 创建tag配置数据
     */
    protected LinkedHashMap<String, String> createTagMap() {
        String tags = tagConfigField.getText();
        LinkedHashMap<String, String> tagMap = new LinkedHashMap<>();
        List<String> tagConfigs = StrUtil.splitTrim(tags, "\n");
        for (String tagConfig : tagConfigs) {
            List<String> l = StrUtil.split(tagConfig, ";");
            tagMap.put(l.get(0), l.get(1));
        }
        return tagMap;
    }

    /**
     * 处理category数据
     */
    protected List<Map<String, Object>> dealCategoryData() {
        LinkedHashMap<String, String> categoryMap = createCategoryMap();
        String excel = excelField.getText();
        String categorySheetName = categorySheetNameField.getText();
        int categoryStartRow = Integer.parseInt(categoryStartRowField.getText());
        List<Map<String, Object>> categoryInfos = new ArrayList<>();
        ExcelReader categoryReader = ExcelUtil.getReader(FileUtil.file(excel), categorySheetName);
        int categoryEndRow = categoryReader.getRowCount();
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
        return categoryInfos;
    }

    /**
     * 处理product数据
     */
    protected void dealProductData() {
        String excel = excelField.getText();
        String deviceSheetName = sheetNameField.getText();
        int deviceStartRow = Integer.parseInt(startRowField.getText());
        String errorSourceIdCol = errorSourceIdColField.getText();
        String resultPath = outputField.getText() + outParentFolder;
        // 处理数据
        LinkedHashMap<String, String> productMap = createProductMap();
        List<Map<String, Object>> categoryInfos = dealCategoryData();
        // 处理excel数据
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(excel), deviceSheetName);
        int endRow = reader.getRowCount();
        // 遍历productMap
        for (String key : productMap.keySet()) {
            List<Map<String, Object>> errorSourceInfos = new ArrayList<>();
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
                errorSourceInfos.add(dealErrorSourceData(reader, i, key));
            }
            Map<String, Object> paramMap = new HashMap<>(4);
            paramMap.put("categoryInfos", categoryInfos);
            paramMap.put("errorSourceInfos", errorSourceInfos);
            // 若paramMap需要扩展，可在此处新增新接口
            File result = FileUtil.newFile(resultPath + "\\" + key + ".xml");
            FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(paramMap, getFtlPath()), result);
        }
        reader.close();
    }

    /**
     * ErrorSource数据处理
     */
    protected abstract Map<String, Object> dealErrorSourceData(ExcelReader reader, int rowNum, String product);

    /**
     * 获取tag数据
     */
    protected List<Map<String, Object>> buildTagData(LinkedHashMap<String, String> tagMap, ExcelReader reader,
        int rowNum) {
        List<Map<String, Object>> tag = new ArrayList<>();
        for (String tagkey : tagMap.keySet()) {
            String tagCol = tagMap.get(tagkey);
            String tagValue = reader.getCell(tagCol + rowNum).getStringCellValue();
            Map<String, Object> tagMeta = new HashMap<>();
            if ("psedu".equals(tagkey)) {
                tagValue = String.valueOf(Boolean.valueOf(!"―".equals(tagValue) && tagValue.trim().length() > 0));
            }
            tagMeta.put("key", tagkey);
            tagMeta.put("value", tagValue);
            tag.add(tagMeta);
        }
        return tag;
    }

    /**
     * 合并结果文件
     * 相同device不同pin如果文件内容一致则合并内容，结果文件如： RH850U2A6.xml
     * 否则按照device_pin命名，如：RH850U2A6_144.xml
     */
    protected void postMergeResult() {
        ListValueMap<String, String> productsInfo = createProductInfo();
        String resultPath = outputField.getText() + outParentFolder;
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
     * 获取freemarker模板路径
     */
    protected abstract String getFtlPath();

    /**
     * 获取groovy脚本路径
     */
    protected abstract String getGroovyPath();

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.ecm.description");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.8";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/xml.png"));
    }

    @Override
    public Node getControlPanel() {
        String content = """
            {templateDesc}
            {groovyDesc}

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
        map.put("groovyDesc", I18nUtils.get("smc.tool.ecm.control.groovyDesc"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }
}
