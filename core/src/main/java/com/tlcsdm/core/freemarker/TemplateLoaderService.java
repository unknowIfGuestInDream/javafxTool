package com.tlcsdm.core.freemarker;

import freemarker.cache.TemplateLoader;

/**
 * freemarker TemplateLoader发现
 * @author unknowIfGuestInDream
 */
public interface TemplateLoaderService {

    /**
     * 获取TemplateLoader
     */
    @SuppressWarnings("exports")
    TemplateLoader getTemplateLoader();

}
