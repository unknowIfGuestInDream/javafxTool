package com.tlcsdm.smc.model.ecm;

import java.io.Serializable;
import java.util.ArrayList;

public class ECMInfos implements Serializable {

    private static final long serialVersionUID = 4306622990187402035L;
    private ArrayList<Category> CategoryInfos;
    private ArrayList<ErrorSource> ErrorSourceInfos;

    public ECMInfos() {
        CategoryInfos = new ArrayList<>();
        ErrorSourceInfos = new ArrayList<>();
    }

    public ArrayList<Category> getCategoryInfos() {
        return CategoryInfos;
    }

    public void setCategoryInfos(ArrayList<Category> categoryInfos) {
        CategoryInfos = categoryInfos;
    }

    public ArrayList<ErrorSource> getErrorSourceInfos() {
        return ErrorSourceInfos;
    }

    public void setErrorSourceInfos(ArrayList<ErrorSource> errorSourceInfos) {
        ErrorSourceInfos = errorSourceInfos;
    }

}
