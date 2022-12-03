package com.tlcsdm.core.javafx.util;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Locale;

/*
 * 存取框架配置
 */
public class Config {

    public static final String CONFIG_FILE_NAME = "systemConfigure.properties";

    public static Locale defaultLocale = Locale.getDefault();// 设置系统语言

    public static final String JAVAFX_TOOL_VERSION = "V1.0.0";// xJavaFxTool版本信息

    ///////////////////////////////////////////////////////////////

    public enum Keys {
        MainWindowWidth, MainWindowHeight, MainWindowTop, MainWindowLeft,
        Locale, NotepadEnabled, RememberWindowLocation, ConfirmExit,
        NewLauncher
    }

    private static PropertiesConfiguration conf;

    public static PropertiesConfiguration getConfig() {
        try {
            if (conf == null) {
                conf = new PropertiesConfiguration();
//                File file = ConfigureUtil.getConfigureFile(CONFIG_FILE_NAME);
//                conf = new PropertiesConfiguration();
//                conf.setAutoSave(true); // 启用自动保存
                Configurations configs = new Configurations();

                // 每个Configuration代表这一个配置文件~（依赖beanutils这个jar）
                Configuration config = configs.properties("my.properties");
                FileBasedConfigurationBuilder.setDefaultEncoding(PropertiesConfiguration.class, "UTF-8");
            } else {
                //conf.reload();
            }
        } catch (Exception e) {
            e.printStackTrace();

            // 即使加载失败，也要返回一个内存中的 PropertiesConfiguration 对象，以免程序报错。
            conf = new PropertiesConfiguration();
        }

        return conf;
    }

    /**
     * 修改配置，修改后的值将会自动保存
     */
    public static void set(Keys key, Object value) {
        getConfig().setProperty(key.name(), value);
    }

    public static String get(Keys key, String def) {
        Object value = getConfig().getProperty(key.name());
        return value == null ? def : value.toString();
    }

    public static double getDouble(Keys key, double def) {
        return NumberUtils.toDouble(get(key, null), def);
    }

    public static boolean getBoolean(Keys key, boolean def) {
        return Boolean.parseBoolean(get(key, String.valueOf(def)));
    }
}
