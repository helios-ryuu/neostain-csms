package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Employee {

    private String employeeId;
    private Date hireDate;
    private String employeeName;
    private String position;
    private String departmentId;
    private String email;
    private String phoneNumber;
    private String address;
    private BigDecimal hourlyWage;
    private BigDecimal baseSalary;
    private String employeeStatusId;

    public Employee(String employeeId, String employeeName, String position, String departmentId,
                    Date hireDate, String email, String phoneNumber, String address,
                    BigDecimal hourlyWage, BigDecimal baseSalary, String employeeStatusId) {
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
        this.employeeStatusId = employeeStatusId;
    }

    // Getters và setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public java.sql.Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
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

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getEmployeeStatusId() {
        return employeeStatusId;
    }

    public void setEmployeeStatusId(String employeeStatusId) {
        this.employeeStatusId = employeeStatusId;
    }
}
