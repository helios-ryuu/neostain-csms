package com.neostain.csms.dao;

import com.neostain.csms.model.PointUpdateLog;

import java.util.List;

public interface PointUpdateLogDAO {
    PointUpdateLog findById(String id);

    List<PointUpdateLog> findByMemberId(String memberId);

    List<PointUpdateLog> findByInvoiceId(String invoiceId);
}
