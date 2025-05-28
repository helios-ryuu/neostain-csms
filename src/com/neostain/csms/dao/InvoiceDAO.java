package com.neostain.csms.dao;

import com.neostain.csms.model.Invoice;

import java.util.List;

public interface InvoiceDAO {
    Invoice findById(String id);

    List<Invoice> findByStoreId(String storeId);

    List<Invoice> findByMemberId(String memberId);

    List<Invoice> findByPaymentId(String paymentId);

    List<Invoice> findByStatus(String status);

    List<Invoice> findByEmployeeId(String employeeId);

    List<Invoice> findAll();

    List<Invoice> search(
            String id, String customerId, String employeeId,
            String status, String paymentMethod,
            String from, String to
    );

    String create(String storeId, String memberId, String paymentId, String employeeId, int pointUsed);

    void addItem(String invoiceId, String productId, int quantity);

    void addGift(String invoiceId, String productId, int quantity);

    boolean calculateTotal(String invoiceId);

    boolean cancel(String invoiceId);

    boolean updateStatus(String invoiceId, String status);
}
