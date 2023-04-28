package com.tlcsdm.core.util.groovy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/24 22:20
 */
@DisabledIfSystemProperty(named = "env", matches = "workflow", disabledReason = "The scope of Groovy is provided")
public class JSR223Test {

    @Test
    public void test() {
        try {
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("groovy");
            String HelloLanguage = "def hello(language) {return \"Hello $language\"}";
            engine.eval(HelloLanguage);
            Invocable inv = (Invocable) engine;
            Object[] params = { new String("Groovy") };
            Object result = inv.invokeFunction("hello", params);
            // assert result.equals("Hello Groovy");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
