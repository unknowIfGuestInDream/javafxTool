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

/**
 * 依赖工具类.
 * 主要用于获取当前是否存在某个依赖项
 *
 * @author unknowIfGuestInDream
 */
public class DependencyUtil {

    private DependencyUtil() {
    }

    /**
     * 是否存在JNA库.
     */
    public static boolean hasJna() {
        return CoreUtil.hasClass("com.sun.jna.platform.win32.User32") && CoreUtil.hasClass("com.sun.jna.Native");
    }

    /**
     * 是否存在Groovy库.
     */
    public static boolean hasGroovy() {
        return CoreUtil.hasClass("groovy.util.GroovyScriptEngine");
    }

    /**
     * 是否存在Freemarker库.
     */
    public static boolean hasFreemarker() {
        return CoreUtil.hasClass("freemarker.cache.TemplateLoader");
    }

    /**
     * 是否存在commons-csv库.
     */
    public static boolean hasCommonsCsv() {
        return CoreUtil.hasClass("org.apache.commons.csv.CSVParser");
    }

    /**
     * 是否存在poi库.
     */
    public static boolean hasPoi() {
        return CoreUtil.hasClass("org.apache.poi.Version");
    }

    /**
     * 是否存在Jackson库.
     */
    public static boolean hasJackson() {
        return CoreUtil.hasClass("com.fasterxml.jackson.databind.ObjectMapper");
    }

    /**
     * 是否存在java-diff-utils库.
     */
    public static boolean hasDifflib() {
        return CoreUtil.hasClass("com.github.difflib.DiffUtils");
    }

    /**
     * 是否存在caffeine库.
     */
    public static boolean hasCaffeine() {
        return CoreUtil.hasClass("com.github.benmanes.caffeine.cache.Cache");
    }

    /**
     * 是否存在Hikari库.
     */
    public static boolean hasHikari() {
        return CoreUtil.hasClass("com.zaxxer.hikari.HikariDataSource");
    }

    /**
     * 是否存在Druid库.
     */
    public static boolean hasDruid() {
        return CoreUtil.hasClass("com.alibaba.druid.VERSION");
    }

    /**
     * 是否存在javafx-web库.
     */
    public static boolean hasJavafxWeb() {
        return CoreUtil.hasClass("javafx.scene.web.WebView");
    }

    /**
     * 是否存在thumbnailator库.
     */
    public static boolean hasThumbnailator() {
        return CoreUtil.hasClass("net.coobird.thumbnailator.Thumbnails");
    }

    /**
     * 是否存在richtextfx库.
     */
    public static boolean hasRichTextFX() {
        return CoreUtil.hasClass("org.fxmisc.richtext.CodeArea");
    }
}
