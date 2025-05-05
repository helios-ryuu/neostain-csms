package com.neostain.csms.dao;

import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.model.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StoreDAOImpl implements StoreDAO {
    public static final Logger LOGGER = Logger.getLogger(StoreDAOImpl.class.getName());
    private final Connection conn;

    public StoreDAOImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public Store findByManagerId(String id) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.STORE_FIND_BY_MANAGER_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStore(rs);
                } else {
                    LOGGER.warning("[FIND_BY_MANAGER_ID] Cửa hàng không tồn tại với mã quản lý: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_MANAGER_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Store> findAll() {
        List<Store> stores = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.STORE_FIND_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stores.add(mapResultSetToStore(rs));
            }
            return stores;
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Store findById(String id) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.STORE_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStore(rs);
                } else {
                    LOGGER.warning("[FIND_BY_ID] Cửa hàng không tồn tại: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateStoreName(String id, String name) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.STORE_UPDATE_STORE_NAME)) {
            ps.setString(1, name);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_STORE_NAME] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStoreAddress(String id, String address) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.STORE_UPDATE_ADDRESS)) {
            ps.setString(1, address);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_STORE_ADDRESS] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean create(Store store) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.STORE_CREATE)) {
            ps.setString(1, store.getStoreName());
            ps.setString(2, store.getStoreAddress());
            ps.setString(3, store.getManagerId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    private Store mapResultSetToStore(ResultSet rs) throws SQLException {
        return new Store(
                rs.getString("STORE_ID"),
                rs.getString("STORE_NAME"),
                rs.getString("ADDRESS"),
                rs.getString("MANAGER_ID")
        );
    }
}
