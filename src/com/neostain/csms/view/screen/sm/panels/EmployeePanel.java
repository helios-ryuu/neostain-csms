package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.util.Constants;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.ScrollableTable;
import com.neostain.csms.view.component.StandardMenu;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Employee management panels for Store Manager screen
 */
public class EmployeePanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(EmployeePanel.class.getName());

    /**
     * Creates a new employee panels
     */
    public EmployeePanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        // Create a panels with GridBagLayout
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);

        // Create GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create menu panels with standardized component
        StandardMenu menuPanel = createMenuPanel();

        // Set preferred size for menu panels
        menuPanel.setPreferredSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 600));
        menuPanel.setMinimumSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 200));

        // Add menu panels to left side
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        this.add(menuPanel, gbc);

        // Create employee panels with controls and data
        BorderedPanel employeeListPanel = createEmployeeListPanel();

        // Set preferred size for employee list panels
        employeeListPanel.setPreferredSize(new Dimension(800, 600));

        // Add employeeListPanel to right side
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        this.add(employeeListPanel, gbc);
    }

    private StandardMenu createMenuPanel() {
        StandardMenu menuPanel = new StandardMenu("Quản lý nhân viên");
        menuPanel.addMenuItem("Danh sách nhân viên", item -> {
            LOGGER.info("Employee list selected");
            // Future functionality for employee list
        });

        menuPanel.addMenuItem("Thêm nhân viên mới", item -> {
            LOGGER.info("Add employee selected");
            // Future functionality for adding employees
        });

        menuPanel.addMenuItem("Phân quyền", item -> {
            LOGGER.info("Roles management selected");
            // Future functionality for role management
        });

        return menuPanel;
    }

    private BorderedPanel createEmployeeListPanel() {
        BorderedPanel employeeListPanel = new BorderedPanel("Danh sách nhân viên");
        employeeListPanel.setLayout(new BorderLayout(10, 10));

        // Create search and action toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbarPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm nhân viên...");

        JButton searchButton = createStandardButton("Tìm kiếm", null);
        JButton addButton = createStandardButton("Thêm mới", null);
        JButton editButton = createStandardButton("Chỉnh sửa", null);
        JButton deleteButton = createStandardButton("Xóa", null);

        toolbarPanel.add(searchField);
        toolbarPanel.add(searchButton);
        toolbarPanel.add(addButton);
        toolbarPanel.add(editButton);
        toolbarPanel.add(deleteButton);

        employeeListPanel.add(toolbarPanel, BorderLayout.NORTH);

        // Create sample employee data
        String[] columnNames = {"Mã nhân viên", "Họ và tên", "Email", "Số điện thoại", "Chức vụ"};
        Object[][] data = {
                {"NV001", "Nguyễn Văn A", "nguyenvana@example.com", "0912345678", "Quản lý cửa hàng"},
                {"NV002", "Trần Thị B", "tranthib@example.com", "0987654321", "Nhân viên bán hàng"},
                {"NV003", "Lê Văn C", "levanc@example.com", "0912345679", "Nhân viên bán hàng"},
                {"NV004", "Phạm Thị D", "phamthid@example.com", "0987654322", "Nhân viên bán hàng"},
                {"NV005", "Hoàng Văn E", "hoangvane@example.com", "0912345670", "Nhân viên bán hàng"}
        };

        // Add table with scrolling
        ScrollableTable employeeTable = new ScrollableTable(columnNames, data);
        employeeListPanel.add(employeeTable, BorderLayout.CENTER);

        return employeeListPanel;
    }

    private JButton createStandardButton(String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }
}
