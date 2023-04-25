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
import com.tlcsdm.core.freemarker.GroovyLoaderService;
import com.tlcsdm.core.util.GroovyUtil;
import groovy.util.GroovyScriptEngine;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 扫描TemplateLoaderService实现类
 * 如果应用模块不实现TemplateLoaderService接口就不提供freemarker模板功能
 *
 * @author unknowIfGuestInDream
 */
public class GroovyLoaderScanner implements InitializingFactory {

    @Override
    public void initialize() throws Exception {
        List<String> list = new ArrayList<>();
        ServiceLoader<GroovyLoaderService> templateLoaders = ServiceLoader.load(GroovyLoaderService.class);
        for (GroovyLoaderService groovyLoaderService : templateLoaders) {
            list.add(groovyLoaderService.getGroovyLoaderPath());
        }
        if (list.size() == 0) {
            return;
        }
        //core 下模板作为默认模板，这代表着core中的默认模板可以被应用模块重写
        list.add(GroovyLoaderScanner.class.getResource("/com/tlcsdm/core/groovy").getPath());
        //系统groovy路径
//        File file = new File(ConfigureUtil.getConfigureTemplatePath());
//        if (!file.exists()) {
//            file.mkdirs();
//        }
        //list.add(0, new FileTemplateLoader(file));
        GroovyScriptEngine scriptEngine = GroovyUtil.init(list.toArray(new String[0]));
        CompilerConfiguration config = new CompilerConfiguration();
        config.setSourceEncoding("UTF-8");
        scriptEngine.setConfig(config);

    }

}
