/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.NetworkIF;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.UsbDevice;
import oshi.hardware.VirtualMemory;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.Constants;
import oshi.util.EdidUtil;
import oshi.util.FormatUtil;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 */
@Disabled
public class OshiTest {

    @Test
    @Tag(value = "OS_HWInfo")
    void operatingSystem() {
        StringBuilder sb = new StringBuilder("Operating System: ");
        OperatingSystem os = OshiUtil.getOs();
        sb.append(os);
        sb.append("\n").append("Booted: ").append(Instant.ofEpochSecond(os.getSystemBootTime())).append('\n')
            .append("Uptime: ");
        sb.append(os.getSystemUptime());
        System.out.println(sb);
    }

    @Test
    @Tag(value = "OS_HWInfo")
    void processor() {
        StringBuilder sb = new StringBuilder();
        CentralProcessor proc = OshiUtil.getProcessor();
        sb.append(proc.toString());
        System.out.println(sb);
    }

    @Test
    @Tag(value = "OS_HWInfo")
    void displays() {
        StringBuilder sb = new StringBuilder();
        List<Display> displays = OshiUtil.getHardware().getDisplays();
        if (displays.isEmpty()) {
            sb.append("None detected.");
        } else {
            int i = 0;
            for (Display display : displays) {
                byte[] edid = display.getEdid();
                byte[][] desc = EdidUtil.getDescriptors(edid);
                String name = "Display " + i;
                for (byte[] b : desc) {
                    if (EdidUtil.getDescriptorType(b) == 0xfc) {
                        name = EdidUtil.getDescriptorText(b);
                    }
                }
                if (i++ > 0) {
                    sb.append('\n');
                }
                sb.append(name).append(": ");
                int hSize = EdidUtil.getHcm(edid);
                int vSize = EdidUtil.getVcm(edid);
                sb.append(String.format(Locale.ROOT, "%d x %d cm (%.1f x %.1f in)", hSize, vSize, hSize / 2.54,
                    vSize / 2.54));
            }
        }
        System.out.println(sb);
    }

    @Test
    @Tag(value = "OS_HWInfo")
    void hardwareInformation() {
        ComputerSystem computerSystem = OshiUtil.getSystem();
        System.out.println(computerSystem.getFirmware());
        System.out.println(computerSystem.getBaseboard());
        System.out.println(computerSystem);
    }

    @Test
    @Tag(value = "Memory")
    void phyMemory() {
        StringBuilder sb = new StringBuilder();
        List<PhysicalMemory> pmList = OshiUtil.getMemory().getPhysicalMemory();
        for (PhysicalMemory pm : pmList) {
            sb.append('\n').append(pm.toString());
        }
        System.out.println(sb);
        System.out.println(OshiUtil.getMemory());
    }

    @Test
    @Tag(value = "Memory")
    void virMemory() {
        System.out.println(OshiUtil.getMemory().getVirtualMemory().toString());
    }

    @Test
    @Tag(value = "Memory")
    void memoryUsed() {
        System.out.println("Phy Used: " + (OshiUtil.getMemory().getTotal() - OshiUtil.getMemory().getAvailable()));
        System.out.println("Phy Available: " + OshiUtil.getMemory().getAvailable());

        VirtualMemory virtualMemory = OshiUtil.getMemory().getVirtualMemory();
        System.out.println("Virtual Used: " + virtualMemory.getSwapUsed());
        System.out.println("Virtual Available: " + (virtualMemory.getSwapTotal() - virtualMemory.getSwapUsed()));
    }

    @Test
    @Tag(value = "CPU")
    void cpu() {
        System.out.println(OshiUtil.getCpuInfo());
    }

    @Test
    @Tag(value = "fileStore")
    void fileStore() {
        List<OSFileStore> fileStores = OshiUtil.getOs().getFileSystem().getFileStores();
        for (OSFileStore store : fileStores) {
            System.out.println(store.getName());
            System.out.println(store.getLabel());

            long usable = store.getUsableSpace();
            long total = store.getTotalSpace();
            System.out.println("Title: " + FormatUtil.formatBytes(usable) + "/" + FormatUtil.formatBytes(total));
            System.out.println("Used: " + ((double) total - usable));
            System.out.println("Available: " + usable);
        }
    }

    @Test
    @Tag(value = "processes")
    void processes() {
        List<OSProcess> list = OshiUtil.getOs().getProcesses(null, null, 0);
        long totalMem = OshiUtil.getMemory().getTotal();
        for (OSProcess p : list) {
            System.out.println("PID: " + p.getProcessID());
            System.out.println("PPID: " + p.getParentProcessID());
            System.out.println("Threads: " + p.getThreadCount());
            System.out.println(
                "% CPU: " + String.format(Locale.ROOT, "%.1f", 100d * p.getProcessCpuLoadBetweenTicks(p)));
            System.out.println(
                "Cumulative: " + String.format(Locale.ROOT, "%.1f", 100d * p.getProcessCpuLoadCumulative()));
            System.out.println("VSZ: " + FormatUtil.formatBytes(p.getVirtualSize()));
            System.out.println("RSS: " + FormatUtil.formatBytes(p.getResidentSetSize()));
            System.out.println(
                "% Memory: " + String.format(Locale.ROOT, "%.1f", 100d * p.getResidentSetSize() / totalMem));
            System.out.println("Process Name: " + p.getName());
        }
    }

    @Test
    @Tag(value = "USB")
    void usb() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (UsbDevice usbDevice : OshiUtil.getHardware().getUsbDevices(true)) {
            if (first) {
                first = false;
            } else {
                sb.append('\n');
            }
            sb.append(usbDevice);
        }
        System.out.println(sb);
    }

    @Test
    @Tag(value = "network")
    void networkParameters() {
        NetworkParams params = OshiUtil.getOs().getNetworkParams();
        StringBuilder sb = new StringBuilder("Host Name: ").append(params.getHostName());
        if (!params.getDomainName().isEmpty()) {
            sb.append("\nDomain Name: ").append(params.getDomainName());
        }
        sb.append("\nIPv4 Default Gateway: ").append(params.getIpv4DefaultGateway());
        if (!params.getIpv6DefaultGateway().isEmpty()) {
            sb.append("\nIPv6 Default Gateway: ").append(params.getIpv6DefaultGateway());
        }
        sb.append("\nDNS Servers: ").append(getIPAddressesString(params.getDnsServers()));
        System.out.println(sb);
    }

    @Test
    @Tag(value = "network")
    void networkInterfaces() {
        List<NetworkIF> networkIfList = OshiUtil.getHardware().getNetworkIFs(true);
        for (NetworkIF intf : networkIfList) {
            System.out.println("Name: " + intf.getName());
            System.out.println("Index: " + intf.getIndex());
            System.out.println("Speed: " + intf.getSpeed());
            System.out.println("IPv4 Address: " + getIPAddressesString(intf.getIPv4addr()));
            System.out.println("IPv6 address: " + getIPAddressesString(intf.getIPv6addr()));
            System.out.println(
                "MAC address: " + (Constants.UNKNOWN.equals(intf.getMacaddr()) ? "" : intf.getMacaddr()));
        }
    }

    private String getIPAddressesString(String[] ipAddressArr) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (String ipAddress : ipAddressArr) {
            if (first) {
                first = false;
            } else {
                sb.append("; ");
            }
            sb.append(ipAddress);
        }

        return sb.toString();
    }

}
