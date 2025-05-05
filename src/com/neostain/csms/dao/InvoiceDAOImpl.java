package com.neostain.csms.dao;

import com.neostain.csms.model.Invoice;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

public class InvoiceDAOImpl implements InvoiceDAO {
    private static final Logger LOGGER = Logger.getLogger(InvoiceDAOImpl.class.getName());
    private final Connection conn;

    public InvoiceDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Invoice findById(String id) {
        return null;
    }

    @Override
    public List<Invoice> findByStoreId(String storeId) {
        return List.of();
    }

    @Override
    public List<Invoice> findByMemberId(String memberId) {
        return List.of();
    }

    @Override
    public List<Invoice> findByPaymentId(String paymentId) {
        return List.of();
    }

    @Override
    public List<Invoice> findByStatus(String status) {
        return List.of();
    }

    @Override
    public List<Invoice> findByEmployeeId(String employeeId) {
        return List.of();
    }

    @Override
    public List<Invoice> findAll() {
        return List.of();
    }

    @Override
    public String create(String storeId, String memberId, String paymentId, String employeeId) {
        return "";
    }

    @Override
    public boolean addItem(String invoiceId, String productId, int quantity) {
        return false;
    }

    @Override
    public boolean calculateTotal(String invoiceId, int pointUsed) {
        return false;
    }

    @Override
    public boolean cancel(String invoiceId) {
        return false;
    }
}
