package com.neostain.csms.model;

public class Store {
    private String storeId;
    private String storeName;
    private String address;
    private String managerId;

    public Store(String storeId, String storeName, String address, String managerId) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.managerId = managerId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return address;
    }

    public void setStoreAddress(String address) {
        this.address = address;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
}
