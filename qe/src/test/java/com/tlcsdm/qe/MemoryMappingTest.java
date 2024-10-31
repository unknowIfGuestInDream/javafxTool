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

package com.tlcsdm.qe;

import cn.hutool.core.io.resource.ResourceUtil;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author unknowIfGuestInDream
 */
public class MemoryMappingTest {

    private final String optionsFlag = "*** Options ***";
    private final String symbolFlag = "*** Symbol List ***";
    private final String romStartFlag = "-ROm=";
    private final String sectionStartFlag = "SECTION=";
    private final String endFlag = "*** ";
    private final String globalVarFlag = "data ,g";
    List<String> sections = new ArrayList<>();
    List<Symbol> symbols = new ArrayList<>();
    private boolean inOptions = false;
    private boolean inSymbol = false;
    private boolean inRomSectionFlag = false;

    @Test
    public void read() throws IOException {
        File file = new File(ResourceUtil.getResource("cs+.map").getPath());
        List<String> content = FileUtils.readLines(file, StandardCharsets.UTF_8);
        String previousLine = "";
        for (String line : content) {
            if (!inOptions && !inSymbol && line.equals(optionsFlag)) {
                inOptions = true;
            } else if (inOptions) {
                if (line.startsWith(romStartFlag)) {
                    String[] section = line.split("=", 3);
                    if (!section[2].isEmpty()) {
                        sections.add(section[2]);
                    }
                } else if (line.startsWith(endFlag) && !line.equals(optionsFlag)) {
                    inOptions = false;
                }
            }

            if (!inOptions && !inSymbol && line.equals(symbolFlag)) {
                inSymbol = true;
            } else if (inSymbol) {
                if (line.startsWith(sectionStartFlag)) {
                    String sect = line.substring(sectionStartFlag.length());
                    if (sections.contains(sect)) {
                        inRomSectionFlag = true;
                    } else {
                        inRomSectionFlag = false;
                    }
                } else if (inRomSectionFlag) {
                    if (line.contains(globalVarFlag)) {
                        String[] data = line.split("\\s+");
                        symbols.add(new Symbol(previousLine.trim(), data[1],
                            Integer.parseInt(data[2])));
                    }
                }
                if (line.startsWith(endFlag) && !line.equals(symbolFlag)) {
                    inSymbol = false;
                    break;
                }
            }
            previousLine = line;
        }
        sections = null;
        content = null;
        System.out.println(symbols);
        Assertions.assertEquals(
            "[Symbol[name=_g_send_busy_flg, address=000fd3d7, size=1], Symbol[name=_g_run_ack_flg, address=000fd3d8, size=1], Symbol[name=_g_variable_flg, address=000fd3d9, size=1], Symbol[name=_request_flag, address=000fd3da, size=1], Symbol[name=_A1, address=000fd3ee, size=4], Symbol[name=_A2, address=000fd3f2, size=4], Symbol[name=_g_led1_feedback_is_enabled, address=000fd3f8, size=1], Symbol[name=_g_led2_feedback_is_enabled, address=000fd3fc, size=1], Symbol[name=_g_led3_feedback_is_enabled, address=000fd400, size=1], Symbol[name=_count, address=000fd404, size=2], Symbol[name=_maxCount, address=000fd406, size=2], Symbol[name=_timer_flag, address=000fd408, size=1], Symbol[name=_g_tx_end_flag, address=000fd40a, size=1], Symbol[name=_g_rx_end_flag, address=000fd40b, size=1], Symbol[name=_g_u08_change_interrupt_vector_flag, address=000fde30, size=1], Symbol[name=_g_u08_cpu_frequency, address=000fde31, size=1], Symbol[name=_g_u08_fset_cpu_frequency, address=000fde32, size=1], Symbol[name=_g_led1_vr, address=000ffe22, size=2], Symbol[name=_g_led1_fb_ad, address=000ffe24, size=2], Symbol[name=_g_led1_fb_ad_old, address=000ffe26, size=2], Symbol[name=_g_led1_offset, address=000ffe28, size=2], Symbol[name=_g_led1_duty, address=000ffe2a, size=4], Symbol[name=_g_led1_err, address=000ffe2e, size=4], Symbol[name=_g_led2_vr, address=000ffe32, size=2], Symbol[name=_g_led2_fb_ad, address=000ffe34, size=2], Symbol[name=_g_led2_fb_ad_old, address=000ffe36, size=2], Symbol[name=_g_led2_offset, address=000ffe38, size=2], Symbol[name=_g_led2_duty, address=000ffe3a, size=4], Symbol[name=_g_led2_err, address=000ffe3e, size=4], Symbol[name=_g_led3_vr, address=000ffe42, size=2], Symbol[name=_g_led3_fb_ad, address=000ffe44, size=2], Symbol[name=_g_led3_fb_ad_old, address=000ffe46, size=2], Symbol[name=_g_led3_offset, address=000ffe48, size=2], Symbol[name=_g_led3_duty, address=000ffe4a, size=4], Symbol[name=_g_led3_err, address=000ffe4e, size=4]]",
            symbols.toString());
    }

    private final String iarSymbolFlag = "*** ENTRY LIST";
    private final String iarGlobalVarFlag = "Data  Gb";

    @Test
    public void readIarMap() throws IOException {
        File file = new File(ResourceUtil.getResource("iar.map").getPath());
        List<String> content = FileUtils.readLines(file, StandardCharsets.UTF_8);
        String previousLine = "";
        for (String line : content) {
            if (!inSymbol) {
                if (line.equals(iarSymbolFlag)) {
                    inSymbol = true;
                }
            } else {
                if (line.contains(iarGlobalVarFlag)) {
                    String[] data = line.split("\\s+");
                    int size = 0;
                    if (!data[2].equals("Data")) {
                        size = Integer.decode(data[2]);
                    }
                    if (!line.startsWith("_") && previousLine.startsWith("_")) {
                        symbols.add(
                            new Symbol(previousLine.trim(), data[1].replaceAll("'", ""), size));
                    } else {
                        symbols.add(
                            new Symbol(data[0], data[1].replaceAll("'", ""), size));
                    }
                }
                previousLine = line;
                if (line.startsWith(endFlag) && !line.equals(iarSymbolFlag)) {
                    inSymbol = false;
                    break;
                }
            }
        }
        content = null;
        System.out.println(symbols);
    }

    private record Symbol(String name, String address, int size) {

    }
}
