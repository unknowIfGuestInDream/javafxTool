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

package com.tlcsdm.smc.unitTest;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.cell.CellLocation;
import cn.hutool.poi.excel.cell.CellUtil;
import com.tlcsdm.core.factory.config.ThreadPoolTaskExecutor;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.bind.MultiTextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.bind.TextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.ExceptionDialog;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.core.util.DiffHandleUtil;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 为specGeneral测试文档的测试生成差异文件, 提高测试效率
 *
 * @author: unknowIfGuestInDream
 * @date: 2022/12/8 23:12
 */
public class SpecGeneralTest extends SmcSample {

    // 只生成测试文件
    private CheckBox mergeResultCheck;
    private CheckBox onlyGenerateCheck;
    private TextField excelField;
    private FileChooser excelFileChooser;
    private TextField generalField;
    private DirectoryChooser generalChooser;
    private TextField outputField;
    private DirectoryChooser outputChooser;
    private NumberTextField macroLengthField;
    private TextField ignoreSheetField;
    private TextField markSheetField;
    private TextField startCellField;
    private TextField generalFileCellField;
    private TextField endCellColumnField;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action openOutDir = FxAction.openOutDir(actionEvent -> {
        String outPath = outputField.getText();
        if (StrUtil.isEmpty(outPath)) {
            notificationBuilder.text(I18nUtils.get("smc.tool.button.openOutDir.warnMsg"));
            notificationBuilder.showWarning();
            return;
        }
        String excelName = FileUtil.getName(excelField.getText());
        String path = "";
        if (!StrUtil.isEmpty(excelName)) {
            path = outPath + File.separator + excelName.substring(0, excelName.lastIndexOf("."));
        }
        if (!FileUtil.exist(path)) {
            path = outPath;
        } else {
            boolean onlyGenerate = onlyGenerateCheck.isSelected();
            if (onlyGenerate && FileUtil.exist(path + File.separator + "files")) {
                path = path + File.separator + "files";
            }
        }
        JavaFxSystemUtil.openDirectory(path);
    });

