package com.neostain.csms.service.impl;

import com.neostain.csms.model.Account;
import com.neostain.csms.service.api.AccountService;
import com.neostain.csms.util.DatabaseUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class CSMSAccountService implements AccountService {
    private static final Logger LOGGER = Logger.getLogger(CSMSAccountService.class.getName());

    @Override
    public Account getAccount(String accountID) {
        if (accountID == null || accountID.trim().isEmpty()) {
            LOGGER.warning("[ACCOUNT.SERVICE.GET_ACCOUNT] ID tài khoản trống");
            return null;
        }

        String query = "SELECT * FROM ACCOUNT a WHERE a.ACCOUNT_ID = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String ACCOUNT_ID = rs.getString("ACCOUNT_ID");
                    String USERNAME = rs.getString("USERNAME");
                    String PASSWORD_HASH = rs.getString("PASSWORD_HASH");
                    String ROLE_ID = rs.getString("ROLE_ID");
                    Date ACCOUNT_CREATION_DATE = rs.getDate("ACCOUNT_CREATION_TIME");

                    return new Account(ACCOUNT_ID, USERNAME, PASSWORD_HASH, ROLE_ID, ACCOUNT_CREATION_DATE);
                } else {
                    LOGGER.severe("[ACCOUNT.SERVICE.GET_ACCOUNT] Tài khoản không tồn tại: " + accountID);
                    return null;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("[ACCOUNT.SERVICE.GET_ACCOUNT] Lỗi: " + e.getMessage());
            return null;
        }
    }
}
