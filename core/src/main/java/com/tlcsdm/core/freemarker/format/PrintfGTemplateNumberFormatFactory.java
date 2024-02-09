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
import freemarker.core.HTMLOutputFormat;
import freemarker.core.InvalidFormatParametersException;
import freemarker.core.TemplateFormatUtil;
import freemarker.core.TemplateNumberFormat;
import freemarker.core.TemplateNumberFormatFactory;
import freemarker.core.UnformattableValueException;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.utility.StringUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

/**
 * Formats like {@code %G} in {@code printf}, with the specified number of significant digits. Also has special
 * formatter for HTML output format, where it uses the HTML "sup" element for exponents.
 *
 * @author unknowIfGuestInDream
 */
public class PrintfGTemplateNumberFormatFactory extends TemplateNumberFormatFactory {

    public static final PrintfGTemplateNumberFormatFactory INSTANCE = new PrintfGTemplateNumberFormatFactory();

    private PrintfGTemplateNumberFormatFactory() {
        // Defined to decrease visibility
    }

    @Override
    public TemplateNumberFormat get(String params, Locale locale, Environment env)
        throws InvalidFormatParametersException {
        Integer significantDigits;
        if (!params.isEmpty()) {
            try {
                significantDigits = Integer.valueOf(params);
            } catch (NumberFormatException e) {
                throw new InvalidFormatParametersException(
                    "The format parameter must be an integer, but was (shown quoted) "
                        + StringUtil.jQuote(params) + ".");
            }
        } else {
            // Use the default of %G
            significantDigits = null;
        }
        return new PrintfGTemplateNumberFormat(significantDigits, locale);
    }

    private static class PrintfGTemplateNumberFormat extends TemplateNumberFormat {

        private final Locale locale;
        private final String printfFormat;

        private PrintfGTemplateNumberFormat(Integer significantDigits, Locale locale) {
            this.printfFormat = "%" + (significantDigits != null ? "." + significantDigits : "") + "G";
            this.locale = locale;
        }

        @Override
        public String formatToPlainText(TemplateNumberModel numberModel)
            throws UnformattableValueException, TemplateModelException {
            final Number n = TemplateFormatUtil.getNonNullNumber(numberModel);

            // printf %G only accepts Double, BigDecimal and Float
            final Number gCompatibleN;
            if (n instanceof Double || n instanceof BigDecimal || n instanceof Float) {
                gCompatibleN = n;
            } else {
                if (n instanceof BigInteger) {
                    gCompatibleN = new BigDecimal((BigInteger) n);
                } else if (n instanceof Long) {
                    gCompatibleN = BigDecimal.valueOf(n.longValue());
                } else {
                    gCompatibleN = Double.valueOf(n.doubleValue());
                }
            }

            return String.format(locale, printfFormat, gCompatibleN);
        }

        @Override
        public Object format(TemplateNumberModel numberModel)
            throws UnformattableValueException, TemplateModelException {
            String strResult = formatToPlainText(numberModel);

            int expIdx = strResult.indexOf('E');
            if (expIdx == -1) {
                return strResult;
            }

            String expStr = strResult.substring(expIdx + 1);
            int expSignifNumBegin = 0;
            while (expSignifNumBegin < expStr.length() && isExpSignifNumPrefix(expStr.charAt(expSignifNumBegin))) {
                expSignifNumBegin++;
            }

            return HTMLOutputFormat.INSTANCE.fromMarkup(
                strResult.substring(0, expIdx)
                    + "*10<sup>"
                    + (expStr.charAt(0) == '-' ? "-" : "") + expStr.substring(expSignifNumBegin)
                    + "</sup>");
        }

        private boolean isExpSignifNumPrefix(char c) {
            return c == '+' || c == '-' || c == '0';
        }

        @Override
        public boolean isLocaleBound() {
            return true;
        }

        @Override
        public String getDescription() {
            return "printf " + printfFormat;
        }

    }

}
