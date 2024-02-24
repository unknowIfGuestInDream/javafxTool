/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.control;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;

/**
 * 下划线样式文本框.
 *
 * @author unknowIfGuestInDream
 */
public class UnderlineTextField extends TextField {
    private static final StyleablePropertyFactory<UnderlineTextField> FACTORY
        = new StyleablePropertyFactory<>(TextField.getClassCssMetaData());
    private static final double STD_FONT_SIZE = 13;
    private static final double SMALL_FONT_SIZE = 10;
    private static final double TOP_OFFSET_Y = 4;
    private static final int ANIMATION_DURATION = 5;

    /**
     * 组件
     **/
    private Text promptText;
    private HBox promptTextBox;

    /**
     * paint
     **/
    private static final Color DEFAULT_MATERIAL_DESIGN_COLOR = Color.web("#009688");
    private static final Color DEFAULT_PROMPT_TEXT_COLOR = Color.web("#757575");

    /**
     * 动画时间线
     **/
    private Timeline timeline;

    /**
     * Properties
     **/
    private final StyleableProperty<Color> materialDesignColor;
    private final StyleableProperty<Color> promptTextColor;
    private DoubleProperty fontSize;

    /**
     * 用户代理样式表
     **/
    private static String userAgentStyleSheet;

    /**
     * css样式属性
     **/
    private static final CssMetaData<UnderlineTextField, Color> MATERIAL_DESIGN_COLOR
        = FACTORY.createColorCssMetaData
        ("-material-design-color", s -> s.materialDesignColor, DEFAULT_MATERIAL_DESIGN_COLOR, false);
    private static final CssMetaData<UnderlineTextField, Color> PROMPT_TEXT_COLOR
        = FACTORY.createColorCssMetaData
        ("-prompt-text-color", s -> s.promptTextColor, DEFAULT_PROMPT_TEXT_COLOR, false);

    public UnderlineTextField() {
        this("");
    }

    public UnderlineTextField(final String promptTextBox) {
        super(promptTextBox);
        materialDesignColor = new SimpleStyleableObjectProperty<>(MATERIAL_DESIGN_COLOR, this, "materialDesignColor");
        promptTextColor = new SimpleStyleableObjectProperty<>(PROMPT_TEXT_COLOR, this, "promptTextColor");
        fontSize = new SimpleDoubleProperty(UnderlineTextField.this, "fontSize", getFont().getSize());
        timeline = new Timeline();
        initGraphics();
        registerListeners();
    }

    /**
     * 初始化
     */
    private void initGraphics() {
        getStyleClass().addAll("material-field");
        final String fontFamily = getFont().getFamily();
        final int length = getText().length();
        promptText = new Text(getPromptText());
        promptText.getStyleClass().add("prompt-text");
        promptTextBox = new HBox(promptText);
        promptTextBox.getStyleClass().add("material-field");
        if (!isEditable() || isDisabled() || length > 0) {
            promptText.setFont(Font.font(fontFamily, SMALL_FONT_SIZE));
            promptTextBox.setTranslateY(-STD_FONT_SIZE - TOP_OFFSET_Y);
        } else {
            promptText.setFont(Font.font(fontFamily, STD_FONT_SIZE));
        }
        getChildren().addAll(promptTextBox);
    }

    /**
     * 注册监听器
     */
    private void registerListeners() {
        textProperty().addListener(o -> handleTextAndFocus(isFocused()));
        promptTextProperty().addListener(o -> promptText.setText(getPromptText()));
        focusedProperty().addListener(o -> handleTextAndFocus(isFocused()));
        promptTextColorProperty().addListener(o -> promptText.setFill(getPromptTextColor()));
        fontSize.addListener(o -> promptText.setFont(Font.font(fontSize.get())));
        timeline.setOnFinished(evt -> {
            final int length = null == getText() ? 0 : getText().length();
            if (length > 0 && promptTextBox.getTranslateY() >= 0) {
                promptTextBox.setTranslateY(-STD_FONT_SIZE - TOP_OFFSET_Y);
                fontSize.set(SMALL_FONT_SIZE);
            }
        });
    }

