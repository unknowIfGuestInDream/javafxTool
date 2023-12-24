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

package com.tlcsdm.core.factory.config;

import com.tlcsdm.core.factory.InitializingFactory;
import com.tlcsdm.core.freemarker.TemplateLoaderService;
import com.tlcsdm.core.javafx.util.ConfigureUtil;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.core.util.FreemarkerUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.UndefinedOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 扫描TemplateLoaderService实现类
 * 如果应用模块不实现TemplateLoaderService接口就不提供freemarker模板功能
 *
 * @author unknowIfGuestInDream
 */
public class TemplateLoaderScanner implements InitializingFactory {

    @Override
    public void initialize() throws Exception {
        List<TemplateLoader> list = new ArrayList<>();
        ServiceLoader<TemplateLoaderService> templateLoaders = ServiceLoader.load(TemplateLoaderService.class);
        for (TemplateLoaderService templateLoader : templateLoaders) {
            list.add(templateLoader.getTemplateLoader());
        }
        // core 下模板作为默认模板，这代表着core中的默认模板可以被应用模块重写
        list.add(new ClassTemplateLoader(TemplateLoaderScanner.class, "/com/tlcsdm/core/static/templates"));
        // 系统模板路径
        File file = new File(ConfigureUtil.getConfigureTemplatePath());
        if (!file.exists()) {
            file.mkdirs();
        }
        list.add(0, new FileTemplateLoader(file));
        TemplateLoader[] loaders = list.toArray(new TemplateLoader[0]);
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        Configuration configuration = FreemarkerUtil.init();
        configuration.setTemplateLoader(mtl);
        configuration.setDefaultEncoding(CoreConstant.ENCODING_UTF_8);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20, soft:250");
        configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        configuration.setDateFormat("yyyy-MM-dd");
        configuration.setNumberFormat("number");
        configuration.setOutputFormat(UndefinedOutputFormat.INSTANCE);
    }

}
