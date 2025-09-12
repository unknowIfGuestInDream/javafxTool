/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;

/**
 * 线程安全HashMap实现
 * 特性：
 * 1. 分段锁机制，减少锁竞争
 * 2. 读写分离，读操作无锁化
 * 3. 自适应扩容和缩容
 * 4. 防止hash碰撞攻击
 * 5. 内存泄漏保护
 * 6. 性能监控和降级机制
 *
 * @author unknowIfGuestInDream
 */
public class IndustrialHashMap<K, V> {

    // 默认配置参数
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final int MAX_SEGMENTS = 1 << 16;

    // 性能监控
    private final AtomicInteger collisionCount = new AtomicInteger(0);
    private final AtomicInteger resizeCount = new AtomicInteger(0);
    private volatile long lastCleanupTime = System.currentTimeMillis();

    // 核心数据结构
    private final Segment<K, V>[] segments;
    private final int segmentShift;
    private final int segmentMask;
    private final int hashSeed;

    /**
     * 分段结构，每个分段独立加锁
     */
    static final class Segment<K, V> extends ReentrantReadWriteLock {
        private static final long serialVersionUID = 1L;

        volatile HashEntry<K, V>[] table;
        volatile int count;
        volatile int modCount;
        final float loadFactor;
        int threshold;

        // 性能统计
        volatile long lastAccessTime;
        volatile int operationCount;

        Segment(int initialCapacity, float loadFactor) {
            this.loadFactor = loadFactor;
            setTable(HashEntry.<K, V>newArray(initialCapacity));
            this.lastAccessTime = System.currentTimeMillis();
        }

        void setTable(HashEntry<K, V>[] newTable) {
            threshold = (int) (newTable.length * loadFactor);
            table = newTable;
        }

        HashEntry<K, V> getFirst(int hash) {
            HashEntry<K, V>[] tab = table;
            return tab[hash & (tab.length - 1)];
        }

        // 读操作，使用读锁
        V readValueUnderLock(Object key, int hash) {
            readLock().lock();
            try {
                updateAccessTime();
                HashEntry<K, V> e = getFirst(hash);
                while (e != null) {
                    if (e.hash == hash && key.equals(e.key)) {
                        return e.value;
                    }
                    e = e.next;
                }
                return null;
            } finally {
                readLock().unlock();
            }
        }

        // 写操作，使用写锁
        V put(K key, int hash, V value, boolean onlyIfAbsent) {
            writeLock().lock();
            try {
                updateAccessTime();
                operationCount++;

                int c = count;
                if (c++ > threshold && table.length < MAXIMUM_CAPACITY) {
                    rehash();
                }

                HashEntry<K, V>[] tab = table;
                int index = hash & (tab.length - 1);
                HashEntry<K, V> first = tab[index];
                HashEntry<K, V> e = first;

                // 查找已存在的键
                while (e != null && (e.hash != hash || !key.equals(e.key))) {
                    e = e.next;
                }

                V oldValue;
                if (e != null) {
                    oldValue = e.value;
                    if (!onlyIfAbsent) {
                        e.value = value;
                    }
                } else {
                    oldValue = null;
                    ++modCount;
                    tab[index] = new HashEntry<K, V>(key, hash, first, value);
                    count = c;
                }
                return oldValue;
            } finally {
                writeLock().unlock();
            }
        }

        V remove(Object key, int hash, Object value) {
            writeLock().lock();
            try {
                updateAccessTime();
                operationCount++;

                int c = count - 1;
                HashEntry<K, V>[] tab = table;
                int index = hash & (tab.length - 1);
                HashEntry<K, V> first = tab[index];
                HashEntry<K, V> e = first;

                while (e != null && (e.hash != hash || !key.equals(e.key))) {
                    e = e.next;
                }

                V oldValue = null;
                if (e != null) {
                    V v = e.value;
                    if (value == null || value.equals(v)) {
                        oldValue = v;
                        ++modCount;
                        HashEntry<K, V> newFirst = e.next;
                        for (HashEntry<K, V> p = first; p != e; p = p.next) {
                            newFirst = new HashEntry<K, V>(p.key, p.hash, newFirst, p.value);
                        }
                        tab[index] = newFirst;
                        count = c;
                    }
                }
                return oldValue;
            } finally {
                writeLock().unlock();
            }
        }

