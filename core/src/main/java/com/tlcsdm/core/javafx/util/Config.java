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
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.util.Locale;

/**
 * 存取框架配置
 *
 * @author unknowIfGuestInDream
 */
public class Config {

    public static final String CONFIG_FILE_NAME = "systemConfigure.properties";
    public static final String USERDATA_FILE_NAME = "data.xml";
    // 设置系统语言
    public static Locale defaultLocale = Locale.getDefault();

    public enum Keys {
        MainWindowWidth, MainWindowHeight, MainWindowTop, MainWindowLeft, Locale, NotepadEnabled,
        RememberWindowLocation, ConfirmExit, CheckForUpdatesAtStartup
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
            StaticLog.error(e);
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
