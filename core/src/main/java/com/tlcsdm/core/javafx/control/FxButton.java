package com.tlcsdm.core.javafx.control;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.control.Button;

/**
 * 封装一些常用的button的初始化
 * 
 * @author: unknowIfGuestInDream
 */
public class FxButton {

	public static Button choose() {
		return new Button(I18nUtils.get("core.button.choose"));
	}

	public static Button chooseWithGrapgic() {
		return new Button(I18nUtils.get("core.button.choose"),
				LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/choose.png")));
	}

	public static Button clear() {
		return new Button(I18nUtils.get("core.button.clear"));
	}

	public static Button clearWithGrapgic() {
		return new Button(I18nUtils.get("core.button.clear"),
				LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/clear.png")));
	}

	public static Button reset() {
		return new Button(I18nUtils.get("core.button.reset"));
	}

	public static Button resetWithGrapgic() {
		return new Button(I18nUtils.get("core.button.reset"),
				LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/reset.png")));
	}

	public static Button copy() {
		return new Button(I18nUtils.get("core.button.copy"));
	}

	public static Button copyWithGrapgic() {
		return new Button(I18nUtils.get("core.button.copy"),
				LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/copy.png")));
	}

}
