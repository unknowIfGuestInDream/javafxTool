/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author unknowIfGuestInDream
 */
public class DetachedThreadLocalTest {

    @Test
    public void testLocalExpunction() throws Exception {
        final DetachedThreadLocal<Object> threadLocal = new DetachedThreadLocal<>(
            DetachedThreadLocal.Cleaner.INLINE);
        Assertions.assertNull(threadLocal.getBackingMap().getCleanerThread());
        new ThreadLocalTestCase(threadLocal) {
            @Override
            protected void triggerClean() {
                threadLocal.map.expungeStaleEntries();
            }
        }.doTest();
    }

    @Test
    public void testExternalThread() throws Exception {
        DetachedThreadLocal<Object> threadLocal = new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.MANUAL);
        Assertions.assertNull(threadLocal.getBackingMap().getCleanerThread());
        Thread thread = new Thread(threadLocal);
        thread.start();
        new ThreadLocalTestCase(threadLocal).doTest();
        thread.interrupt();
        Thread.sleep(200L);
        Assertions.assertFalse(thread.isAlive());
    }

    @Test
    @DisabledIfSystemProperty(named = "workEnv", matches = "ci")
    public void testInternalThread() throws Exception {
        DetachedThreadLocal<Object> threadLocal = new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.THREAD);
        Assertions.assertNotNull(threadLocal.getBackingMap().getCleanerThread());
        new ThreadLocalTestCase(threadLocal).doTest();
        threadLocal.getBackingMap().getCleanerThread().interrupt();
        Thread.sleep(500L);
        Assertions.assertFalse(threadLocal.getBackingMap().getCleanerThread().isAlive());
    }

    private static class ThreadLocalTestCase {

        private final DetachedThreadLocal<Object> threadLocal;

        public ThreadLocalTestCase(DetachedThreadLocal<Object> threadLocal) {
            this.threadLocal = threadLocal;
        }

        void doTest() throws Exception {
            int size = 100;
            List<Thread> threads = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Thread thread = new Thread(new ThreadLocalInteraction(threadLocal));
                threads.add(thread);
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
            Assertions.assertEquals(threadLocal.map.target.size(), size);
            threads.clear();
            System.gc();
            Thread.sleep(1500L);
            System.gc();
            Thread.sleep(1500L);
            triggerClean();
            Assertions.assertEquals(threadLocal.map.target.size(), 0);
        }

        void triggerClean() {
        }
    }

    private static class ThreadLocalInteraction implements Runnable {

        private final DetachedThreadLocal<Object> threadLocal;

        ThreadLocalInteraction(DetachedThreadLocal<Object> threadLocal) {
            this.threadLocal = threadLocal;
        }

        @Override
        public void run() {
            Object value = new Object();
            threadLocal.set(value);
            Assertions.assertEquals(threadLocal.get(), value);
        }
    }
}
