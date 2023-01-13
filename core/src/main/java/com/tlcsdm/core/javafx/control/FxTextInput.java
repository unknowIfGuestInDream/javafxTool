package com.tlcsdm.core.javafx.control;

import javafx.scene.control.TextArea;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/12/5 20:31
 */
public class FxTextInput {

	public static TextArea textArea() {
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.getStyleClass().add("control-textarea");
		textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		textArea.setWrapText(true);
		return textArea;
	}

	public static TextArea textArea(String text) {
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.getStyleClass().add("control-textarea");
		textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		textArea.setWrapText(true);
		textArea.setText(text);
		return textArea;
	}
}
