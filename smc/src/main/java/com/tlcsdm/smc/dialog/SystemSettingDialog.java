package com.tlcsdm.smc.dialog;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxDialog;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.smc.controller.SystemSettingController;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.scene.control.ButtonType;

import java.util.ResourceBundle;

/**
 * @author: 唐 亮
 * @date: 2022/12/10 21:19
 */
public class SystemSettingDialog {

    public static void openSystemSettings(String title) {
        FxDialog<SystemSettingController> dialog = new FxDialog<SystemSettingController>()
                .setResourceBundle(ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale))
                .setTitle(title)
                .setBodyFxml(SystemSettingDialog.class.getResource("/com/tlcsdm/smc/fxml/SystemSetting.fxml"))
                .setOwner(FxApp.primaryStage)
                .setButtonTypes(ButtonType.OK, ButtonType.CANCEL);

        SystemSettingController controller = dialog.show();

        dialog.setButtonHandler(ButtonType.OK, (actionEvent, stage) -> {
            controller.applySettings();
            stage.close();
        }).setButtonHandler(ButtonType.CANCEL, (actionEvent, stage) -> stage.close());
    }

}
