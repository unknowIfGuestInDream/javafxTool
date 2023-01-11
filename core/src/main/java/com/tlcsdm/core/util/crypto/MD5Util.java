package com.tlcsdm.core.util.crypto;

import com.tlcsdm.core.exception.UnsupportedMethodException;

public class MD5Util extends AbstractCryptoUtil {

	@Override
	public String encrypt(String data) {
		return encryptEncode("MD5", data);
	}

	@Override
	public String decrypt(String data) {
		throw new UnsupportedMethodException();
	}

}
