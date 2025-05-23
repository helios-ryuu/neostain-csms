package com.neostain.csms.model;

import java.sql.Timestamp;

public class Inventory {
    private String id;
    private String productId;
    private String storeId;
    private int quantity;
    private Timestamp modificationTime;

    public Inventory() {
    }

    public Inventory(String id, String productId, String storeId, int quantity, Timestamp modificationTime) {
        this.id = id;
        this.productId = productId;
        this.storeId = storeId;
        this.quantity = quantity;
        this.modificationTime = modificationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Timestamp modificationTime) {
        this.modificationTime = modificationTime;
    }
} 