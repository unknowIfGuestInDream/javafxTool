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

package com.tlcsdm.core.javafx.controlsfx;

import cn.hutool.core.io.FileUtil;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.controller.PreferencesView;
import com.tlcsdm.core.javafx.dialog.ExceptionDialog;
import com.tlcsdm.core.javafx.dialog.FxButtonType;
import com.tlcsdm.core.javafx.dialog.FxDialog;
import com.tlcsdm.core.javafx.dialog.PathWatchToolDialog;
import com.tlcsdm.core.javafx.dialog.SystemSettingDialog;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.richtext.PropertiesArea;
import com.tlcsdm.core.javafx.richtext.XmlEditorArea;
import com.tlcsdm.core.javafx.stage.PdfViewStage;
import com.tlcsdm.core.javafx.stage.ScreenColorPickerStage;
import com.tlcsdm.core.javafx.stage.ScreenshotStage;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.ConfigureUtil;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.javafx.util.Keys;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.core.util.I18nUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.action.Action;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.util.function.Consumer;

/**
 * controlsfx Action的初始化封装
 *
 * @author unknowIfGuestInDream
 */
public class FxAction {

    private FxAction() {
    }

    public static Action create(String text, Consumer<ActionEvent> eventHandler, String url) {
        return create(text, eventHandler, LayoutHelper.iconView(FxAction.class.getResource(url)));
    }

    /**
     * create action.
     */
    public static Action create(String text, Consumer<ActionEvent> eventHandler, Node graphic) {
        Action action = new Action(text, actionEvent -> {
            try {
                eventHandler.accept(actionEvent);
            } catch (Exception e) {
                ExceptionDialog exceptionDialog = new ExceptionDialog(e);
                exceptionDialog.show();
            }
        });
        action.setGraphic(graphic);
        return action;
    }

    public static Action generate(Consumer<ActionEvent> eventHandler) {
        return generate(I18nUtils.get("core.button.generate"), eventHandler);
    }

    public static Action generate(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/generate.png");
    }

    public static Action download(Consumer<ActionEvent> eventHandler) {
        return download(I18nUtils.get("core.button.download"), eventHandler);
    }

    public static Action download(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/download.png");
    }

    public static Action convert(Consumer<ActionEvent> eventHandler) {
        return convert(I18nUtils.get("core.button.convert"), eventHandler);
    }

    public static Action convert(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/convert.png");
    }

    public static Action copy(Consumer<ActionEvent> eventHandler) {
        return copy(I18nUtils.get("core.button.copy"), eventHandler);
    }

    public static Action copy(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/copy.png");
    }

    public static Action reset(Consumer<ActionEvent> eventHandler) {
        return reset(I18nUtils.get("core.button.reset"), eventHandler);
    }

    public static Action reset(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/reset.png");
    }

    public static Action restart(Consumer<ActionEvent> eventHandler) {
        return restart(I18nUtils.get("core.menubar.file.restart"), eventHandler);
    }

    /**
     * menubar restart
     */
    public static Action restart(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/restart.png");
    }

    public static Action exit(Consumer<ActionEvent> eventHandler) {
        return exit(I18nUtils.get("core.menubar.file.exit"), eventHandler);
    }

    /**
     * menubar exit
     */
    public static Action exit(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/exit.png");
    }

    /**
     * menubar systemSetting
     */
    public static Action systemSetting(Keys... excludeKeys) {
        return create(I18nUtils.get("core.menubar.setting.systemSetting"), actionEvent -> {
            SystemSettingDialog.openSystemSettings(I18nUtils.get("core.menubar.setting.systemSetting"), excludeKeys);
        }, "/com/tlcsdm/core/static/menubar/system.png");
    }

