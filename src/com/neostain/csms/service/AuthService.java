package com.neostain.csms.service;

import com.neostain.csms.model.Account;
import com.neostain.csms.model.Role;
import com.neostain.csms.model.Token;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.util.List;

/**
 * Interface defining authentication and authorization functions.
 * Designed to separate authentication and authorization tasks for easier extension.
 */
public interface AuthService {
    /**
     * Authenticates a user with username and password.
     *
     * @param username Username
     * @param password Password
     * @param storeId  Store ID to authenticate for, or null if not applicable (e.g. for employee login)
     * @return true if authentication is successful, false if failed
     */
    boolean authenticate(String username, String password, String storeId);

    /**
     * Checks if a user has a specific role.
     *
     * @param username Username
     * @param role     Role name to check
     * @return true if user has the role, false otherwise
     */
    boolean isAuthorized(String username, String role);

    Token getTokenByValue(String tokenValue);

    String generateToken(String username);

    boolean validate(String tokenValue);

    void invalidateToken(String username);

    Account getAccountByUsername(String username);

    boolean register(Account acc);

    boolean changePassword(String username, String newHash);

    boolean removeByUsername(String username);

    Role getRoleById(String roleID);

    // Account management
    boolean createAccount(Account account) throws DuplicateFieldException;

    boolean updateAccountPassword(String username, String newPasswordHash);

    boolean updateAccountStatus(String username, String status);

    boolean deleteAccount(String username);

    Account getAccountById(String id);

    List<Account> getAccountsByRoleId(String roleId);

    List<Account> getAccountsByStatus(String status);

    List<Account> getAllAccounts();

    // Role management
    boolean createRole(Role role) throws DuplicateFieldException;

    boolean updateRoleName(String id, String name) throws DuplicateFieldException;

    boolean deleteRole(String id);

    List<Role> getRolesByName(String name);

    List<Role> getAllRoles();
}
