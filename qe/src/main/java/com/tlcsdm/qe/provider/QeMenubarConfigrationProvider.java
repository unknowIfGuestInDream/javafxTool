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

package com.tlcsdm.qe.provider;

import cn.hutool.core.net.NetUtil;
import com.tlcsdm.core.httpserver.SimpleHttpServer;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.DependencyTableView;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.controlsfx.FxActionGroup;
import com.tlcsdm.core.javafx.controlsfx.FxLanguageActionGroup;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.dialog.FxButtonType;
import com.tlcsdm.core.javafx.dialog.FxDialog;
import com.tlcsdm.core.javafx.dialog.LicenseDialog;
import com.tlcsdm.core.javafx.dialog.LogConsoleDialog;
import com.tlcsdm.core.javafx.factory.KeyCombinationFactory;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.richtext.hyperlink.TextHyperlinkArea;
import com.tlcsdm.core.javafx.util.FxXmlHelper;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.core.util.DependencyInfo;
import com.tlcsdm.core.util.DependencyInfo.Dependency;
import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.frame.service.MenubarConfigration;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import com.tlcsdm.qe.util.QeConstant;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionGroup;
import org.controlsfx.control.action.ActionUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static org.controlsfx.control.action.ActionUtils.ACTION_SEPARATOR;

/**
 * @author unknowIfGuestInDream
 */
public class QeMenubarConfigrationProvider implements MenubarConfigration {

    private final Action restart = FxAction.restart(actionEvent -> FXSampler.restart());

    private final Action export = FxAction.export(actionEvent -> FxXmlHelper.exportData(QeConstant.PROJECT_NAME));

    private final Action induct = FxAction.induct(actionEvent -> {
        if (FxXmlHelper.importData(QeConstant.PROJECT_NAME)) {
            FXSampler.restart();
        }
    });

    private final Action logConsole = FxAction.logConsole(actionEvent -> LogConsoleDialog.addLogConsole());

    private final Action exit = FxAction.exit(actionEvent -> FXSampler.doExit());

    private final Action systemSetting = FxAction.preferences();

    private final Action pathWatch = FxAction.pathWatch();

    private final Action colorPicker = FxAction.colorPicker();

    private final Action screenshot = FxAction.screenshot();

    private final Action contactSupport = FxAction
        .contactSupport(actionEvent -> CoreUtil.openWeb(QeConstant.GITHUB_PROJECT_SUPPORT_URL));

    private final Action submitFeedback = FxAction
        .submitFeedback(actionEvent -> CoreUtil.openWeb(QeConstant.GITHUB_PROJECT_FEEDBACK_URL));

    private final Action openLogDir = FxAction.openLogDir(actionEvent -> JavaFxSystemUtil.openDirectory("logs/qe/"));

    private final Action openSysConfig = FxAction.openSysConfig();

    private final Action openUserData = FxAction.openUserData();

    private final Action openPropertiesDialog = FxAction.openPropertiesDialog();

    private final Action api = FxAction.api();
    private final Action css = FxAction.cssApi();
    private final Action fxml = FxAction.fxmlApi();

