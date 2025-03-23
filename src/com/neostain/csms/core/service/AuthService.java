package com.neostain.csms.core.service;

/**
 * Service interface for authentication and authorization operations.
 */
public interface AuthService {
    /**
     * Authenticates a user with the provided credentials.
     *
     * @param username the username
     * @param password the password (will be hashed and compared)
     * @return true if authentication succeeds, false otherwise
     */
    boolean authenticate(String username, String password);

    /**
     * Logs out a user from the system.
     *
     * @param username the username to log out
     */
    void logout(String username);

    /**
     * Checks if a user has the specified role.
     *
     * @param username the username to check
     * @param role the role to verify
     * @return true if the user has the specified role, false otherwise
     */
    boolean isAuthorized(String username, String role);
    
    /**
     * Generates a token for the specified user.
     *
     * @param username the username for which to generate a token
     * @return the generated token or null if token generation fails
     */
    String generateToken(String username);
}