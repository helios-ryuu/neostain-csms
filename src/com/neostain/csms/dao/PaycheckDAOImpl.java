package com.neostain.csms.dao;

import com.neostain.csms.model.Paycheck;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PaycheckDAOImpl implements PaycheckDAO {
    private static final Logger LOGGER = Logger.getLogger(PaycheckDAOImpl.class.getName());
    private final Connection conn;

    public PaycheckDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Paycheck findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] Paycheck ID is empty");
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PAYCHECK_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPaycheck(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Paycheck> findByEmployeeId(String employeeId) {
        if (StringUtils.isNullOrEmpty(employeeId)) {
            LOGGER.warning("[FIND_BY_EMPLOYEE_ID] Employee ID is empty");
            return null;
        }
        List<Paycheck> paychecks = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PAYCHECK_FIND_BY_EMPLOYEE_ID)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    paychecks.add(mapResultSetToPaycheck(rs));
                }
                return paychecks;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_EMPLOYEE_ID] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Paycheck> findAll() {
        List<Paycheck> paychecks = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PAYCHECK_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    paychecks.add(mapResultSetToPaycheck(rs));
                }
                return paychecks;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Paycheck> search(String id, String employeeId, String from, String to, String periodStart, String periodEnd) {
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM PAYCHECK WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (!StringUtils.isNullOrEmpty(id)) {
            sql.append(" AND ID=?");
            params.add(id);
        }

        if (!StringUtils.isNullOrEmpty(employeeId)) {
            sql.append(" AND EMPLOYEE_ID=?");
            params.add(employeeId);
        }

        if (!StringUtils.isNullOrEmpty(from)) {
            sql.append(" AND PAY_DATE >= ?");
            params.add(from);
        }

        if (!StringUtils.isNullOrEmpty(to)) {
            sql.append(" AND PAY_DATE <= ?");
            params.add(to);
        }

        if (!StringUtils.isNullOrEmpty(periodStart)) {
            sql.append(" AND PERIOD_START >= ?");
            params.add(periodStart);
        }

        if (!StringUtils.isNullOrEmpty(periodEnd)) {
            sql.append(" AND PERIOD_END <= ?");
            params.add(periodEnd);
        }
        sql.append(" ORDER BY PAY_DATE DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<Paycheck> paychecks = new ArrayList<>();
            while (rs.next()) {
                paychecks.add(mapResultSetToPaycheck(rs));
            }
            return paychecks;
        } catch (SQLException e) {
            LOGGER.severe("[SEARCH] Lỗi: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public String create(String employeeId, BigDecimal deduction, Timestamp periodStard, Timestamp periodEnd) {
        if (StringUtils.isNullOrEmpty(employeeId) || deduction == null || periodStard == null || periodEnd == null) {
            LOGGER.warning("[CREATE] Employee ID, deduction or periodStard or periodEnd is empty");
            return "";
        }
        try (CallableStatement cs = conn.prepareCall(SQLQueries.PAYCHECK_CREATE)) {
            cs.setString(1, employeeId);
            cs.setBigDecimal(2, deduction);
            cs.setTimestamp(3, periodStard);
            cs.setTimestamp(4, periodEnd);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.execute();
            return cs.getString(5);
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Error: " + e.getMessage());
        }
        return "";
    }

    private Paycheck mapResultSetToPaycheck(ResultSet rs) throws SQLException {
        return new Paycheck(
                rs.getString("ID"),
                rs.getString("EMPLOYEE_ID"),
                rs.getBigDecimal("GROSS_AMOUNT"),
                rs.getBigDecimal("DEDUCTIONS"),
                rs.getBigDecimal("NET_AMOUNT"),
                rs.getTimestamp("PAY_DATE"),
                rs.getTimestamp("PERIOD_START"),
                rs.getTimestamp("PERIOD_END")
        );
    }
}
