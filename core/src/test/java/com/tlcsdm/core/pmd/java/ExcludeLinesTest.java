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

package com.tlcsdm.core.pmd.java;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.java.JavaLanguageModule;
import net.sourceforge.pmd.testframework.RuleTst;
import net.sourceforge.pmd.testframework.TestDescriptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class ExcludeLinesTest extends RuleTst {
    private Rule rule;

    @BeforeEach
    public void setUp() {
        rule = findRule("java-unusedcode", "UnusedLocalVariable");
    }

    @Test
    public void testAcceptance() {
        runTest(new TestDescriptor(TEST1, "NOPMD should work", 0, rule));
        runTest(new TestDescriptor(TEST2, "Should fail without exclude marker", 1, rule));
    }

    @Test
    public void testAlternateMarker() throws Exception {
        PMD p = new PMD();
        p.getConfiguration().setSuppressMarker("FOOBAR");
        RuleContext ctx = new RuleContext();
        Report r = new Report();
        ctx.setReport(r);
        ctx.setSourceCodeFilename("n/a");
        ctx.setLanguageVersion(LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getDefaultVersion());
        RuleSet rules = new RuleSetFactory().createSingleRuleRuleSet(rule);
        p.getSourceCodeProcessor().processSourceCode(new StringReader(TEST3), new RuleSets(rules), ctx);
        Assertions.assertTrue(r.isEmpty());
        Assertions.assertEquals(r.getSuppressedRuleViolations().size(), 1);
    }

    private static final String TEST1 = "public class Foo {" + PMD.EOL + " void foo() {" + PMD.EOL + "  int x; //NOPMD "
        + PMD.EOL + " } " + PMD.EOL + "}";

    private static final String TEST2 = "public class Foo {" + PMD.EOL + " void foo() {" + PMD.EOL + "  int x;"
        + PMD.EOL + " } " + PMD.EOL + "}";

    private static final String TEST3 = "public class Foo {" + PMD.EOL + " void foo() {" + PMD.EOL
        + "  int x; // FOOBAR" + PMD.EOL + " } " + PMD.EOL + "}";
}
