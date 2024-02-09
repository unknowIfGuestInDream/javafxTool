/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.freemarker.format;

import freemarker.core.Environment;
import freemarker.core.InvalidFormatParametersException;
import freemarker.core.TemplateFormatUtil;
import freemarker.core.TemplateNumberFormat;
import freemarker.core.TemplateNumberFormatFactory;
import freemarker.core.UnformattableValueException;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

import java.util.Locale;

/**
 * @author unknowIfGuestInDream
 */
public class LocaleSensitiveTemplateNumberFormatFactory extends TemplateNumberFormatFactory {

    public static final LocaleSensitiveTemplateNumberFormatFactory INSTANCE = new LocaleSensitiveTemplateNumberFormatFactory();

    private LocaleSensitiveTemplateNumberFormatFactory() {
        // Defined to decrease visibility
    }

    @Override
    public TemplateNumberFormat get(String params, Locale locale, Environment env)
        throws InvalidFormatParametersException {
        TemplateFormatUtil.checkHasNoParameters(params);
        return new LocaleSensitiveTemplateNumberFormat(locale);
    }

    private static class LocaleSensitiveTemplateNumberFormat extends TemplateNumberFormat {

        private final Locale locale;

        private LocaleSensitiveTemplateNumberFormat(Locale locale) {
            this.locale = locale;
        }

        @Override
        public String formatToPlainText(TemplateNumberModel numberModel)
            throws UnformattableValueException, TemplateModelException {
            Number n = numberModel.getAsNumber();
            try {
                return n + "_" + locale;
            } catch (ArithmeticException e) {
                throw new UnformattableValueException(n + " doesn't fit into an int");
            }
        }

        @Override
        public boolean isLocaleBound() {
            return true;
        }

        @Override
        public String getDescription() {
            return "test locale sensitive";
        }

    }

}
