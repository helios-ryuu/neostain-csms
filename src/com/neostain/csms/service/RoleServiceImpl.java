package com.neostain.csms.service;

import com.neostain.csms.dao.RoleDAO;
import com.neostain.csms.model.Role;
import com.neostain.csms.util.StringUtils;

import java.sql.SQLException;
import java.util.logging.Logger;

public class RoleServiceImpl implements RoleService {
    private static final Logger LOGGER = Logger.getLogger(RoleServiceImpl.class.getName());
    private final RoleDAO dao;

    public RoleServiceImpl(RoleDAO dao) {
        this.dao = dao;
    }

    @Override
    public Role getRole(String roleID) {
        if (StringUtils.isNullOrEmpty(roleID)) {
            LOGGER.warning("[ROLE.SERVICE.GET_ROLE] ID vai trò trống");
            return null;
        }

        try {
            return dao.findById(roleID);
        } catch (SQLException e) {
            LOGGER.severe("[ROLE.SERVICE.GET_ROLE] Lỗi: " + e.getMessage());
            return null;
        }
    }
}
