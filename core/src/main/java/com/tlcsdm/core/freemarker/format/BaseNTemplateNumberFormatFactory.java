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
import freemarker.core.TemplateValueFormatException;
import freemarker.core.UnformattableValueException;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.utility.NumberUtil;
import freemarker.template.utility.StringUtil;

import java.util.Locale;

/**
 * Shows a number in base N number system. Can only format numbers that fit into an {@code int},
 * however, optionally you can specify a fallback format. This format has one required parameter,
 * the numerical system base. That can be optionally followed by "|" and a fallback format.
 *
 * @author unknowIfGuestInDream
 */
public class BaseNTemplateNumberFormatFactory extends TemplateNumberFormatFactory {

    public static final BaseNTemplateNumberFormatFactory INSTANCE = new BaseNTemplateNumberFormatFactory();

    private BaseNTemplateNumberFormatFactory() {
        // Defined to decrease visibility
    }

    @Override
    public TemplateNumberFormat get(String params, Locale locale, Environment env) throws
        InvalidFormatParametersException {
        TemplateNumberFormat fallbackFormat;
        {
            int barIdx = params.indexOf('|');
            if (barIdx != -1) {
                String fallbackFormatStr = params.substring(barIdx + 1);
                params = params.substring(0, barIdx);
                try {
                    fallbackFormat = env.getTemplateNumberFormat(fallbackFormatStr, locale);
                } catch (TemplateValueFormatException e) {
                    throw new InvalidFormatParametersException(
                        "Couldn't get the fallback number format (specified after the \"|\"), " + StringUtil.jQuote(
                            fallbackFormatStr) + ". Reason: " + e.getMessage(), e);
                }
            } else {
                fallbackFormat = null;
            }
        }

        int base;
        try {
            base = Integer.parseInt(params);
        } catch (NumberFormatException e) {
            if (params.length() == 0) {
                throw new InvalidFormatParametersException(
                    "A format parameter is required to specify the numerical system base.");
            }
            throw new InvalidFormatParametersException(
                "The format paramter must be an integer, but was (shown quoted): " + StringUtil.jQuote(params));
        }
        if (base < 2) {
            throw new InvalidFormatParametersException("A base must be at least 2.");
        }
        return new BaseNTemplateNumberFormat(base, fallbackFormat);
    }

    private static class BaseNTemplateNumberFormat extends TemplateNumberFormat {

        private final int base;
        private final TemplateNumberFormat fallbackFormat;

        private BaseNTemplateNumberFormat(int base, TemplateNumberFormat fallbackFormat) {
            this.base = base;
            this.fallbackFormat = fallbackFormat;
        }

        @Override
        public String formatToPlainText(TemplateNumberModel numberModel) throws TemplateModelException,
            TemplateValueFormatException {
            Number n = TemplateFormatUtil.getNonNullNumber(numberModel);
            try {
                return Integer.toString(NumberUtil.toIntExact(n), base);
            } catch (ArithmeticException e) {
                if (fallbackFormat == null) {
                    throw new UnformattableValueException(
                        n + " doesn't fit into an int, and there was no fallback format " + "specified.");
                } else {
                    return fallbackFormat.formatToPlainText(numberModel);
                }
            }
        }

        @Override
        public boolean isLocaleBound() {
            return false;
        }

        @Override
        public String getDescription() {
            return "base " + base;
        }

    }

}
