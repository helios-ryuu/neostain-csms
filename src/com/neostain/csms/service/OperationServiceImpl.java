package com.neostain.csms.service;

import com.neostain.csms.dao.AssignmentDAO;
import com.neostain.csms.dao.PaycheckDAO;
import com.neostain.csms.dao.ShiftReportDAO;
import com.neostain.csms.model.Assignment;
import com.neostain.csms.model.Paycheck;
import com.neostain.csms.model.ShiftReport;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class OperationServiceImpl implements OperationService {
    private final AssignmentDAO assignmentDAO;
    private final ShiftReportDAO shiftReportDAO;
    private final PaycheckDAO paycheckDAO;

    public OperationServiceImpl(AssignmentDAO assignmentDAO, ShiftReportDAO shiftReportDAO, PaycheckDAO paycheckDAO) {
        this.assignmentDAO = assignmentDAO;
        this.shiftReportDAO = shiftReportDAO;
        this.paycheckDAO = paycheckDAO;
    }

    // Assignment
    @Override
    public Assignment getAssignmentById(String id) {
        return assignmentDAO.findById(id);
    }

    @Override
    public List<Assignment> getAssignmentsByEmployeeId(String employeeId) {
        return assignmentDAO.findByEmployeeId(employeeId);
    }

    @Override
    public List<Assignment> getAssignmentsByStoreId(String storeId) {
        return assignmentDAO.findByStoreId(storeId);
    }

    @Override
    public List<Assignment> getAssignmentsByRange(String start, String end) {
        return assignmentDAO.findByRange(start, end);
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentDAO.findAll();
    }

    @Override
    public boolean createAssignment(Assignment assignment) {
        return assignmentDAO.create(assignment);
    }

    @Override
    public boolean updateAssignmentStartTime(String id, String startTime) {
        return assignmentDAO.updateStartTime(id, startTime);
    }

    @Override
    public boolean updateAssignmentEndTime(String id, String endTime) {
        return assignmentDAO.updateEndTime(id, endTime);
    }

    @Override
    public boolean deleteAssignment(String id) {
        return assignmentDAO.delete(id);
    }

    // ShiftReport
    @Override
    public ShiftReport getShiftReportById(String id) {
        return shiftReportDAO.findById(id);
    }

    @Override
    public List<ShiftReport> getShiftReportsByStoreId(String storeId) {
        return shiftReportDAO.findByStoreId(storeId);
    }

    @Override
    public List<ShiftReport> getShiftReportsByEmployeeId(String employeeId) {
        return shiftReportDAO.findByEmployeeId(employeeId);
    }

    @Override
    public List<ShiftReport> getAllShiftReports() {
        return shiftReportDAO.findAll();
    }

    @Override
    public String createShiftReport(String storeId, String employeeId) {
        // This would call a stored procedure in the DAO, but for now, just return false
        return shiftReportDAO.create(storeId, employeeId);
    }

    @Override
    public boolean closeShiftReport(String shiftReportId) {
        return shiftReportDAO.close(shiftReportId);
    }

    // Paycheck
    @Override
    public Paycheck getPaycheckById(String id) {
        return paycheckDAO.findById(id);
    }

    @Override
    public List<Paycheck> getPaychecksByEmployeeId(String employeeId) {
        return paycheckDAO.findByEmployeeId(employeeId);
    }

    @Override
    public List<Paycheck> getAllPaychecks() {
        return paycheckDAO.findAll();
    }

    @Override
    public String generatePaycheck(String employeeId, BigDecimal deductions, Timestamp periodStart, Timestamp periodEnd) {
        // This would call a stored procedure in the DAO, but for now, just return false
        return paycheckDAO.create(employeeId, deductions, periodStart, periodEnd);
    }
} 