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

package com.tlcsdm.smc.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.bind.TextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.FxXmlUtil;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.action.ActionUtils.ActionTextBehavior;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 检测指定路径下文件内容长度是否超过120.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.0
 */
public class CodeStyleLength120 extends SmcSample {

    /**
     * 结果文件名称(目前支持结果输出到excel)
     */
    private String resultFileName = "CodeStyleLength120.xlsx";
    /**
     * 结果信息输出路径
     */
    private String resultPath = "";
    /**
     * 忽略的文件
     */
    private List<String> ignoreFilesList;
    /**
     * 检测的文件类型
     */
    private List<String> fileTypeList;
    /**
     * 待比对文件的父级路径
     */
    private File generateFilesParentPath;
    /**
     * 结果信息
     */
    private final List<Map<String, Object>> result = new ArrayList<>();

    private TextField checkDirField;
    private TextField checkFileTypeField;
    private TextField ignoreFileField;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();
    FileChooser outPutChooser = new FileChooser();

    private final Action generate = FxAction.generate(actionEvent -> {
        ignoreFilesList = StrUtil.splitTrim(ignoreFileField.getText(), ",");
        fileTypeList = StrUtil.splitTrim(checkFileTypeField.getText(), ",");
        if (generateFilesParentPath == null) {
            notificationBuilder.text(I18nUtils.get("smc.tool.codeStyleLength120.button.generate.warn.message1"));
            notificationBuilder.showWarning();
            return;
        }
        File file = outPutChooser.showSaveDialog(FxApp.primaryStage);
        if (file != null) {
            if (!StrUtil.endWith(file.getName(), ".xlsx")) {
                notificationBuilder.text(I18nUtils.get("smc.tool.codeStyleLength120.button.generate.warn.message2"));
                notificationBuilder.showWarning();
                return;
            }
            resultFileName = file.getName();
            resultPath = file.getParent();
            outPutChooser.setInitialDirectory(file.getParentFile());
            outPutChooser.setInitialFileName(resultFileName);
            result.clear();
            initData();
            handleResult();
            notificationBuilder.text(I18nUtils.get("smc.tool.button.generate.success"));
            notificationBuilder.showInformation();
            if (!result.isEmpty()) {
                OSUtil.openAndSelectedFile(file);
            }
            bindUserData();
        }
    });

    private final Collection<? extends Action> actions = List.of(generate);

    @Override
    public Node getPanel(Stage stage) {
        initComponment();
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);

        Label checkDirLabel = new Label(I18nUtils.get("smc.tool.codeStyleLength120.label.checkDir") + ": ");
        checkDirField = new TextField();
        checkDirField.setMaxWidth(Double.MAX_VALUE);
        DirectoryChooser checkDirChooser = new DirectoryChooser();
        Button checkDirButton = FxButton.choose();
        checkDirField.setEditable(false);
        checkDirButton.setOnAction(arg0 -> {
            File file = checkDirChooser.showDialog(stage);
            if (file != null) {
                checkDirField.setText(file.getPath());
                generateFilesParentPath = file;
                checkDirChooser.setInitialDirectory(file);
            }
        });

        Label checkFileTypeLabel = new Label(I18nUtils.get("smc.tool.codeStyleLength120.label.checkFileType") + ": ");
        checkFileTypeField = new TextField();
        checkFileTypeField.setPrefWidth(Double.MAX_VALUE);
        checkFileTypeField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label ignoreFileLabel = new Label(I18nUtils.get("smc.tool.codeStyleLength120.label.ignoreFile") + ": ");
        ignoreFileField = new TextField();
        ignoreFileField.setPrefWidth(Double.MAX_VALUE);
        ignoreFileField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        checkFileTypeField.setText("c,h");

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(checkDirLabel, 0, 1);
        grid.add(checkDirButton, 1, 1);
        grid.add(checkDirField, 2, 1);

        grid.add(checkFileTypeLabel, 0, 2);
        grid.add(checkFileTypeField, 1, 2, 2, 1);

        grid.add(ignoreFileLabel, 0, 3);
        grid.add(ignoreFileField, 1, 3, 2, 1);