    private final Action about = FxAction.about(actionEvent -> {
        Consumer<String> showLink = (string) -> {
            if ("openSourceSoftware".equals(string)) {
                List<Dependency> dependencyList = DependencyInfo.getDependencyList();
                List<Dependency> list = dependencyList.stream()
                    .filter(d -> d.getInUsed() || QeConstant.DEPENDENCY_LIST.contains(d.getArtifact())).toList();
                DependencyTableView tableView = new DependencyTableView(list);
                VBox vbox = new VBox();
                vbox.getChildren().add(tableView);
                VBox.setVgrow(tableView, Priority.ALWAYS);
                FxDialog<VBox> dialog = new FxDialog<VBox>()
                    .setTitle(I18nUtils.get("qe.menubar.help.about.contentText.openSourceSoftware"))
                    .setOwner(FxApp.primaryStage).setPrefSize(800, 600).setResizable(true).setBody(vbox)
                    .setButtonTypes(FxButtonType.CLOSE);
                dialog.setButtonHandler(FxButtonType.CLOSE, (e, s) -> s.close());
                dialog.show();
            } else if ("license".equals(string)) {
                LicenseDialog.openLicenseDialog();
            } else {
                CoreUtil.openWeb(string);
            }
        };
        VBox vbox = new VBox();
        ImageView imageView = LayoutHelper.iconView(FxApp.appIcon, 80);
        TextHyperlinkArea area = new TextHyperlinkArea(showLink);
        area.setEditable(false);
        area.setStyle("-fx-font-size: 14;-fx-padding: 10 0 0 0;");
        area.appendText(I18nUtils.get("qe.menubar.help.about.contentText.version") + ": "
            + QeSample.PROJECT_INFO.getVersion() + "\n");
        area.appendText(
            I18nUtils.get("qe.menubar.help.about.contentText.date") + ": " + QeSample.PROJECT_INFO.getDate() + "\n");
        area.appendText(I18nUtils.get("qe.menubar.help.about.contentText.licenseName") + ": ");
        area.appendWithLink(QeConstant.PROJECT_LICENSE_NAME, "license");
        area.appendText("\n");
        area.appendText(I18nUtils.get("qe.menubar.help.about.contentText.licenseUrl") + ": ");
        area.appendWithLink(QeConstant.PROJECT_LICENSE_URL, QeConstant.PROJECT_LICENSE_URL);
        area.appendText("\n");
        area.appendText("\n");
        area.appendText(
            I18nUtils.get("qe.menubar.help.about.contentText.author") + ": " + QeConstant.PROJECT_AUTHOR + "\n");
        area.appendText(I18nUtils.get("qe.menubar.help.about.contentText.projectUrl") + ": ");
        String displayedUrl = QeConstant.GITHUB_PROJECT_URL.length() < 51 ? QeConstant.GITHUB_PROJECT_URL :
            CoreUtil.getDomainName(QeConstant.GITHUB_PROJECT_URL);
        area.appendWithLink(displayedUrl, QeConstant.GITHUB_PROJECT_URL);
        area.appendText("\n");
        area.appendText("\n");
        area.appendText(I18nUtils.get("qe.menubar.help.about.contentText.technicalSupport") + ": [");
        area.appendWithLink(I18nUtils.get("qe.menubar.help.about.contentText.openSourceSoftware"),
            "openSourceSoftware");
        area.appendText("]\n");
        area.appendText(QeConstant.PROJECT_COPYRIGHT);
        vbox.getChildren().addAll(imageView, area);
        VBox.setVgrow(area, Priority.ALWAYS);

        FxDialog<VBox> dialog = new FxDialog<VBox>()
            .setTitle(I18nUtils.get("qe.menubar.help.about.title") + " " + FxApp.title).setOwner(FxApp.primaryStage)
            .setPrefSize(480, 360).setBody(vbox).setButtonTypes(FxButtonType.CLOSE);
        dialog.setButtonHandler(FxButtonType.CLOSE, (e, s) -> s.close());
        dialog.show();
    });

    private final Action helpContent = FxAction.helpContent(actionEvent -> {
        int port = NetUtil.getUsableLocalPort(8000);
        new SimpleHttpServer(port, "/", CoreUtil.getRootPath() + File.separator + "docs").start();
        CoreUtil.openWeb("http://localhost:" + port);
    });

    private final Action release = FxAction.release(actionEvent -> CoreUtil.openWeb(QeConstant.PROJECT_RELEASE_URL));

    private final ActionGroup languageGroup = new FxLanguageActionGroup((s) -> {
        if (FxAlerts.confirmOkCancel(I18nUtils.get("qe.menubar.setting.language.dialog.title"),
            I18nUtils.get("qe.menubar.setting.language.dialog.message"))) {
            FXSampler.restart();
        }
    }).create();

    private final Collection<? extends Action> actions = List.of(
        FxActionGroup.file(export, induct, ACTION_SEPARATOR, restart, exit),
        FxActionGroup.setting(systemSetting, languageGroup),
        FxActionGroup.tool(logConsole, pathWatch, colorPicker, screenshot),
        FxActionGroup.help(openSysConfig, openLogDir, openUserData, openPropertiesDialog, ACTION_SEPARATOR,
            contactSupport, submitFeedback,
            ACTION_SEPARATOR, api, css, fxml, ACTION_SEPARATOR, helpContent, release, about));

    @Override
    public void setMenuBar(MenuBar menuBar) {
        ActionUtils.updateMenuBar(menuBar, actions);
        systemSetting.setAccelerator(KeyCombinationFactory.SHORTCUT_P);
        logConsole.setAccelerator(KeyCombinationFactory.CTRL_SHIFT_L);
        screenshot.setAccelerator(KeyCombinationFactory.CTRL_SHIFT_S);
        colorPicker.setAccelerator(KeyCombinationFactory.CTRL_SHIFT_C);
    }

}
