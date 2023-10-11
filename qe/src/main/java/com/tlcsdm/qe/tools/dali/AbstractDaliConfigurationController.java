package com.tlcsdm.qe.tools.dali;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

/**
 * @author unknowIfGuestInDream
 */
public abstract class AbstractDaliConfigurationController {

    /**
     * 设置输入格式为整数
     */
    protected void setIntFormatter(TextInputControl... textInputControls) {
        for (TextInputControl textInputControl : textInputControls) {
            TextFormatter<String> intFormatter = new TextFormatter<>(change -> {
                String text = change.getText();
                if (text.matches("[0-9]*")) {
                    return change;
                }
                return null;
            });
            textInputControl.setTextFormatter(intFormatter);
        }

    }
}
