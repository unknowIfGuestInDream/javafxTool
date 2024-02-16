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

package com.tlcsdm.core.pmd.java.rule;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDException;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.Parser;
import net.sourceforge.pmd.lang.ParserOptions;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.JavaLanguageModule;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.rule.XPathRule;
import net.sourceforge.pmd.lang.rule.xpath.JaxenXPathRuleQuery;
import net.sourceforge.pmd.lang.rule.xpath.SaxonXPathRuleQuery;
import net.sourceforge.pmd.lang.rule.xpath.XPathRuleQuery;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.StringMultiProperty;
import net.sourceforge.pmd.properties.StringProperty;
import net.sourceforge.pmd.testframework.RuleTst;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author daniels
 */
public class XPathRuleTest extends RuleTst {

    XPathRule rule;

    @BeforeEach
    public void setUp() {
        rule = new XPathRule();
        rule.setLanguage(LanguageRegistry.getLanguage(JavaLanguageModule.NAME));
        rule.setMessage("XPath Rule Failed");
    }

    @Test
    public void testPluginname() throws Exception {
        rule.setXPath("//VariableDeclaratorId[string-length(@Image) < 3]");
        rule.setMessage("{0}");
        Report report = getReportForTestString(rule, TEST1);
        RuleViolation rv = report.iterator().next();
        Assertions.assertEquals("a", rv.getDescription());
    }

    @Test
    public void testXPathMultiProperty() throws Exception {
        rule.setXPath("//VariableDeclaratorId[@Image=$forbiddenNames]");
        rule.setMessage("Avoid vars");
        rule.setVersion(XPathRuleQuery.XPATH_2_0);
        StringMultiProperty varDescriptor
            = StringMultiProperty.named("forbiddenNames")
            .desc("Forbidden names")
            .defaultValues("forbid1", "forbid2")
            .delim('$')
            .build();

        rule.definePropertyDescriptor(varDescriptor);

        Report report = getReportForTestString(rule, TEST3);
        Iterator<RuleViolation> rv = report.iterator();
        int i = 0;
        for (; rv.hasNext(); ++i) {
            rv.next();
        }
        Assertions.assertEquals(2, i);
    }

    @Test
    public void testVariables() throws Exception {
        rule.setXPath("//VariableDeclaratorId[@Image=$var]");
        rule.setMessage("Avoid vars");
        StringProperty varDescriptor = new StringProperty("var", "Test var", null, 1.0f);
        rule.definePropertyDescriptor(varDescriptor);
        rule.setProperty(varDescriptor, "fiddle");
        Report report = getReportForTestString(rule, TEST2);
        RuleViolation rv = report.iterator().next();
        Assertions.assertEquals(3, rv.getBeginLine());
    }

    /**
     * Test for problem reported in bug #1219 PrimarySuffix/@Image does not work
     * in some cases in xpath 2.0
     *
     * @throws Exception any error
     */
    @Test
    public void testImageOfPrimarySuffix() throws Exception {
        final String SUFFIX = "import java.io.File;\n" + "\n" + "public class TestSuffix {\n"
            + "    public static void main(String args[]) {\n" + "        new File(\"subdirectory\").list();\n"
            + "    }\n" + "}";
        LanguageVersion language = LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getDefaultVersion();
        ParserOptions parserOptions = language.getLanguageVersionHandler().getDefaultParserOptions();
        Parser parser = language.getLanguageVersionHandler().getParser(parserOptions);
        ASTCompilationUnit cu = (ASTCompilationUnit) parser.parse("test", new StringReader(SUFFIX));
        RuleContext ruleContext = new RuleContext();
        ruleContext.setLanguageVersion(language);

        String xpath = "//PrimarySuffix[@Image='list']";

        // XPATH version 1.0
        XPathRuleQuery xpathRuleQuery = new JaxenXPathRuleQuery();
        xpathRuleQuery.setXPath(xpath);
        xpathRuleQuery.setProperties(new HashMap<PropertyDescriptor<?>, Object>());
        xpathRuleQuery.setVersion(XPathRuleQuery.XPATH_1_0);
        List<Node> nodes = xpathRuleQuery.evaluate(cu, ruleContext);
        Assertions.assertEquals(1, nodes.size());

        // XPATH version 1.0 Compatibility
        xpathRuleQuery = new SaxonXPathRuleQuery();
        xpathRuleQuery.setXPath(xpath);
        xpathRuleQuery.setProperties(new HashMap<PropertyDescriptor<?>, Object>());
        xpathRuleQuery.setVersion(XPathRuleQuery.XPATH_1_0_COMPATIBILITY);
        nodes = xpathRuleQuery.evaluate(cu, ruleContext);
        Assertions.assertEquals(1, nodes.size());

        // XPATH version 2.0
        xpathRuleQuery = new SaxonXPathRuleQuery();
        xpathRuleQuery.setXPath(xpath);
        xpathRuleQuery.setProperties(new HashMap<PropertyDescriptor<?>, Object>());
        xpathRuleQuery.setVersion(XPathRuleQuery.XPATH_2_0);
        nodes = xpathRuleQuery.evaluate(cu, ruleContext);
        Assertions.assertEquals(1, nodes.size());
    }

