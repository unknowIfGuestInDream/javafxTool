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

package com.tlcsdm.smc.provider;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.controlsfx.FxActionGroup;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.dialog.FxDialog;
import com.tlcsdm.core.javafx.dialog.LogConsoleDialog;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.ConfigureUtil;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.frame.service.MenubarConfigration;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.controller.CheckTreeViewBuilderFactory;
import com.tlcsdm.smc.controller.SampleTreeController;
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
import org.controlsfx.control.action.ActionCheck;
import org.controlsfx.control.action.ActionUtils;

import java.util.*;

import static org.controlsfx.control.action.ActionUtils.ACTION_SEPARATOR;

public class SmcMenubarConfigrationProvider implements MenubarConfigration {

    private final Stage stage = FXSampler.getStage();

    private final Action export = FxAction.restart(actionEvent -> Platform.runLater(() -> {
        FxDialog<SampleTreeController> dialog = new FxDialog<SampleTreeController>()
                .setResourceBundle(ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale)).setTitle("title")
                .setBodyFxml(SmcMenubarConfigrationProvider.class.getResource("/com/tlcsdm/smc/fxml/sampleTree.fxml"))
                .setBuilderFactory(new CheckTreeViewBuilderFactory())
                .setOwner(FxApp.primaryStage).setButtonTypes(ButtonType.OK, ButtonType.CANCEL);

        SampleTreeController controller = dialog.show();

        dialog.setButtonHandler(ButtonType.OK, (ae, stage) -> {
            //controller.applySettings();
            stage.close();
        }).setButtonHandler(ButtonType.CANCEL, (ae, stage) -> stage.close());
    }));

    private final Action restart = FxAction.restart(actionEvent -> Platform.runLater(() -> {
        stage.close();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new FXSampler().start(new Stage());
    }));

    private final Action logConsole = FxAction.logConsole(actionEvent -> LogConsoleDialog.addLogConsole());

    private final Action exit = FxAction.exit(actionEvent -> FXSampler.doExit());

    private final Action systemSetting = FxAction.systemSetting();

    private final Action contactSupport = FxAction
            .contactSupport(actionEvent -> CoreUtil.openWeb(SmcConstant.GITHUB_PROJECT_SUPPORT_URL));

    private final Action submitFeedback = FxAction
            .submitFeedback(actionEvent -> CoreUtil.openWeb(SmcConstant.GITHUB_PROJECT_FEEDBACK_URL));

    private final Action openLogDir = FxAction.openLogDir(actionEvent -> JavaFxSystemUtil.openDirectory("logs/smc/"));

    private final Action openSysConfig = FxAction.openSysConfig(
            actionEvent -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.CONFIG_FILE_NAME)));

    private final Action openUserData = FxAction.openUserData(
            actionEvent -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.USERDATA_FILE_NAME)));

    private final Action about = FxAction.about(actionEvent -> {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getDialogPane().setStyle("-fx-min-width: 480; -fx-min-height: 360;");
        alert.setResizable(false);
        alert.setTitle(I18nUtils.get("smc.menubar.help.about.title") + " " + FxApp.title);
        alert.setHeaderText(FxApp.title);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        ImageView imageView = LayoutHelper.iconView(FxApp.appIcon, 80);
        alert.setGraphic(imageView);
        ButtonType closeButton = new ButtonType(I18nUtils.get("smc.menubar.help.about.button.close"),
                ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().addAll(closeButton);
        Map<String, String> map = new HashMap<>(32);
        map.put("versionLabel", I18nUtils.get("smc.menubar.help.about.contentText.version"));
        map.put("version", SmcSample.PROJECT_INFO.getVersion());
        map.put("dateLabel", I18nUtils.get("smc.menubar.help.about.contentText.date"));
        map.put("date", SmcSample.PROJECT_INFO.getDate());
        map.put("licenseNameLabel", I18nUtils.get("smc.menubar.help.about.contentText.licenseName"));
        map.put("licenseName", SmcConstant.PROJECT_LICENSE_NAME);
        map.put("licenseUrlLabel", I18nUtils.get("smc.menubar.help.about.contentText.licenseUrl"));
        map.put("licenseUrl", SmcConstant.PROJECT_LICENSE_URL);
        map.put("authorLabel", I18nUtils.get("smc.menubar.help.about.contentText.author"));
        map.put("author", SmcConstant.PROJECT_AUTHOR);
        map.put("projectUrlLabel", I18nUtils.get("smc.menubar.help.about.contentText.projectUrl"));
        map.put("projectUrl", SmcConstant.GITHUB_PROJECT_URL);
        map.put("technicalSupport", I18nUtils.get("smc.menubar.help.about.contentText.technicalSupport"));
        map.put("openSourceSoftware", I18nUtils.get("smc.menubar.help.about.contentText.openSourceSoftware"));
        map.put("copyright", SmcConstant.PROJECT_COPYRIGHT);
        String context = """
                {versionLabel}: {version}
                {dateLabel}: {date}
                {licenseNameLabel}: {licenseName}
                {licenseUrlLabel}: {licenseUrl}

                {authorLabel}: {author}
                {projectUrlLabel}: {projectUrl}

                {technicalSupport}: [{openSourceSoftware}]
                {copyright}
                """;
        alert.setContentText(StrUtil.format(context, map));
        alert.show();
        alert.resultProperty().addListener(o -> {
            if (closeButton.equals(alert.getResult())) {
                alert.close();
            }
        });
    });

    private final Action release = FxAction.release(actionEvent -> CoreUtil.openWeb(SmcConstant.PROJECT_RELEASE_URL));

    CheckLangAction chinese = new CheckLangAction(SmcConstant.LANGUAGE_CHINESE);
    CheckLangAction english = new CheckLangAction(SmcConstant.LANGUAGE_ENGLISH);
    CheckLangAction japanese = new CheckLangAction(SmcConstant.LANGUAGE_JAPANESE);

    private final Collection<? extends Action> actions = List.of(FxActionGroup.file(export, ACTION_SEPARATOR, restart, exit),
            FxActionGroup.setting(systemSetting, FxActionGroup.language(chinese, english, japanese)),
            FxActionGroup.tool(logConsole), FxActionGroup.help(openSysConfig, openLogDir, openUserData,
                    ACTION_SEPARATOR, contactSupport, submitFeedback, ACTION_SEPARATOR, release, about));

    /**
     * 初始化action
     */
    private void initActions() {
        // 语言设置
        if (Config.defaultLocale.equals(Locale.ENGLISH)) {
            english.setSelected(true);
        } else if (Config.defaultLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
            chinese.setSelected(true);
        } else if (Config.defaultLocale.equals(Locale.JAPANESE)) {
            japanese.setSelected(true);
        }
        chinese.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                return;
            }
            if (newValue) {
                english.setSelected(false);
                japanese.setSelected(false);
            }
        });
        english.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                return;
            }
            if (newValue) {
                japanese.setSelected(false);
                chinese.setSelected(false);
            }
        });
        japanese.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                return;
            }
            if (newValue) {
                english.setSelected(false);
                chinese.setSelected(false);
            }
        });
    }

    @Override
    public MenuBar setMenuBar(MenuBar menuBar) {
        initActions();
        ActionUtils.updateMenuBar(menuBar, actions);
        return menuBar;
    }

    @ActionCheck
    private static class CheckLangAction extends Action {

        public CheckLangAction(String name) {
            super(name);
            init();
        }

        private void init() {
            setEventHandler(ae -> {
                String languageType = getText();
                if (SmcConstant.LANGUAGE_CHINESE.equals(languageType)) {
                    if (Config.defaultLocale == Locale.SIMPLIFIED_CHINESE) {
                        return;
                    }
                    Config.set(Config.Keys.Locale, Locale.SIMPLIFIED_CHINESE);
                } else if (SmcConstant.LANGUAGE_ENGLISH.equals(languageType)) {
                    if (Config.defaultLocale == Locale.ENGLISH) {
                        return;
                    }
                    Config.set(Config.Keys.Locale, Locale.ENGLISH);
                } else if (SmcConstant.LANGUAGE_JAPANESE.equals(languageType)) {
                    if (Config.defaultLocale == Locale.JAPANESE) {
                        return;
                    }
                    Config.set(Config.Keys.Locale, Locale.JAPANESE);
                }
                if (FxAlerts.confirmOkCancel(I18nUtils.get("smc.menubar.setting.language.dialog.title"),
                        I18nUtils.get("smc.menubar.setting.language.dialog.message"))) {
                    FXSampler.getStage().close();
                    Platform.runLater(() -> {
                        try {
                            new FXSampler().start(new Stage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
    }

}
