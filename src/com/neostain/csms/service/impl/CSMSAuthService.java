package com.neostain.csms.service.impl;

import com.neostain.csms.service.api.AuthService;
import com.neostain.csms.util.DatabaseUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/// Implementation của AuthService cung cấp các chức năng xác thực, phân quyền và tạo token cho người dùng.
public class CSMSAuthService implements AuthService {
    private static final Logger LOGGER = Logger.getLogger(CSMSAuthService.class.getName());

    @Override
    public boolean authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            LOGGER.warning("[AUTH.SERVICE.AUTHENTICATE] Tên đăng nhập trống");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            LOGGER.warning("[AUTH.SERVICE.AUTHENTICATE] Mật khẩu trống");
            return false;
        }

        final String query = "SELECT PASSWORD_HASH FROM ACCOUNT WHERE ACCOUNT_ID = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("PASSWORD_HASH");
                    boolean isMatch = hash(password).equals(storedHash);
                    LOGGER.info("[AUTH.SERVICE.AUTHENTICATE] Xác thực " + (isMatch ? "Thành công" : "Thất bại") + " - Người dùng: " + username);
                    return isMatch;
                } else {
                    LOGGER.severe("[AUTH.SERVICE.AUTHENTICATE] Người dùng không tồn tại: " + username);
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("[AUTH.SERVICE.AUTHENTICATE] Lỗi: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean isAuthorized(String username, String role) {
        if (username == null || username.trim().isEmpty()) {
            LOGGER.warning("[AUTH.SERVICE.IS_AUTHORIZED] Tên đăng nhập trống");
            return false;
        }

        final String query = "SELECT r.ROLE_NAME FROM ACCOUNT a JOIN ROLE r ON a.ROLE_ID = r.ROLE_ID WHERE a.username = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean hasRole = role.equals(rs.getString("ROLE_NAME"));
                    LOGGER.info("[AUTH.SERVICE.IS_AUTHORIZED] " + (hasRole ? "Được cấp quyền" : "Từ chối quyền") + " - Người dùng: " + username + " - " + role);
                    return hasRole;
                } else {
                    LOGGER.severe("[AUTH.SERVICE.IS_AUTHORIZED] Người dùng không tồn tại: " + username);
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("[AUTH.SERVICE.IS_AUTHORIZED] Lỗi: " + e.getMessage());
            return false;
        }
    }


    @Override
    public String generateToken(String accountID) {
        if (accountID == null || accountID.trim().isEmpty()) {
            LOGGER.warning("[AUTH.SERVICE.GENERATE_TOKEN] Tên đăng nhập trống");
            return null;
        }

        try (Connection conn = DatabaseUtils.getConnection()) {
            // Tạo token dựa trên thời gian hiện tại và băm bằng SHA-256
            String tokenValue = hash(System.currentTimeMillis() / 1000 + accountID);
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
            final String insertQuery = "INSERT INTO TOKEN (ACCOUNT_ID, TOKEN_VALUE, EXPIRES_AT, TOKEN_STATUS_ID) VALUES (?, ?, ?, '01')";

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, accountID);
                insertStmt.setString(2, tokenValue);
                insertStmt.setTimestamp(3, Timestamp.valueOf(expiresAt));
                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    LOGGER.info("[AUTH.SERVICE.GENERATE_TOKEN] Tạo token thành công cho: " + accountID + "\nToken value: " + tokenValue
                            + "\nExpires at: " + expiresAt);
                    return tokenValue;
                } else {
                    LOGGER.severe("[AUTH.SERVICE.GENERATE_TOKEN] Lỗi tạo token cho: " + accountID);
                    return null;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("[AUTH.SERVICE.GENERATE_TOKEN] Lỗi: " + e.getMessage());
            return null;
        }
    }

    /// Băm chuỗi đầu vào sử dụng thuật toán SHA-256.
    ///
    /// @param input Chuỗi cần băm.
    /// @return Chuỗi băm dưới dạng hex.
    /// @throws SecurityException Nếu có lỗi trong quá trình mã hóa.
    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new SecurityException("Lỗi mã hóa", e);
        }
    }
}
