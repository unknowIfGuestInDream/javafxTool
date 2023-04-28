package com.tlcsdm.core.util.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/24 21:28
 */
@DisabledIfSystemProperty(named = "env", matches = "workflow", disabledReason = "The scope of Groovy is provided")
public class GroovyShellTest {

    @Test
    public void test() {
        Binding binding = new Binding();
        binding.setVariable("x", 10);
        binding.setVariable("language", "Groovy");

        GroovyShell shell = new GroovyShell(binding);
        Object value = shell.evaluate("println \"Welcome to $language\"; y = x * 2; z = x * 3; return x ");

        System.out.println(value + ", " + value.equals(10));
        System.out.println(binding.getVariable("y") + ", " + binding.getVariable("y").equals(20));
        System.out.println(binding.getVariable("z") + ", " + binding.getVariable("z").equals(30));
    }
}
