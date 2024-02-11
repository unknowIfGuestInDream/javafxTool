/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Description: 组合组件程序demo
 */
public class DecorationTitlePaneTest extends Application {

    @Override
    public void init() {
    }

    @Override
    public void start(final Stage stage) {
        DecorationTitlePane pane = new DecorationTitlePane();
        HBox hBox = new HBox();
        DecorationTextfield control = new DecorationTextfield();
        control.setLayoutX(80);
        control.setLayoutY(60);
        Button btnError = new Button("Error");
        btnError.setOnAction(e -> {
            pane.setDecoration(Severity.ERROR, "Error!");
        });
        Button btnWarn = new Button("Warning");
        btnWarn.setOnAction(e -> {
            pane.setDecoration(Severity.WARNING, "Warning!");
        });
        Button btnInfo = new Button("Info");
        btnInfo.setOnAction(e -> {
            pane.setDecoration(Severity.INFO, "Info!");
        });
        Button btnOK = new Button("Ok");
        btnOK.setOnAction(e -> {
            pane.setDecoration(Severity.OK);
        });
        hBox.getChildren().addAll(control, btnError, btnWarn, btnInfo, btnOK);
        pane.setContent(hBox);
        pane.setPadding(new Insets(40));
        Scene scene = new Scene(pane);
        stage.setTitle("DecorationTextfield");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
