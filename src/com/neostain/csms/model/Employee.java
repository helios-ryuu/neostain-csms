package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Employee {

    private String id;
    private String managerId;
    private String name;
    private Timestamp hireDate;
    private String email;
    private String phoneNumber;
    private String address;
    private BigDecimal hourlyWage;
    private String status;

    public Employee(String id, String managerId, String name, Timestamp hireDate,
                    String email, String phoneNumber, String address,
                    BigDecimal hourlyWage, String status) {
        this.id = id;
        this.managerId = managerId;
        this.name = name;
        this.hireDate = hireDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.hourlyWage = hourlyWage;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
