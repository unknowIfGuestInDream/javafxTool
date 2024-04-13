package com.tlcsdm.core.freemarker.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.IOException;
import java.util.Map;

public class MementoDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Object mementoObj = params.get("memento");
        if (!(mementoObj instanceof Memento)) {
            throw new TemplateException("The 'memento' parameter must be an instance of Memento.", env);
        }
        Memento memento = (Memento) mementoObj;
        processMemento(env, memento);
        if (body != null) {
            body.render(env.getOut());
        }
    }

    private void processMemento(Environment env, Memento memento) throws TemplateException, IOException {
        env.setVariable("mementoType", env.getObjectWrapper().wrap(memento.getType()));
        env.setVariable("mementoId", env.getObjectWrapper().wrap(memento.getId()));
        env.setVariable("mementoAttributes", env.getObjectWrapper().wrap(memento.getAttributes()));
        for (Map.Entry<String, Set<Memento>> entry : memento.typeMap.entrySet()) {
            env.setVariable(entry.getKey(), env.getObjectWrapper().wrap(entry.getValue()));
            for (Memento child : entry.getValue()) {
                processMemento(env, child);
            }
        }
    }
}
