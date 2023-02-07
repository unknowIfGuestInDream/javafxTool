package com.tlcsdm.core.factory.config;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import com.tlcsdm.core.factory.InitializingFactory;
import com.tlcsdm.core.freemarker.TemplateLoaderService;
import com.tlcsdm.core.util.FreemarkerUtil;

import cn.hutool.core.io.FileUtil;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * 扫描TemplateLoaderService实现类
 */
public class TemplateLoaderScanner implements InitializingFactory {

    @Override
    public void initialize() throws Exception {
        List<TemplateLoader> list = new ArrayList<>();
        ServiceLoader<TemplateLoaderService> templateLoaders = ServiceLoader.load(TemplateLoaderService.class);
        for (TemplateLoaderService templateLoader : templateLoaders) {
            list.add(templateLoader.getTemplateLoader());
        }
        if (list.size() == 0) {
            return;
        }
        list.add(0, new FileTemplateLoader(FileUtil.file(getClass().getResource("/com/tlcsdm/core/static/templates"))));
        TemplateLoader[] loaders = list.toArray(new TemplateLoader[list.size()]);
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        Configuration configuration = FreemarkerUtil.configuration();
        configuration.setTemplateLoader(mtl);
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20, soft:250");
    }

}
