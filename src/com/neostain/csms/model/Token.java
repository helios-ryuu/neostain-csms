package com.neostain.csms.model;

import java.sql.Timestamp;

public class Token {
    private String id;
    private String username;
    private String value;
    private Timestamp expiresAt;
    private Timestamp issuedAt;
    private String status;

    public Token() {
    }

    public Token(String id, String username, String value, Timestamp expiresAt, Timestamp issuedAt, String status) {
        this.id = id;
        this.username = username;
        this.value = value;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
