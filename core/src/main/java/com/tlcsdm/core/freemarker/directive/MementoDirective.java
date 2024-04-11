package com.tlcsdm.core.freemarker.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.io.IOException;
import java.util.Map;

public class MementoDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (params.isEmpty()) {
            throw new TemplateModelException("MementoDirective requires parameters.");
        }

        Object mementoObj = params.get("memento");
        if (mementoObj == null || !(mementoObj instanceof TemplateModel)) {
            throw new TemplateModelException("The 'memento' parameter must be provided and be of type TemplateModel.");
        }

        Memento memento = convertToMemento((TemplateModel) mementoObj);
        exposeMementoProperties(env, memento);

        if (body != null) {
            body.render(env.getOut());
        }
    }

    private Memento convertToMemento(TemplateModel model) throws TemplateModelException {
        if (!(model instanceof BeanModel)) {
            throw new TemplateModelException("The model must be of type BeanModel.");
        }
        Object wrappedObject = ((BeanModel) model).getWrappedObject();
        if (!(wrappedObject instanceof Memento)) {
            throw new TemplateModelException("The wrapped object is not of type Memento.");
        }
        return (Memento) wrappedObject;
    }

    private void exposeMementoProperties(Environment env, Memento memento) throws TemplateModelException, IOException {
        env.setVariable("mementoType", env.getObjectWrapper().wrap(memento.getType()));
        env.setVariable("mementoId", env.getObjectWrapper().wrap(memento.getId()));
    }
}
