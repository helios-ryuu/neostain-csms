package com.neostain.csms.dao;

import com.neostain.csms.model.Account;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.util.List;

public interface AccountDAO {
    List<Account> findByEmployeeId(String employeeId);

    Account findById(String id);

    Account findByUsername(String username);

    List<Account> findByRoleId(String roleId);

    List<Account> findByStatus(String status);

    List<Account> findAll();

    boolean create(Account acc) throws DuplicateFieldException;

    boolean updatePasswordHash(String username, String newHash);

    boolean updateStatus(String username, String status);
}
