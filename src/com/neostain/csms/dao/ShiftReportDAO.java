package com.neostain.csms.dao;

import com.neostain.csms.model.ShiftReport;

import java.util.List;

public interface ShiftReportDAO {
    ShiftReport findById(String id);

    List<ShiftReport> findByStoreId(String storeId);

    List<ShiftReport> findByEmployeeId(String employeeId);

    List<ShiftReport> findAll();

    String create(String storeId, String employeeId);

    boolean close(String shiftReportId);
}
