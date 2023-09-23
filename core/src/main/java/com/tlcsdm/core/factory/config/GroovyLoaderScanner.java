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
import com.tlcsdm.core.groovy.GroovyLoaderService;
import com.tlcsdm.core.javafx.util.ConfigureUtil;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.core.util.GroovyUtil;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.classgen.BytecodeExpression;
import org.codehaus.groovy.classgen.BytecodeSequence;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;

import java.io.File;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (!CoreUtil.hasClass("groovy.util.GroovyScriptEngine")) {
            return;
        }
        List<URL> list = new ArrayList<>();
        ServiceLoader<GroovyLoaderService> templateLoaders = ServiceLoader.load(GroovyLoaderService.class);
        for (GroovyLoaderService groovyLoaderService : templateLoaders) {
            list.add(groovyLoaderService.getGroovyLoaderPath());
        }
        // core 下模板作为默认模板，这代表着core中的默认模板可以被应用模块重写
        list.add(GroovyLoaderScanner.class.getResource("/com/tlcsdm/core/groovy/"));
        // 系统groovy路径
        File file = new File(ConfigureUtil.getConfigureGroovyPath());
        if (!file.exists()) {
            file.mkdirs();
        }
        list.add(0, file.toURI().toURL());
        GroovyScriptEngine scriptEngine = GroovyUtil.init(list.toArray(new URL[0]));
        CompilerConfiguration config = new CompilerConfiguration();
        SecureASTCustomizer sac = new SecureASTCustomizer();
        /* disable calling the System.exit() method and use of other dangerous imports */
        List<String> varList = Arrays.asList(System.class.getName(), GroovyShell.class.getName(),
            GroovyClassLoader.class.getName(), Runtime.class.getName(), Socket.class.getName());
        sac.setDisallowedImports(varList);
        sac.setDisallowedReceivers(varList);
        sac.setIndirectImportCheckEnabled(true);
        /* disable dangerous Expressions */
        sac.setDisallowedExpressions(List.of(BytecodeExpression.class));
        sac.setDisallowedStatements(Arrays.asList(BytecodeSequence.class, SynchronizedStatement.class));
        config.addCompilationCustomizers(sac);
        config.setSourceEncoding(CoreConstant.ENCODING_UTF_8);
        scriptEngine.setConfig(config);
    }

}
