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

package com.tlcsdm.core.javafx.controller;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.Keys;
import com.tlcsdm.core.javafx.view.SystemSettingView;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 设置页面
 *
 * @author unknowIfGuestInDream
 */
public class SystemSettingController extends SystemSettingView {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
    }

    private void initView() {
        try {
            exitShowAlertCheckBox.setSelected(Config.getBoolean(Keys.ConfirmExit, true));
            saveStageBoundCheckBox.setSelected(Config.getBoolean(Keys.RememberWindowLocation, true));
            checkForUpdatesAtStartupCheckBox.setSelected(Config.getBoolean(Keys.CheckForUpdatesAtStartup, true));
            screenshotHideWindowCheckBox.setSelected(Config.getBoolean(Keys.ScreenshotHideWindow, true));
            screenColorPickerHideWindowCheckBox.setSelected(Config.getBoolean(Keys.ScreenColorPickerHideWindow, true));
        } catch (Exception e) {
            StaticLog.error("Init setting failed: ", e);
        }
    }

    public void disableKeys(Keys... excludeKeys) {
        for (Keys key : excludeKeys) {
            switch (key) {
                case ConfirmExit -> disableNode(exitShowAlertCheckBox);
                case RememberWindowLocation -> disableNode(saveStageBoundCheckBox);
                case CheckForUpdatesAtStartup -> disableNode(checkForUpdatesAtStartupCheckBox);
                case ScreenshotHideWindow -> disableNode(screenshotHideWindowCheckBox);
                case ScreenColorPickerHideWindow -> disableNode(screenColorPickerHideWindowCheckBox);
            }
        }
    }

    private void disableNode(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    public void applySettings() {
        try {
            Config.set(Keys.ConfirmExit, exitShowAlertCheckBox.isSelected());
            Config.set(Keys.RememberWindowLocation, saveStageBoundCheckBox.isSelected());
            Config.set(Keys.CheckForUpdatesAtStartup, checkForUpdatesAtStartupCheckBox.isSelected());
            Config.set(Keys.ScreenshotHideWindow, screenshotHideWindowCheckBox.isSelected());
            Config.set(Keys.ScreenColorPickerHideWindow, screenColorPickerHideWindowCheckBox.isSelected());
        } catch (Exception e) {
            StaticLog.error("Save setting failed: ", e);
        }
    }
}
