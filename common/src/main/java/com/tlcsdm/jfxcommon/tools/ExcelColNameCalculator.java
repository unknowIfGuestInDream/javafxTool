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
import cn.hutool.poi.excel.ExcelUtil;
import com.tlcsdm.core.javafx.bind.MultiTextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.textfield.TextFields;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

/**
 * Excel列名计算器
 *
 * @author unknowIfGuestInDream
 * @date 2022/3/8 8:12
 */
public class ExcelColNameCalculator extends CommonSample {

    private final Notifications notificationBuilder = FxNotifications.defaultNotify();
    private TextField colNameField;
    private NumberTextField offsetField;
    private TextField resultField;
    private final Action generate = FxAction.generate(actionEvent -> {
        List<String> colNameList = StrUtil.splitTrim(colNameField.getText(), ",");
        int offset = Integer.parseInt(offsetField.getText());
        StringJoiner sj = new StringJoiner(", ");
        for (int i = 0; i < colNameList.size(); i++) {
            int index = ExcelUtil.colNameToIndex(colNameList.get(i));
            sj.add(ExcelUtil.indexToColName(index + offset));
        }
        resultField.setText(sj.toString());
        notificationBuilder.text(I18nUtils.get("common.button.generate.success"));
        notificationBuilder.showInformation();
    });

    private final Collection<? extends Action> actions = List.of(generate);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);

        Label colNameLabel = new Label(I18nUtils.get("common.tool.excelColNameCalculator.label.colName") + ": ");
        colNameField = TextFields.createClearableTextField();
        colNameField.setPromptText(I18nUtils.get("common.textfield.promptText.list"));

        Label offsetLabel = new Label(I18nUtils.get("common.tool.excelColNameCalculator.label.offset") + ": ");
        offsetField = new NumberTextField();
        offsetField.setNumber(new BigDecimal(1));

        Label resultLabel = new Label(I18nUtils.get("common.tool.excelColNameCalculator.label.result") + ": ");
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
    public void initializeBindings() {
        super.initializeBindings();
        BooleanBinding emptyValidation = new MultiTextInputControlEmptyBinding(colNameField, offsetField).build();
        generate.disabledProperty().bind(emptyValidation);
    }

    @Override
    public String getSampleId() {
        return "excelColNameCalculator";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("common.tool.excelColNameCalculator.sampleName");
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
        return I18nUtils.get("common.tool.excelColNameCalculator.sampleDesc");
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/jfxcommon/static/icon/calculator.png"));
    }
}
