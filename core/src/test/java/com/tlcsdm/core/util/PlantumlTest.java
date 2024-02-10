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

package com.tlcsdm.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.SourceStringReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class PlantumlTest {

    @BeforeAll
    static void init() {
        System.setProperty("PLANTUML_LIMIT_SIZE", "80000");
    }

    @Test
    void test1() throws IOException {
        File source = new File(ResourceUtil.getResource("plantuml/smc.puml").getFile());
        SourceFileReader reader = new SourceFileReader(source);
        List<GeneratedImage> list = reader.getGeneratedImages();
        // Generated files
        File png = list.get(0).getPngFile();
    }

    @Test
    void test2() throws IOException {
        File source = new File(ResourceUtil.getResource("plantuml/core.puml").getFile());
        FileFormatOption fileFormatOption = new FileFormatOption(FileFormat.PNG);
        SourceFileReader reader = new SourceFileReader(source, source.getAbsoluteFile().getParentFile(),
            fileFormatOption);
        List<GeneratedImage> list = reader.getGeneratedImages();
    }

    @Test
    void svg() throws IOException {
        File source = new File(ResourceUtil.getResource("plantuml/core.puml").getFile());
        SourceStringReader reader = new SourceStringReader(Files.readString(source.toPath()));
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Write the first image to "os"
        String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        // The XML is stored into svg
        final String svg = new String(os.toByteArray(), StandardCharsets.UTF_8);
        File s = new File(ResourceUtil.getResource("plantuml").getFile() + "/core.svg");
        FileUtil.writeUtf8String(svg, s);
        os.close();
    }

    @Test
    void getFlows() throws IOException {
        OutputStream png = new FileOutputStream(ResourceUtil.getResource("plantuml").getFile() + "/flow.png");
        String source = """
            @startmindmap
            skinparam dpi 300
            title 组织架构变更
            *[#Orange] 一级部门
            ** 数字金融部-张三【主管】
            *** Linux Mint
            *** Kubuntu
            *** Lubuntu
            *** KDE Neon
            ** LMDE
            ** SolydXK
            ** SteamOS
            ** Raspbian with a very long name
            *** <s>Raspmbc</s> => OSMC
            *** <s>Raspyfi</s> => Volumio
            @endmindmap""";

        SourceStringReader reader = new SourceStringReader(source);
        String desc = reader.outputImage(png).getDescription();
    }
}
