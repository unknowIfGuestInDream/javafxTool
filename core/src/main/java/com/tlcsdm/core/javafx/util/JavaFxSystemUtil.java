package com.tlcsdm.core.javafx.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author: 唐 亮
 * @date: 2022/11/27 0:00
 */
public class JavaFxSystemUtil {
    /**
     * @deprecated
     */
    @Deprecated
    public static Stage mainStage = null;

    public JavaFxSystemUtil() {
    }

    public static void openDirectory(String directoryPath) {
        try {
            Desktop.getDesktop().open(new File(directoryPath));
        } catch (IOException var2) {
            System.err.println("打开目录异常：" + directoryPath);
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
}
