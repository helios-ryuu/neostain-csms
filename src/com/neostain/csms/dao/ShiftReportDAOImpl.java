package com.neostain.csms.dao;

import com.neostain.csms.model.ShiftReport;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ShiftReportDAOImpl implements ShiftReportDAO {
    private static final Logger LOGGER = Logger.getLogger(ShiftReportDAOImpl.class.getName());
    private final Connection conn;

    public ShiftReportDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ShiftReport findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] ShiftReport ID is empty");
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.SHIFT_REPORT_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToShiftReport(rs);
                } else {
                    LOGGER.warning("[FIND_BY_ID] ShiftReport không tồn tại: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ShiftReport> findByStoreId(String storeId) {
        List<ShiftReport> reports = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(storeId)) {
            LOGGER.warning("[FIND_BY_STORE_ID] Store ID is empty");
            return reports;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.SHIFT_REPORT_FIND_BY_STORE_ID)) {
            ps.setString(1, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToShiftReport(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_STORE_ID] Error: " + e.getMessage());
        }
        return reports;
    }

    @Override
    public List<ShiftReport> findByEmployeeId(String employeeId) {
        List<ShiftReport> reports = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(employeeId)) {
            LOGGER.warning("[FIND_BY_EMPLOYEE_ID] Employee ID is empty");
            return reports;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.SHIFT_REPORT_FIND_BY_EMPLOYEE_ID)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToShiftReport(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_EMPLOYEE_ID] Error: " + e.getMessage());
        }
        return reports;
    }

    @Override
    public List<ShiftReport> findAll() {
        List<ShiftReport> reports = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.SHIFT_REPORT_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToShiftReport(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Error: " + e.getMessage());
        }
        return reports;
    }

    @Override
    public String create(String storeId, String employeeId) {
        if (StringUtils.isNullOrEmpty(storeId) || StringUtils.isNullOrEmpty(employeeId)) {
            LOGGER.warning("[CREATE] Store ID or employee ID is empty");
            return "";
        }
        try (CallableStatement cs = conn.prepareCall(SQLQueries.SHIFT_REPORT_CREATE)) {
            cs.setString(1, storeId);
            cs.setString(2, employeeId);
            cs.registerOutParameter(3, Types.CHAR);
            cs.execute();
            return cs.getString(3).trim();
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Error: " + e.getMessage());
        }
        return "";
    }

    @Override
    public boolean close(String shiftReportId) {
        if (StringUtils.isNullOrEmpty(shiftReportId)) {
            LOGGER.warning("[CLOSE] ShiftReport ID is empty");
            return false;
        }
        try (CallableStatement cs = conn.prepareCall(SQLQueries.SHIFT_REPORT_CLOSE)) {
            cs.setString(1, shiftReportId);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("[CLOSE] Error: " + e.getMessage());
        }
        LOGGER.warning("[CLOSE] Not implemented: requires stored procedure PRC_END_SHIFT");
        return false;
    }

    private ShiftReport mapResultSetToShiftReport(ResultSet rs) throws SQLException {
        return new ShiftReport(
                rs.getString("ID"),
                rs.getTimestamp("START_TIME"),
                rs.getTimestamp("END_TIME"),
                rs.getBigDecimal("EWALLET_REVENUE"),
                rs.getBigDecimal("CASH_REVENUE"),
                rs.getBigDecimal("BANK_REVENUE"),
                rs.getInt("TRANSACTION_COUNT"),
                rs.getString("STORE_ID"),
                rs.getString("EMPLOYEE_ID")
        );
    }
}