    /**
     * css样式属性
     */
    public Color getMaterialDesignColor() {
        return materialDesignColor.getValue();
    }

    public Color getPromptTextColor() {
        return promptTextColor.getValue();
    }

    @SuppressWarnings("unchecked")
    public ObjectProperty<Color> promptTextColorProperty() {
        return (ObjectProperty<Color>) promptTextColor;
    }

    /**
     * style related
     */
    @Override
    public String getUserAgentStylesheet() {
        if (null == userAgentStyleSheet) {
            userAgentStyleSheet = Objects.requireNonNull(getClass().getResource(
                "/com/tlcsdm/core/static/javafx/control/underlineTextField.css")).toExternalForm();
        }
        return userAgentStyleSheet;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    /**
     * function method
     */
    private void handleTextAndFocus(final boolean isFocused) {
        final int length = null == getText() ? 0 : getText().length();
        KeyFrame kf0;
        KeyFrame kf1;
        KeyValue kvTextY0;
        KeyValue kvTextY1;
        KeyValue kvTextFontSize0;
        KeyValue kvTextFontSize1;
        KeyValue kvPromptTextFill0;
        KeyValue kvPromptTextFill1;
        if (isFocused | length > 0 || isDisabled() || !isEditable()) {
            if (Double.compare(promptTextBox.getTranslateY(), -STD_FONT_SIZE - TOP_OFFSET_Y) != 0) {
                kvTextY0 = new KeyValue(promptTextBox.translateYProperty(), 0);
                kvTextY1 = new KeyValue(promptTextBox.translateYProperty(), -STD_FONT_SIZE - TOP_OFFSET_Y);
                kvTextFontSize0 = new KeyValue(fontSize, STD_FONT_SIZE);
                kvTextFontSize1 = new KeyValue(fontSize, SMALL_FONT_SIZE);
                kvPromptTextFill0 = new KeyValue(promptTextColorProperty(), DEFAULT_PROMPT_TEXT_COLOR);
                kvPromptTextFill1 = new KeyValue
                    (promptTextColorProperty(), isFocused ? getMaterialDesignColor() : DEFAULT_PROMPT_TEXT_COLOR);
                kf0 = new KeyFrame(Duration.ZERO, kvTextY0, kvTextFontSize0, kvPromptTextFill0);
                kf1 = new KeyFrame(Duration.millis(ANIMATION_DURATION), kvTextY1, kvTextFontSize1, kvPromptTextFill1);
                timeline.getKeyFrames().setAll(kf0, kf1);
                timeline.play();
            }
            promptText.setFill(isFocused ? getMaterialDesignColor() : DEFAULT_PROMPT_TEXT_COLOR);
        } else {
            if (Double.compare(promptTextBox.getTranslateY(), 0) != 0) {
                kvTextY0 = new KeyValue(promptTextBox.translateYProperty(), promptTextBox.getTranslateY());
                kvTextY1 = new KeyValue(promptTextBox.translateYProperty(), 0);
                kvTextFontSize0 = new KeyValue(fontSize, SMALL_FONT_SIZE);
                kvTextFontSize1 = new KeyValue(fontSize, STD_FONT_SIZE);
                kvPromptTextFill0 = new KeyValue(promptTextColorProperty(), getMaterialDesignColor());
                kvPromptTextFill1 = new KeyValue(promptTextColorProperty(), DEFAULT_PROMPT_TEXT_COLOR);
                kf0 = new KeyFrame(Duration.ZERO, kvTextY0, kvTextFontSize0, kvPromptTextFill0);
                kf1 = new KeyFrame(Duration.millis(ANIMATION_DURATION), kvTextY1, kvTextFontSize1, kvPromptTextFill1);
                timeline.getKeyFrames().setAll(kf0, kf1);
                timeline.play();
            }
        }
    }
}
