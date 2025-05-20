package com.neostain.csms.model;

import java.math.BigDecimal;

public class Product {
    private String id;
    private String nane;
    private BigDecimal unitPrice;
    private String categoryId;
    private boolean isDeleted;

    public Product(String id, String nane, BigDecimal unitPrice, String categoryId, boolean isDeleted) {
        this.id = id;
        this.nane = nane;
        this.unitPrice = unitPrice;
        this.categoryId = categoryId;
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return nane;
    }

    public void setName(String nane) {
        this.nane = nane;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
