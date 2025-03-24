package com.neostain.csms.view;

import com.neostain.csms.model.Member;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JPanel {
    private final Member member;
    private final JLabel welcomeLabel;
    private final JLabel roleLabel;

    public DashboardView(Member member) {
        this.member = member;
        this.welcomeLabel = new JLabel();
        this.roleLabel = new JLabel();

        setupLayout();
        updateDisplay();
    }

    private void setupLayout() {
        setLayout(new GridLayout(3, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add welcome and role labels
        add(welcomeLabel);
        add(roleLabel);

        // Add logout button
        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.addActionListener(e -> handleLogout());
        add(logoutButton);
    }

    private void updateDisplay() {
        welcomeLabel.setText("Chào mừng, " + member.getUsername());
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 16));
        roleLabel.setText("Vai trò: " + member.getRole());
    }

    private void handleLogout() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof MainFrame) {
            ((MainFrame) window).showLoginScreen();
        }
    }

    public void refresh() {
        updateDisplay();
    }
}