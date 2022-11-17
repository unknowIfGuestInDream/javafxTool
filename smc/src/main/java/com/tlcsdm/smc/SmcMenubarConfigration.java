package com.tlcsdm.smc;

import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.frame.MenubarConfigration;
import com.tlcsdm.smc.util.I18nUtils;
import com.tlcsdm.smc.util.SmcConstant;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionGroup;
import org.controlsfx.control.action.ActionUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import static org.controlsfx.control.action.ActionUtils.ACTION_SEPARATOR;

public class SmcMenubarConfigration implements MenubarConfigration {

    private Stage stage;

    private final Action restart = new Action(I18nUtils.get("smc.menubar.file.restart"), actionEvent -> {
        Platform.runLater(() -> {
            stage.close();
            new FXSampler().start(new Stage());
        });
    });

    private final Action exit = new Action(I18nUtils.get("smc.menubar.file.exit"), actionEvent -> {
        Platform.runLater(() -> {
            stage.close();
            Platform.setImplicitExit(false);
            Platform.exit();
            System.exit(0);
        });
    });

    private final Action contactSupport = new Action(I18nUtils.get("smc.menubar.help.contactSupport"), actionEvent -> {
        Desktop d = Desktop.getDesktop();
        try {
            URI address = new URI(SmcConstant.GITHUB_PROJECT_SUPPORT_URL);
            d.browse(address);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    });

    private final Action submitFeedback = new Action(I18nUtils.get("smc.menubar.help.submitFeedback"), actionEvent -> {
        Desktop d = Desktop.getDesktop();
        try {
            URI address = new URI(SmcConstant.GITHUB_PROJECT_FEEDBACK_URL);
            d.browse(address);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    });

    private final Action about = new Action(I18nUtils.get("smc.menubar.help.about"), actionEvent -> {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setResizable(false);
        alert.setTitle(I18nUtils.get("smc.menubar.help.about.title"));
        alert.setHeaderText(I18nUtils.get("smc.menubar.help.about.headerText"));
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        if (stage.getIcons().size() > 0) {
            ImageView imageView = new ImageView(stage.getIcons().get(0));
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            alert.setGraphic(imageView);
        }

        ButtonType closeButton = new ButtonType(I18nUtils.get("smc.menubar.help.about.button.close"), ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().addAll(closeButton);
        String context = """
                Version: 1.0.0
                发布时间: 2022-11-17
                LICENSE名称: MIT
                LICENSE路径: https://www.tlcsdm.com/LICENSE.txt
                                
                作者: unknowIfGuestInDream
                项目地址: https://github.com/unknowIfGuestInDream/javafxTool
                	
                技术支持: [开源软件]
                Copyright © 2022 unknowIfGuestInDream
                """;
        //String.format("xxx   %s")  / formatted
        alert.setContentText(context);
        alert.show();
        alert.resultProperty().addListener(o -> {
            if (closeButton.equals(alert.getResult())) {
                alert.close();
            }
        });
    });

    private final Collection<? extends Action> actions = List.of(new ActionGroup(I18nUtils.get("smc.menubar.file"), restart, exit),
            new ActionGroup(I18nUtils.get("smc.menubar.setting")),
            new ActionGroup(I18nUtils.get("smc.menubar.help"), contactSupport, submitFeedback, ACTION_SEPARATOR,
                    about));

    /**
     * 初始化action
     */
    private void initActions() {
        // Do nothing
    }

    @Override
    public MenuBar setMenuBar(MenuBar menuBar, Stage stage) {
        this.stage = stage;
        initActions();
        ActionUtils.updateMenuBar(menuBar, actions);
        return menuBar;
    }

}
