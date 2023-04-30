/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.util.jexl;

import org.apache.commons.jexl3.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

/**
 * Simple example to show how to access method and properties.
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/4/23 21:18
 */
@DisabledIfEnvironmentVariable(named = "ENV", matches = "workflow", disabledReason = "The scope of JEXL is provided")
public class MethodPropertyTest {
    /**
     * An example for method access.
     */
    public static void example() throws Exception {
        /*
         * First step is to retrieve an instance of a JexlEngine;
         * it might be already existing and shared or created anew.
         */
        final JexlEngine jexl = new JexlBuilder().create();
        /*
         *  Second make a jexlContext and put stuff in it
         */
        final JexlContext jc = new MapContext();

        /*
         * The Java equivalents of foo and number for comparison and checking
         */
        final Foo foo = new Foo();
        final Integer number = 10;

        jc.set("foo", foo);
        jc.set("number", number);

        /*
         *  access a method w/o args
         */
        JexlExpression e = jexl.createExpression("foo.getFoo()");
        Object o = e.evaluate(jc);
        System.out.println("value returned by the method getFoo() is : " + o + foo.getFoo());

        /*
         *  access a method w/ args
         */
        e = jexl.createExpression("foo.convert(1)");
        o = e.evaluate(jc);
        System.out.println("value of " + e.getParsedText() + " is : " + o + foo.convert(1));

        e = jexl.createExpression("foo.convert(1+7)");
        o = e.evaluate(jc);
        System.out.print("value of " + e.getParsedText() + " is : " + o + foo.convert(1 + 7));

        e = jexl.createExpression("foo.convert(1+number)");
        o = e.evaluate(jc);
        System.out.print("value of " + e.getParsedText() + " is : " + o + foo.convert(1 + number));

        /*
         * access a property
         */
        e = jexl.createExpression("foo.bar");
        o = e.evaluate(jc);
        System.out.print("value returned for the property 'bar' is : " + o + foo.get("bar"));

    }

    /**
     * Helper example class.
     */
    public static class Foo {

        /**
         * Gets foo.
         *
         * @return a string.
         */
        public String getFoo() {
            return "This is from getFoo()";
        }

        /**
         * Gets an arbitrary property.
         *
         * @param arg property name.
         * @return arg prefixed with 'This is the property '.
         */
        public String get(final String arg) {
            return "This is the property " + arg;
        }

        /**
         * Gets a string from the argument.
         *
         * @param i a long.
         * @return The argument prefixed with 'The value is : '
         */
        public String convert(final long i) {
            return "The value is : " + i;
        }
    }

    /**
     * Unit test entry point.
     *
     * @throws Exception
     */
    @Test
    public void testExample() throws Exception {
        example();
    }
}
