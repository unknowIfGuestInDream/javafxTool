package com.tlcsdm.core.javafx.bind;

import javafx.beans.binding.BooleanBinding;

public abstract class FxBooleanBindingBuilder implements FxBindingBuilder<Boolean> {

    @Override
    public abstract BooleanBinding build();
}