    /**
     * Following sibling check: See https://sourceforge.net/p/pmd/bugs/1209/
     *
     * @throws Exception any error
     */
    @Test
    public void testFollowingSibling() throws Exception {
        final String SOURCE = "public class dummy {\n" + "  public String toString() {\n"
            + "    String test = \"bad example\";\n" + "    test = \"a\";\n" + "    return test;\n" + "  }\n" + "}";
        LanguageVersion language = LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getDefaultVersion();
        ParserOptions parserOptions = language.getLanguageVersionHandler().getDefaultParserOptions();
        Parser parser = language.getLanguageVersionHandler().getParser(parserOptions);
        ASTCompilationUnit cu = (ASTCompilationUnit) parser.parse("test", new StringReader(SOURCE));
        RuleContext ruleContext = new RuleContext();
        ruleContext.setLanguageVersion(language);

        String xpath = "//Block/BlockStatement/following-sibling::BlockStatement";

        // XPATH version 1.0
        XPathRuleQuery xpathRuleQuery = new JaxenXPathRuleQuery();
        xpathRuleQuery.setXPath(xpath);
        xpathRuleQuery.setProperties(new HashMap<PropertyDescriptor<?>, Object>());
        xpathRuleQuery.setVersion(XPathRuleQuery.XPATH_1_0);
        List<Node> nodes = xpathRuleQuery.evaluate(cu, ruleContext);
        Assertions.assertEquals(2, nodes.size());
        Assertions.assertEquals(4, nodes.get(0).getBeginLine());
        Assertions.assertEquals(5, nodes.get(1).getBeginLine());

        // XPATH version 2.0
        xpathRuleQuery = new SaxonXPathRuleQuery();
        xpathRuleQuery.setXPath(xpath);
        xpathRuleQuery.setProperties(new HashMap<PropertyDescriptor<?>, Object>());
        xpathRuleQuery.setVersion(XPathRuleQuery.XPATH_2_0);
        nodes = xpathRuleQuery.evaluate(cu, ruleContext);
        Assertions.assertEquals(2, nodes.size());
        Assertions.assertEquals(4, nodes.get(0).getBeginLine());
        Assertions.assertEquals(5, nodes.get(1).getBeginLine());
    }

    private static Report getReportForTestString(Rule r, String test) throws PMDException {
        PMD p = new PMD();
        RuleContext ctx = new RuleContext();
        Report report = new Report();
        ctx.setReport(report);
        ctx.setSourceCodeFilename("n/a");
        RuleSet rules = new RuleSetFactory().createSingleRuleRuleSet(r);
        p.getSourceCodeProcessor().processSourceCode(new StringReader(test), new RuleSets(rules), ctx);
        return report;
    }

    private static final String TEST1 = "public class Foo {" + PMD.EOL + " int a;" + PMD.EOL + "}";

    private static final String TEST2 = "public class Foo {" + PMD.EOL + " int faddle;" + PMD.EOL + " int fiddle;"
        + PMD.EOL + "}";

    private static final String TEST3 = "public class Foo {" + PMD.EOL + " int forbid1; int forbid2; int forbid1$forbid2;" + PMD.EOL + "}";

}
