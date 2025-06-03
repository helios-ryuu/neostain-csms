package com.neostain.csms.dao;

import com.neostain.csms.model.Paycheck;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface PaycheckDAO {
    Paycheck findById(String id);

    List<Paycheck> findByEmployeeId(String employeeId);

    List<Paycheck> findAll();

    List<Paycheck> search(String id, String employeeId, String from, String to, String periodStart, String periodEnd);

    String create(String employeeId, BigDecimal deduction, Timestamp periodStard, Timestamp periodEnd) throws SQLException;
}
