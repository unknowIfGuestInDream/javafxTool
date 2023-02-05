package com.tlcsdm.core.factory.config;

import com.tlcsdm.core.factory.InitializingFactory;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池初始化
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/2/5 8:36
 */
public final class ThreadPoolTaskExecutor implements InitializingFactory {
    private static int corePoolSize;
    private static int maximumPoolSize;
    private static long keepAliveTime;
    private static TimeUnit unit;
    private static int queueSize;
    private static String threadPreName;
    private static RejectedExecutionHandler handler;

    @Override
    public void initialize() throws Exception {
        corePoolSize = 2;
        maximumPoolSize = 50;
        keepAliveTime = 30;
        unit = TimeUnit.SECONDS;
        queueSize = 200;
        threadPreName = "sample-";
        handler = new ThreadPoolExecutor.CallerRunsPolicy();
    }

    public static ThreadPoolExecutor get() {
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        private static final ThreadPoolExecutor INSTANCE = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingQueue<>(queueSize),
                new BasicThreadFactory.Builder()
                        .namingPattern(threadPreName).build(),
                handler);
    }
}
