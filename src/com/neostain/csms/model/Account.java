package com.neostain.csms.model;

import java.sql.Timestamp;

public class Account {
    private String id;
    private String employeeId;
    private String username;
    private String passwordHash;
    private String roleId;
    private Timestamp creationTime;
    private String status;

    public Account(String id, String employeeId, String username, String passwordHash, String roleId, Timestamp creationTime, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
        this.creationTime = creationTime;
        this.status = status;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
