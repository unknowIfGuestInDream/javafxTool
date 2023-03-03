package com.tlcsdm.smc;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.frame.SplashScreen;
import javafx.scene.image.Image;

/**
 * 闪屏图片
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/3/3 22:47
 */
public class SmcSplash implements SplashScreen {
    @Override
    public Image getImage() {
        return LayoutHelper.icon(getClass().getResource("/com/tlcsdm/smc/static/splash.png"));
    }
}
