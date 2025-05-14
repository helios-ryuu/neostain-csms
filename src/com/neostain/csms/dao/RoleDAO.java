package com.neostain.csms.dao;

import com.neostain.csms.model.Role;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.util.List;

public interface RoleDAO {
    Role findById(String id);

    List<Role> findByName(String name);

    List<Role> findAll();

    boolean create(Role role) throws DuplicateFieldException;

    boolean updateName(String id, String name) throws DuplicateFieldException;

    boolean delete(String id);
}
