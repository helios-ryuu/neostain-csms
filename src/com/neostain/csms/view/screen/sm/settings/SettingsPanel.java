package com.neostain.csms.view.screen.sm.settings;

import com.neostain.csms.util.Constants;
import com.neostain.csms.view.component.StandardMenuPanel;
import com.neostain.csms.view.component.TitledBorderPanel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Settings panel for the Store Manager screen
 */
public class SettingsPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(SettingsPanel.class.getName());

    /**
     * Constructs a new SettingsPanel
     */
    public SettingsPanel() {
        initializeComponents();
    }

    /**
     * Initialize panel components
     */
    private void initializeComponents() {
        // Set up the panel layout
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);

        // Create constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create menu panel
        StandardMenuPanel menuPanel = createMenuPanel();

        // Set preferred size for menu panel
        menuPanel.setPreferredSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 600));
        menuPanel.setMinimumSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 200));

        // Add menu panel to left side
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        this.add(menuPanel, gbc);

        // Create content panel
        JPanel contentPanel = createContentPanel();

        // Set preferred size for content panel
        contentPanel.setPreferredSize(new Dimension(800, 600));

        // Add content panel to right side
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        this.add(contentPanel, gbc);
    }

    /**
     * Creates the menu panel
     *
     * @return Configured menu panel
     */
    private StandardMenuPanel createMenuPanel() {
        StandardMenuPanel menuPanel = new StandardMenuPanel("Cài đặt");

        menuPanel.addMenuItem("Thông tin hệ thống", item -> {
            LOGGER.info("System info selected");
            // Future functionality for showing system info
        });

        menuPanel.addMenuItem("Cài đặt hệ thống", item -> {
            LOGGER.info("System settings selected");
            // Future functionality for system settings
        });

        menuPanel.addMenuItem("Quản lý tài khoản", item -> {
            LOGGER.info("Account management selected");
            // Future functionality for account management
        });

        return menuPanel;
    }

    /**
     * Creates the content panel
     *
     * @return Configured content panel
     */
    private JPanel createContentPanel() {
        TitledBorderPanel contentPanel = new TitledBorderPanel("Thông tin hệ thống");
        contentPanel.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setBackground(Color.WHITE);

        // Add system information
        JPanel systemInfoPanel = new JPanel();
        systemInfoPanel.setLayout(new BoxLayout(systemInfoPanel, BoxLayout.Y_AXIS));
        systemInfoPanel.setBackground(Color.WHITE);
        systemInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Software information
        addInfoRow(systemInfoPanel, "Tên phần mềm:", "Hệ thống quản lý cửa hàng (CSMS)");
        addInfoRow(systemInfoPanel, "Phiên bản:", "1.0.0");
        addInfoRow(systemInfoPanel, "Ngày phát hành:", "15/06/2023");
        addInfoRow(systemInfoPanel, "Công ty phát triển:", "Neostain Technology");

        // Add some spacing
        systemInfoPanel.add(Box.createVerticalStrut(20));

        // System information
        addInfoRow(systemInfoPanel, "Hệ điều hành:", System.getProperty("os.name"));
        addInfoRow(systemInfoPanel, "Phiên bản Java:", System.getProperty("java.version"));

        infoPanel.add(systemInfoPanel, BorderLayout.CENTER);
        contentPanel.add(infoPanel);

        return contentPanel;
    }

    /**
     * Helper method to add an information row to the system info panel
     *
     * @param panel Panel to add the row to
     * @param label Label text
     * @param value Value text
     */
    private void addInfoRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(10, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(600, 30));
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font(labelComponent.getFont().getName(), Font.BOLD, 12));
        labelComponent.setPreferredSize(new Dimension(150, 25));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font(valueComponent.getFont().getName(), Font.PLAIN, 12));

        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.CENTER);

        panel.add(rowPanel);
        panel.add(Box.createVerticalStrut(8));
    }
}
