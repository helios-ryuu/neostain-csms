package com.neostain.csms.view.screen.sm.dashboard;

import com.neostain.csms.Constants;
import com.neostain.csms.view.component.*;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Dashboard panel for the store manager screen
 */
public class DashboardPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(DashboardPanel.class.getName());

    /**
     * Creates a new dashboard panel
     */
    public DashboardPanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        // Create panel with GridBagLayout
        this.setLayout(new GridBagLayout());
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create a menu panel with a standardized component
        StandardMenuPanel menuPanel = createMenuPanel();
        menuPanel.setPreferredSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 600));
        menuPanel.setMinimumSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 200));

        // Add menu panel to left side
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        this.add(menuPanel, gbc);

        // Create dashboard content panel
        JPanel dashboardContent = new JPanel(new BorderLayout(10, 10));
        dashboardContent.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create dashboard tiles panel
        JPanel tilesPanel = createTilesPanel();
        tilesPanel.setOpaque(false);
        tilesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create store information panel
        TitledBorderPanel storeInfoPanel = createStoreInfoPanel();

        // Create member customers panel
        TitledBorderPanel memberPanel = createMemberPanel();

        // Thay thế JSplitPane bằng GridBagLayout hoặc BorderLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        GridBagConstraints contentGbc = new GridBagConstraints();
        contentGbc.fill = GridBagConstraints.BOTH;
        contentGbc.insets = new Insets(0, 0, 5, 0);

        // Thêm tiles panel ở trên cùng
        contentGbc.gridx = 0;
        contentGbc.gridy = 0;
        contentGbc.weightx = 1.0;
        contentGbc.weighty = 0.1; // Chiếm ít không gian dọc
        contentPanel.add(tilesPanel, contentGbc);

        // Thêm store info panel ở giữa
        contentGbc.gridx = 0;
        contentGbc.gridy = 1;
        contentGbc.weightx = 1.0;
        contentGbc.weighty = 0.3; // Chiếm 30% không gian dọc
        contentPanel.add(storeInfoPanel, contentGbc);

        // Thêm member panel ở dưới
        contentGbc.gridx = 0;
        contentGbc.gridy = 2;
        contentGbc.weightx = 1.0;
        contentGbc.weighty = 0.6; // Chiếm 60% không gian dọc
        contentPanel.add(memberPanel, contentGbc);

        dashboardContent.add(contentPanel, BorderLayout.CENTER);

        // Create overall dashboard panel
        TitledBorderPanel dashboardPanel = new TitledBorderPanel("Thống kê bán hàng");
        dashboardPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        dashboardPanel.setLayout(new BorderLayout());
        dashboardPanel.add(dashboardContent, BorderLayout.CENTER);

        // Set preferred size for dashboard panel
        dashboardPanel.setPreferredSize(new Dimension(800, 600));

        // Add dashboard panel to right side
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        this.add(dashboardPanel, gbc);

    }

    private StandardMenuPanel createMenuPanel() {
        StandardMenuPanel menuPanel = new StandardMenuPanel("Dashboard");
        menuPanel.addMenuItem("Thống kê bán hàng", item -> {
            LOGGER.info("Sales statistics selected");
            // Future functionality for sales statistics
        });

        menuPanel.addMenuItem("Thông tin cửa hàng", item -> {
            LOGGER.info("Store information selected");
            // Future functionality for store information
        });

        menuPanel.addMenuItem("Khách hàng thành viên", item -> {
            LOGGER.info("Member customers selected");
            // Future functionality for member customers
        });

        menuPanel.addMenuItem("Xuất báo cáo doanh thu", item -> {
            LOGGER.info("Export revenue report selected");
            // Future functionality for report export
        });

        return menuPanel;
    }

    private JPanel createTilesPanel() {
        JPanel tilesPanel = new JPanel(new GridLayout(2, 4, 10, 10));

        // Add dashboard tiles for the first row-sales statistics
        tilesPanel.add(new DashboardTile("Tổng hoá đơn", "128", new Color(41, 128, 185)));
        tilesPanel.add(new DashboardTile("Hoá đơn hôm nay", "24", new Color(39, 174, 96)));
        tilesPanel.add(new DashboardTile("Doanh thu hôm nay", "5,280,000 VNĐ", new Color(211, 84, 0)));
        tilesPanel.add(new DashboardTile("Doanh thu tháng", "42,750,000 VNĐ", new Color(142, 68, 173)));

        // Add dashboard tiles for the second row-additional statistics
        tilesPanel.add(new DashboardTile("Sản phẩm bán chạy", "Cà phê", new Color(52, 152, 219)));
        tilesPanel.add(new DashboardTile("Khách hàng mới", "15", new Color(46, 204, 113)));
        tilesPanel.add(new DashboardTile("Tổng nhân viên", "12", new Color(230, 126, 34)));
        tilesPanel.add(new DashboardTile("Đơn hàng online", "8", new Color(155, 89, 182)));

        return tilesPanel;
    }

    private TitledBorderPanel createStoreInfoPanel() {
        TitledBorderPanel storeInfoPanel = new TitledBorderPanel("Thông tin cửa hàng");
        storeInfoPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        storeInfoPanel.setLayout(new BorderLayout());

        // Tool panel for store info actions
        JPanel storeToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        storeToolPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JButton editStoreInfoButton = new StandardButton(this, "Chỉnh sửa thông tin");
        JButton updateContactButton = new StandardButton(this, "Cập nhật liên hệ");
        JButton updateWorkingHoursButton = new StandardButton(this, "Cập nhật giờ làm việc");

        storeToolPanel.add(editStoreInfoButton);
        storeToolPanel.add(updateContactButton);
        storeToolPanel.add(updateWorkingHoursButton);

        storeInfoPanel.add(storeToolPanel, BorderLayout.NORTH);

        JPanel storeInfoContent = new JPanel(new GridBagLayout());
        storeInfoContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        storeInfoContent.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        GridBagConstraints storeGbc = new GridBagConstraints();
        storeGbc.fill = GridBagConstraints.HORIZONTAL;
        storeGbc.insets = new Insets(5, 5, 5, 5);

        // Add store info fields với GridBagLayout
        // Hàng 1
        storeGbc.gridx = 0;
        storeGbc.gridy = 0;
        storeGbc.weightx = 0.3;
        JLabel storeNameLabel = new JLabel("Tên cửa hàng:");
        storeNameLabel.setFont(new Font(storeNameLabel.getFont().getName(), Font.BOLD, 12));
        storeInfoContent.add(storeNameLabel, storeGbc);

        storeGbc.gridx = 1;
        storeGbc.weightx = 0.7;
        storeInfoContent.add(new JLabel("NeoStain Convenience Store"), storeGbc);

        // Hàng 2
        storeGbc.gridx = 0;
        storeGbc.gridy = 1;
        storeGbc.weightx = 0.3;
        JLabel addressLabel = new JLabel("Địa chỉ:");
        addressLabel.setFont(new Font(addressLabel.getFont().getName(), Font.BOLD, 12));
        storeInfoContent.add(addressLabel, storeGbc);

        storeGbc.gridx = 1;
        storeGbc.weightx = 0.7;
        storeInfoContent.add(new JLabel("123 Đường ABC, Quận XYZ, TP. HCM"), storeGbc);

        // Hàng 3
        storeGbc.gridx = 0;
        storeGbc.gridy = 2;
        storeGbc.weightx = 0.3;
        JLabel phoneLabel = new JLabel("Số điện thoại:");
        phoneLabel.setFont(new Font(phoneLabel.getFont().getName(), Font.BOLD, 12));
        storeInfoContent.add(phoneLabel, storeGbc);

        storeGbc.gridx = 1;
        storeGbc.weightx = 0.7;
        storeInfoContent.add(new JLabel("(028) 1234 5678"), storeGbc);

        // Hàng 4
        storeGbc.gridx = 0;
        storeGbc.gridy = 3;
        storeGbc.weightx = 0.3;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font(emailLabel.getFont().getName(), Font.BOLD, 12));
        storeInfoContent.add(emailLabel, storeGbc);

        storeGbc.gridx = 1;
        storeGbc.weightx = 0.7;
        storeInfoContent.add(new JLabel("contact@neostain.com"), storeGbc);

        // Hàng 5
        storeGbc.gridx = 0;
        storeGbc.gridy = 4;
        storeGbc.weightx = 0.3;
        JLabel hoursLabel = new JLabel("Giờ mở cửa:");
        hoursLabel.setFont(new Font(hoursLabel.getFont().getName(), Font.BOLD, 12));
        storeInfoContent.add(hoursLabel, storeGbc);

        storeGbc.gridx = 1;
        storeGbc.weightx = 0.7;
        storeInfoContent.add(new JLabel("07:00 - 22:00 (Hàng ngày)"), storeGbc);

        storeInfoPanel.add(storeInfoContent, BorderLayout.CENTER);

        return storeInfoPanel;
    }

    private TitledBorderPanel createMemberPanel() {
        TitledBorderPanel memberPanel = new TitledBorderPanel("Khách hàng thành viên");
        memberPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        memberPanel.setLayout(new BorderLayout());

        // Tool panel for member actions
        JPanel memberToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memberToolPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField memberSearchField = new JTextField(20);
        memberSearchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm khách hàng...");

        JButton memberSearchButton = new StandardButton(this, "Tìm kiếm");
        JButton addMemberButton = new StandardButton(this, "Thêm mới");
        JButton editMemberButton = new StandardButton(this, "Chỉnh sửa");
        JButton deleteMemberButton = new StandardButton(this, "Xóa");

        memberToolPanel.add(memberSearchField);
        memberToolPanel.add(memberSearchButton);
        memberToolPanel.add(addMemberButton);
        memberToolPanel.add(editMemberButton);
        memberToolPanel.add(deleteMemberButton);

        memberPanel.add(memberToolPanel, BorderLayout.NORTH);

        // Create sample member data
        String[] memberColumns = {"Mã KH", "Họ và tên", "Số điện thoại", "Email", "Điểm tích lũy", "Hạng thành viên"};
        Object[][] memberData = {
                {"KH001", "Nguyễn Thị Hằng", "0912345678", "hang@example.com", "250", "Vàng"},
                {"KH002", "Trần Văn Bình", "0987654321", "binh@example.com", "120", "Bạc"},
                {"KH003", "Lê Thị Cam", "0923456789", "cam@example.com", "85", "Đồng"},
                {"KH004", "Phạm Văn Dũng", "0934567890", "dung@example.com", "320", "Vàng"},
                {"KH005", "Hoàng Thị Em", "0945678901", "em@example.com", "45", "Đồng"},
                {"KH006", "Vũ Văn Giang", "0956789012", "giang@example.com", "150", "Bạc"}
        };

        // Create a table with data
        ScrollableContentTable memberTable = new ScrollableContentTable(memberColumns, memberData);
        memberPanel.add(memberTable, BorderLayout.CENTER);

        // Add pagination panel
        PaginationPanel memberPagination = new PaginationPanel(5, memberData.length);
        memberPanel.add(memberPagination, BorderLayout.SOUTH);

        return memberPanel;
    }
}
