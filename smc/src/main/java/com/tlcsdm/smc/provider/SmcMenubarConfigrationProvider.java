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

import static org.controlsfx.control.action.ActionUtils.ACTION_SEPARATOR;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.controlsfx.FxActionGroup;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.dialog.FxDialog;
import com.tlcsdm.core.javafx.dialog.LogConsoleDialog;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.richtext.PropertiesArea;
import com.tlcsdm.core.javafx.richtext.XmlEditorArea;
import com.tlcsdm.core.javafx.richtext.hyperlink.TextHyperlinkArea;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.ConfigureUtil;
import com.tlcsdm.core.javafx.util.FxXmlHelper;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.core.util.DependencyInfo;
import com.tlcsdm.core.util.DependencyInfo.Dependency;
import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.frame.service.MenubarConfigration;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import com.tlcsdm.smc.util.SmcConstant;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionCheck;
import org.controlsfx.control.action.ActionUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class SmcMenubarConfigrationProvider implements MenubarConfigration {

    private final Stage stage = FXSampler.getStage();

    private final Action restart = FxAction.restart(actionEvent -> FXSampler.restart());

    private final Action export = FxAction.export(actionEvent -> {
        FxXmlHelper.exportData(SmcConstant.PROJECT_NAME);
    });

    private final Action induct = FxAction.induct(actionEvent -> {
        FxXmlHelper.importData(SmcConstant.PROJECT_NAME);
        FXSampler.restart();
    });

    private final Action logConsole = FxAction.logConsole(actionEvent -> LogConsoleDialog.addLogConsole());

    private final Action exit = FxAction.exit(actionEvent -> FXSampler.doExit());

    private final Action systemSetting = FxAction.systemSetting();

    private final Action pathWatch = FxAction.pathWatch();

    private final Action contactSupport = FxAction
        .contactSupport(actionEvent -> CoreUtil.openWeb(SmcConstant.GITHUB_PROJECT_SUPPORT_URL));

    private final Action submitFeedback = FxAction
        .submitFeedback(actionEvent -> CoreUtil.openWeb(SmcConstant.GITHUB_PROJECT_FEEDBACK_URL));

    private final Action openLogDir = FxAction.openLogDir(actionEvent -> JavaFxSystemUtil.openDirectory("logs/smc/"));

    private final Action openSysConfig = FxAction.openSysConfig(actionEvent -> {
        VBox vbox = new VBox();
        Button button = FxButton.openWithSystemWithGrapgic();
        button
            .setOnAction(ae -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.CONFIG_FILE_NAME)));
        PropertiesArea area = new PropertiesArea();
        area.setEditable(false);
        area.appendText(
            FileUtil.readUtf8String(FileUtil.file(ConfigureUtil.getConfigurePath(Config.CONFIG_FILE_NAME))));
        VirtualizedScrollPane<PropertiesArea> pane = new VirtualizedScrollPane<>(area);
        vbox.getChildren().addAll(button, pane);
        VBox.setVgrow(pane, Priority.ALWAYS);
        FxDialog<VBox> dialog = new FxDialog<VBox>()
            .setTitle(com.tlcsdm.core.util.I18nUtils.get("core.menubar.help.openSysConfigDir"))
            .setOwner(FxApp.primaryStage).setPrefSize(800, 600).setResizable(true).setBody(vbox)
            .setButtonTypes(ButtonType.CLOSE);
        dialog.setButtonHandler(ButtonType.CLOSE, (e, s) -> s.close());
        dialog.show();

    });

    private final Action openUserData = FxAction.openUserData(actionEvent -> {
        VBox vbox = new VBox();
        Button button = FxButton.openWithSystemWithGrapgic();
        button.setOnAction(
            ae -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.USERDATA_FILE_NAME)));
        XmlEditorArea area = new XmlEditorArea();
        area.setEditable(false);
        area.appendText(
            FileUtil.readUtf8String(FileUtil.file(ConfigureUtil.getConfigurePath(Config.USERDATA_FILE_NAME))));
        VirtualizedScrollPane<XmlEditorArea> pane = new VirtualizedScrollPane<>(area);
        vbox.getChildren().addAll(button, pane);
        VBox.setVgrow(pane, Priority.ALWAYS);
        FxDialog<VBox> dialog = new FxDialog<VBox>()
            .setTitle(com.tlcsdm.core.util.I18nUtils.get("core.menubar.help.openUserData")).setOwner(FxApp.primaryStage)
            .setPrefSize(1000, 800).setResizable(true).setBody(vbox).setButtonTypes(ButtonType.CLOSE);
        dialog.setButtonHandler(ButtonType.CLOSE, (e, s) -> s.close());
        dialog.show();
    });

    private final Action about = FxAction.about(actionEvent -> {
        Consumer<String> showLink = (string) -> {
            if ("openSourceSoftware".equals(string)) {
                List<Dependency> dependencyList = DependencyInfo.getDependencyList();
                List<String> smcDependencyList = List.of("poi", "freemarker", "dom4j", "java-diff-utils", "richtextfx");
                List<Dependency> list = dependencyList.stream()
                    .filter(d -> d.inUsed() || smcDependencyList.contains(d.artifact())).toList();
                // {@link com.tlcsdm.demo.samples.tableview2.HelloFilteredTableView }
            } else {
                CoreUtil.openWeb(string);
            }
        };
        VBox vbox = new VBox();
        ImageView imageView = LayoutHelper.iconView(FxApp.appIcon, 80);
        TextHyperlinkArea area = new TextHyperlinkArea(showLink);
        area.setEditable(false);
        area.setStyle("-fx-font-size: 14;-fx-padding: 10 0 0 0;");
        area.appendText(I18nUtils.get("smc.menubar.help.about.contentText.version") + ": "
            + SmcSample.PROJECT_INFO.getVersion() + "\n");
        area.appendText(
            I18nUtils.get("smc.menubar.help.about.contentText.date") + ": " + SmcSample.PROJECT_INFO.getDate() + "\n");
        area.appendText(I18nUtils.get("smc.menubar.help.about.contentText.licenseName") + ": "
            + SmcConstant.PROJECT_LICENSE_NAME + "\n");
        area.appendText(I18nUtils.get("smc.menubar.help.about.contentText.licenseUrl") + ": ");
        area.appendWithLink(SmcConstant.PROJECT_LICENSE_URL, SmcConstant.PROJECT_LICENSE_URL);
        area.appendText("\n");
        area.appendText("\n");
        area.appendText(
            I18nUtils.get("smc.menubar.help.about.contentText.author") + ": " + SmcConstant.PROJECT_AUTHOR + "\n");
        area.appendText(I18nUtils.get("smc.menubar.help.about.contentText.projectUrl") + ": ");
        area.appendWithLink(SmcConstant.GITHUB_PROJECT_URL, SmcConstant.GITHUB_PROJECT_URL);
        area.appendText("\n");
        area.appendText("\n");
        area.appendText(I18nUtils.get("smc.menubar.help.about.contentText.technicalSupport") + ": [");
        area.appendWithLink(I18nUtils.get("smc.menubar.help.about.contentText.openSourceSoftware"),
            "openSourceSoftware");
        area.appendText("]\n");
        area.appendText(SmcConstant.PROJECT_COPYRIGHT);
        vbox.getChildren().addAll(imageView, area);
        VBox.setVgrow(area, Priority.ALWAYS);

        FxDialog<VBox> dialog = new FxDialog<VBox>()
            .setTitle(I18nUtils.get("smc.menubar.help.about.title") + " " + FxApp.title).setOwner(FxApp.primaryStage)
            .setPrefSize(480, 360).setBody(vbox).setButtonTypes(ButtonType.CLOSE);
        dialog.setButtonHandler(ButtonType.CLOSE, (e, s) -> s.close());
        dialog.show();
    });

    private final Action release = FxAction.release(actionEvent -> CoreUtil.openWeb(SmcConstant.PROJECT_RELEASE_URL));

    CheckLangAction chinese = new CheckLangAction(SmcConstant.LANGUAGE_CHINESE);
    CheckLangAction english = new CheckLangAction(SmcConstant.LANGUAGE_ENGLISH);
    CheckLangAction japanese = new CheckLangAction(SmcConstant.LANGUAGE_JAPANESE);

    private final Collection<? extends Action> actions = List.of(
        FxActionGroup.file(export, induct, ACTION_SEPARATOR, restart, exit),
        FxActionGroup.setting(systemSetting, FxActionGroup.language(chinese, english, japanese)),
        FxActionGroup.tool(logConsole, pathWatch), FxActionGroup.help(openSysConfig, openLogDir, openUserData,
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
                            StaticLog.error(e);
                        }
                    });
                }
            });
        }
    }

}
