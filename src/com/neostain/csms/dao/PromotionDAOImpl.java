package com.neostain.csms.dao;

import com.neostain.csms.model.Promotion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

public class PromotionDAOImpl implements PromotionDAO {
    private static final Logger LOGGER = Logger.getLogger(PromotionDAOImpl.class.getName());
    private final Connection conn;

    public PromotionDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Promotion findById(String id) {
        return null;
    }

    @Override
    public List<Promotion> findByName(String name) {
        return List.of();
    }

    @Override
    public List<Promotion> findRange(Timestamp start, Timestamp end) {
        return List.of();
    }

    @Override
    public List<Promotion> findByProductId(String productId) {
        return List.of();
    }

    @Override
    public List<Promotion> findAll() {
        return List.of();
    }

    @Override
    public boolean create(Promotion promotion) {
        return false;
    }

    @Override
    public boolean updateName(String id, String name) {
        return false;
    }

    @Override
    public boolean updateStartTime(String id, Timestamp startTime) {
        return false;
    }

    @Override
    public boolean updateEndTime(String id, Timestamp endTime) {
        return false;
    }

    @Override
    public boolean updateProductId(String id, String productId) {
        return false;
    }

    @Override
    public boolean updateMinimumPurchaseQuantity(String id, int quantity) {
        return false;
    }

    @Override
    public boolean updatePromoProductId(String id, String promoProductId) {
        return false;
    }

    @Override
    public boolean updatePromoProductQuantity(String id, String promoProductQuantity) {
        return false;
    }

    @Override
    public boolean updateDiscountRate(String id, BigDecimal discountRate) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
