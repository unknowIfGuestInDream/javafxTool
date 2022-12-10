package com.tlcsdm.core.javafx.view;

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
}
