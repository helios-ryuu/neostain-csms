package com.neostain.csms.dao;

import com.neostain.csms.model.InventoryTransaction;
import com.neostain.csms.util.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryTransactionDAOImpl implements InventoryTransactionDAO {
    private final Connection connection;

    public InventoryTransactionDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public InventoryTransaction findById(String id) {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INVENTORY_TRANSACTION_FIND_BY_ID)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<InventoryTransaction> findByProductId(String productId) {
        List<InventoryTransaction> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INVENTORY_TRANSACTION_FIND_BY_PRODUCT_ID)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<InventoryTransaction> findByStoreId(String storeId) {
        List<InventoryTransaction> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INVENTORY_TRANSACTION_FIND_BY_STORE_ID)) {
            stmt.setString(1, storeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<InventoryTransaction> findAll() {
        List<InventoryTransaction> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INVENTORY_TRANSACTION_FIND_ALL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private InventoryTransaction map(ResultSet rs) throws SQLException {
        InventoryTransaction tx = new InventoryTransaction();
        tx.setId(rs.getString("ID"));
        tx.setProductId(rs.getString("PRODUCT_ID"));
        tx.setStoreId(rs.getString("STORE_ID"));
        tx.setTransactionType(rs.getString("TRANSACTION_TYPE"));
        tx.setQuantity(rs.getInt("QUANTITY"));
        tx.setCreationTime(rs.getTimestamp("CREATION_TIME"));
        return tx;
    }
} 