    /**
     * 结果文件结构:
     *
     * <pre>
     *  excelField同级目录下
     *    excelField同名文件夹
     *      html差分文件
     *      files文件夹
     *        ud读取后生成的用于差分的文件
     *        ud读取后生成的excel
     * </pre>
     */
    private final Action diff = FxAction.create(I18nUtils.get("smc.tool.specGeneralTest.button.diff"), actionEvent -> {
        ThreadPoolTaskExecutor.get().execute(new Runnable() {

            @Override
            public void run() {
                try {
                    StaticLog.info("Extracting data...");
                    // 输入值获取
                    List<String> ignoreSheetNames = StrUtil.splitTrim(ignoreSheetField.getText(), ",");
                    List<String> markSheetNames = StrUtil.splitTrim(markSheetField.getText(), ",");
                    String parentDirectoryPath = FileUtil.getParent(excelField.getText(), 1);
                    String excelName = FileUtil.getName(excelField.getText());
                    String startCell = startCellField.getText();
                    String endCellColumn = endCellColumnField.getText();
                    String generateFileCell = generalFileCellField.getText();
                    String generateFilesParentPath = generalField.getText();
                    String outputPath = outputField.getText();
                    boolean onlyGenerate = onlyGenerateCheck.isSelected();
                    boolean mergeResult = mergeResultCheck.isSelected();
                    // 此处传入的是从头文件获取的列索引，长度需要-1
                    int macroLength = Integer.parseInt(macroLengthField.getText()) - 1;
                    StaticLog.info("Processing data...");
                    // 需要数据抽取
                    ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName));
                    List<String> sheetNames = reader.getSheetNames().stream()
                        .filter(s -> (markSheetNames.size() == 0 && !ignoreSheetNames.contains(s))
                            || (markSheetNames.size() != 0 && markSheetNames.contains(s)))
                        .collect(Collectors.toList());
                    reader.close();
                    String resultPath = outputPath + File.separator
                        + excelName.substring(0, excelName.lastIndexOf("."));
                    String filesPath = resultPath + File.separator + "files";
                    // 清空resultPath下文件
                    FileUtil.clean(resultPath);
                    StaticLog.info("Processing data...");
                    // 处理数据
                    File udFile = FileUtil.file(parentDirectoryPath, excelName);
                    Map<String, String> generateFileMap = new HashMap<>();
                    for (String sheetName : sheetNames) {
                        BigExcelWriter excelWriter = ExcelUtil.getBigWriter();
                        StaticLog.info("========================= Begin Reading {} =========================",
                            sheetName);
                        ExcelReader r = ExcelUtil.getReader(udFile, sheetName);
                        String endCell = getEndCell(endCellColumn, r);
                        StaticLog.info("endCell: {}", endCell);
                        CellLocation start = ExcelUtil.toLocation(startCell);
                        CellLocation end = ExcelUtil.toLocation(endCell);
                        int startX = start.getX();
                        int startY = start.getY();
                        int endX = end.getX();
                        int endY = end.getY();
                        String generateFileName = r.getCell(generateFileCell).getStringCellValue();
                        generateFileMap.put(sheetName, generateFileName);
                        excelWriter.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
                        excelWriter.getStyleSet().setBorder(BorderStyle.NONE, IndexedColors.BLACK);
                        excelWriter.renameSheet(0, sheetName);
                        excelWriter.setColumnWidth(0, 15);
                        excelWriter.setColumnWidth(1, 60);
                        excelWriter.setColumnWidth(2, 18);
                        excelWriter.setColumnWidth(3, 38);
                        List<List<String>> list = new ArrayList<>(endY - startY + 1);
                        boolean isIfnDef = false;
                        boolean isMultDesc = false;
                        for (int j = startY; j <= endY; j++) {
                            List<String> l = new ArrayList<>(endX - startX + 1);
                            boolean isDefine = false;
                            // 第一列发现是define 或者 ifndef 即在后面添加空格
                            String firstValue = CoreUtil.valueOf(CellUtil.getCellValue(r.getCell(startX, j)));
                            if ("#define".equals(StrUtil.trim(firstValue))) {
                                isDefine = true;
                            }
                            if ("#ifndef".equals(StrUtil.trim(firstValue))) {
                                isIfnDef = true;
                            }
                            for (int j2 = startX; j2 <= endX; j2++) {
                                String cellValue = CoreUtil.valueOf(CellUtil.getCellValue(r.getCell(j2, j)));
                                if (isDefine && !isIfnDef && j2 < endX) {
                                    String cellSubString = " ";
                                    String cv = StrUtil.trimEnd(cellValue);
                                    // 给macro值填充空格
                                    if (j2 == startX + 1) {
                                        String s = "#define " + cv;
                                        if (s.length() < macroLength && StrUtil
                                            .trimEnd(CoreUtil.valueOf(CellUtil.getCellValue(r.getCell(j2 + 1, j))))
                                            .length() != 0) {
                                            cellSubString = CharSequenceUtil.repeat(" ", macroLength - s.length());
                                        }
                                    }
                                    cellValue = cv + cellSubString;
                                }
                                if (isIfnDef && j2 == startX) {
                                    cellValue = cellValue + " ";
                                }
                                // 多行注释处理
                                if (isDefine && j2 == endX) {
                                    if (cellValue.contains("/*") && !cellValue.contains("*/")) {
                                        isMultDesc = true;
                                    }
                                }
                                l.add(cellValue);
                            }
                            if (isIfnDef && isDefine) {
                                isIfnDef = false;
                            }
                            // 多行注释处理
                            if (!isDefine && isMultDesc) {
                                List<String> pre = list.get(list.size() - 1);
                                int length = 0;
                                for (int i = 0; i < pre.size() - 1; i++) {
                                    length += pre.get(i).length();
                                }
                                l.add(0, CharSequenceUtil.repeat(" ", length + 3));
                                isMultDesc = false;
                            }
                            list.add(l);
                        }
                        r.close();
                        // 将从UD中的内容生成到指定路径, 用来后续进行差分
                        excelWriter.write(list, false);
                        File file = FileUtil.file(filesPath, sheetName + ".xlsx");
                        excelWriter.flush(file);
                        excelWriter.close();
                        StaticLog.info("========================= End Reading {} =========================", sheetName);
                    }
                    StaticLog.info("Generate result...");
                    // 将之前读取的内容与generalField文件夹下的文件进行差分
                    List<List<String>> diffStringList = new ArrayList<>();
                    for (String sheetName : sheetNames) {
                        ExcelReader r = ExcelUtil.getReader(FileUtil.file(filesPath, sheetName + ".xlsx"), sheetName);
                        String generateFileName = generateFileMap.get(sheetName);
                        FileUtil.writeUtf8String(r.readAsText(false).replaceAll("\\t", ""),
                            FileUtil.file(filesPath, generateFileName));
                        r.close();
                        if (onlyGenerate) {
                            continue;
                        }
                        StaticLog.info("========================= Begin Comparing {} =========================",
                            generateFileName);
                        File generateFile = FileUtil.file(generateFilesParentPath, generateFileName);
                        if (FileUtil.exist(generateFile)) {
                            List<String> diffString = DiffHandleUtil.diffString(
                                filesPath + File.separator + generateFileName,
                                generateFilesParentPath + File.separator + generateFileName);
                            if (mergeResult) {
                                diffStringList.add(diffString);
                            } else {
                                DiffHandleUtil.generateDiffHtml(diffString,
                                    resultPath + File.separator + sheetName + ".html");
                            }
                        } else {
                            StaticLog.info("========================= Not Found {} =========================",
                                generateFileName);
                            continue;
                        }
                        // 此处睡眠, 防止出现读取上的错误
                        ThreadUtil.safeSleep(500);
                        StaticLog.info("========================= End Comparing {} =========================",
                            generateFileName);
                    }
                    if (!onlyGenerate && mergeResult) {
                        DiffHandleUtil.generateDiffHtml(resultPath + File.separator + "overview.html", diffStringList);
                    }
                    FxApp.runLater(() -> {
                        notificationBuilder.text(I18nUtils.get("smc.tool.specGeneralTest.button.diff.success"));
                        notificationBuilder.showInformation();
                    });
                    bindUserData();
                } catch (Exception e) {
                    FxApp.runLater(() -> {
                        ExceptionDialog exceptionDialog = new ExceptionDialog(e);
                        exceptionDialog.show();
                    });
                }
            }
        });
    }, LayoutHelper.iconView(this.getClass().getResource("/com/tlcsdm/smc/static/icon/diff.png")));

    private final Collection<? extends Action> actions = List.of(diff, openOutDir);

    @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);

        Label mergeResultLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.mergeResult") + ": ");
        mergeResultCheck = new CheckBox();

        Label onlyGenerateLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.onlyGenerate") + ": ");
        onlyGenerateCheck = new CheckBox();

        Label excelLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.excel") + ": ");
        excelField = new TextField();
        excelField.setMaxWidth(Double.MAX_VALUE);
        excelFileChooser = new FileChooser();
        excelFileChooser.getExtensionFilters().add(FileChooserUtil.excelFilter());

        Button excelButton = FxButton.choose();
        excelField.setEditable(false);
        excelButton.setOnAction(arg0 -> {
            File file = excelFileChooser.showOpenDialog(stage);
            if (file != null) {
                excelField.setText(file.getPath());
                excelFileChooser.setInitialDirectory(file.getParentFile());
            }
        });

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

        Label outputLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.output") + ": ");
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

        Label macroLengthLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.macroLength") + ": ");
        macroLengthField = new NumberTextField();

        Label ignoreSheetLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.ignoreSheet") + ": ");
        ignoreSheetField = new TextField();
        ignoreSheetField.setPrefWidth(Double.MAX_VALUE);
        ignoreSheetField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label markSheetLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.markSheet") + ": ");
        markSheetField = new TextField();
        markSheetField.setPrefWidth(Double.MAX_VALUE);
        markSheetField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label startCellLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.startCell") + ": ");
        startCellField = new TextField();

        Label endCellColumnLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.endCellColumn") + ": ");
        endCellColumnField = new TextField();

        Label generalFileCellLabel = new Label(I18nUtils.get("smc.tool.specGeneralTest.label.generalFileCell") + ": ");
        generalFileCellField = new TextField();

        ignoreSheetField.setText("Overview, Summary, Sample-CT");
        startCellField.setText("C19");
        endCellColumnField.setText("F");
        generalFileCellField.setText("C15");
        macroLengthField.setNumber(new BigDecimal("60"));

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(mergeResultLabel, 0, 1);
        grid.add(mergeResultCheck, 1, 1, 2, 1);
        grid.add(onlyGenerateLabel, 0, 2);
        grid.add(onlyGenerateCheck, 1, 2, 2, 1);
        grid.add(excelLabel, 0, 3);
        grid.add(excelButton, 1, 3);
        grid.add(excelField, 2, 3);
        grid.add(generalLabel, 0, 4);
        grid.add(generalButton, 1, 4);
        grid.add(generalField, 2, 4);
        grid.add(outputLabel, 0, 5);
        grid.add(outputButton, 1, 5);
        grid.add(outputField, 2, 5);
        grid.add(macroLengthLabel, 0, 6);
        grid.add(macroLengthField, 1, 6, 2, 1);
        grid.add(ignoreSheetLabel, 0, 7);
        grid.add(ignoreSheetField, 1, 7, 2, 1);
        grid.add(markSheetLabel, 0, 8);
        grid.add(markSheetField, 1, 8, 2, 1);
        grid.add(startCellLabel, 0, 9);
        grid.add(startCellField, 1, 9, 2, 1);
        grid.add(endCellColumnLabel, 0, 10);
        grid.add(endCellColumnField, 1, 10, 2, 1);
        grid.add(generalFileCellLabel, 0, 11);
        grid.add(generalFileCellField, 1, 11, 2, 1);
        return grid;
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        BooleanBinding outputValidation = new TextInputControlEmptyBinding(outputField).build();
        BooleanBinding emptyValidation = new MultiTextInputControlEmptyBinding(excelField, generalField, outputField,
            macroLengthField, startCellField, generalFileCellField, endCellColumnField).build();
        diff.disabledProperty().bind(emptyValidation);
        openOutDir.disabledProperty().bind(outputValidation);
        mergeResultCheck.disableProperty().bindBidirectional(onlyGenerateCheck.selectedProperty());
        FileChooserUtil.setOnDrag(excelField, FileChooserUtil.FileType.FILE);
        FileChooserUtil.setOnDrag(outputField, FileChooserUtil.FileType.FOLDER);
        FileChooserUtil.setOnDrag(generalField, FileChooserUtil.FileType.FOLDER);
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        userData.put("mergeResult", mergeResultCheck);
        userData.put("onlyGenerate", onlyGenerateCheck);
        userData.put("excel", excelField);
        userData.put("excelFileChooser", excelFileChooser);
        userData.put("general", generalField);
        userData.put("generalChooser", generalChooser);
        userData.put("output", outputField);
        userData.put("outputChooser", outputChooser);
        userData.put("macroLength", macroLengthField);
        userData.put("ignoreSheet", ignoreSheetField);
        userData.put("markSheet", markSheetField);
        userData.put("startCell", startCellField);
        userData.put("generalFileCell", generalFileCellField);
        userData.put("endCellColumn", endCellColumnField);
    }

    @Override
    public Node getControlPanel() {
        String content = """
            {diffButton}:
            {diffDesc}

            {excelLabel}: {excelDesc}
            {generalLabel}: {generalDesc}
            {macroLengthLabel}: {macroLengthDesc}
            {markSheetLabel}: {markSheetDesc}

            {note}
            {noteDesc}
            """;

        Map<String, String> map = new HashMap<>(32);
        map.put("diffButton", diff.getText());
        map.put("diffDesc", I18nUtils.get("smc.tool.specGeneralTest.button.diff.desc"));
        map.put("Required", I18nUtils.get("smc.tool.control.required"));
        map.put("excelLabel", I18nUtils.get("smc.tool.specGeneralTest.label.excel"));
        map.put("excelDesc", "eg: TestSpec_General_RH850U2A.xlsx");
        map.put("generalLabel", I18nUtils.get("smc.tool.specGeneralTest.label.general"));
        map.put("generalDesc", "eg: {user.dir}\\src\\smc_gen\\general");
        map.put("macroLengthLabel", I18nUtils.get("smc.tool.specGeneralTest.label.macroLength"));
        map.put("macroLengthDesc", I18nUtils.get("smc.tool.specGeneralTest.control.macroLengthDesc"));
        map.put("markSheetLabel", I18nUtils.get("smc.tool.specGeneralTest.label.markSheet"));
        map.put("markSheetDesc", I18nUtils.get("smc.tool.specGeneralTest.control.markSheetDesc"));
        map.put("note", I18nUtils.get("smc.tool.control.note"));
        map.put("noteDesc", I18nUtils.get("smc.tool.specGeneralTest.control.noteDesc"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "specGeneralTest";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.specGeneralTest");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.4";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/test1.png"));
    }

    @Override
    public String getOrderKey() {
        return "SpecGeneralTest";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.specGeneralTest.description");
    }

    /**
     * 获取EndCell值
     * <p>
     * 为End Sheet 所在行数 -2
     */
    private String getEndCell(String endCellColumn, ExcelReader reader) {
        return endCellColumn + (reader.getRowCount() - 2);
    }

}
