package com.neostain.csms.model;

import java.sql.Timestamp;

public class Token {
    private int tokenID;
    private String username;
    private Timestamp issuedAt;
    private Timestamp expiresAt;
    private String tokenValue;
    private String tokenStatusID;

    public Token() {
    }

    public Token(int tokenID, String username, String tokenValue, Timestamp expiresAt, Timestamp issuedAt, String tokenStatusID) {
        this.tokenID = tokenID;
        this.username = username;
        this.tokenValue = tokenValue;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.tokenStatusID = tokenStatusID;
    }

    // Getters và Setters
    public int getTokenID() {
        return tokenID;
    }

    public void setTokenID(int tokenID) {
        this.tokenID = tokenID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Timestamp getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Timestamp issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getTokenStatusID() {
        return tokenStatusID;
    }

    public void setTokenStatusID(String tokenStatusID) {
        this.tokenStatusID = tokenStatusID;
    }
}
