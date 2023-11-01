/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.wrap.hutool;

import java.util.List;
import java.util.Map;

/**
 * {@link cn.hutool.core.util.StrUtil}
 * 
 * @author unknowIfGuestInDream
 */
public class StrUtil {

    /**
     * {@inheritDoc}
     */
    public static String format(CharSequence template, Map<?, ?> map) {
        return cn.hutool.core.util.StrUtil.format(template, map, true);
    }

    /**
     * {@inheritDoc}
     */
    public static List<String> split(CharSequence str, CharSequence separator) {
        return cn.hutool.core.util.StrUtil.split(str, separator);
    }

    /**
     * {@inheritDoc}
     */
    public static List<String> splitTrim(CharSequence str, CharSequence separator) {
        return cn.hutool.core.util.StrUtil.splitTrim(str, separator, -1);
    }

    /**
     * {@inheritDoc}
     */
    public static boolean isEmpty(CharSequence str) {
        return cn.hutool.core.util.StrUtil.isEmpty(str);
    }

    /**
     * {@inheritDoc}
     */
    public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
        return cn.hutool.core.util.StrUtil.replace(str, searchStr, replacement);
    }

}
