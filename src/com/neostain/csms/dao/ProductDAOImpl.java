package com.neostain.csms.dao;

import com.neostain.csms.model.Product;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductDAOImpl implements ProductDAO {
    private static final Logger LOGGER = Logger.getLogger(ProductDAOImpl.class.getName());
    private final Connection conn;

    public ProductDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Product findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] Product ID is empty");
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PRODUCT_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> findByName(String name) {
        List<Product> products = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(name)) {
            LOGGER.warning("[FIND_BY_NAME] Product name is empty");
            return products;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PRODUCT_FIND_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_NAME] Error: " + e.getMessage());
        }
        return products;
    }

    @Override
    public List<Product> findByCategoryId(String categoryID) {
        List<Product> products = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(categoryID)) {
            LOGGER.warning("[FIND_BY_CATEGORY_ID] Category ID is empty");
            return products;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PRODUCT_FIND_BY_CATEGORY_ID)) {
            ps.setString(1, categoryID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_CATEGORY_ID] Error: " + e.getMessage());
        }
        return products;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PRODUCT_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Error: " + e.getMessage());
        }
        return products;
    }

    @Override
    public boolean create(Product product) {
        if (product == null) {
            LOGGER.warning("[CREATE] Product is null");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PRODUCT_CREATE)) {
            ps.setString(1, product.getId());
            ps.setString(2, product.getName());
            ps.setBigDecimal(3, product.getUnitPrice());
            ps.setString(4, product.getCategoryId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateName(String id, String name) {
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(name)) {
            LOGGER.warning("[UPDATE_NAME] Product ID or name is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PRODUCT_UPDATE_NAME)) {
            ps.setString(1, name);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_NAME] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateUnitPrice(String id, int unitPrice) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[UPDATE_UNIT_PRICE] Product ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PRODUCT_UPDATE_UNIT_PRICE)) {
            ps.setInt(1, unitPrice);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_UNIT_PRICE] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCategoryId(String id, String categoryId) {
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(categoryId)) {
            LOGGER.warning("[UPDATE_CATEGORY_ID] Product ID or category ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PRODUCT_UPDATE_CATEGORY_ID)) {
            ps.setString(1, categoryId);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_CATEGORY_ID] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[DELETE] Product ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PRODUCT_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE] Error: " + e.getMessage());
            return false;
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getString("ID"),
                rs.getString("NAME"),
                rs.getBigDecimal("UNIT_PRICE"),
                rs.getString("CATEGORY_ID"),
                rs.getInt("IS_DELETED") == 1
        );
    }
}
