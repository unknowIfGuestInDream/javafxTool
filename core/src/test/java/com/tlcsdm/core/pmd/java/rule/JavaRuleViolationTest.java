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

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersionHandler;
import net.sourceforge.pmd.lang.ParserOptions;
import net.sourceforge.pmd.lang.java.JavaLanguageModule;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.rule.JavaRuleViolation;
import net.sourceforge.pmd.lang.java.symboltable.ScopeAndDeclarationFinder;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * @author Philip Graf
 */
public class JavaRuleViolationTest {
    /**
     * Verifies that {@link JavaRuleViolation} sets the variable name for an
     * {@link ASTFormalParameter} node.
     */
    @Test
    public void testASTFormalParameterVariableName() {
        ASTCompilationUnit ast = parse("class Foo { void bar(int x) {} }");
        final ASTFormalParameter node = ast.getFirstDescendantOfType(ASTFormalParameter.class);
        final RuleContext context = new RuleContext();
        final JavaRuleViolation violation = new JavaRuleViolation(null, context, node, null);
        assertEquals("x", violation.getVariableName());
    }

    private ASTCompilationUnit parse(final String code) {
        final LanguageVersionHandler languageVersionHandler = LanguageRegistry.getLanguage(JavaLanguageModule.NAME)
            .getDefaultVersion().getLanguageVersionHandler();
        final ParserOptions options = languageVersionHandler.getDefaultParserOptions();
        final ASTCompilationUnit ast = (ASTCompilationUnit) languageVersionHandler.getParser(options).parse(null,
            new StringReader(code));
        // set scope of AST nodes
        ast.jjtAccept(new ScopeAndDeclarationFinder(), null);
        return ast;
    }

    /**
     * Tests that the method name is taken correctly from the given node.
     *
     * @see <a href="https://sourceforge.net/p/pmd/bugs/1250/">#1250</a>
     */
    @Test
    public void testMethodName() {
        ASTCompilationUnit ast = parse("class Foo { void bar(int x) {} }");
        ASTMethodDeclaration md = ast.getFirstDescendantOfType(ASTMethodDeclaration.class);
        final RuleContext context = new RuleContext();
        final JavaRuleViolation violation = new JavaRuleViolation(null, context, md, null);
        assertEquals("bar", violation.getMethodName());
    }

    /**
     * Tests that the class name is taken correctly, even if the node is outside
     * of a class scope, e.g. a import declaration.
     *
     * @see <a href="https://sourceforge.net/p/pmd/bugs/1529/">#1529</a>
     */
    @Test
    public void testPackageAndClassName() {
        ASTCompilationUnit ast = parse("package pkg; import java.util.List; public class Foo { }");
        ASTImportDeclaration importNode = ast.getFirstDescendantOfType(ASTImportDeclaration.class);

        JavaRuleViolation violation = new JavaRuleViolation(null, new RuleContext(), importNode, null);
        assertEquals("pkg", violation.getPackageName());
        assertEquals("Foo", violation.getClassName());
    }

    @Test
    public void testPackageAndEnumName() {
        ASTCompilationUnit ast = parse("package pkg; import java.util.List; public enum FooE { }");
        ASTImportDeclaration importNode = ast.getFirstDescendantOfType(ASTImportDeclaration.class);

        JavaRuleViolation violation = new JavaRuleViolation(null, new RuleContext(), importNode, null);
        assertEquals("pkg", violation.getPackageName());
        assertEquals("FooE", violation.getClassName());
    }

    @Test
    public void testDefaultPackageAndClassName() {
        ASTCompilationUnit ast = parse("import java.util.List; public class Foo { }");
        ASTImportDeclaration importNode = ast.getFirstDescendantOfType(ASTImportDeclaration.class);

        JavaRuleViolation violation = new JavaRuleViolation(null, new RuleContext(), importNode, null);
        assertEquals("", violation.getPackageName());
        assertEquals("Foo", violation.getClassName());
    }

    @Test
    public void testPackageAndMultipleClassesName() {
        ASTCompilationUnit ast = parse("package pkg; import java.util.List; class Foo { } public class Bar { }");
        ASTImportDeclaration importNode = ast.getFirstDescendantOfType(ASTImportDeclaration.class);

        JavaRuleViolation violation = new JavaRuleViolation(null, new RuleContext(), importNode, null);
        assertEquals("pkg", violation.getPackageName());
        assertEquals("Bar", violation.getClassName());
    }
}
