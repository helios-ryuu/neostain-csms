package com.neostain.csms.dao;

import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.model.Account;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AccountDAOImpl implements AccountDAO {
    private static final Logger LOGGER = Logger.getLogger(AccountDAOImpl.class.getName());
    private final Connection conn;

    public AccountDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Account> findByEmployeeId(String employeeId) {
        if (StringUtils.isNullOrEmpty(employeeId)) {
            LOGGER.warning("[FIND_BY_EMPLOYEE_ID] ID nhân viên của tài khoản trống");
            return null;
        }
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_FIND_BY_EMPLOYEE_ID)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
                return accounts;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_EMPLOYEE_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Account findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] ID tài khoản trống");
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                } else {
                    LOGGER.warning("[FIND_BY_ID] Tài khoản không tồn tại: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Account findByUsername(String username) {
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
            LOGGER.severe("[FIND_BY_USERNAME] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
                return accounts;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Account> findByRoleId(String roleId) {
        if (StringUtils.isNullOrEmpty(roleId)) {
            LOGGER.warning("[FIND_BY_ROLE_ID] Mã vai trò của tài khoản trống");
            return null;
        }

        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_FIND_BY_ROLE_ID)) {
            ps.setString(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
                return accounts;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ROLE_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Account> findByStatus(String status) {
        if (StringUtils.isNullOrEmpty(status)) {
            LOGGER.warning("[FIND_BY_ROLE_STATUS] Trạng thái của tài khoản trống");
            return null;
        }

        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_FIND_BY_STATUS)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
                return accounts;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ROLE_STATUS] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean create(Account acc) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_CREATE)) {
            ps.setString(1, acc.getEmployeeId());
            ps.setString(2, acc.getUsername());
            ps.setString(3, acc.getPasswordHash());
            ps.setString(4, acc.getRoleId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePasswordHash(String username, String newHash) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_UPDATE_PASSWORD_HASH)) {
            ps.setString(1, newHash);
            ps.setString(2, username);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_PASSWORD_HASH] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStatus(String username, String status) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_UPDATE_STATUS)) {
            ps.setString(1, status);
            ps.setString(2, username);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_STATUS] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String username) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ACCOUNT_DELETE)) {
            ps.setString(1, username);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) {
        try {
            return new Account(
                    rs.getString("ACCOUNT_ID"),
                    rs.getString("EMPLOYEE_ID"),
                    rs.getString("USERNAME"),
                    rs.getString("PASSWORD_HASH"),
                    rs.getString("ROLE_ID"),
                    rs.getTimestamp("ACCOUNT_CREATION_TIME"),
                    rs.getString("ACCOUNT_STATUS")
            );
        } catch (SQLException e) {
            LOGGER.severe("[MAP_RESULT_SET_TO_ACCOUNT] Lỗi: " + e.getMessage());
            return null;
        }
    }
}
