package com.tlcsdm.core.javafx.bind;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.TextInputControl;

import java.util.Arrays;

/**
 * 如果control状态为disabled 则跳过此组件的空判断
 */
public class MultiTextInputControlEmptyBinding extends FxBooleanBindingBuilder {

    private final TextInputControl[] inputs;

    public MultiTextInputControlEmptyBinding(TextInputControl... inputs) {
        this.inputs = inputs;
    }

    @Override
    public BooleanBinding build() {
        Observable[] observables = Arrays.stream(inputs).filter(i -> !i.isDisabled()).map(TextInputControl::textProperty).toArray(Observable[]::new);
        return Bindings.createBooleanBinding(() -> Arrays.stream(inputs).filter(i -> !i.isDisabled()).anyMatch(i -> i.getText().isEmpty()), observables);
    }
}
