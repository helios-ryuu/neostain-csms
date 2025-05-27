package com.neostain.csms.service;

import com.neostain.csms.util.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticServiceImpl implements StatisticService {
    private Object runSingleValueQuery(String sql) {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getTodayRevenue() {
        return runSingleValueQuery("SELECT SUM(TOTAL_DUE) FROM INVOICE WHERE TRUNC(CREATION_TIME) = TRUNC(SYSDATE)");
    }

    @Override
    public Object getTodayInvoices() {
        return runSingleValueQuery("SELECT COUNT(*) FROM INVOICE WHERE TRUNC(CREATION_TIME) = TRUNC(SYSDATE)");
    }

    @Override
    public Object getTotalMembers() {
        return runSingleValueQuery("SELECT COUNT(*) FROM MEMBER WHERE IS_DELETED = 0");
    }

    @Override
    public Object getTotalVIPMembers() {
        return runSingleValueQuery("SELECT COUNT(*) FROM MEMBER WHERE IS_DELETED = 0 AND LOYALTY_POINTS > 1000");
    }

    @Override
    public Object getTotalProducts(String storeId) {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(com.neostain.csms.util.SQLQueries.INVENTORY_COUNT_BY_STORE)) {
            stmt.setString(1, storeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getTotalEmployees(String storeId) {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(com.neostain.csms.util.SQLQueries.EMPLOYEE_COUNT_BY_STORE)) {
            stmt.setString(1, storeId);
            stmt.setString(2, storeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getCanceledInvoices() {
        return runSingleValueQuery("SELECT COUNT(*) FROM INVOICE WHERE STATUS = 'ĐÃ HỦY'");
    }

    @Override
    public Object getUncompletedInvoices() {
        return runSingleValueQuery("SELECT COUNT(*) FROM INVOICE WHERE STATUS = 'CHƯA HOÀN THÀNH'");
    }

    @Override
    public Object getActivePromotions() {
        return runSingleValueQuery("SELECT ");
    }

    @Override
    public Object getTotalInvoicesLast30Days() {
        return runSingleValueQuery("SELECT COUNT(*) FROM INVOICE WHERE CREATION_TIME >= TRUNC(SYSDATE) - 30");
    }

    @Override
    public Object getTotalRevenueLast30Days() {
        return runSingleValueQuery("SELECT SUM(TOTAL_DUE) FROM INVOICE WHERE CREATION_TIME >= TRUNC(SYSDATE) - 30");
    }

    @Override
    public Object getCancelRequestedInvoices() {
        return runSingleValueQuery("SELECT COUNT(*) FROM INVOICE WHERE STATUS = 'ĐANG YÊU CẦU HỦY'");
    }
} 