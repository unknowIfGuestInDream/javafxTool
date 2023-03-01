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
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 头文件转换为excel UD
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/2/28 23:12
 */
public class HconvertExcel extends SmcSample {

    private FileChooser outputChooser = new FileChooser();
    private TextField generalField;
    private DirectoryChooser generalChooser;
    private TextField ignoreFileNamesField;
    private TextField markFileNamesField;
    private TextField supportFileTypeField;

    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action generate = FxAction.generate(actionEvent -> {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel file", "*.xlsx");
        outputChooser.getExtensionFilters().add(extFilter);
        File file = outputChooser.showSaveDialog(FxApp.primaryStage);
        if (file != null) {
            if (!StrUtil.endWith(file.getName(), ".xlsx")) {
                notificationBuilder.text(I18nUtils.get("smc.tool.codeStyleLength120.button.generate.warn.message2"));
                notificationBuilder.showWarning();
                return;
            }
            if (file.exists()) {
                FileUtil.del(file);
            }
            // 输入值获取
            List<String> ignoreFileNamesNames = StrUtil.splitTrim(ignoreFileNamesField.getText(), ",");
            List<String> markFileNamesNames = StrUtil.splitTrim(markFileNamesField.getText(), ",");
            List<String> supportFileType = StrUtil.splitTrim(supportFileTypeField.getText(), ",");
            String generateFilesPath = generalField.getText();

            //FileUtil.writeFromStream(templateFile, file);
            notificationBuilder.text(I18nUtils.get("smc.tool.dtsTriggerSourceXml.button.generate.success"));
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

        Label ignoreFileNamesLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.ignoreSheet") + ": ");
        ignoreFileNamesField = new TextField();
        ignoreFileNamesField.setPrefWidth(Double.MAX_VALUE);
        ignoreFileNamesField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label markFileNamesLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.markSheet") + ": ");
        markFileNamesField = new TextField();
        markFileNamesField.setPrefWidth(Double.MAX_VALUE);
        markFileNamesField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label supportFileTypeLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.markSheet") + ": ");
        supportFileTypeField = new TextField();
        supportFileTypeField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        supportFileTypeField.setText("h");

        userData.put("general", generalField);
        userData.put("generalChooser", generalChooser);
        userData.put("outputChooser", outputChooser);
        userData.put("ignoreFileNames", ignoreFileNamesField);
        userData.put("markFileNames", markFileNamesField);
        userData.put("supportFileType", supportFileTypeField);

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
    public Node getControlPanel() {
        String content = """
                {diffButton}:
                {diffDesc}

                {excelLabel}: {excelDesc}
                {generalLabel}: {generalDesc}
                {macroLengthLabel}: {macroLengthDesc}
                {markFileNamesLabel}: {markFileNamesDesc}

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
        map.put("markFileNamesLabel", I18nUtils.get("smc.tool.specGeneralTest.label.markSheet"));
        map.put("markFileNamesDesc", I18nUtils.get("smc.tool.specGeneralTest.control.markSheetDesc"));
        map.put("note", I18nUtils.get("smc.tool.control.note"));
        map.put("noteDesc", I18nUtils.get("smc.tool.specGeneralTest.control.noteDesc"));
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
        return "HconvertExcel";
        // return I18nUtils.get("smc.sampleName.specGeneralTest");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public String getOrderKey() {
        return "HconvertExcel";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.specGeneralTest.description");
    }

}
