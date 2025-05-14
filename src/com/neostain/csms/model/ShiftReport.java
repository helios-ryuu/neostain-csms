package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ShiftReport {
    private String id;
    private Timestamp startTime;
    private Timestamp endTime;
    private BigDecimal ewalletRevenue;
    private BigDecimal cashRevenue;
    private BigDecimal bankRevenue;
    private int transactionCount;
    private String storeId;
    private String employeeId;

    public ShiftReport(String id, Timestamp startTime, Timestamp endTime, BigDecimal ewalletRevenue, BigDecimal cashRevenue, BigDecimal bankRevenue, int transactionCount, String storeId, String employeeId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ewalletRevenue = ewalletRevenue;
        this.cashRevenue = cashRevenue;
        this.bankRevenue = bankRevenue;
        this.transactionCount = transactionCount;
        this.storeId = storeId;
        this.employeeId = employeeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getEwalletRevenue() {
        return ewalletRevenue;
    }

    public void setEwalletRevenue(BigDecimal ewalletRevenue) {
        this.ewalletRevenue = ewalletRevenue;
    }

    public BigDecimal getCashRevenue() {
        return cashRevenue;
    }

    public void setCashRevenue(BigDecimal cashRevenue) {
        this.cashRevenue = cashRevenue;
    }

    public BigDecimal getBankRevenue() {
        return bankRevenue;
    }

    public void setBankRevenue(BigDecimal bankRevenue) {
        this.bankRevenue = bankRevenue;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
