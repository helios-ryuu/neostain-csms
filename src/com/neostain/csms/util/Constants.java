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
        public static final int DEFAULT_SIZE = 12;
        public static final String DEFAULT_FONT_NAME = "Segoe UI";
    }

    public static class View {
        // Application name
        public static final String APP_NAME = "Convenience Store Management System";
        public static final String APP_VERSION = "1.0.0";
        // Standard sizes
        public static final Dimension STANDARD_BUTTON_SIZE = new Dimension(120, 30);
        public static final Dimension STANDARD_FIELD_SIZE = new Dimension(200, 20);
        public static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(1200, 600);
        public static final Dimension LOGIN_WINDOW_SIZE = new Dimension(550, 260);
        // Standard insets
        public static final Insets STANDARD_INSETS = new Insets(5, 5, 5, 5);
        public static final Insets DIALOG_INSETS = new Insets(10, 10, 10, 10);
        public static final Insets CONTENT_INSETS = new Insets(15, 15, 15, 15);
        // Text constants
        public static final int FIELD_COLUMNS = 30;
        public static final int PASSWORD_FIELD_COLUMNS = 30;
        public static final int TEXT_AREA_ROWS = 5;
        public static final int TEXT_AREA_COLUMNS = 30;
        // Border constants
        public static final int BORDER_THICKNESS = 1;
        public static final int BORDER_RADIUS = 5;
        public static final int PANEL_BORDER_PADDING = 10;
        // Font constants
        public static final java.awt.Font TITLE_FONT = new java.awt.Font("Dialog", java.awt.Font.BOLD, 16);
        public static final java.awt.Font LABEL_FONT = new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);
        public static final java.awt.Font BUTTON_FONT = new java.awt.Font("Dialog", java.awt.Font.BOLD, 12);
        public static final java.awt.Font DEFAULT_FONT = new java.awt.Font(DEFAULT_FONT_NAME, java.awt.Font.PLAIN, DEFAULT_SIZE);
        // Dialog constants
        public static final String ERROR_DIALOG_TITLE = "Lỗi";
        public static final String INFO_DIALOG_TITLE = "Thông tin";
        public static final String WARNING_DIALOG_TITLE = "Cảnh báo";
        public static final String SUCCESS_DIALOG_TITLE = "Thành công";
        // Menu constants
        public static final int MENU_ITEM_HEIGHT = 40;
        public static final int MENU_PANEL_WIDTH = 250;
        // Table constants
        public static final int TABLE_ROW_HEIGHT = 25;
        public static final java.awt.Color TABLE_HEADER_BACKGROUND = new java.awt.Color(240, 240, 240);
        public static final java.awt.Color TABLE_ALTERNATE_ROW_COLOR = new java.awt.Color(245, 245, 250);
        // Pagination constants
        public static final int DEFAULT_PAGE_SIZE = 10;
        public static final String PAGINATION_PREV = "« Trước";
        public static final String PAGINATION_NEXT = "Sau »";
        public static final String PAGINATION_FORMAT = "Trang %d / %d";
        // Animation constants
        public static final int ANIMATION_DURATION = 300; // milliseconds
        public static final int TOOLTIP_DISMISS_DELAY = 5000; // milliseconds
    }
}