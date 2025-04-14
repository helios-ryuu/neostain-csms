package com.neostain.csms.model;

import java.sql.Date;

public class Cancellation {
    private final String cancellationID;
    private final String productID;
    private final Date cancellationTime;
    private final int cancelledQuantity;
    private final String description;
    private String cancellationStatus;

    public Cancellation(String cancellationID, String productID, Date cancellationTime, int cancelledQuantity, String description, String cancellationStatus) {
        this.cancellationID = cancellationID;
        this.productID = productID;
        this.cancellationTime = cancellationTime;
        this.cancelledQuantity = cancelledQuantity;
        this.description = description;
        this.cancellationStatus = cancellationStatus;
    }

    // Getters và Setters
    public String getCancellationID() {
        return cancellationID;
    }

    public String getProductID() {
        return productID;
    }

    public Date getCancellationTime() {
        return cancellationTime;
    }

    public int getCancelledQuantity() {
        return cancelledQuantity;
    }

    public String getDescription() {
        return description;
    }

    public String getCancellationStatus() {
        return cancellationStatus;
    }

    public void setCancellationStatus(String cancellationStatus) {
        this.cancellationStatus = cancellationStatus;
    }
}