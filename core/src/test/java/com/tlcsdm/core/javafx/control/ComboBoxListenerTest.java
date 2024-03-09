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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Gerrit Grunwald
 * Description: 组合组件程序demo
 */
public class ComboBoxListenerTest extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        //List for the ComboBox.
        ObservableList<String> fontFamilies = FXCollections.observableArrayList(Font.getFamilies());

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(fontFamilies);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            System.out.println("selectedItemProperty");
        });
        comboBox.setOnDragDropped(e -> {
            System.out.println("setOnDragDropped");
        });
        comboBox.setOnMouseClicked(e -> {
            System.out.println("setOnMouseClicked");
        });
        comboBox.setOnMouseDragged(e -> {
            System.out.println("setOnMouseDragged");
        });
        comboBox.setOnMousePressed(e -> {
            System.out.println("setOnMousePressed");
        });
        comboBox.setOnMouseDragEntered(e -> {
            System.out.println("setOnMouseDragEntered");
        });
        comboBox.setOnMouseDragExited(e -> {
            System.out.println("setOnMouseDragExited");
        });
        comboBox.setOnMouseDragReleased(e -> {
            System.out.println("setOnMouseDragReleased");
        });
        comboBox.setOnMouseEntered(e -> {
            System.out.println("setOnMouseEntered");
        });
        comboBox.setOnTouchMoved(e -> {
            System.out.println("setOnTouchMoved");
        });
        comboBox.setOnAction(e -> {
            System.out.println("setOnAction");
        });
        Button disable = new Button("Disable");
        disable.setOnAction(e -> {
            comboBox.setDisable(!comboBox.isDisable());
        });

        //Scene and containers.
        VBox vBox = new VBox(comboBox, disable);
        Scene scene = new Scene(vBox, 320, 240);
        stage.setTitle("ComboBox");
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
