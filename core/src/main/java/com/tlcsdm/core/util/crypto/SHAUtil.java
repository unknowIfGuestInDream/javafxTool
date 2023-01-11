package com.tlcsdm.core.util.crypto;

import com.tlcsdm.core.exception.UnsupportedMethodException;

public class SHAUtil extends AbstractCryptoUtil {

	@Override
	public String encrypt(String data) {
		return encryptEncode("SHA", data);
	}

	@Override
	public String decrypt(String data) {
		throw new UnsupportedMethodException();
	}
}
