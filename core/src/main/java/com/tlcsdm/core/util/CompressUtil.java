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
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * yui compressor工具实现.
 * 用于js/css压缩
 *
 * @author unknowIfGuestInDream
 */
public final class CompressUtil {

    private CompressUtil() {
        // Do nothing
    }

    public static void compressJS(File js, Writer out) {
        compressJS(js, out, -1, true, false, false, false);
    }

    /**
     * 压缩js到输出流.
     *
     * @param js                    js文件
     * @param out                   输出流
     * @param linebreakpos          Insert a line break after the specified column number
     * @param munge                 Whether obfuscate
     * @param verbose               Display informational messages and warnings
     * @param preserveAllSemiColons Preserve all semicolons
     * @param disableOptimizations  Disable all micro optimizations
     */
    public static void compressJS(File js, Writer out, int linebreakpos, boolean munge, boolean verbose, boolean preserveAllSemiColons,
                                  boolean disableOptimizations) {
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(js), StandardCharsets.UTF_8);) {
            JavaScriptCompressor compressor = new JavaScriptCompressor(in, ERROR_REPORTER);
            compressor.compress(out, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
        } catch (IOException | EvaluatorException e) {
            StaticLog.error(e);
        }
    }

    public static String compressJS(String code) {
        return compressJS(code, -1, true, false, false, false);
    }

    /**
     * @param code 待压缩的代码.
     */
    public static String compressJS(String code, int linebreakpos, boolean munge, boolean verbose, boolean preserveAllSemiColons,
                                    boolean disableOptimizations) {
        try (InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(code.getBytes()))) {
            StringWriter writer = new StringWriter();
            JavaScriptCompressor compressor = new JavaScriptCompressor(in, ERROR_REPORTER);
            compressor.compress(writer, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
            return writer.toString();
        } catch (IOException | EvaluatorException e) {
            StaticLog.error(e);
        }
        return "";
    }

    public static void compressCSS(File css, Writer out) {
        compressCSS(css, out, -1);
    }

    /**
     * @param css          css文件
     * @param out          输出流
     * @param linebreakpos Insert a line break after the specified column number
     */
    public static void compressCSS(File css, Writer out, int linebreakpos) {
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(css), StandardCharsets.UTF_8)) {
            CssCompressor compressor = new CssCompressor(in);
            compressor.compress(out, linebreakpos);
        } catch (IOException e) {
            StaticLog.error(e);
        }
    }

    public static String compressCSS(String code) {
        return compressCSS(code, -1);
    }

    /**
     * @param code 待压缩的代码.
     */
    public static String compressCSS(String code, int linebreakpos) {
        try (InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(code.getBytes()))) {
            CssCompressor compressor = new CssCompressor(in);
            StringWriter writer = new StringWriter();
            compressor.compress(writer, linebreakpos);
            return writer.toString();
        } catch (IOException e) {
            StaticLog.error(e);
        }
        return "";
    }

    private static final ErrorReporter ERROR_REPORTER = new ErrorReporter() {
        @Override
        public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
            if (line < 0) {
                StaticLog.warn(message);
            } else {
                StaticLog.warn(line + ':' + lineOffset + ':' + message);
            }
        }

        @Override
        public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
            if (line < 0) {
                StaticLog.error(message);
            } else {
                StaticLog.error(line + ':' + lineOffset + ':' + message);
            }
        }

        @Override
        public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
            error(message, sourceName, line, lineSource, lineOffset);
            return new EvaluatorException(message);
        }
    };

}
