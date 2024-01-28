package com.tlcsdm.core.javafx.control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Gerrit Grunwald
 * Description: 组合组件程序demo
 */
public class DemoDecorationTextfield extends Application {
    private DecorationTextfield control;

    @Override
    public void init() {
        control = new DecorationTextfield();
    }

    @Override
    public void start(final Stage stage) {
        Pane pane = new Pane();
        control.setLayoutX(80);
        control.setLayoutY(60);
        pane.getChildren().add(control);
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
