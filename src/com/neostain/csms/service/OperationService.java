package com.neostain.csms.service;

import com.neostain.csms.model.Assignment;
import com.neostain.csms.model.Paycheck;
import com.neostain.csms.model.ShiftReport;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface OperationService {
    // Assignment
    Assignment getAssignmentById(String id);

    List<Assignment> getAssignmentsByEmployeeId(String employeeId);

    List<Assignment> getAssignmentsByStoreId(String storeId);

    List<Assignment> getAssignmentsByRange(String start, String end);

    List<Assignment> getAllAssignments();

    boolean createAssignment(Assignment assignment);

    boolean updateAssignmentStartTime(String id, String startTime);

    boolean updateAssignmentEndTime(String id, String endTime);

    boolean deleteAssignment(String id);

    // ShiftReport
    ShiftReport getShiftReportById(String id);

    List<ShiftReport> getShiftReportsByStoreId(String storeId);

    List<ShiftReport> getShiftReportsByEmployeeId(String employeeId);

    List<ShiftReport> getAllShiftReports();

    String createShiftReport(String storeId, String employeeId);

    boolean closeShiftReport(String shiftReportId);

    // Paycheck
    Paycheck getPaycheckById(String id);

    List<Paycheck> getPaychecksByEmployeeId(String employeeId);

    List<Paycheck> getAllPaychecks();

    String generatePaycheck(String employeeId, BigDecimal deductions, Timestamp periodStart, Timestamp periodEnd);

    List<Assignment> searchAssignments(String assignmentId, String employeeId, String storeId, String from, String to);
} 