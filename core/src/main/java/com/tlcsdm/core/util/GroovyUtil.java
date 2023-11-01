/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

import com.tlcsdm.core.exception.GroovyCompilationErrorsException;
import com.tlcsdm.core.exception.UnsupportedFeatureException;
import com.tlcsdm.core.logging.StaticLog;
import com.tlcsdm.core.wrap.hutool.NetUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.runtime.IOGroovyMethods;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Groovy工具类
 *
 * @author unknowIfGuestInDream
 * @date 2023/4/24 22:05
 */
public class GroovyUtil {
    static GroovyScriptEngine groovyScriptEngine;

    private GroovyUtil() {
    }

    public static GroovyScriptEngine init(URL[] root) {
        if (groovyScriptEngine == null) {
            groovyScriptEngine = new GroovyScriptEngine(root);
        }
        return groovyScriptEngine;
    }

    public static GroovyScriptEngine getEngine() {
        if (groovyScriptEngine == null) {
            throw new UnsupportedFeatureException(
                    "Groovy is not supported, please confirm whether there is a groovy dependency.");
        }
        return groovyScriptEngine;
    }

    /**
     * 用于调用指定Groovy脚本中的指定方法
     *
     * @param scriptName 脚本名称
     * @param methodName 方法名称
     * @param params     方法参数
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public static Object invokeMethod(String scriptName, String methodName, Object... params) {
        Object ret = null;
        Class scriptClass;
        GroovyObject scriptInstance = null;

        try {
            scriptClass = groovyScriptEngine.loadScriptByName(scriptName);
            scriptInstance = (GroovyObject) scriptClass.getDeclaredConstructor().newInstance();
        } catch (ResourceException | ScriptException | InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e1) {
            StaticLog.warn("Load script [" + scriptName + "] failed.", e1);
        } catch (MultipleCompilationErrorsException e) {
            throw new GroovyCompilationErrorsException(
                    scriptName + " compilation exception, the program has terminated.", e);
        }

        try {
            ret = scriptInstance.invokeMethod(methodName, params);
        } catch (IllegalArgumentException e) {
            StaticLog.warn("Execute " + methodName + " params error, params are " + Arrays.toString(params), e);
        } catch (Exception e) {
            StaticLog.error("Execute " + methodName + " error.", e);
        }

        return ret;
    }

    /**
     * 运行groovy脚本
     */
    public static Object run(String scriptName, Map<String, Object> params) {
        Object ret = null;
        Binding binding = new Binding(params);
        try {
            ret = groovyScriptEngine.run(scriptName, binding);
        } catch (ResourceException | ScriptException e) {
            StaticLog.warn("Load script [" + scriptName + "] failed.", e);
        } catch (MultipleCompilationErrorsException e) {
            throw new GroovyCompilationErrorsException(
                    scriptName + " compilation exception, the program has terminated.", e);
        }
        return ret;
    }

    public static Object run(String scriptName) {
        return run(scriptName, Map.of());
    }

    /**
     * 获取运行脚本内容
     */
    public static String getScriptContent(String scriptName) {
        URLConnection conn;
        String content = "";
        try {
            conn = groovyScriptEngine.getResourceConnection(scriptName);
            content = IOGroovyMethods.getText(conn.getInputStream(), CoreConstant.ENCODING_UTF_8);
        } catch (ResourceException | IOException e) {
            StaticLog.error(e);
        }
        return content;
    }

    /**
     * 简易服务器
     * 需要引用模块jdk.httpserver
     */
    public static void simpleHttpServer(int port, String contextRoot, String docBase) {
        if (!NetUtil.isUsableLocalPort(port)) {
            port = NetUtil.getUsableLocalPort();
        }
        Map<String, Object> map = new HashMap<>(4);
        map.put("args", new String[] { String.valueOf(port), contextRoot, docBase });
        GroovyUtil.run("SimpleHttpServer.groovy", map);
    }
}
