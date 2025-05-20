package com.neostain.csms.model;

import java.sql.Date;

public class Member {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private Date registrationDate;
    private int loyaltyPoints;
    private boolean isDeleted;

    public Member(String id, String name, String phoneNumber, String email, Date registrationDate, int loyaltyPoints, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.registrationDate = registrationDate;
        this.loyaltyPoints = loyaltyPoints;
        this.isDeleted = isDeleted;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
