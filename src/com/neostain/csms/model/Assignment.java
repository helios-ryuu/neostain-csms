package com.neostain.csms.model;

import java.sql.Timestamp;

public class Assignment {
    private String id;
    private String employeeId;
    private String storeId;
    private Timestamp startTime;
    private Timestamp endTime;

    public Assignment(String id, String employeeId, String storeId, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.employeeId = employeeId;
        this.storeId = storeId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
