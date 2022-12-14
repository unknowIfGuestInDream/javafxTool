package com.tlcsdm.smc.tools;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.DiffHandleUtils;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
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
 * 检测指定路径下文件内容长度是否超过120
 *
 * @author unknowIfGuestInDream
 */
public class FileDiff extends SmcSample {

    private TextField originalField;
    private FileChooser originalFileChooser;
    private TextField compareField;
    private FileChooser compareFileChooser;
    private TextField outputField;
    private DirectoryChooser outputChooser;
    private WebView webView;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action generate = new Action(I18nUtils.get("smc.tool.button.generate"), actionEvent -> {
        // 对比 两个文件，获得不同点
        List<String> diffString = DiffHandleUtils.diffString(originalField.getText(), compareField.getText());
        String template = DiffHandleUtils.getDiffHtml(diffString);
        webView.getEngine().loadContent(template);
        notificationBuilder.text(I18nUtils.get("smc.tool.fileDiff.button.generate.success"));
        notificationBuilder.showInformation();
        bindUserData();
    });

    private final Action download = new Action(I18nUtils.get("smc.tool.button.download"), actionEvent -> {
        if (StrUtil.isEmpty(outputField.getText())) {
            notificationBuilder.text(I18nUtils.get("smc.tool.fileDiff.label.output.valid"));
            notificationBuilder.showWarning();
            return;
        }
        List<String> diffString = DiffHandleUtils.diffString(originalField.getText(), compareField.getText());
        // 生成一个diff.html文件，打开便可看到两个文件的对比
        DiffHandleUtils.generateDiffHtml(diffString, outputField.getText() + "\\diff.html");
        notificationBuilder.text(I18nUtils.get("smc.tool.fileDiff.button.download.success"));
        notificationBuilder.showInformation();
        bindUserData();
    });

    private final Collection<? extends Action> actions = List.of(generate, download);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.fileDiff");
    }

    @Override
    public Node getPanel(Stage stage) {
        initActions();
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
        Label originalLabel = new Label(I18nUtils.get("smc.tool.fileDiff.label.original") + ": ");
        originalField = new TextField();
        originalField.setMaxWidth(Double.MAX_VALUE);
        originalFileChooser = new FileChooser();
        originalFileChooser.getExtensionFilters().add(extFilter);
        originalFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(I18nUtils.get("smc.tool.fileChooser.extensionFilter.all"), "*"));
        Button originalButton = new Button(I18nUtils.get("smc.tool.button.choose"));
        originalField.setEditable(false);
        originalButton.setOnAction(arg0 -> {
            File file = originalFileChooser.showOpenDialog(stage);
            if (file != null) {
                originalField.setText(file.getPath());
                originalFileChooser.setInitialDirectory(file.getParentFile());
            }
        });

        // compare
        Label compareLabel = new Label(I18nUtils.get("smc.tool.fileDiff.label.compare") + ": ");
        compareField = new TextField();
        compareField.setMaxWidth(Double.MAX_VALUE);
        compareFileChooser = new FileChooser();
        compareFileChooser.getExtensionFilters().add(extFilter);
        compareFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("all", "*"));
        Button compareButton = new Button(I18nUtils.get("smc.tool.button.choose"));
        compareField.setEditable(false);
        compareButton.setOnAction(arg0 -> {
            File file = compareFileChooser.showOpenDialog(stage);
            if (file != null) {
                compareField.setText(file.getPath());
                compareFileChooser.setInitialDirectory(file.getParentFile());
            }
        });

        // output
        Label outputLabel = new Label(I18nUtils.get("smc.tool.fileDiff.label.output") + ": ");
        outputField = new TextField();
        outputField.setMaxWidth(Double.MAX_VALUE);
        outputChooser = new DirectoryChooser();
        Button outputButton = new Button(I18nUtils.get("smc.tool.button.choose"));
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

        userData.put("original", originalField);
        userData.put("originalChoose", originalFileChooser);
        userData.put("compare", compareField);
        userData.put("compareChoose", compareFileChooser);
        userData.put("output", outputField);
        userData.put("outputChoose", outputChooser);

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
        map.put("generateButton", I18nUtils.get("smc.tool.button.generate"));
        map.put("generateDesc", I18nUtils.get("smc.tool.fileDiff.control.textarea1"));
        map.put("Required", I18nUtils.get("smc.tool.control.required"));
        map.put("originalLabel", I18nUtils.get("smc.tool.fileDiff.label.original"));
        map.put("compareLabel", I18nUtils.get("smc.tool.fileDiff.label.compare"));
        map.put("downloadButton", I18nUtils.get("smc.tool.button.download"));
        map.put("downloadDesc", I18nUtils.get("smc.tool.fileDiff.control.textarea2"));
        map.put("outputLabel", I18nUtils.get("smc.tool.fileDiff.label.output"));
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
        return I18nUtils.get("smc.sampleName.fileDiff.description");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    private void initActions() {
        generate.setGraphic(LayoutHelper.iconView(FxApp.class.getResource("/com/tlcsdm/core/static/icon/generate.png")));
        download.setGraphic(LayoutHelper.iconView(FxApp.class.getResource("/com/tlcsdm/core/static/icon/download.png")));
        notificationBuilder.owner(FxApp.primaryStage);
    }

}
