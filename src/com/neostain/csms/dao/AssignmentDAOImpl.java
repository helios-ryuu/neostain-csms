package com.neostain.csms.dao;

import com.neostain.csms.model.Assignment;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

public class AssignmentDAOImpl implements AssignmentDAO {
    private static final Logger LOGGER = Logger.getLogger(AssignmentDAOImpl.class.getName());
    private final Connection conn;

    public AssignmentDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Assignment findById(String id) {
        return null;
    }

    @Override
    public List<Assignment> findByEmployeeId(String employeeId) {
        return List.of();
    }

    @Override
    public List<Assignment> findByStoreId(String storeId) {
        return List.of();
    }

    @Override
    public List<Assignment> findByRange(String start, String end) {
        return List.of();
    }

    @Override
    public List<Assignment> findAll() {
        return List.of();
    }

    @Override
    public boolean create(Assignment assignment) {
        return false;
    }

    @Override
    public boolean updateStartTime(String id, String startTime) {
        return false;
    }

    @Override
    public boolean updateEndTime(String id, String endTime) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
