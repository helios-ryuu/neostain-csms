package com.neostain.csms.service;

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
}
