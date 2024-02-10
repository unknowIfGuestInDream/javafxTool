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

package com.tlcsdm.core.util;

import cn.hutool.log.StaticLog;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Interface Scanner
 */
public class InterfaceScanner {

    /**
     * Gets the list of sample classes to load
     *
     * @return The classes
     */
    public static List<Class<?>> discover(Class<?> clazz) {
        Class<?>[] results = new Class[]{};
        List<Class<?>> list = new ArrayList<>();
        try {
            results = loadFromPathScanning(clazz);
        } catch (Exception e) {
            StaticLog.error(e);
        }

        for (Class<?> sampleClass : results) {
            if (sampleClass.isInterface()) {
                continue;
            }
            if (Modifier.isAbstract(sampleClass.getModifiers())) {
                continue;
            }
            list.add(sampleClass);
        }
        return list;
    }

    public static void invoke(Class<?> clazz, String name, Class<?>... parameterTypes) {
        List<Class<?>> list = discover(clazz);
        CoreUtil.sortByOrder(list);
        for (Class<?> i : list) {
            try {
                i.getDeclaredMethod(name, parameterTypes).invoke(i.getDeclaredConstructor().newInstance());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException e) {
                StaticLog.error(e);
            } catch (NoClassDefFoundError e) {
                // fix freemarker 依赖找不到却进行初始化的问题
                StaticLog.debug("NoClassDefFoundError: {}", e.getMessage());
            }
        }
    }

    public static Class<?>[] loadFromPathScanning(Class<?> cls) {
        // scan the module-path
        Set<Class<?>> classes;
        try (ScanResult scanResult = new ClassGraph().enableClassInfo().rejectClasses("module-info", "package-info")
            .rejectPackages(rejectPackages()).scan()) {
            ClassInfoList controlClasses = scanResult.getClassesImplementing(cls);
            List<Class<?>> controlClassRefs = controlClasses.loadClasses(true);
            classes = new HashSet<>(controlClassRefs);
        }
        return classes.toArray(new Class[0]);
    }

    /**
     * Scans all classes.
     *
     * @return The classes
     */
    private Class<?>[] loadFromPathScanning() {
        final Set<Class<?>> classes = new LinkedHashSet<>();
        // scan the module-path
        ModuleLayer.boot().configuration().modules().stream().map(ResolvedModule::reference).filter(
            rm -> !InterfaceScanner.isSystemModule(rm.descriptor().name())).forEach(mref -> {
            try (ModuleReader reader = mref.open()) {
                reader.list().forEach(c -> {
                    final Class<?> clazz = processClassName(c);
                    if (clazz != null) {
                        classes.add(clazz);
                    }
                });
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        });
        return classes.toArray(new Class[0]);
    }

    private static Class<?> processClassName(final String name) {
        String className = name.replace("\\", ".");
        className = className.replace("/", ".");

        // some cleanup code
        if (className.contains("$")) {
            // we don't care about samples as inner classes, so
            // we jump out
            return null;
        }
        if (className.contains(".bin")) {
            className = className.substring(className.indexOf(".bin") + 4);
            className = className.replace(".bin", "");
        }
        if (className.startsWith(".")) {
            className = className.substring(1);
        }
        if (className.endsWith(".class")) {
            className = className.substring(0, className.length() - 6);
        }

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (Throwable e) {
            StaticLog.error(e);
        }
        return clazz;
    }

    /**
     * Return true if the given module name is a system module. There can be system
     * modules in layers above the boot layer.
     */
    public static boolean isSystemModule(final String moduleName) {
        return moduleName.startsWith("java.") || moduleName.startsWith("javax.") || moduleName.startsWith(
            "javafx.") || moduleName.startsWith("jdk.") || moduleName.startsWith("oracle.") || moduleName.startsWith(
            "cn.hutool.") || moduleName.startsWith("ch.qos.logback.") || moduleName.startsWith(
            "com.sun.jna.") || moduleName.startsWith(
            "org.fxmisc.") || moduleName.startsWith("com.fasterxml.jackson.") || moduleName.startsWith(
            "org.apache.") || "commons.beanutils".equals(moduleName) || "io.github.javadiffutils".equals(
            moduleName) || "org.slf4j".equals(moduleName) || "commons.math3".equals(
            moduleName) || "org.controlsfx.controls".equals(moduleName) || "SparseBitSet".equals(
            moduleName) || "freemarker".equals(moduleName) || "reactfx".equals(moduleName) || "yuicompressor".equals(
            moduleName) || "tabula".equals(moduleName) || "vosk".equals(moduleName) || "jave.core".equals(
            moduleName) || "jython.slim".equals(moduleName) || "com.zaxxer.hikari".equals(moduleName) || "druid".equals(
            moduleName) || "jssc".equals(moduleName) || "com.github.oshi".equals(moduleName) || "zip4j".equals(
            moduleName);
    }

    /**
     * 不希望扫描的包, 加快启动时间.
     */
    public static String[] rejectPackages() {
        return new String[]{"java", "javax", "javafx", "jdk", "oracle", "cn.hutool", "ch.qos.logback", "org.apache", "org.slf4j", "org.controlsfx", "impl.org.controlsfx", "freemarker", "io.github.difflib", "com.fasterxml.jackson", "org.dom4j", "com.github.benmanes.caffeine", "io.github.classgraph", "org.fxmisc.richtext", "org.fxmisc.flowless", "org.fxmisc.undo", "org.reactfx", "net.coobird.thumbnailator", "com.dlsc.pdfviewfx", "technology.tabula", "com.dlsc.preferencesfx", "com.yahoo.platform.yui", "com.sun.jna", "com.ziclix.python", "org.python", "com.zaxxer.hikari", "com.alibaba.druid", "jssc", "oshi", "net.lingala.zip4j", "org.checkerframework", "org.kordamp.ikonli", "com.graphbuilder", "org.jaxen", "org.fxmisc.wellbehaved", "org.openxmlformats", "com.microsoft", "org.etsi", "org.w3"};
    }

}
