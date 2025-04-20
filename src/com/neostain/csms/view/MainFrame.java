package com.neostain.csms.view;

import com.neostain.csms.Constants;
import com.neostain.csms.ServiceManager;
import com.neostain.csms.ViewManager;
import com.neostain.csms.util.FontUtils;
import com.neostain.csms.util.ScreenType;
import com.neostain.csms.util.StringUtils;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Main application window that manages the display of different screens.
 */
public class MainFrame extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(MainFrame.class.getName());

    /**
     * Initializes the main window with basic settings.
     */
    public MainFrame() {
        // Set window title
        super("NeoStain Convenience Store Management System");

        // Set UI scale
        System.setProperty("sun.java2d.uiScale", "1.0");
        System.setProperty("sun.java2d.dpiaware", "true");

        // Set application font
        FontUtils.setFont("Segoe UI");

        // Initialize screen manager and show login screen
        ViewManager viewManager = ViewManager.getInstance(this);
        viewManager.switchScreen(ScreenType.LOGIN, null);

        // Configure window properties
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Setup application shutdown handler
        this.setupShutdownHook();

        LOGGER.info("Hoàn tất quá trình khởi tạo cửa sổ ứng dụng");
    }

    /**
     * Sets up a shutdown hook to handle cleanup when the application closes.
     */
    private void setupShutdownHook() {
        // Add a shutdown hook to update token status when the application closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                String currentToken = ServiceManager.getInstance().getCurrentToken();
                if (!StringUtils.isNullOrEmpty(currentToken)) {
                    ServiceManager.getInstance().getTokenService().updateStatus(
                            currentToken, Constants.TokenStatus.EXPIRED);
                    System.out.println("Updated token status to EXPIRED on application shutdown.");
                } else {
                    System.out.println("No active token found. No token invalidation needed.");
                }
            } catch (Exception ex) {
                System.out.println("Error updating token status on shutdown: " + ex);
            }
        }));
    }
}
