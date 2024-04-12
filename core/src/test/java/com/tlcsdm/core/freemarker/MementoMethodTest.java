package com.tlcsdm.core.freemarker;

import com.tlcsdm.core.Memento;
import com.tlcsdm.core.freemarker.method.MementoMethod;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MementoMethodTest {

    private Configuration cfg;
    private Memento rootMemento;

    @BeforeEach
    public void setUp() throws Exception {
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(this.getClass(), "/freemarker/templates");

        rootMemento = new Memento("root");
        Memento child1 = new Memento("child", "1");
        Memento child2 = new Memento("child", "2");
        rootMemento.putChild(child1);
        rootMemento.putChild(child2);
    }

    @Test
    public void testGetChild() throws TemplateException {
        Map<String, Object> root = new HashMap<>();
        root.put("mementoMethod", new MementoMethod(rootMemento));

        Template template = cfg.getTemplate("testGetChild.ftl");

        StringWriter out = new StringWriter();
        template.process(root, out);

        assertEquals("Child ID: 1", out.toString().trim());
    }

    @Test
    public void testGetChildren() throws TemplateException {
        Map<String, Object> root = new HashMap<>();
        root.put("mementoMethod", new MementoMethod(rootMemento));

        Template template = cfg.getTemplate("testGetChildren.ftl");

        StringWriter out = new StringWriter();
        template.process(root, out);

        assertEquals("Children IDs: 1, 2", out.toString().trim());
    }
}
