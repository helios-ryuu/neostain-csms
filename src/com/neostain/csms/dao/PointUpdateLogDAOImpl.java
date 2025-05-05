package com.neostain.csms.dao;

import com.neostain.csms.model.PointUpdateLog;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

public class PointUpdateLogDAOImpl implements PointUpdateLogDAO {
    private static final Logger LOGGER = Logger.getLogger(PointUpdateLogDAOImpl.class.getName());
    private final Connection conn;

    public PointUpdateLogDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public PointUpdateLog findById(String id) {
        return null;
    }

    @Override
    public List<PointUpdateLog> findByMemberId(String memberId) {
        return List.of();
    }

    @Override
    public List<PointUpdateLog> findByInvoiceId(String invoiceId) {
        return List.of();
    }
}
