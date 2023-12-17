/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.factory;

import cn.hutool.log.StaticLog;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonFactory {

    private static final Map<Class<?>, Object> instaces = new ConcurrentHashMap<>();

    private static final Map<Class<?>, WeakReference<Object>> weakReferenceInstaces = new ConcurrentHashMap<>();

    /**
     * 创建可不被回收的单例模式,当没有对象引用，单例对象将被gc掉.
     *
     * @param className Class Name
     */
    @SuppressWarnings("unchecked")
    public static <E> E getInstace(Class<E> className) {
        Object instace = instaces.get(className);
        if (instace == null) {
            synchronized (SingletonFactory.class) {
                instace = instaces.get(className);
                if (instace == null) {
                    try {
                        instace = className.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        StaticLog.error(e);
                    }
                    instaces.put(className, instace);
                }
            }
        }
        return (E) instace;
    }

    /**
     * 创建可回收的单例模式,当没有对象引用，单例对象将被gc掉.
     *
     * @param className Class Name
     */
    @SuppressWarnings("unchecked")
    public static <E> E getWeakInstace(Class<E> className) {
        WeakReference<Object> reference = weakReferenceInstaces.get(className);
        Object instace = reference == null ? null : reference.get();
        if (instace == null) {
            synchronized (SingletonFactory.class) {
                reference = weakReferenceInstaces.get(className);
                instace = reference == null ? null : reference.get();
                if (instace == null) {
                    try {
                        instace = className.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        StaticLog.error(e);
                    }
                    weakReferenceInstaces.put(className, new WeakReference<>(instace));
                }
            }
        }
        return (E) instace;
    }

}
