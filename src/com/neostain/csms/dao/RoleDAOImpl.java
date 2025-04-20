package com.neostain.csms.dao;

import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class RoleDAOImpl implements RoleDAO {
    private static final Logger LOGGER = Logger.getLogger(RoleDAOImpl.class.getName());
    private final Connection conn;

    public RoleDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Role findById(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ROLE_FIND_BY_ID)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String ROLE_ID = rs.getString("ROLE_ID");
                String ROLE_NAME = rs.getString("ROLE_NAME");
                return new Role(
                        ROLE_ID,
                        ROLE_NAME
                );
            } else {
                LOGGER.severe("[FIND_BY_ID] Vai trò không tồn tại: " + id);
                return null;
            }
        }
    }

    @Override
    public int create(Role role) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ROLE_CREATE)) {
            ps.setString(1, role.getRoleId());
            ps.setString(2, role.getRoleName());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ROLE_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        }
    }
}
