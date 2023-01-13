package com.tlcsdm.core.javafx.helper;

import java.net.URL;

import com.tlcsdm.core.exception.ResourceNotFoundException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/11/27 0:05
 */
public class LayoutHelper {
	public LayoutHelper() {
	}

	public static TextField textField(String text, double prefWidth) {
		TextField textField = new TextField(text);
		if (prefWidth > 0.0D) {
			textField.setPrefWidth(prefWidth);
		}

		return textField;
	}

	public static Label label(String text) {
		return new Label(text);
	}

	public static Label label(String text, double maxWidth) {
		Label label = label(text);
		if (maxWidth > 0.0D) {
			label.setWrapText(true);
			label.setMaxWidth(maxWidth);
		}

		return label;
	}

	public static VBox vbox(double padding, double spacing, Pos alignment, Node... children) {
		VBox vBox = new VBox(spacing, children);
		vBox.setPadding(new Insets(padding));
		if (alignment != null) {
			vBox.setAlignment(alignment);
		}

		return vBox;
	}

	public static VBox vbox(double padding, double spacing, Node... children) {
		return vbox(padding, spacing, (Pos) null, children);
	}

	public static HBox hbox(double padding, double spacing, Pos alignment, Node... children) {
		HBox hBox = new HBox(spacing, children);
		hBox.setPadding(new Insets(padding));
		if (alignment != null) {
			hBox.setAlignment(alignment);
		}

		return hBox;
	}

	public static HBox hbox(double padding, double spacing, Node... children) {
		return hbox(padding, spacing, Pos.BASELINE_LEFT, children);
	}

	public static Button button(String text, Runnable action) {
		Button button = new Button(text);
		button.setOnAction((event) -> {
			action.run();
		});
		return button;
	}

	public static Hyperlink hyperlink(String text, Runnable action) {
		Hyperlink hyperlink = new Hyperlink(text);
		hyperlink.setOnAction((event) -> {
			action.run();
		});
		return hyperlink;
	}

	public static Image icon(String resourcePath) {
		URL resource = LayoutHelper.class.getResource(resourcePath);
		if (resource == null) {
			throw new ResourceNotFoundException("Resource '" + resourcePath + "' not found.");
		} else {
			return new Image(resource.toExternalForm());
		}
	}

	public static Image icon(URL resource) {
		if (resource == null) {
			throw new ResourceNotFoundException();
		} else {
			return new Image(resource.toExternalForm());
		}
	}

	public static ImageView iconView(Image icon) {
		return new ImageView(icon);
	}

	public static ImageView iconView(URL resource) {
		return iconView(icon(resource));
	}

	public static ImageView iconView(Image icon, double size) {
		ImageView imageView = iconView(icon);
		imageView.setFitHeight(size);
		imageView.setFitWidth(size);
		return imageView;
	}

	public static ImageView iconView(URL resource, double size) {
		return iconView(icon(resource), size);
	}
}
