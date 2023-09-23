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

package com.tlcsdm.core.util;

import cn.hutool.log.StaticLog;
import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * yui compressor工具实现.
 * 用于js/css压缩
 * line-break Insert a line break after the specified column number
 * verbose Display informational messages and warnings
 * preservehints Don't elide unrecognized compiler hints (e.g. \"use strict\", \"use asm\")
 * nomunge Minify only, do not obfuscate
 * preserve-semi Preserve all semicolons
 * disable-optimizations Disable all micro optimizations
 *
 * @author unknowIfGuestInDream
 */
public class CompressUtil {

    public void compressJS(File js, Writer out) throws Exception {
        compressJS(js, out, -1, true, true, false, false);
    }

    public void compressJS(File js, Writer out, int linebreakpos, boolean munge, boolean verbose, boolean preserveAllSemiColons, boolean disableOptimizations) throws IOException {
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(js), StandardCharsets.UTF_8);) {
            JavaScriptCompressor compressor = new JavaScriptCompressor(in, new ErrorReporter() {
                @Override
                public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
                    System.err.println("[ERROR] in " + js.getAbsolutePath() + line + ':' + lineOffset + ':' + message);
                }

                @Override
                public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
                    System.err.println("[ERROR] in " + js.getAbsolutePath() + line + ':' + lineOffset + ':' + message);
                }

                @Override
                public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
                    error(message, sourceName, line, lineSource, lineOffset);
                    return new EvaluatorException(message);
                }
            });

            compressor.compress(out, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
        }
    }

    public void compressCSS(File css, Writer out) throws Exception {
        compressCSS(css, out, -1);
    }

    public void compressCSS(File css, Writer out, int linebreakpos) throws IOException {
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(css), StandardCharsets.UTF_8);) {
            CssCompressor compressor = new CssCompressor(in);

            compressor.compress(out, linebreakpos);
        }
    }

    private static String yuicompressor(String code) {
        String result = null;
        try (StringWriter writer = new StringWriter();
             InputStream in = new ByteArrayInputStream(code.getBytes());
             Reader reader = new InputStreamReader(in);) {
            JavaScriptCompressor compressor = new JavaScriptCompressor(reader, reporter);
            compressor.compress(writer, -1, true, true, false, false);
            result = writer.toString();
        } catch (EvaluatorException | IOException e) {
            StaticLog.error(e);
        }
        return result;
    }

    private static final ErrorReporter reporter = new ErrorReporter() {
        @Override
        public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
            if (line < 0)
                StaticLog.warn(message);
            else
                StaticLog.warn(line + ':' + lineOffset + ':' + message);
        }

        @Override
        public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
            if (line < 0)
                StaticLog.error(message);
            else
                StaticLog.error(line + ':' + lineOffset + ':' + message);
        }

        @Override
        public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource,
                                               int lineOffset) {
            error(message, sourceName, line, lineSource, lineOffset);
            return new EvaluatorException(message);
        }
    };

}
