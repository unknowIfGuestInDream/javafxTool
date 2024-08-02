/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.javaluator;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Operator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author unknowIfGuestInDream
 */
public class OperatorTest {
    @Test
    public void test() {
        final Operator plus = new Operator("+", 2, Operator.Associativity.LEFT, 1);
        assertEquals(DoubleEvaluator.PLUS, plus);
        assertEquals(DoubleEvaluator.PLUS.hashCode(), plus.hashCode());
        assertNotEquals(plus, new Operator("-", 2, Operator.Associativity.LEFT, 1));
        assertNotEquals(plus, new Operator("+", 1, Operator.Associativity.LEFT, 1));
        assertNotEquals(plus, new Operator("+", 2, Operator.Associativity.RIGHT, 1));
        assertNotEquals(plus, new Operator("+", 2, Operator.Associativity.LEFT, 2));

        @SuppressWarnings("unlikely-arg-type") final boolean wrongEquals = plus.equals(null) || plus.equals("+");
        assertFalse(wrongEquals);
    }

    @Test
    public void emptySymbol() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Operator("", 2, Operator.Associativity.LEFT, 1);
        });
    }

    @Test
    public void trailingBlankSymbol() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Operator("+ ", 2, Operator.Associativity.LEFT, 1);
        });
    }

    @Test
    public void noSymbol() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Operator(null, 2, Operator.Associativity.LEFT, 1);
        });
    }

    @Test
    public void noAssociativity() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Operator("+", 2, null, 1);
        });
    }

    @Test
    public void IllegalAssociativity() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Operator("+", 2, Operator.Associativity.NONE, 1);
        });
    }

    @Test
    public void operandCountTooHigh() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Operator("+", 3, Operator.Associativity.LEFT, 1);
        });
    }

    @Test
    public void operandCountTooLow() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Operator("+", 0, Operator.Associativity.LEFT, 1);
        });
    }

    @Test
    public void totallyWrong() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Operator(null, 0, null, 1);
        });
    }
}
