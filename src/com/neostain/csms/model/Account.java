package com.neostain.csms.model;

import java.sql.Date;

public class Account {
    private final String accountID;
    private final Date accountCreationTime;
    private String username;
    private String passwordHash;
    private String roleID;

    public Account(String accountID, String username, String passwordHash, String roleID, Date accountCreationTime) {
        this.accountID = accountID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleID = roleID;
        this.accountCreationTime = accountCreationTime;
    }

    public String getAccountID() {
        return accountID;
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

}
