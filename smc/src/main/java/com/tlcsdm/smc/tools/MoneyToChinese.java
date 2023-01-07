package com.tlcsdm.smc.tools;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.dialog.ExceptionDialog;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.util.MoneyToChineseUtil;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 金额转换为中文大写金额
 *
 * @author: 唐 亮
 * @date: 2022/12/8 23:12
 */
public class MoneyToChinese extends SmcSample {

    private NumberTextField amountField;
    private TextField chineseAmountField;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action convert = new Action(I18nUtils.get("smc.tool.button.convert"), actionEvent -> {
        try {
            String chineseAmount = MoneyToChineseUtil.number2CNMonetaryUnit(NumberUtil.toBigDecimal(amountField.getText()));
            chineseAmountField.setText(chineseAmount);
            notificationBuilder.text(I18nUtils.get("smc.tool.moneyToChinese.button.convert.success"));
            notificationBuilder.showInformation();
        } catch (NumberFormatException e) {
            ExceptionDialog exceptionDialog = new ExceptionDialog(e);
            exceptionDialog.show();
        }
    });

    private final Collection<? extends Action> actions = List.of(convert);

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

        Label amountLabel = new Label(I18nUtils.get("smc.tool.moneyToChinese.label.amount") + ": ");
        amountField = new NumberTextField();

        Label chineseAmountLabel = new Label(I18nUtils.get("smc.tool.moneyToChinese.label.chineseAmount") + ": ");
        chineseAmountField = new TextField();
        chineseAmountField.setEditable(false);

        grid.add(toolBar, 0, 0, 2, 1);

        grid.add(amountLabel, 0, 1);
        grid.add(amountField, 1, 1);

        grid.add(chineseAmountLabel, 0, 2);
        grid.add(chineseAmountField, 1, 2);

        return grid;
    }

    @Override
    public Node getControlPanel() {
        String content = """
                {convertButton}:
                {convertDesc}
                {Required} {amountLabel}
                """;
        Map<String, String> map = new HashMap<>();
        map.put("convertButton", I18nUtils.get("smc.tool.button.convert"));
        map.put("convertDesc", I18nUtils.get("smc.tool.moneyToChinese.control.textarea"));
        map.put("Required", I18nUtils.get("smc.tool.control.required"));
        map.put("amountLabel", I18nUtils.get("smc.tool.moneyToChinese.label.amount"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "moneyToChinese";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.moneyToChinese");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public String getOrderKey() {
        return "MoneyToChinese";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.moneyToChinese.description");
    }

    private void initComponment() {
        convert.setGraphic(LayoutHelper.iconView(FxApp.class.getResource("/com/tlcsdm/core/static/icon/convert.png")));
    }

}
