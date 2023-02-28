package com.tlcsdm.core.javafx.util;

import java.io.File;
import java.util.Locale;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 存取框架配置
 *
 * @author unknowIfGuestInDream
 */
public class Config {

    public static final String CONFIG_FILE_NAME = "systemConfigure.properties";
    public static final String USERDATA_FILE_NAME = "data.xml";

    public static Locale defaultLocale = Locale.getDefault();// 设置系统语言

    public static final String JAVAFX_TOOL_VERSION = "1.0.0";// JavaFxTool版本信息
    public static final String JAVAFX_TOOL_PUBLISHDATE = "2023-02-28";// 发布日期

    public enum Keys {
        MainWindowWidth, MainWindowHeight, MainWindowTop, MainWindowLeft, Locale, NotepadEnabled,
        RememberWindowLocation, ConfirmExit
    }

    private static PropertiesConfiguration conf;

    public static PropertiesConfiguration getConfig() {
        if (conf != null) {
            return conf;
        }
        Parameters params = new Parameters();
        File propertiesFile = ConfigureUtil.getConfigureFile(CONFIG_FILE_NAME);
        PropertiesBuilderParameters propertiesBuilderParameters = params.properties().setFile(propertiesFile)
                .setEncoding("UTF-8").setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                .setThrowExceptionOnMissing(false);
        ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder = new ReloadingFileBasedConfigurationBuilder<>(
                PropertiesConfiguration.class).configure(propertiesBuilderParameters);
        builder.setAutoSave(true);
        try {
            conf = builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
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
