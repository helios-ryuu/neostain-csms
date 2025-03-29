package com.neostain.csms.view.screen;

import com.neostain.csms.service.ServiceManager;

import javax.swing.*;
import java.awt.*;

public class ManagerScreen extends JPanel {
    private final ServiceManager serviceManager;

    // Các thành phần giao diện
    public JTabbedPane mainManagerPane;

    private final JPanel dashboardTabPanel;
    private final JPanel employeeTabPanel;
    private final JPanel productTabPanel;

    public ManagerScreen(String username) {
        // Tạo Service Manager
        this.serviceManager = ServiceManager.getInstance();
        
        // Khởi tạo các tab
        this.mainManagerPane = new JTabbedPane();
        this.dashboardTabPanel = new JPanel();
        this.employeeTabPanel = new JPanel();
        this.productTabPanel = new JPanel();

        this.mainManagerPane.setTabPlacement(JTabbedPane.TOP);
        this.mainManagerPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.mainManagerPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        this.mainManagerPane.setFocusable(false);
        
        // Tạo các panel cho từng tab
        this.setupDashboardPanel();
        this.setupEmployeePanel();
        this.setupProductPanel();
        
        // Thêm các tab vào mainManagerPane
        mainManagerPane.addTab("Dashboard", dashboardTabPanel);
        mainManagerPane.addTab("Quản lý nhân viên", employeeTabPanel);
        mainManagerPane.addTab("Quản lý hàng hóa", productTabPanel);

        // Thêm mainManagerPane vào ManagerScreen
        this.setLayout(new BorderLayout());
        this.add(mainManagerPane, BorderLayout.CENTER);
    }
    
    private void setupDashboardPanel() {
        this.dashboardTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.dashboardTabPanel.setBackground(new Color(249, 249, 249));
        this.dashboardTabPanel.add(new JLabel("Dashboard content will be here"));
    }
    
    private void setupEmployeePanel() {
        this.employeeTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.employeeTabPanel.setBackground(new Color(249, 249, 249));
        this.employeeTabPanel.add(new JLabel("Employee content will be here"));
    }
    
    private void setupProductPanel() {
        this.productTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.productTabPanel.setBackground(new Color(249, 249, 249));
        this.productTabPanel.add(new JLabel("Product content will be here"));
    }
}
