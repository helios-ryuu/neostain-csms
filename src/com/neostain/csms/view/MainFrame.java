package com.neostain.csms.view;

import com.neostain.csms.view.screen.LoginScreen;
import com.neostain.csms.view.screen.POSScreen;

import javax.swing.*;
import java.awt.*;

/**
 * Cửa sổ chính của ứng dụng, quản lý hiển thị các màn hình khác nhau
 */
public class MainFrame extends JFrame {
    /**
     * Khởi tạo cửa sổ chính với các thiết lập cơ bản
     */
    public MainFrame() {
        // Đặt tiêu đề cửa sổ
        super("NeoStain Convenience Store Management System");

        // Thiết lập cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // Hiển thị màn hình đăng nhập đầu tiên
        this.showLoginScreen();
        this.setVisible(true);
    }

    /**
     * Chuyển đến màn hình đăng nhập
     * Tạo và hiển thị một màn hình đăng nhập mới
     */
    public void showLoginScreen() {
        // Tạo màn hình đăng nhập
        LoginScreen loginScreen = new LoginScreen();

        // Hiển thị màn hình đăng nhập
        this.setContentPane(loginScreen);
        this.setSize(800, 400);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(500, 300));
        this.revalidate();
        this.repaint();
    }

    /**
     * Chuyển đến màn hình POS
     *
     * @param username Tên người dùng đã đăng nhập
     */
    public void showPOSScreen(String username) {
        // Tạo màn hình POS
        POSScreen posScreen = new POSScreen(username);

        // Hiển thị màn hình POS
        this.setContentPane(posScreen);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.revalidate();
        this.repaint();
    }
}