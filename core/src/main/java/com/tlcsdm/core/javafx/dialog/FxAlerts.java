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

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.FxApp;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Optional;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/11/27 0:09
 */
public class FxAlerts {

    private FxAlerts() {
    }

    public static void info(String title, String message) {
        alert(AlertType.INFORMATION, title, message);
    }

    public static void warn(String title, String message) {
        alert(AlertType.WARNING, title, message);
    }

    public static void error(String message) {
        alert(AlertType.ERROR, "error", message);
    }

    public static void error(String title, String message) {
        alert(AlertType.ERROR, title, message);
    }

    public static void error(Window owner, String title, String message) {
        alert(owner, AlertType.ERROR, title, message);
    }

    public static void error(String title, Throwable throwable) {
        boolean noMessage = StringUtils.isBlank(throwable.getMessage());
        String message = noMessage ? throwable.toString() : throwable.getMessage();
        error(title, message, ExceptionUtils.getStackTrace(throwable));
    }

    public static void error(Window owner, String title, Throwable throwable) {
        boolean noMessage = StringUtils.isBlank(throwable.getMessage());
        String message = noMessage ? throwable.toString() : throwable.getMessage();
        error(owner, title, message, ExceptionUtils.getStackTrace(throwable));
    }

    public static void alert(AlertType alertType, String title, String message) {
        alert(null, alertType, title, message);
    }

    public static void alert(Window owner, AlertType alertType, String title, String message) {
        FxApp.runLater(() -> {
            try {
                Alert alert = new Alert(alertType, message, FxButtonType.OK);
                alert.setTitle(title);
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                FxApp.setupIcon(stage);
                if (owner != null) {
                    stage.initOwner(owner);
                } else {
                    FxApp.setupModality(alert);
                }

                alert.showAndWait();
            } catch (Exception var6) {
                var6.printStackTrace();
            }

        });
    }

    public static void error(String title, String message, String details) {
        FxApp.runLater(() -> error0(null, title, message, details));
    }

    public static void error(Window owner, String title, String message, String details) {
        FxApp.runLater(() -> error0(owner, title, message, details));
    }

    private static void error0(Window owner, String title, String message, String details) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message.trim());
        TextArea textArea = new TextArea(details);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);
        alert.getDialogPane().setExpandableContent(expContent);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        FxApp.setupIcon(stage);
        if (owner != null) {
            stage.initOwner(owner);
        } else {
            FxApp.setupModality(alert);
        }

        alert.showAndWait();
    }

    public static boolean confirmOkCancel(String title, String message) {
        return confirm(AlertType.CONFIRMATION, title, message, FxButtonType.OK, FxButtonType.CANCEL) == FxButtonType.OK;
    }

    public static boolean confirmYesNo(String title, String message) {
        return confirm(AlertType.CONFIRMATION, title, message, FxButtonType.YES, FxButtonType.NO) == FxButtonType.YES;
    }

    public static ButtonType confirmYesNoCancel(String title, String message) {
        return confirm(AlertType.WARNING, title, message, FxButtonType.YES, FxButtonType.NO, FxButtonType.CANCEL);
    }

    public static ButtonType confirm(AlertType alertType, String title, String message, ButtonType... buttonTypes) {
        try {
            Alert alert = new Alert(alertType, message, buttonTypes);
            alert.setTitle(title);
            alert.setHeaderText(null);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            FxApp.setupIcon(stage);
            FxApp.setupModality(alert);
            Optional<ButtonType> result = alert.showAndWait();
            return result.orElse(FxButtonType.CANCEL);
        } catch (Exception var7) {
            var7.printStackTrace();
            return FxButtonType.CANCEL;
        }
    }

    public static void exception(Throwable exception) {
        ExceptionDialog exceptionDialog = new ExceptionDialog(exception);
        exceptionDialog.show();
    }

    public void delayClose(Dialog<?> dlg) {
        delayClose(dlg, 5000);
    }

    public void delayClose(Dialog<?> dlg, long millis) {
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                StaticLog.error(e);
            }
            Platform.runLater(dlg::close);
        }).start();
    }
}
