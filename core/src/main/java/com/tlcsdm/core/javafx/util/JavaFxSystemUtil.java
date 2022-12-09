package com.tlcsdm.core.javafx.util;

import cn.hutool.log.StaticLog;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author: 唐 亮
 * @date: 2022/11/27 0:00
 */
public class JavaFxSystemUtil {

    public static List<Locale> SUPPORT_LOCALE = List.of(Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE, Locale.JAPANESE);
    public static List<String> SUPPORT_LOCALE_STRING = List.of("en", "en_US", "zh", "zh_CN", "ja", "ja_JP");

    public JavaFxSystemUtil() {
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
            String localeString = Config.get(Config.Keys.Locale, "");
            if (StringUtils.isNotEmpty(localeString)) {
                localeString = localeString.toLowerCase();
                switch (localeString) {
                    case "en", "en_us" -> Config.defaultLocale = Locale.ENGLISH;
                    case "zh", "zh_cn" -> Config.defaultLocale = Locale.SIMPLIFIED_CHINESE;
                    case "ja", "ja_jp" -> Config.defaultLocale = Locale.JAPANESE;
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
