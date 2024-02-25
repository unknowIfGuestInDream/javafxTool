/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.jfxcommon.tools.escape;

import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.core.javafx.util.TooltipUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author unknowIfGuestInDream
 */
public abstract class AbstractEscape extends CommonSample {

    private TextArea originalField;
    private TextArea resultField;
    protected TextArea tipArea;

    private final Action escape = FxAction.create(I18nUtils.get("common.tool.escape.button.escape"),
        actionEvent -> resultField.setText(escape(originalField.getText())),
        LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/jfxcommon/static/icon/encode.png")));

    private final Action unescape = FxAction.create(I18nUtils.get("common.tool.escape.button.unescape"),
        actionEvent -> resultField.setText(unescape(originalField.getText())),
        LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/jfxcommon/static/icon/decode.png")));

    private final Action copyResult = FxAction.copyResult(actionEvent -> {
        if (resultField.getText().isEmpty()) {
            return;
        }
        OSUtil.writeToClipboard(resultField.getText());
        TooltipUtil.showToast(I18nUtils.get("common.button.copyResult.success"));
    });

    private final Collection<? extends Action> actions = List.of(escape, unescape, copyResult);

    @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(12));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);

        Label originalLabel = new Label(I18nUtils.get("common.tool.escape.label.original"));
        originalField = new TextArea();

        Label resultLabel = new Label(I18nUtils.get("common.tool.escape.label.result"));
        resultField = new TextArea();
        resultField.setEditable(false);

        Label tipLabel = new Label(I18nUtils.get("common.tool.escape.label.tip"));
        tipArea = new TextArea("");
        tipArea.setEditable(false);

        grid.add(toolBar, 0, 0, 2, 1);
        grid.add(originalLabel, 0, 1, 2, 1);
        grid.add(originalField, 0, 2, 2, 1);
        grid.add(resultLabel, 0, 3, 2, 1);
        grid.add(resultField, 0, 4, 2, 1);
        grid.add(tipLabel, 0, 5, 2, 1);
        grid.add(tipArea, 0, 6, 2, 1);

        return grid;
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        copyResult.disabledProperty().bind(resultField.textProperty().isEmpty());
    }

    @Override
    public String getOrderKey() {
        return getSampleId();
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    protected abstract String escape(String original);

    protected abstract String unescape(String original);

}
