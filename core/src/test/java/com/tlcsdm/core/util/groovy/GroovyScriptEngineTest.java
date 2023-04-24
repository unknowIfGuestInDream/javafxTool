package com.tlcsdm.core.util.groovy;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.junit.jupiter.api.Test;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/24 21:27
 */
public class GroovyScriptEngineTest {

    @Test
    public void test() {
        try {
            String[] roots = new String[]{getClass().getResource("/groovy").getPath()};
            GroovyScriptEngine engine = new GroovyScriptEngine(roots);
            Binding binding = new Binding();
            binding.setVariable("language", "Groovy");
            Object value = engine.run("SimpleScript.groovy", binding);
            assert value.equals("The End");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
