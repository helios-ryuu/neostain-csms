package com.neostain.csms.dao;

import com.neostain.csms.model.Payment;

import java.sql.Connection;
import java.util.logging.Logger;

public class PaymentDAOImpl implements PaymentDAO {
    private static final Logger LOGGER = Logger.getLogger(PaymentDAOImpl.class.getName());
    private final Connection conn;

    public PaymentDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Payment findById(String id) {
        return null;
    }

    @Override
    public Payment findByName(String name) {
        return null;
    }

    @Override
    public Payment findAll() {
        return null;
    }

    @Override
    public boolean create(Payment payment) {
        return false;
    }

    @Override
    public boolean updateName(String id, String name) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
