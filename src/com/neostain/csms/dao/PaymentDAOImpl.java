package com.neostain.csms.dao;

import com.neostain.csms.model.Payment;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PaymentDAOImpl implements PaymentDAO {
    private static final Logger LOGGER = Logger.getLogger(PaymentDAOImpl.class.getName());
    private final Connection conn;

    public PaymentDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Payment findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] Payment ID is empty");
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PAYMENT_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Payment findByName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            LOGGER.warning("[FIND_BY_NAME] Payment name is empty");
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PAYMENT_FIND_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_NAME] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PAYMENT_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Error: " + e.getMessage());
        }
        return payments;
    }

    @Override
    public boolean create(Payment payment) throws DuplicateFieldException {
        if (payment == null || StringUtils.isNullOrEmpty(payment.getName())) {
            LOGGER.warning("[CREATE] Payment or payment name is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PAYMENT_CREATE)) {
            ps.setString(1, payment.getName());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                String msg = e.getMessage().toUpperCase();
                if (msg.contains("UK_PAYMENT_NAME")) {
                    throw new DuplicateFieldException("paymentName", "Tên phương thức thanh toán đã tồn tại.");
                }
            }
            LOGGER.severe("[CREATE] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateName(String id, String name) throws DuplicateFieldException {
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(name)) {
            LOGGER.warning("[UPDATE_NAME] Payment ID or name is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PAYMENT_UPDATE_NAME)) {
            ps.setString(1, name);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                String msg = e.getMessage().toUpperCase();
                if (msg.contains("UK_PAYMENT_NAME")) {
                    throw new DuplicateFieldException("paymentName", "Tên phương thức thanh toán đã tồn tại.");
                }
            }
            LOGGER.severe("[UPDATE_NAME] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[DELETE] Payment ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.PAYMENT_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE] Error: " + e.getMessage());
            return false;
        }
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getString("ID"),
                rs.getString("NAME"),
                rs.getInt("IS_DELETED") == 1
        );
    }
}
