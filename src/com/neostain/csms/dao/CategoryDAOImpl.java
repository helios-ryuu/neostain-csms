package com.neostain.csms.dao;

import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.model.Category;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CategoryDAOImpl implements CategoryDAO {
    private static final Logger LOGGER = Logger.getLogger(CategoryDAOImpl.class.getName());
    private final Connection conn;

    public CategoryDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Category findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] ID danh mục trống");
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CATEGORY_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                } else {
                    LOGGER.warning("[FIND_BY_ID] Danh mục không tồn tại: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Category findByName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            LOGGER.warning("[FIND_BY_NAME] Tên danh mục trống");
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CATEGORY_FIND_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                } else {
                    LOGGER.warning("[FIND_BY_NAME] Danh mục không tồn tại: " + name);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_NAME] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CATEGORY_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapResultSetToCategory(rs));
                }
                return categories;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_NAME] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean create(Category category) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CATEGORY_CREATE)) {
            ps.setString(1, category.getCategoryId());
            ps.setString(2, category.getCategoryName());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateName(String id, String name) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CATEGORY_UPDATE_NAME)) {
            ps.setString(1, name);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_NAME] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CATEGORY_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        try {
            return new Category(
                    rs.getString("CATEGORY_ID"),
                    rs.getString("CATEGORY_NAME")
            );
        } catch (SQLException e) {
            LOGGER.severe("[MAP_RESULT_SET_TO_CATEGORY] Lỗi: " + e.getMessage());
            return null;
        }
    }
}
