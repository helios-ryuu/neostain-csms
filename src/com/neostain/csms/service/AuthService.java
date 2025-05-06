package com.neostain.csms.service;

import com.neostain.csms.model.Account;
import com.neostain.csms.model.Role;
import com.neostain.csms.model.Token;

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
     * @return true if authentication is successful, false if failed
     */
    boolean authenticate(String username, String password);

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
}
