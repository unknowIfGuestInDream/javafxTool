/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.factory.config.ThreadPoolTaskExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Process封装.
 *
 * @author unknowIfGuestInDream
 */
public class ProcessUtil {

    private ProcessUtil() {
    }

    /**
     * 启动jar.
     */
    public static void launchJar(String jarPath) {
        final List<String> launchArgs = new ArrayList<>();
        launchArgs.add(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
        launchArgs.add("-jar");
        launchArgs.add(jarPath);
        ProcessBuilder builder = new ProcessBuilder().command(launchArgs).redirectErrorStream(true);
        ExecutorService executorService = ThreadPoolTaskExecutor.hasInitialized() ? ThreadPoolTaskExecutor.get() :
            Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                return builder.start();
            } catch (IOException e) {
                StaticLog.error(e);
            }
            return null;
        });
    }

    /**
     * 获取process输出.
     */
    public String getInput(Process process) {
        SequenceInputStream sis = new SequenceInputStream(process.getInputStream(), process.getErrorStream());
        InputStreamReader inst;
        try {
            inst = new InputStreamReader(sis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(inst);
            String res;
            StringBuilder sb = new StringBuilder();
            while ((res = br.readLine()) != null) {
                sb.append(res).append("\n");
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            StaticLog.error(e);
        }
        return "";
    }
}
