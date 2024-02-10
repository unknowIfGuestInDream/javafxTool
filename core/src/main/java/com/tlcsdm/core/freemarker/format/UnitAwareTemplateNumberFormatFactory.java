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
import freemarker.core.TemplateNumberFormat;
import freemarker.core.TemplateNumberFormatFactory;
import freemarker.core.TemplateValueFormatException;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

import java.util.Locale;

/**
 * A number format that takes any other number format as parameter (specified as a string, as
 * usual in FreeMarker), then if the model is a {@link UnitAwareTemplateNumberModel}, it  shows
 * the unit after the number formatted with the other format, otherwise it just shows the formatted
 * number without unit.
 *
 * @author unknowIfGuestInDream
 */
public class UnitAwareTemplateNumberFormatFactory extends TemplateNumberFormatFactory {

    public static final UnitAwareTemplateNumberFormatFactory INSTANCE
        = new UnitAwareTemplateNumberFormatFactory();

    private UnitAwareTemplateNumberFormatFactory() {
        // Defined to decrease visibility
    }

    @Override
    public TemplateNumberFormat get(String params, Locale locale, Environment env)
        throws TemplateValueFormatException {
        return new UnitAwareNumberFormat(env.getTemplateNumberFormat(params, locale));
    }

    private static class UnitAwareNumberFormat extends TemplateNumberFormat {

        private final TemplateNumberFormat innerFormat;

        private UnitAwareNumberFormat(TemplateNumberFormat innerFormat) {
            this.innerFormat = innerFormat;
        }

        @Override
        public String formatToPlainText(TemplateNumberModel numberModel)
            throws TemplateModelException, TemplateValueFormatException {
            String innerResult = innerFormat.formatToPlainText(numberModel);
            return numberModel instanceof UnitAwareTemplateNumberModel
                ? innerResult + " " + ((UnitAwareTemplateNumberModel) numberModel).getUnit()
                : innerResult;
        }

        @Override
        public boolean isLocaleBound() {
            return innerFormat.isLocaleBound();
        }

        @Override
        public String getDescription() {
            return "unit-aware " + innerFormat.getDescription();
        }

    }

}
