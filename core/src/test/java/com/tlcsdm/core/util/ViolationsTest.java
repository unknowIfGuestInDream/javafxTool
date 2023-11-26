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

package com.tlcsdm.core.util;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.bjurr.violations.lib.ViolationsApi;
import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.reports.Parser;

import java.util.ArrayList;
import java.util.Set;

/**
 * violations.
 */
class ViolationsTest {

    private static final String MSG_1 = "The scope of the variable 'n' can be reduced.";
    private static final String MSG_2 = "The scope of the variable 'i' can be reduced.";

    /**
     * cppcheck --quiet --enable=all --force --inline-suppr --xml --xml-version=2 . 2>
     * cppcheck-result.xml
     */
    @Test
    public void cppcheck() {
        String rootFolder = ResourceUtil.getResource("violations").getPath();
        Set<Violation> actual =
            ViolationsApi.violationsApi() //
                .withPattern(".*/cppcheck/main\\.xml$") //
                .inFolder(rootFolder) //
                .findAll(Parser.CPPCHECK) //
                .violations();
        Assertions.assertEquals(3, actual.size());

        final Violation v1 =
            Violation.violationBuilder() //
                .setParser(Parser.CPPCHECK) //
                .setFile("api.c") //
                .setStartLine(498) //
                .setEndLine(498) //
                .setRule("variableScope") //
                .setMessage(MSG_1) //
                .setSeverity(SEVERITY.INFO) //
                .setGroup("1") //
                .build();

        final Violation v2 =
            Violation.violationBuilder() //
                .setParser(Parser.CPPCHECK) //
                .setFile("api_storage.c") //
                .setStartLine(104) //
                .setEndLine(104) //
                .setRule("variableScope") //
                .setMessage(MSG_2) //
                .setSeverity(SEVERITY.ERROR) //
                .setGroup("2") //
                .build();
//        Assertions.assertTrue(actual.contains(v1));
//        Assertions.assertTrue(actual.contains(v2));

        actual =
            ViolationsApi.violationsApi() //
                .withPattern(".*/cppcheck/example1\\.xml$") //
                .inFolder(rootFolder) //
                .findAll(Parser.CPPCHECK) //
                .violations();

        final Violation violation0 = new ArrayList<>(actual).get(0);
        Assertions.assertEquals(violation0.getMessage(), "Variable 'it' is reassigned a value before the old one has been used.");

        final Violation violation1 = new ArrayList<>(actual).get(1);
        Assertions.assertEquals(violation1.getMessage(), "Variable 'it' is reassigned a value before the old one has been used.");

        final Violation violation2 = new ArrayList<>(actual).get(2);
        Assertions.assertEquals(violation2.getMessage(), "Condition 'rc' is always true");

        final Violation violation3 = new ArrayList<>(actual).get(3);
        Assertions.assertEquals(violation3.getMessage(), "Condition 'rc' is always true. Assignment 'rc=true', assigned value is 1");
    }

}
