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

    String create(String storeId, String memberId, String paymentId, String employeeId);

    boolean addItem(String invoiceId, String productId, int quantity);

    boolean calculateTotal(String invoiceId, int pointUsed);

    boolean cancel(String invoiceId);
}
