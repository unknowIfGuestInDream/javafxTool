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

package com.tlcsdm.core.util;

import java.util.ArrayList;

/**
 * 字符串分割工具.
 *
 * @author unknowIfGuestInDream
 * @author Konloch
 */
public class SplitUtil {

    /**
     * Splits a String with a specified separator String.
     *
     * @param s         any String
     * @param separator any String
     * @return a String array split on the separator supplied
     */
    public static String[] split(String s, String separator) {
        return split(s, separator, -1);
    }

    /**
     * Splits a String with a specified separator String.
     *
     * @param s         any String
     * @param separator any String
     * @param maxAmount -1 for unlimited, or else the maximum size of results returned
     * @return a String array split on the separator supplied
     */
    public static String[] split(String s, String separator, int maxAmount) {
        return split(s, separator, maxAmount, false);
    }

    /**
     * Splits a String with a specified separator String.
     *
     * @param s                 any String
     * @param separator         any String
     * @param maxAmount         -1 for unlimited, or else the maximum size of results returned
     * @param preserveSeparator if true it will include the separator at the end of each split line
     * @return a String array split on the separator supplied
     */
    public static String[] split(String s, String separator, int maxAmount, boolean preserveSeparator) {
        return split(s, separator, maxAmount, preserveSeparator, true);
    }

    /**
     * Splits a String with a specified separator String.
     *
     * @param s                 any String
     * @param separator         any String
     * @param maxAmount         -1 for unlimited, or else the maximum size of results returned
     * @param preserveSeparator if true it will include the separator at the end of each split line
     * @param allowEmptyResults is true the results will include empty results on repeating search parameters
     * @return a String array split on the separator supplied
     */
    public static String[] split(String s, String separator, int maxAmount, boolean preserveSeparator, boolean allowEmptyResults) {
        ArrayList<String> results = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        StringBuilder searchBuffer = new StringBuilder();
        StringBuilder escapeBuffer = new StringBuilder();

        char[] lineChars = s.toCharArray();
        char[] sepChars = separator.toCharArray();

        boolean isSearching = false;
        int sepCharsIndex = 0;

        for (char c : lineChars) {
            boolean completedSearch = false;
            if (isSearching) {
                //if current c isn't found while searching
                if (sepCharsIndex < sepChars.length && c != sepChars[sepCharsIndex++]) {
                    isSearching = false;
                    buffer.append(searchBuffer);
                    searchBuffer = new StringBuilder();
                    sepCharsIndex = 0;
                }
                //completely found, search is over, time for next iteration
                if (sepCharsIndex >= sepChars.length) {
                    completedSearch = true;
                    boolean multiCharSearch = sepCharsIndex > 1;
                    //restart the search for this character since we have ended
                    isSearching = false;
                    if (preserveSeparator) {
                        buffer.append(searchBuffer);
                    }
                    searchBuffer = new StringBuilder();
                    sepCharsIndex = 0;
                    if (!buffer.isEmpty()) {
                        results.add(buffer.toString());
                    }
                    buffer = new StringBuilder();
                    //TODO multi-line characters search is not fully functional
                    if (multiCharSearch) {
                        continue;
                    }
                }
            }
            if (!isSearching) {
                //if current c isn't found while searching
                if (c != sepChars[sepCharsIndex++]) {
                    buffer.append(c);
                    sepCharsIndex = 0;
                } else {
                    if (allowEmptyResults && completedSearch) {
                        results.add("");
                    }
                    isSearching = true;
                    if (!completedSearch) {
                        searchBuffer.append(c);
                    }
                }
            }
        }

        //add whatever is left
        if (!buffer.isEmpty())
            results.add(buffer.toString());

        //trim amount found
        if (maxAmount > 0 && results.size() > maxAmount) {
            ArrayList<String> trimmedList = new ArrayList<>();
            for (int i = 0; i < maxAmount - 1; i++)
                trimmedList.add(results.get(i));

            //read the rest of the split content
            StringBuilder sb = new StringBuilder();
            boolean b = false;
            for (int i = maxAmount - 1; i < results.size(); i++) {
                if (!b) {
                    b = true;
                } else {
                    sb.append(separator);
                }
                sb.append(results.get(i));
            }
            trimmedList.add(sb.toString());
            return trimmedList.toArray(new String[0]);
        }
        return results.toArray(new String[0]);
    }
}
