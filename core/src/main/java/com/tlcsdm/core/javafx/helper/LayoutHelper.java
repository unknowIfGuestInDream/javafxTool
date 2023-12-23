/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.javafx.helper;

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

import java.net.URL;

/**
 * @author unknowIfGuestInDream
 * @date 2022/11/27 0:05
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
        return vbox(padding, spacing, null, children);
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
        button.setOnAction((event) -> action.run());
        return button;
    }

    public static Hyperlink hyperlink(String text, Runnable action) {
        Hyperlink hyperlink = new Hyperlink(text);
        hyperlink.setOnAction((event) -> action.run());
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

    public static ImageView imageView(String path, double width, double height) {
        ImageView imageView = new ImageView(ImageHelper.image(path));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    public static ImageView iconView(URL resource, double size) {
        return iconView(icon(resource), size);
    }
}
