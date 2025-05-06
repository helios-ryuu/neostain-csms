package com.neostain.csms.service;

import com.neostain.csms.dao.AccountDAO;
import com.neostain.csms.dao.RoleDAO;
import com.neostain.csms.dao.TokenDAO;
import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Role;
import com.neostain.csms.model.Token;
import com.neostain.csms.util.DatabaseUtils;
import com.neostain.csms.util.PasswordUtils;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import static com.neostain.csms.util.PasswordUtils.hash;

/**
 * Implementation của AuthService cung cấp các chức năng xác thực và phân quyền
 * Tách biệt logic xác thực và token để dễ quản lý, mở rộng
 */
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = Logger.getLogger(AuthServiceImpl.class.getName());
    private final TokenDAO tokenDao;
    private final AccountDAO accountDao;
    private final RoleDAO roleDAO;

    public AuthServiceImpl(TokenDAO tokenDao, AccountDAO accountDao, RoleDAO roleDAO) {
        this.tokenDao = tokenDao;
        this.accountDao = accountDao;
        this.roleDAO = roleDAO;
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

    @Override
    public boolean validate(String tokenValue) {
        Token token = this.getTokenByValue(tokenValue);
        if (token == null) {
            LOGGER.warning("[VALIDATE_TOKEN] Token không tồn tại");
            return false;
        }

        // Kiểm tra trạng thái token
        if (!"Có hiệu lực".equals(token.getTokenStatus())) {
            LOGGER.warning("[VALIDATE_TOKEN] Token không hợp lệ");
            return false;
        }

        // Kiểm tra thời hạn token
        Timestamp expiresAt = token.getExpiresAt();
        if (expiresAt == null || expiresAt.before(new Timestamp(System.currentTimeMillis()))) {
            LOGGER.warning("[VALIDATE_TOKEN] Token đã hết hạn");
            return false;
        }

        LOGGER.info("[VALIDATE_TOKEN] Token hợp lệ");
        return true;
    }

    @Override
    public Token getTokenByValue(String tokenValue) {
        if (StringUtils.isNullOrEmpty(tokenValue)) {
            LOGGER.warning("[GET_TOKEN_BY_VALUE] Giá trị token trống");
            return null;
        }

        try {
            return tokenDao.findByValue(tokenValue);
        } catch (Exception e) {
            LOGGER.warning("[GET_TOKEN_BY_VALUE] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String generateToken(String username) {
        try {
            String tokenValue = hash(System.currentTimeMillis() / 1000 + String.valueOf(username));
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);

            Token token = new Token();
            token.setUsername(username);
            token.setTokenValue(tokenValue);
            token.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
            token.setExpiresAt(Timestamp.valueOf(expiresAt));
            token.setTokenStatus("Có hiệu lực");

            boolean affected = tokenDao.create(token);
            if (affected) {
                LOGGER.info("[GENERATE_TOKEN] Tạo token thành công cho: " + username + "\nToken value: " + tokenValue
                        + "\nExpires at: " + expiresAt);
                return tokenValue;
            } else {
                LOGGER.severe("[GENERATE_TOKEN] Lỗi tạo token cho: " + username);
                return null;
            }
        } catch (Exception e) {
            LOGGER.severe("[GENERATE_TOKEN] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void invalidateToken(String tokenValue) {
        Token token = this.getTokenByValue(tokenValue);

        boolean affected = tokenDao.updateStatus(tokenValue, "Vô hiệu");
        if (affected) {
            LOGGER.info("[UPDATE_STATUS] Cập nhật trạng thái Vô hiệu cho token của user " + token.getUsername() + " thành công");
        } else {
            LOGGER.warning("[UPDATE_STATUS] Không tìm thấy token để cập nhật");
        }
    }


    @Override
    public Account getAccountByUsername(String username) {
        try {
            return accountDao.findByUsername(username);
        } catch (Exception e) {
            LOGGER.warning("[GET_ACCOUNT_BY_USERNAME] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean register(Account acc) {
        if (acc.getUsername().length() < 3) {
            LOGGER.warning("[REGISTER] Lỗi: Tên người dùng quá ngắn");
            return false;
        }
        try {
            accountDao.create(acc);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[REGISTER] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean changePassword(String username, String newPlainPassword) {
        if (StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(newPlainPassword)) {
            LOGGER.warning("[CHANGE_PWD] Tên người dùng trống hoặc mật khẩu trống");
            return false;
        }
        if (!PasswordUtils.isComplex(newPlainPassword)) {
            LOGGER.warning("[CHANGE_PWD] Password phải ≥8 ký tự, có uppercase, digit, special char");
            return false;
        }
        try {
            Account acc = accountDao.findByUsername(username);
            if (acc == null) {
                LOGGER.warning("[CHANGE_PWD] Account không tồn tại: " + username);
                return false;
            }
            if (PasswordUtils.verify(newPlainPassword, acc.getPasswordHash())) {
                LOGGER.warning("[CHANGE_PWD] New password trùng old password");
                return false;
            }
            String hashed = PasswordUtils.hash(newPlainPassword);
            return accountDao.updatePasswordHash(username, hashed);
        } catch (Exception e) {
            LOGGER.severe("[CHANGE_PWD] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeByUsername(String username) {
        if (StringUtils.isNullOrEmpty(username)) {
            LOGGER.warning("[REMOVE_BY_USERNAME] Username tài khoản trống");
            return false;
        }
        try {
            Account acc = accountDao.findByUsername(username);
            if (acc == null) {
                LOGGER.warning("[REMOVE_BY_USERNAME] Account không tồn tại: " + username);
                return false;
            }
            if ("ADMIN".equalsIgnoreCase(acc.getRoleId())) {
                LOGGER.warning("[REMOVE_BY_USERNAME] Không được xóa account ADMIN");
                return false;
            }
            accountDao.delete(username);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[REMOVE_BY_USERNAME] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Role getRoleById(String roleID) {
        return roleDAO.findById(roleID);
    }
}
