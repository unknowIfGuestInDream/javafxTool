package com.tlcsdm.core.exception;

/**
 * 不符合预期的数据异常
 *
 * @author: TangLiang
 * @date: 2022/1/7 9:21
 * @since: 1.0
 */
public class UnExpectedResultException extends RuntimeException {
    public UnExpectedResultException() {
    }

    public UnExpectedResultException(String message) {
        super(message);
    }

    public UnExpectedResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnExpectedResultException(Throwable cause) {
        super(cause);
    }
}
