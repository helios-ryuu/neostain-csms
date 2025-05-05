package com.neostain.csms.model;

public class Payment {
    private String paymentId;
    private String paymentName;

    public Payment(String paymentId, String paymentName) {
        this.paymentId = paymentId;
        this.paymentName = paymentName;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
