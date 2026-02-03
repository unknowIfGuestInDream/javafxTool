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

import com.tlcsdm.core.javafx.controller.PathWatchToolController;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.util.I18nUtils;

import java.util.ResourceBundle;

/**
 * 文件夹监控
 *
 * @author unknowIfGuestInDream
 * @date 2022/12/10 21:19
 */
public class PathWatchToolDialog {

    private PathWatchToolDialog() {
    }

    public static void openPathWatchTool(String title) {
        FxDialog<PathWatchToolController> dialog = new FxDialog<PathWatchToolController>()
            .setResourceBundle(ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale)).setTitle(title)
            .setBodyFxml(PathWatchToolDialog.class.getResource("/com/tlcsdm/core/fxml/PathWatchTool.fxml"))
            .setModal(false).setResizable(true).setButtonTypes(FxButtonType.CLOSE);
        dialog.show();
        dialog.setButtonHandler(FxButtonType.CLOSE, (actionEvent, stage) -> stage.close());
    }

}
