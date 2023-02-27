package com.tlcsdm.core.javafx.controlsfx;

import com.tlcsdm.core.javafx.dialog.SystemSettingDialog;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.util.I18nUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.controlsfx.control.action.Action;

import java.util.function.Consumer;

/**
 * controlsfx Action的初始化封装
 *
 * @author unknowIfGuestInDream
 */
public class FxAction {

    public static Action create(String text, Consumer<ActionEvent> eventHandler, String url) {
        return create(text, eventHandler, LayoutHelper.iconView(FxAction.class.getResource(url)));
    }

    /**
     * create action
     */
    public static Action create(String text, Consumer<ActionEvent> eventHandler, Node graphic) {
        Action action = new Action(text, eventHandler);
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
    public static Action systemSetting() {
        return create(I18nUtils.get("core.menubar.setting.systemSetting"), actionEvent -> {
            SystemSettingDialog.openSystemSettings(I18nUtils.get("core.menubar.setting.systemSetting"));
        }, "/com/tlcsdm/core/static/menubar/system.png");
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

    public static Action openSysConfig(Consumer<ActionEvent> eventHandler) {
        return openSysConfig(I18nUtils.get("core.menubar.help.openSysConfigDir"), eventHandler);
    }

    public static Action openSysConfig(String text, Consumer<ActionEvent> eventHandler) {
        return create(text, eventHandler, "/com/tlcsdm/core/static/menubar/sysConfig.png");
    }

    public static Action openUserData(Consumer<ActionEvent> eventHandler) {
        return openUserData(I18nUtils.get("core.menubar.help.openUserData"), eventHandler);
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
}
