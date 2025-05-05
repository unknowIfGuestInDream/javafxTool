/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.dlsc.preferencesfx.util.VisibilityProperty;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.Keys;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.core.util.I18nUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.StackPane;

import java.util.ResourceBundle;

/**
 * Preferences视图.
 *
 * @author unknowIfGuestInDream
 */
public class PreferencesView extends StackPane {

    private final PreferencesFx preferencesFx;

    BooleanProperty exitShowAlert = new SimpleBooleanProperty(true);
    BooleanProperty saveStageBound = new SimpleBooleanProperty(true);
    BooleanProperty checkForUpdatesAtStartup = new SimpleBooleanProperty(true);
    BooleanProperty screenshotHideWindow = new SimpleBooleanProperty(true);
    BooleanProperty screenColorPickerHideWindow = new SimpleBooleanProperty(true);
    BooleanProperty useDevMode = new SimpleBooleanProperty(false);
    BooleanProperty useEasterEgg = new SimpleBooleanProperty(true);
    BooleanProperty skipBootAnimation = new SimpleBooleanProperty(false);

    // VisibilityProperty
    BooleanProperty supExitShowAlert = new SimpleBooleanProperty(true);
    BooleanProperty supSaveStageBound = new SimpleBooleanProperty(true);
    BooleanProperty supCheckForUpdatesAtStartup = new SimpleBooleanProperty(true);
    BooleanProperty supScreenshotHideWindow = new SimpleBooleanProperty(true);
    BooleanProperty supScreenColorPickerHideWindow = new SimpleBooleanProperty(true);
    BooleanProperty supUseDevMode = new SimpleBooleanProperty(true);
    BooleanProperty supUseEasterEgg = new SimpleBooleanProperty(true);
    BooleanProperty supSkipBootAnimation = new SimpleBooleanProperty(true);

    /**
     * 资源初始化.
     *
     * @param excludeKeys 排除的配置项
     */
    public PreferencesView(Keys... excludeKeys) {
        initVisibilityProperty(excludeKeys);
        preferencesFx = createPreferences();
    }

    /**
     * 显示视图.
     */
    public void show() {
        preferencesFx.show();
    }

    private void initVisibilityProperty(Keys... excludeKeys) {
        if (OSUtil.getOS().equals(OSUtil.OS.MAC)) {
            supUseEasterEgg.setValue(false);
            supScreenshotHideWindow.setValue(false);
            supScreenColorPickerHideWindow.setValue(false);
        }

        for (Keys key : excludeKeys) {
            switch (key) {
                case ConfirmExit -> supExitShowAlert.setValue(false);
                case RememberWindowLocation -> supSaveStageBound.setValue(false);
                case CheckForUpdatesAtStartup -> supCheckForUpdatesAtStartup.setValue(false);
                case ScreenshotHideWindow -> supScreenshotHideWindow.setValue(false);
                case ScreenColorPickerHideWindow -> supScreenColorPickerHideWindow.setValue(false);
                case UseDevMode -> supUseDevMode.setValue(false);
                case UseEasterEgg -> supUseEasterEgg.setValue(false);
                case SkipBootAnimation -> supSkipBootAnimation.setValue(false);
                default -> {
                    // Do nothing
                }
            }
        }
    }

    private PreferencesFx createPreferences() {
        // i18n
        ResourceBundle rb = ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale);
        ResourceBundleService rbs = new ResourceBundleService(rb);
        return PreferencesFx.of(new CoreStorageHandler(),
                Category.of("core.preference.general").expand().subCategories(
                    Category.of("core.menubar.setting.systemSetting",
                        VisibilityProperty.of(supExitShowAlert.or(supSaveStageBound).or(supCheckForUpdatesAtStartup)
                            .or(supUseDevMode).or(supUseEasterEgg)),
                        Group.of(Setting.of("core.dialog.systemSetting.check.confirmExit", exitShowAlert,
                                    VisibilityProperty.of(supExitShowAlert)),
                                Setting.of("core.dialog.systemSetting.check.rememberWindowLocation", saveStageBound,
                                    VisibilityProperty.of(supSaveStageBound)),
                                Setting.of("core.dialog.systemSetting.check.checkForUpdatesAtStartup", checkForUpdatesAtStartup,
                                    VisibilityProperty.of(supCheckForUpdatesAtStartup)),
                                Setting.of("core.dialog.systemSetting.check.useDevMode", useDevMode,
                                    VisibilityProperty.of(supUseDevMode)),
                                Setting.of("core.dialog.systemSetting.check.useEasterEgg", useEasterEgg,
                                    VisibilityProperty.of(supUseEasterEgg)),
                                Setting.of("core.dialog.systemSetting.check.skipBootAnimation", skipBootAnimation,
                                    VisibilityProperty.of(supSkipBootAnimation)))
                            .description("core.menubar.setting.systemSetting")),
                    Category.of("core.menubar.tool",
                        VisibilityProperty.of(supScreenshotHideWindow.or(supScreenColorPickerHideWindow)),
                        Group.of(VisibilityProperty.of(supScreenshotHideWindow),
                                Setting.of("core.dialog.systemSetting.check.screenshotHideWindow", screenshotHideWindow,
                                    VisibilityProperty.of(supScreenshotHideWindow)))
                            .description("core.menubar.setting.screenshot"),
                        Group.of(VisibilityProperty.of(supScreenColorPickerHideWindow),
                                Setting.of("core.dialog.systemSetting.check.screenColorPickerHideWindow",
                                    screenColorPickerHideWindow,
                                    VisibilityProperty.of(supScreenColorPickerHideWindow)))
                            .description("core.menubar.setting.colorPicker")))).i18n(rbs).persistWindowState(true)
            .saveSettings(true).debugHistoryMode(false).buttonsVisibility(true).
            instantPersistent(false).dialogTitle(I18nUtils.get("core.button.preferences")).dialogIcon(FxApp.appIcon);
    }
}
