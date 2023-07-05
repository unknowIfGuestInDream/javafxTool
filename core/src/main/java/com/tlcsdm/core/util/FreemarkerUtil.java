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

package com.tlcsdm.core.util;

import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.exception.UnsupportedFeatureException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * freemarker 引擎工具
 *
 * @author unknowIfGuestInDream
 */
public class FreemarkerUtil {
    private static Configuration configuration;
    /**
     * 最开始设计是用于防止用户手动调用init方法
     * 但是重启功能可能会重复调用, 目前此参数保留，也可以去除。
     */
    private static final AtomicBoolean IS_INIT = new AtomicBoolean(false);

    private FreemarkerUtil() {
    }

    /**
     * 初始化, 仅供TemplateLoaderScanner调用
     */
    public static Configuration init() {
        if (configuration == null) {
            if (!IS_INIT.compareAndSet(false, true)) {
                throw new UnExpectedResultException("Please do not manually call the init method.");
            }
            configuration = new Configuration(Configuration.VERSION_2_3_32);
        }
        return configuration;
    }

    /**
     * 用于获取configuration 对象
     */
    public static Configuration configuration() {
        if (configuration == null) {
            throw new UnsupportedFeatureException(
                    "Freemarker is not supported, please confirm whether there is a freemarker dependency.");
        }
        return configuration;
    }

    public static Template getTemplate(String name) {
        return getTemplate(configuration, name);
    }

    public static Template getTemplate(Configuration conf, String name) {
        Template template = null;
        try {
            template = conf.getTemplate(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }

    public static String getTemplateContent(Map<String, Object> objectMap, String name) {
        return getTemplateContent(configuration, objectMap, name);
    }

    public static String getTemplateContent(Configuration conf, Map<String, Object> objectMap, String name) {
        StringWriter stringWriter = new StringWriter();
        Template template;
        try {
            template = conf.getTemplate(name);
            template.process(objectMap, stringWriter);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public static String getTemplateContent(String name) {
        return getTemplateContent(new HashMap<>(0), name);
    }

    public static String getTemplateContent(Configuration conf, String name) {
        return getTemplateContent(conf, new HashMap<>(0), name);
    }
}
