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
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.bind.MultiTextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * 头文件转换为excel UD
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/2/28 23:12
 */
public class HconvertExcel extends SmcSample {

    private final FileChooser outputChooser = new FileChooser();
    private TextField generalField;
    private DirectoryChooser generalChooser;
    private TextField ignoreFileNamesField;
    private TextField markFileNamesField;
    private TextField supportFileTypeField;

    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action generate = FxAction.generate(actionEvent -> {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel file", "*.xlsx");
        outputChooser.getExtensionFilters().add(extFilter);
        File output = outputChooser.showSaveDialog(FxApp.primaryStage);
        if (output != null) {
            if (!StrUtil.endWith(output.getName(), ".xlsx")) {
                notificationBuilder.text(I18nUtils.get("smc.tool.codeStyleLength120.button.generate.warn.message2"));
                notificationBuilder.showWarning();
                return;
            }
            String resultFileName = output.getName();
            String resultPath = output.getParent();
            outputChooser.setInitialDirectory(output.getParentFile());
            outputChooser.setInitialFileName(output.getName());

            if (output.exists()) {
                FileUtil.del(output);
            }
            // 输入值获取
            List<String> ignoreFileNames = StrUtil.splitTrim(ignoreFileNamesField.getText(), ",");
            List<String> markFileNames = StrUtil.splitTrim(markFileNamesField.getText(), ",");
            List<String> supportFileType = StrUtil.splitTrim(supportFileTypeField.getText(), ",");
            String generateFilesPath = generalField.getText();
            // 要生成UD的文件
            List<File> files = FileUtil.loopFiles(generateFilesPath, new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (markFileNames.size() != 0) {
                        for (String markFile : markFileNames) {
                            if (file.isFile() && markFile.equals(file.getName())) {
                                return true;
                            }
                        }
                    }
                    if (markFileNames.size() == 0 && file.isFile() && !ignoreFileNames.contains(file.getName())) {
                        for (String fileType : supportFileType) {
                            if (fileType.equals(FileUtil.getSuffix(file))) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
            StaticLog.info("Generate result...");
            ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file(resultPath, resultFileName));
            writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
            writer.getStyleSet().setBorder(BorderStyle.NONE, IndexedColors.BLACK);
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                if (i == 0) {
                    writer.renameSheet(file.getName());
                } else {
                    writer.setSheet(file.getName());
                }
                writer.setColumnWidth(0, 15);
                writer.setColumnWidth(1, 60);
                writer.setColumnWidth(2, 18);
                writer.setColumnWidth(3, 38);
                List<String> content = FileUtil.readUtf8Lines(file);
                List<List<String>> newContent = new ArrayList<>();
                boolean f = false;
                for (String s : content) {
                    // 去除原文件的脏内容(tab)
                    s = s.replaceAll("\\t", "    ");
                    // 为换行的注释添加tab
                    if (f) {
                        s = "\t\t\t" + StrUtil.trim(s);
                        f = false;
                    }
                    if (s.startsWith("#define")) {
                        s = dealLine(s);
                        if (s.contains("/*") && !s.contains("*/")) {
                            f = true;
                        }
                    }
                    if ("r_smc_interrupt.h".equals(file.getName())) {
                        s = dealSmcInterrupt(s);
                    }
                    newContent.add(StrUtil.split(s, "\t"));
                }
                writer.write(newContent, false);
            }
            writer.close();

            notificationBuilder.text(I18nUtils.get("smc.tool.button.generate.success"));
            notificationBuilder.showInformation();
            bindUserData();
        }
    });

    private final Collection<? extends Action> actions = List.of(generate);

    @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);

        Label generalLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.general") + ": ");
        generalField = new TextField();
        generalField.setMaxWidth(Double.MAX_VALUE);
        generalChooser = new DirectoryChooser();
        Button generalButton = FxButton.choose();
        generalField.setEditable(false);
        generalButton.setOnAction(arg0 -> {
            File file = generalChooser.showDialog(stage);
            if (file != null) {
                generalField.setText(file.getPath());
                generalChooser.setInitialDirectory(file);
            }
        });

