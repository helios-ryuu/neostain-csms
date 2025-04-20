package com.neostain.csms.util;

import com.neostain.csms.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class providing methods to connect and interact with the database.
 */
public class DatabaseUtils {
    private static final Logger LOGGER = Logger.getLogger(DatabaseUtils.class.getName());

    /**
     * Creates and returns a connection to the database.
     *
     * @return Initialized Connection object
     * @throws SQLException If unable to connect to the database
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Register Oracle JDBC driver
            Class.forName(Constants.Database.DRIVER);
            // Create a new connection to the database
            return DriverManager.getConnection(
                    Constants.Database.URL,
                    Constants.Database.USER,
                    Constants.Database.PASSWORD);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Database driver not found", e);
            throw new SQLException("Database driver not found: " + e.getMessage());
        }
    }

    /**
     * Safely closes a database connection.
     *
     * @param connection The connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }
}
