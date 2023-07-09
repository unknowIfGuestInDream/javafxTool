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

package com.tlcsdm.jfxcommon.tools;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.bind.MultiTextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.bind.TextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.util.DiffHandleUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.action.ActionUtils.ActionTextBehavior;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件差分
 *
 * @author unknowIfGuestInDream
 */
public class FileDiff extends CommonSample {

    private TextField originalField;
    private FileChooser originalFileChooser;
    private TextField compareField;
    private FileChooser compareFileChooser;
    private TextField outputField;
    private DirectoryChooser outputChooser;
    private WebView webView;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action openOutDir = FxAction.openOutDir(actionEvent -> {
        String outPath = outputField.getText();
        if (StrUtil.isEmpty(outPath)) {
            notificationBuilder.text(I18nUtils.get("common.button.openOutDir.warnMsg"));
            notificationBuilder.showWarning();
            return;
        }
        JavaFxSystemUtil.openDirectory(outPath);
    });

    private final Action generate = FxAction.generate(actionEvent -> {
        // 对比 两个文件，获得不同点
        List<String> diffString = DiffHandleUtil.diffString(originalField.getText(), compareField.getText());
        String template = DiffHandleUtil.getDiffHtml(List.of(diffString));
        webView.getEngine().loadContent(template);
        notificationBuilder.text(I18nUtils.get("common.tool.fileDiff.button.generate.success"));
        notificationBuilder.showInformation();
        bindUserData();
    });

    private final Action download = FxAction.download(actionEvent -> {
        if (StrUtil.isEmpty(outputField.getText())) {
            notificationBuilder.text(I18nUtils.get("common.tool.fileDiff.label.output.valid"));
            notificationBuilder.showWarning();
            return;
        }
        List<String> diffString = DiffHandleUtil.diffString(originalField.getText(), compareField.getText());
        // 生成一个diff.html文件，打开便可看到两个文件的对比
        DiffHandleUtil.generateDiffHtml(diffString, outputField.getText() + File.separator + "diff.html");
        notificationBuilder.text(I18nUtils.get("common.tool.fileDiff.button.download.success"));
        notificationBuilder.showInformation();
        bindUserData();
    });

    private final Collection<? extends Action> actions = List.of(generate, download, openOutDir);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("common.tool.fileDiff.sampleName");
    }

    @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("text files", "*.txt", "*.c", "*.h",
            "*.java", "*.html", "*.xml");
        // original
        Label originalLabel = new Label(I18nUtils.get("common.tool.fileDiff.label.original") + ": ");
        originalField = new TextField();
        originalField.setMaxWidth(Double.MAX_VALUE);
        originalFileChooser = new FileChooser();
        originalFileChooser.getExtensionFilters().add(extFilter);
        originalFileChooser.getExtensionFilters()
            .add(new FileChooser.ExtensionFilter(I18nUtils.get("common.fileChooser.extensionFilter.all"), "*"));
        Button originalButton = FxButton.choose();
        originalField.setEditable(false);
        originalButton.setOnAction(arg0 -> {
            File file = originalFileChooser.showOpenDialog(stage);
            if (file != null) {
                originalField.setText(file.getPath());
                originalFileChooser.setInitialDirectory(file.getParentFile());
            }
        });

        // compare
        Label compareLabel = new Label(I18nUtils.get("common.tool.fileDiff.label.compare") + ": ");
        compareField = new TextField();
        compareField.setMaxWidth(Double.MAX_VALUE);
        compareFileChooser = new FileChooser();
        compareFileChooser.getExtensionFilters().add(extFilter);
        compareFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(I18nUtils.get("common.fileChooser.extensionFilter.all"), "*"));
        Button compareButton = FxButton.choose();
        compareField.setEditable(false);
        compareButton.setOnAction(arg0 -> {
            File file = compareFileChooser.showOpenDialog(stage);
            if (file != null) {
                compareField.setText(file.getPath());
                compareFileChooser.setInitialDirectory(file.getParentFile());
            }
        });

        // output
        Label outputLabel = new Label(I18nUtils.get("common.tool.label.output") + ": ");
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

        // webView
        webView = new WebView();
        webView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setVgrow(webView, Priority.ALWAYS);

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(originalLabel, 0, 1);
        grid.add(originalButton, 1, 1);
        grid.add(originalField, 2, 1);
        grid.add(compareLabel, 0, 2);
        grid.add(compareButton, 1, 2);
        grid.add(compareField, 2, 2);
        grid.add(outputLabel, 0, 3);
        grid.add(outputButton, 1, 3);
        grid.add(outputField, 2, 3);
        grid.add(webView, 0, 4, 3, 1);

        return grid;
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        BooleanBinding outputValidation = new TextInputControlEmptyBinding(outputField).build();
        BooleanBinding emptyValidation = new MultiTextInputControlEmptyBinding(originalField, compareField).build();

        generate.disabledProperty().bind(emptyValidation);
        download.disabledProperty().bind(emptyValidation.or(outputValidation));
        openOutDir.disabledProperty().bind(outputValidation);
        FileChooserUtil.setOnDrag(originalField, FileChooserUtil.FileType.FILE);
        FileChooserUtil.setOnDrag(outputField, FileChooserUtil.FileType.FOLDER);
        FileChooserUtil.setOnDrag(compareField, FileChooserUtil.FileType.FILE);
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        userData.put("original", originalField);
        userData.put("originalChoose", originalFileChooser);
        userData.put("compare", compareField);
        userData.put("compareChoose", compareFileChooser);
        userData.put("output", outputField);
        userData.put("outputChoose", outputChooser);
    }

    @Override
    public Node getControlPanel() {
        String content = """
            {generateButton}:
            {generateDesc}
            {Required} {originalLabel}, {compareLabel}

            {downloadButton}:
            {downloadDesc}
            {Required} {originalLabel}, {compareLabel}, {outputLabel}
            """;
        Map<String, String> map = new HashMap<>();
        map.put("generateButton", generate.getText());
        map.put("generateDesc", I18nUtils.get("common.tool.fileDiff.control.textarea1"));
        map.put("Required", I18nUtils.get("common.control.required"));
        map.put("originalLabel", I18nUtils.get("common.tool.fileDiff.label.original"));
        map.put("compareLabel", I18nUtils.get("common.tool.fileDiff.label.compare"));
        map.put("downloadButton", download.getText());
        map.put("downloadDesc", I18nUtils.get("common.tool.fileDiff.control.textarea2"));
        map.put("outputLabel", I18nUtils.get("common.tool.label.output"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    @Override
    public String getSampleId() {
        return "fillDiff";
    }

    @Override
    public String getOrderKey() {
        return "FillDiff";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("common.tool.fileDiff.sampleDesc");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/jfxcommon/static/icon/diff.png"));
    }

}
