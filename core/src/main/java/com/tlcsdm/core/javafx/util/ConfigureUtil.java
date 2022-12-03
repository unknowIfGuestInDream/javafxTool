package com.tlcsdm.core.javafx.util;

import java.io.File;
import java.io.IOException;

/**
 * @author: 唐 亮
 * @date: 2022/11/27 0:10
 */
public class ConfigureUtil {
    public ConfigureUtil() {
    }

    public static String getConfigurePath() {
        return System.getProperty("user.home") + "/javafxTool/";
    }

    public static String getConfigurePath(String fileName) {
        String var10000 = getConfigurePath();
        return var10000 + fileName;
    }

    public static File getConfigureFile(String fileName) {
        File file = new File(getConfigurePath(fileName));
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
