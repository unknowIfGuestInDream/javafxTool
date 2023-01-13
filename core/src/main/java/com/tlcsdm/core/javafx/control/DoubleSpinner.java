package com.tlcsdm.core.javafx.control;

import javafx.beans.NamedArg;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/12/11 17:39
 */
public class DoubleSpinner extends Spinner<Double> {
	public DoubleSpinner(@NamedArg(value = "min", defaultValue = "0") double min,
			@NamedArg(value = "max", defaultValue = "100") double max,
			@NamedArg(value = "initialValue", defaultValue = "0") double initialValue,
			@NamedArg(value = "amountToStepBy", defaultValue = "1") double amountToStepBy) {
		super(min, max, initialValue, amountToStepBy);
	}

	public void setValue(double value) {
		SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory = this.getSpinnerValueFactory();
		if (!(value < valueFactory.getMin()) && !(value > valueFactory.getMax())) {
			valueFactory.setValue(value);
		} else {
			double var10002 = valueFactory.getMin();
			throw new IllegalArgumentException("Value out of bounds(" + var10002 + "~" + valueFactory.getMax() + ").");
		}
	}

	public SpinnerValueFactory.DoubleSpinnerValueFactory getSpinnerValueFactory() {
		return (SpinnerValueFactory.DoubleSpinnerValueFactory) this.getValueFactory();
	}

	public double getMin() {
		return this.getSpinnerValueFactory().getMin();
	}

	public double getMax() {
		return this.getSpinnerValueFactory().getMax();
	}
}
