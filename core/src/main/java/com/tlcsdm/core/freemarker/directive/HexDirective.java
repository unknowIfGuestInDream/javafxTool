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

package com.tlcsdm.core.freemarker.directive;

import cn.hutool.log.StaticLog;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * 进制转换.
 *
 * @author unknowIfGuestInDream
 */
public class HexDirective implements TemplateDirectiveModel {

    private static final String PARAM_NAME_TARGET = "target";
    private static final String PARAM_NAME_LENGTH = "length";

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws
        TemplateException, IOException {
        if (body == null) {
            return;
        }
        int targetParam = 0;
        int lengthParam = 0;
        boolean targetParamSet = false;

        var paramIter = params.entrySet().iterator();
        while (paramIter.hasNext()) {
            Map.Entry ent = (Map.Entry) paramIter.next();
            String paramName = (String) ent.getKey();
            TemplateModel paramValue = (TemplateModel) ent.getValue();
            if (paramName.equals(PARAM_NAME_TARGET)) {
                if (!(paramValue instanceof TemplateNumberModel)) {
                    throw new TemplateModelException("The " + PARAM_NAME_TARGET + " parameter " + "must be a number.");
                }
                targetParam = ((TemplateNumberModel) paramValue).getAsNumber().intValue();
                targetParamSet = true;
                if (targetParam <= 0) {
                    throw new TemplateModelException(
                        "The \"" + PARAM_NAME_TARGET + "\" parameter " + "can't be negative.");
                }
            } else if (paramName.equals(PARAM_NAME_LENGTH)) {
                if (!(paramValue instanceof TemplateNumberModel)) {
                    throw new TemplateModelException(
                        "The \"" + PARAM_NAME_LENGTH + "\" parameter " + "must be a number.");
                }
                lengthParam = ((TemplateNumberModel) paramValue).getAsNumber().intValue();
            } else {
                throw new TemplateModelException("Unsupported parameter: " + paramName);
            }
        }
        if (!targetParamSet) {
            throw new TemplateModelException("The required \"" + PARAM_NAME_TARGET + "\" paramter" + "is missing.");
        }
        if (loopVars.length > 1) {
            throw new TemplateModelException("At most one loop variable is allowed.");
        }
        body.render(new HexWriter(env.getOut(), targetParam, lengthParam));
    }

    private static class HexWriter extends Writer {

        private final Writer out;
        private final int target;
        private final int length;

        HexWriter(Writer out, int target, int length) {
            this.out = out;
            this.target = target;
            this.length = length;
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
            char[] transformedCbuf = new char[len];
            for (int i = 0; i < len; i++) {
                transformedCbuf[i] = cbuf[i + off];
            }
            String data = String.valueOf(transformedCbuf);
            try {
                int i = Integer.parseInt(data);
                String s = "";
                if (target == 2) {
                    s = Integer.toBinaryString(i);
                } else if (target == 8) {
                    s = Integer.toOctalString(i);
                } else if (target == 16) {
                    s = Integer.toHexString(i);
                }
                if (length > s.length()) {
                    data = String.format("%0" + length + "d", Integer.parseInt(s));
                }
            } catch (NumberFormatException e) {
                StaticLog.error(e);
            }
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
