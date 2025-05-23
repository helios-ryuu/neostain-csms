package com.neostain.csms.dao;

import com.neostain.csms.model.Inventory;
import com.neostain.csms.util.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAOImpl implements InventoryDAO {
    private final Connection connection;

    public InventoryDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Inventory findById(String id) {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INVENTORY_FIND_BY_ID)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Inventory> findByProductId(String productId) {
        List<Inventory> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INVENTORY_FIND_BY_PRODUCT_ID)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Inventory> findByStoreId(String storeId) {
        List<Inventory> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INVENTORY_FIND_BY_STORE_ID)) {
            stmt.setString(1, storeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Inventory> findAll() {
        List<Inventory> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INVENTORY_FIND_ALL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private Inventory map(ResultSet rs) throws SQLException {
        Inventory inv = new Inventory();
        inv.setId(rs.getString("ID"));
        inv.setProductId(rs.getString("PRODUCT_ID"));
        inv.setStoreId(rs.getString("STORE_ID"));
        inv.setQuantity(rs.getInt("QUANTITY"));
        inv.setModificationTime(rs.getTimestamp("MODIFICATION_TIME"));
        return inv;
    }
} 