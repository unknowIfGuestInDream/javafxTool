package com.tlcsdm.core.util.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AbstractCryptoUtil {

	/**
	 * 加密数据
	 * 
	 * @param data 待加密数据
	 */
	public abstract String encrypt(String data);

	/**
	 * 解密数据
	 * 
	 * @param data 待解密数据
	 */
	public abstract String decrypt(String data);

	/**
	 * 信息摘要算法
	 * 
	 * @param algorithm 算法类型
	 * @param data      要加密的字符串
	 * @return 返回加密后的摘要信息
	 */
	protected String encryptEncode(String algorithm, String data) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			return new String(md.digest(data.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
