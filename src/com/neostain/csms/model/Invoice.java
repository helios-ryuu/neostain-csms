package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Invoice {
    private String id;
    private Timestamp creationTime;
    private BigDecimal netAmount;
    private BigDecimal discount;
    private BigDecimal totalDue;
    private String storeId;
    private String memberId;
    private String paymentId;
    private String status;
    private String employeeId;
    private int pointUsed;

    public Invoice(String id, Timestamp creationTime, BigDecimal netAmount, BigDecimal discount, BigDecimal totalDue, String storeId, String memberId, String paymentId, String status, String employeeId, int pointUsed) {
        this.id = id;
        this.creationTime = creationTime;
        this.netAmount = netAmount;
        this.discount = discount;
        this.totalDue = totalDue;
        this.storeId = storeId;
        this.memberId = memberId;
        this.paymentId = paymentId;
        this.status = status;
        this.employeeId = employeeId;
        this.pointUsed = pointUsed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public int getPointUsed() {
        return pointUsed;
    }

    public void setPointUsed(int pointUsed) {
        this.pointUsed = pointUsed;
    }

}
