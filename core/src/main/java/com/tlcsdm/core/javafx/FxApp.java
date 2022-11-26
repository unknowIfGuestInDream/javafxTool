package com.tlcsdm.core.javafx;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import javafx.application.Platform;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 唐 亮
 * @date: 2022/11/26 23:58
 */
public class FxApp {
    public static Stage primaryStage;
    public static Image appIcon;
    public static final List<String> styleSheets = new ArrayList();

    public FxApp() {
    }

    public static void init(Stage primaryStage, String iconPath) {
        FxApp.primaryStage = primaryStage;
        JavaFxSystemUtil.mainStage = primaryStage;
        if (StringUtils.isNotBlank(iconPath)) {
            appIcon = LayoutHelper.icon(iconPath);
            primaryStage.getIcons().add(appIcon);
        }

    }

    public static void setupIcon(Stage stage) {
        if (appIcon != null) {
            stage.getIcons().clear();
            stage.getIcons().add(appIcon);
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