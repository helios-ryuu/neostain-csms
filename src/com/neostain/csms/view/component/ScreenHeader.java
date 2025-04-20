package com.neostain.csms.view.component;

import com.neostain.csms.Constants;

import javax.swing.*;
import java.awt.*;

public class ScreenHeader extends JPanel {
    public ScreenHeader(String employeeName, String roleName) {

        this.setLayout(new BorderLayout());
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_HEADER);
        this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        this.setPreferredSize(new Dimension(0, 50));

        // User info on the left
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.X_AXIS));
        userInfoPanel.setOpaque(false);
        userInfoPanel.add(Box.createHorizontalStrut(10)); // Add some padding

        JLabel userLabel = new JLabel(employeeName + " (" + roleName + ")");
        userLabel.setFont(new Font(userLabel.getFont().getName(), Font.BOLD, 12));
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        userInfoPanel.add(userLabel);

        // Actions on the right
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setOpaque(false);

        JButton logoutButton = new LogoutButton(this, "Đăng xuất");
        logoutButton.setFont(new Font(logoutButton.getFont().getName(), Font.BOLD, 12));
        logoutButton.setForeground(Color.RED);
        logoutButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        actionsPanel.add(logoutButton);
        actionsPanel.add(Box.createHorizontalStrut(10)); // Add some padding

        // Container panels with vertical centering
        JPanel leftContainer = new JPanel(new GridBagLayout());
        leftContainer.setOpaque(false);
        leftContainer.add(userInfoPanel);

        JPanel rightContainer = new JPanel(new GridBagLayout());
        rightContainer.setOpaque(false);
        rightContainer.add(actionsPanel);

        // Add panels to header
        this.add(leftContainer, BorderLayout.WEST);
        this.add(rightContainer, BorderLayout.EAST);
    }
}
