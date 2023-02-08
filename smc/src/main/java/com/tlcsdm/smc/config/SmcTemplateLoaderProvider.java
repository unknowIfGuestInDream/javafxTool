package com.tlcsdm.smc.config;

import java.io.IOException;

import com.tlcsdm.core.freemarker.TemplateLoaderService;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;

public class SmcTemplateLoaderProvider implements TemplateLoaderService {

    @Override
    public TemplateLoader getTemplateLoader() {
        try {
            return new FileTemplateLoader(FileUtil.file(getClass().getResource("/com/tlcsdm/smc/static/templates")));
        } catch (IORuntimeException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
