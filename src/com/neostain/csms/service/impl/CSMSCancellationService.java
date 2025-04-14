package com.neostain.csms.service.impl;

import com.neostain.csms.model.Cancellation;
import com.neostain.csms.service.api.CancellationService;
import com.neostain.csms.util.DatabaseUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CSMSCancellationService implements CancellationService {
    private static final Logger LOGGER = Logger.getLogger(CSMSCancellationService.class.getName());

    @Override
    public Cancellation getCancellation(String cancellationID) {
        if  (cancellationID == null || cancellationID.trim().isEmpty()) {
            LOGGER.warning("[CANCELLATION.SERVICE.GET_CANCELLATION] ID yêu cầu hủy hàng trống");
            return null;
        }

        String query = "SELECT * FROM CANCELLATION c WHERE c.CANCELLATION_ID = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cancellationID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String CANCELLATION_ID = rs.getString("CANCELLATION_ID");
                    String PRODUCT_ID = rs.getString("PRODUCT_ID");
                    Date CANCELLATION_DATE = rs.getDate("CANCELLATION_DATE");
                    int CANCELLED_QUANTITY = rs.getInt("CANCELLED_QUANTITY");
                    String DESCRIPTION = rs.getString("DESCRIPTION");
                    String CANCELLATION_STATUS = rs.getString("CANCELLATION_STATUS");

                    return new Cancellation(CANCELLATION_ID, PRODUCT_ID, CANCELLATION_DATE, CANCELLED_QUANTITY, DESCRIPTION, CANCELLATION_STATUS);
                } else {
                    LOGGER.severe("[CANCELLATION.SERVICE.GET_CANCELLATION] Yêu cầu hủy không tồn tại: " + cancellationID);
                    return null;
                }
            }
        }
        catch (Exception e) {
            LOGGER.severe("[CANCELLATION.SERVICE.GET_CANCELLATION] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Cancellation> getCancellationsList() {
        // TODO: complete this
        return new ArrayList<>();
    }
}
