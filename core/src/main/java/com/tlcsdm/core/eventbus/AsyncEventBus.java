package com.tlcsdm.core.eventbus;

import com.tlcsdm.core.factory.config.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 异步非阻塞的EventBus
 */
public class AsyncEventBus extends EventBus {
    public AsyncEventBus(Executor executor) {
        super(executor);
    }

    public static AsyncEventBus getDefault() {
        return SingletonInstance.EVENT_BUS;
    }

    private static class SingletonInstance {
        private static final AsyncEventBus EVENT_BUS = new AsyncEventBus(
            ThreadPoolTaskExecutor.hasInitialized() ? ThreadPoolTaskExecutor.get()
                : Executors.newSingleThreadExecutor());
    }
}
