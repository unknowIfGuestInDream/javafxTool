package com.tlcsdm.core.javafx.util;

import cn.hutool.log.StaticLog;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

/**
 * @author: 唐 亮
 * @date: 2022/11/27 18:59
 */
public class StageUtils {
    //加载Stage边框位置
    public static void loadPrimaryStageBound(Stage stage) {
        try {
            if (!Config.getBoolean(Config.Keys.RememberWindowLocation, true)) {
                return;
            }

            double left = Config.getDouble(Config.Keys.MainWindowLeft, stage.getX());
            double top = Config.getDouble(Config.Keys.MainWindowTop, -1);
            double width = Config.getDouble(Config.Keys.MainWindowWidth, -1);
            double height = Config.getDouble(Config.Keys.MainWindowHeight, -1);

            List<Screen> list = Screen.getScreens();
            double minX = 0;
            for (Screen screen : list) {
                Rectangle2D screenRectangle2 = screen.getBounds();
                if (screenRectangle2.getMinX() < minX) {
                    minX = screenRectangle2.getMinX();
                }
            }

            if (left > minX) {
                stage.setX(left);
            }
            if (top > 0) {
                stage.setY(top);
            }
            if (width > 0) {
                stage.setWidth(width);
            }
            if (height > 0) {
                stage.setHeight(height);
            }
        } catch (Exception e) {
            StaticLog.error("Initialization interface position failure");
        }
    }

    //保存Stage边框位置
    public static void savePrimaryStageBound(Stage stage) {
        if (!Config.getBoolean(Config.Keys.RememberWindowLocation, true)) {
            return;
        }
        if (stage == null || stage.isIconified()) {
            return;
        }
        try {
            Config.set(Config.Keys.MainWindowLeft, stage.getX());
            Config.set(Config.Keys.MainWindowTop, stage.getY());
            Config.set(Config.Keys.MainWindowWidth, stage.getWidth());
            Config.set(Config.Keys.MainWindowHeight, stage.getHeight());
        } catch (Exception e) {
            StaticLog.error("Initialization interface position failure");
        }
    }
}
