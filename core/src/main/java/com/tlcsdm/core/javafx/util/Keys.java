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

package com.tlcsdm.core.javafx.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 系统配置属性类
 *
 * @author unknowIfGuestInDream
 */
public enum Keys {
    /**
     * 系统属性
     */
    MainWindowWidth("mainWindowWidth"),
    MainWindowHeight("mainWindowHeight"),
    MainWindowTop("mainWindowTop"),
    MainWindowLeft("mainWindowLeft"),
    ControlDividerPosition("controlDividerPosition"),
    Locale("locale"),
    RememberWindowLocation("rememberWindowLocation"),
    ConfirmExit("confirmExit"),
    CheckForUpdatesAtStartup("checkForUpdatesAtStartup"),
    ScreenshotHideWindow("screenshotHideWindow"),
    ScreenColorPickerHideWindow("screenColorPickerHideWindow"),
    Theme("theme"),
    UseDevMode("useDevMode");

    private final String keyName;

    Keys(String keyName) {
        this.keyName = keyName;
    }

    public static Keys fromKeyName(String keyName) {
        for (Keys type : Keys.values()) {
            if (StringUtils.isNotEmpty(keyName) && type.getKeyName().equals(keyName)) {
                return type;
            }
        }
        return null;
    }

    public String getKeyName() {
        return this.keyName;
    }
}
