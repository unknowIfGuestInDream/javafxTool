package com.tlcsdm.core.javafx.bind;

import javafx.beans.binding.Binding;

public interface FxBindingBuilder<T> {

    Binding<T> build();
}
