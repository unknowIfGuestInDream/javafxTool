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

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 实现 同步阻塞的 EventBus
 *
 * @author unknowIfGuestInDream
 */
public class EventBus {
    private Executor executor;
    private final ObserverRegister register = new ObserverRegister();

    public EventBus() {
    }

    protected EventBus(Executor executor) {
        this.executor = executor;
    }

    public static EventBus getDefault() {
        return SingletonInstance.EVENT_BUS;
    }

    private static class SingletonInstance {
        private static final EventBus EVENT_BUS = new EventBus();
    }

    /**
     * 注册观察者
     */
    public void register(Object observer) {
        register.register(observer);
    }

    public void unregister(Object observer) {
        register.unregister(observer);
    }

    /**
     * 发布者-发送消息
     */
    public void post(Object event) {
        List<ObserverAction> observerActions = register.getMatchedObserverActions(event);
        for (ObserverAction observerAction : observerActions) {
            if (executor == null) {
                observerAction.execute(event);
            } else {
                executor.execute(() -> observerAction.execute(event));
            }
        }
    }
}
