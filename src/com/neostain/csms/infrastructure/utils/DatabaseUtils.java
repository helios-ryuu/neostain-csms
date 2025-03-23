package com.neostain.csms.infrastructure.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple utility for database connections
 */
public class DatabaseUtils {
    private static final Logger LOGGER = Logger.getLogger(DatabaseUtils.class.getName());
    
    /**
     * Get a database connection
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/NEOSTAIN_CSMS", "CSMS_ADMIN", "12345678");
            // Dành cho các thành viên sử dụng Radmin VPN
            // return DriverManager.getConnection("jdbc:oracle:thin:@26.141.93.197:1521/NEOSTAIN_CSMS", "CSMS_ADMIN", "12345678");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot connect to database", e);
            throw e;
        }
    }
}