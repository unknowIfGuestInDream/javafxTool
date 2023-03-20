package com.tlcsdm.smc.model.ecm;

import java.io.Serializable;

public class Operation implements Serializable {

    public Operation(String funcId, String support, String errorNote) {
        this.funcId = funcId;
        this.support = support;
        this.errorNote = errorNote;
    }

    public Operation() {

    }

    private static final long serialVersionUID = -1814595839203458699L;
    private String funcId;
    private String support;
    private String errorNote;

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getErrorNote() {
        return errorNote;
    }

    public void setErrorNote(String errorNote) {
        this.errorNote = errorNote;
    }
}
