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

package com.tlcsdm.core.eventbus;

import com.tlcsdm.core.exception.CoreException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Observer 注册表
 *
 * @author unknowIfGuestInDream
 */
public class ObserverRegister {
    // 注册表, 消息类型: 观察者方法
    private ConcurrentMap<Class<?>, CopyOnWriteArraySet<ObserverAction>> registry = new ConcurrentHashMap<>();

    /**
     * 将观察者注册到 注册表中
     *
     * @param observer 观察者
     */
    public void register(Object observer) {
        // 遍历带有注解的方法，将事件和对应的多个处理方法，存储到map中
        Map<Class<?>, Collection<ObserverAction>> observerActions = findAllObserverActions(observer);
        // 将获取到的单个观察者的可执行方法，放到如全局的map中，使用并发类
        for (Map.Entry<Class<?>, Collection<ObserverAction>> entry : observerActions.entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<ObserverAction> evenActions = entry.getValue();
            CopyOnWriteArraySet<ObserverAction> registryEvenActions = registry.getOrDefault(eventType,
                new CopyOnWriteArraySet<>());
            registryEvenActions.addAll(evenActions);
            registry.put(eventType, registryEvenActions);
        }
    }

    public void unregister(Object observer) {
        registry.values().forEach(o -> o.removeIf(b -> b.getTarget().equals(observer)));
        registry.entrySet().removeIf(e -> e.getValue().isEmpty());
    }

    /**
     * 获取匹配的观察者事件
     */
    public List<ObserverAction> getMatchedObserverActions(Object event) {
        List<ObserverAction> result = new ArrayList<>();
        Class<?> postedEventClass = event.getClass();
        for (Map.Entry<Class<?>, CopyOnWriteArraySet<ObserverAction>> entry : registry.entrySet()) {
            Class<?> eventClass = entry.getKey();
            // 匹配相同类型或父类型
            if (postedEventClass.isAssignableFrom(eventClass)) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }

    /**
     * 遍历带有注解的方法，将事件和对应的多个处理方法，存储到map中
     */
    public Map<Class<?>, Collection<ObserverAction>> findAllObserverActions(Object observer) {
        Map<Class<?>, Collection<ObserverAction>> result = new HashMap<>();
        // 观察者类型
        Class<?> observerClass = observer.getClass();
        for (Method method : getAnnotatedMethods(observerClass)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];
            result.putIfAbsent(eventType, new ArrayList<>());
            result.get(eventType).add(new ObserverAction(observer, method));
        }
        return result;
    }

    /**
     * 根据观察者类型，查找方法列表
     */
    public List<Method> getAnnotatedMethods(Class<?> clazz) {
        List<Method> result = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new CoreException("Method requires only one parameter.");
                }
                result.add(method);
            }
        }
        return result;
    }
}
