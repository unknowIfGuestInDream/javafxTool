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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author unknowIfGuestInDream
 */
public class WeakConcurrentSetTest {

    @Test
    public void testLocalExpunction() throws Exception {
        final WeakConcurrentSet<Object> set = new WeakConcurrentSet<Object>(WeakConcurrentSet.Cleaner.INLINE);
        Assertions.assertNull(set.getCleanerThread());
        new SetTestCase(set) {
            @Override
            protected void triggerClean() {
                set.target.expungeStaleEntries();
            }
        }.doTest();
    }

    @Test
    public void testExternalThread() throws Exception {
        WeakConcurrentSet<Object> set = new WeakConcurrentSet<Object>(WeakConcurrentSet.Cleaner.MANUAL);
        Assertions.assertNull(set.getCleanerThread());
        Thread thread = new Thread(set);
        thread.start();
        new SetTestCase(set).doTest();
        thread.interrupt();
        Thread.sleep(200L);
        Assertions.assertFalse(thread.isAlive());
    }

    @Test
    @DisabledIfSystemProperty(named = "workEnv", matches = "ci")
    public void testInternalThread() throws Exception {
        WeakConcurrentSet<Object> set = new WeakConcurrentSet<Object>(WeakConcurrentSet.Cleaner.THREAD);
        Assertions.assertNotNull(set.getCleanerThread());
        new SetTestCase(set).doTest();
        set.getCleanerThread().interrupt();
        Thread.sleep(500L);
        Assertions.assertFalse(set.getCleanerThread().isAlive());
    }

    private static class SetTestCase {

        private final WeakConcurrentSet<Object> set;

        public SetTestCase(WeakConcurrentSet<Object> set) {
            this.set = set;
        }

        void doTest() throws Exception {
            Object value1 = new Object(), value2 = new Object(), value3 = new Object(), value4 = new Object();
            set.add(value1);
            set.add(value2);
            set.add(value3);
            set.add(value4);
            Assertions.assertTrue(set.contains(value1));
            Assertions.assertTrue(set.contains(value2));
            Assertions.assertTrue(set.contains(value3));
            Assertions.assertTrue(set.contains(value4));
            Set<Object> values = new HashSet<Object>(Arrays.asList(value1, value2, value3, value4));
            for (Object value : set) {
                Assertions.assertTrue(values.remove(value));
            }
            Assertions.assertTrue(values.isEmpty());
            // Make eligible for GC
            value1 = value2 = null;
            System.gc();
            Thread.sleep(200L);
            triggerClean();
            Assertions.assertTrue(set.contains(value3));
            Assertions.assertTrue(set.contains(value4));
            Assertions.assertEquals(2, set.approximateSize());
            Assertions.assertEquals(2, set.target.target.size());
            Assertions.assertTrue(set.remove(value3));
            Assertions.assertFalse(set.contains(value3));
            Assertions.assertTrue(set.contains(value4));
            Assertions.assertEquals(1, set.approximateSize());
            Assertions.assertEquals(1, set.target.target.size());
            set.clear();
            Assertions.assertFalse(set.contains(value3));
            Assertions.assertFalse(set.contains(value4));
            Assertions.assertEquals(0, set.approximateSize());
            Assertions.assertEquals(0, set.target.target.size());
            Assertions.assertFalse(set.iterator().hasNext());
        }

        protected void triggerClean() {
        }
    }

    @Test
    public void testSetContract() {
        final WeakConcurrentSet<Object> set = new WeakConcurrentSet<Object>(WeakConcurrentSet.Cleaner.INLINE);
        Object obj = new Object();
        Assertions.assertFalse(set.contains(obj));
        Assertions.assertFalse(set.remove(obj));
        Assertions.assertTrue(set.add(obj));
        Assertions.assertFalse(set.add(obj));
        Assertions.assertTrue(set.contains(obj));
        Assertions.assertTrue(set.remove(obj));
        Assertions.assertFalse(set.contains(obj));
        Assertions.assertFalse(set.remove(obj));
    }
}
