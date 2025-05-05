package com.neostain.csms.service;

import com.neostain.csms.dao.RoleDAO;
import com.neostain.csms.model.Role;

import java.util.logging.Logger;

public class RoleServiceImpl implements RoleService {
    private static final Logger LOGGER = Logger.getLogger(RoleServiceImpl.class.getName());
    private final RoleDAO dao;

    public RoleServiceImpl(RoleDAO dao) {
        this.dao = dao;
    }

    @Override
    public Role getRoleById(String roleID) {
        return dao.findById(roleID);
    }
}
