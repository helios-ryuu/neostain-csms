package com.neostain.csms.dao;

import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.model.Account;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Thực thi AccountDAO để quản lý tài khoản người dùng
 * Sử dụng các câu truy vấn SQL từ SQLQueries để dễ bảo trì
 */
public class AccountDAOImpl implements AccountDAO {
    private static final Logger LOGGER = Logger.getLogger(AccountDAOImpl.class.getName());
    private final Connection conn;

    public AccountDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(Account acc) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_CREATE)) {
            ps.setString(1, acc.getEmployeeID());
            ps.setString(2, acc.getUsername());
            ps.setString(3, acc.getPasswordHash());
            ps.setString(4, acc.getRoleID());
            ps.setDate(5, acc.getAccountCreationTime());
            return ps.executeUpdate();
        }
    }

    @Override
    public Account findByUsername(String username) throws SQLException {
        if (StringUtils.isNullOrEmpty(username)) {
            LOGGER.warning("[FIND_BY_USERNAME] Username tài khoản trống");
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_FIND_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                } else {
                    LOGGER.warning("[FIND_BY_USERNAME] Tài khoản không tồn tại: " + username);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_USERNAME] Lỗi truy vấn tài khoản với Username: " + username + " - " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean updatePassword(String username, String newHash) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_UPDATE_PASSWORD)) {
            ps.setString(1, newHash);
            ps.setString(2, username);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public int delete(String username) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_DELETE)) {
            ps.setString(1, username);
            return ps.executeUpdate();
        }
    }

    /**
     * Ánh xạ ResultSet sang đối tượng Account
     * Tách biệt logic đọc dữ liệu và tạo đối tượng để dễ bảo trì
     */
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("ACCOUNT_ID"),
                rs.getString("EMPLOYEE_ID"),
                rs.getString("USERNAME"),
                rs.getString("PASSWORD_HASH"),
                rs.getString("ROLE_ID"),
                rs.getDate("ACCOUNT_CREATION_TIME")
        );
    }
}
