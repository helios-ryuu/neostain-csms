package com.neostain.csms.view.screen;

import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Role;
import com.neostain.csms.service.ServiceManager;
import com.neostain.csms.util.ColorUtils;
import com.neostain.csms.view.component.MenuItemPanel;
import com.neostain.csms.view.component.ScrollableContentTable;
import com.neostain.csms.view.component.TitledBorderPanel;

import javax.naming.NoPermissionException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Màn hình của nhân viên quản lý cửa hàng
 */
public class StoreManagerScreen extends JPanel {
    private final Account account;
    private final Employee employee;
    private final Role role;

    public StoreManagerScreen(String username) throws NoPermissionException {
        // Tạo Service Manager
        ServiceManager serviceManager = ServiceManager.getInstance();
        this.account = serviceManager.getAccountService().getAccount(username);
        this.employee = serviceManager.getEmployeeService().getEmployee(username);
        this.role = serviceManager.getRoleService().getRole(this.account.getRoleID());


        JTabbedPane mainManagerPane = this.createMainManagerPane();

        // Khởi tạo các tab
        JPanel dashboardTabPanel = this.createInvoiceTabPanel();
        JPanel employeeTabPanel = this.createEmployeeTabPanel();
        JPanel settingTabPanel = this.createSettingTabPanel();

        // Thêm các tab vào mainManagerPane
        mainManagerPane.addTab("Dashboard", dashboardTabPanel);
        mainManagerPane.addTab("Quản lý nhân viên", employeeTabPanel);
        mainManagerPane.addTab("Cài đặt", settingTabPanel);

        // Thêm mainManagerPane vào StoreManagerScreen
        this.setLayout(new BorderLayout());
        this.add(mainManagerPane, BorderLayout.CENTER);
    }

    // JTabbedPane Main Manager. Chứa các Tab Panel ở dưới
    private JTabbedPane createMainManagerPane() {
        JTabbedPane mainAuthorizePane = new JTabbedPane();
        mainAuthorizePane.setTabPlacement(JTabbedPane.TOP);
        mainAuthorizePane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainAuthorizePane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        mainAuthorizePane.setFocusable(false);

        return mainAuthorizePane;
    }

    // Các JPanel tab của JTabbedPane Main Manager
    // Thiết lập Setting Tab
    private JPanel createSettingTabPanel() throws NoPermissionException {
        // Tạo panel và thiết lập GridBagLayout
        JPanel settingTabPanel = new JPanel(new GridBagLayout());
        settingTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        settingTabPanel.setBackground(ColorUtils.componentBackgroundWhite);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Tạo panel menu
        // Tạo các menu item
        String[] menuItems = {"Thông tin"};
        JPanel menuPanel = createMenuPanel(menuItems);
        menuPanel.setPreferredSize(new Dimension(250, menuPanel.getPreferredSize().height));

        // Thêm menuPanel vào vị trí (0,0) với weightx = 0 để không mở rộng theo chiều ngang
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        settingTabPanel.add(menuPanel, gbc);

        // Tạo panel content bên phải
        // Tạo bảng hiển thị thông tin
        String[] columnNames = {"Thuộc tính", "Nội dung"};
        Object[][] data = {
                {"Tên người dùng", this.employee.getEmployeeName()},
                {"Email", this.employee.getEmail()},
                {"Số điện thoại", this.employee.getPhoneNumber()},
                {"Vai trò", this.role.getRoleName()},
                {"Ngày tạo", this.account.getAccountCreationTime()},
                {"Địa chỉ", this.employee.getAddress()},
        };
        JPanel contentPanel = getContentPanel(columnNames, data);

        // Thêm contentPanel vào vị trí (1,0) với weightx = 1 để chiếm hết không gian còn lại
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        settingTabPanel.add(contentPanel, gbc);

        return settingTabPanel;
    }

    private TitledBorderPanel createMenuPanel(String[] menuItems) {
        TitledBorderPanel menuPanel = new TitledBorderPanel("Menu");
        menuPanel.setPreferredSize(new Dimension(200, 0));
        menuPanel.setBackground(ColorUtils.componentBackgroundWhite);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        for (String itemName : menuItems) {
            JPanel menuItem = this.createMenuItem(itemName);
            menuPanel.add(menuItem);
        }

        // Thêm một panel trống để đẩy các menu item lên trên
        menuPanel.add(Box.createVerticalGlue());

        // Đặt JPanel vào JScrollPane để có thể scroll
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return menuPanel;
    }

    private MenuItemPanel createMenuItem(String text) {
        MenuItemPanel menuItem = new MenuItemPanel(text);

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        return menuItem;
    }

    private TitledBorderPanel getContentPanel(String[] columnNames, Object[][] data) throws NoPermissionException {
        TitledBorderPanel contentPanel = new TitledBorderPanel("Thông tin");
        contentPanel.setLayout(new BorderLayout(5, 5));

        ScrollableContentTable scrollableContentTable = new ScrollableContentTable(columnNames, data);
        contentPanel.add(scrollableContentTable, BorderLayout.CENTER);

        return contentPanel;
    }

    // Thiết lập Dashboard Tab
    private JPanel createInvoiceTabPanel() throws NoPermissionException {
        JPanel invoiceTabPanel = new JPanel(new GridBagLayout());
        invoiceTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        invoiceTabPanel.setBackground(ColorUtils.componentBackgroundWhite);
        invoiceTabPanel.add(new JLabel("Dashboard content will be here"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Tạo panel menu
        // Tạo các menu item
        String[] menuItems = {"Yêu cầu cần xét duyệt"};
        JPanel menuPanel = createMenuPanel(menuItems);
        menuPanel.setPreferredSize(new Dimension(250, menuPanel.getPreferredSize().height));

        // Thêm menuPanel vào vị trí (0,0) với weightx = 0 để không mở rộng theo chiều ngang
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        invoiceTabPanel.add(menuPanel, gbc);

        // Tạo panel content bên phải
        // Tạo bảng hiển thị thông tin
        String[] columnNames = {"Mã hóa đơn", "Nội dung"};
        Object[][] data = {
                {"Tên người dùng", this.employee.getEmployeeName()},
                {"Email", this.employee.getEmail()},
                {"Số điện thoại", this.employee.getPhoneNumber()},
                {"Vai trò", this.role.getRoleName()},
                {"Ngày tạo", this.account.getAccountCreationTime()},
                {"Địa chỉ", this.employee.getAddress()},
        };
        JPanel contentPanel = getContentPanel(columnNames, data);

        // Thêm contentPanel vào vị trí (1,0) với weightx = 1 để chiếm hết không gian còn lại
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        invoiceTabPanel.add(contentPanel, gbc);

        return invoiceTabPanel;
    }

    // Thiết lập Employee Tab
    private JPanel createEmployeeTabPanel() {
        JPanel employeeTabPanel = new JPanel();
        employeeTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        employeeTabPanel.setBackground(new Color(249, 249, 249));
        employeeTabPanel.add(new JLabel("Employee content will be here"));

        return employeeTabPanel;
    }
}