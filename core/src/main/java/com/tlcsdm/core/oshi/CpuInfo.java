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

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <p>CPU相关信息</p>
 **/
public class CpuInfo {

    private static final DecimalFormat LOAD_FORMAT = new DecimalFormat("#.00");

    /**
     * CPU核心数
     */
    private Integer cpuNum;

    /**
     * CPU总的使用率
     */
    private double toTal;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU用户使用率
     */
    private double user;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;

    /**
     * CPU型号信息
     */
    private String cpuModel;

    /**
     * CPU型号信息
     */
    private CpuTicks ticks;

    /**
     * 空构造
     */
    public CpuInfo() {
    }

    /**
     * 构造，等待时间为用于计算在一定时长内的CPU负载情况，如传入1000表示最近1秒的负载情况
     *
     * @param processor   {@link CentralProcessor}
     * @param waitingTime 设置等待时间，单位毫秒
     */
    public CpuInfo(CentralProcessor processor, long waitingTime) {
        init(processor, waitingTime);
    }

    /**
     * 构造
     *
     * @param cpuNum   CPU核心数
     * @param toTal    CPU总的使用率
     * @param sys      CPU系统使用率
     * @param user     CPU用户使用率
     * @param wait     CPU当前等待率
     * @param free     CPU当前空闲率
     * @param cpuModel CPU型号信息
     */
    public CpuInfo(Integer cpuNum, double toTal, double sys, double user, double wait, double free, String cpuModel) {
        this.cpuNum = cpuNum;
        this.toTal = toTal;
        this.sys = sys;
        this.user = user;
        this.wait = wait;
        this.free = free;
        this.cpuModel = cpuModel;
    }

    public Integer getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(Integer cpuNum) {
        this.cpuNum = cpuNum;
    }

    public double getToTal() {
        return toTal;
    }

    public void setToTal(double toTal) {
        this.toTal = toTal;
    }

    public double getSys() {
        return sys;
    }

    public void setSys(double sys) {
        this.sys = sys;
    }

    public double getUser() {
        return user;
    }

    public void setUser(double user) {
        this.user = user;
    }

    public double getWait() {
        return wait;
    }

    public void setWait(double wait) {
        this.wait = wait;
    }

    public double getFree() {
        return free;
    }

    public void setFree(double free) {
        this.free = free;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public CpuTicks getTicks() {
        return ticks;
    }

    public void setTicks(CpuTicks ticks) {
        this.ticks = ticks;
    }

    /**
     * 获取用户+系统的总的CPU使用率
     *
     * @return 总CPU使用率
     */
    public double getUsed() {
        BigDecimal res = new BigDecimal(100);
        return res.subtract(new BigDecimal(this.free)).doubleValue();
    }

    @Override
    public String toString() {
        return "CpuInfo{" +
            "CPU核心数=" + cpuNum +
            ", CPU总的使用率=" + toTal +
            ", CPU系统使用率=" + sys +
            ", CPU用户使用率=" + user +
            ", CPU当前等待率=" + wait +
            ", CPU当前空闲率=" + free +
            ", CPU利用率=" + getUsed() +
            ", CPU型号信息='" + cpuModel + '\'' +
            '}';
    }

    /**
     * 获取指定等待时间内系统CPU 系统使用率、用户使用率、利用率等等 相关信息
     *
     * @param processor   {@link CentralProcessor}
     * @param waitingTime 设置等待时间，单位毫秒
     */
    private void init(CentralProcessor processor, long waitingTime) {
        final CpuTicks ticks = new CpuTicks(processor, waitingTime);
        this.ticks = ticks;

        this.cpuNum = processor.getLogicalProcessorCount();
        this.cpuModel = processor.toString();

        final long totalCpu = ticks.totalCpu();
        this.toTal = totalCpu;
        this.sys = formatDouble(ticks.cSys, totalCpu);
        this.user = formatDouble(ticks.user, totalCpu);
        this.wait = formatDouble(ticks.ioWait, totalCpu);
        this.free = formatDouble(ticks.idle, totalCpu);
    }

    /**
     * 获取每个CPU核心的tick，计算方式为 100 * tick / totalCpu
     *
     * @param tick     tick
     * @param totalCpu CPU总数
     * @return 平均每个CPU核心的tick
     */
    private static double formatDouble(long tick, long totalCpu) {
        if (0 == totalCpu) {
            return 0D;
        }
        return Double.parseDouble(LOAD_FORMAT.format(tick <= 0 ? 0 : (100d * tick / totalCpu)));
    }
}
