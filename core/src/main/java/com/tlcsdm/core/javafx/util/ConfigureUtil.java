/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.javafx.util;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/11/27 0:10
 */
public class ConfigureUtil {
    public ConfigureUtil() {
    }

    public static String getConfigurePath() {
        return System.getProperty("user.home") + "/javafxTool";
    }

    public static String getConfigureTemplatePath() {
        return getConfigurePath() + "/templates";
    }

    public static String getConfigureTemplatePath(String fileName) {
        return getConfigureTemplatePath() + fileName;
    }

    public static String getConfigurePath(String fileName) {
        return getConfigurePath() + "/" + fileName;
    }

    /**
     * 系统配置文件
     */
    public static File getConfigureFile(String fileName) {
        File file = new File(getConfigurePath(fileName));
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 用户xml数据文件
     */
    public static File getConfigureXmlFile(String fileName) {
        File file = new File(getConfigurePath(fileName));
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (FileUtil.isEmpty(file)) {
            List<String> list = new ArrayList<>();
            list.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            list.add("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
            list.add("<properties>");
            list.add("  <comment>This file was auto-generated. Do not modify it manually</comment>");
            list.add("</properties>");
            FileUtil.appendUtf8Lines(list, file);
        }
        return file;
    }

    /**
     * 模板文件
     */
    public static File getConfigureTemplateFile(String fileName) {
        File file = new File(getConfigureTemplatePath(fileName));
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
