/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.core.factory.config;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.annotation.Order;
import com.tlcsdm.core.concurrent.VerboseScheduledExecutorService;
import com.tlcsdm.core.factory.InitializingFactory;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时线程池初始化.
 *
 * @author unknowIfGuestInDream
 */
@Order(2)
public final class ScheduledTaskExecutor implements InitializingFactory {
    private static int corePoolSize;
    private static String threadPreName;
    private static RejectedExecutionHandler handler;
    private static final AtomicBoolean hasInitialized = new AtomicBoolean();

    @Override
    public void initialize() throws Exception {
        corePoolSize = 2;
        threadPreName = "schedule-%d";
        handler = new ScheduledThreadPoolExecutor.AbortPolicy();
        hasInitialized.set(true);
    }

    public static VerboseScheduledExecutorService get() {
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        private static final VerboseScheduledExecutorService INSTANCE = new VerboseScheduledExecutorService(
            new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern(threadPreName).daemon(true)
                    .uncaughtExceptionHandler((t, e) -> StaticLog.error("Thread threw exception: " + t, e)).build(), handler));
    }

    public static boolean hasInitialized() {
        return hasInitialized.get();
    }
}
