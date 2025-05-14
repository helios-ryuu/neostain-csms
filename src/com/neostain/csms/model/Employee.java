package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Employee {

    private String id;
    private String name;
    private String position;
    private Timestamp hireDate;
    private String email;
    private String phoneNumber;
    private String address;
    private BigDecimal hourlyWage;
    private String status;
    private boolean isDeleted;

    public Employee(String id, String name, String position,
                    Timestamp hireDate, String email, String phoneNumber, String address,
                    BigDecimal hourlyWage, String status, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.hireDate = hireDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.hourlyWage = hourlyWage;
        this.status = status;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Timestamp getHireDate() {
        return hireDate;
    }

    public void setHireDate(Timestamp hireDate) {
        this.hireDate = hireDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(BigDecimal hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
