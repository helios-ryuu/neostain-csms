package com.neostain.csms.service;

import com.neostain.csms.util.DatabaseUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// Basic authentication service
public class AuthService {
    
    // Check if user credentials are valid
    public boolean authenticate(String username, String password) {
        // Check empty inputs
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Error: Username is empty");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Error: Password is empty");
            return false;
        }

        try {
            // Connect to database
            Connection connection = DatabaseUtils.getConnection();
            
            // Step 3: Prepare SQL query to find the user
            String sql = "SELECT PASSWORD_HASH FROM ACCOUNT WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            
            ResultSet results = statement.executeQuery();
            
            // Check password match
            if (results.next()) {
                // Get the stored password hash from database
                String storedHash = results.getString("PASSWORD_HASH");
                
                // Create hash of the input password
                String inputHash = hash(password);
                
                // Compare the two hashes
                boolean isMatch = storedHash.equals(inputHash);
                
                // Print result message
                if (isMatch) {
                    System.out.println("Login successful for user: " + username);
                } else {
                    System.out.println("Login failed: incorrect password");
                }
                
                return isMatch;
            } else {
                System.out.println("User not found: " + username);
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    // Log out a user
    public void logout(String username) {
        if (username != null) {
            System.out.println("Logged out: " + username);
        }
    }

    // Get user's role
    public String getUserRole(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Error: Username is empty");
            return null;
        }

        try {
            Connection connection = DatabaseUtils.getConnection();
            
            // Step 3: Prepare SQL query to find user's role
            String sql = "SELECT r.ROLE_NAME FROM ACCOUNT a JOIN ROLE r ON a.ROLE_ID = r.ROLE_ID WHERE a.username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            
            ResultSet results = statement.executeQuery();
            
            if (results.next()) {
                String role = results.getString("ROLE_NAME");
                System.out.println("Found role for user " + username + ": " + role);
                return role;
            } else {
                System.out.println("No role found for user: " + username);
                return null;
            }
            
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    // Check if user has the required role
    public boolean isAuthorized(String username, String role) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Error: Username is empty");
            return false;
        }

        try {
            // Step 2: Connect to database
            Connection connection = DatabaseUtils.getConnection();
            
            // Step 3: Prepare SQL query to find user's role
            String sql = "SELECT r.ROLE_NAME FROM ACCOUNT a JOIN ROLE r ON a.ROLE_ID = r.ROLE_ID WHERE a.username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            
            // Step 4: Execute query
            ResultSet results = statement.executeQuery();
            
            // Step 5: Check if user has the role
            if (results.next()) {
                // Compare the user's role with the required role
                boolean hasRole = role.equals(results.getString("ROLE_NAME"));
                System.out.println("User " + username + " authorization check: " + hasRole);
                return hasRole;
            } else {
                System.out.println("User " + username + " not found during authorization check");
                return false;
            }
            
        } catch (Exception e) {
            // Print error message if something goes wrong
            System.out.println("Error checking authorization: " + e.getMessage());
            return false;
        }
    }
    
    // Create a simple login token
    public String generateToken(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Error: Username is empty");
            return null;
        }
        
        try {
            // Step 2: Connect to database
            Connection connection = DatabaseUtils.getConnection();
            
            // Step 3: Get account ID for the user
            String sql = "SELECT ACCOUNT_ID FROM ACCOUNT WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            
            if (results.next()) {
                // Step 4: Get the account ID
                String accountId = results.getString("ACCOUNT_ID");
                
                // Step 5: Create token value using current time
                String tokenValue = hash(String.valueOf(System.currentTimeMillis() / 1000));
                
                // Step 6: Set token to expire in 5 minutes
                LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
                
                // Step 7: Save token in database
                String insertSql = "INSERT INTO TOKEN (ACCOUNT_ID, TOKEN_VALUE, EXPIRES_AT, TOKEN_STATUS_ID) VALUES (?, ?, ?, '01')";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setString(1, accountId);
                insertStatement.setString(2, tokenValue);
                insertStatement.setTimestamp(3, Timestamp.valueOf(expiresAt));
                insertStatement.executeUpdate();
                
                System.out.println("Generated new token for user: " + username);
                return tokenValue;
            } else {
                System.out.println("Cannot generate token: user not found");
                return null;
            }
            
        } catch (Exception e) {
            // Print error message if something goes wrong
            System.out.println("Error generating token: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Hashes input using SHA-256.
     * 
     * @param input string to hash
     * @return hashed string
     */
    private String hash(String input) {
        try {
            // Step 1: Create SHA-256 hash generator
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            
            // Step 2: Convert input string to bytes and generate hash
            byte[] hashBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            // Step 3: Convert bytes to readable hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                // Convert each byte to a 2-digit hex number
                hexString.append(String.format("%02x", b & 0xff));
            }
            
            return hexString.toString();
            
        } catch (Exception e) {
            // Print error message if something goes wrong
            System.out.println("Error creating hash: " + e.getMessage());
            throw new RuntimeException("Security error");
        }
    }
}