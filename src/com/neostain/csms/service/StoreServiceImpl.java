package com.neostain.csms.service;

import com.neostain.csms.dao.StoreDAO;
import com.neostain.csms.model.Store;
import com.neostain.csms.util.StringUtils;

import java.util.logging.Logger;

public class StoreServiceImpl implements StoreService {
    private final static Logger LOGGER = Logger.getLogger(StoreServiceImpl.class.getName());
    private final StoreDAO dao;

    public StoreServiceImpl(StoreDAO dao) {
        this.dao = dao;
    }


    @Override
    public Store getStoreByManagerId(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[GET_STORE_BY_MANAGER_ID] ID nhân viên quản lý trống");
            return null;
        }

        try {
            return dao.findByManagerId(id);
        } catch (Exception e) {
            LOGGER.warning("[GET_STORE_BY_MANAGER_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Store getStoreById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[GET_STORE_BY_ID] ID nhân viên quản lý trống");
            return null;
        }

        try {
            return dao.findById(id);
        } catch (Exception e) {
            LOGGER.warning("[GET_STORE_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean add(Store store) {
        try {
            dao.create(store);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[ADD] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean changeStoreName(String id, String name) {
        try {
            dao.updateStoreName(id, name);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[CHANGE_STORE_NAME] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean changeStoreAddress(String id, String address) {
        try {
            dao.updateStoreAddress(id, address);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[CHANGE_STORE_ADDRESS] Lỗi: " + e.getMessage());
            return false;
        }
    }
}
