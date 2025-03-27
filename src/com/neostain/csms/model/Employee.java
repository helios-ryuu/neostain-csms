package com.neostain.csms.model;

import java.math.BigDecimal;
import java.util.Date;

public class Employee {

    private final String employeeId;
    private final Date hireDate;
    private String employeeName;
    private String position;
    private String departmentId;
    private String email;
    private String phoneNumber;
    private String address;
    private BigDecimal hourlyWage;
    private BigDecimal baseSalary;

    public Employee(String employeeId, String employeeName, String position, String departmentId,
                    Date hireDate, String email, String phoneNumber, String address,
                    BigDecimal hourlyWage, BigDecimal baseSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.position = position;
        this.departmentId = departmentId;
        this.hireDate = hireDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.hourlyWage = hourlyWage;
        this.baseSalary = baseSalary;
    }

    // Getters và setters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public Date getHireDate() {
        return hireDate;
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

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }
}
