package com.neostain.csms.model;

public class InvoiceDetail {
    private String invoiceId;
    private String productId;
    private int quantitySold;

    public InvoiceDetail(String invoiceId, String productId, int quantitySold) {
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantitySold = quantitySold;
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
}
