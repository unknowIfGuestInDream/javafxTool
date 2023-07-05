package com.tlcsdm.core.logging.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.log.StaticLog;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 配置规则类, 用于日志规则获取ip地址
 * <pre>
 * {@code
 * <conversionRule conversionWord="ip" converterClass="com.tlcsdm.core.logging.logback.IPLogConverter"/>
 * <pattern>%ip</pattern>
 * }
 * </pre>
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/2/12 21:17
 */
public class IPLogConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            StaticLog.error("Fail to get ip", e);
        }
        return "UNKNOW";
    }
}
