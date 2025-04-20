package com.neostain.csms.service;

import com.neostain.csms.dao.AccountDAO;
import com.neostain.csms.model.Account;
import com.neostain.csms.util.PasswordUtils;
import com.neostain.csms.util.StringUtils;

import java.util.logging.Logger;

public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = Logger.getLogger(AccountServiceImpl.class.getName());
    private final AccountDAO dao;

    public AccountServiceImpl(AccountDAO dao) {
        this.dao = dao;
    }

    @Override
    public Account getByUsername(String username) {
        if (StringUtils.isNullOrEmpty(username)) {
            LOGGER.warning("[GET_BY_USERNAME] Username tài khoản trống");
            return null;
        }

        try {
            return dao.findByUsername(username);
        } catch (Exception e) {
            LOGGER.warning("[GET_BY_USERNAME] Lỗi: " + e.getMessage());
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
            dao.create(acc);
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
            Account acc = dao.findByUsername(username);
            if (acc == null) {
                LOGGER.warning("[CHANGE_PWD] Account không tồn tại: " + username);
                return false;
            }
            if (PasswordUtils.verify(newPlainPassword, acc.getPasswordHash())) {
                LOGGER.warning("[CHANGE_PWD] New password trùng old password");
                return false;
            }
            String hashed = PasswordUtils.hash(newPlainPassword);
            return dao.updatePassword(username, hashed);
        } catch (Exception e) {
            LOGGER.severe("[CHANGE_PWD] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean remove(String username) {
        if (StringUtils.isNullOrEmpty(username)) {
            LOGGER.warning("[REMOVE] Username tài khoản trống");
            return false;
        }
        try {
            Account acc = dao.findByUsername(username);
            if (acc == null) {
                LOGGER.warning("[REMOVE] Account không tồn tại: " + username);
                return false;
            }
            if ("ADMIN".equalsIgnoreCase(acc.getRoleID())) {
                LOGGER.warning("[REMOVE] Không được xóa account ADMIN");
                return false;
            }
            dao.delete(username);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[REMOVE] Lỗi: " + e.getMessage());
            return false;
        }
    }
}
