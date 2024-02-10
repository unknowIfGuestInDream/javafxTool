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
import freemarker.core.UnparsableValueException;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModelException;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 将日期/时间值格式化为 自纪元以来的毫秒数.
 *
 * @author unknowIfGuestInDream
 */
public class EpochMillisTemplateDateFormatFactory extends TemplateDateFormatFactory {

    public static final EpochMillisTemplateDateFormatFactory INSTANCE = new EpochMillisTemplateDateFormatFactory();

    private EpochMillisTemplateDateFormatFactory() {
        // Defined to decrease visibility
    }

    @Override
    public TemplateDateFormat get(String params, int dateType, Locale locale, TimeZone timeZone, boolean zonelessInput, Environment env) throws
        InvalidFormatParametersException {
        TemplateFormatUtil.checkHasNoParameters(params);
        return EpochMillisTemplateDateFormat.INSTANCE;
    }

    private static class EpochMillisTemplateDateFormat extends TemplateDateFormat {

        private static final EpochMillisTemplateDateFormat INSTANCE = new EpochMillisTemplateDateFormat();

        private EpochMillisTemplateDateFormat() {
        }

        @Override
        public String formatToPlainText(TemplateDateModel dateModel) throws UnformattableValueException,
            TemplateModelException {
            return String.valueOf(TemplateFormatUtil.getNonNullDate(dateModel).getTime());
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
                return new Date(Long.parseLong(s));
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
