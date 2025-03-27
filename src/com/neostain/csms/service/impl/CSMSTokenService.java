package com.neostain.csms.service.impl;

import com.neostain.csms.model.Token;
import com.neostain.csms.service.api.TokenService;
import com.neostain.csms.util.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class CSMSTokenService implements TokenService {
    private final static Logger LOGGER = Logger.getLogger(CSMSTokenService.class.getName());

    @Override
    public boolean validateToken(String tokenValue) {
        Token token = getToken(tokenValue);
        if (token == null) {
            LOGGER.warning("[TOKEN.SERVICE.VALIDATE_TOKEN] Token không tồn tại");
            return false;
        }

        // Kiểm tra trạng thái token
        if (!"01".equals(token.getTokenStatusID())) {
            LOGGER.warning("[TOKEN.SERVICE.VALIDATE_TOKEN] Token không hợp lệ");
            return false;
        }

        // Kiểm tra thời hạn token
        Timestamp expiresAt = token.getExpiresAt();
        if (expiresAt == null || expiresAt.before(new Timestamp(System.currentTimeMillis()))) {
            LOGGER.warning("[TOKEN.SERVICE.VALIDATE_TOKEN] Token đã hết hạn");
            return false;
        }

        LOGGER.info("[TOKEN.SERVICE.VALIDATE_TOKEN] Token hợp lệ");
        return true;
    }

    @Override
    public boolean updateTokenStatus(String tokenValue, String tokenStatusID) {
        if (tokenValue == null || tokenValue.trim().isEmpty()) {
            LOGGER.warning("[TOKEN.SERVICE.UPDATE_TOKEN_STATUS] Giá trị token trống");
            return false;
        }

        String query = "UPDATE TOKEN SET TOKEN_STATUS_ID = ? WHERE TOKEN_VALUE = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tokenStatusID);
            stmt.setString(2, tokenValue);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("[TOKEN.SERVICE.UPDATE_TOKEN_STATUS] Cập nhật trạng thái token thành công");
                return true;
            } else {
                LOGGER.warning("[TOKEN.SERVICE.UPDATE_TOKEN_STATUS] Không tìm thấy token để cập nhật");
                return false;
            }
        } catch (Exception e) {
            LOGGER.severe("[TOKEN.SERVICE.UPDATE_TOKEN_STATUS] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Token getToken(String tokenValue) {
        if (tokenValue == null || tokenValue.trim().isEmpty()) {
            LOGGER.warning("[TOKEN.SERVICE.GET_TOKEN] Giá trị token trống");
            return null;
        }

        String query = "SELECT * FROM TOKEN t WHERE t.TOKEN_VALUE= ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tokenValue);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int TOKEN_ID = rs.getInt("TOKEN_ID");
                    String ACCOUNT_ID = rs.getString("ACCOUNT_ID");
                    String TOKEN_VALUE = rs.getString("TOKEN_VALUE");
                    Timestamp EXPIRES_AT = rs.getTimestamp("EXPIRES_AT");
                    Timestamp ISSUED_AT = rs.getTimestamp("ISSUED_AT");
                    String TOKEN_STATUS_ID = rs.getString("TOKEN_STATUS_ID");

                    return new Token(TOKEN_ID, ACCOUNT_ID, TOKEN_VALUE, EXPIRES_AT, ISSUED_AT, TOKEN_STATUS_ID);
                } else {
                    LOGGER.severe("[TOKEN.SERVICE.GET_TOKEN] Token không tồn tại: " + tokenValue);
                    return null;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("[TOKEN.SERVICE.GET_TOKEN] Lỗi: " + e.getMessage());
            return null;
        }
    }
}
