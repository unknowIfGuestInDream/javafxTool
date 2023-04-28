package com.tlcsdm.core.util.groovy;

import cn.hutool.core.io.resource.ResourceUtil;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/24 21:31
 */
@DisabledIfSystemProperty(named = "env", matches = "workflow", disabledReason = "The scope of Groovy is provided")
public class GroovyClassLoaderTest {

    @Test
    public void test() {
        try {
            GroovyClassLoader loader = new GroovyClassLoader();
            Class fileCreator = loader
                    .parseClass(new File(ResourceUtil.getResource("groovy/GroovySimpleFileCreator.groovy").getPath()));
            GroovyObject object = (GroovyObject) fileCreator.getDeclaredConstructor().newInstance();
            object.invokeMethod("printName", "Groovy");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        try {
            GroovyClassLoader loader = new GroovyClassLoader();
            Class scriptClass = loader.parseClass(new File(ResourceUtil.getResource("groovy/hello2.groovy").getPath()));
            GroovyObject scriptInstance = (GroovyObject) scriptClass.getDeclaredConstructor().newInstance();
            Object ret = scriptInstance.invokeMethod("helloWithoutParam", null);
            System.out.println("testGroovy2:" + ret);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception e=" + e.toString());
        }
    }

    @Test
    public void test2() {
        try {
            GroovyClassLoader loader = new GroovyClassLoader();
            Person person = new Person("wchi", "nanjing", 30);
            Class scriptClass = loader.parseClass(new File(ResourceUtil.getResource("groovy/hello2.groovy").getPath()));
            GroovyObject scriptInstance = (GroovyObject) scriptClass.getDeclaredConstructor().newInstance();
            Object ret = scriptInstance.invokeMethod("helloWithParam", new Object[] { person, "lxi" });
            System.out.println("testGroovy3:" + ret);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception e=" + e.toString());
        }
    }

    @Test
    public void test3() {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        String helloScript = """
                package com.vivo.groovy.util
                class Hello {
                String say(String name) {
                System.out.println("hello, " + name)
                return name + " result";
                }
                }
                """;
        Class helloClass = groovyClassLoader.parseClass(helloScript);
        GroovyObject object = null;
        try {
            object = (GroovyObject) helloClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object ret = object.invokeMethod("say", "vivo");
        System.out.println(ret.toString());
    }

    /**
     * 使用GroovyClassLoader另一种情景便是：存在一个Java接口和一个实现该Java接口的Groovy类。
     * 此时，可以通过GroovyClassLoader加载Groovy实现类到应用中，这样就可以直接调用该接口的方法。
     */
    @Test
    public void invokeGroovy() {
        GroovyClassLoader groovyCl = new GroovyClassLoader();
        try {
            // 从文件中读取，将实现IFoo接口的groovy类写在一个groovy文件中
            Class groovyClass = groovyCl.parseClass(new File(ResourceUtil.getResource("groovy/Foo.groovy").getPath()));
            // 直接使用Groovy字符串,也可以获得正确结果
            // Class groovyClass = groovyCl.parseClass("class Foo implements IFoo {public
            // Object run(Object foo) {return 2+2>1}}");//这个返回true
            IFoo foo = (IFoo) groovyClass.getDeclaredConstructor().newInstance();
            System.out.println(foo.run(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
