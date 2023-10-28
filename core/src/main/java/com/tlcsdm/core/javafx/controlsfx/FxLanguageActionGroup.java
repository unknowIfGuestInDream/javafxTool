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
import org.controlsfx.control.action.ActionGroup;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * 语言切换ActionGroup.
 *
 * @author unknowIfGuestInDream
 */
public class FxLanguageActionGroup {
    ConfigLanguageAction chinese;
    ConfigLanguageAction english;
    ConfigLanguageAction japanese;

    public FxLanguageActionGroup(Consumer<Object> consumer) {
        chinese = new ConfigLanguageAction(LanguageType.CHINESE, consumer);
        english = new ConfigLanguageAction(LanguageType.ENGLISH, consumer);
        japanese = new ConfigLanguageAction(LanguageType.JAPANESE, consumer);
        // 语言设置
        if (Config.defaultLocale.equals(Locale.ENGLISH)) {
            english.setSelected(true);
        } else if (Config.defaultLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
            chinese.setSelected(true);
        } else if (Config.defaultLocale.equals(Locale.JAPANESE)) {
            japanese.setSelected(true);
        }
        chinese.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                return;
            }
            if (newValue) {
                english.setSelected(false);
                japanese.setSelected(false);
            }
        });
        english.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                return;
            }
            if (newValue) {
                japanese.setSelected(false);
                chinese.setSelected(false);
            }
        });
        japanese.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                return;
            }
            if (newValue) {
                english.setSelected(false);
                chinese.setSelected(false);
            }
        });
    }

    public ActionGroup create() {
        return FxActionGroup.language(chinese, english, japanese);
    }
}
