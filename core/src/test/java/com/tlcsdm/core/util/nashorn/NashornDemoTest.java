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

import java.io.IOException;
import java.io.InputStreamReader;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 */
public class NashornDemoTest {

    @Test
    public void bind() throws ScriptException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("Nashorn");

        engine.put("name", "Rahman");
        engine.put("surname", "Usta");

        String fullName = engine.get("name") + " " + engine.get("surname");

        engine.put("fullName", fullName);

        engine.eval("var person={};");
        engine.eval("person.name=name;");
        engine.eval("person.surname=surname;");

        engine.eval("print(JSON.stringify(person));");

        engine.put("age", 26);
        engine.eval("person.age=age; person.fullName=fullName");

        engine.eval("print(JSON.stringify(person));");
    }

    @Test
    public void compilable() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");

        Compilable compilable = (Compilable) engine;

        CompiledScript compiled =
            compilable.compile("var calculate=function(one,two){ return (one*two); }");

        Object mul = null;
        try {
            mul = engine.eval("calculate(20,30)");
        } catch (ScriptException se) {
            System.out.println(se.getMessage());
            compiled.eval();

            mul = engine.eval("calculate(5,10)");
        }

        System.out.println(mul);
    }

    @Test
    public void engines() throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();

        for (ScriptEngineFactory factory : mgr.getEngineFactories()) {
            System.out.println("ScriptEngineFactory Info");
            System.out.printf("\tScript Engine: %s (%s)\n", factory.getEngineName(), factory.getEngineVersion());
            System.out.printf("\tLanguage: %s (%s)\n", factory.getLanguageName(), factory.getLanguageVersion());
            for (String name : factory.getNames()) {
                System.out.printf("\tEngine Alias: %s\n", name);
            }
        }
    }

    @Test
    public void eval() throws ScriptException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("Nashorn");

        engine.eval("var person= new Object();");
        engine.eval("person.name='Rahman';");
        engine.eval("person.surname='Usta';");

        engine.eval("print(JSON.stringify(person));");

        engine.eval("person.age=26;");

        engine.eval("print(JSON.stringify(person));");
    }

    @Test
    public void func() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");

        engine.eval("var person={};");
        engine.eval("person.name='Rahman';");
        engine.eval("person.surname='Usta';");
        engine.eval("person.fullName=function(){return this.name+' '+this.surname};");

        engine.eval("print('FullName: '+person.fullName());");
    }

    @Test
    public void geo() throws ScriptException, IOException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("js");
        engine.eval(new InputStreamReader(ResourceUtil.getResource("nashorn/geo.js").openStream()));
    }

    @Test
    public void invokable() throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");

        engine.eval("var person={};");
        engine.eval("person.name='Rahman';");
        engine.eval("person.surname='Usta';");

        engine.eval("person.calculate=function(age){return this.name+':'+this.surname+':'+age};");
        engine.eval("calculate=function(one,two){ return (one*two); }");

        Invocable inv = (Invocable) engine;

        Object person = engine.get("person");

        Object calculate = inv.invokeMethod(person, "calculate", 26);

        System.out.println(calculate);

        System.out.println(inv.invokeFunction("calculate", 5, 4));
    }

    @Test
    public void javatype() throws ScriptException, IOException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        engine.eval(new InputStreamReader(ResourceUtil.getResource("nashorn/javatype.js").openStream()));
    }

    @Test
    public void jruby() throws ScriptException, IOException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("ruby");
        engine.eval(new InputStreamReader(ResourceUtil.getResource("nashorn/jruby.rb").openStream()));
    }

}
