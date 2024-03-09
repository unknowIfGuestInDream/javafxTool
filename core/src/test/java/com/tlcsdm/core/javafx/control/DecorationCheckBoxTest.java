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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Gerrit Grunwald
 * Description: 组合组件程序demo
 */
public class DecorationCheckBoxTest extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        DecorationCheckBox check1 = new DecorationCheckBox();
        check1.setText("test");

        DecorationCheckBox check2 = new DecorationCheckBox();
        check2.setText("");

        Button btnError = new Button("Error");
        btnError.setOnAction(e -> {
            check1.setDecoration(Severity.ERROR, "Error!");
            check2.setDecoration(Severity.ERROR, "Error!");
        });
        Button btnWarn = new Button("Warning");
        btnWarn.setOnAction(e -> {
            check1.setDecoration(Severity.WARNING, "Warning!");
            check2.setDecoration(Severity.WARNING, "Warning!");
        });
        Button btnInfo = new Button("Info");
        btnInfo.setOnAction(e -> {
            check1.setDecoration(Severity.INFO, "Info!");
            check2.setDecoration(Severity.INFO, "Info!");
        });
        Button btnOK = new Button("Ok");
        btnOK.setOnAction(e -> {
            check1.setDecoration(Severity.OK);
            check2.setDecoration(Severity.OK);
        });
        //Scene and containers.
        VBox vBox = new VBox(check1, check2, btnError, btnWarn, btnInfo, btnOK);
        Scene scene = new Scene(vBox, 320, 240);
        stage.setTitle("DecorationCheckBox!");
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
