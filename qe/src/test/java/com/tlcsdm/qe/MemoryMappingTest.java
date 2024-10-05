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
    List<String> varContent = new ArrayList<>();
    List<Symbol> symbols = new ArrayList<>();
    private boolean inOptions = false;
    private boolean inSymbol = false;
    private boolean inRomSectionFlag = false;

    @Test
    public void read() throws IOException {
        File file = new File(ResourceUtil.getResource("cs+.map").getPath());
        List<String> content = FileUtils.readLines(file, StandardCharsets.UTF_8);
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
                        symbols.add(new Symbol(varContent.get(varContent.size() - 1).trim(), data[1],
                            Integer.parseInt(data[2])));
                    } else {
                        varContent.add(line);
                    }
                }
                if (line.startsWith(endFlag) && !line.equals(symbolFlag)) {
                    inSymbol = false;
                    break;
                }
            }
        }
        varContent = null;
        sections = null;
        content = null;
        System.out.println(symbols);
    }

    private record Symbol(String name, String address, int size) {

    }
}
