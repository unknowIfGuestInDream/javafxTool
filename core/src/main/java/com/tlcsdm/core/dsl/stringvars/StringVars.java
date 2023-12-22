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

package com.tlcsdm.core.dsl.stringvars;

import cn.hutool.log.StaticLog;

/**
 * @author unknowIfGuestInDream
 * @author Konloch
 */
public class StringVars {

    private StringVars() {
    }

    /**
     * Search for the delimiter, and if it exists it will use the String between the delimiter as a key to do a key-value search.
     *
     * @param delimiter   any character to represent the start and end of the variable key
     * @param originalRSF an implementation of the original getStringValue function this API is going to be replacing
     * @param rsf         an implementation of a String key / value search
     * @return the fully extracted String
     */
    public static String getVariableValue(char delimiter, OriginalReturnString originalRSF, ReturnStringFromKey rsf) {
        String value = originalRSF.getString();
        char[] chars = value.toCharArray();
        StringBuilder parsedValue = new StringBuilder();
        StringBuilder parsedKey = new StringBuilder();
        boolean escaped = false;
        boolean parsingKey = false;
        for (int i = 0; i < value.length(); i++) {
            char c = chars[i];
            if (escaped && c != delimiter) {
                escaped = false;
                parsedValue.append('\\');
            }
            if (c == '\\') {
                escaped = true;
            } else if (c == delimiter) {
                if (escaped) {
                    parsedValue.append(c);
                    escaped = false;
                    continue;
                }
                parsingKey = !parsingKey;
                if (!parsingKey) {
                    //handle recursion here
                    try {
                        String parsedKeyCopy = parsedKey.toString();
                        String parsedKeyCopyValue = rsf.getString(parsedKeyCopy);
                        parsedValue.append(parsedKeyCopyValue);
                    } catch (StackOverflowError e) {
                        StaticLog.error(e);
                    }
                    parsedKey = new StringBuilder();
                }
            } else {
                if (parsingKey) {
                    parsedKey.append(c);
                } else {
                    parsedValue.append(c);
                }
            }
        }
        return parsedValue.toString();
    }

}
