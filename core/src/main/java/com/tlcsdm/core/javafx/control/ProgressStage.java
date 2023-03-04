package com.tlcsdm.core.javafx.control;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 加载组件
 *
 * <p><b>Example:</b></p>
 * <pre><code>
 *         ProgressStage ps=ProgressStage.of();
 *         ps.show();
 *
 *         ThreadPoolTaskExecutor.get().execute(() -> {
 *             ThreadUtil.safeSleep(5000);
 *             ps.close();
 *         });
 * </code></pre>
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/3/4 21:05
 */
public class ProgressStage {
    private Stage stage;

    private ProgressStage() {
    }

    /**
     * 创建
     */
    public static ProgressStage of(Stage parent, String msg) {
        ProgressStage ps = new ProgressStage();
        ps.initUI(parent, msg);
        return ps;
    }

    public static ProgressStage of(String msg) {
        return ProgressStage.of(FxApp.primaryStage, msg);
    }

    public static ProgressStage of() {
        return ProgressStage.of(FxApp.primaryStage, I18nUtils.get("core.control.progressStage.msg"));
    }

    /**
     * 显示
     */
    public void show() {
        stage.show();
    }

    public void close() {
        FxApp.runLater(() -> {
            if (stage.isShowing()) {
                stage.close();
            }
        });
    }

    private void initUI(Stage parent, String msg) {
        stage = new Stage();
        stage.initOwner(parent);
        // style
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);

        // message
        Label adLbl = new Label(msg);
        adLbl.setTextFill(Color.BLUE);

        // progress
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setProgress(-1);

        // pack
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setBackground(Background.EMPTY);
        vBox.getChildren().addAll(indicator, adLbl);

        // scene
        Scene scene = new Scene(vBox);
        scene.setFill(null);
        stage.setScene(scene);
        stage.setWidth(msg.length() * 8 + 10);
        stage.setHeight(100);

        // show center of parent
        double x = parent.getX() + (parent.getWidth() - stage.getWidth()) / 2;
        double y = parent.getY() + (parent.getHeight() - stage.getHeight()) / 2;
        stage.setX(x);
        stage.setY(y);
    }
}
