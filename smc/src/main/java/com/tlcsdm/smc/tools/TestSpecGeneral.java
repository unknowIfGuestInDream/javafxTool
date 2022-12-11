package com.tlcsdm.smc.tools;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.IntegerSpinner;
import com.tlcsdm.core.javafx.dialog.ExceptionDialog;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 唐 亮
 * @date: 2022/12/8 23:12
 */
public class TestSpecGeneral extends SmcSample {

    private final Action generate = new Action(I18nUtils.get("smc.tool.button.generate"), actionEvent -> {
        ExceptionDialog exceptionDialog = new ExceptionDialog(new RuntimeException("request called failed."));
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

        IntegerSpinner i = new IntegerSpinner(0, Integer.MAX_VALUE, 50, 1);

        grid.add(toolBar, 0, 0);
        grid.add(i, 0, 1);
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
        return "TestSpecGeneral";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.girretReview");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public String getOrderKey() {
        return "TestSpecGeneral";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.girretReview.description");
    }

    private void initComponment() {

    }

}
