package com.tlcsdm.core.eventbus;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 实现 同步阻塞的 EventBus
 */
public class EventBus {
    private Executor executor;
    private ObserverRegister register = new ObserverRegister();

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
                executor.execute(() -> {
                    observerAction.execute(event);
                });
            }
        }
    }
}
