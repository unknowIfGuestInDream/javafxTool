package com.tlcsdm.smc.tools;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.dialog.ExceptionDialog;
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
 * 为specGeneral测试文档的测试生成差异文件, 提高测试效率
 *
 * @author: 唐 亮
 * @date: 2022/12/8 23:12
 */
public class SpecGeneralTest extends SmcSample {

    private TextField originalField;
    private FileChooser originalFileChooser;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action generate = new Action(I18nUtils.get("smc.tool.button.generate"), actionEvent -> {
        ExceptionDialog exceptionDialog = new ExceptionDialog(new UnExpectedResultException("request called failed."));
        exceptionDialog.show();
    });

    private final Collection<? extends Action> actions = List.of(generate);

    @Override
    public Node getPanel(Stage stage) {
        initComponment();
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel file", "*.xlsx");

        Label originalLabel = new Label(I18nUtils.get("smc.tool.fileDiff.label.original") + ": ");
        originalField = new TextField();
        originalField.setMaxWidth(Double.MAX_VALUE);
        originalFileChooser = new FileChooser();
        originalFileChooser.getExtensionFilters().add(extFilter);
        Button originalButton = new Button(I18nUtils.get("smc.tool.button.choose"));
        originalField.setEditable(false);
        originalButton.setOnAction(arg0 -> {
            File file = originalFileChooser.showOpenDialog(stage);
            if (file != null) {
                originalField.setText(file.getPath());
                originalFileChooser.setInitialDirectory(file.getParentFile());
            }
        });

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(originalLabel, 0, 1);
        grid.add(originalButton, 1, 1);
        grid.add(originalField, 2, 1);
        return grid;
    }

    @Override
    public Node getControlPanel() {
        String content = """
                GerritAccount&XSRF_TOKEN{tokenDesc}
                {userName}&{passwd}{girretUserDesc}
                {ownerEmail}{ownerEmailDesc}
                {limit}{limitDesc}
                {ignoreGirretNumber}{ignoreGirretNumberDesc}
                {startDate}: {startDateDesc}
                {reserveJson}: {reserveJsonDesc}
                {girretUrl}{girretUrlDesc}
                """;
        Map<String, String> map = new HashMap<>(32);
        map.put("tokenDesc", I18nUtils.get("smc.tool.girretReview.control.textarea1"));
        map.put("userName", I18nUtils.get("smc.tool.girretReview.label.userName"));
        map.put("passwd", I18nUtils.get("smc.tool.girretReview.label.passwd"));
        map.put("girretUserDesc", I18nUtils.get("smc.tool.girretReview.control.textarea2"));
        map.put("ownerEmail", I18nUtils.get("smc.tool.girretReview.label.ownerEmail"));
        map.put("ownerEmailDesc", I18nUtils.get("smc.tool.girretReview.control.textarea3"));
        map.put("limit", I18nUtils.get("smc.tool.girretReview.label.limit"));
        map.put("limitDesc", I18nUtils.get("smc.tool.girretReview.control.textarea4"));
        map.put("ignoreGirretNumber", I18nUtils.get("smc.tool.girretReview.label.ignoreGirretNumber"));
        map.put("ignoreGirretNumberDesc", I18nUtils.get("smc.tool.girretReview.control.textarea5"));
        map.put("startDate", I18nUtils.get("smc.tool.girretReview.label.startDate"));
        map.put("startDateDesc", I18nUtils.get("smc.tool.girretReview.control.textarea6"));
        map.put("reserveJson", I18nUtils.get("smc.tool.girretReview.label.reserveJson"));
        map.put("reserveJsonDesc", I18nUtils.get("smc.tool.girretReview.control.textarea7"));
        map.put("girretUrl", I18nUtils.get("smc.tool.girretReview.label.girretUrl"));
        map.put("girretUrlDesc", I18nUtils.get("smc.tool.girretReview.control.textarea8"));
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
        return "1.0.0";
    }

    @Override
    public String getOrderKey() {
        return "SpecGeneralTest";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.specGeneralTest.description");
    }

    private void initComponment() {
        generate.setGraphic(LayoutHelper.iconView(FxApp.class.getResource("/com/tlcsdm/core/static/icon/generate.png")));
    }

}