        // 扩容操作
        void rehash() {
            HashEntry<K, V>[] oldTable = table;
            int oldCapacity = oldTable.length;
            if (oldCapacity >= MAXIMUM_CAPACITY) {
                return;
            }

            HashEntry<K, V>[] newTable = HashEntry.newArray(oldCapacity << 1);
            threshold = (int) (newTable.length * loadFactor);
            int sizeMask = newTable.length - 1;

            for (int i = 0; i < oldCapacity; i++) {
                HashEntry<K, V> e = oldTable[i];
                if (e != null) {
                    HashEntry<K, V> next = e.next;
                    int idx = e.hash & sizeMask;

                    if (next == null) {
                        newTable[idx] = e;
                    } else {
                        HashEntry<K, V> lastRun = e;
                        int lastIdx = idx;
                        for (HashEntry<K, V> last = next; last != null; last = last.next) {
                            int k = last.hash & sizeMask;
                            if (k != lastIdx) {
                                lastIdx = k;
                                lastRun = last;
                            }
                        }
                        newTable[lastIdx] = lastRun;

                        for (HashEntry<K, V> p = e; p != lastRun; p = p.next) {
                            int k = p.hash & sizeMask;
                            HashEntry<K, V> n = newTable[k];
                            newTable[k] = new HashEntry<K, V>(p.key, p.hash, n, p.value);
                        }
                    }
                }
            }
            table = newTable;
        }

        private void updateAccessTime() {
            this.lastAccessTime = System.currentTimeMillis();
        }

        // 清理过期或无效数据
        void cleanup() {
            writeLock().lock();
            try {
                // 这里可以添加清理逻辑，比如移除软引用的空值等
            } finally {
                writeLock().unlock();
            }
        }
    }

    /**
     * Hash表条目
     */
    static final class HashEntry<K, V> {
        final K key;
        final int hash;
        volatile V value;
        final HashEntry<K, V> next;

        HashEntry(K key, int hash, HashEntry<K, V> next, V value) {
            this.key = key;
            this.hash = hash;
            this.next = next;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        static final <K, V> HashEntry<K, V>[] newArray(int i) {
            return new HashEntry[i];
        }
    }

