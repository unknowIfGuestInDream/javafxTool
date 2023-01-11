package com.tlcsdm.core.util;

import org.junit.jupiter.api.Test;

import com.tlcsdm.core.util.crypto.MD5Util;
import com.tlcsdm.core.util.crypto.SHAUtil;

/**
 * 加密解密工具测试
 */
public class CryptoUtilTest {
	private String data = "unknowIfGuestInDream";

	@Test
	public void MD5() {
		MD5Util md5Util = new MD5Util();
		System.out.println(md5Util.encrypt(data));
	}

	@Test
	public void SHA() {
		SHAUtil s = new SHAUtil();
		System.out.println(s.encrypt(data));
	}

}
