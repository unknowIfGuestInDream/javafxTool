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
import org.apache.commons.configuration2.XMLPropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.XMLBuilderParameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author unknowIfGuestInDream
 * @date 2022/12/11 19:03
 */
public class FxXmlUtil {

    private static XMLPropertiesConfiguration conf;

    public static XMLPropertiesConfiguration getConfig() {
        if (conf != null) {
            return conf;
        }

        Parameters params = new Parameters();
        File propertiesFile = ConfigureUtil.getConfigureXmlFile(Config.USERDATA_FILE_NAME);
        XMLBuilderParameters xmlBuilderParameters = params.xml().setFile(propertiesFile).setEncoding("UTF-8")
            .setListDelimiterHandler(new DefaultListDelimiterHandler(',')).setThrowExceptionOnMissing(false);
        ReloadingFileBasedConfigurationBuilder<XMLPropertiesConfiguration> builder = new ReloadingFileBasedConfigurationBuilder<>(
            XMLPropertiesConfiguration.class).configure(xmlBuilderParameters);
        builder.setAutoSave(true);
        try {
            conf = builder.getConfiguration();
        } catch (ConfigurationException e) {
            StaticLog.error(e);
        }
        return conf;
    }

    public static boolean hasKey(String key) {
        Object value = getConfig().getProperty(key);
        return value != null;
    }

    public static boolean hasKey(String prefix, String key) {
        return hasKey(prefix + "." + key);
    }

    public static void set(String key, Object value) {
        getConfig().setProperty(key, value);
    }

    public static void set(String prefix, String key, Object value) {
        set(prefix + "." + key, value);
    }

    public static String get(String key, String def) {
        Object value = getConfig().getProperty(key);
        if (value == null) {
            return def;
        }
        if (value instanceof List v) {
            return toString(v);
        }
        return value.toString();
    }

    public static String get(String key) {
        return get(key, "");
    }

    public static String get(String prefix, String key, String def) {
        return get(prefix + "." + key, "");
    }

    public static double getDouble(String key, double def) {
        return getConfig().getDouble(key, def);
    }

    public static boolean getBoolean(String key, boolean def) {
        return getConfig().getBoolean(key, def);
    }

    public static List<Object> getList(String key, List<String> def) {
        return getConfig().getList(key, def);
    }

    public static void del(String prefix, String key) {
        del(prefix + "." + key);
    }

    public static void del(String key) {
        getConfig().clearProperty(key);
    }

    public static void del() {
        getConfig().clear();
    }

    public static List<String> getKeys(String prefix) {
        Iterator<String> iter = getConfig().getKeys(prefix);
        List<String> list = new ArrayList<>();
        iter.forEachRemaining(list::add);
        return list;
    }

    public static Map<String, Object> getValues(String prefix) {
        Iterator<String> iter = getConfig().getKeys(prefix);
        Map<String, Object> map = new LinkedHashMap<>();
        while (iter.hasNext()) {
            String key = iter.next();
            map.put(key, get(key));
        }
        return map;
    }

    public static void delKeys(String prefix) {
        Iterator<String> iter = getConfig().getKeys(prefix);
        while (iter.hasNext()) {
            String key = iter.next();
            del(key);
        }
    }

    public static void setKeys(String prefix, Map<String, Object> map) {
        Iterator<String> iter = getConfig().getKeys(prefix);
        while (iter.hasNext()) {
            String key = iter.next();
            set(key, map.get(key));
        }
    }

    public static void write(File file) {
        try {
            getConfig().write(new FileWriter(file));
        } catch (ConfigurationException | IOException e) {
            StaticLog.error(e);
        }
    }

    public static void read(File file) {
        try {
            getConfig().read(new FileReader(file));
        } catch (ConfigurationException | IOException e) {
            StaticLog.error(e);
        }
    }

    private static String toString(List<Object> list) {
        StringJoiner sj = new StringJoiner(", ");
        for (Object object : list) {
            sj.add(object.toString());
        }
        return sj.toString();
    }
}
