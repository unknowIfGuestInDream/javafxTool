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

import com.tlcsdm.core.exception.CoreException;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/12/10 20:49
 */
public class FxDialog<T> {
    private boolean modal = true;
    private boolean resizable = false;
    private double prefWidth;
    private double prefHeight;
    private boolean closeable = true;
    private Window owner;
    private URL bodyFxmlPath;
    private Parent body;
    private String title;
    private ButtonType[] buttonTypes;
    private final Map<ButtonType, BiConsumer<ActionEvent, Stage>> buttonHandlers = new HashMap<>();
    private Consumer<Stage> withStage;
    private ResourceBundle resourceBundle;

    public FxDialog<T> setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public FxDialog<T> setPrefSize(double prefWidth, double prefHeight) {
        this.prefWidth = prefWidth;
        this.prefHeight = prefHeight;
        return this;
    }

    public FxDialog<T> setPrefHeight(double prefHeight) {
        this.prefHeight = prefHeight;
        return this;
    }

    public FxDialog<T> setPrefWidth(double prefWidth) {
        this.prefWidth = prefWidth;
        return this;
    }

    public FxDialog<T> setTitle(String title) {
        this.title = title;
        return this;
    }

    public FxDialog<T> setOwner(Window owner) {
        this.owner = owner;
        return this;
    }

    public FxDialog<T> setBody(Parent body) {
        this.body = body;
        return this;
    }

    public FxDialog<T> setBodyFxml(URL bodyFxmlPath) {
        this.bodyFxmlPath = bodyFxmlPath;
        return this;
    }

    public FxDialog<T> setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        return this;
    }

    public FxDialog<T> setButtonTypes(ButtonType... buttonTypes) {
        this.buttonTypes = buttonTypes;
        return this;
    }

    public FxDialog<T> setModal(boolean modal) {
        this.modal = modal;
        return this;
    }

    public FxDialog<T> setCloseable(boolean closeable) {
        this.closeable = closeable;
        return this;
    }

    public FxDialog<T> withStage(Consumer<Stage> withStage) {
        this.withStage = withStage;
        return this;
    }

    public FxDialog<T> setButtonHandler(ButtonType buttonType, BiConsumer<ActionEvent, Stage> buttonHandler) {
        this.buttonHandlers.put(buttonType, buttonHandler);
        return this;
    }

    public T show() {
        if (this.bodyFxmlPath != null) {
            FXMLLoader fxmlLoader = this.resourceBundle == null ? FxmlUtil.loadFxmlFromResource(this.bodyFxmlPath)
                : FxmlUtil.loadFxmlFromResource(this.bodyFxmlPath, this.resourceBundle);
            Stage stage = this.createStage(fxmlLoader.getRoot());
            stage.show();
            return fxmlLoader.getController();
        } else if (this.body != null) {
            Stage stage = this.createStage(this.body);
            stage.show();
            return null;
        } else {
            throw new CoreException();
        }
    }

    public T showAndWait() {
        if (this.bodyFxmlPath != null) {
            FXMLLoader fxmlLoader = this.resourceBundle == null ? FxmlUtil.loadFxmlFromResource(this.bodyFxmlPath)
                : FxmlUtil.loadFxmlFromResource(this.bodyFxmlPath, this.resourceBundle);
            Stage stage = this.createStage(fxmlLoader.getRoot());
            stage.showAndWait();
            return fxmlLoader.getController();
        } else if (this.body != null) {
            Stage stage = this.createStage(this.body);
            stage.showAndWait();
            return null;
        } else {
            throw new CoreException();
        }
    }

    private Stage createStage(Parent content) {
        VBox dialogContainer = new VBox(content);
        VBox.setVgrow(content, Priority.ALWAYS);
        dialogContainer.setPadding(new Insets(5.0D));
        dialogContainer.setSpacing(5.0D);
        Stage stage = new Stage();
        if (ArrayUtils.isNotEmpty(this.buttonTypes)) {
            dialogContainer.getChildren().add(new Separator());
            dialogContainer.getChildren().add(this.buttonsPanel(stage));
        }

        stage.setTitle(this.title);
        stage.setScene(new Scene(dialogContainer));
        stage.setResizable(this.resizable);
        if (FxApp.appIcon != null) {
            stage.getIcons().add(FxApp.appIcon);
        }

        if (this.modal) {
            if (this.owner != null) {
                stage.initOwner(this.owner);
                stage.initModality(Modality.WINDOW_MODAL);
                this.adjustPosition(stage, this.owner);
            } else {
                stage.initModality(Modality.APPLICATION_MODAL);
            }
        }

        if (this.prefWidth > 0.0D) {
            stage.setWidth(this.prefWidth);
        }

        if (this.prefHeight > 0.0D) {
            stage.setHeight(this.prefHeight);
        }

        if (!this.closeable) {
            stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, Event::consume);
        }

        if (this.withStage != null) {
            this.withStage.accept(stage);
        }

        return stage;
    }

    private ButtonBar buttonsPanel(Stage stage) {
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(Stream.of(this.buttonTypes).map((buttonType) -> {
            return this.createButton(buttonType, stage);
        }).collect(Collectors.toList()));
        return buttonBar;
    }

    private Button createButton(ButtonType buttonType, Stage stage) {
        Button button = new Button(buttonType.getText());
        ButtonBar.ButtonData buttonData = buttonType.getButtonData();
        ButtonBar.setButtonData(button, buttonData);
        button.setDefaultButton(buttonData.isDefaultButton());
        button.setCancelButton(buttonData.isCancelButton());
        button.setOnAction((event) -> {
            BiConsumer<ActionEvent, Stage> handler = this.buttonHandlers.get(buttonType);
            if (handler != null) {
                handler.accept(event, stage);
            }

        });
        return button;
    }

    private void adjustPosition(Window dialog, Window owner) {
        dialog.addEventHandler(WindowEvent.WINDOW_SHOWN, (event) -> {
            dialog.setX(Math.max(0.0D, owner.getX() + owner.getWidth() / 2.0D - dialog.getWidth() / 2.0D));
            dialog.setY(Math.max(0.0D, owner.getY() + owner.getHeight() / 2.0D - dialog.getHeight() / 2.0D));
        });
    }
}
