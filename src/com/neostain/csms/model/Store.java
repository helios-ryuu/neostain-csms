package com.neostain.csms.model;

public class Store {
    private String id;
    private String name;
    private String address;
    private String managerId;

    public Store(String id, String name, String address, String managerId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.managerId = managerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
