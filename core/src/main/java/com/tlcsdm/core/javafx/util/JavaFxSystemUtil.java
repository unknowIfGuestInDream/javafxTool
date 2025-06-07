/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

import cn.hutool.log.StaticLog;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.apache.commons.lang3.StringUtils;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author unknowIfGuestInDream
 * @date 2022/11/27 0:00
 */
public class JavaFxSystemUtil {

    public static List<Locale> SUPPORT_LOCALE = List.of(Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE, Locale.JAPANESE);
    public static List<String> SUPPORT_LOCALE_STRING = List.of("en", "en_US", "zh", "zh_CN", "ja", "ja_JP");

    private JavaFxSystemUtil() {
    }

    public static void openDirectory(String directoryPath) {
        try {
            Desktop.getDesktop().open(new File(directoryPath));
        } catch (IOException var2) {
            StaticLog.error("Failed to open directory");
        }
    }

    public static double[] getScreenSizeByScale(double width, double height) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = (double) screenSize.width * width;
        double screenHeight = (double) screenSize.height * height;
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        if (screenWidth > bounds.getWidth() || screenHeight > bounds.getHeight()) {
            screenWidth = bounds.getWidth();
            screenHeight = bounds.getHeight();
        }

        return new double[]{screenWidth, screenHeight};
    }

    public static void initSystemLocal() {
        try {
            String localeString = Config.get(Keys.Locale, "");
            if (StringUtils.isNotEmpty(localeString)) {
                localeString = localeString.toLowerCase();
                switch (localeString) {
                    case "en", "en_us" -> {
                        Config.defaultLocale = Locale.ENGLISH;
                        Locale.setDefault(Locale.ENGLISH);
                    }
                    case "zh", "zh_cn" -> {
                        Config.defaultLocale = Locale.SIMPLIFIED_CHINESE;
                        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
                    }
                    case "ja", "ja_jp" -> {
                        Config.defaultLocale = Locale.JAPANESE;
                        Locale.setDefault(Locale.JAPANESE);
                    }
                    default -> Config.defaultLocale = Locale.ENGLISH;
                }
            } else {
                if (!SUPPORT_LOCALE.contains(Config.defaultLocale)) {
                    if (!SUPPORT_LOCALE_STRING.contains(Config.defaultLocale.getCountry())) {
                        Config.defaultLocale = Locale.ENGLISH;
                    }
                }
            }
        } catch (Exception e) {
            StaticLog.error("Failed to initialize locale");
        }
    }
}
