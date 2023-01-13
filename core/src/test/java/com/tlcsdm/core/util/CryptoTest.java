package com.tlcsdm.core.util;

import java.security.SecureRandom;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/1/11 19:57
 */
public class CryptoTest {

	@Test
	public void md5() {
		System.out.println(SecureUtil.md5("unknowIfGuestInDream"));
	}

	@Test
	public void aes() {
		final SecureRandom random = RandomUtil.getSecureRandom("123456".getBytes());
		final SecretKey secretKey = KeyUtil.generateKey("AES", 128, random);

		String content = "12sdfsdfs你好啊！";
		AES aes = new AES(secretKey);
		final String result1 = aes.encryptBase64(content);
		System.out.println(result1);
		final String decryptStr = aes.decryptStr(result1);
		System.out.println(decryptStr);
	}

	/**
	 * des
	 */
	@Test
	public void des() {
		String content = "我是一个测试的test字符串123";
		final DES des = SecureUtil.des("unknowIfGuestInDream".getBytes());

		final String encryptHex = des.encryptHex(content);
		System.out.println(encryptHex);
		final String result = des.decryptStr(encryptHex);
		System.out.println(result);
	}

}
