package com.neostain.csms.dao;

import com.neostain.csms.model.PointUpdateLog;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PointUpdateLogDAOImpl implements PointUpdateLogDAO {
    private static final Logger LOGGER = Logger.getLogger(PointUpdateLogDAOImpl.class.getName());
    private final Connection conn;

    public PointUpdateLogDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public PointUpdateLog findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] PointUpdateLog ID is empty");
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.POINT_UPDATE_LOG_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPointUpdateLog(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<PointUpdateLog> findByMemberId(String memberId) {
        List<PointUpdateLog> logs = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(memberId)) {
            LOGGER.warning("[FIND_BY_MEMBER_ID] Member ID is empty");
            return logs;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.POINT_UPDATE_LOG_FIND_BY_MEMBER_ID)) {
            ps.setString(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    logs.add(mapResultSetToPointUpdateLog(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_MEMBER_ID] Error: " + e.getMessage());
        }
        return logs;
    }

    @Override
    public List<PointUpdateLog> findByInvoiceId(String invoiceId) {
        List<PointUpdateLog> logs = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(invoiceId)) {
            LOGGER.warning("[FIND_BY_INVOICE_ID] Invoice ID is empty");
            return logs;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.POINT_UPDATE_LOG_FIND_BY_INVOICE_ID)) {
            ps.setString(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    logs.add(mapResultSetToPointUpdateLog(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_INVOICE_ID] Error: " + e.getMessage());
        }
        return logs;
    }

    private PointUpdateLog mapResultSetToPointUpdateLog(ResultSet rs) throws SQLException {
        return new PointUpdateLog(
                rs.getString("ID"),
                rs.getString("MEMBER_ID"),
                rs.getString("INVOICE_ID"),
                rs.getInt("POINT_CHANGE")
        );
    }
}
