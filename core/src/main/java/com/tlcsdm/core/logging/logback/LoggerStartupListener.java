package com.tlcsdm.core.logging.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <pre>
 * {@code
 * <configuration debug="true" scan="true" scanPeriod="60 seconds">
 * <contextListener class="com.tlcsdm.core.logging.logback.LoggerStartupListener" />
 * </configuration>
 * 以上配置代表logback每隔60秒扫描logback.xml的更改，并将改动加载到系统，实现动态配置
 * ${userName}
 * }
 * </pre>
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/2/12 21:34
 */
public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

    private static final AtomicBoolean STARTED = new AtomicBoolean(false);

    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext context) {

    }

    @Override
    public void onReset(LoggerContext context) {

    }

    @Override
    public void onStop(LoggerContext context) {

    }

    @Override
    public void onLevelChange(Logger logger, Level level) {

    }

    @Override
    public void start() {
        if (!STARTED.compareAndSet(false, true)) {
            return;
        }
        String userHome = System.getProperty("user.home");
        Context context = getContext();
        context.putProperty("userHome", userHome);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return STARTED.get();
    }
}
