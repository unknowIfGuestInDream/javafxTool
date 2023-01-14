package com.tlcsdm.core.javafx.dialog;

import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public final class FxButtonType {

	public static final ButtonType OK = new ButtonType(I18nUtils.get("core.buttonType.ok"), ButtonData.OK_DONE);

	public static final ButtonType YES = new ButtonType(I18nUtils.get("core.buttonType.yes"), ButtonData.YES);

	public static final ButtonType NO = new ButtonType(I18nUtils.get("core.buttonType.no"), ButtonData.NO);

	public static final ButtonType CANCEL = new ButtonType(I18nUtils.get("core.buttonType.cancel"),
			ButtonData.CANCEL_CLOSE);

}
