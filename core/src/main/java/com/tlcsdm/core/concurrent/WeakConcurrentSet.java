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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import net.jcip.annotations.NotThreadSafe;

/**
 * <p>
 * A thread-safe set with weak values. Entries are based on a key's system hash code and keys are considered equal only by reference equality.
 * </p>
 * This class does not implement the {@link java.util.Set} interface because this implementation is incompatible
 * with the set contract. While iterating over a set's entries, any value that has not passed iteration is referenced non-weakly.
 *
 * @author unknowIfGuestInDream
 */
@NotThreadSafe
public class WeakConcurrentSet<V> implements Runnable, Iterable<V> {

    final WeakConcurrentMap<V, Boolean> target;

    public WeakConcurrentSet(Cleaner cleaner) {
        switch (cleaner) {
            case INLINE:
                target = new WeakConcurrentMap.WithInlinedExpunction<>();
                break;
            case THREAD:
            case MANUAL:
                target = new WeakConcurrentMap<>(cleaner == Cleaner.THREAD);
                break;
            default:
                throw new AssertionError();
        }
    }

    /**
     * @param value The value to add to the set.
     * @return {@code true} if the value was added to the set and was not contained before.
     */
    public boolean add(V value) {
        return target.put(value, Boolean.TRUE) == null; // is null or Boolean.TRUE
    }

    /**
     * @param value The value to check if it is contained in the set.
     * @return {@code true} if the set contains the value.
     */
    public boolean contains(V value) {
        return target.containsKey(value);
    }

    /**
     * @param value The value to remove from the set.
     * @return {@code true} if the value is contained in the set.
     */
    public boolean remove(V value) {
        return target.remove(value) != null; // is null or Boolean.TRUE
    }

    /**
     * Clears the set.
     */
    public void clear() {
        target.clear();
    }

    /**
     * Returns the approximate size of this set where the returned number is at least as big as the actual number of entries.
     *
     * @return The minimum size of this set.
     */
    public int approximateSize() {
        return target.approximateSize();
    }

    @Override
    public void run() {
        target.run();
    }

    /**
     * Determines the cleaning format. A reference is removed either by an explicitly started cleaner thread
     * associated with this instance ({@link Cleaner#THREAD}), as a result of interacting with this thread local
     * from any thread ({@link Cleaner#INLINE} or manually by submitting the detached thread local to a thread
     * ({@link Cleaner#MANUAL}).
     */
    public enum Cleaner {
        THREAD, INLINE, MANUAL
    }

    /**
     * Cleans all unused references.
     */
    public void expungeStaleEntries() {
        target.expungeStaleEntries();
    }

    /**
     * @return The cleaner thread or {@code null} if no such thread was set.
     */
    public Thread getCleanerThread() {
        return target.getCleanerThread();
    }

    @Override
    public Iterator<V> iterator() {
        return new ReducingIterator<V>(target.iterator());
    }

    @Override
    public String toString() {
        return Collections.newSetFromMap(target.target).toString();
    }

    private static class ReducingIterator<V> implements Iterator<V> {

        private final Iterator<Map.Entry<V, Boolean>> iterator;

        private ReducingIterator(Iterator<Map.Entry<V, Boolean>> iterator) {
            this.iterator = iterator;
        }

        @Override
        public void remove() {
            iterator.remove();
        }

        @Override
        public V next() {
            return iterator.next().getKey();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
    }
}
