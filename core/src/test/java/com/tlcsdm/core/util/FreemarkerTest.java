package com.tlcsdm.core.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.tlcsdm.core.freemarker.template.LowerDirective;
import com.tlcsdm.core.freemarker.template.RepeatDirective;
import com.tlcsdm.core.freemarker.template.UpperDirective;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ResourceUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerTest {
    private static Configuration configuration;

    @BeforeAll
    public static void init() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        try {
            configuration.setDirectoryForTemplateLoading(
                    new File(ResourceUtil.getResource("freemarker/templates").getPath()));
            configuration.setDefaultEncoding("utf-8");
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
        System.out.println(stringWriter.toString());
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
        System.out.println(stringWriter.toString());
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
        System.out.println(stringWriter.toString());
    }

    /**
     * 自定义指令通过内建函数new来生效
     */
    @Test
    public void upper2() throws IOException, TemplateException {
        Template template = configuration.getTemplate("upper2.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(new HashMap(), stringWriter);
        System.out.println(stringWriter.toString());
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
        System.out.println(stringWriter.toString());
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
        System.out.println(stringWriter.toString());
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
        System.out.println(stringWriter.toString());
    }

}
