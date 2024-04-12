package com.tlcsdm.core.freemarker.method;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import java.util.List;
import com.tlcsdm.core.Memento;

public class MementoMethod implements TemplateMethodModelEx {

    private Memento rootMemento;

    public MementoMethod(Memento rootMemento) {
        this.rootMemento = rootMemento;
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.isEmpty()) {
            throw new TemplateModelException("No arguments provided.");
        }

        String action = arguments.get(0).toString();
        switch (action) {
            case "getChild":
                return getChild(arguments);
            case "getChildren":
                return getChildren(arguments);
            default:
                throw new TemplateModelException("Unsupported action: " + action);
        }
    }

    private Memento getChild(List arguments) throws TemplateModelException {
        if (arguments.size() != 3) {
            throw new TemplateModelException("Incorrect number of arguments for getChild. Expected 2.");
        }
        String type = arguments.get(1).toString();
        String id = arguments.get(2).toString();
        return rootMemento.getChild(type, id);
    }

    private List<Memento> getChildren(List arguments) throws TemplateModelException {
        if (arguments.size() == 2) {
            String type = arguments.get(1).toString();
            return new ArrayList<>(rootMemento.getChildren(type));
        } else if (arguments.size() == 1) {
            return new ArrayList<>(rootMemento.getChildren());
        } else {
            throw new TemplateModelException("Incorrect number of arguments for getChildren.");
        }
    }
}
