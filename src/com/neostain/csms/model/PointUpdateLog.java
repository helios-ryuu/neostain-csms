package com.neostain.csms.model;

public class PointUpdateLog {
    private String id;
    private String memberId;
    private String invoiceId;
    private int pointChange;

    public PointUpdateLog(String id, String memberId, String invoiceId, int pointChange) {
        this.id = id;
        this.memberId = memberId;
        this.invoiceId = invoiceId;
        this.pointChange = pointChange;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getPointChange() {
        return pointChange;
    }

    public void setPointChange(int pointChange) {
        this.pointChange = pointChange;
    }
}
