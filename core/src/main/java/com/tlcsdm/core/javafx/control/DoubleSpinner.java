/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * @author unknowIfGuestInDream
 * @date 2022/12/11 17:39
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
