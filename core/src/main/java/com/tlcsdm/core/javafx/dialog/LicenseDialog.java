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
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.core.util.CoreConstant;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;

/**
 * License弹窗
 *
 * @author unknowIfGuestInDream
 */
public class LicenseDialog {

    private LicenseDialog() {
    }

    public static void openLicenseDialog() {
        VBox vbox = new VBox();
        InlineCssTextArea area = new InlineCssTextArea();
        area.setEditable(false);
        area.setStyle("-fx-font-size: 14;-fx-padding: 5 0 0 5;");
        area.appendText(CoreConstant.PROJECT_LICENSE_CONTENT_STRING);
        VirtualizedScrollPane<InlineCssTextArea> pane = new VirtualizedScrollPane<>(area);
        vbox.getChildren().addAll(pane);
        VBox.setVgrow(pane, Priority.ALWAYS);
        FxDialog<VBox> dialog = new FxDialog<VBox>().setTitle("License").setOwner(FxApp.primaryStage)
            .setPrefSize(680, 540).setResizable(true).setBody(vbox)
            .setButtonTypes(FxButtonType.COPY, FxButtonType.CLOSE);
        dialog.setButtonHandler(FxButtonType.COPY, (actionEvent, stage) -> OSUtil.writeToClipboard(area.getText()))
            .setButtonHandler(FxButtonType.CLOSE, (e, s) -> s.close());
        dialog.show();
    }

}
