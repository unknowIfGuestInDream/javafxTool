package com.tlcsdm.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 工具包
 */
public class CoreUtil {

	/**
	 * Object为null时, toString()会返回 "null". 本方法会在Object为null时返回空串.
	 */
	public static String valueOf(Object obj) {
		return (obj == null) ? "" : obj.toString();
	}

	/**
	 * 首字母小写
	 */
	public static String toLowerCase4Index(String string) {
		if (Character.isLowerCase(string.charAt(0))) {
			return string;
		}

		char[] chars = string.toCharArray();
		chars[0] += 32;
		return String.valueOf(chars);
	}

	/**
	 * 首字母大写
	 */
	public static String toUpperCase4Index(String string) {
		char[] chars = string.toCharArray();
		chars[0] = toUpperCase(chars[0]);
		return String.valueOf(chars);
	}

	/**
	 * 字符转成大写
	 */
	public static char toUpperCase(char chars) {
		if (97 <= chars && chars <= 122) {
			chars ^= 32;
		}
		return chars;
	}

	/**
	 * 根据容量获取map初始大小, 参考JDK8中putAll方法中的实现以及guava的newHashMapWithExpectedSize方法
	 *
	 * @param expectedSize 容量大小
	 */
	public static int newHashMapWithExpectedSize(int expectedSize) {
		if (expectedSize < 3) {
			return 4;
		} else {
			return expectedSize < 1073741824 ? (int) ((float) expectedSize / 0.75F + 1.0F) : 2147483647;
		}
	}

	/**
	 * 深度克隆
	 */
	public static Object deepClone(Object object) {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			if (object != null) {
				objectOutputStream.writeObject(object);
			}
			ObjectInputStream objectInputStream = new ObjectInputStream(
					new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
			return objectInputStream.readObject();
		} catch (Exception e) {
			return null;
		}
	}

}
