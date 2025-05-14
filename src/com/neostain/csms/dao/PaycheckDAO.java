package com.neostain.csms.dao;

import com.neostain.csms.model.Paycheck;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface PaycheckDAO {
    Paycheck findById(String id);

    List<Paycheck> findByEmployeeId(String employeeId);

    List<Paycheck> findAll();

    String create(String employeeId, BigDecimal deduction, Timestamp periodStard, Timestamp periodEnd);

    boolean createForAll(BigDecimal deduction, Timestamp periodStard, Timestamp periodEnd);
}
