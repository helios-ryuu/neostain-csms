package com.neostain.csms.dao;

import com.neostain.csms.model.Invoice;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class InvoiceDAOImpl implements InvoiceDAO {
    private static final Logger LOGGER = Logger.getLogger(InvoiceDAOImpl.class.getName());
    private final Connection conn;

    public InvoiceDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Invoice findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] Invoice ID is empty");
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.INVOICE_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Invoice> findByStoreId(String storeId) {
        List<Invoice> invoices = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(storeId)) {
            LOGGER.warning("[FIND_BY_STORE_ID] Store ID is empty");
            return invoices;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.INVOICE_FIND_BY_STORE_ID)) {
            ps.setString(1, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_STORE_ID] Error: " + e.getMessage());
        }
        return invoices;
    }

    @Override
    public List<Invoice> findByMemberId(String memberId) {
        List<Invoice> invoices = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(memberId)) {
            LOGGER.warning("[FIND_BY_MEMBER_ID] Member ID is empty");
            return invoices;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.INVOICE_FIND_BY_MEMBER_ID)) {
            ps.setString(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_MEMBER_ID] Error: " + e.getMessage());
        }
        return invoices;
    }

    @Override
    public List<Invoice> findByPaymentId(String paymentId) {
        List<Invoice> invoices = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(paymentId)) {
            LOGGER.warning("[FIND_BY_PAYMENT_ID] Payment ID is empty");
            return invoices;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.INVOICE_FIND_BY_PAYMENT_ID)) {
            ps.setString(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_PAYMENT_ID] Error: " + e.getMessage());
        }
        return invoices;
    }

    @Override
    public List<Invoice> findByStatus(String status) {
        List<Invoice> invoices = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(status)) {
            LOGGER.warning("[FIND_BY_STATUS] Status is empty");
            return invoices;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.INVOICE_FIND_BY_STATUS)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_STATUS] Error: " + e.getMessage());
        }
        return invoices;
    }

    @Override
    public List<Invoice> findByEmployeeId(String employeeId) {
        List<Invoice> invoices = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(employeeId)) {
            LOGGER.warning("[FIND_BY_EMPLOYEE_ID] Employee ID is empty");
            return invoices;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.INVOICE_FIND_BY_EMPLOYEE_ID)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_EMPLOYEE_ID] Error: " + e.getMessage());
        }
        return invoices;
    }

    @Override
    public List<Invoice> findAll() {
        List<Invoice> invoices = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.INVOICE_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Error: " + e.getMessage());
        }
        return invoices;
    }

    @Override
    public List<Invoice> search(
            String id, String customerId, String employeeId,
            String status, String paymentMethod,
            String from, String to
    ) {
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM invoice WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (!StringUtils.isNullOrEmpty(id)) {
            sql.append(" AND ID=?");
            params.add(id);
        }
        if (!StringUtils.isNullOrEmpty(customerId)) {
            sql.append(" AND MEMBER_ID=?");
            params.add(customerId);
        }
        if (!StringUtils.isNullOrEmpty(employeeId)) {
            sql.append(" AND EMPLOYEE_ID=?");
            params.add(employeeId);
        }
        if (!Objects.equals(status, "Tất cả trạng thái")) {
            sql.append(" AND STATUS=?");
            params.add(status);
        }
        if (!Objects.equals(paymentMethod, "Tất cả phương thức")) {
            sql.append(" AND PAYMENT_ID=?");
            params.add(paymentMethod);
        }
        if (!StringUtils.isNullOrEmpty(from)) {
            sql.append(" AND CREATION_TIME >= ?");
            params.add(Date.valueOf(from));
        }
        if (!StringUtils.isNullOrEmpty(to)) {
            sql.append(" AND CREATION_TIME <= ? ");
            params.add(Date.valueOf(to));
        }
        sql.append("ORDER BY CREATION_TIME DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<Invoice> invoices = new ArrayList<>();
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
            return invoices;
        } catch (SQLException e) {
            LOGGER.severe("[SEARCH] Lỗi: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public String create(String storeId, String memberId, String paymentId, String employeeId, int pointUsed) {
        if (StringUtils.isNullOrEmpty(storeId) || StringUtils.isNullOrEmpty(memberId) || StringUtils.isNullOrEmpty(paymentId) || StringUtils.isNullOrEmpty(employeeId) || pointUsed < 0) {
            LOGGER.warning("[CREATE] Some fields is empty");
            return "";
        }
        try (CallableStatement cs = conn.prepareCall(SQLQueries.INVOICE_CREATE)) {
            cs.setString(1, storeId);
            cs.setString(2, memberId);
            cs.setString(3, paymentId);
            cs.setString(4, employeeId);
            cs.setInt(5, pointUsed);
            cs.registerOutParameter(6, Types.VARCHAR);
            if (cs.execute()) {
                return cs.getString("INVOICE_ID");
            }
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Error: " + e.getMessage());
        }
        return "";
    }

    @Override
    public boolean addItem(String invoiceId, String productId, int quantity) {
        if (StringUtils.isNullOrEmpty(invoiceId) || StringUtils.isNullOrEmpty(productId) || quantity < 0) {
            LOGGER.warning("[ADD_ITEM] invoiceId or productId is empty");
            return false;
        }
        try (CallableStatement cs = conn.prepareCall(SQLQueries.INVOICE_ADD_ITEM)) {
            cs.setString(1, invoiceId);
            cs.setString(2, productId);
            cs.setInt(3, quantity);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("[ADD_ITEM] Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean calculateTotal(String invoiceId) {
        if (StringUtils.isNullOrEmpty(invoiceId)) {
            LOGGER.warning("[CALCULATE_TOTAL] invoiceId is empty");
            return false;
        }
        try (CallableStatement cs = conn.prepareCall(SQLQueries.INVOICE_CALCULATE)) {
            cs.setString(1, invoiceId);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("[CALCULATE_TOTAL] Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean cancel(String invoiceId) {
        if (StringUtils.isNullOrEmpty(invoiceId)) {
            LOGGER.warning("[CANCEL] invoiceId is empty");
            return false;
        }
        if (Objects.equals(findById(invoiceId).getStatus(), "Đã hủy")) {
            LOGGER.warning("[CANCEL] Invoice is cancelled already: " + invoiceId);
            return false;
        }
        try (CallableStatement cs = conn.prepareCall(SQLQueries.INVOICE_CANCEL)) {
            cs.setString(1, invoiceId);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("[CANCEL] Error: " + e.getMessage());
        }
        return false;
    }

    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        return new Invoice(
                rs.getString("ID"),
                rs.getTimestamp("CREATION_TIME"),
                rs.getBigDecimal("NET_AMOUNT"),
                rs.getBigDecimal("DISCOUNT"),
                rs.getBigDecimal("TOTAL_DUE"),
                rs.getString("STORE_ID"),
                rs.getString("MEMBER_ID"),
                rs.getString("PAYMENT_ID"),
                rs.getString("STATUS"),
                rs.getString("EMPLOYEE_ID"),
                rs.getInt("POINT_USED")
        );
    }
}
