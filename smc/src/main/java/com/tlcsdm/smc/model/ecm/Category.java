package com.tlcsdm.smc.model.ecm;

import java.io.Serializable;

public class Category implements Serializable {

    public Category(String categoryId, String categoryEnName, String categoryJpName) {
        this.categoryId = categoryId;
        this.categoryEnName = categoryEnName;
        this.categoryJpName = categoryJpName;
    }

    public Category() {

    }

    private static final long serialVersionUID = -7807341224241617751L;
    private String categoryId;
    private String categoryEnName;
    private String categoryJpName;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryEnName() {
        return categoryEnName;
    }

    public void setCategoryEnName(String categoryEnName) {
        this.categoryEnName = categoryEnName;
    }

    public String getCategoryJpName() {
        return categoryJpName;
    }

    public void setCategoryJpName(String categoryJpName) {
        this.categoryJpName = categoryJpName;
    }

}
