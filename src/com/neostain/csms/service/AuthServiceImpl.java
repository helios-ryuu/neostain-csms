package com.neostain.csms.service;

import com.neostain.csms.dao.*;
import com.neostain.csms.model.*;
import com.neostain.csms.util.DatabaseUtils;
import com.neostain.csms.util.PasswordUtils;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import static com.neostain.csms.util.PasswordUtils.hash;

/**
 * Implementation của AuthService cung cấp các chức năng xác thực và phân quyền
 * Tách biệt logic xác thực và token để dễ quản lý, mở rộng
 */
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = Logger.getLogger(AuthServiceImpl.class.getName());
    private final TokenDAO tokenDAO;
    private final AccountDAO accountDAO;
    private final RoleDAO roleDAO;
    private final EmployeeDAO employeeDAO;
    private final StoreDAO storeDAO;

    public AuthServiceImpl(TokenDAO tokenDAO, AccountDAO accountDAO, RoleDAO roleDAO, EmployeeDAO employeeDAO, StoreDAO storeDAO) {
        this.tokenDAO = tokenDAO;
        this.accountDAO = accountDAO;
        this.roleDAO = roleDAO;
        this.employeeDAO = employeeDAO;
        this.storeDAO = storeDAO;
    }

    @Override
    public boolean authenticate(String username, String password, String storeId) {
        if (StringUtils.isNullOrEmpty(username)) {
            LOGGER.warning("[AUTHENTICATE] Tên đăng nhập trống");
            return false;
        }
        if (StringUtils.isNullOrEmpty(password)) {
            LOGGER.warning("[AUTHENTICATE] Mật khẩu trống");
            return false;
        }
        if (StringUtils.isNullOrEmpty(storeId)) {
            LOGGER.warning("[AUTHENTICATE] Mã cửa hàng trống");
            return false;
        }

        try {
            Account account = accountDAO.findByUsername(username);
            if (account == null) {
                LOGGER.warning("[AUTHENTICATE] Người dùng không tồn tại: " + username);
                return false;
            }

            Store store = storeDAO.findById(storeId);
            if (store == null) {
                LOGGER.warning("[AUTHENTICATE] Cửa hàng không tồn tại: " + storeId);
                return false;
            }

            String hashedInputPassword = hash(password);
            if (!hashedInputPassword.equals(account.getPasswordHash())) {
                LOGGER.warning("[AUTHENTICATE] Mật khẩu không đúng - Người dùng: " + username);
                return false;
            }

            String role = account.getRoleId();  // Giả sử có getRole()
            Employee employee = employeeDAO.findById(account.getEmployeeId());
            if (employee == null) {
                LOGGER.warning("[AUTHENTICATE] Nhân viên không tồn tại: " + account.getEmployeeId());
                return false;
            }

            switch (role) {
                case "R001": // Quản lý cửa hàng
                    if (store.getManagerId().equals(employee.getId())) {
                        LOGGER.info("[AUTHENTICATE] Xác thực thành công (Quản lý cửa hàng) - Người dùng: " + username);
                        return true;
                    } else {
                        LOGGER.warning("[AUTHENTICATE] Người dùng không phải quản lý cửa hàng này");
                        return false;
                    }

                case "R002":
                    LOGGER.info("[AUTHENTICATE] Xác thực thành công (Nhân viên bán hàng) - Người dùng: " + username);
                    return true;
                default:
                    LOGGER.warning("[AUTHENTICATE] Vai trò không hợp lệ: " + role);
                    return false;
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
        if (!"CÓ HIỆU LỰC".equals(token.getStatus())) {
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
            return tokenDAO.findByValue(tokenValue);
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
            token.setValue(tokenValue);
            token.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
            token.setExpiresAt(Timestamp.valueOf(expiresAt));
            token.setStatus("CÓ HIỆU LỰC");

            boolean affected = tokenDAO.create(token);
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

        boolean affected = tokenDAO.updateStatus(tokenValue, "VÔ HIỆU");
        if (affected) {
            LOGGER.info("[UPDATE_STATUS] Cập nhật trạng thái Vô hiệu cho token của user " + token.getUsername() + " thành công");
        } else {
            LOGGER.warning("[UPDATE_STATUS] Không tìm thấy token để cập nhật");
        }
    }

    @Override
    public Account getAccountByUsername(String username) {
        try {
            return accountDAO.findByUsername(username);
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
            accountDAO.create(acc);
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
            Account acc = accountDAO.findByUsername(username);
            if (acc == null) {
                LOGGER.warning("[CHANGE_PWD] Account không tồn tại: " + username);
                return false;
            }
            if (PasswordUtils.verify(newPlainPassword, acc.getPasswordHash())) {
                LOGGER.warning("[CHANGE_PWD] New password trùng old password");
                return false;
            }
            String hashed = PasswordUtils.hash(newPlainPassword);
            return accountDAO.updatePasswordHash(username, hashed);
        } catch (Exception e) {
            LOGGER.severe("[CHANGE_PWD] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Role getRoleById(String roleID) {
        return roleDAO.findById(roleID);
    }

    @Override
    public boolean createAccount(Account account) throws DuplicateFieldException {
        return accountDAO.create(account);
    }

    @Override
    public boolean updateAccountPassword(String username, String newPasswordHash) {
        return accountDAO.updatePasswordHash(username, newPasswordHash);
    }

    @Override
    public boolean updateAccountStatus(String username, String status) {
        return accountDAO.updateStatus(username, status);
    }

    @Override
    public Account getAccountById(String id) {
        return accountDAO.findById(id);
    }

    @Override
    public List<Account> getAccountsByRoleId(String roleId) {
        return accountDAO.findByRoleId(roleId);
    }

    @Override
    public List<Account> getAccountsByStatus(String status) {
        return accountDAO.findByStatus(status);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }

    @Override
    public boolean createRole(Role role) throws DuplicateFieldException {
        return roleDAO.create(role);
    }

    @Override
    public boolean updateRoleName(String id, String name) throws DuplicateFieldException {
        return roleDAO.updateName(id, name);
    }

    @Override
    public boolean deleteRole(String id) {
        return roleDAO.delete(id);
    }

    @Override
    public List<Role> getRolesByName(String name) {
        return roleDAO.findByName(name);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDAO.findAll();
    }
}
