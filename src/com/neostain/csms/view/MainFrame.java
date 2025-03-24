package com.neostain.csms.view;

import com.neostain.csms.controller.MemberController;
import com.neostain.csms.model.Member;

import javax.swing.*;
import java.awt.*;

// Main window of the application
public class MainFrame extends JFrame {
    private final MemberController memberController;

    public MainFrame() {
        // Set window title
        super("CSMS - Convenience Store Management System");
        
        // Create controller
        memberController = new MemberController();
        
        // Set up window
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Show login screen first
        showLoginScreen();
        setVisible(true);
    }

    // Switch to log in screen
    public void showLoginScreen() {
        // Create login screen
        LoginScreen loginScreen = new LoginScreen(memberController);
        
        // Add login success handler
        loginScreen.setLoginHandler(() -> {
            Member member = memberController.getCurrentMember();
            if (member != null) {
                showDashboard(member);
            }
        });
        
        // Show login screen
        setContentPane(loginScreen);
        revalidate();
        repaint();
    }

    // Switch to dashboard
    private void showDashboard(Member member) {
        // Create and show dashboard
        DashboardView dashboardView = new DashboardView(member);
        setContentPane(dashboardView);
        revalidate();
        repaint();
    }
}