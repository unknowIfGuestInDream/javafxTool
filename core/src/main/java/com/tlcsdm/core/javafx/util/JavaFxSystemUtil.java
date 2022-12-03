package com.tlcsdm.core.javafx.util;

import cn.hutool.log.StaticLog;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * @author: 唐 亮
 * @date: 2022/11/27 0:00
 */
public class JavaFxSystemUtil {
    /**
     * @deprecated
     */

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
                String[] locale1 = localeString.split("_");
                Config.defaultLocale = new Locale(locale1[0], locale1[1]);
            }

            //XJavaFxToolApplication.RESOURCE_BUNDLE = ResourceBundle.getBundle("locale.Menu", Config.defaultLocale);
            //RESOURCE_BUNDLE.getString("Title")
        } catch (Exception e) {
            //log.error("初始化本地语言失败", e);
        }
    }
}
