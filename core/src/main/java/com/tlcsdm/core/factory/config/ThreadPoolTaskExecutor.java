package com.tlcsdm.core.factory.config;

import com.tlcsdm.core.factory.InitializingFactory;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/2/5 8:36
 */
public class ThreadPoolTaskExecutor implements InitializingFactory {
    public static ThreadPoolExecutor CORE_THREADPOOL;

    @Override
    public void initialize() throws Exception {
        CORE_THREADPOOL = new ThreadPoolExecutor(2,
                50,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(200),
                new BasicThreadFactory.Builder()
                        .namingPattern("sample-").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static ThreadPoolExecutor get() {
        return CORE_THREADPOOL;
    }
}
