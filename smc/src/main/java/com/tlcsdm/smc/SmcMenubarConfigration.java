package com.tlcsdm.smc;

import com.tlcsdm.frame.MenubarConfigration;
import com.tlcsdm.smc.util.I18nUtils;
import com.tlcsdm.smc.util.SmcConstant;
import javafx.scene.control.MenuBar;
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

    /**
     * @todo 完善关于菜单功能
     * @body Dialogs 实现版本号， 包含项目名,版本号,技术支持 超链接是个table software  + License，Copyright © 2022 unknowIfGuestInDream
     */
    private final Action about = new Action(I18nUtils.get("smc.menubar.help.about"), actionEvent -> {
        // Do nothing
    });

    private final Collection<? extends Action> actions = List.of(
            new ActionGroup(I18nUtils.get("smc.menubar.setting")),
            new ActionGroup(I18nUtils.get("smc.menubar.help"), contactSupport, submitFeedback, ACTION_SEPARATOR, about));

    /**
     * 初始化action
     */
    private void initActions() {
        // Do nothing
    }

    @Override
    public MenuBar setMenuBar(MenuBar menuBar) {
        initActions();
        ActionUtils.updateMenuBar(menuBar, actions);
        return menuBar;
    }

}
