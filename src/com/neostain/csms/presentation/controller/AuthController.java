package com.neostain.csms.presentation.controller;

import com.neostain.csms.core.service.AuthService;
import com.neostain.csms.infrastructure.persistence.jdbc.AuthServiceImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for authentication-related operations.
 */
public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    private final AuthService authService;
    private String currentToken;

    /**
     * Creates a new AuthController with the provided auth service.
     */
    public AuthController() {
        this.authService = new AuthServiceImpl();
    }

    /**
     * Attempts to log in a user with the provided credentials.
     *
     * @param username the username
     * @param password the password
     * @return true if login succeeds, false otherwise
     * @throws IllegalArgumentException if username or password is empty
     * @throws RuntimeException if a system error occurs
     */
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        try {
            boolean authenticated = this.authService.authenticate(username, password);
            if (authenticated) {
                this.currentToken = this.authService.generateToken(username);
                LOGGER.info("Token generated for user: " + username);
            }
            return authenticated;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Login error", e);
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    /**
     * Gets the current authentication token.
     *
     * @return the current token or null if not authenticated
     */
    public String getCurrentToken() {
        return this.currentToken;
    }

    /**
     * Logs out a user.
     *
     * @param username the username to log out
     * @throws IllegalArgumentException if username is empty
     */
    public void logout(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.authService.logout(username);
        }
    }

    /**
     * Checks if a user has the specified role.
     *
     * @param username the username to check
     * @param role the role to verify
     * @return true if the user has the specified role, false otherwise
     * @throws IllegalArgumentException if username or role is empty
     */
    public boolean checkAccess(String username, String role) {
        if (username == null || username.trim().isEmpty() || 
            role == null || role.trim().isEmpty()) {
            return false;
        }
        return this.authService.isAuthorized(username, role);
    }
}