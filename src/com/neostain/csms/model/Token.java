package com.neostain.csms.model;

import java.sql.Timestamp;

public class Token {
    private final int tokenID;
    private final String accountID;
    private final Timestamp issuedAt;
    private final Timestamp expiresAt;
    private String tokenValue;
    private String tokenStatusID;

    public Token(int tokenID, String accountID, String tokenValue, Timestamp expiresAt, Timestamp issuedAt, String tokenStatusID) {
        this.tokenID = tokenID;
        this.accountID = accountID;
        this.tokenValue = tokenValue;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.tokenStatusID = tokenStatusID;
    }

    // Getters và Setters
    public int getTokenID() {
        return tokenID;
    }

    public String getAccountID() {
        return accountID;
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

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public String getTokenStatusID() {
        return tokenStatusID;
    }

    public void setTokenStatusID(String tokenStatusID) {
        this.tokenStatusID = tokenStatusID;
    }
}
