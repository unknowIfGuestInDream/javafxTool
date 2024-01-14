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
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * |拼接值.
 *
 * @author unknowIfGuestInDream
 */
public class RegorDirective implements TemplateDirectiveModel {

    private static final String PARAM_NAME_TYPE = "type";
    private static final String TYPE_KEY = "key";
    private static final String TYPE_VALUE = "value";

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws
        TemplateException, IOException {
        if (body == null) {
            return;
        }
        String type = TYPE_VALUE;
        if (params.containsKey(PARAM_NAME_TYPE) && TYPE_KEY.equals(params.get(PARAM_NAME_TYPE).toString())) {
            type = TYPE_KEY;
        }
        body.render(new RegorWriter(env.getOut(), type, env));
    }

    /**
     * 分割值.
     */
    private static class RegorWriter extends Writer {

        private final Writer out;
        private final String type;
        private final Environment env;

        RegorWriter(Writer out, String type, Environment env) {
            this.out = out;
            this.type = type;
            this.env = env;
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
            char[] transformedCbuf = new char[len];
            System.arraycopy(cbuf, off, transformedCbuf, 0, len);
            String[] data = String.valueOf(transformedCbuf).split(",");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                String str = data[i].trim();
                if (str.isEmpty()) {
                    continue;
                }
                if (TYPE_KEY.equals(type)) {
                    try {
                        var dt = env.getDataModel().get(str);
                        if (dt instanceof SimpleScalar d) {
                            str = d.getAsString();
                        }
                    } catch (TemplateModelException e) {
                        StaticLog.warn(e, "Not support the type of '{}'", str);
                        continue;
                    }
                }
                if (str.isEmpty()) {
                    continue;
                }
                if (!stringBuilder.isEmpty()) {
                    char lastC = str.charAt(str.length() - 1);
                    if (lastC == '=' || lastC == '|' || lastC == '&' || lastC == '+' || lastC == '-' || lastC == '*' || lastC == '/') {
                        stringBuilder.append(" ");
                    } else {
                        stringBuilder.append(" | ");
                    }
                }
                stringBuilder.append(str);
            }
            out.write(stringBuilder.toString());
        }

        public void flush() throws IOException {
            out.flush();
        }

        public void close() throws IOException {
            out.close();
        }
    }

}
