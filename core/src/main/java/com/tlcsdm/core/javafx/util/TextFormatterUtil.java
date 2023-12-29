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

package com.tlcsdm.core.javafx.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.converter.DefaultStringConverter;

import java.util.function.UnaryOperator;

/**
 * This class provides simple tools for setting up JavaFX UI control text formatter such as
 * {@link TextField#setTextFormatter(TextFormatter)}.
 *
 * @author unknowIfGuestInDream
 */
public class TextFormatterUtil {
    private static final String ipAddressRegex = makePartialIPRegex();

    /**
     * @return a TextFormatter that ensures proper formatting of an IP address.
     */
    public static TextFormatter<String> ipAddressTextFormatter() {
        return ipAddressTextFormatter(null);
    }

    public static TextFormatter<String> ipAddressTextFormatter(String defaultValue) {
        UnaryOperator<Change> ipAddressFilter = change -> change.getControlNewText().matches(
            ipAddressRegex) ? change : null;
        return new TextFormatter<>(new DefaultStringConverter(), defaultValue, ipAddressFilter);
    }

    private static String makePartialIPRegex() {
        String partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))";
        String subsequentPartialBlock = "(\\." + partialBlock + ")";
        String ipAddress = partialBlock + "?" + subsequentPartialBlock + "{0,3}";
        return "^" + ipAddress;
    }

    /**
     * @return a TextFormatter that ensures the amount of characters in a text is less or equal the specified amount.
     */
    public static TextFormatter<String> maxLengthTextFormatter(int maxCharacters) {
        UnaryOperator<Change> lengthFilter = change -> change.getControlNewText()
            .length() <= maxCharacters ? change : null;
        return new TextFormatter<>(lengthFilter);
    }
}
