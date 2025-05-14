package com.neostain.csms.dao;

import com.neostain.csms.model.Promotion;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface PromotionDAO {
    Promotion findById(String id);

    List<Promotion> findByName(String name);

    List<Promotion> findByProductId(String productId);

    List<Promotion> findAll();

    boolean create(Promotion promotion);

    boolean updateName(String id, String name);

    boolean updateStartTime(String id, Timestamp startTime);

    boolean updateEndTime(String id, Timestamp endTime);

    boolean updateProductId(String id, String productId);

    boolean updateMinimumPurchaseQuantity(String id, int quantity);

    boolean updatePromoProductId(String id, String promoProductId);

    boolean updatePromoProductQuantity(String id, String promoProductQuantity);

    boolean updateDiscountRate(String id, BigDecimal discountRate);

    boolean delete(String id);
}
