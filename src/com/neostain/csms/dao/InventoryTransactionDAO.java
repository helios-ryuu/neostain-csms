package com.neostain.csms.dao;

import com.neostain.csms.model.InventoryTransaction;

import java.util.List;

public interface InventoryTransactionDAO {
    InventoryTransaction findById(String id);

    List<InventoryTransaction> findByProductId(String productId);

    List<InventoryTransaction> findByStoreId(String storeId);

    List<InventoryTransaction> findAll();

    boolean create(InventoryTransaction tx);
} 