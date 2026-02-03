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

package com.tlcsdm.core.javafx.dialog;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.controller.SystemSettingController;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.Keys;
import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.control.ButtonType;

import java.util.ResourceBundle;

/**
 * 系统设置弹窗.
 *
 * @author unknowIfGuestInDream
 */
public class SystemSettingDialog {

    private SystemSettingDialog() {
    }

    /**
     * 打开系统设置弹窗.
     *
     * @param title       标题
     * @param excludeKeys 排除的配置项
     */
    public static void openSystemSettings(String title, Keys... excludeKeys) {
        FxDialog<SystemSettingController> dialog = new FxDialog<SystemSettingController>()
            .setResourceBundle(ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale)).setTitle(title)
            .setBodyFxml(SystemSettingDialog.class.getResource("/com/tlcsdm/core/fxml/SystemSetting.fxml"))
            .setOwner(FxApp.primaryStage).setButtonTypes(ButtonType.OK, ButtonType.CANCEL);

        SystemSettingController controller = dialog.show();
        controller.disableKeys(excludeKeys);
        dialog.setButtonHandler(ButtonType.OK, (actionEvent, stage) -> {
            controller.applySettings();
            stage.close();
        }).setButtonHandler(ButtonType.CANCEL, (actionEvent, stage) -> stage.close());
    }

}