    /**
     * menubar pathWatch
     */
    public static Action pathWatch() {
        return create(I18nUtils.get("core.menubar.setting.pathWatch"), actionEvent -> {
            PathWatchToolDialog.openPathWatchTool(I18nUtils.get("core.menubar.setting.pathWatch"));
        }, "/com/tlcsdm/core/static/menubar/monitor.png");
    }

    public static Action colorPicker() {
        return create(I18nUtils.get("core.menubar.setting.colorPicker"), actionEvent -> {
            new ScreenColorPickerStage().showStage();
        }, "/com/tlcsdm/core/static/menubar/colorPicker.png");
    }

    public static Action screenshot() {
        return create(I18nUtils.get("core.menubar.setting.screenshot"), actionEvent -> {
            new ScreenshotStage().showStage();
        }, "/com/tlcsdm/core/static/menubar/screenshot.png");
    }

    public static Action pdf(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/pdf.png");
    }

    public static Action pdf(Consumer<ActionEvent> eventHandler) {
        return pdf(I18nUtils.get("core.button.pdf"), eventHandler);
    }

    public static Action pdf() {
        return pdf(actionEvent -> {
            new PdfViewStage().showStage();
        });
    }

    public static Action about(Consumer<ActionEvent> eventHandler) {
        return about(I18nUtils.get("core.menubar.help.about"), eventHandler);
    }

    /**
     * menubar about
     */
    public static Action about(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/about.png");
    }

    public static Action contactSupport(Consumer<ActionEvent> eventHandler) {
        return contactSupport(I18nUtils.get("core.menubar.help.contactSupport"), eventHandler);
    }

    public static Action contactSupport(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/support.png");
    }

    public static Action submitFeedback(Consumer<ActionEvent> eventHandler) {
        return submitFeedback(I18nUtils.get("core.menubar.help.submitFeedback"), eventHandler);
    }

    public static Action submitFeedback(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/feedback.png");
    }

    public static Action openLogDir(Consumer<ActionEvent> eventHandler) {
        return openDir(I18nUtils.get("core.menubar.help.openLogDir"), eventHandler);
    }

    public static Action openOutDir(Consumer<ActionEvent> eventHandler) {
        return openDir(I18nUtils.get("core.menubar.help.openOutDir"), eventHandler);
    }

    public static Action openDir(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/folder.png");
    }

    public static Action api() {
        return create("JavaFX", actionEvent -> {
            CoreUtil.openWeb(CoreConstant.JAVAFX_API_URL);
        }, "/com/tlcsdm/core/static/menubar/jfx.png");
    }

    public static Action cssApi() {
        return create("CSS", actionEvent -> {
            CoreUtil.openWeb(CoreConstant.JAVAFX_API_CSS_URL);
        }, "/com/tlcsdm/core/static/menubar/css.png");
    }

    public static Action fxmlApi() {
        return create("FXML", actionEvent -> {
            CoreUtil.openWeb(CoreConstant.JAVAFX_API_FXML_URL);
        }, "/com/tlcsdm/core/static/menubar/fxml.png");
    }

    public static Action openSysConfig(Consumer<ActionEvent> eventHandler) {
        return openSysConfig(I18nUtils.get("core.menubar.help.openSysConfigDir"), eventHandler);
    }

