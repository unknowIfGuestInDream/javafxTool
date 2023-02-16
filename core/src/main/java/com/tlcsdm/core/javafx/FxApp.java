package com.tlcsdm.core.javafx;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import javafx.application.Platform;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/11/26 23:58
 */
public class FxApp {
    public static Stage primaryStage;
    public static Image appIcon;
    public static String title;

    public FxApp() {
    }

    public static void init(Stage primaryStage, URL resource) {
        FxApp.primaryStage = primaryStage;
        if (resource != null) {
            appIcon = LayoutHelper.icon(resource);
            primaryStage.getIcons().add(appIcon);
        }
    }

    public static void init(Stage primaryStage) {
        init(primaryStage, null);
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