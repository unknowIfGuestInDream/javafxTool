/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.Node;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionGroup;

import java.util.Collection;

/**
 * controlsfx ActionGroup的初始化封装
 *
 * @author unknowIfGuestInDream
 */
public class FxActionGroup {

    private FxActionGroup() {
    }

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
     * menubar view
     */
    public static ActionGroup view(Action... actions) {
        return create(I18nUtils.get("core.menubar.view"), "/com/tlcsdm/core/static/menubar/view.png", actions);
    }

    /**
     * menubar jdkTool
     */
    public static ActionGroup jdkTool(Action... actions) {
        return create(I18nUtils.get("core.menubar.setting.jdkTool"), "/com/tlcsdm/core/static/icon/java.png",
            actions);
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
