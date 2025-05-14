package com.neostain.csms.model;

import java.math.BigDecimal;

public class InvoiceDetail {
    private String invoiceId;
    private String productId;
    private int quantitySold;
    private BigDecimal unitPrice;

    public InvoiceDetail(String invoiceId, String productId, int quantitySold, BigDecimal unitPrice) {
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
