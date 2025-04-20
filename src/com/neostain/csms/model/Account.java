package com.neostain.csms.model;

import java.sql.Date;

public class Account {
    private int accountID;
    private String employeeID;
    private Date accountCreationTime;
    private String username;
    private String passwordHash;
    private String roleID;

    public Account(int accountID, String employeeID, String username, String passwordHash, String roleID, Date accountCreationTime) {
        this.accountID = accountID;
        this.employeeID = employeeID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleID = roleID;
        this.accountCreationTime = accountCreationTime;
    }

    // Getters và Setters
    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
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

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public Date getAccountCreationTime() {
        return accountCreationTime;
    }

    public void setAccountCreationTime(Date accountCreationTime) {
        this.accountCreationTime = accountCreationTime;
    }
}
