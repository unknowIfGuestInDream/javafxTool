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
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.java.JavaLanguageModule;
import net.sourceforge.pmd.testframework.RuleTst;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReportTest extends RuleTst {

    private LanguageVersion defaultLanguage = LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getDefaultVersion();

    @Test
    public void testBasic() {
        Report r = new Report();
        runTestFromString(TEST1, new FooRule(), r, defaultLanguage);
        assertFalse(r.isEmpty());
    }

    @Test
    public void testExclusionsInReportWithRuleViolationSuppressRegex() {
        Report rpt = new Report();
        Rule rule = new FooRule();
        rule.setProperty(Rule.VIOLATION_SUPPRESS_REGEX_DESCRIPTOR, ".*No Foo.*");
        runTestFromString(TEST1, rule, rpt, defaultLanguage);
        assertTrue(rpt.isEmpty());
        assertEquals(1, rpt.getSuppressedRuleViolations().size());
    }

    @Test
    @Ignore
    public void testExclusionsInReportWithRuleViolationSuppressXPath() {
        Report rpt = new Report();
        Rule rule = new FooRule();
        rule.setProperty(Rule.VIOLATION_SUPPRESS_XPATH_DESCRIPTOR, ".[@Image = 'Foo']");
        runTestFromString(TEST1, rule, rpt, defaultLanguage);
        assertTrue(rpt.isEmpty());
        assertEquals(1, rpt.getSuppressedRuleViolations().size());
    }

    @Test
    public void testExclusionsInReportWithAnnotations() {
        Report rpt = new Report();
        runTestFromString(TEST2, new FooRule(), rpt,
            LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getVersion("1.5"));
        assertTrue(rpt.isEmpty());
        assertEquals(1, rpt.getSuppressedRuleViolations().size());
    }

    @Test
    public void testExclusionsInReportWithAnnotationsFullName() {
        Report rpt = new Report();
        runTestFromString(TEST2_FULL, new FooRule(), rpt,
            LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getVersion("1.5"));
        assertTrue(rpt.isEmpty());
        assertEquals(1, rpt.getSuppressedRuleViolations().size());
    }

    @Test
    public void testExclusionsInReportWithNOPMD() {
        Report rpt = new Report();
        runTestFromString(TEST3, new FooRule(), rpt, defaultLanguage);
        assertTrue(rpt.isEmpty());
        assertEquals(1, rpt.getSuppressedRuleViolations().size());
    }

    private static final String TEST1 = "public class Foo {}" + PMD.EOL;

    private static final String TEST2 = "@SuppressWarnings(\"PMD\")" + PMD.EOL + "public class Foo {}";
    private static final String TEST2_FULL = "@java.lang.SuppressWarnings(\"PMD\")" + PMD.EOL + "public class Foo {}";

    private static final String TEST3 = "public class Foo {} // NOPMD";
}
