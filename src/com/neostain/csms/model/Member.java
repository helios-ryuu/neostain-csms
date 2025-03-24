package com.neostain.csms.model;

// A simple class to store member information
public class Member {
    private String username;
    private String password;
    private String role;
    private String email;
    private String phoneNumber;
    private boolean active;

    /**
     * Default constructor
     */
    public Member() {}

    // Main constructor
    public Member(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = true;
    }

    // Basic getters and setters with simple validation
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.username = username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        this.role = role.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Check if member has all required fields
    public boolean isValid() {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Error: Username is required");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Error: Password is required");
            return false;
        }
        if (role == null || role.trim().isEmpty()) {
            System.out.println("Error: Role is required");
            return false;
        }
        return true;
    }

    // Simple string representation
    public String toString() {
        return "Member{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", active=" + active +
                '}';
    }
}