package com.tlcsdm.core.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

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
	public void aes1() {
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

	/**
	 * aes
	 */
	@Test
	public void aes() {
//		AES aes = new AES(Mode.valueOf(Mode.ECB.name()), Padding.PKCS5Padding,
//				HexUtil.decodeHex("0102030405060708090a0b0c0d0e0f10"));
		AES aes = new AES(Mode.ECB, Padding.PKCS5Padding,
				"3f4eefd3525675154a5e3a0183d8087b".getBytes());
		String encryptStr = aes.encryptHex("16c5");
		System.out.println(encryptStr);
		System.out.println(aes.decryptStr(encryptStr));
	}

}
