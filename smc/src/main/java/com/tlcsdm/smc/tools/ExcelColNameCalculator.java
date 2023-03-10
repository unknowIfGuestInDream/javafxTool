package com.tlcsdm.smc.tools;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.textfield.TextFields;

import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.ExceptionDialog;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Excel列名计算器
 *
 * @author: unknowIfGuestInDream
 * @date: 2022/3/8 8:12
 */
public class ExcelColNameCalculator extends SmcSample {

    private TextField colNameField;
    private NumberTextField offsetField;
    private TextField resultField;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action generate = FxAction.generate(actionEvent -> {
        try {
            List<String> colNameList = StrUtil.splitTrim(colNameField.getText(), ",");
            int offset = Integer.parseInt(offsetField.getText());
            StringJoiner sj = new StringJoiner(", ");
            for (int i = 0; i < colNameList.size(); i++) {
                int index = ExcelUtil.colNameToIndex(colNameList.get(i));
                sj.add(ExcelUtil.indexToColName(index + offset));
            }
            resultField.setText(sj.toString());
            notificationBuilder.text(I18nUtils.get("smc.tool.dtsTriggerSourceXml.button.generate.success"));
            notificationBuilder.showInformation();
        } catch (NumberFormatException e) {
            ExceptionDialog exceptionDialog = new ExceptionDialog(e);
            exceptionDialog.show();
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

        Label colNameLabel = new Label(I18nUtils.get("smc.tool.excelColNameCalculator.label.colName") + ": ");
        colNameField = TextFields.createClearableTextField();
        colNameField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label offsetLabel = new Label(I18nUtils.get("smc.tool.excelColNameCalculator.label.offset") + ": ");
        offsetField = new NumberTextField();

        Label resultLabel = new Label(I18nUtils.get("smc.tool.excelColNameCalculator.label.result") + ": ");
        resultField = new TextField();
        resultField.setEditable(false);

        grid.add(toolBar, 0, 0, 2, 1);
        grid.add(colNameLabel, 0, 1);
        grid.add(colNameField, 1, 1);
        grid.add(offsetLabel, 0, 2);
        grid.add(offsetField, 1, 2);
        grid.add(resultLabel, 0, 3);
        grid.add(resultField, 1, 3);

        return grid;
    }

    @Override
    public Node getControlPanel() {
        return FxTextInput.textArea("");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "excelColNameCalculator";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.excelColNameCalculator");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public String getOrderKey() {
        return "ExcelColNameCalculator";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.excelColNameCalculator.description");
    }

}
