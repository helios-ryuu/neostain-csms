package com.neostain.csms.dao;

import com.neostain.csms.model.Role;

import java.util.List;

public interface RoleDAO {
    Role findById(String id);

    List<Role> findByName(String name);

    List<Role> findAll();

    boolean create(Role role);

    boolean updateName(String id, String name);

    boolean delete(String id);
}
