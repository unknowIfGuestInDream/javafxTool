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
import com.fathzer.soft.javaluator.StaticVariableSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author unknowIfGuestInDream
 */
public class DoubleEvaluatorTest {
    private static final DoubleEvaluator EVALUATOR = new DoubleEvaluator();

    @Test
    public void excelLike() {
        DoubleEvaluator excelLike = new DoubleEvaluator(DoubleEvaluator.getDefaultParameters(
            DoubleEvaluator.Style.EXCEL));
        assertEquals(4, excelLike.evaluate("-2^2"), 0.001);
    }

    @Test
    public void testResults() {
        assertEquals(-2, EVALUATOR.evaluate("2+-2^2"), 0.001);
        assertEquals(2, EVALUATOR.evaluate("6 / 3"), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, EVALUATOR.evaluate("2/0"), 0.001);
        assertEquals(2, EVALUATOR.evaluate("7 % 2.5"), 0.001);
        assertEquals(-1., EVALUATOR.evaluate("-1"), 0.001);
        assertEquals(1., EVALUATOR.evaluate("1"), 0.001);
        assertEquals(-3, EVALUATOR.evaluate("1+-4"), 0.001);
        assertEquals(2, EVALUATOR.evaluate("3-1"), 0.001);
        assertEquals(-4, EVALUATOR.evaluate("-2^2"), 0.001);
        assertEquals(2, EVALUATOR.evaluate("4^0.5"), 0.001);

        assertEquals(1, EVALUATOR.evaluate("sin ( pi /2)"), 0.001);
        assertEquals(-1, EVALUATOR.evaluate("cos(pi)"), 0.001);
        assertEquals(1, EVALUATOR.evaluate("tan(pi/4)"), 0.001);
        assertEquals(Math.PI, EVALUATOR.evaluate("acos( -1)"), 0.001);
        assertEquals(Math.PI / 2, EVALUATOR.evaluate("asin(1)"), 0.001);
        assertEquals(Math.PI / 4, EVALUATOR.evaluate("atan(1)"), 0.001);

        assertEquals(1, EVALUATOR.evaluate("ln(e)"), 0.001);
        assertEquals(2, EVALUATOR.evaluate("log(100)"), 0.001);
        assertEquals(-1, EVALUATOR.evaluate("min(1,-1)"), 0.001);
        assertEquals(-1, EVALUATOR.evaluate("min(8,3,1,-1)"), 0.001);
        assertEquals(11, EVALUATOR.evaluate("sum(8,3,1,-1)"), 0.001);
        assertEquals(3, EVALUATOR.evaluate("avg(8,3,1,0)"), 0.001);

        assertEquals(3, EVALUATOR.evaluate("abs(-3)"), 0.001);
        assertEquals(3, EVALUATOR.evaluate("ceil(2.45)"), 0.001);
        assertEquals(2, EVALUATOR.evaluate("floor(2.45)"), 0.001);
        assertEquals(2, EVALUATOR.evaluate("round(2.45)"), 0.001);

        double rnd = EVALUATOR.evaluate("random()");
        assertTrue(rnd >= 0 && rnd <= 1.0);

        assertEquals(EVALUATOR.evaluate("tanh(5)"), EVALUATOR.evaluate("sinh(5)/cosh(5)"), 0.001);

        assertEquals(-1, EVALUATOR.evaluate("min(1,min(3+2,2))+-round(4.1)*0.5"), 0.001);
    }

    @Test
    public void testWithVariable() {
        String expression = "x+2";
        StaticVariableSet<Double> variables = new StaticVariableSet<Double>();

        variables.set("x", 1.);
        assertEquals(3, EVALUATOR.evaluate(expression, variables), 0.001);
        variables.set("x", -1.);
        assertEquals(1, EVALUATOR.evaluate(expression, variables), 0.001);
    }

    @Test
    public void test2ValuesFollowing() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("10 5 +");
        });
    }

    @Test
    public void test2ValuesFollowing2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("(10) (5)");
        });
    }

    @Test
    public void test2OperatorsFollowing() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("10**5");
        });
    }

    @Test
    public void testMissingEndValue() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("10*");
        });
    }

    @Test
    public void testNoFunctionArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("sin()");
        });
    }

    @Test
    public void testNoFunctionArgument2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("average()");
        });
    }

    @Test
    public void testEmptyFunctionArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("min(,2)");
        });
    }

    @Test
    public void TestFunctionSeparatorHidenByBrackets() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DoubleEvaluator().evaluate("min((10,15),20)");
        });

    }

    @Test
    public void TestComplexFunctionAndParenthesis() {
        assertEquals(15.0, new DoubleEvaluator().evaluate("min((max(((10-6)*2),7,(15))),19+min(1,5))"), 0.001);
    }

    @Test
    public void TestScientificNotation() {
        DoubleEvaluator evaluator = new DoubleEvaluator(DoubleEvaluator.getDefaultParameters(), true);
        assertEquals(3e-2, evaluator.evaluate("3e-2"), 1e-5);
        assertEquals(30.9317695E+115, evaluator.evaluate("30.9317695E+115"), 1.0);
    }

    @Test
    public void testSomethingBetweenFunctionAndOpenBracket() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("sin3(45)");
        });
    }

    @Test
    public void testMissingArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("min(1,)");
        });

    }

    @Test
    public void testInvalidArgumentACos() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("acos(2)");
        });
    }

    @Test
    public void testInvalidArgumentASin() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("asin(2)");
        });
    }

    @Test
    public void testOnlyCloseBracket() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate(")");
        });
    }

    @Test
    public void testDSuffixInLiteral() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("3d+4");
        });
    }

    @Test
    public void testStartWithFunctionSeparator() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate(",3");
        });
    }

    @Test
    public void testNoArgInAverageFunction() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EVALUATOR.evaluate("avg()");
        });
    }
}
