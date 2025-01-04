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

import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Parameters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 */
public class BracketsTest {

    @Test
    public void test() {
        DoubleEvaluator eval = buildEvaluator();
        Assertions.assertEquals(1.0, eval.evaluate("[(0.5)+(0.5)]"), 0.0001);
        Assertions.assertEquals(1.0, eval.evaluate("sin<[pi/2]>"), 0.0001);
    }

    private DoubleEvaluator buildEvaluator() {
        Parameters defaultParams = DoubleEvaluator.getDefaultParameters();
        Parameters params = new Parameters();
        params.add(DoubleEvaluator.SINE);
        params.addConstants(defaultParams.getConstants());
        params.addOperators(defaultParams.getOperators());
        params.addExpressionBracket(BracketPair.PARENTHESES);
        params.addExpressionBracket(BracketPair.BRACKETS);
        params.addFunctionBracket(BracketPair.ANGLES);
        DoubleEvaluator eval = new DoubleEvaluator(params);
        return eval;
    }

    @Test
    public void testInvalidBrackets() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DoubleEvaluator().evaluate("[(0.5)+(0.5)]");
        });
    }

    @Test
    public void testInvalidBracketsMatching() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            buildEvaluator().evaluate("([0.5)+(0.5)]");
        });
    }

    @Test
    public void testInvalidFunctionBrackets() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            buildEvaluator().evaluate("sin[0.5]");
        });
    }

    @Test
    public void testInvalidExpressionBrackets() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            buildEvaluator().evaluate("<0.5*2>");
        });
    }
}
