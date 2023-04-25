package com.tlcsdm.core.javafx.bind;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ComboBoxBase;

/**
 * ComboBoxBase 值为空的BooleanBinding
 * 为空返回true
 */
public class ComboBoxBaseEmptyBinding<T> extends FxBooleanBindingBuilder {
    private final ComboBoxBase<T> combo;

    public ComboBoxBaseEmptyBinding(ComboBoxBase<T> combo) {
        this.combo = combo;
    }

    @Override
    public BooleanBinding build() {
        return Bindings.createBooleanBinding(() ->
            combo.getValue() == null || combo.getValue().toString().length() == 0, combo.valueProperty());
    }
}
