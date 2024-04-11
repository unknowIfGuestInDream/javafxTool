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

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.exception.UnsupportedFeatureException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
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
            configuration.setSharedVariable("memento", new MementoDirective());
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
            StaticLog.error(e);
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
            StaticLog.error(e);
        }
        return stringWriter.toString();
    }

    public static String getTemplateContent(String name) {
        return getTemplateContent(new HashMap<>(0), name);
    }

    public static String getTemplateContent(Configuration conf, String name) {
        return getTemplateContent(conf, new HashMap<>(0), name);
    }

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param configuration
     * @param model
     */
    public static void generateFileByFile(String templateFilePath, String destFilePath, Configuration configuration,
        Map<String, Object> model) throws IOException, TemplateException {
        generateFileByFile(templateFilePath, destFilePath, configuration, model, true, false);
    }

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param configuration
     * @param model
     * @param override
     */
    public static void generateFileByFile(String templateFilePath, String destFilePath, Configuration configuration,
        Map<String, Object> model, boolean override) throws IOException, TemplateException {
        generateFileByFile(templateFilePath, destFilePath, configuration, model, override, false);
    }

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param configuration
     * @param model
     * @param override
     * @param append
     */
    public static void generateFileByFile(String templateFilePath, String destFilePath, Configuration configuration,
        Map<String, Object> model, boolean override, boolean append)
        throws IOException, TemplateException {
        Template t = configuration.getTemplate(templateFilePath);
        Path destPath = Paths.get(destFilePath);
        if (override || append || !Files.exists(destPath)) {
            Path parent = destPath.getParent();
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (OutputStream outputStream = append
                ? Files.newOutputStream(Paths.get(destFilePath), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
                : Files.newOutputStream(Paths.get(destFilePath))) {
                Writer out = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                t.process(model, out);
                StaticLog.info(new StringBuilder(destFilePath).append(" saved!").toString());
            }
        } else {
            StaticLog.error(new StringBuilder(destFilePath).append(" already exists!").toString());
        }
    }

    /**
     * @param template
     * @param configuration
     * @return render result
     */
    public static String generateStringByFile(String template, Configuration configuration)
        throws IOException, TemplateException {
        Map<String, Object> model = Collections.emptyMap();
        return generateStringByFile(template, configuration, model);
    }

    /**
     * @param template
     * @param configuration
     * @param model
     * @return render result
     */
    public static String generateStringByFile(String template, Configuration configuration, Map<String, Object> model)
        throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        generateStringByFile(writer, template, configuration, model);
        return writer.toString();
    }

    /**
     * @param writer
     * @param template
     * @param configuration
     * @param model
     */
    public static void generateStringByFile(Writer writer, String template, Configuration configuration,
        Map<String, Object> model)
        throws IOException,
        TemplateException {
        Template tpl = configuration.getTemplate(template);
        tpl.process(model, writer);
    }

    /**
     * @param templateContent
     * @param configuration
     * @param model
     * @return render result
     */
    public static String generateStringByString(String templateContent, Configuration configuration, Map<String, Object> model)
        throws IOException, TemplateException {
        Template tpl = new Template(null, templateContent, configuration);
        StringWriter writer = new StringWriter();
        tpl.process(model, writer);
        return writer.toString();
    }
}