        Label ignoreFileNamesLabel = new Label(I18nUtils.get("smc.tool.hconvertExcel.label.ignoreFileNames") + ": ");
        ignoreFileNamesField = new TextField();
        ignoreFileNamesField.setPrefWidth(Double.MAX_VALUE);
        ignoreFileNamesField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label markFileNamesLabel = new Label(I18nUtils.get("smc.tool.hconvertExcel.label.markFileNames") + ": ");
        markFileNamesField = new TextField();
        markFileNamesField.setPrefWidth(Double.MAX_VALUE);
        markFileNamesField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label supportFileTypeLabel = new Label(I18nUtils.get("smc.tool.hconvertExcel.label.supportFileType") + ": ");
        supportFileTypeField = new TextField();
        supportFileTypeField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        supportFileTypeField.setText("h");
        outputChooser.setInitialFileName("hconvert.xlsx");

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(generalLabel, 0, 1);
        grid.add(generalButton, 1, 1);
        grid.add(generalField, 2, 1);
        grid.add(ignoreFileNamesLabel, 0, 2);
        grid.add(ignoreFileNamesField, 1, 2, 2, 1);
        grid.add(markFileNamesLabel, 0, 3);
        grid.add(markFileNamesField, 1, 3, 2, 1);
        grid.add(supportFileTypeLabel, 0, 4);
        grid.add(supportFileTypeField, 1, 4, 2, 1);
        return grid;
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        BooleanBinding emptyValidation = new MultiTextInputControlEmptyBinding(generalField, supportFileTypeField).build();
        generate.disabledProperty().bind(emptyValidation);
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        userData.put("general", generalField);
        userData.put("generalChooser", generalChooser);
        userData.put("outputChooser", outputChooser);
        userData.put("ignoreFileNames", ignoreFileNamesField);
        userData.put("markFileNames", markFileNamesField);
        userData.put("supportFileType", supportFileTypeField);
    }

    @Override
    public Node getControlPanel() {
        String content = """
            {markFileNames}: {markFileNamesDesc}
            {supportFileType}: {supportFileTypeDesc}
            """;

        Map<String, String> map = new HashMap<>(8);
        map.put("markFileNames", I18nUtils.get("smc.tool.hconvertExcel.label.markFileNames"));
        map.put("markFileNamesDesc", I18nUtils.get("smc.tool.hconvertExcel.control.markFileNamesDesc"));
        map.put("supportFileType", I18nUtils.get("smc.tool.hconvertExcel.label.supportFileType"));
        map.put("supportFileTypeDesc", I18nUtils.get("smc.tool.hconvertExcel.control.supportFileTypeDesc"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "hconvertExcel";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.hconvertExcel");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/xlsx.png"));
    }

    @Override
    public String getOrderKey() {
        return "HconvertExcel";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.hconvertExcel.description");
    }

    /**
     * 处理文本
     */
    private String dealLine(String s) {
        if (s.length() < 8) {
            return s;
        }
        int i1 = s.indexOf("(");
        int i2 = s.indexOf(")");
        int i3 = s.indexOf("/*");
        if (i1 < 0 || i2 < 0) {
            return s;
        }
        String s1 = s.substring(0, 7);
        String s2 = s.substring(7, i1);
        s2 = StrUtil.trim(s2);
        String s3 = s.substring(i1, i2 + 1);
        if (i3 < 0) {
            return s1 + "\t" + s2 + "\t" + s3;
        }
        String s4 = s.substring(i3);
        return s1 + "\t" + s2 + "\t" + s3 + "\t" + s4;
    }

    /**
     * r_smc_interrupt.h 特殊处理
     */
    private String dealSmcInterrupt(String s) {
        if (s.length() < 8) {
            return s;
        }
        int i1 = s.indexOf("_INT_PRIORITY");
        if (i1 < 0) {
            return s;
        }
        String s1 = s.substring(0, 7);
        String s2 = s.substring(7, i1);
        s2 = StrUtil.trim(s2);
        String s3 = s.substring(i1);
        return s1 + "\t" + s2 + "\t" + s3;
    }

}