        return grid;
    }

    @Override
    protected void initializeUserData() {
        super.initializeUserData();
        if (!FxXmlUtil.hasKey(getSampleXmlPrefix(), "id")) {
            return;
        }
        generateFilesParentPath = new File(FxXmlUtil.get(getSampleXmlPrefix(), "checkDir", ""));
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        BooleanBinding checkDirValidation = new TextInputControlEmptyBinding(checkDirField).build();
        generate.disabledProperty().bind(checkDirValidation);
        FileChooserUtil.setOnDrag(checkDirField, FileChooserUtil.FileType.FOLDER);
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        userData.put("checkDir", checkDirField);
        userData.put("checkFileType", checkFileTypeField);
        userData.put("ignoreFile", ignoreFileField);
        userData.put("outPut", outPutChooser);
    }

    @Override
    public Node getControlPanel() {
        String content = """
            {generateButton}:
            {generateDesc}
            {Required} {checkDirLabel}, {checkFileTypeLabel}, {ignoreFileLabel}

            {Note}
            {checkFileTypeLabel} {emptyDesc} {promptTextList}
            {ignoreFileLabel} {emptyDesc} {promptTextList}
            """;
        Map<String, String> map = new HashMap<>();
        map.put("generateButton", generate.getText());
        map.put("generateDesc", I18nUtils.get("smc.tool.codeStyleLength120.control.textarea1"));
        map.put("Required", I18nUtils.get("smc.tool.control.required"));
        map.put("checkDirLabel", I18nUtils.get("smc.tool.codeStyleLength120.label.checkDir"));
        map.put("checkFileTypeLabel", I18nUtils.get("smc.tool.codeStyleLength120.label.checkFileType"));
        map.put("ignoreFileLabel", I18nUtils.get("smc.tool.codeStyleLength120.label.ignoreFile"));
        map.put("Note", I18nUtils.get("smc.tool.control.note"));
        map.put("emptyDesc", I18nUtils.get("smc.tool.textfield.empty.desc"));
        map.put("promptTextList", I18nUtils.get("smc.tool.textfield.promptText.list"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "codeStyleLength120";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.codeStyleLength120");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/valid.png"));
    }

    @Override
    public String getOrderKey() {
        return "CodeStyleLength120";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.codeStyleLength120.description");
    }

    // 初始化组件
    private void initComponment() {
        FileChooser.ExtensionFilter extFilter = FileChooserUtil.xlsxFilter();
        outPutChooser.getExtensionFilters().add(extFilter);
        outPutChooser.setInitialFileName(resultFileName);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        List<File> files = FileUtil.loopFiles(generateFilesParentPath, new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isFile() && !ignoreFilesList.contains(file.getName())) {
                    if (fileTypeList.isEmpty()) {
                        return true;
                    }
                    for (String fileType : fileTypeList) {
                        if (StrUtil.endWith(file.getName(), "." + fileType)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        if (files.isEmpty()) {
            notificationBuilder.text(I18nUtils.get("smc.tool.codeStyleLength120.button.generate.warn.message3"));
            notificationBuilder.showWarning();
            return;
        }
        for (File file : files) {
            AtomicInteger atomicInteger = new AtomicInteger(0);
            FileUtil.readUtf8Lines(file, (LineHandler) line -> {
                atomicInteger.incrementAndGet();
                if (line.length() > 120) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("lineNumber", atomicInteger.get());
                    map.put("fileName", file.getName());
                    map.put("lineLength", line.length());
                    map.put("filePath", file.getPath());
                    map.put("line", line);
                    result.add(map);
                }
            });
        }
    }

    /**
     * 结果信息处理
     */
    private void handleResult() {
        if (result.isEmpty()) {
            notificationBuilder.text(I18nUtils.get("smc.tool.codeStyleLength120.button.generate.info.message"));
            notificationBuilder.showInformation();
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file(resultPath, resultFileName));
        setExcelStyle(writer);
        handleData(writer);
        writer.addHeaderAlias("lineNumber", I18nUtils.get("smc.tool.codeStyleLength120.result.lineNumber"));
        writer.addHeaderAlias("fileName", I18nUtils.get("smc.tool.codeStyleLength120.result.fileName"));
        writer.addHeaderAlias("lineLength", I18nUtils.get("smc.tool.codeStyleLength120.result.lineLength"));
        writer.addHeaderAlias("filePath", I18nUtils.get("smc.tool.codeStyleLength120.result.filePath"));
        writer.addHeaderAlias("line", I18nUtils.get("smc.tool.codeStyleLength120.result.line"));
        writer.write(result, true);
        writer.close();
    }

    /**
     * 设置生成的excel样式
     */
    private void setExcelStyle(ExcelWriter writer) {
        writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        writer.getHeadCellStyle().setAlignment(HorizontalAlignment.CENTER);
        CellStyle style = writer.getStyleSet().getHeadCellStyle();
        StyleUtil.setColor(style, IndexedColors.LIGHT_YELLOW, FillPatternType.SOLID_FOREGROUND);
        writer.setColumnWidth(0, 10);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 10);
        writer.setColumnWidth(3, 70);
        writer.setColumnWidth(4, 110);
    }

    /**
     * 数据处理
     */
    private void handleData(ExcelWriter writer) {

    }

}
