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

/**
 * Copyright (c) 2013, 2015, ControlsFX
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.tlcsdm.core.javafx.control;

import com.tlcsdm.core.javafx.control.skin.CustomTextFieldSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.SizeConverter;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A base class for people wanting to customize a {@link TextField} to contain nodes
 * inside the text field itself, without being on top of the users typed-in text.
 *
 * <h3>Screenshot</h3>
 * <p>The following demo shows a normal TextField, with a {@link TextFields#createClearableTextField() clearable text field},
 * followed by three CustomTextFields. Note what happens with long text input -
 * it is prevented from going beneath the left and right graphics. Of course, if
 * the keyboard caret moves to the right, the text will become visible, but this
 * is because it will all scroll to the left (as is the case in a normal {@link TextField}).
 *
 * @see TextFields
 * @see CustomPasswordField
 */
public class CustomTextField extends TextField {

    /**
     * Instantiates a default CustomTextField.
     */
    public CustomTextField() {
        getStyleClass().add("custom-text-field");
    }

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    // --- left
    private final ObjectProperty<Node> left = new SimpleObjectProperty<>(this, "left");

    /**
     * @return An ObjectProperty wrapping the {@link Node} that is placed
     * on the left of the text field.
     */
    public final ObjectProperty<Node> leftProperty() {
        return left;
    }

    /**
     * @return the {@link Node} that is placed on the left of
     * the text field.
     */
    public final Node getLeft() {
        return left.get();
    }

    /**
     * Sets the {@link Node} that is placed on the left of
     * the text field.
     *
     * @param value
     */
    public final void setLeft(Node value) {
        left.set(value);
    }

    // --- right
    private final ObjectProperty<Node> right = new SimpleObjectProperty<>(this, "right");

    /**
     * Property representing the {@link Node} that is placed on the right of
     * the text field.
     *
     * @return An ObjectProperty.
     */
    public final ObjectProperty<Node> rightProperty() {
        return right;
    }

    /**
     * @return The {@link Node} that is placed on the right of
     * the text field.
     */
    public final Node getRight() {
        return right.get();
    }

    /**
     * Sets the {@link Node} that is placed on the right of
     * the text field.
     *
     * @param value
     */
    public final void setRight(Node value) {
        right.set(value);
    }

    private DoubleProperty offsetX;

    public final DoubleProperty offsetXProperty() {
        if (offsetX == null) {
            offsetX = new StyleableDoubleProperty(0) {

                @Override
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return CustomTextField.StyleableProperties.REGION_OFFSET_X;
                }

                @Override
                public Object getBean() {
                    return CustomTextField.this;
                }

                @Override
                public String getName() {
                    return "offsetX";
                }
            };
        }
        return offsetX;
    }

    public final double getOffsetX() {
        return offsetX == null ? 0 : offsetX.get();
    }

    public final void setOffsetX(double value) {
        offsetXProperty().set(value);
    }

    private final DoubleProperty offsetY = new SimpleDoubleProperty(this, "offsetY");

    public final DoubleProperty offsetYProperty() {
        return offsetY;
    }

    public final double getOffsetY() {
        return offsetY.get();
    }

    public final void setOffsetY(double value) {
        offsetY.set(value);
    }

    private static class StyleableProperties {
        // 按钮显示
        private static final CssMetaData<CustomTextField, Number> REGION_OFFSET_X = new CssMetaData<>(
            "-jfx-custom-offsetX", SizeConverter.getInstance(), 0) {

            @Override
            public boolean isSettable(CustomTextField n) {
                return n.offsetX == null || !n.offsetX.isBound();
            }

            @SuppressWarnings("unchecked")
            @Override
            public StyleableProperty<Number> getStyleableProperty(CustomTextField n) {
                return (StyleableProperty<Number>) n.offsetXProperty();
            }
        };

        // 创建一个CSS样式的表
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(
                TextField.getClassCssMetaData());
            Collections.addAll(styleables, REGION_OFFSET_X);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new CustomTextFieldSkin(this) {
            @Override
            public ObjectProperty<Node> leftProperty() {
                return CustomTextField.this.leftProperty();
            }

            @Override
            public ObjectProperty<Node> rightProperty() {
                return CustomTextField.this.rightProperty();
            }

            @Override
            public DoubleProperty offsetXProperty() {
                return CustomTextField.this.offsetXProperty();
            }

            @Override
            public DoubleProperty offsetYProperty() {
                return CustomTextField.this.offsetYProperty();
            }
        };
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return CustomTextField.StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(
                CustomTextField.class.getResource("/com/tlcsdm/core/static/javafx/control/customtextfield.css"))
            .toExternalForm();
    }
}
