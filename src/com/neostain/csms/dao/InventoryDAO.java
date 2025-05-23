package com.neostain.csms.dao;

import com.neostain.csms.model.Inventory;

import java.util.List;

public interface InventoryDAO {
    Inventory findById(String id);

    List<Inventory> findByProductId(String productId);

    List<Inventory> findByStoreId(String storeId);

    List<Inventory> findAll();
} 