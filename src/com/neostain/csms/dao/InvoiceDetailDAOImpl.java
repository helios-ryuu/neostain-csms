package com.neostain.csms.dao;

import com.neostain.csms.model.InvoiceDetail;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class InvoiceDetailDAOImpl implements InvoiceDetailDAO {
    private static final Logger LOGGER = Logger.getLogger(InvoiceDetailDAOImpl.class.getName());
    private final Connection conn;

    public InvoiceDetailDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<InvoiceDetail> findByInvoiceId(String invoiceId) {
        if (StringUtils.isNullOrEmpty(invoiceId)) {
            LOGGER.warning("[FIND_BY_INVOICE_ID] Invoice ID is empty");
            return null;
        }
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.INVOICE_DETAIL_FIND_BY_INVOICE_ID)) {
            ps.setString(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoiceDetails.add(mapResultSetToInvoiceDetail(rs));
                }
                return invoiceDetails;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_INVOICE_ID] Error: " + e.getMessage());
        }
        return null;
    }

    private InvoiceDetail mapResultSetToInvoiceDetail(ResultSet rs) throws SQLException {
        return new InvoiceDetail(
                rs.getString("INVOICE_ID"),
                rs.getString("PRODUCT_ID"),
                rs.getInt("QUANTITY_SOLD"),
                rs.getBigDecimal("UNIT_PRICE")
        );
    }
}
