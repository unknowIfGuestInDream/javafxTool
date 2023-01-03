package com.tlcsdm.core.exception;

/**
 * @author: 唐 亮
 * @date: 2023/1/3 21:08
 */
public class CoreException extends RuntimeException {
    public CoreException() {
    }

    public CoreException(String message) {
        super(message);
    }

    public CoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreException(Throwable cause) {
        super(cause);
    }
}
