package com.neostain.csms.model;

import java.sql.Timestamp;

public class Token {
    private String tokenId;
    private String username;
    private String tokenValue;
    private Timestamp expiresAt;
    private Timestamp issuedAt;
    private String tokenStatus;

    public Token() {
    }

    public Token(String tokenId, String username, String tokenValue, Timestamp expiresAt, Timestamp issuedAt, String tokenStatus) {
        this.tokenId = tokenId;
        this.username = username;
        this.tokenValue = tokenValue;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.tokenStatus = tokenStatus;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
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

    public String getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(String tokenStatus) {
        this.tokenStatus = tokenStatus;
    }
}
