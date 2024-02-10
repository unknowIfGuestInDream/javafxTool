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
import freemarker.core.TemplateDateFormat;
import freemarker.core.TemplateDateFormatFactory;
import freemarker.core.TemplateFormatUtil;
import freemarker.core.UnformattableValueException;
import freemarker.core.UnknownDateTypeFormattingUnsupportedException;
import freemarker.core.UnparsableValueException;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModelException;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author unknowIfGuestInDream
 */
public class LocAndTZSensitiveTemplateDateFormatFactory extends TemplateDateFormatFactory {

    public static final LocAndTZSensitiveTemplateDateFormatFactory INSTANCE = new LocAndTZSensitiveTemplateDateFormatFactory();

    private LocAndTZSensitiveTemplateDateFormatFactory() {
        // Defined to decrease visibility
    }

    @Override
    public TemplateDateFormat get(String params, int dateType, Locale locale, TimeZone timeZone, boolean zonelessInput,
        Environment env) throws UnknownDateTypeFormattingUnsupportedException, InvalidFormatParametersException {
        TemplateFormatUtil.checkHasNoParameters(params);
        return new LocAndTZSensitiveTemplateDateFormat(locale, timeZone);
    }

    private static class LocAndTZSensitiveTemplateDateFormat extends TemplateDateFormat {

        private final Locale locale;
        private final TimeZone timeZone;

        public LocAndTZSensitiveTemplateDateFormat(Locale locale, TimeZone timeZone) {
            this.locale = locale;
            this.timeZone = timeZone;
        }

        @Override
        public String formatToPlainText(TemplateDateModel dateModel)
            throws UnformattableValueException, TemplateModelException {
            return String.valueOf(
                TemplateFormatUtil.getNonNullDate(dateModel).getTime() + "@" + locale + ":" + timeZone.getID());
        }

        @Override
        public boolean isLocaleBound() {
            return true;
        }

        @Override
        public boolean isTimeZoneBound() {
            return true;
        }

        @Override
        public Date parse(String s, int dateType) throws UnparsableValueException {
            try {
                int atIdx = s.indexOf("@");
                if (atIdx == -1) {
                    throw new UnparsableValueException("Missing @");
                }
                return new Date(Long.parseLong(s.substring(0, atIdx)));
            } catch (NumberFormatException e) {
                throw new UnparsableValueException("Malformed long");
            }
        }

        @Override
        public String getDescription() {
            return "millis since the epoch";
        }

    }

}
