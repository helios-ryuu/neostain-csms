package com.neostain.csms.service.impl;

import com.neostain.csms.model.Role;
import com.neostain.csms.service.api.RoleService;
import com.neostain.csms.util.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class CSMSRoleService implements RoleService {
    private static final Logger LOGGER = Logger.getLogger(CSMSRoleService.class.getName());

    @Override
    public Role getRole(String roleID) {
        if (roleID == null || roleID.trim().isEmpty()) {
            LOGGER.warning("[ROLE.SERVICE.GET_ROLE] ID vai trò trống");
            return null;
        }

        String query = "SELECT * FROM ROLE r WHERE r.ROLE_ID = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, roleID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String ROLE_ID = rs.getString("ROLE_ID");
                    String ROLE_NAME = rs.getString("ROLE_NAME");

                    return new Role(ROLE_ID, ROLE_NAME);
                } else {
                    LOGGER.severe("[ROLE.SERVICE.GET_ROLE] Vai trò không tồn tại: " + roleID);
                    return null;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("[ROLE.SERVICE.GET_ROLE] Lỗi: " + e.getMessage());
            return null;
        }
    }
}
