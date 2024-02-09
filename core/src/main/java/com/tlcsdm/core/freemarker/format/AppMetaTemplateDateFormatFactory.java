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
public class AppMetaTemplateDateFormatFactory extends TemplateDateFormatFactory {

    public static final AppMetaTemplateDateFormatFactory INSTANCE = new AppMetaTemplateDateFormatFactory();

    private AppMetaTemplateDateFormatFactory() {
        // Defined to decrease visibility
    }

    @Override
    public TemplateDateFormat get(String params, int dateType, Locale locale, TimeZone timeZone, boolean zonelessInput, Environment env) throws
        UnknownDateTypeFormattingUnsupportedException, InvalidFormatParametersException {
        TemplateFormatUtil.checkHasNoParameters(params);
        return AppMetaTemplateDateFormat.INSTANCE;
    }

    private static class AppMetaTemplateDateFormat extends TemplateDateFormat {

        private static final AppMetaTemplateDateFormat INSTANCE = new AppMetaTemplateDateFormat();

        private AppMetaTemplateDateFormat() {
        }

        @Override
        public String formatToPlainText(TemplateDateModel dateModel) throws UnformattableValueException,
            TemplateModelException {
            String result = String.valueOf(TemplateFormatUtil.getNonNullDate(dateModel).getTime());
            if (dateModel instanceof AppMetaTemplateDateModel) {
                result += "/" + ((AppMetaTemplateDateModel) dateModel).getAppMeta();
            }
            return result;
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
        public Object parse(String s, int dateType) throws UnparsableValueException {
            int slashIdx = s.indexOf('/');
            try {
                if (slashIdx != -1) {
                    return new AppMetaTemplateDateModel(new Date(Long.parseLong(s.substring(0, slashIdx))), dateType,
                        s.substring(slashIdx + 1));
                } else {
                    return new Date(Long.parseLong(s));
                }
            } catch (NumberFormatException e) {
                throw new UnparsableValueException("Malformed long");
            }
        }

        @Override
        public String getDescription() {
            return "millis since the epoch";
        }

    }

    public static class AppMetaTemplateDateModel implements TemplateDateModel {

        private final Date date;
        private final int dateType;
        private final String appMeta;

        public AppMetaTemplateDateModel(Date date, int dateType, String appMeta) {
            this.date = date;
            this.dateType = dateType;
            this.appMeta = appMeta;
        }

        public Date getAsDate() throws TemplateModelException {
            return date;
        }

        public int getDateType() {
            return dateType;
        }

        public String getAppMeta() {
            return appMeta;
        }

    }

}
