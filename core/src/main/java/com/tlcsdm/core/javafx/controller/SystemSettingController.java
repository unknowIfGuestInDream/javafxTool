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

import com.tlcsdm.core.event.ConfigRefreshEvent;
import com.tlcsdm.core.eventbus.EventBus;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.Keys;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.core.javafx.view.AbstractSystemSettingView;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 设置页面.
 *
 * @author unknowIfGuestInDream
 */
public class SystemSettingController extends AbstractSystemSettingView {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
    }

    private void initView() {
        exitShowAlertCheckBox.setSelected(Config.getBoolean(Keys.ConfirmExit, true));
        saveStageBoundCheckBox.setSelected(Config.getBoolean(Keys.RememberWindowLocation, true));
        checkForUpdatesAtStartupCheckBox.setSelected(Config.getBoolean(Keys.CheckForUpdatesAtStartup, true));
        screenshotHideWindowCheckBox.setSelected(Config.getBoolean(Keys.ScreenshotHideWindow, true));
        screenColorPickerHideWindowCheckBox.setSelected(Config.getBoolean(Keys.ScreenColorPickerHideWindow, true));
        useDevModeCheckBox.setSelected(Config.getBoolean(Keys.UseDevMode, false));
        useEasterEggCheckBox.setSelected(Config.getBoolean(Keys.UseEasterEgg, true));
        useSkipBootAnimation.setSelected(Config.getBoolean(Keys.SkipBootAnimation, false));
    }

    /**
     * 禁用配置项.
     *
     * @param excludeKeys Keys
     */
    public void disableKeys(Keys... excludeKeys) {
        if (OSUtil.getOS().equals(OSUtil.OS.MAC)) {
            disableNode(useEasterEggCheckBox);
            disableNode(screenshotHideWindowCheckBox);
            disableNode(screenColorPickerHideWindowCheckBox);
        }
        for (Keys key : excludeKeys) {
            switch (key) {
                case ConfirmExit -> disableNode(exitShowAlertCheckBox);
                case RememberWindowLocation -> disableNode(saveStageBoundCheckBox);
                case CheckForUpdatesAtStartup -> disableNode(checkForUpdatesAtStartupCheckBox);
                case ScreenshotHideWindow -> disableNode(screenshotHideWindowCheckBox);
                case ScreenColorPickerHideWindow -> disableNode(screenColorPickerHideWindowCheckBox);
                case UseDevMode -> disableNode(useDevModeCheckBox);
                case UseEasterEgg -> disableNode(useEasterEggCheckBox);
                case SkipBootAnimation -> disableNode(useSkipBootAnimation);
                default -> {
                    // Do nothing
                }
            }
        }
    }

    private void disableNode(Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(false);
            node.setManaged(false);
        }
    }

    /**
     * 执行保存.
     * 将UI值设置到系统配置中
     */
    public void applySettings() {
        Config.set(Keys.ConfirmExit, exitShowAlertCheckBox.isSelected());
        Config.set(Keys.RememberWindowLocation, saveStageBoundCheckBox.isSelected());
        Config.set(Keys.CheckForUpdatesAtStartup, checkForUpdatesAtStartupCheckBox.isSelected());
        Config.set(Keys.ScreenshotHideWindow, screenshotHideWindowCheckBox.isSelected());
        Config.set(Keys.ScreenColorPickerHideWindow, screenColorPickerHideWindowCheckBox.isSelected());
        Config.set(Keys.UseDevMode, useDevModeCheckBox.isSelected());
        Config.set(Keys.UseEasterEgg, useEasterEggCheckBox.isSelected());
        Config.set(Keys.SkipBootAnimation, useSkipBootAnimation.isSelected());
        EventBus.getDefault().post(new ConfigRefreshEvent());
    }
}
