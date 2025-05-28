package com.neostain.csms.dao;

import com.neostain.csms.model.Promotion;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
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
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] Promotion ID is empty");
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPromotion(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Promotion> findByName(String name) {
        List<Promotion> promotions = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(name)) {
            LOGGER.warning("[FIND_BY_NAME] Promotion name is empty");
            return promotions;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_FIND_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    promotions.add(mapResultSetToPromotion(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_NAME] Error: " + e.getMessage());
        }
        return promotions;
    }

    @Override
    public List<Promotion> findByProductId(String productId) {
        List<Promotion> promotions = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(productId)) {
            LOGGER.warning("[FIND_BY_PRODUCT_ID] Product ID is empty");
            return promotions;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_FIND_BY_PRODUCT_ID)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    promotions.add(mapResultSetToPromotion(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_PRODUCT_ID] Error: " + e.getMessage());
        }
        return promotions;
    }

    @Override
    public List<Promotion> findAll() {
        List<Promotion> promotions = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    promotions.add(mapResultSetToPromotion(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Error: " + e.getMessage());
        }
        return promotions;
    }

    @Override
    public boolean create(Promotion promotion) {
        if (promotion == null) {
            LOGGER.warning("[CREATE] Promotion is null");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_CREATE)) {
            ps.setString(1, promotion.getName());
            ps.setTimestamp(2, promotion.getStartTime());
            ps.setTimestamp(3, promotion.getEndTime());
            ps.setString(4, promotion.getProductId());
            ps.setInt(5, promotion.getMinimumPurchaseQuantity());
            ps.setString(6, promotion.getPromoProductId());
            ps.setInt(7, promotion.getPromoProductQuantity());
            ps.setBigDecimal(8, promotion.getDiscountRate());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateName(String id, String name) {
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(name)) {
            LOGGER.warning("[UPDATE_NAME] Promotion ID or name is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_UPDATE_NAME)) {
            ps.setString(1, name);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_NAME] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStartTime(String id, Timestamp startTime) {
        if (StringUtils.isNullOrEmpty(id) || startTime == null) {
            LOGGER.warning("[UPDATE_START_TIME] Promotion ID or start time is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_UPDATE_START_TIME)) {
            ps.setTimestamp(1, startTime);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_START_TIME] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEndTime(String id, Timestamp endTime) {
        if (StringUtils.isNullOrEmpty(id) || endTime == null) {
            LOGGER.warning("[UPDATE_END_TIME] Promotion ID or end time is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_UPDATE_END_TIME)) {
            ps.setTimestamp(1, endTime);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_END_TIME] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateProductId(String id, String productId) {
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(productId)) {
            LOGGER.warning("[UPDATE_PRODUCT_ID] Promotion ID or product ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_UPDATE_PRODUCT_ID)) {
            ps.setString(1, productId);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_PRODUCT_ID] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateMinimumPurchaseQuantity(String id, int quantity) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[UPDATE_MINIMUM_PURCHASE_QUANTITY] Promotion ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_UPDATE_MINIMUM_PURCHASE_QUANTITY)) {
            ps.setInt(1, quantity);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_MINIMUM_PURCHASE_QUANTITY] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePromoProductId(String id, String promoProductId) {
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(promoProductId)) {
            LOGGER.warning("[UPDATE_PROMO_PRODUCT_ID] Promotion ID or promo product ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_UPDATE_PROMO_PRODUCT_ID)) {
            ps.setString(1, promoProductId);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_PROMO_PRODUCT_ID] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePromoProductQuantity(String id, int promoProductQuantity) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[UPDATE_PROMO_PRODUCT_QUANTITY] Promotion ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_UPDATE_PROMO_PRODUCT_QUANTITY)) {
            ps.setInt(1, promoProductQuantity);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_PROMO_PRODUCT_QUANTITY] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateDiscountRate(String id, BigDecimal discountRate) {
        if (StringUtils.isNullOrEmpty(id) || discountRate == null) {
            LOGGER.warning("[UPDATE_DISCOUNT_RATE] Promotion ID or discount rate is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_UPDATE_DISCOUNT_RATE)) {
            ps.setBigDecimal(1, discountRate);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_DISCOUNT_RATE] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[DELETE] Promotion ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PROMOTION_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE] Error: " + e.getMessage());
            return false;
        }
    }

    private Promotion mapResultSetToPromotion(ResultSet rs) throws SQLException {
        return new Promotion(
                rs.getString("ID"),
                rs.getString("NAME"),
                rs.getTimestamp("START_TIME"),
                rs.getTimestamp("END_TIME"),
                rs.getString("PRODUCT_ID"),
                rs.getInt("MINIMUM_PURCHASE_QUANTITY"),
                rs.getString("PROMO_PRODUCT_ID"),
                rs.getInt("PROMO_PRODUCT_QUANTITY"),
                rs.getBigDecimal("DISCOUNT_RATE"),
                rs.getInt("IS_DELETED") == 1
        );
    }
}
