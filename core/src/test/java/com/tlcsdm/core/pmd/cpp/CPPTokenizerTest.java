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

package com.tlcsdm.core.pmd.cpp;

import cn.hutool.core.io.resource.ResourceUtil;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.cpd.CPPTokenizer;
import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.TokenEntry;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.Tokens;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

public class CPPTokenizerTest {

    @Test
    public void testUTFwithBOM() throws IOException {
        Tokens tokens = parse("\ufeffint start()\n{ int ret = 1;\nreturn ret;\n}\n");
        Assertions.assertNotSame(TokenEntry.getEOF(), tokens.getTokens().get(0));
        Assertions.assertEquals(15, tokens.size());
    }

    @Test
    public void testUnicodeSupport() throws IOException {
        String code = "\ufeff" + "#include <iostream>\n" + "#include <string>\n" + "\n" + "// example\n" + "\n"
            + "int main()\n" + "{\n" + "    std::string text(\"ąęćśźńó\");\n" + "    std::cout << text;\n"
            + "    return 0;\n" + "}\n";
        Tokens tokens = parse(code);
        Assertions.assertNotSame(TokenEntry.getEOF(), tokens.getTokens().get(0));
        Assertions.assertEquals(24, tokens.size());
    }

    @Test
    public void testIgnoreBetweenSpecialComments() throws IOException {
        String code = "#include <iostream>\n" + "#include <string>\n" + "\n" + "// CPD-OFF\n"
            + "int main()\n" + "{\n" + "    std::string text(\"ąęćśźńó\");\n" + "    std::cout << text;\n"
            + "    return 0;\n" + "// CPD-ON\n" + "}\n";
        Tokens tokens = parse(code);
        Assertions.assertNotSame(TokenEntry.getEOF(), tokens.getTokens().get(0));
        Assertions.assertEquals(2, tokens.size()); // "}" + EOF
    }

    @Test
    public void testMultiLineMacros() throws IOException {
        Tokens tokens = parse(TEST1);
        Assertions.assertEquals(7, tokens.size());
    }

    @Test
    public void testDollarSignInIdentifier() throws IOException {
        parse(TEST2);
    }

    @Test
    public void testDollarSignStartingIdentifier() throws IOException {
        parse(TEST3);
    }

    @Test
    public void testWideCharacters() throws IOException {
        parse(TEST4);
    }

    @Test
    public void testTokenizerWithSkipBlocks() throws Exception {
        String test = IOUtils.toString(ResourceUtil.getResourceObj("pmd/cpp/cpp_with_asm.cpp").getStream());
        Tokens tokens = parse(test, true);
        Assertions.assertEquals(19, tokens.size());
    }

    @Test
    @Disabled
    public void testTokenizerWithSkipBlocksPattern() throws Exception {
        String test = IOUtils.toString(ResourceUtil.getResourceObj("pmd/cpp/cpp_with_asm.cpp").getStream());
        Tokens tokens = parse(test, true, "#if debug|#endif");
        Assertions.assertEquals(31, tokens.size());
    }

    @Test
    @Disabled
    public void testTokenizerWithoutSkipBlocks() throws Exception {
        String test = IOUtils.toString(ResourceUtil.getResourceObj("pmd/cpp/cpp_with_asm.cpp").getStream());
        Tokens tokens = parse(test, false);
        Assertions.assertEquals(37, tokens.size());
    }

    @Test
    // ASM code containing the '@' character
    public void testAsmWithAtSign() throws IOException {
        Tokens tokens = parse(TEST7);
        Assertions.assertEquals(22, tokens.size());
    }

    @Test
    public void testEOLCommentInPreprocessingDirective() throws IOException {
        parse("#define LSTFVLES_CPP  //*" + PMD.EOL);
    }

    @Test
    @Disabled
    public void testEmptyCharacter() throws IOException {
        Tokens tokens = parse("std::wstring wsMessage( sMessage.length(), L'');" + PMD.EOL);
        Assertions.assertEquals(15, tokens.size());
    }

    @Test
    public void testHexCharacter() throws IOException {
        Tokens tokens = parse("if (*pbuf == '\\0x05')" + PMD.EOL);
        Assertions.assertEquals(8, tokens.size());
    }

    @Test
    public void testWhiteSpaceEscape() throws IOException {
        Tokens tokens = parse("szPath = m_sdcacheDir + _T(\"\\    oMedia\");" + PMD.EOL);
        Assertions.assertEquals(10, tokens.size());
    }

    @Test
    public void testRawStringLiteral() throws IOException {
        String code = "const char* const KDefaultConfig = R\"(\n" + "    [Sinks.1]\n" + "    Destination=Console\n"
            + "    AutoFlush=true\n"
            + "    Format=\"[%TimeStamp%] %ThreadId% %QueryIdHigh% %QueryIdLow% %LoggerFile%:%Line% (%Severity%) - %Message%\"\n"
            + "    Filter=\"%Severity% >= WRN\"\n" + ")\";\n";
        Tokens tokens = parse(code);
        Assertions.assertNotSame(TokenEntry.getEOF(), tokens.getTokens().get(0));
        Assertions.assertEquals(9, tokens.size());
    }

    private Tokens parse(String snippet) throws IOException {
        return parse(snippet, false);
    }

    private Tokens parse(String snippet, boolean skipBlocks) throws IOException {
        return parse(snippet, skipBlocks, null);
    }

    private Tokens parse(String snippet, boolean skipBlocks, String skipPattern) throws IOException {
        Properties properties = new Properties();
        properties.setProperty(Tokenizer.OPTION_SKIP_BLOCKS, Boolean.toString(skipBlocks));
        if (skipPattern != null) {
            properties.setProperty(Tokenizer.OPTION_SKIP_BLOCKS_PATTERN, skipPattern);
        }

        CPPTokenizer tokenizer = new CPPTokenizer();
        tokenizer.setProperties(properties);

        SourceCode code = new SourceCode(new SourceCode.StringCodeLoader(snippet));
        Tokens tokens = new Tokens();
        tokenizer.tokenize(code, tokens);
        return tokens;
    }

    private static final String TEST1 = "#define FOO a +\\" + PMD.EOL + "            b +\\" + PMD.EOL
        + "            c +\\" + PMD.EOL + "            d +\\" + PMD.EOL + "            e +\\" + PMD.EOL
        + "            f +\\" + PMD.EOL + "            g" + PMD.EOL + " void main() {}";

    private static final String TEST2 = " void main() { int x$y = 42; }";

    private static final String TEST3 = " void main() { int $x = 42; }";

    private static final String TEST4 = " void main() { char x = L'a'; }";

    private static final String TEST7 = "asm void eSPI_boot()" + PMD.EOL + "{" + PMD.EOL + "  // setup stack pointer"
        + PMD.EOL + "  lis r1, _stack_addr@h" + PMD.EOL + "  ori r1, r1, _stack_addr@l" + PMD.EOL + "}";
}
