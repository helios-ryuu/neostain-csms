package com.neostain.csms.view;

import com.neostain.csms.service.AuthService;

import javax.swing.*;

// Cửa sổ chính của ứng dụng
public class MainFrame extends JFrame {
    public MainFrame() {
        // Đặt tiêu đề cửa sổ
        super("CSMS - Convenience Store Management System");

        // Thiết lập cửa sổ
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Hiển thị màn hình đăng nhập đầu tiên
        showLoginScreen();
        setVisible(true);
    }

    // Chuyển đến màn hình đăng nhập
    public void showLoginScreen() {
        // Tạo dịch vụ xác thực
        AuthService authService = new AuthService();

        // Tạo màn hình đăng nhập
        LoginScreen loginScreen = new LoginScreen(authService);

        // Hiển thị màn hình đăng nhập
        setContentPane(loginScreen);
        revalidate();
        repaint();
    }
}