package com.tlcsdm.core.javafx.control;

import javafx.beans.NamedArg;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * @author: 唐 亮
 * @date: 2022/12/11 17:32
 */
public class IntegerSpinner extends Spinner<Integer> {
    public IntegerSpinner(@NamedArg(value = "min", defaultValue = "0") int min, @NamedArg(value = "max", defaultValue = "100") int max, @NamedArg(value = "initialValue", defaultValue = "0") int initialValue, @NamedArg(value = "amountToStepBy", defaultValue = "1") int amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);
    }

    public void setValue(int value) {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = this.getSpinnerValueFactory();
        if (value >= valueFactory.getMin() && value <= valueFactory.getMax()) {
            valueFactory.setValue(value);
        } else {
            int var10002 = valueFactory.getMin();
            throw new IllegalArgumentException("Value out of bounds(" + var10002 + "~" + valueFactory.getMax() + ").");
        }
    }

    public SpinnerValueFactory.IntegerSpinnerValueFactory getSpinnerValueFactory() {
        return (SpinnerValueFactory.IntegerSpinnerValueFactory) this.getValueFactory();
    }

    public int getMin() {
        return this.getSpinnerValueFactory().getMin();
    }

    public int getMax() {
        return this.getSpinnerValueFactory().getMax();
    }

    public void setMax(int max) {
        this.getSpinnerValueFactory().setMax(max);
    }

    public void setMin(int min) {
        this.getSpinnerValueFactory().setMin(min);
    }
}
