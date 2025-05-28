package com.neostain.csms.view.screen.cs;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.component.ScreenHeader;
import com.neostain.csms.view.component.StandardTabbedPane;
import com.neostain.csms.view.screen.cs.panels.InvoicePanel;
import com.neostain.csms.view.screen.cs.panels.POSPanel;
import com.neostain.csms.view.screen.cs.panels.ProductPanel;
import com.neostain.csms.view.screen.cs.panels.PromotionPanel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class CashierStaffScreen extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(CashierStaffScreen.class.getName());
    private static final ServiceManager serviceManager = ServiceManager.getInstance();

    private final Employee employee;

    private JTabbedPane mainCashierPanel;

    public CashierStaffScreen(String username) {
        // Load user data
        Account account = serviceManager.getAuthService().getAccountByUsername(username);
        this.employee = serviceManager.getManagementService().getEmployeeById(account.getEmployeeId());

        // Call to set up the UI components
        initializeComponents();
        LOGGER.info("[INIT] Màn hình Cashier Staff được khởi tạo cho người dùng: " + username);
    }

    private void initializeComponents() {
        try {
            this.setLayout(new BorderLayout());
            this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

            // Create a header toolbar with user info and logout
            String managerId = serviceManager.getManagementService().getEmployeeById(this.employee.getManagerId()).getId();
            String storeId = serviceManager.getManagementService().getStoreByManagerId(managerId).getId();
            String storeName = serviceManager.getManagementService().getStoreByManagerId(managerId).getName();
            JPanel headerPanel = new ScreenHeader(
                    this.employee.getId(),
                    this.employee.getName(),
                    storeId,
                    storeName
            );
            this.add(headerPanel, BorderLayout.NORTH);

            // Create tabbed pane for the main content
            mainCashierPanel = new StandardTabbedPane();
            POSPanel POSTabPanel = new POSPanel();
            InvoicePanel invoiceTabPanel = new InvoicePanel();
            invoiceTabPanel.setPOSPanel(POSTabPanel);
            PromotionPanel promotionTabPanel = new PromotionPanel(POSTabPanel, () -> mainCashierPanel.setSelectedIndex(0));
            JPanel productTabPanel = new ProductPanel(POSTabPanel, () -> mainCashierPanel.setSelectedIndex(0));

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