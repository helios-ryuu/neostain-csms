package com.neostain.csms.view.screen.sm;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Role;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.component.ScreenHeader;
import com.neostain.csms.view.component.StandardTabbedPane;
import com.neostain.csms.view.screen.sm.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Màn hình của nhân viên quản lý cửa hàng
 */
public class StoreManagerScreen extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(StoreManagerScreen.class.getName());
    private static final ServiceManager serviceManager = ServiceManager.getInstance();

    private final Account account;
    private final Employee employee;
    private final Role role;

    public StoreManagerScreen(String username) {
        // Load user data
        this.account = serviceManager.getAuthService().getAccountByUsername(username);
        this.employee = serviceManager.getManagementService().getEmployeeById(account.getEmployeeId());
        this.role = serviceManager.getAuthService().getRoleById(this.account.getRoleId());

        // Call to set up the UI components
        initializeComponents();
        LOGGER.info("[INIT] Màn hình Store Manager được khởi tạo cho người dùng: " + username);
    }

    private void initializeComponents() {
        try {
            // Set the layout for this panels first
            this.setLayout(new BorderLayout());
            this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

            // Create a header toolbar with user info and logout
            JPanel headerPanel = new ScreenHeader(
                    this.employee.getName(),
                    this.role.getName()
            );
            this.add(headerPanel, BorderLayout.NORTH);

            // Create tabbed pane for the main content
            JTabbedPane mainManagerPane = new StandardTabbedPane();

            // Initialize tabs
            JPanel dashboardTabPanel = new DashboardPanel();
            JPanel invoiceTabPanel = new InvoicePanel();
            JPanel employeeTabPanel = new EmployeePanel();
            JPanel workShiftTabPanel = new WorkShiftPanel();
            JPanel memberTabPanel = new MemberPanel();

            // Add tabs to the mainManagerPane
            mainManagerPane.addTab("Dashboard", dashboardTabPanel);
            mainManagerPane.addTab("Quản lý hóa đơn", invoiceTabPanel);
            mainManagerPane.addTab("Quản lý thành viên", memberTabPanel);
            mainManagerPane.addTab("Quản lý nhân viên", employeeTabPanel);
            mainManagerPane.addTab("Phân công", workShiftTabPanel);

            // Add mainManagerPane to StoreManagerScreen
            this.add(mainManagerPane, BorderLayout.CENTER);

        } catch (Exception e) {
            LOGGER.severe("Error initializing StoreManagerScreen: " + e.getMessage());
            DialogFactory.showErrorDialog(
                    this,
                    "Initialization Error",
                    "Error initializing Store Manager Screen: " + e.getMessage()
            );
        }
    }
}