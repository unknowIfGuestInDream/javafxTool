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
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.LongStringConverter;

/**
 * A {@link javafx.scene.control.SpinnerValueFactory} implementation designed to iterate through
 * long values.
 *
 * <p>
 * Note that the default {@link #converterProperty() converter} is implemented as an
 * {@link javafx.util.converter.LongStringConverter} instance.
 *
 * @author unknowIfGuestInDream
 */
public class LongSpinnerValueFactory extends SpinnerValueFactory<Long> {

    /**
     * Constructs a new LongSpinnerValueFactory that sets the initial value to be equal to the min
     * value, and a default {@code amountToStepBy} of one.
     *
     * @param min The minimum allowed long value for the Spinner.
     * @param max The maximum allowed long value for the Spinner.
     */
    public LongSpinnerValueFactory(@NamedArg("min") long min, @NamedArg("max") long max) {
        this(min, max, min);
    }

    /**
     * Constructs a new LongSpinnerValueFactory with a default {@code amountToStepBy} of one.
     *
     * @param min          The minimum allowed long value for the Spinner.
     * @param max          The maximum allowed long value for the Spinner.
     * @param initialValue The value of the Spinner when first instantiated, must be within the bounds
     *                     of the min and max arguments, or else the min value will be used.
     */
    public LongSpinnerValueFactory(@NamedArg("min") long min, @NamedArg("max") long max, @NamedArg("initialValue") long initialValue) {
        this(min, max, initialValue, 1L);
    }

    /**
     * Constructs a new LongSpinnerValueFactory.
     *
     * @param min            The minimum allowed long value for the Spinner.
     * @param max            The maximum allowed long value for the Spinner.
     * @param initialValue   The value of the Spinner when first instantiated, must be within the bounds
     *                       of the min and max arguments, or else the min value will be used.
     * @param amountToStepBy The amount to increment or decrement by, per step.
     */
    public LongSpinnerValueFactory(@NamedArg("min") long min, @NamedArg("max") long max, @NamedArg("initialValue") long initialValue,
        @NamedArg("amountToStepBy") long amountToStepBy) {
        setMin(min);
        setMax(max);
        setAmountToStepBy(amountToStepBy);
        setConverter(new LongStringConverter());

        valueProperty().addListener((o, oldValue, newValue) -> {
            // when the value is set, we need to react to ensure it is a
            // valid value (and if not, blow up appropriately)
            if (newValue < getMin()) {
                setValue(getMin());
            } else if (newValue > getMax()) {
                setValue(getMax());
            }
        });
        setValue(initialValue >= min && initialValue <= max ? initialValue : min);
    }

    private final LongProperty min = new SimpleLongProperty(this, "min") {
        @Override
        protected void invalidated() {
            Long currentValue = LongSpinnerValueFactory.this.getValue();
            if (currentValue == null) {
                return;
            }

            long newMin = get();
            if (newMin > getMax()) {
                setMin(getMax());
                return;
            }

            if (currentValue < newMin) {
                LongSpinnerValueFactory.this.setValue(newMin);
            }
        }
    };

    public final void setMin(long value) {
        min.set(value);
    }

    public final long getMin() {
        return min.get();
    }

    /**
     * Sets the minimum allowable value for this value factory
     */
    public final LongProperty minProperty() {
        return min;
    }

    private final LongProperty max = new SimpleLongProperty(this, "max") {
        @Override
        protected void invalidated() {
            Long currentValue = LongSpinnerValueFactory.this.getValue();
            if (currentValue == null) {
                return;
            }

            long newMax = get();
            if (newMax < getMin()) {
                setMax(getMin());
                return;
            }

            if (currentValue > newMax) {
                LongSpinnerValueFactory.this.setValue(newMax);
            }
        }
    };

    public final void setMax(long value) {
        max.set(value);
    }

    public final long getMax() {
        return max.get();
    }

    /**
     * Sets the maximum allowable value for this value factory
     */
    public final LongProperty maxProperty() {
        return max;
    }

    // --- amountToStepBy
    private final LongProperty amountToStepBy = new SimpleLongProperty(this, "amountToStepBy");

    public final void setAmountToStepBy(long value) {
        amountToStepBy.set(value);
    }

    public final long getAmountToStepBy() {
        return amountToStepBy.get();
    }

    /**
     * Sets the amount to increment or decrement by, per step.
     */
    public final LongProperty amountToStepByProperty() {
        return amountToStepBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decrement(int steps) {
        final long min = getMin();
        final long max = getMax();
        final long newIndex = getValue() - steps * getAmountToStepBy();
        setValue(newIndex >= min ? newIndex : (isWrapAround() ? wrapValue(newIndex, min, max) + 1 : min));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(int steps) {
        final long min = getMin();
        final long max = getMax();
        final long currentValue = getValue();
        final long newIndex = currentValue + steps * getAmountToStepBy();
        setValue(newIndex <= max ? newIndex : (isWrapAround() ? wrapValue(newIndex, min, max) - 1 : max));
    }

    /*
     * Convenience method to support wrapping values around their min / max constraints. Used by the
     * SpinnerValueFactory implementations when the Spinner wrapAround property is true.
     */
    static long wrapValue(long value, long min, long max) {
        if (max == 0) {
            throw new RuntimeException();
        }

        long r = value % max;
        if (r > min && max < min) {
            r = r + max - min;
        } else if (r < min && max > min) {
            r = r + max - min;
        }
        return r;
    }
}
