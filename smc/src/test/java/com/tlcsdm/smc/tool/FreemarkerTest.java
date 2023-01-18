package com.tlcsdm.smc.tool;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import cn.hutool.core.io.resource.ResourceUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerTest {

    @Test
    public void test1() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setDirectoryForTemplateLoading(new File(ResourceUtil.getResource("templates").getPath()));
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20, soft:250");
        Template template = configuration.getTemplate("test1.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "freemarker");
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter.toString());
    }

}
