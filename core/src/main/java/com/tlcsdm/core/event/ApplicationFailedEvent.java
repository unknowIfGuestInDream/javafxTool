package com.tlcsdm.core.event;

/**
 * 程序启动失败事件
 */
public class ApplicationFailedEvent extends ApplicationEvent {
    private Throwable exception;

    public ApplicationFailedEvent(Throwable exception) {
        this.exception = exception;
    }

    public Throwable getException() {
        return this.exception;
    }
}
