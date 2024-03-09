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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Test for ProcessBuilder.
 */
class ProcessBuilderTest {

    @Test
    void test() throws IOException, InterruptedException {
        ProcessBuilder builder;
        String charSet;
        String exportCmd = "java -version";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("win")) {
            builder = new ProcessBuilder("cmd", "/c", exportCmd);
            charSet = "gbk";
        } else {
            builder = new ProcessBuilder("sh", "-c", exportCmd);
            charSet = "utf-8";
        }
        //builder.directory();
        builder.redirectErrorStream(true);

        Process process = builder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), charSet));
        StringWriter sw = new StringWriter();
        br.transferTo(sw);
        System.out.println(sw);
        int exitCode = process.waitFor();
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testError() throws IOException, InterruptedException {
        ProcessBuilder builder;
        String charSet;
        String exportCmd = "javas -version";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("win")) {
            builder = new ProcessBuilder("cmd", "/c", exportCmd);
            charSet = "gbk";
        } else {
            builder = new ProcessBuilder("sh", "-c", exportCmd);
            charSet = "utf-8";
        }
        //builder.directory();
        builder.redirectErrorStream(true);

        Process process = builder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), charSet));
        StringWriter sw = new StringWriter();
        br.transferTo(sw);
        System.out.println(sw);
        int exitCode = process.waitFor();
        if (os.toLowerCase().contains("win")) {
            Assertions.assertEquals(1, exitCode);
        } else {
            Assertions.assertEquals(127, exitCode);
        }
    }

    @Test
    void givenProcessBuilder_whenStartingPipeline_thenSuccess()
        throws IOException, InterruptedException {
        ProcessBuilder b1 = new ProcessBuilder("java", "-version");
        ProcessBuilder b2 = new ProcessBuilder("java", "-help");
        List<ProcessBuilder> builders = Arrays.asList(
            b1, b2);
        b2.redirectErrorStream(true);
        String os = System.getProperty("os.name");
        String charSet;
        if (os.toLowerCase().contains("win")) {
            charSet = "gbk";
        } else {
            charSet = "utf-8";
        }
        List<Process> processes = ProcessBuilder.startPipeline(builders);
        Process last = processes.get(processes.size() - 1);
        BufferedReader br = new BufferedReader(new InputStreamReader(last.getInputStream(), charSet));
        StringWriter sw = new StringWriter();
        br.transferTo(sw);
        System.out.println(sw);
        int exitCode = last.waitFor();
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void env() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> environment = processBuilder.environment();
        environment.forEach((key, value) -> System.out.println(key + value));

        environment.put("GREETING", "Hola ENV");

        processBuilder.command("/bin/bash", "-c", "echo $GREETING");
        Process process = processBuilder.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        StringWriter sw = new StringWriter();
        br.transferTo(sw);
        System.out.println(sw);
        int exitCode = process.waitFor();
        Assertions.assertEquals(0, exitCode);
    }
}
