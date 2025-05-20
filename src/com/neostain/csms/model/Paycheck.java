package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Paycheck {
    private String id;
    private String employeeId;
    private BigDecimal grossAmount;
    private BigDecimal deductions;
    private BigDecimal netAmount;
    private Timestamp payDate;
    private Timestamp periodStart;
    private Timestamp periodEnd;

    public Paycheck(String id, String employeeId, BigDecimal grossAmount, BigDecimal deductions, BigDecimal netAmount, Timestamp payDate, Timestamp periodStart, Timestamp periodEnd) {
        this.id = id;
        this.employeeId = employeeId;
        this.grossAmount = grossAmount;
        this.deductions = deductions;
        this.netAmount = netAmount;
        this.payDate = payDate;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Timestamp getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Timestamp periodStart) {
        this.periodStart = periodStart;
    }

    public Timestamp getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Timestamp periodEnd) {
        this.periodEnd = periodEnd;
    }
}
