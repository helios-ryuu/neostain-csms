package com.neostain.csms.view.screen.sm.invoice;

import com.neostain.csms.util.Constants;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.ScrollableTable;
import com.neostain.csms.view.component.StandardMenu;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Invoice management panel for Store Manager screen
 */
public class InvoicePanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(InvoicePanel.class.getName());

    /**
     * Creates a new invoice panel
     */
    public InvoicePanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        // Create panel with GridBagLayout
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);

        // Create GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create menu panel with standardized component
        StandardMenu menuPanel = createMenuPanel();

        // Set preferred size for menu panel - sử dụng View.MENU_PANEL_WIDTH
        menuPanel.setPreferredSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 600));
        menuPanel.setMinimumSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 200));

        // Add menu panel to left side
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        this.add(menuPanel, gbc);

        // Create invoice management content
        JPanel invoiceContent = new JPanel(new BorderLayout(10, 10));
        invoiceContent.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create search and filter panel
        JPanel searchPanel = createSearchPanel();

        // Create invoice table panel
        BorderedPanel invoiceTablePanel = createInvoiceTablePanel();

        // Create cancel request panel
        BorderedPanel cancelRequestPanel = createCancelRequestPanel();

        // Create JSplitPane to divide the layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(450);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);

        // Create upper panel containing search and invoice list
        JPanel upperPanel = new JPanel(new BorderLayout());
        upperPanel.add(searchPanel, BorderLayout.NORTH);
        upperPanel.add(invoiceTablePanel, BorderLayout.CENTER);

        splitPane.setTopComponent(upperPanel);
        splitPane.setBottomComponent(cancelRequestPanel);

        // Add splitPane to invoiceContent
        invoiceContent.add(splitPane, BorderLayout.CENTER);

        // Create overall invoice management panel
        BorderedPanel invoicePanel = createTitledPanel("Quản lý hóa đơn");
        invoicePanel.setLayout(new BorderLayout());
        invoicePanel.add(invoiceContent, BorderLayout.CENTER);

        // Set preferred size for invoice panel
        invoicePanel.setPreferredSize(new Dimension(800, 600));

        // Add invoice panel to right side
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        this.add(invoicePanel, gbc);
    }

    private StandardMenu createMenuPanel() {
        StandardMenu menuPanel = new StandardMenu("Quản lý hoá đơn");
        menuPanel.addMenuItem("Tra cứu hóa đơn", item -> {
            LOGGER.info("All invoices selected");
            // Future functionality for all invoices
        });

        menuPanel.addMenuItem("Yêu cầu hủy hóa đơn", item -> {
            LOGGER.info("Cancel invoice requests selected");
            // Future functionality for cancel requests
        });

        return menuPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm hóa đơn"));

        // Create search fields
        JTextField invoiceIdField = new JTextField(12);
        invoiceIdField.putClientProperty("JTextField.placeholderText", "Mã hóa đơn");

        JTextField customerField = new JTextField(15);
        customerField.putClientProperty("JTextField.placeholderText", "Tên khách hàng");

        JTextField employeeField = new JTextField(15);
        employeeField.putClientProperty("JTextField.placeholderText", "Nhân viên bán hàng");

        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{
                "Tất cả trạng thái", "Đã thanh toán", "Chờ thanh toán", "Đã hủy"
        });

        JLabel fromDateLabel = new JLabel("Từ ngày:");
        JTextField fromDateField = new JTextField(10);
        fromDateField.putClientProperty("JTextField.placeholderText", "dd/mm/yyyy");

        JLabel toDateLabel = new JLabel("Đến ngày:");
        JTextField toDateField = new JTextField(10);
        toDateField.putClientProperty("JTextField.placeholderText", "dd/mm/yyyy");

        JButton searchButton = createStandardButton("Tìm kiếm", null);
        JButton resetButton = createStandardButton("Đặt lại", null);

        // Add components to search panel
        searchPanel.add(new JLabel("Mã hóa đơn:"));
        searchPanel.add(invoiceIdField);
        searchPanel.add(new JLabel("Khách hàng:"));
        searchPanel.add(customerField);
        searchPanel.add(new JLabel("Nhân viên:"));
        searchPanel.add(employeeField);
        searchPanel.add(new JLabel("Trạng thái:"));
        searchPanel.add(statusComboBox);
        searchPanel.add(fromDateLabel);
        searchPanel.add(fromDateField);
        searchPanel.add(toDateLabel);
        searchPanel.add(toDateField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        return searchPanel;
    }

    private BorderedPanel createInvoiceTablePanel() {
        BorderedPanel invoiceTablePanel = createTitledPanel("Danh sách hóa đơn");
        invoiceTablePanel.setLayout(new BorderLayout());

        // Create toolbar for invoice actions
        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JButton viewDetailsButton = createStandardButton("Xem chi tiết", null);
        JButton printButton = createStandardButton("In hóa đơn", null);
        JButton exportButton = createStandardButton("Xuất Excel", null);
        JButton deleteButton = createStandardButton("Xóa hóa đơn", null);
        JButton processCancelRequestButton = createStandardButton("Xử lý yêu cầu hủy", null);

        toolPanel.add(viewDetailsButton);
        toolPanel.add(printButton);
        toolPanel.add(exportButton);
        toolPanel.add(deleteButton);
        toolPanel.add(processCancelRequestButton);

        invoiceTablePanel.add(toolPanel, BorderLayout.NORTH);

        // Create sample invoice data
        String[] columnNames = {"Mã hóa đơn", "Ngày tạo", "Khách hàng", "Nhân viên", "Tổng tiền", "Trạng thái", "Ghi chú"};
        Object[][] data = {
                {"HD00128", "15/06/2023 14:25", "Khách lẻ", "Nguyễn Văn A", "280,000 VNĐ", "Đã thanh toán", ""},
                {"HD00127", "15/06/2023 13:40", "Công ty TNHH ABC", "Nguyễn Văn A", "1,450,000 VNĐ", "Đã thanh toán", ""},
                {"HD00126", "15/06/2023 11:15", "Khách lẻ", "Trần Thị B", "85,000 VNĐ", "Đã thanh toán", ""},
                {"HD00125", "15/06/2023 10:30", "Khách lẻ", "Trần Thị B", "175,000 VNĐ", "Đã thanh toán", ""},
                {"HD00124", "14/06/2023 16:45", "Công ty CP XYZ", "Lê Văn C", "2,300,000 VNĐ", "Đã thanh toán", ""},
                {"HD00123", "14/06/2023 15:20", "Khách lẻ", "Lê Văn C", "150,000 VNĐ", "Đã thanh toán", ""},
                {"HD00122", "14/06/2023 14:10", "Khách lẻ", "Nguyễn Văn A", "95,000 VNĐ", "Đã thanh toán", ""},
                {"HD00121", "14/06/2023 11:05", "Khách lẻ", "Trần Thị B", "120,000 VNĐ", "Đã thanh toán", ""},
                {"HD00120", "14/06/2023 09:30", "Khách lẻ", "Nguyễn Văn A", "65,000 VNĐ", "Đã thanh toán", ""},
                {"HD00119", "13/06/2023 17:45", "Công ty TNHH DEF", "Trần Thị B", "780,000 VNĐ", "Đã thanh toán", ""}
        };

        // Create table with data
        ScrollableTable invoiceTable = new ScrollableTable(columnNames, data);
        invoiceTablePanel.add(invoiceTable, BorderLayout.CENTER);

        return invoiceTablePanel;
    }

    private BorderedPanel createCancelRequestPanel() {
        BorderedPanel cancelRequestPanel = createTitledPanel("Yêu cầu hủy hóa đơn");
        cancelRequestPanel.setLayout(new BorderLayout());

        // Create sample cancel request data
        String[] requestColumnNames = {"Mã hóa đơn", "Ngày tạo", "Khách hàng", "Lý do hủy", "Nhân viên yêu cầu", "Trạng thái"};
        Object[][] requestData = {
                {"HD00130", "16/06/2023 10:15", "Nguyễn Thị X", "Khách hàng đổi ý", "Trần Thị B", "Chờ duyệt"},
                {"HD00129", "16/06/2023 09:30", "Công ty TNHH MNO", "Sai thông tin", "Lê Văn C", "Chờ duyệt"}
        };

        // Create table for cancel requests
        ScrollableTable requestTable = new ScrollableTable(requestColumnNames, requestData);
        cancelRequestPanel.add(requestTable, BorderLayout.CENTER);

        // Create toolbar for request actions
        JPanel requestToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        requestToolPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JButton approveButton = createStandardButton("Duyệt yêu cầu", null);
        JButton rejectButton = createStandardButton("Từ chối", null);
        JButton viewRequestDetailsButton = createStandardButton("Xem chi tiết", null);

        requestToolPanel.add(approveButton);
        requestToolPanel.add(rejectButton);
        requestToolPanel.add(viewRequestDetailsButton);

        cancelRequestPanel.add(requestToolPanel, BorderLayout.NORTH);

        return cancelRequestPanel;
    }

    private BorderedPanel createTitledPanel(String title) {
        BorderedPanel panel = new BorderedPanel(title);
        panel.setBackground(Color.WHITE);
        return panel;
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
