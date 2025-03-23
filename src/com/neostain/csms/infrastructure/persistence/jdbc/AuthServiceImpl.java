package com.neostain.csms.infrastructure.persistence.jdbc;

import com.neostain.csms.core.service.AuthService;
import com.neostain.csms.infrastructure.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple implementation of authentication service
 */
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = Logger.getLogger(AuthServiceImpl.class.getName());

    @Override
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String sql = "SELECT PASSWORD_HASH FROM ACCOUNT WHERE username = ?";
        
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("PASSWORD_HASH");
                    String inputHash = hashPassword(password);
                    return storedHash.equals(inputHash);
                }
            }
            return false;
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Login failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void logout(String username) {
        LOGGER.info("User logged out: " + username);
    }

    @Override
    public boolean isAuthorized(String username, String role) {
        if (username == null || role == null) {
            return false;
        }

        String sql = "SELECT r.ROLE_NAME FROM ACCOUNT a JOIN ROLE r ON a.ROLE_ID = r.ROLE_ID WHERE a.username = ?";
        
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return role.equals(rs.getString("ROLE_NAME"));
                }
            }
            return false;
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Authorization check failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String generateToken(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        
        String sql = "SELECT ACCOUNT_ID FROM ACCOUNT WHERE username = ?";
        String insertSql = "INSERT INTO TOKEN (ACCOUNT_ID, TOKEN_VALUE, EXPIRES_AT, TOKEN_STATUS_ID) VALUES (?, ?, ?, '01')";
        
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String accountId = rs.getString("ACCOUNT_ID");
                    
                    // Generate token value as SHA-256 hash of current system time in seconds
                    String tokenValue = hashValue(String.valueOf(System.currentTimeMillis() / 1000));
                    
                    // Set expiration time to 5 minutes from now
                    LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
                    
                    // Insert the token
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, accountId);
                        insertStmt.setString(2, tokenValue);
                        insertStmt.setTimestamp(3, Timestamp.valueOf(expiresAt));
                        insertStmt.executeUpdate();
                    }
                    
                    LOGGER.info("Generated token for user: " + username);
                    return tokenValue;
                }
            }
            
            return null;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Token generation failed", e);
            return null;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b & 0xff));
            }
            
            return hex.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Password hashing failed", e);
            throw new RuntimeException("Security error");
        }
    }
    
    private String hashValue(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b & 0xff));
            }
            
            return hex.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Hashing failed", e);
            throw new RuntimeException("Security error");
        }
    }
}