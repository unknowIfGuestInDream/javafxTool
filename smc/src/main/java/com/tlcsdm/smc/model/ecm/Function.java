package com.tlcsdm.smc.model.ecm;

import java.io.Serializable;

public class Function implements Serializable {

    public Function(Operation operation) {
        this.operation = operation;
    }

    public Function() {

    }

    private static final long serialVersionUID = -2020879350946056924L;
    private Operation operation;

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

}
