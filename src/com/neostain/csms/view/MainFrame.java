package com.neostain.csms.view;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.ViewManager;
import com.neostain.csms.model.Employee;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.FontUtils;
import com.neostain.csms.util.ScreenType;
import com.neostain.csms.util.StringUtils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;
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
        // Set the window title
        super("NeoStain Convenience Store Management System");

        // Set UI scale
        System.setProperty("sun.java2d.uiScale", "1.0");
        System.setProperty("sun.java2d.dpiaware", "true");

        // Set application font
        FontUtils.setFont("Segoe UI");

        initDefaultDialogFont();

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

    // Đặt font mặc định cho tất cả các dialog của JOptionPane
    private static void initDefaultDialogFont() {
        FontUIResource fontResource = new FontUIResource(Constants.View.DEFAULT_FONT);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (key.toString().contains("OptionPane.messageFont")
                    || key.toString().contains("OptionPane.buttonFont")
                    || key.toString().contains("OptionPane.font")) {
                UIManager.put(key, fontResource);
            }
        }
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
                String currentUsername = ServiceManager.getInstance().getCurrentUsername();
                if (!StringUtils.isNullOrEmpty(currentToken)) {
                    System.out.println("INFO: [SHUTDOWN_HOOK] Token đang hoạt động hiện tại: " + currentToken);
                    System.out.println("INFO: [SHUTDOWN_HOOK] Tiến hành vô hiệu Token...");
                    ServiceManager.getInstance().getAuthService().invalidateToken(currentToken);
                    System.out.println("INFO: [SHUTDOWN_HOOK] Hoàn thành vô hiệu Token.");
                } else {
                    System.out.println("INFO: [SHUTDOWN_HOOK] Không có Token đang hoạt động hiện tại. Không cần vô hiệu Token.");
                }
                if (currentUsername != null) {
                    if (ServiceManager.getInstance().getAuthService().getAccountByUsername(ServiceManager.getInstance().getCurrentUsername()).getStatus().equals("ĐANG HOẠT ĐỘNG")) {
                        System.out.println("INFO: [SHUTDOWN_HOOK] Tài khoản đang hoạt động hiện tại: " + ServiceManager.getInstance().getCurrentUsername());
                        System.out.println("INFO: [SHUTDOWN_HOOK] Tiến hành ngừng hoạt động tài khoản...");
                        ServiceManager.getInstance().getAuthService().updateAccountStatus(ServiceManager.getInstance().getCurrentUsername(), "NGỪNG HOẠT ĐỘNG");
                        System.out.println("INFO: [SHUTDOWN_HOOK] Hoàn thành ngừng hoạt động tài khoản.");
                    }
                    if (ServiceManager.getInstance().getAuthService().getAccountByUsername(ServiceManager.getInstance().getCurrentUsername()).getStatus().equals("ĐANG HOẠT ĐỘNG")) {
                        System.out.println("INFO: [SHUTDOWN_HOOK] nhân viên đang hoạt động hiện tại: " + ServiceManager.getInstance().getAuthService().getAccountByUsername(ServiceManager.getInstance().getCurrentUsername()).getId());
                        System.out.println("INFO: [SHUTDOWN_HOOK] Tiến hành tạm ngừng hoạt động nhân viên...");
                        Employee employee = ServiceManager.getInstance().getManagementService().getEmployeeById(ServiceManager.getInstance().getAuthService().getAccountByUsername(ServiceManager.getInstance().getCurrentUsername()).getId());
                        employee.setStatus("TẠM NGỪNG HOẠT ĐỘNG");
                        ServiceManager.getInstance().getManagementService().updateEmployee(employee);
                        System.out.println("INFO: [SHUTDOWN_HOOK] Hoàn thành tạm ngừng hoạt động nhân viên.");
                    }
                } else {
                    System.out.println("INFO: [SHUTDOWN_HOOK] Không có nhân viên đang hoạt động hiện tại. Không cần tạm ngừng.");
                }
            } catch (Exception ex) {
                System.out.println("ERROR: [SHUTDOWN_HOOK] Lỗi khi vô hiệu Token và tạm ngưng hoạt động nhân viên: " + ex);
            }
        }));
    }
}
