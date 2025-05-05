package com.neostain.csms.dao;

import com.neostain.csms.model.Paycheck;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface PaycheckDAO {
    Paycheck findById(String id);

    Paycheck findByEmployeeId(String employeeId);

    Paycheck findAll();

    String create(String employeeId, BigDecimal deduction, Timestamp periodStard, Timestamp periodEnd);

    boolean createForAll(BigDecimal deduction, Timestamp periodStard, Timestamp periodEnd);
}
