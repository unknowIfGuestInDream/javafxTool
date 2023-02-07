package com.tlcsdm.core.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * freemarker 引擎工具
 * @author unknowIfGuestInDream
 */
public class FreemarkerUtil {
    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

    public static Configuration configuration() {
        return configuration;
    }

    public static Template getTemplate(String name) {
        Template template = null;
        try {
            template = configuration.getTemplate(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }

    public static String getTemplateContent(Map<String, Object> objectMap, String name) {
        StringWriter stringWriter = new StringWriter();
        Template template;
        try {
            template = configuration.getTemplate(name);
            template.process(objectMap, stringWriter);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
}
