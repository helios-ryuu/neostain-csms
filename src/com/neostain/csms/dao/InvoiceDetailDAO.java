package com.neostain.csms.dao;

import com.neostain.csms.model.InvoiceDetail;

import java.util.List;

public interface InvoiceDetailDAO {
    List<InvoiceDetail> findByInvoiceId(String invoiceId);
}
