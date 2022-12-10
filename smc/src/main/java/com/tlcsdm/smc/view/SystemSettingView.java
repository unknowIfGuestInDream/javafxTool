package com.tlcsdm.smc.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

/**
 * 设置页面
 *
 * @author xufeng
 */

public abstract class SystemSettingView implements Initializable {

    @FXML
    protected CheckBox exitShowAlertCheckBox;

    @FXML
    protected CheckBox saveStageBoundCheckBox;

    @FXML
    protected Button saveButton;

    @FXML
    protected Button cancelButton;

    public CheckBox getExitShowAlertCheckBox() {
        return exitShowAlertCheckBox;
    }

    public void setExitShowAlertCheckBox(CheckBox exitShowAlertCheckBox) {
        this.exitShowAlertCheckBox = exitShowAlertCheckBox;
    }

    public CheckBox getSaveStageBoundCheckBox() {
        return saveStageBoundCheckBox;
    }

    public void setSaveStageBoundCheckBox(CheckBox saveStageBoundCheckBox) {
        this.saveStageBoundCheckBox = saveStageBoundCheckBox;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }
}
