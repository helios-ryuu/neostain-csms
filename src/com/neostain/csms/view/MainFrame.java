package com.neostain.csms.view;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.ViewManager;
import com.neostain.csms.util.Constants;
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
        this.shutdownHook();

        LOGGER.info("[INIT] Hoàn tất quá trình khởi tạo cửa sổ ứng dụng");
    }

    /**
     * Sets up a shutdown hook to handle cleanup when the application closes.
     */
    private void shutdownHook() {
        // Add a shutdown hook to update token status when the application closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("INFO: [SHUTDOWN_HOOK] Kích hoạt các sự kiện sau khi tắt ứng dụng");
                String currentToken = ServiceManager.getInstance().getCurrentToken();
                if (!StringUtils.isNullOrEmpty(currentToken)) {
                    ServiceManager.getInstance().getTokenService().updateStatus(
                            currentToken, Constants.TokenStatus.EXPIRED);
                    System.out.println("INFO: [SHUTDOWN_HOOK] Token đang hoạt động hiện tại: " + currentToken);
                    System.out.println("INFO: [SHUTDOWN_HOOK] Tiến hành vô hiệu Token...");
                    ServiceManager.getInstance().getTokenService().updateStatus(
                            currentToken, Constants.TokenStatus.EXPIRED);
                    System.out.println("INFO: [SHUTDOWN_HOOK] Hoàn thành vô hiệu Token.");
                } else {
                    System.out.println("INFO: [SHUTDOWN_HOOK] Không có Token đang hoạt động hiện tại. Không cần vô hiệu Token.");
                }
            } catch (Exception ex) {
                System.out.println("ERROR: [SHUTDOWN_HOOK] Lỗi khi vô hiệu Token: " + ex);
            }
        }));
    }
}
