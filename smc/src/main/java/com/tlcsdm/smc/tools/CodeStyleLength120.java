package com.tlcsdm.smc.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
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
import org.apache.poi.ss.usermodel.*;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.action.ActionUtils.ActionTextBehavior;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 检测指定路径下文件内容长度是否超过120
 *
 * @author unknowIfGuestInDream
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

    private final Action generate = new Action(I18nUtils.get("smc.tool.fileDiff.button.generate"), actionEvent -> {
        ignoreFilesList = StrUtil.splitTrim(ignoreFileField.getText(), ",");
        fileTypeList = StrUtil.splitTrim(checkFileTypeField.getText(), ",");
        if (generateFilesParentPath == null) {
            notificationBuilder.text("The variable checkDirLabel must be a folder");
            notificationBuilder.showWarning();
            return;
        }
        File file = outPutChooser.showSaveDialog(FxApp.primaryStage);
        if (file != null) {
            if (!StrUtil.endWith(file.getName(), ".xlsx")) {
                notificationBuilder.text("请保存为xlsx文件");
                notificationBuilder.showWarning();
                return;
            }
            resultFileName = file.getName();
            resultPath = file.getParent();
            outPutChooser.setInitialDirectory(file.getParentFile());
            outPutChooser.setInitialFileName(resultFileName);
            initData();
            handleResult();
            notificationBuilder.text(I18nUtils.get("smc.tool.fileDiff.button.generate.success"));
            notificationBuilder.showInformation();
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

        Label checkDirLabel = new Label("校验文件夹路径" + ": ");
        checkDirField = new TextField();
        checkDirField.setMaxWidth(Double.MAX_VALUE);
        DirectoryChooser checkDirChooser = new DirectoryChooser();
        Button checkDirButton = new Button(I18nUtils.get("smc.tool.fileDiff.button.choose"));
        checkDirField.setEditable(false);
        checkDirButton.setOnAction(arg0 -> {
            File file = checkDirChooser.showDialog(stage);
            if (file != null) {
                checkDirField.setText(file.getPath());
                generateFilesParentPath = file;
                checkDirChooser.setInitialDirectory(file);
            }
        });

        Label checkFileTypeLabel = new Label("校验文件类型" + ": ");
        checkFileTypeField = new TextField();
        checkFileTypeField.setPrefWidth(Double.MAX_VALUE);
        checkFileTypeField.setText("c,h");

        Label ignoreFileLabel = new Label("忽略文件" + ": ");
        ignoreFileField = new TextField();
        ignoreFileField.setPrefWidth(Double.MAX_VALUE);

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

    public static void main(String[] args) {
        launch(args);
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
    public String getOrderKey() {
        return "CodeStyleLength120";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.codeStyleLength120.description");
    }

    // 初始化组件
    private void initComponment() {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel file", "*.xlsx");
        outPutChooser.getExtensionFilters().add(extFilter);
        outPutChooser.setInitialFileName(resultFileName);

        generate.setGraphic(LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/generate.png")));
        notificationBuilder.owner(FxApp.primaryStage);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        List<File> files = FileUtil.loopFiles(generateFilesParentPath, new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isFile() && !ignoreFilesList.contains(file.getName())) {
                    for (String fileType : fileTypeList) {
                        if (StrUtil.endWith(file.getName(), fileType)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        if (files.size() == 0) {
            notificationBuilder.text("There are no eligible files in the current path");
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
        if (result.size() == 0) {
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file(resultPath, resultFileName));
        setExcelStyle(writer);
        handleData(writer);
        writer.addHeaderAlias("lineNumber", "行号");
        writer.addHeaderAlias("fileName", "文件名");
        writer.addHeaderAlias("lineLength", "行文本长度");
        writer.addHeaderAlias("filePath", "文件路径");
        writer.addHeaderAlias("line", "行文本数据");
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
