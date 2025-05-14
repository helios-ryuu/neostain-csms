package com.neostain.csms.view.screen.cs;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Role;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.component.ScreenHeader;
import com.neostain.csms.view.component.StandardTabbedPane;
import com.neostain.csms.view.screen.cs.panels.InvoicePanel;
import com.neostain.csms.view.screen.cs.panels.POSTabPanel;
import com.neostain.csms.view.screen.cs.panels.ProductPanel;
import com.neostain.csms.view.screen.cs.panels.PromotionPanel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class CashierStaffScreen extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(CashierStaffScreen.class.getName());
    private static final ServiceManager serviceManager = ServiceManager.getInstance();

    private final Account account;
    private final Employee employee;
    private final Role role;

    public CashierStaffScreen(String username) {
        // Load user data
        this.account = serviceManager.getAuthService().getAccountByUsername(username);
        this.employee = serviceManager.getManagementService().getEmployeeById(account.getEmployeeId());
        this.role = serviceManager.getAuthService().getRoleById(this.account.getRoleId());

        // Call to set up the UI components
        initializeComponents();
        LOGGER.info("[INIT] Màn hình Cashier Staff được khởi tạo cho người dùng: " + username);
    }

    private void initializeComponents() {
        try {
            this.setLayout(new BorderLayout());
            this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

            // Create a header toolbar with user info and logout
            JPanel headerPanel = new ScreenHeader(
                    this.employee.getName(),
                    this.role.getName()
            );
            this.add(headerPanel, BorderLayout.NORTH);

            // Create tabbed pane for the main content
            JTabbedPane mainCashierPanel = new StandardTabbedPane();

            // Initialize tabs
            JPanel POSTabPanel = new POSTabPanel();
            JPanel invoiceTabPanel = new InvoicePanel();
            JPanel promotionTabPanel = new PromotionPanel();
            JPanel productTabPanel = new ProductPanel();

            // Add tabs to the mainCashierPanel
            mainCashierPanel.addTab("POS", POSTabPanel);
            mainCashierPanel.addTab("Tra cứu hóa đơn", invoiceTabPanel);
            mainCashierPanel.addTab("Tra cứu khuyến mãi", promotionTabPanel);
            mainCashierPanel.addTab("Tra cứu sản phẩm", productTabPanel);

            // Add mainCashierPanel to StoreManagerScreen
            this.add(mainCashierPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            LOGGER.severe("Error initializing cashierStaff: " + e.getMessage());
            DialogFactory.showErrorDialog(
                    this,
                    "Initialization Error",
                    "Error initializing Cashier Staff Screen: " + e.getMessage()
            );
        }
    }
}