    public static Action openSysConfig() {
        return openSysConfig(actionEvent -> {
            VBox vbox = new VBox();
            Button button = FxButton.openWithSystemWithGrapgic();
            button.setOnAction(
                ae -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.CONFIG_FILE_NAME)));
            PropertiesArea area = new PropertiesArea();
            area.setEditable(false);
            area.appendText(
                FileUtil.readUtf8String(FileUtil.file(ConfigureUtil.getConfigurePath(Config.CONFIG_FILE_NAME))));
            VirtualizedScrollPane<PropertiesArea> pane = new VirtualizedScrollPane<>(area);
            vbox.getChildren().addAll(button, pane);
            VBox.setVgrow(pane, Priority.ALWAYS);
            FxDialog<VBox> dialog = new FxDialog<VBox>().setTitle(I18nUtils.get("core.menubar.help.openSysConfigDir"))
                .setOwner(FxApp.primaryStage).setPrefSize(800, 600).setResizable(true).setBody(vbox)
                .setButtonTypes(FxButtonType.CLOSE);
            dialog.setButtonHandler(FxButtonType.CLOSE, (e, s) -> s.close());
            dialog.show();
        });
    }

    public static Action openSysConfig(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/sysConfig.png");
    }

    public static Action openUserData(Consumer<ActionEvent> eventHandler) {
        return openUserData(I18nUtils.get("core.menubar.help.openUserData"), eventHandler);
    }

    public static Action openUserData() {
        return openUserData(actionEvent -> {
            VBox vbox = new VBox();
            Button button = FxButton.openWithSystemWithGrapgic();
            button.setOnAction(
                ae -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.USERDATA_FILE_NAME)));
            XmlEditorArea area = new XmlEditorArea();
            area.setEditable(false);
            area.appendText(
                FileUtil.readUtf8String(FileUtil.file(ConfigureUtil.getConfigurePath(Config.USERDATA_FILE_NAME))));
            area.showParagraphAtTop(0);
            VirtualizedScrollPane<XmlEditorArea> pane = new VirtualizedScrollPane<>(area);
            vbox.getChildren().addAll(button, pane);
            VBox.setVgrow(pane, Priority.ALWAYS);
            FxDialog<VBox> dialog = new FxDialog<VBox>().setTitle(I18nUtils.get("core.menubar.help.openUserData"))
                .setOwner(FxApp.primaryStage).setPrefSize(1000, 800).setResizable(true).setBody(vbox)
                .setButtonTypes(FxButtonType.CLOSE);
            dialog.setButtonHandler(FxButtonType.CLOSE, (e, s) -> s.close());
            dialog.show();
        });
    }

    public static Action openUserData(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/userData.png");
    }

    public static Action release(Consumer<ActionEvent> eventHandler) {
        return release(I18nUtils.get("core.menubar.help.release"), eventHandler);
    }

    public static Action release(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/release.png");
    }

    public static Action helpContent(Consumer<ActionEvent> eventHandler) {
        return helpContent(I18nUtils.get("core.menubar.help.helpContent"), eventHandler);
    }

    public static Action helpContent(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/document.png");
    }

    public static Action clear(Consumer<ActionEvent> eventHandler) {
        return clear(I18nUtils.get("core.button.clear"), eventHandler);
    }

    public static Action clear(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/clear.png");
    }

    public static Action logConsole(Consumer<ActionEvent> eventHandler) {
        return logConsole(I18nUtils.get("core.button.logConsole"), eventHandler);
    }

    public static Action logConsole(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/console.png");
    }

    public static Action export(Consumer<ActionEvent> eventHandler) {
        return export(I18nUtils.get("core.button.export"), eventHandler);
    }

    public static Action export(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/export.png");
    }

    public static Action induct(Consumer<ActionEvent> eventHandler) {
        return induct(I18nUtils.get("core.button.import"), eventHandler);
    }

    public static Action induct(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/import.png");
    }

    public static Action view(Consumer<ActionEvent> eventHandler) {
        return view(I18nUtils.get("core.button.view"), eventHandler);
    }

    public static Action view(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/view.png");
    }

    public static Action preferences(Consumer<ActionEvent> eventHandler) {
        return preferences(I18nUtils.get("core.button.preferences"), eventHandler);
    }

    public static Action preferences(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/preferences.png");
    }

    public static Action preferences(Keys... excludeKeys) {
        return preferences(actionEvent -> new PreferencesView(excludeKeys).show());
    }

    public static Action choose(Consumer<ActionEvent> eventHandler) {
        return choose(I18nUtils.get("core.button.choose"), eventHandler);
    }

    public static Action choose(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/icon/choose.png");
    }
}
