package com.tlcsdm.core.javafx.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.FileUtil;

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
		return getConfigurePath() + fileName;
	}

	public static File getConfigureFile(String fileName) {
		File file = new File(getConfigurePath(fileName));
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static File getConfigureXmlFile(String fileName) {
		File file = new File(getConfigurePath(fileName));
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (FileUtil.isEmpty(file)) {
			List<String> list = new ArrayList<>();
			list.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			list.add("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
			list.add("<properties>");
			list.add("  <comment>This file was auto-generated. Do not modify it manually</comment>");
			list.add("</properties>");
			FileUtil.appendUtf8Lines(list, file);
		}
		return file;
	}
}
