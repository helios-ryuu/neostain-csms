package com.neostain.csms.controller;

import com.neostain.csms.model.Member;
import com.neostain.csms.service.AuthService;

// Controls member operations and login
public class MemberController {
    private final AuthService authService;
    private Member currentMember;
    private String currentToken;

    public MemberController() {
        this.authService = new AuthService();
    }

    // Try to log in a member
    public boolean login(String username, String password) {
        // Check inputs
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
                this.currentMember = new Member(username, password, 
                    this.authService.getUserRole(username));
                System.out.println("Login success: " + username);
            }
            return authenticated;
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    // Get current token
    public String getCurrentToken() {
        return this.currentToken;
    }

    // Get current member
    public Member getCurrentMember() {
        return this.currentMember;
    }

    // Log out current member
    public void logout() {
        if (this.currentMember != null) {
            this.authService.logout(this.currentMember.getUsername());
            this.currentMember = null;
            this.currentToken = null;
            System.out.println("Logged out successfully");
        }
    }

    // Check if member has role
    public boolean checkAccess(String role) {
        if (this.currentMember == null) {
            System.out.println("Error: No member logged in");
            return false;
        }
        if (role == null || role.trim().isEmpty()) {
            System.out.println("Error: Role is empty");
            return false;
        }
        return this.authService.isAuthorized(this.currentMember.getUsername(), role);
    }
}