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

package com.tlcsdm.core.oshi;

import oshi.hardware.CentralProcessor;
import oshi.util.Util;

/**
 * CPU负载时间信息
 */
public class CpuTicks {

    long idle;
    long nice;
    long irq;
    long softIrq;
    long steal;
    long cSys;
    long user;
    long ioWait;

    /**
     * 构造，等待时间为用于计算在一定时长内的CPU负载情况，如传入1000表示最近1秒的负载情况
     *
     * @param processor   {@link CentralProcessor}
     * @param waitingTime 设置等待时间，单位毫秒
     */
    public CpuTicks(CentralProcessor processor, long waitingTime) {
        // CPU信息
        final long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 这里必须要设置延迟
        Util.sleep(waitingTime);
        final long[] ticks = processor.getSystemCpuLoadTicks();

        this.idle = tick(prevTicks, ticks, CentralProcessor.TickType.IDLE);
        this.nice = tick(prevTicks, ticks, CentralProcessor.TickType.NICE);
        this.irq = tick(prevTicks, ticks, CentralProcessor.TickType.IRQ);
        this.softIrq = tick(prevTicks, ticks, CentralProcessor.TickType.SOFTIRQ);
        this.steal = tick(prevTicks, ticks, CentralProcessor.TickType.STEAL);
        this.cSys = tick(prevTicks, ticks, CentralProcessor.TickType.SYSTEM);
        this.user = tick(prevTicks, ticks, CentralProcessor.TickType.USER);
        this.ioWait = tick(prevTicks, ticks, CentralProcessor.TickType.IOWAIT);
    }

    public long getIdle() {
        return idle;
    }

    public void setIdle(long idle) {
        this.idle = idle;
    }

    public long getNice() {
        return nice;
    }

    public void setNice(long nice) {
        this.nice = nice;
    }

    public long getIrq() {
        return irq;
    }

    public void setIrq(long irq) {
        this.irq = irq;
    }

    public long getSoftIrq() {
        return softIrq;
    }

    public void setSoftIrq(long softIrq) {
        this.softIrq = softIrq;
    }

    public long getSteal() {
        return steal;
    }

    public void setSteal(long steal) {
        this.steal = steal;
    }

    public long getcSys() {
        return cSys;
    }

    public void setcSys(long cSys) {
        this.cSys = cSys;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getIoWait() {
        return ioWait;
    }

    public void setIoWait(long ioWait) {
        this.ioWait = ioWait;
    }

    /**
     * 获取CPU总的使用率
     *
     * @return CPU总使用率
     */
    public long totalCpu() {
        return Math.max(user + nice + cSys + idle + ioWait + irq + softIrq + steal, 0);
    }

    @Override
    public String toString() {
        return "CpuTicks{" +
            "idle=" + idle +
            ", nice=" + nice +
            ", irq=" + irq +
            ", softIrq=" + softIrq +
            ", steal=" + steal +
            ", cSys=" + cSys +
            ", user=" + user +
            ", ioWait=" + ioWait +
            '}';
    }

    /**
     * 获取一段时间内的CPU负载标记差
     *
     * @param prevTicks 开始的ticks
     * @param ticks     结束的ticks
     * @param tickType  tick类型
     * @return 标记差
     */
    private static long tick(long[] prevTicks, long[] ticks, CentralProcessor.TickType tickType) {
        return ticks[tickType.getIndex()] - prevTicks[tickType.getIndex()];
    }
}
