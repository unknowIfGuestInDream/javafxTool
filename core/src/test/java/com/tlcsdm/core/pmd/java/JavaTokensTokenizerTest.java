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
import net.sourceforge.pmd.cpd.JavaTokenizer;
import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.TokenEntry;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.Tokens;
import net.sourceforge.pmd.lang.java.ast.JavaParserConstants;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JavaTokensTokenizerTest {

    @Test
    public void test1() throws IOException {
        Tokenizer tokenizer = new JavaTokenizer();
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader("public class Foo {}"));
        Tokens tokens = new Tokens();
        tokenizer.tokenize(sourceCode, tokens);
        assertEquals(6, tokens.size());
        assertEquals("public class Foo {}", sourceCode.getSlice(1, 1));
    }

    @Test
    public void testCommentsIgnored() throws IOException {
        Tokenizer tokenizer = new JavaTokenizer();
        SourceCode sourceCode = new SourceCode(
            new SourceCode.StringCodeLoader("public class Foo { // class Bar */ \n }"));
        Tokens tokens = new Tokens();
        tokenizer.tokenize(sourceCode, tokens);
        assertEquals(6, tokens.size());
    }

    @Test
    @Ignore
    /*
      换行符有差异.
     */
    public void test2() throws IOException {
        Tokenizer t = new JavaTokenizer();
        String data = "public class Foo {" + PMD.EOL + "public void bar() {}" + PMD.EOL + "public void buz() {}"
            + PMD.EOL + "}";
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader(data));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        assertEquals("public class Foo {" + PMD.EOL + "public void bar() {}", sourceCode.getSlice(1, 2));
    }

    @Test
    public void testDiscardSemicolons() throws IOException {
        Tokenizer t = new JavaTokenizer();
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader("public class Foo {private int x;}"));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        assertEquals(9, tokens.size());
    }

    @Test
    public void testDiscardImports() throws IOException {
        Tokenizer t = new JavaTokenizer();
        SourceCode sourceCode = new SourceCode(
            new SourceCode.StringCodeLoader("import java.io.File;" + PMD.EOL + "public class Foo {}"));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        assertEquals(6, tokens.size());
    }

    @Test
    public void testDiscardPkgStmts() throws IOException {
        Tokenizer t = new JavaTokenizer();
        SourceCode sourceCode = new SourceCode(
            new SourceCode.StringCodeLoader("package foo.bar.baz;" + PMD.EOL + "public class Foo {}"));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        assertEquals(6, tokens.size());
    }

    @Test
    public void testDiscardSimpleOneLineAnnotation() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(true);
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader(
            "package foo.bar.baz;" + PMD.EOL + "@MyAnnotation" + PMD.EOL + "public class Foo {}"));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        assertEquals(6, tokens.size());
    }

    @Test
    public void testIgnoreComments() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(false);
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader("package foo.bar.baz;" + PMD.EOL
            + "/*****" + PMD.EOL + " * ugh" + PMD.EOL + " *****/" + PMD.EOL + "public class Foo {}"));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        assertEquals(6, tokens.size());
    }

    @Test
    public void testDiscardOneLineAnnotationWithParams() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(true);

        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader(
            "package foo.bar.baz;" + PMD.EOL + "@ MyAnnotation (\"ugh\")" + PMD.EOL + "@NamedQueries({" + PMD.EOL
                + "@NamedQuery(" + PMD.EOL + ")})" + PMD.EOL + "public class Foo {" + PMD.EOL + "}"));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        TokenEntry.getEOF();
        assertEquals(6, tokens.size());
    }

    @Test
    public void testIgnoreBetweenSpecialComments() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(false);
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader("package foo.bar.baz;" + PMD.EOL
            + "// CPD-OFF" + PMD.EOL + "// CPD-OFF" + PMD.EOL
            + "@ MyAnnotation (\"ugh\")" + PMD.EOL + "@NamedQueries({" + PMD.EOL + "@NamedQuery(" + PMD.EOL + ")})"
            + PMD.EOL + "public class Foo {" + "// CPD-ON" + PMD.EOL
            + "}"
        ));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        TokenEntry.getEOF();
        assertEquals(2, tokens.size()); // 2 tokens: "}" + EOF
    }

    @Test
    public void testIgnoreBetweenSpecialCommentsMultiple() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(false);
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader("package foo.bar.baz;" + PMD.EOL
            + "// CPD-OFF" + PMD.EOL + "// another irrelevant comment" + PMD.EOL
            + "@ MyAnnotation (\"ugh\")" + PMD.EOL + "@NamedQueries({" + PMD.EOL + "@NamedQuery(" + PMD.EOL + ")})"
            + PMD.EOL + "public class Foo {" + "// CPD-ON" + PMD.EOL
            + "}"
        ));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        TokenEntry.getEOF();
        assertEquals(2, tokens.size()); // 2 tokens: "}" + EOF
    }

    @Test
    public void testIgnoreBetweenSpecialCommentsMultiline() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(false);
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader("package foo.bar.baz;" + PMD.EOL
            + "/* " + PMD.EOL + " * CPD-OFF" + PMD.EOL + "*/" + PMD.EOL
            + "@ MyAnnotation (\"ugh\")" + PMD.EOL + "@NamedQueries({" + PMD.EOL + "@NamedQuery(" + PMD.EOL + ")})"
            + PMD.EOL + "public class Foo {" + PMD.EOL
            + "/* " + PMD.EOL + " * CPD-ON" + PMD.EOL + "*/" + PMD.EOL
            + "}"
        ));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        TokenEntry.getEOF();
        assertEquals(2, tokens.size()); // 2 tokens: "}" + EOF
    }

    @Test
    public void testIgnoreBetweenSpecialAnnotation() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(false);
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader("package foo.bar.baz;" + PMD.EOL
            + "@SuppressWarnings({\"woof\",\"CPD-START\"})" + PMD.EOL + "@SuppressWarnings(\"CPD-START\")" + PMD.EOL
            + "@ MyAnnotation (\"ugh\")" + PMD.EOL + "@NamedQueries({" + PMD.EOL + "@NamedQuery(" + PMD.EOL + ")})"
            + PMD.EOL + "public class Foo {}" + "@SuppressWarnings({\"ugh\",\"CPD-END\"})" + PMD.EOL

        ));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        TokenEntry.getEOF();
        assertEquals(10, tokens.size());
    }

    @Test
    public void testIgnoreBetweenSpecialAnnotationAndIgnoreAnnotations() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(true);
        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader("package foo.bar.baz;" + PMD.EOL
            + "@SuppressWarnings({\"woof\",\"CPD-START\"})" + PMD.EOL + "@SuppressWarnings(\"CPD-START\")" + PMD.EOL
            + "@ MyAnnotation (\"ugh\")" + PMD.EOL + "@NamedQueries({" + PMD.EOL + "@NamedQuery(" + PMD.EOL + ")})"
            + PMD.EOL + "public class Foo {}" + PMD.EOL + "@SuppressWarnings({\"ugh\",\"CPD-END\"})" + PMD.EOL

        ));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        TokenEntry.getEOF();
        assertEquals(1, tokens.size());
    }

    @Test
    public void testIgnoreIdentifiersDontAffectConstructors() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(false);
        t.setIgnoreIdentifiers(true);

        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader("package foo.bar.baz;" + PMD.EOL
            + "public class Foo extends Bar {" + PMD.EOL + "private Foo notAConstructor;" + PMD.EOL
            + "public Foo(int i) { super(i); }" + PMD.EOL + "private Foo(int i, String s) { super(i, s); }"
            + PMD.EOL + "/* default */ Foo(int i, String s, Object o) { super(i, s, o); }" + PMD.EOL
            + "private static class Inner {" + PMD.EOL + "Inner() { System.out.println(\"Guess who?\"); }" + PMD.EOL
            + "}" + PMD.EOL + "}" + PMD.EOL

        ));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        TokenEntry.getEOF();
        List<TokenEntry> tokenList = tokens.getTokens();

        // Member variable of type Foo
        assertEquals(String.valueOf(JavaParserConstants.IDENTIFIER), tokenList.get(7).toString());
        // Public constructor
        assertEquals("Foo", tokenList.get(10).toString());
        // Private constructor
        assertEquals("Foo", tokenList.get(22).toString());
        // Package-private constructor
        assertEquals("Foo", tokenList.get(38).toString());
        // Inner class constructor
        assertEquals("Inner", tokenList.get(64).toString());
    }

    @Test
    public void testIgnoreIdentifiersHandlesEnums() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(false);
        t.setIgnoreIdentifiers(true);

        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader(
            "package foo.bar.baz;" + PMD.EOL + "public enum Foo {" + PMD.EOL + "BAR(1)," + PMD.EOL + "BAZ(2);"
                + PMD.EOL + "Foo(int val) {" + PMD.EOL + "}" + PMD.EOL + "}" + PMD.EOL

        ));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        TokenEntry.getEOF();
        List<TokenEntry> tokenList = tokens.getTokens();

        // Enum member
        assertEquals(String.valueOf(JavaParserConstants.IDENTIFIER), tokenList.get(4).toString());
        assertEquals(String.valueOf(JavaParserConstants.IDENTIFIER), tokenList.get(9).toString());
        // Enum constructor
        assertEquals("Foo", tokenList.get(13).toString());
    }

    @Test
    public void testIgnoreIdentifiersWithClassKeyword() throws IOException {
        JavaTokenizer t = new JavaTokenizer();
        t.setIgnoreAnnotations(false);
        t.setIgnoreIdentifiers(true);

        SourceCode sourceCode = new SourceCode(new SourceCode.StringCodeLoader(
            "package foo.bar.baz;" + PMD.EOL + "public class Foo {" + PMD.EOL + "Foo() {" + PMD.EOL
                + "}" + PMD.EOL + "public void bar() {" + PMD.EOL + "Bar.baz(Foo.class, () -> {});"
                + PMD.EOL + "}" + PMD.EOL + "}" + PMD.EOL
        ));
        Tokens tokens = new Tokens();
        t.tokenize(sourceCode, tokens);
        TokenEntry.getEOF();
        List<TokenEntry> tokenList = tokens.getTokens();

        // Class constructor
        assertEquals("Foo", tokenList.get(4).toString());
        assertEquals(String.valueOf(JavaParserConstants.IDENTIFIER), tokenList.get(11).toString());
    }
}
