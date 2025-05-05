package com.neostain.csms.service;

import com.neostain.csms.model.Store;

public interface StoreService {
    Store getStoreByManagerId(String id);

    Store getStoreById(String id);

    boolean add(Store store);

    boolean changeStoreName(String id, String name);

    boolean changeStoreAddress(String id, String address);
}
