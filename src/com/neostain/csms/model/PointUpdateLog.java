package com.neostain.csms.model;

public class PointUpdateLog {
    private String pointUpdateLogId;
    private String memberId;
    private String invoiceId;
    private int pointChange;

    public PointUpdateLog(String pointUpdateLogId, String memberId, String invoiceId, int pointChange) {
        this.pointUpdateLogId = pointUpdateLogId;
        this.memberId = memberId;
        this.invoiceId = invoiceId;
        this.pointChange = pointChange;
    }

    public String getPointUpdateLogId() {
        return pointUpdateLogId;
    }

    public void setPointUpdateLogId(String pointUpdateLogId) {
        this.pointUpdateLogId = pointUpdateLogId;
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
