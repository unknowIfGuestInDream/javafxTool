package com.tlcsdm.core.javafx.controller;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.view.SystemSettingView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 设置页面
 *
 * @author xufeng
 */
public class SystemSettingController extends SystemSettingView {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
    }

    private void initView() {
        try {
            exitShowAlertCheckBox.setSelected(Config.getBoolean(Config.Keys.ConfirmExit, true));
            saveStageBoundCheckBox.setSelected(Config.getBoolean(Config.Keys.RememberWindowLocation, true));
        } catch (Exception e) {
            StaticLog.error("Init setting failed: ", e);
        }
    }

    public void applySettings() {
        try {
            Config.set(Config.Keys.ConfirmExit, exitShowAlertCheckBox.isSelected());
            Config.set(Config.Keys.RememberWindowLocation, saveStageBoundCheckBox.isSelected());
        } catch (Exception e) {
            StaticLog.error("Save setting failed: ", e);
        }
    }
}
