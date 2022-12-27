package com.tlcsdm.core.javafx.util;

import java.io.File;

import org.apache.commons.configuration2.XMLPropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.XMLBuilderParameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.math.NumberUtils;

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
		File propertiesFile = ConfigureUtil.getConfigureXmlFile("smc.xml");
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

	public static void set(String key, Object value) {
		getConfig().setProperty(key, value);
	}

	public static String get(String key, String def) {
		Object value = getConfig().getProperty(key);
		return value == null ? def : value.toString();
	}

	public static String get(String key) {
		return get(key, "");
	}

	public static double getDouble(String key, double def) {
		return NumberUtils.toDouble(get(key, null), def);
	}

	public static boolean getBoolean(String key, boolean def) {
		return Boolean.parseBoolean(get(key, String.valueOf(def)));
	}
}
