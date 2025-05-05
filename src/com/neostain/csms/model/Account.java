package com.neostain.csms.model;

import java.sql.Timestamp;

public class Account {
    private String accountId;
    private String employeeId;
    private String username;
    private String passwordHash;
    private String roleId;
    private Timestamp accountCreationTime;
    private String accountStatus;

    public Account(String accountId, String employeeId, String username, String passwordHash, String roleId, Timestamp accountCreationTime, String accountStatus) {
        this.accountId = accountId;
        this.employeeId = employeeId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
        this.accountCreationTime = accountCreationTime;
        this.accountStatus = accountStatus;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public Timestamp getAccountCreationTime() {
        return accountCreationTime;
    }

    public void setAccountCreationTime(Timestamp accountCreationTime) {
        this.accountCreationTime = accountCreationTime;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
