package com.tlcsdm.core.javafx.control;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.I18nUtils;

import javafx.scene.control.Button;

/**
 * 封装一些常用的button的初始化
 * 
 * @author: unknowIfGuestInDream
 */
public class FxButton {

	public static Button choose() {
		Button button = new Button(I18nUtils.get("core.button.choose"));
		return button;
	}

	public static Button chooseWithGrapgic() {
		Button button = new Button(I18nUtils.get("core.button.choose"),
				LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/choose.png")));
		return button;
	}

	public static Button clear() {
		Button button = new Button(I18nUtils.get("core.button.clear"));
		return button;
	}

	public static Button clearWithGrapgic() {
		Button button = new Button(I18nUtils.get("core.button.clear"),
				LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/clear.png")));
		return button;
	}

	public static Button reset() {
		Button button = new Button(I18nUtils.get("core.button.reset"));
		return button;
	}

	public static Button resetWithGrapgic() {
		Button button = new Button(I18nUtils.get("core.button.reset"),
				LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/reset.png")));
		return button;
	}

	public static Button copy() {
		Button button = new Button(I18nUtils.get("core.button.copy"));
		return button;
	}

	public static Button copyWithGrapgic() {
		Button button = new Button(I18nUtils.get("core.button.copy"),
				LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/copy.png")));
		return button;
	}

}
