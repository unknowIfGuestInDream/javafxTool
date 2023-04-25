package com.tlcsdm.core.util;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.UnsupportedFeatureException;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 后期考虑重构
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/4/24 22:05
 */
public class GroovyUtil {
    static GroovyScriptEngine groovyScriptEngine;

    public static GroovyScriptEngine init(String[] root) {
        if (groovyScriptEngine == null) {
            try {
                groovyScriptEngine = new GroovyScriptEngine(root);
            } catch (IOException e) {
                StaticLog.error(e);
            }
        }
        return groovyScriptEngine;
    }

    public static GroovyScriptEngine engine() {
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
    @SuppressWarnings({"rawtypes"})
    public static Object invokeMethod(String scriptName, String methodName, Object... params) {
        Object ret = null;
        Class scriptClass;
        GroovyObject scriptInstance = null;

        try {
            scriptClass = groovyScriptEngine.loadScriptByName(scriptName);
            scriptInstance = (GroovyObject) scriptClass.getDeclaredConstructor().newInstance();
        } catch (ResourceException | ScriptException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1) {
            StaticLog.warn("Load script [" + scriptName + "] failed", e1);
        }

        try {
            ret = scriptInstance.invokeMethod(methodName, params);
        } catch (IllegalArgumentException e) {
            StaticLog.warn("Execute " + methodName + " params error, params are " + params, e);
        } catch (Exception e) {
            StaticLog.warn("Execute " + methodName + " error", e);
        }

        return ret;
    }
}
