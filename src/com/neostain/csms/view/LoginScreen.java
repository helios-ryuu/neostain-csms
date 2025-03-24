package com.neostain.csms.view;

import com.neostain.csms.controller.MemberController;
import com.neostain.csms.model.Member;

import javax.swing.*;
import java.awt.*;

// Login screen with basic components
public class LoginScreen extends JPanel {
    private final MemberController memberController;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;
    private Runnable loginHandler;

    public LoginScreen(MemberController memberController) {
        this.memberController = memberController;
        
        // Create text fields
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Đăng nhập");
        statusLabel = new JLabel("Vui lòng đăng nhập");

        // Set up simple layout
        setLayout(new GridLayout(4, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add login form
        add(makeRow(new JLabel("Tên đăng nhập:"), usernameField));
        add(makeRow(new JLabel("Mật khẩu:"), passwordField));
        add(loginButton);
        add(statusLabel);

        // Add login button click handler
        loginButton.addActionListener(e -> tryLogin());
    }

    // Make a row with label and field
    private JPanel makeRow(JLabel label, JComponent field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(label);
        row.add(field);
        return row;
    }

    // Try to log in with entered details
    private void tryLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (memberController.login(username, password)) {
                // Login worked
                statusLabel.setText("Đăng nhập thành công!");
                statusLabel.setForeground(Color.GREEN);
                passwordField.setText("");
                
                // Call success handler
                if (loginHandler != null) {
                    loginHandler.run();
                }
            } else {
                // Login failed
                statusLabel.setText("Sai tên đăng nhập hoặc mật khẩu");
                statusLabel.setForeground(Color.RED);
            }
        } catch (Exception ex) {
            // Show error
            statusLabel.setText("Lỗi: " + ex.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }

    // Set what happens after successful login
    public void setLoginHandler(Runnable handler) {
        this.loginHandler = handler;
    }
}

