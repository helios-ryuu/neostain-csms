package com.neostain.csms.presentation.view.screens;

import com.neostain.csms.infrastructure.persistence.jdbc.AuthServiceImpl;
import com.neostain.csms.presentation.controller.AuthController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        // Title of application
        super("CSMS - Convenience Store Management System");
        
        // Create login screen
        LoginScreen loginScreen = new LoginScreen();
        
        // Set up frame
        this.setContentPane(loginScreen);
        this.setSize(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}