package com.neostain.csms.util;

/**
 * Enum representing the types of screens in the application
 * Provides standardized identifiers and display names for screens
 */
public enum ScreenType {
    LOGIN("Đăng nhập"),
    POS("Bán hàng"),
    STORE_MANAGER("Quản lý cửa hàng");

    private final String displayName;

    /**
     * Constructor
     *
     * @param displayName Human-readable display name
     */
    ScreenType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the human-readable display name for the screen type
     *
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }
}