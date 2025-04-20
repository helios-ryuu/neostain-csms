package com.neostain.csms;

import com.neostain.csms.util.ScreenType;
import com.neostain.csms.view.MainFrame;
import com.neostain.csms.view.screen.LoginScreen;
import com.neostain.csms.view.screen.cs.CashierStaffScreen;
import com.neostain.csms.view.screen.sm.StoreManagerScreen;

import javax.naming.NoPermissionException;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class ViewManager {
    private final static Logger LOGGER = Logger.getLogger(ViewManager.class.getName());
    private static ViewManager instance;
    private final MainFrame mainFrame;

    // Tham chiếu đến ServiceManager để truy cập thông tin token
    private final ServiceManager serviceManager;
    private JPanel currentScreen;

    private ViewManager(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.serviceManager = ServiceManager.getInstance();
    }

    public static ViewManager getInstance(MainFrame mainFrame) {
        if (instance == null) {
            instance = new ViewManager(mainFrame);
        }
        return instance;
    }

    /**
     * Switches to the specified screen type with user context
     *
     * @param screenType Type of screen to switch to
     * @param username   Username for context (can be null for LOGIN screen)
     */
    public void switchScreen(ScreenType screenType, String username) {
        // Kiểm tra quyền truy cập màn hình
        if (screenType != ScreenType.LOGIN && !serviceManager.isLoggedIn()) {
            LOGGER.warning("[AUTHENTICATION_ERROR] Attempting to access " + screenType + " without valid token");
            DialogFactory.showConfirmDialog(mainFrame, "Authentication Error", "Vui lòng đăng nhập để tiếp tục");
            this.switchScreen(ScreenType.LOGIN, username);
            return;
        }

        try {
            mainFrame.getContentPane().removeAll();
            JPanel newScreen = this.createScreen(screenType, username);
            this.updateFrameProperties(screenType);
            this.setScreen(newScreen);

            LOGGER.info("[SWITCH_SCREEN] Chuyển sang màn hình: " + screenType);
        } catch (NoPermissionException e) {
            LOGGER.severe("[PERMISSION_ERROR] Insufficient permissions to access " + screenType);
            DialogFactory.showErrorDialog(mainFrame, "Permission Error", "Không đủ quyền hạn để truy cập giao diện chỉ định");
        } catch (Exception e) {
            LOGGER.severe("[SCREEN_ERROR] Error switching screen: " + e.getMessage());
            DialogFactory.showErrorDialog(mainFrame, "System Error", "Lỗi khi chuyển đổi giao diện: " + e.getMessage());
        }
    }

    private JPanel createScreen(ScreenType screenType, String username) throws NoPermissionException {
        return switch (screenType) {
            case LOGIN -> new LoginScreen();
            case POS -> new CashierStaffScreen(username);
            case STORE_MANAGER -> new StoreManagerScreen(username);
        };
    }

    private void updateFrameProperties(ScreenType screenType) {
        String username = serviceManager.getCurrentUsername();

        switch (screenType) {
            case LOGIN -> {
                // Then set the other properties
                mainFrame.setMinimumSize(new Dimension(50, 50));
                mainFrame.pack();
                // First, reset the extended state to ensure we're not in maximized mode
                mainFrame.setExtendedState(JFrame.NORMAL);
                mainFrame.setResizable(false);
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setTitle(Constants.View.APP_NAME + " - " + screenType.getDisplayName());
            }
            case POS, STORE_MANAGER -> {
                mainFrame.setMinimumSize(Constants.View.MINIMUM_WINDOW_SIZE);
                mainFrame.setResizable(true);
                mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                mainFrame.setTitle(Constants.View.APP_NAME + " - " + screenType.getDisplayName() +
                        (username != null ? " (" + username + ")" : ""));
            }
        }
    }

    private void setScreen(JPanel newScreen) {
        mainFrame.setContentPane(newScreen);
        currentScreen = newScreen;

        // Add additional validation to ensure UI is properly refreshed
        mainFrame.invalidate();
        mainFrame.validate();
        mainFrame.repaint();

        // Force the window manager to recognize the size changes
        if (newScreen instanceof LoginScreen) {
            // Ensure window size is applied properly for login screen
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null);
        }
    }

    /**
     * Gets the current logged-in username from a token
     *
     * @return Username or null if not logged in
     */
    public String getCurrentUser() {
        return serviceManager.getCurrentUsername();
    }

    public JPanel getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Logs the user out and redirects to log in screen
     */
    public void logout() {
        serviceManager.logout();

        // Sử dụng invokeLater xếp tầng để đảm bảo thứ tự thực thi
        SwingUtilities.invokeLater(() -> switchScreen(ScreenType.LOGIN, null));
    }
}