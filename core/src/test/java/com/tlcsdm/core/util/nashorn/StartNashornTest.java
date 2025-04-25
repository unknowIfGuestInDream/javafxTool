/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.util.nashorn;

import cn.hutool.core.io.resource.ResourceUtil;
import com.tlcsdm.core.util.User;
import org.openjdk.nashorn.api.scripting.ClassFilter;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 */
public class StartNashornTest {

    /**
     * 在Java中调用Nashorn引擎
     */
    @Test
    public void start() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("print('Hello World!');");
    }

    @Test
    public void startjs() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/hello.js").getFile()));
    }

    /**
     * 编译JavaScript代码
     * 可以将脚本编译为Java字节码后调用，这样在多次调用的情况下效率会更高，例如：
     */
    @Test
    public void compiler() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        CompiledScript compiledScript = ((Compilable) engine).compile("print('Hello World!');");
        compiledScript.eval();
    }

    /**
     * 传递数据到脚本
     * 数据可以通过定义Bindings传递到引擎中：
     */
    @Test
    public void bindings() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        Bindings bindings = engine.createBindings();
        bindings.put("name", "Nashorn");
        engine.eval("print('Hello ' + name);", bindings);
    }

    /**
     * 在Java中调用JavaScript函数
     * 从Java代码中直接调用定义在脚本中的JavaScript函数。你可以将Java对象作为函数参数传递，并且使用函数返回值调用Java方法
     * 为了调用函数，你首先需要将脚本引擎转换为Invocable接口。NashornScriptEngine已经实现了Invocable接口，
     * 并且定义了invokeFunction方法来调用指定名称的JavaScript函数。
     */
    @Test
    public void func() throws ScriptException, FileNotFoundException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/hello.js").getFile()));

        Invocable invocable = (Invocable) engine;

        Object result = invocable.invokeFunction("fun1", "Nashorn");
        System.out.println(result);
        System.out.println(result.getClass());

        invocable.invokeFunction("fun2", new Date());
        // [object java.util.Date]

        invocable.invokeFunction("fun2", LocalDateTime.now());
        // [object java.time.LocalDateTime]

        invocable.invokeFunction("fun2", new User());
        // [object my.package.Person]
    }

    /**
     * 调用Java静态方法和字段
     */
    @Test
    public void java() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/java.js").getFile()));
    }

    /**
     * 类型数组,数组转换
     */
    @Test
    public void array() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/array.js").getFile()));
    }

    /**
     * JavaScript使用foreach语句迭代数组或集合：
     */
    @Test
    public void foreach() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/foreach").getFile()));
    }

    /**
     * Lambda表达式和数据流
     */
    @Test
    public void lamada() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/lamada.js").getFile()));
    }

    /**
     * 类的继承
     * Java类型可以由Java.extend轻易继承。如下所示，你甚至可以在脚本中创建多线程的代码
     */
    @Test
    public void extend() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/extend.js").getFile()));
    }

    /**
     * 函数重载
     * 方法和函数可以通过点运算符或方括号运算符来调用：
     */
    @Test
    public void override() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/override.js").getFile()));
    }

    /**
     * Java Beans
     * 你可以简单地使用属性名称来向Java Beans获取或设置值，不需要显式调用读写器：
     */
    @Test
    public void beans() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/beans.js").getFile()));
    }

    /**
     * 属性绑定
     * 两个不同对象的属性可以绑定到一起：
     */
    @Test
    public void prop() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/prop.js").getFile()));
    }

    /**
     * 字符串扩展
     * Nashorn在String原型上提供了两个简单但非常有用的扩展。这就是trimRight和trimLeft函数，它们可返回String得副本并删除空格：
     */
    @Test
    public void strExtend() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/strExtend.js").getFile()));
    }

    /**
     * 位置
     * 当前文件名，目录和行可以通过全局变量__FILE__、__LINE__和__DIR__获取：
     */
    @Test
    public void location() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/location.js").getFile()));
    }

    /**
     * 导入作用域
     * 有时一次导入多个Java包会很方便。我们可以使用JavaImporter类，和with语句一起使用。所有被导入包的类文件都可以在with语句的局部域中访问到。
     */
    @Test
    public void imports() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/import.js").getFile()));
    }

    /**
     * Java.asJSONCompatible 函数
     * 使用该函数，我们可以得到一个与Java JSON库期望兼容的对象。代码如下：
     */
    @Test
    public void json() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        Object obj = engine.eval("Java.asJSONCompatible({ number: 42, greet: 'hello', primes: [2,3,5,7,11,13] })");
        Map<String, Object> map = (Map<String, Object>) obj;

        System.out.println(map.get("greet"));
        System.out.println(map.get("primes"));
        System.out.println(List.class.isAssignableFrom(map.get("primes").getClass()));
    }

    /**
     * 载入脚本
     * 你可以在脚本引擎中载入其他JavaScript文件：
     */
    @Test
    public void loadScript() throws ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ResourceUtil.getResource("nashorn/load.js").getFile()));
    }

    /**
     * 限制脚本对特定Java类的访问
     */
    @Test
    public void limit() throws ScriptException, FileNotFoundException {
        final String script =
            "print(java.lang.System.getProperty(\"java.home\"));" +
                "print(\"Create file variable\");" +
                "var File = Java.type(\"java.io.File\");";
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine(new MyCF());
        Assertions.assertThrows(RuntimeException.class, () -> engine.eval(script));
    }

    class MyCF implements ClassFilter { // 创建类过滤器
        @Override
        public boolean exposeToScripts(String s) {
            if (s.equals("java.io.File")) {
                return false;
            }
            return true;
        }
    }

}
