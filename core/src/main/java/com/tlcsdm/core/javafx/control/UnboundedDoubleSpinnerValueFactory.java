/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.DoubleStringConverter;

/**
 * A {@link javafx.scene.control.SpinnerValueFactory} implementation designed to iterate through
 * double values like {@link javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory}.
 * <p>
 * Unlike {@code DoubleSpinnerValueFactory}, this implementation handles the values
 * {@link Double#NaN}, {@link Double#POSITIVE_INFINITY}, and {@link Double#NEGATIVE_INFINITY}.
 * </p>
 * <p>
 * Note that the default {@link #converterProperty() converter} is implemented as an
 * {@link javafx.util.converter.DoubleStringConverter} instance.
 * </p>
 *
 * @author unknowIfGuestInDream
 */
public class UnboundedDoubleSpinnerValueFactory extends SpinnerValueFactory<Double> {
    /**
     * Constructs a new DoubleSpinnerValueFactory that sets the initial value to be equal to the min
     * value, and a default {@code amountToStepBy} of one.
     *
     * @param min The minimum allowed double value for the Spinner.
     * @param max The maximum allowed double value for the Spinner.
     */
    public UnboundedDoubleSpinnerValueFactory(@NamedArg("min") double min, @NamedArg("max") double max) {
        this(min, max, min);
    }

    /**
     * Constructs a new DoubleSpinnerValueFactory with a default {@code amountToStepBy} of one.
     *
     * @param min          The minimum allowed double value for the Spinner.
     * @param max          The maximum allowed double value for the Spinner.
     * @param initialValue The value of the Spinner when first instantiated, must be within the bounds
     *                     of the min and max arguments, or else the min value will be used.
     */
    public UnboundedDoubleSpinnerValueFactory(@NamedArg("min") double min, @NamedArg("max") double max, @NamedArg("initialValue") double initialValue) {
        this(min, max, initialValue, 1);
    }

    /**
     * Constructs a new DoubleSpinnerValueFactory.
     *
     * @param min            The minimum allowed double value for the Spinner.
     * @param max            The maximum allowed double value for the Spinner.
     * @param initialValue   The value of the Spinner when first instantiated, must be within the bounds
     *                       of the min and max arguments, or else the min value will be used.
     * @param amountToStepBy The amount to increment or decrement by, per step.
     */
    public UnboundedDoubleSpinnerValueFactory(@NamedArg("min") double min, @NamedArg("max") double max, @NamedArg("initialValue") double initialValue,
        @NamedArg("amountToStepBy") double amountToStepBy) {
        setMin(min);
        setMax(max);
        setAmountToStepBy(amountToStepBy);
        setConverter(new DoubleStringConverter());

        valueProperty().addListener((o, oldValue, newValue) ->
        {
            if (newValue == null) {
                setValue(oldValue);
                return;
            }
            // when the value is set, we need to react to ensure it is a
            // valid value (and if not, blow up appropriately)
            if (newValue < getMin())
                setValue(getMin());
            else if (newValue > getMax())
                setValue(getMax());
        });
        setValue(initialValue >= min && initialValue <= max ? initialValue : min);
    }

    private final DoubleProperty min = new SimpleDoubleProperty(this, "min") {
        @Override
        protected void invalidated() {
            Double currentValue = UnboundedDoubleSpinnerValueFactory.this.getValue();
            if (currentValue == null) {
                return;
            }

            final double newMin = get();
            if (newMin > getMax()) {
                setMin(getMax());
                return;
            }

            if (currentValue < newMin) {
                UnboundedDoubleSpinnerValueFactory.this.setValue(newMin);
            }
        }
    };

    public final void setMin(double value) {
        min.set(value);
    }

    public final double getMin() {
        return min.get();
    }

    /**
     * Sets the minimum allowable value for this value factory
     */
    public final DoubleProperty minProperty() {
        return min;
    }

    // --- max
    private DoubleProperty max = new SimpleDoubleProperty(this, "max") {
        @Override
        protected void invalidated() {
            Double currentValue = UnboundedDoubleSpinnerValueFactory.this.getValue();
            if (currentValue == null) {
                return;
            }

            final double newMax = get();
            if (newMax < getMin()) {
                setMax(getMin());
                return;
            }

            if (currentValue > newMax) {
                UnboundedDoubleSpinnerValueFactory.this.setValue(newMax);
            }
        }
    };

    public final void setMax(double value) {
        max.set(value);
    }

    public final double getMax() {
        return max.get();
    }

    /**
     * Sets the maximum allowable value for this value factory
     */
    public final DoubleProperty maxProperty() {
        return max;
    }

    // --- amountToStepBy
    private DoubleProperty amountToStepBy = new SimpleDoubleProperty(this, "amountToStepBy");

    public final void setAmountToStepBy(double value) {
        amountToStepBy.set(value);
    }

    public final double getAmountToStepBy() {
        return amountToStepBy.get();
    }

    /**
     * Sets the amount to increment or decrement by, per step.
     */
    public final DoubleProperty amountToStepByProperty() {
        return amountToStepBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decrement(int steps) {
        double currentValue = getValue();
        double min = getMin();
        double max = getMax();
        double amountToStepBy = getAmountToStepBy();
        double newValue = currentValue - amountToStepBy * steps;

        if (newValue >= min)
            setValue(newValue);
        else if (isWrapAround())
            setValue(wrapValue(newValue, min, max));
        else
            setValue(min);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(int steps) {
        double currentValue = getValue();
        double min = getMin();
        double max = getMax();
        double amountToStepBy = getAmountToStepBy();
        double newValue = currentValue + amountToStepBy * steps;

        if (newValue <= max)
            setValue(newValue);
        else if (isWrapAround())
            setValue(wrapValue(newValue, min, max));
        else
            setValue(max);
    }

    /*
     * Convenience method to support wrapping values around their min / max constraints. Used by the
     * SpinnerValueFactory implementations when the Spinner wrapAround property is true.
     */
    static double wrapValue(double value, double min, double max) {
        if (max == 0.0)
            throw new RuntimeException();

        if (value < min)
            return max;
        else if (value > max)
            return min;
        else
            return value;
    }
}
