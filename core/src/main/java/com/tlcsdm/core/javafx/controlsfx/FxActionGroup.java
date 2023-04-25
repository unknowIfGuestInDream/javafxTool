package com.tlcsdm.core.javafx.controlsfx;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.Node;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionGroup;

import java.util.Collection;

/**
 * controlsfx ActionGroup的初始化封装
 *
 * @author: unknowIfGuestInDream
 */
public class FxActionGroup {

    public static Action create(String text, Node icon, Collection<Action> actions) {
        return new ActionGroup(text, icon, actions);
    }

    public static ActionGroup create(String text, Node icon, Action... actions) {
        return new ActionGroup(text, icon, actions);
    }

    public static ActionGroup create(String text, Collection<Action> actions) {
        return new ActionGroup(text, actions);
    }

    public static ActionGroup create(String text, Action... actions) {
        return new ActionGroup(text, actions);
    }

    public static ActionGroup create(String text, String url, Action... actions) {
        return new ActionGroup(text, LayoutHelper.iconView(FxActionGroup.class.getResource(url)), actions);
    }

    /**
     * menubar file
     */
    public static ActionGroup file(Action... actions) {
        return create(I18nUtils.get("core.menubar.file"), "/com/tlcsdm/core/static/menubar/file.png", actions);
    }

    /**
     * menubar setting
     */
    public static ActionGroup setting(Action... actions) {
        return create(I18nUtils.get("core.menubar.setting"), "/com/tlcsdm/core/static/menubar/setting.png", actions);
    }

    /**
     * menubar language
     */
    public static ActionGroup language(Action... actions) {
        return create(I18nUtils.get("core.menubar.setting.language"), "/com/tlcsdm/core/static/menubar/language.png",
            actions);
    }

    /**
     * menubar help
     */
    public static ActionGroup tool(Action... actions) {
        return create(I18nUtils.get("core.menubar.tool"), "/com/tlcsdm/core/static/menubar/tool.png", actions);
    }

    /**
     * menubar help
     */
    public static ActionGroup help(Action... actions) {
        return create(I18nUtils.get("core.menubar.help"), "/com/tlcsdm/core/static/menubar/help.png", actions);
    }

}
