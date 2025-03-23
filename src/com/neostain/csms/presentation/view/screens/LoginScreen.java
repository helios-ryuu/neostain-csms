package com.neostain.csms.presentation.view.screens;

import com.neostain.csms.infrastructure.persistence.jdbc.AuthServiceImpl;
import com.neostain.csms.presentation.controller.AuthController;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JPanel {
    private final AuthController authController;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;

    // Thêm
    public LoginScreen() {
        this.authController = new AuthController();
        // Khởi tạo component
        this.usernameField = new JTextField(20);
        this.passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Đăng nhập");
        this.statusLabel = new JLabel("Vui lòng đăng nhập");

        // Sử dụng BoxLayout dọc cho giao diện đơn giản
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel cho username
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("Tên đăng nhập:"));
        userPanel.add(usernameField);

        // Panel cho password
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(new JLabel("Mật khẩu:"));
        passPanel.add(passwordField);

        // Panel cho nút đăng nhập
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        // Thêm các panel vào giao diện chính
        this.add(userPanel);
        this.add(passPanel);
        this.add(buttonPanel);
        this.add(statusLabel);

        // Gắn xử lý sự kiện
        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = this.usernameField.getText();
        String password = new String(this.passwordField.getPassword());

        try {
            if (this.authController.login(username, password)) {
                this.statusLabel.setText("Đăng nhập thành công!");
                this.statusLabel.setForeground(Color.GREEN);
                
                // Display the token in the welcome message
                String token = this.authController.getCurrentToken();
                this.showWelcomeMessage(username, token);
            } else {
                this.statusLabel.setText("Sai tên đăng nhập hoặc mật khẩu");
                this.statusLabel.setForeground(Color.RED);
            }
        } catch (Exception ex) {
            this.statusLabel.setText("Lỗi: " + ex.getMessage());
            this.statusLabel.setForeground(Color.RED);
        }
    }

    private void showWelcomeMessage(String username) {
        showWelcomeMessage(username, null);
    }
    
    private void showWelcomeMessage(String username, String token) {
        boolean isAdmin = this.authController.checkAccess(username, "Admin");
        String message = "Chào mừng, " + username + "!\nQuyền Admin: " + isAdmin;
        
        if (token != null) {
            message += "\nToken được tạo thành công (hết hạn sau 5 phút): " + token;
        }
        
        JOptionPane.showMessageDialog(this,
                message,
                "Đăng nhập thành công",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
