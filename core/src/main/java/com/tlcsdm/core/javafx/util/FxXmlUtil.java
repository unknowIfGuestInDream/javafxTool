package com.tlcsdm.core.javafx.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.XMLPropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.XMLBuilderParameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * @author: 唐 亮
 * @date: 2022/12/11 19:03
 */
public class FxXmlUtil {

	private static XMLPropertiesConfiguration conf;

	public static XMLPropertiesConfiguration getConfig() {
		if (conf != null) {
			return conf;
		}

		Parameters params = new Parameters();
		File propertiesFile = ConfigureUtil.getConfigureXmlFile("data.xml");
		XMLBuilderParameters xmlBuilderParameters = params.xml().setFile(propertiesFile).setEncoding("UTF-8")
				.setListDelimiterHandler(new DefaultListDelimiterHandler(',')).setThrowExceptionOnMissing(false);
		ReloadingFileBasedConfigurationBuilder<XMLPropertiesConfiguration> builder = new ReloadingFileBasedConfigurationBuilder<>(
				XMLPropertiesConfiguration.class).configure(xmlBuilderParameters);
		builder.setAutoSave(true);
		try {
			conf = builder.getConfiguration();
		} catch (ConfigurationException e) {
			e.printStackTrace();
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
		return value == null ? def : value.toString();
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
		Map<String, Object> map = new HashMap<>();
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
}
