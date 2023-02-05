package com.tlcsdm.core.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
    public static List<Class<?>> discover(Class clazz) {
        Class<?>[] results = new Class[]{};
        List<Class<?>> list = new ArrayList<>();
        try {
            results = loadFromPathScanning(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Class<?> sampleClass : results) {
            if (!clazz.isAssignableFrom(sampleClass)) {
                continue;
            }
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

    public static void invoke(Class clazz, String name, Class<?>... parameterTypes) {
        List<Class<?>> list = discover(clazz);
        for (Class<?> i : list) {
            try {
                i.getDeclaredMethod(name, parameterTypes).invoke(i.getDeclaredConstructor().newInstance());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private static Class<?>[] loadFromPathScanning(Class cls) {
        final Set<Class<?>> classes = new LinkedHashSet<>();
        // scan the module-path
        ModuleLayer.boot().configuration().modules().stream().map(ResolvedModule::reference)
                .filter(rm -> isTlcsdmModule(rm.descriptor().name())).forEach(mref -> {
                    try (ModuleReader reader = mref.open()) {
                        reader.list().forEach(c -> {
                            final Class<?> clazz = processClassName(c);
                            if (clazz != null && cls.isAssignableFrom(clazz)) {
                                classes.add(clazz);
                            }
                        });
                    } catch (IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                });
        return classes.toArray(new Class[classes.size()]);
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

        }
        return clazz;
    }

    /**
     * 只扫描tlcsdm下的包
     */
    private static boolean isTlcsdmModule(final String moduleName) {
        return moduleName.startsWith("com.tlcsdm.");
    }
}
