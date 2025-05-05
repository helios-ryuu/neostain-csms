package com.neostain.csms.model;

import java.math.BigDecimal;

public class Product {
    private String productId;
    private String productName;
    private BigDecimal unitPrice;
    private String categoryId;

    public Product(String productId, String productName, BigDecimal unitPrice, String categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
}
