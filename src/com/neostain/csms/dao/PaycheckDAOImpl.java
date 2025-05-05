package com.neostain.csms.dao;

import com.neostain.csms.model.Paycheck;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class PaycheckDAOImpl implements PaycheckDAO {
    private static final Logger LOGGER = Logger.getLogger(PaycheckDAOImpl.class.getName());
    private final Connection conn;

    public PaycheckDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Paycheck findById(String id) {
        return null;
    }

    @Override
    public Paycheck findByEmployeeId(String employeeId) {
        return null;
    }

    @Override
    public Paycheck findAll() {
        return null;
    }

    @Override
    public String create(String employeeId, BigDecimal deduction, Timestamp periodStard, Timestamp periodEnd) {
        return "";
    }

    @Override
    public boolean createForAll(BigDecimal deduction, Timestamp periodStard, Timestamp periodEnd) {
        return false;
    }
}
