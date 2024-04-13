package com.tlcsdm.core.freemarker.directive;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class MementoDirectiveTest {
    private Configuration cfg;

    @BeforeEach
    public void setUp() throws Exception {
        cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setSharedVariable("memento", new MementoDirective());
    }

    @Test
    public void testValidMemento() throws IOException, TemplateException {
        Map<String, Object> root = new HashMap<>();
        Memento memento = new Memento("TestType", "TestId", new HashMap<>());
        root.put("memento", memento);

        Template temp = new Template("test", "<@memento memento=memento/>", cfg);
        StringWriter out = new StringWriter();
        temp.process(root, out);

        Assertions.assertTrue(out.toString().contains("TestType"));
        Assertions.assertTrue(out.toString().contains("TestId"));
    }

    @Test
    public void testInvalidMemento() {
        Map<String, Object> root = new HashMap<>();
        root.put("memento", new Object());

        TemplateException exception = Assertions.assertThrows(TemplateException.class, () -> {
            Template temp = new Template("test", "<@memento memento=memento/>", cfg);
            StringWriter out = new StringWriter();
            temp.process(root, out);
        });

        Assertions.assertTrue(exception.getMessage().contains("instance of Memento"));
    }

    @Test
    public void testRecursiveMemento() throws IOException, TemplateException {
        Map<String, Object> root = new HashMap<>();
        Memento childMemento = new Memento("ChildType", "ChildId", new HashMap<>());
        Memento parentMemento = new Memento("ParentType", "ParentId", new HashMap<>());
        parentMemento.addChildMemento(childMemento);
        root.put("memento", parentMemento);

        Template temp = new Template("test", "<@memento memento=memento/>", cfg);
        StringWriter out = new StringWriter();
        temp.process(root, out);

        Assertions.assertTrue(out.toString().contains("ParentType"));
        Assertions.assertTrue(out.toString().contains("ChildType"));
    }
}
