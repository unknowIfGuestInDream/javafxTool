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
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.util.I18nUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.StackPane;

import java.util.ResourceBundle;

/**
 * @author unknowIfGuestInDream
 */
public class PreferencesView extends StackPane {

    public PreferencesFx preferencesFx;

    BooleanProperty exitShowAlert = new SimpleBooleanProperty(true);
    BooleanProperty saveStageBound = new SimpleBooleanProperty(true);
    BooleanProperty checkForUpdatesAtStartup = new SimpleBooleanProperty(true);
    BooleanProperty screenshotHideWindow = new SimpleBooleanProperty(true);

    public PreferencesView() {
        preferencesFx = createPreferences();
    }

    public void show() {
        preferencesFx.show();
    }

    private PreferencesFx createPreferences() {
        // i18n
        ResourceBundle rb = ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale);
        ResourceBundleService rbs = new ResourceBundleService(rb);
        return PreferencesFx
            .of(new CoreStorageHandler(),
                Category.of("core.preference.general").expand().subCategories(
                    Category.of("core.menubar.setting.systemSetting",
                        Group.of(Setting.of("core.dialog.systemSetting.check.confirmExit", exitShowAlert),
                            Setting.of("core.dialog.systemSetting.check.rememberWindowLocation", saveStageBound),
                            Setting.of("core.dialog.systemSetting.check.checkForUpdatesAtStartup",
                                checkForUpdatesAtStartup))
                            .description("core.menubar.setting.systemSetting")),
                    Category.of("core.menubar.tool",
                        Group.of(
                            Setting.of("core.dialog.systemSetting.check.screenshotHideWindow", screenshotHideWindow))
                            .description("core.menubar.setting.screenshot"))))
            .i18n(rbs).persistWindowState(false).saveSettings(true).debugHistoryMode(false).buttonsVisibility(true)
            .instantPersistent(false).dialogTitle(I18nUtils.get("core.button.preferences")).dialogIcon(FxApp.appIcon);
    }
}
