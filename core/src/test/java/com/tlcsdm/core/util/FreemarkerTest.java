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

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ResourceUtil;
import com.tlcsdm.core.freemarker.template.HexDirective;
import com.tlcsdm.core.freemarker.template.LowerDirective;
import com.tlcsdm.core.freemarker.template.RegorDirective;
import com.tlcsdm.core.freemarker.template.RepeatDirective;
import com.tlcsdm.core.freemarker.template.StyleDirective;
import com.tlcsdm.core.freemarker.template.UpperDirective;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
public class FreemarkerTest {
    private static Configuration configuration;

    @BeforeAll
    public static void init() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        try {
            configuration.setDirectoryForTemplateLoading(
                new File(ResourceUtil.getResource("freemarker/templates").getPath()));
            configuration.setDefaultEncoding(CoreConstant.ENCODING_UTF_8);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20,soft:250");
        } catch (IORuntimeException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    /**
     * include, assign, list
     */
    @Test
    public void test1() throws IOException, TemplateException {
        Template template = configuration.getTemplate("test1.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "freemarker");
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * if, switch, noparse, compress
     */
    @Test
    public void test2() throws IOException, TemplateException {
        Template template = configuration.getTemplate("test2.ftl");
        Map<String, Object> map = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令通过设置共享变量来生效
     */
    @Test
    public void upper1() throws IOException, TemplateException {
        configuration.setSharedVariable("upper", new UpperDirective());
        Template template = configuration.getTemplate("upper1.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(new HashMap(), stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令通过内建函数new来生效
     */
    @Test
    public void upper2() throws IOException, TemplateException {
        Template template = configuration.getTemplate("upper2.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(new HashMap(), stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令通过放到数据模型来生效
     */
    @Test
    public void upper3() throws IOException, TemplateException {
        Template template = configuration.getTemplate("upper1.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("upper", new UpperDirective());
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 lower
     */
    @Test
    public void lower() throws IOException, TemplateException {
        Template template = configuration.getTemplate("lower.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("lower", new LowerDirective());
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 repeat
     */
    @Test
    public void repeat() throws IOException, TemplateException {
        Template template = configuration.getTemplate("repeat.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("repeat", new RepeatDirective());
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 hex
     */
    @Test
    public void hex() throws IOException, TemplateException {
        Template template = configuration.getTemplate("hex.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("hextest", new HexDirective());
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 regor
     */
    @Test
    public void regor() throws IOException, TemplateException {
        Template template = configuration.getTemplate("regor.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("regor", new RegorDirective());
        map.put("list", "dma");
        map.put("gogo", "tau");
        map.put("aaa", "dts");
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 style
     */
    @Test
    public void style() throws IOException, TemplateException {
        Template template = configuration.getTemplate("style.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("style", new StyleDirective());
        map.put("list", "dmaasasqsdfqsfa".repeat(10));
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

}
