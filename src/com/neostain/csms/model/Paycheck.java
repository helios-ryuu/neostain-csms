package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Paycheck {
    private String paycheckId;
    private String employeeId;
    private BigDecimal grossAmount;
    private BigDecimal deductions;
    private BigDecimal netAmount;
    private Timestamp payDate;

    public Paycheck(String paycheckId, String employeeId, BigDecimal grossAmount, BigDecimal deductions, BigDecimal netAmount, Timestamp payDate) {
        this.paycheckId = paycheckId;
        this.employeeId = employeeId;
        this.grossAmount = grossAmount;
        this.deductions = deductions;
        this.netAmount = netAmount;
        this.payDate = payDate;
    }

    public String getPaycheckId() {
        return paycheckId;
    }

    public void setPaycheckId(String paycheckId) {
        this.paycheckId = paycheckId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public Timestamp getPayDate() {
        return payDate;
    }

    public void setPayDate(Timestamp payDate) {
        this.payDate = payDate;
    }
}
