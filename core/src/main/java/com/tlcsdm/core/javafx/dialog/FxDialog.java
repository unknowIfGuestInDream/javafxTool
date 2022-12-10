package com.tlcsdm.core.javafx.dialog;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: 唐 亮
 * @date: 2022/12/10 20:49
 */
public class FxDialog<T> {
    private boolean modal = true;
    private boolean resizable = false;
    private double prefWidth;
    private double prefHeight;
    private boolean closeable = true;
    private Window owner;
    private ClassLoader bodyFxmlClassLoader;
    private URL bodyFxmlPath;
    private Parent body;
    private String title;
    private ButtonType[] buttonTypes;
    private Map<ButtonType, BiConsumer<ActionEvent, Stage>> buttonHandlers = new HashMap<>();
    private Consumer<Stage> withStage;
    private ResourceBundle resourceBundle;

    public FxDialog() {
    }

    public FxDialog<T> setBodyFxmlClassLoader(ClassLoader bodyFxmlClassLoader) {
        this.bodyFxmlClassLoader = bodyFxmlClassLoader;
        return this;
    }

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

    public FxDialog<T> setBodyFxml(ClassLoader classLoader, URL bodyFxmlPath) {
        this.bodyFxmlClassLoader = classLoader;
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
            FXMLLoader fxmlLoader = this.resourceBundle == null ? FxmlUtil.loadFxmlFromResource(this.bodyFxmlPath) : FxmlUtil.loadFxmlFromResource(this.bodyFxmlPath, this.resourceBundle);
            Stage stage = this.createStage((Parent) fxmlLoader.getRoot());
            stage.show();
            return fxmlLoader.getController();
        } else if (this.body != null) {
            Stage stage = this.createStage(this.body);
            stage.show();
            return null;
        } else {
            throw new RuntimeException("bodyFxmlPath 和 body 不能都为空");
        }
    }

    public T showAndWait() {
        if (this.bodyFxmlPath != null) {
            FXMLLoader fxmlLoader = this.resourceBundle == null ? FxmlUtil.loadFxmlFromResource(this.bodyFxmlPath) : FxmlUtil.loadFxmlFromResource(this.bodyFxmlPath, this.resourceBundle);
            Stage stage = this.createStage((Parent) fxmlLoader.getRoot());
            stage.showAndWait();
            return fxmlLoader.getController();
        } else if (this.body != null) {
            Stage stage = this.createStage(this.body);
            stage.showAndWait();
            return null;
        } else {
            throw new RuntimeException("bodyFxmlPath 和 body 不能都为空");
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
        buttonBar.getButtons().addAll((Collection) Stream.of(this.buttonTypes).map((buttonType) -> {
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
            BiConsumer<ActionEvent, Stage> handler = (BiConsumer) this.buttonHandlers.get(buttonType);
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
