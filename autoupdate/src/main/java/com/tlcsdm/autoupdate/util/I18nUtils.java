/*
 * Copyright (c) 2026 unknowIfGuestInDream.
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

package com.tlcsdm.autoupdate.util;

import com.tlcsdm.core.javafx.util.Config;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * 自动更新模块的国际化工具类.
 *
 * @author unknowIfGuestInDream
 */
public class I18nUtils {

    /**
     * 资源包默认路径.
     */
    private static final String BASENAME = "com.tlcsdm.autoupdate.i18n.messages";

    private I18nUtils() {
    }

    /**
     * 返回资源包的基础名称.
     *
     * @return 资源包基础名称
     */
    public static String getBasename() {
        return BASENAME;
    }

    /**
     * 根据 key 获取当前语言环境下的文案, 并使用 {@link MessageFormat} 进行占位符替换.
     *
     * @param key  文案 key
     * @param args 可选的格式化参数
     * @return 本地化后的文案
     */
    public static String get(final String key, final Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle(BASENAME, Config.defaultLocale);
        return MessageFormat.format(bundle.getString(key), args);
    }
}
