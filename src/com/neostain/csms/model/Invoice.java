package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Invoice {
    private String invoiceId;
    private Timestamp invoiceCreationTime;
    private BigDecimal netAmount;
    private BigDecimal discount;
    private BigDecimal totalDue;
    private String storeId;
    private String memberId;
    private String paymentId;
    private String invoiceStatus;
    private String employeeId;
    private int pointUsed;

    public Invoice(String invoiceId, Timestamp invoiceCreationTime, BigDecimal netAmount, BigDecimal discount, BigDecimal totalDue, String storeId, String memberId, String paymentId, String invoiceStatus, String employeeId, int pointUsed) {
        this.invoiceId = invoiceId;
        this.invoiceCreationTime = invoiceCreationTime;
        this.netAmount = netAmount;
        this.discount = discount;
        this.totalDue = totalDue;
        this.storeId = storeId;
        this.memberId = memberId;
        this.paymentId = paymentId;
        this.invoiceStatus = invoiceStatus;
        this.employeeId = employeeId;
        this.pointUsed = pointUsed;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Timestamp getInvoiceCreationTime() {
        return invoiceCreationTime;
    }

    public void setInvoiceCreationTime(Timestamp invoiceCreationTime) {
        this.invoiceCreationTime = invoiceCreationTime;
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

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
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
