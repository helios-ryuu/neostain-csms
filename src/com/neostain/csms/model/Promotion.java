package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Promotion {
    private String id;
    private String name;
    private Timestamp startTime;
    private Timestamp endTime;
    private String productId;
    private int minimumPurchaseQuantity;
    private String promoProductId;
    private int promoProductQuantity;
    private BigDecimal discountRate;
    private boolean isDeleted;

    public Promotion(String id, String name, Timestamp startTime, Timestamp endTime, String productId, int minimumPurchaseQuantity, String promoProductId, int promoProductQuantity, BigDecimal discountRate, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.productId = productId;
        this.minimumPurchaseQuantity = minimumPurchaseQuantity;
        this.promoProductId = promoProductId;
        this.promoProductQuantity = promoProductQuantity;
        this.discountRate = discountRate;
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getMinimumPurchaseQuantity() {
        return minimumPurchaseQuantity;
    }

    public void setMinimumPurchaseQuantity(int minimumPurchaseQuantity) {
        this.minimumPurchaseQuantity = minimumPurchaseQuantity;
    }

    public String getPromoProductId() {
        return promoProductId;
    }

    public void setPromoProductId(String promoProductId) {
        this.promoProductId = promoProductId;
    }

    public int getPromoProductQuantity() {
        return promoProductQuantity;
    }

    public void setPromoProductQuantity(int promoProductQuantity) {
        this.promoProductQuantity = promoProductQuantity;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
