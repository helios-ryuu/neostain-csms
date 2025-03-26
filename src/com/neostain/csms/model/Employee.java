package com.neostain.csms.model;

import java.math.BigDecimal;
import java.util.Date;

public class Employee {

    private final String employeeId;     // Mã nhân viên (12 ký tự)
    private final Date hireDate;         // Ngày gia nhập
    private String employeeName;   // Tên nhân viên (tối đa 50 ký tự)
    private String position;       // Vị trí/chức danh (tối đa 50 ký tự)
    private String departmentId;   // Mã phòng ban (5 ký tự)
    private String email;          // Email nhân viên (tối đa 40 ký tự)
    private String phoneNumber;    // Số điện thoại (tối đa 10 ký tự)
    private String address;        // Địa chỉ (tối đa 255 ký tự)
    private BigDecimal hourlyWage; // Lương theo giờ (NUMBER(15,2))
    private BigDecimal baseSalary; // Lương cơ bản (NUMBER(15,2))

    // Constructor đầy đủ
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

    // Getters and setters
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
