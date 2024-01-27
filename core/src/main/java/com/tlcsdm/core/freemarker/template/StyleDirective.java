/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.freemarker.template;

import cn.hutool.log.StaticLog;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 格式化.
 *
 * @author unknowIfGuestInDream
 */
public class StyleDirective implements TemplateDirectiveModel {
    private static final String PARAM_NAME_TYPE = "type";
    private static final String TYPE_LINELENGTH120 = "linelength120";

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws
        TemplateException, IOException {
        if (body == null) {
            return;
        }
        String type = "";
        if (params.containsKey(PARAM_NAME_TYPE) && TYPE_LINELENGTH120.equals(params.get(PARAM_NAME_TYPE).toString())) {
            type = TYPE_LINELENGTH120;
        }
        if (TYPE_LINELENGTH120.equals(type)) {
            body.render(new Line120Writer(env.getOut()));
        } else {
            StaticLog.warn("Unknown style type: " + type);
            body.render(env.getOut());
        }
    }

    /**
     * 格式化长度最长为120.
     */
    private static class Line120Writer extends Writer {
        private final int lineLength = 120;
        private final Writer out;
        private final String sysType = System.lineSeparator();
        private final Pattern matchRegex = Pattern.compile("^\\s*[\\w.\\->\\[\\]()*]+\\s*(=|\\|=|&=).+;$");
        private int strCount = 0;
        private int spaceStartPlace = 0;
        private boolean isComment = false;
        private String tmpData = "";

        Line120Writer(Writer out) {
            this.out = out;
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
            char[] transformedCbuf = new char[len];
            System.arraycopy(cbuf, off, transformedCbuf, 0, len);
            String data = String.valueOf(transformedCbuf);
            if (strCount == 0) {
                spaceStartPlace = 0;
                for (int i = 0; i < data.length(); i++) {
                    if ((data.charAt(i) != ' ')) {
                        spaceStartPlace++;
                        break;
                    }
                }
                String tmp = data.trim();
                if (tmp.startsWith("//") || tmp.startsWith("/*") || tmp.startsWith("*")) {
                    isComment = true;
                } else {
                    isComment = false;
                }
            }

            if (data.contains("\n")) {
                // todo
                if (strCount + data.length() > lineLength) {
                    String[] strs = data.split("\n");
                    for (int i = 0; i < strs.length; i++) {
                        if (i == 0) {
                            if (strCount + strs[0].length() > lineLength) {
                                // todo
                            }
                            continue;
                        }
                        if (strs[i].length() > lineLength) {
                            // todo
                        }
                    }
                }
            } else {

            }
            strCount += data.length();
            out.write(data);
        }

        public void flush() throws IOException {
            out.flush();
        }

        public void close() throws IOException {
            out.close();
        }
    }

}
