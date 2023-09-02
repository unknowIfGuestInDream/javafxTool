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
import javafx.stage.Stage;

import java.util.List;

/**
 * @author unknowIfGuestInDream
 * @date 2022/11/27 18:59
 */
public class StageUtil {

    private StageUtil() {
    }

    /**
     * 加载Stage边框位置
     */
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

    /**
     * 保存Stage边框位置
     */
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
