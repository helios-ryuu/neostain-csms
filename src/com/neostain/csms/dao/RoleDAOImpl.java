package com.neostain.csms.dao;

import com.neostain.csms.model.Role;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RoleDAOImpl implements RoleDAO {
    private static final Logger LOGGER = Logger.getLogger(RoleDAOImpl.class.getName());
    private final Connection conn;

    public RoleDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Role findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] ID vai trò trống");
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ROLE_FIND_BY_ID)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToRole(rs);
            } else {
                LOGGER.severe("[FIND_BY_ID] Vai trò không tồn tại: " + id);
                return null;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Role> findByName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            LOGGER.warning("[FIND_BY_NAME] Tên vai trò trống");
            return null;
        }

        List<Role> roles = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ROLE_FIND_BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }
            return roles;
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ROLE_FIND_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }
            return roles;
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean create(Role role) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ROLE_CREATE)) {
            ps.setString(1, role.getRoleName());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateName(String id, String name) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ROLE_UPDATE_NAME)) {
            ps.setString(1, name);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_NAME] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ROLE_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        return new Role(
                rs.getString("ROLE_ID"),
                rs.getString("ROLE_NAME")
        );
    }
}
