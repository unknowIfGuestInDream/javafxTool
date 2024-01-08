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

/**
 * <p>
 * A detached local that allows for explicit control of setting and removing values from a thread-local
 * context.
 * </p>
 * Instances of this class are non-blocking and fully thread safe.
 *
 * @author unknowIfGuestInDream
 */
public class DetachedThreadLocal<T> implements Runnable {

    final WeakConcurrentMap<Thread, T> map;

    public DetachedThreadLocal(Cleaner cleaner) {
        switch (cleaner) {
            case THREAD:
            case MANUAL:
                map = new WeakConcurrentMap<>(cleaner == Cleaner.THREAD) {
                    @Override
                    protected T defaultValue(Thread key) {
                        return DetachedThreadLocal.this.initialValue(key);
                    }
                };
                break;
            case INLINE:
                map = new WeakConcurrentMap.WithInlinedExpunction<>() {
                    @Override
                    protected T defaultValue(Thread key) {
                        return DetachedThreadLocal.this.initialValue(key);
                    }
                };
                break;
            default:
                throw new AssertionError();
        }
    }

    public T get() {
        return map.get(Thread.currentThread());
    }

    public T getIfPresent() {
        return map.getIfPresent(Thread.currentThread());
    }

    public void set(T value) {
        map.put(Thread.currentThread(), value);
    }

    public void clear() {
        map.remove(Thread.currentThread());
    }

    /**
     * Clears all thread local references for all threads.
     */
    public void clearAll() {
        map.clear();
    }

    /**
     * @param thread The thread to which this thread's thread local value should be pushed.
     * @return The value being set.
     */
    public T pushTo(Thread thread) {
        T value = get();
        if (value != null) {
            map.put(thread, inheritValue(value));
        }
        return value;
    }

    /**
     * @param thread The thread from which the thread thread local value should be fetched.
     * @return The value being set.
     */
    public T fetchFrom(Thread thread) {
        T value = map.get(thread);
        if (value != null) {
            set(inheritValue(value));
        }
        return value;
    }

    /**
     * @param thread The thread for which to set a thread-local value.
     * @return The value accociated with this thread.
     */
    public T get(Thread thread) {
        return map.get(thread);
    }

    /**
     * @param thread The thread for which to set a thread-local value.
     * @param value  The value to set.
     */
    public void define(Thread thread, T value) {
        map.put(thread, value);
    }

    /**
     * @param thread The thread for which an initial value is created.
     * @return The initial value for any thread local. If no default is set, the default value is {@code null}.
     */
    protected T initialValue(Thread thread) {
        return null;
    }

    /**
     * @param value The value that is inherited.
     * @return The inherited value.
     */
    protected T inheritValue(T value) {
        return value;
    }

    /**
     * @return The weak map that backs this detached thread local.
     */
    public WeakConcurrentMap<Thread, T> getBackingMap() {
        return map;
    }

    @Override
    public void run() {
        map.run();
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
}
