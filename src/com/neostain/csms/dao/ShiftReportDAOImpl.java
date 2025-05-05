package com.neostain.csms.dao;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

public class ShiftReportDAOImpl implements ShiftReportDAO {
    private static final Logger LOGGER = Logger.getLogger(ShiftReportDAOImpl.class.getName());
    private final Connection conn;

    public ShiftReportDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ShiftReportDAO findById(String id) {
        return null;
    }

    @Override
    public List<ShiftReportDAO> findByStoreId(String storeId) {
        return List.of();
    }

    @Override
    public List<ShiftReportDAO> findByEmployeeId(String employeeId) {
        return List.of();
    }

    @Override
    public List<ShiftReportDAO> findAll() {
        return List.of();
    }

    @Override
    public String create(String storeId, String employeeId) {
        return "";
    }

    @Override
    public boolean close(String shiftReportId) {
        return false;
    }
}
