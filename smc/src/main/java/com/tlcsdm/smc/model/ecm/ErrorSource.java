package com.tlcsdm.smc.model.ecm;

import java.io.Serializable;

public class ErrorSource implements Serializable {

    public ErrorSource(String errorSourceId, String categoryId, String errorSourceNumber, String errorSourceenName,
            String errorSourcejpName, Function function) {
        this.errorSourceId = errorSourceId;
        this.categoryId = categoryId;
        this.errorSourceNumber = errorSourceNumber;
        this.errorSourceenName = errorSourceenName;
        this.errorSourcejpName = errorSourcejpName;
        this.function = function;
    }

    public ErrorSource() {

    }

    private static final long serialVersionUID = 4306622990187402035L;

    private String errorSourceId;
    private String categoryId;
    private String errorSourceNumber;
    private String errorSourceenName;
    private String errorSourcejpName;
    private Function function;

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getErrorSourceId() {
        return errorSourceId;
    }

    public void setErrorSourceId(String errorSourceId) {
        this.errorSourceId = errorSourceId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getErrorSourceNumber() {
        return errorSourceNumber;
    }

    public void setErrorSourceNumber(String errorSourceNumber) {
        this.errorSourceNumber = errorSourceNumber;
    }

    public String getErrorSourceenName() {
        return errorSourceenName;
    }

    public void setErrorSourceenName(String errorSourceenName) {
        this.errorSourceenName = errorSourceenName;
    }

    public String getErrorSourcejpName() {
        return errorSourcejpName;
    }

    public void setErrorSourcejpName(String errorSourcejpName) {
        this.errorSourcejpName = errorSourcejpName;
    }

}
