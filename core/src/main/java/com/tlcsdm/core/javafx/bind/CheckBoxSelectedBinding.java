package com.tlcsdm.core.javafx.bind;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.CheckBox;

/**
 * CheckBox 选中的binding
 * 选中返回true
 */
public class CheckBoxSelectedBinding extends FxBooleanBindingBuilder {
    private final CheckBox check;

    public CheckBoxSelectedBinding(CheckBox check) {
        this.check = check;
    }

    @Override
    public BooleanBinding build() {
        return Bindings.createBooleanBinding(check::isSelected, check.selectedProperty());
    }
}
