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
import freemarker.core.TemplateDateFormat;
import freemarker.core.TemplateDateFormatFactory;
import freemarker.core.TemplateFormatUtil;
import freemarker.core.TemplateValueFormatException;
import freemarker.core.UnformattableValueException;
import freemarker.core.UnknownDateTypeFormattingUnsupportedException;
import freemarker.core.UnparsableValueException;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DateUtil;
import freemarker.template.utility.DateUtil.CalendarFieldsToDateConverter;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author unknowIfGuestInDream
 */
public class HTMLISOTemplateDateFormatFactory extends TemplateDateFormatFactory {

    public static final HTMLISOTemplateDateFormatFactory INSTANCE = new HTMLISOTemplateDateFormatFactory();

    private HTMLISOTemplateDateFormatFactory() {
        // Defined to decrease visibility
    }

    @Override
    public TemplateDateFormat get(String params, int dateType, Locale locale, TimeZone timeZone, boolean zonelessInput, Environment env) throws
        UnknownDateTypeFormattingUnsupportedException, InvalidFormatParametersException {
        TemplateFormatUtil.checkHasNoParameters(params);
        return HTMLISOTemplateDateFormat.INSTANCE;
    }

    private static class HTMLISOTemplateDateFormat extends TemplateDateFormat {

        private static final HTMLISOTemplateDateFormat INSTANCE = new HTMLISOTemplateDateFormat();

        private DateUtil.TrivialDateToISO8601CalendarFactory calendarFactory;

        private CalendarFieldsToDateConverter calToDateConverter;

        private HTMLISOTemplateDateFormat() {
        }

        @Override
        public String formatToPlainText(TemplateDateModel dateModel) throws UnformattableValueException,
            TemplateModelException {
            if (calendarFactory == null) {
                calendarFactory = new DateUtil.TrivialDateToISO8601CalendarFactory();
            }
            return DateUtil.dateToISO8601String(TemplateFormatUtil.getNonNullDate(dateModel), true, true, true,
                DateUtil.ACCURACY_SECONDS, DateUtil.UTC, calendarFactory);
        }

        @Override
        public boolean isLocaleBound() {
            return false;
        }

        @Override
        public boolean isTimeZoneBound() {
            return false;
        }

        @Override
        public Date parse(String s, int dateType) throws UnparsableValueException {
            try {
                if (calToDateConverter == null) {
                    calToDateConverter = new DateUtil.TrivialCalendarFieldsToDateConverter();
                }
                return DateUtil.parseISO8601DateTime(s, DateUtil.UTC, calToDateConverter);
            } catch (DateUtil.DateParseException e) {
                throw new UnparsableValueException("Malformed ISO date-time", e);
            }
        }

        @Override
        public Object format(TemplateDateModel dateModel) throws TemplateValueFormatException, TemplateModelException {
            return HTMLOutputFormat.INSTANCE.fromMarkup(
                formatToPlainText(dateModel).replace("T", "<span class='T'>T</span>"));
        }

        @Override
        public String getDescription() {
            return "ISO UTC HTML";
        }

    }

}
