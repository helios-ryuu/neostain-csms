package com.neostain.csms.dao;

import com.neostain.csms.model.Store;

import java.util.List;

public interface StoreDAO {
    Store findById(String id);

    Store findByManagerId(String id);

    List<Store> findAll();

    boolean create(Store store);

    void updateStoreName(String id, String name);

    void updateStoreAddress(String id, String address);
}
