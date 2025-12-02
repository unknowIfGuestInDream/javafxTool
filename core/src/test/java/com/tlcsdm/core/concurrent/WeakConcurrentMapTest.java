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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

/**
 * @author unknowIfGuestInDream
 */
public class WeakConcurrentMapTest {

    @Test
    public void testLocalExpunction() throws Exception {
        final WeakConcurrentMap.WithInlinedExpunction<Object, Object> map = new WeakConcurrentMap.WithInlinedExpunction<>();
        Assertions.assertNull(map.getCleanerThread());
        new MapTestCase(map) {
            @Override
            protected void triggerClean() {
                map.expungeStaleEntries();
            }
        }.doTest();
    }

    @Test
    public void testExternalThread() throws Exception {
        WeakConcurrentMap<Object, Object> map = new WeakConcurrentMap<>(false);
        Assertions.assertNull(map.getCleanerThread());
        Thread thread = new Thread(map);
        thread.start();
        new MapTestCase(map).doTest();
        thread.interrupt();
        Thread.sleep(200L);
        Assertions.assertFalse(thread.isAlive());
    }

    @Test
    @DisabledIfSystemProperty(named = "workEnv", matches = "ci")
    public void testInternalThread() throws Exception {
        WeakConcurrentMap<Object, Object> map = new WeakConcurrentMap<>(true);
        Assertions.assertNotNull(map.getCleanerThread());
        new MapTestCase(map).doTest();
        map.getCleanerThread().interrupt();
        Thread.sleep(500L);
        Assertions.assertFalse(map.getCleanerThread().isAlive());
    }

    static class KeyEqualToWeakRefOfItself {

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof WeakReference<?>) {
                return equals(((WeakReference<?>) obj).get());
            }
            return super.equals(obj);
        }
    }

    static class CheapUnloadableWeakConcurrentMap extends AbstractWeakConcurrentMap<KeyEqualToWeakRefOfItself, Object, Object> {

        @Override
        protected Object getLookupKey(KeyEqualToWeakRefOfItself key) {
            return key;
        }

        @Override
        protected void resetLookupKey(Object lookupKey) {
        }
    }

    @Test
    public void testKeyWithWeakRefEquals() {
        CheapUnloadableWeakConcurrentMap map = new CheapUnloadableWeakConcurrentMap();

        KeyEqualToWeakRefOfItself key = new KeyEqualToWeakRefOfItself();
        Object value = new Object();
        map.put(key, value);
        Assertions.assertTrue(map.containsKey(key));
        Assertions.assertEquals(map.get(key), value);
        Assertions.assertEquals(map.putIfAbsent(key, new Object()), value);
        Assertions.assertEquals(map.remove(key), value);
        Assertions.assertFalse(map.containsKey(key));
    }

    private static class MapTestCase {

        private final WeakConcurrentMap<Object, Object> map;

        public MapTestCase(WeakConcurrentMap<Object, Object> map) {
            this.map = map;
        }

        void doTest() throws Exception {
            Object key1 = new Object(),
                value1 = new Object(),
                key2 = new Object(),
                value2 = new Object(),
                key3 = new Object(),
                value3 = new Object(),
                key4 = new Object(),
                value4 = new Object();
            map.put(key1, value1);
            map.put(key2, value2);
            map.put(key3, value3);
            map.put(key4, value4);
            Assertions.assertEquals(map.get(key1), value1);
            Assertions.assertEquals(map.get(key2), value2);
            Assertions.assertEquals(map.get(key3), value3);
            Assertions.assertEquals(map.get(key4), value4);
            Map<Object, Object> values = new HashMap<>();
            values.put(key1, value1);
            values.put(key2, value2);
            values.put(key3, value3);
            values.put(key4, value4);
            for (Map.Entry<Object, Object> entry : map) {
                Assertions.assertEquals(values.remove(entry.getKey()), entry.getValue());
            }
            Assertions.assertTrue(values.isEmpty());
            // Make eligible for GC
            key1 = key2 = null;
            System.gc();
            Thread.sleep(500L);
            triggerClean();
            Assertions.assertEquals(map.get(key3), value3);
            Assertions.assertEquals(map.getIfPresent(key3), value3);
            Assertions.assertEquals(map.get(key4), value4);
            Assertions.assertEquals(2, map.approximateSize());
            Assertions.assertEquals(2, map.target.size());
            Assertions.assertEquals(map.remove(key3), value3);
            Assertions.assertNull(map.get(key3));
            Assertions.assertNull(map.getIfPresent(key3));
            Assertions.assertEquals(map.get(key4), value4);
            Assertions.assertEquals(1, map.approximateSize());
            Assertions.assertEquals(1, map.target.size());
            map.clear();
            Assertions.assertNull(map.get(key3));
            Assertions.assertNull(map.get(key4));
            Assertions.assertEquals(0, map.approximateSize());
            Assertions.assertEquals(0, map.target.size());
            Assertions.assertFalse(map.iterator().hasNext());
        }

        protected void triggerClean() {
        }
    }
}
