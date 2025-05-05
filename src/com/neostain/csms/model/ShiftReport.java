package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ShiftReport {
    private String shiftReportId;
    private Timestamp shiftStartTime;
    private Timestamp shiftEndTime;
    private BigDecimal ewalletRevenue;
    private BigDecimal cashRevenue;
    private BigDecimal bankRevenue;
    private int transactionCount;
    private String storeId;
    private String employeeId;

    public ShiftReport(String shiftReportId, Timestamp shiftStartTime, Timestamp shiftEndTime, BigDecimal ewalletRevenue, BigDecimal cashRevenue, BigDecimal bankRevenue, int transactionCount, String storeId, String employeeId) {
        this.shiftReportId = shiftReportId;
        this.shiftStartTime = shiftStartTime;
        this.shiftEndTime = shiftEndTime;
        this.ewalletRevenue = ewalletRevenue;
        this.cashRevenue = cashRevenue;
        this.bankRevenue = bankRevenue;
        this.transactionCount = transactionCount;
        this.storeId = storeId;
        this.employeeId = employeeId;
    }

    public String getShiftReportId() {
        return shiftReportId;
    }

    public void setShiftReportId(String shiftReportId) {
        this.shiftReportId = shiftReportId;
    }

    public Timestamp getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(Timestamp shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public Timestamp getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(Timestamp shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
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
