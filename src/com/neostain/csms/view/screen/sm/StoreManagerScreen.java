package com.neostain.csms.view.screen.sm;

import com.neostain.csms.Constants;
import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Role;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.view.component.ScreenHeader;
import com.neostain.csms.view.component.StandardTabbedPane;
import com.neostain.csms.view.screen.sm.dashboard.DashboardPanel;
import com.neostain.csms.view.screen.sm.employee.EmployeePanel;
import com.neostain.csms.view.screen.sm.invoice.InvoicePanel;
import com.neostain.csms.view.screen.sm.settings.SettingsPanel;
import com.neostain.csms.view.screen.sm.workshift.WorkShiftPanel;

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

        if (StringUtils.isNullOrEmpty(username)) {
            throw new IllegalArgumentException("Username không thể rỗng hoặc null");
        }

        // Load user data
        this.account = serviceManager.getAccountService().getByUsername(username);
        this.employee = serviceManager.getEmployeeService().getById(account.getEmployeeID());
        this.role = serviceManager.getRoleService().getRole(this.account.getRoleID());

        LOGGER.info("Store Manager Screen initialized for user: " + username);

        // Call to set up the UI components
        initializeComponents();
    }

    /**
     * Initialize the screen components
     */
    private void initializeComponents() {
        try {
            // Set the layout for this panel first
            this.setLayout(new BorderLayout());
            this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

            // Create a header toolbar with user info and logout
            JPanel headerPanel = createHeaderPanel();
            this.add(headerPanel, BorderLayout.NORTH);

            // Create tabbed pane for the main content
            JTabbedPane mainManagerPane = this.createStandardTabbedPane();

            // Initialize tabs
            JPanel dashboardTabPanel = new DashboardPanel();
            JPanel invoiceTabPanel = new InvoicePanel();
            JPanel employeeTabPanel = new EmployeePanel();
            JPanel workShiftTabPanel = new WorkShiftPanel();
            JPanel settingTabPanel = new SettingsPanel();

            // Add tabs to the mainManagerPane
            mainManagerPane.addTab("Dashboard", dashboardTabPanel);
            mainManagerPane.addTab("Quản lý hóa đơn", invoiceTabPanel);
            mainManagerPane.addTab("Quản lý nhân viên", employeeTabPanel);
            mainManagerPane.addTab("Phân công", workShiftTabPanel);
            mainManagerPane.addTab("Cài đặt", settingTabPanel);

            // Add mainManagerPane to StoreManagerScreen
            this.add(mainManagerPane, BorderLayout.CENTER);

        } catch (Exception e) {
            LOGGER.severe("Error initializing StoreManagerScreen: " + e.getMessage());
            JOptionPane.showMessageDialog(
                    this,
                    "Error initializing Store Manager Screen: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JTabbedPane createStandardTabbedPane() {
        return new StandardTabbedPane();
    }

    /**
     * Creates a header panel with user info and logout button
     *
     * @return Configured header panel
     */
    private JPanel createHeaderPanel() {
        return new ScreenHeader(
                this.employee.getEmployeeName(),
                this.role.getRoleName()
        );
    }
}