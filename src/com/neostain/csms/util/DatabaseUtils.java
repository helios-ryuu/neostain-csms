package com.neostain.csms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Lớp hỗ trợ kết nối cơ sở dữ liệu
 */
public class DatabaseUtils {
    // Thông tin kết nối
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/NEOSTAIN_CSMS";
    private static final String DB_USER = "CSMS_ADMIN";
    private static final String DB_PASSWORD = "12345678";

    /**
     * Lấy kết nối đến cơ sở dữ liệu
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the Oracle database driver
            Class.forName("oracle.jdbc.OracleDriver");

            // Try to connect to the database
            System.out.println("[" + LocalDateTime.now() + "] [DatabaseUtils] Initiating database connection to " + DB_URL);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("[" + LocalDateTime.now() + "] [DatabaseUtils] Database connection established successfully to " + DB_URL);
            return conn;

        } catch (ClassNotFoundException e) {
            // This happens if we can't find the database driver
            System.out.println("[" + LocalDateTime.now() + "] [DatabaseUtils] ERROR: Oracle database driver not found in classpath");
            System.out.println("[" + LocalDateTime.now() + "] [DatabaseUtils] Please ensure Oracle JDBC driver is included in project dependencies");
            throw new SQLException("Database driver not found: " + e.getMessage());

        } catch (SQLException e) {
            // This happens if we can't connect to the database
            System.out.println("[" + LocalDateTime.now() + "] [DatabaseUtils] ERROR: Failed to establish database connection to " + DB_URL);
            System.out.println("[" + LocalDateTime.now() + "] [DatabaseUtils] Please verify database is running and connection parameters are correct");
            throw new SQLException("Database connection failed: " + e.getMessage());
        }
    }
}