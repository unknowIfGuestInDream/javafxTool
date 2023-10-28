/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.controlsfx;

import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.Keys;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionCheck;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * 语言action.
 *
 * @author unknowIfGuestInDream
 */
@ActionCheck
public class ConfigLanguageAction extends Action {

    public ConfigLanguageAction(LanguageType language, Consumer<Object> consumer) {
        super(language.getText());
        setEventHandler(ae -> {
            if (LanguageType.CHINESE.equals(language)) {
                if (Config.defaultLocale == Locale.SIMPLIFIED_CHINESE) {
                    return;
                }
                Config.set(Keys.Locale, Locale.SIMPLIFIED_CHINESE);
            } else if (LanguageType.ENGLISH.equals(language)) {
                if (Config.defaultLocale == Locale.ENGLISH) {
                    return;
                }
                Config.set(Keys.Locale, Locale.ENGLISH);
            } else if (LanguageType.JAPANESE.equals(language)) {
                if (Config.defaultLocale == Locale.JAPANESE) {
                    return;
                }
                Config.set(Keys.Locale, Locale.JAPANESE);
            }
            consumer.accept(null);
        });
    }

}
