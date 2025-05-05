package com.neostain.csms.service;

import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.util.DatabaseUtils;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import static com.neostain.csms.util.PasswordUtils.hash;

/**
 * Implementation của AuthService cung cấp các chức năng xác thực và phân quyền
 * Tách biệt logic xác thực và token để dễ quản lý, mở rộng
 */
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = Logger.getLogger(AuthServiceImpl.class.getName());

    public AuthServiceImpl() {
    }

    @Override
    public boolean authenticate(String username, String password) {
        if (StringUtils.isNullOrEmpty(username)) {
            LOGGER.warning("[AUTHENTICATE] Tên đăng nhập trống");
            return false;
        }
        if (StringUtils.isNullOrEmpty(password)) {
            LOGGER.warning("[AUTHENTICATE] Mật khẩu trống");
            return false;
        }

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLQueries.ACCOUNT_GET_PASSWORD_HASH)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("PASSWORD_HASH");
                    boolean isMatch = hash(password).equals(storedHash);

                    LOGGER.info("[AUTHENTICATE] Xác thực " + (isMatch ? "thành công" : "thất bại") +
                            " - Người dùng: " + username);
                    return isMatch;
                } else {
                    LOGGER.warning("[AUTHENTICATE] Người dùng không tồn tại: " + username);
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("[AUTHENTICATE] Lỗi xác thực: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isAuthorized(String username, String role) {
        if (StringUtils.isNullOrEmpty(username)) {
            LOGGER.warning("[IS_AUTHORIZED] Tên đăng nhập trống");
            return false;
        }

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLQueries.ACCOUNT_GET_ROLE_NAME)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean hasRole = role.equals(rs.getString("ROLE_NAME"));
                    LOGGER.info("[IS_AUTHORIZED] " + (hasRole ? "Được cấp quyền" : "Từ chối quyền") +
                            " - Người dùng: " + username + " - " + role);
                    return hasRole;
                } else {
                    LOGGER.warning("[IS_AUTHORIZED] Người dùng không tồn tại: " + username);
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("[IS_AUTHORIZED] Lỗi kiểm tra quyền: " + e.getMessage());
            return false;
        }
    }
}
