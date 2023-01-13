package com.tlcsdm.core.javafx.controlsfx;

import java.util.function.Consumer;

import org.controlsfx.control.action.Action;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.I18nUtils;

import javafx.event.ActionEvent;

/**
 * controlsfx Action的初始化封装
 * 
 * @author unknowIfGuestInDream
 */
public class FxAction {

	public static Action generate(Consumer<ActionEvent> eventHandler) {
		return generate(I18nUtils.get("core.button.generate"), eventHandler);
	}

	public static Action generate(String text, Consumer<ActionEvent> eventHandler) {
		Action action = new Action(text, eventHandler);
		action.setGraphic(
				LayoutHelper.iconView(FxAction.class.getResource("/com/tlcsdm/core/static/icon/generate.png")));
		return action;
	}

	public static Action download(Consumer<ActionEvent> eventHandler) {
		return download(I18nUtils.get("core.button.download"), eventHandler);
	}

	public static Action download(String text, Consumer<ActionEvent> eventHandler) {
		Action action = new Action(text, eventHandler);
		action.setGraphic(
				LayoutHelper.iconView(FxAction.class.getResource("/com/tlcsdm/core/static/icon/download.png")));
		return action;
	}

	public static Action convert(Consumer<ActionEvent> eventHandler) {
		return convert(I18nUtils.get("core.button.convert"), eventHandler);
	}

	public static Action convert(String text, Consumer<ActionEvent> eventHandler) {
		Action action = new Action(text, eventHandler);
		action.setGraphic(
				LayoutHelper.iconView(FxAction.class.getResource("/com/tlcsdm/core/static/icon/convert.png")));
		return action;
	}

	public static Action copy(Consumer<ActionEvent> eventHandler) {
		return convert(I18nUtils.get("core.button.copy"), eventHandler);
	}

	public static Action copy(String text, Consumer<ActionEvent> eventHandler) {
		Action action = new Action(text, eventHandler);
		action.setGraphic(LayoutHelper.iconView(FxAction.class.getResource("/com/tlcsdm/core/static/icon/copy.png")));
		return action;
	}

	public static Action reset(Consumer<ActionEvent> eventHandler) {
		return convert(I18nUtils.get("core.button.reset"), eventHandler);
	}

	public static Action reset(String text, Consumer<ActionEvent> eventHandler) {
		Action action = new Action(text, eventHandler);
		action.setGraphic(LayoutHelper.iconView(FxAction.class.getResource("/com/tlcsdm/core/static/icon/reset.png")));
		return action;
	}
}
