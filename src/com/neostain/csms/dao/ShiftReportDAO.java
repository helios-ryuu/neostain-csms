package com.neostain.csms.dao;

import java.util.List;

public interface ShiftReportDAO {
    ShiftReportDAO findById(String id);

    List<ShiftReportDAO> findByStoreId(String storeId);

    List<ShiftReportDAO> findByEmployeeId(String employeeId);

    List<ShiftReportDAO> findAll();

    String create(String storeId, String employeeId);

    boolean close(String shiftReportId);
}