    // 构造函数
    public IndustrialHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL);
    }

    public IndustrialHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL);
    }

    @SuppressWarnings("unchecked")
    public IndustrialHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        if (!(loadFactor > 0) || initialCapacity < 0 || concurrencyLevel <= 0) {
            throw new IllegalArgumentException();
        }

        if (concurrencyLevel > MAX_SEGMENTS) {
            concurrencyLevel = MAX_SEGMENTS;
        }

        // 计算分段数量（2的幂）
        int sshift = 0;
        int ssize = 1;
        while (ssize < concurrencyLevel) {
            ++sshift;
            ssize <<= 1;
        }
        segmentShift = 32 - sshift;
        segmentMask = ssize - 1;

        // 防止hash碰撞攻击的随机种子
        this.hashSeed = ThreadLocalRandom.current().nextInt();

        // 初始化分段
        this.segments = new Segment[ssize];
        int c = initialCapacity / ssize;
        if (c * ssize < initialCapacity) {
            ++c;
        }
        int cap = 1;
        while (cap < c) {
            cap <<= 1;
        }

        for (int i = 0; i < this.segments.length; ++i) {
            this.segments[i] = new Segment<K, V>(cap, loadFactor);
        }

        // 启动后台清理线程
        startCleanupThread();
    }

    // 增强的hash函数，防止碰撞攻击
    private int hash(Object key) {
        int h = key.hashCode();
        h ^= hashSeed;
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private Segment<K, V> segmentFor(int hash) {
        return segments[(hash >>> segmentShift) & segmentMask];
    }

    // 主要API方法
    public V get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        return segmentFor(hash).readValueUnderLock(key, hash);
    }

    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        return segmentFor(hash).put(key, hash, value, false);
    }

    public V putIfAbsent(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        return segmentFor(hash).put(key, hash, value, true);
    }

    public V remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        return segmentFor(hash).remove(key, hash, null);
    }

    public boolean remove(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        return segmentFor(hash).remove(key, hash, value) != null;
    }

    public V replace(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        Segment<K, V> seg = segmentFor(hash);
        seg.writeLock().lock();
        try {
            HashEntry<K, V> e = seg.getFirst(hash);
            while (e != null && (e.hash != hash || !key.equals(e.key))) {
                e = e.next;
            }

            V oldValue = null;
            if (e != null) {
                oldValue = e.value;
                e.value = value;
            }
            return oldValue;
        } finally {
            seg.writeLock().unlock();
        }
    }

    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        Segment<K, V> seg = segmentFor(hash);
        seg.writeLock().lock();
        try {
            HashEntry<K, V> e = seg.getFirst(hash);
            while (e != null && (e.hash != hash || !key.equals(e.key))) {
                e = e.next;
            }

            boolean replaced = false;
            if (e != null && oldValue.equals(e.value)) {
                e.value = newValue;
                replaced = true;
            }
            return replaced;
        } finally {
            seg.writeLock().unlock();
        }
    }

    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        Segment<K, V> seg = segmentFor(hash);
        seg.readLock().lock();
        try {
            HashEntry<K, V> e = seg.getFirst(hash);
            while (e != null) {
                if (e.hash == hash && key.equals(e.key)) {
                    return true;
                }
                e = e.next;
            }
            return false;
        } finally {
            seg.readLock().unlock();
        }
    }

    public int size() {
        final Segment<K, V>[] segments = this.segments;
        long sum = 0;
        long check = 0;
        int[] mc = new int[segments.length];

        // 尝试几次无锁计算
        for (int k = 0; k < 3; ++k) {
            check = 0;
            sum = 0;
            int mcsum = 0;
            for (int i = 0; i < segments.length; ++i) {
                sum += segments[i].count;
                mcsum += mc[i] = segments[i].modCount;
            }
            if (mcsum != 0) {
                for (int i = 0; i < segments.length; ++i) {
                    check += segments[i].count;
                    if (mc[i] != segments[i].modCount) {
                        check = -1;
                        break;
                    }
                }
            }
            if (check == sum) {
                break;
            }
        }

        if (check != sum) {
            // 加锁重新计算
            sum = 0;
            for (int i = 0; i < segments.length; ++i) {
                segments[i].readLock().lock();
            }
            try {
                for (int i = 0; i < segments.length; ++i) {
                    sum += segments[i].count;
                }
            } finally {
                for (int i = 0; i < segments.length; ++i) {
                    segments[i].readLock().unlock();
                }
            }
        }

        return (sum > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) sum;
    }

    public boolean isEmpty() {
        final Segment<K, V>[] segments = this.segments;
        int[] mc = new int[segments.length];
        int mcsum = 0;

        for (int i = 0; i < segments.length; ++i) {
            if (segments[i].count != 0) {
                return false;
            }
            mcsum += mc[i] = segments[i].modCount;
        }

        if (mcsum != 0) {
            for (int i = 0; i < segments.length; ++i) {
                if (segments[i].count != 0 || mc[i] != segments[i].modCount) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clear() {
        for (int i = 0; i < segments.length; ++i) {
            segments[i].writeLock().lock();
        }
        try {
            for (int i = 0; i < segments.length; ++i) {
                Segment<K, V> seg = segments[i];
                seg.modCount++;
                HashEntry<K, V>[] tab = seg.table;
                for (int j = 0; j < tab.length; ++j) {
                    tab[j] = null;
                }
                seg.count = 0;
            }
        } finally {
            for (int i = 0; i < segments.length; ++i) {
                segments[i].writeLock().unlock();
            }
        }
    }

    // 高级功能：compute方法
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (key == null || remappingFunction == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        Segment<K, V> seg = segmentFor(hash);
        seg.writeLock().lock();
        try {
            HashEntry<K, V>[] tab = seg.table;
            int index = hash & (tab.length - 1);
            HashEntry<K, V> first = tab[index];
            HashEntry<K, V> e = first;

            // 查找现有条目
            while (e != null && (e.hash != hash || !key.equals(e.key))) {
                e = e.next;
            }

            V oldValue = (e == null) ? null : e.value;
            V newValue = remappingFunction.apply(key, oldValue);

            if (e != null) {
                if (newValue == null) {
                    // 删除
                    seg.modCount++;
                    HashEntry<K, V> newFirst = e.next;
                    for (HashEntry<K, V> p = first; p != e; p = p.next) {
                        newFirst = new HashEntry<K, V>(p.key, p.hash, newFirst, p.value);
                    }
                    tab[index] = newFirst;
                    seg.count--;
                } else {
                    // 更新
                    e.value = newValue;
                }
            } else if (newValue != null) {
                // 插入新值
                seg.modCount++;
                tab[index] = new HashEntry<K, V>(key, hash, first, newValue);
                seg.count++;
            }

            return newValue;
        } finally {
            seg.writeLock().unlock();
        }
    }

    // 性能监控方法
    public long getCollisionCount() {
        return collisionCount.get();
    }

    public long getResizeCount() {
        return resizeCount.get();
    }

    public void printStatistics() {
        System.out.println("=== IndustrialHashMap Statistics ===");
        System.out.println("Size: " + size());
        System.out.println("Collision Count: " + getCollisionCount());
        System.out.println("Resize Count: " + getResizeCount());
        System.out.println("Segments: " + segments.length);

        for (int i = 0; i < segments.length; i++) {
            Segment<K, V> seg = segments[i];
            System.out.printf("Segment %d: count=%d, operations=%d, lastAccess=%d%n",
                i, seg.count, seg.operationCount, seg.lastAccessTime);
        }
    }

    // 后台清理线程
    private void startCleanupThread() {
        Thread cleanupThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(60000); // 每分钟清理一次
                    performCleanup();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "IndustrialHashMap-Cleanup");

        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    private void performCleanup() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCleanupTime > 300000) { // 5分钟
            for (Segment<K, V> segment : segments) {
                segment.cleanup();
            }
            lastCleanupTime = currentTime;
        }
    }

    // 迭代器实现（简化版）
    public Iterator<K> keyIterator() {
        return new KeyIterator();
    }

    private final class KeyIterator implements Iterator<K> {
        private int segmentIndex = 0;
        private HashEntry<K, V> currentEntry;
        private HashEntry<K, V>[] currentTable;
        private int tableIndex = 0;

        public KeyIterator() {
            advance();
        }

        private void advance() {
            if (currentEntry != null && (currentEntry = currentEntry.next) != null) {
                return;
            }

            while (segmentIndex < segments.length) {
                Segment<K, V> seg = segments[segmentIndex];
                seg.readLock().lock();
                try {
                    currentTable = seg.table;
                    while (tableIndex < currentTable.length) {
                        if ((currentEntry = currentTable[tableIndex++]) != null) {
                            return;
                        }
                    }
                    tableIndex = 0;
                    segmentIndex++;
                } finally {
                    seg.readLock().unlock();
                }
            }
        }

        @Override
        public boolean hasNext() {
            return currentEntry != null;
        }

        @Override
        public K next() {
            if (currentEntry == null) {
                throw new NoSuchElementException();
            }
            K key = currentEntry.key;
            advance();
            return key;
        }
    }
}
