package com.tlcsdm.core.exception;

/**
 * 不支持的方法 </br>
 * 通常用于在实现类中调用
 */
public class UnsupportedMethodException extends RuntimeException {
	public UnsupportedMethodException() {
	}

	public UnsupportedMethodException(String message) {
		super(message);
	}

	public UnsupportedMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedMethodException(Throwable cause) {
		super(cause);
	}
}
