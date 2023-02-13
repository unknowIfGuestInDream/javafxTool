package com.tlcsdm.core.logging.logback;

import ch.qos.logback.core.PropertyDefinerBase;
import cn.hutool.log.StaticLog;

import java.net.InetAddress;
import java.net.UnknownHostException;

/***
 * 将本地IP拼接到日志文件名中，以区分不同实例，避免存储到同一位置时的覆盖冲突问题
 * <p>
 *     <define name="localIP" class="com.tlcsdm.core.logging.logback.IPLogDefiner"/>
 *     <File>D:\\logs\\elk\\interface-${localIP}.log</File>
 *     <parttern>${localIP}</parttern>
 * </p>
 * @author: unknowIfGuestInDream
 * @date: 2023/2/12 21:24
 */
public class IPLogDefiner extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            StaticLog.error("Fail to get ip", e);
        }
        return "UNKNOW";
    }
}
