package com.neostain.csms.view.screen.cs.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.util.Constants;
import com.neostain.csms.view.component.BorderedPanel;

import javax.swing.*;
import java.awt.*;

public class POSTabPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();

    private final String username;

    public POSTabPanel() {
        this.username = serviceManager.getCurrentUsername();
        initializeComponents();
    }

    private void initializeComponents() {
        // Create panels with GridBagLayout
        this.setLayout(new GridBagLayout());
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create components
        BorderedPanel productListPanel = new BorderedPanel("Giỏ hàng");
        BorderedPanel infoPanel = new BorderedPanel("Thông tin");
        BorderedPanel paymentPanel = new BorderedPanel("Thanh toán");
        BorderedPanel memberPanel = new BorderedPanel("Thành viên");

        // Cấu hình POSTabPanel
        this.setLayout(new GridBagLayout());
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.insets = new Insets(5, 5, 5, 5);

        // --- 1. Tạo panel con cho cột bên trái (leftColumn) ---
        JPanel leftColumn = new JPanel(new GridBagLayout());
        leftColumn.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.fill = GridBagConstraints.BOTH;
        gbcLeft.insets = new Insets(5, 5, 5, 5);

        // a) productListPanel ở trên, mở rộng cả chiều rộng và chiều cao
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.gridwidth = 2;      // chiếm cả 2 cột trong leftColumn
        gbcLeft.weightx = 1.0;
        gbcLeft.weighty = 1.0;
        leftColumn.add(productListPanel, gbcLeft);

        // b) paymentPanel bên trái dưới
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 1;
        gbcLeft.gridwidth = 1;
        gbcLeft.weightx = 0.5;
        gbcLeft.weighty = 1.0;
        leftColumn.add(paymentPanel, gbcLeft);

        // c) memberPanel bên phải dưới
        gbcLeft.gridx = 1;
        gbcLeft.gridy = 1;
        gbcLeft.weightx = 0.5;
        gbcLeft.weighty = 1.0;
        leftColumn.add(memberPanel, gbcLeft);

        // --- 2. Thêm leftColumn vào POSTabPanel ---
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.weightx = 1.0;    // chiếm không gian rộng mở
        gbcMain.weighty = 1.0;
        this.add(leftColumn, gbcMain);

        // --- 3. Thêm infoPanel bên phải, cố định rộng 300px, cao mở rộng ---
        // Đặt preferredSize để cố định chiều ngang
        infoPanel.setPreferredSize(new Dimension(300, 0));

        gbcMain.gridx = 1;
        gbcMain.gridy = 0;
        gbcMain.gridheight = 2;   // trải dài sang cả hai hàng
        gbcMain.weightx = 0.0; // không mở rộng
        gbcMain.weighty = 1.0; // cao mở rộng
        this.add(infoPanel, gbcMain);
    }
}
