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

package com.tlcsdm.core.javafx;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author unknowIfGuestInDream
 * @date 2022/11/26 23:58
 */
public class FxApp {
    public static Stage primaryStage;
    public static Image appIcon;
    public static String title;
    public static HostServices hostServices;

    private FxApp() {
    }

    public static void init(Stage primaryStage, URL resource, HostServices hostServices) {
        FxApp.primaryStage = primaryStage;
        if (resource != null) {
            appIcon = LayoutHelper.icon(resource);
            primaryStage.getIcons().add(appIcon);
        }
        FxApp.hostServices = hostServices;
    }

    public static void setupIcon(Stage stage) {
        if (appIcon != null) {
            stage.getIcons().clear();
            stage.getIcons().add(appIcon);
        }
    }

    public static void setupIcon() {
        setupIcon(primaryStage);
    }

    public static void setTitle(String t) {
        title = t;
    }

    public static void setAppIcon(Image image) {
        if (image != null) {
            appIcon = image;
            setupIcon();
        }
    }

    public static void setupModality(Stage stage) {
        if (stage != null && primaryStage != null && primaryStage.isShowing()) {
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
        }

    }

    public static void setupModality(Dialog<?> stage) {
        if (stage != null && primaryStage != null && primaryStage.isShowing()) {
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
        }

    }

    public static void runLater(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }

    }
}
