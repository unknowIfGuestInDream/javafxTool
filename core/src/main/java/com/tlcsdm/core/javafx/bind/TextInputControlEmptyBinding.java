package com.tlcsdm.core.javafx.bind;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.TextInputControl;

/**
 * 输入框空值 BooleanBinding
 * 为空返回true
 */
public class TextInputControlEmptyBinding extends FxBooleanBindingBuilder {
    private final TextInputControl input;

    public TextInputControlEmptyBinding(TextInputControl input) {
        this.input = input;
    }

    @Override
    public BooleanBinding build() {
        return Bindings.createBooleanBinding(() ->
                input.getText().isEmpty(), input.textProperty());
    }
}
