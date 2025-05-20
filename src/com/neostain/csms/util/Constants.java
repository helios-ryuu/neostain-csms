package com.neostain.csms.util;

import java.awt.*;

import static com.neostain.csms.util.Constants.Font.DEFAULT_FONT_NAME;
import static com.neostain.csms.util.Constants.Font.DEFAULT_SIZE;

/**
 * Constants used throughout the application.
 * Centralizes all hardcoded values to make them easier to manage and update.
 */
public class Constants {
    /**
     * Database configuration constants
     * Note: In a production environment, these should be moved to a secure configuration file
     */
    public static class Database {
        public static final String URL = "jdbc:oracle:thin:@localhost:1521/NEOSTAIN_CSMS";
        public static final String USER = "CSMS_ADMIN";
        public static final String PASSWORD = "12345678";
        public static final String DRIVER = "oracle.jdbc.OracleDriver";
    }

    public static class Color {
        public final static java.awt.Color COMPONENT_BACKGROUND_WHITE = new java.awt.Color(249, 249, 249);
        public final static java.awt.Color COMPONENT_BACKGROUND_SELECTED = new java.awt.Color(220, 220, 220);
        public final static java.awt.Color COMPONENT_BACKGROUND_HEADER = new java.awt.Color(45, 45, 45);
    }

    public static class Font {
        public static final int DEFAULT_SIZE = 14;
        public static final String DEFAULT_FONT_NAME = "Segoe UI";
    }

    public static class View {
        // Application name
        public static final String APP_NAME = "Convenience Store Management System";
        public static final String APP_VERSION = "1.0.0";
        // Standard sizes
        public static final Dimension STANDARD_BUTTON_SIZE = new Dimension(120, 30);
        public static final Dimension STANDARD_FIELD_SIZE = new Dimension(200, 20);
        public static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(1000, 500);
        public static final Dimension LOGIN_WINDOW_SIZE = new Dimension(550, 260);
        // Standard insets
        public static final Insets STANDARD_INSETS = new Insets(5, 5, 5, 5);
        public static final Insets DIALOG_INSETS = new Insets(10, 10, 10, 10);
        public static final Insets CONTENT_INSETS = new Insets(15, 15, 15, 15);
        // Font constants
        public static final java.awt.Font DEFAULT_FONT = new java.awt.Font(DEFAULT_FONT_NAME, java.awt.Font.PLAIN, DEFAULT_SIZE);
        // Menu constants
        public static final int MENU_ITEM_HEIGHT = 40;
        public static final int MENU_PANEL_WIDTH = 250;
        // Animation constants
        public static final int ANIMATION_DURATION = 300; // milliseconds
        public static final int TOOLTIP_DISMISS_DELAY = 5000; // milliseconds
    }
}