package com.neostain.csms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Promotion {
    private String promotionId;
    private String promotionName;
    private Timestamp promotionStartTime;
    private Timestamp promotionEndTime;
    private String productId;
    private int minimumPurchaseQuantity;
    private String promoProductId;
    private int promoProductQuantity;
    private BigDecimal discountRate;

    public Promotion(String promotionId, String promotionName, Timestamp promotionStartTime, Timestamp promotionEndTime, String productId, int minimumPurchaseQuantity, String promoProductId, int promoProductQuantity, BigDecimal discountRate) {
        this.promotionId = promotionId;
        this.promotionName = promotionName;
        this.promotionStartTime = promotionStartTime;
        this.promotionEndTime = promotionEndTime;
        this.productId = productId;
        this.minimumPurchaseQuantity = minimumPurchaseQuantity;
        this.promoProductId = promoProductId;
        this.promoProductQuantity = promoProductQuantity;
        this.discountRate = discountRate;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Timestamp getPromotionStartTime() {
        return promotionStartTime;
    }

    public void setPromotionStartTime(Timestamp promotionStartTime) {
        this.promotionStartTime = promotionStartTime;
    }

    public Timestamp getPromotionEndTime() {
        return promotionEndTime;
    }

    public void setPromotionEndTime(Timestamp promotionEndTime) {
        this.promotionEndTime = promotionEndTime;
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
}
