package com.neostain.csms.view;

import com.neostain.csms.service.ServiceManager;
import com.neostain.csms.view.screen.AuthorizeScreen;
import com.neostain.csms.view.screen.POSScreen;

import javax.swing.*;
import java.awt.*;

/// Cửa sổ chính của ứng dụng, quản lý hiển thị các màn hình khác nhau
public class MainFrame extends JFrame {

    /// Khởi tạo cửa sổ chính với các thiết lập cơ bản
    public MainFrame() {
        // Đặt tiêu đề cửa sổ
        super("NeoStain Convenience Store Management System");

        // Thêm shutdown hook để cập nhật trạng thái token khi đóng ứng dụng
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                String currentToken = ServiceManager.getInstance().getCurrentTokenValue();
                if (currentToken != null && !currentToken.isEmpty()) {
                    ServiceManager.getInstance().getTokenService().updateTokenStatus(currentToken, "02");
                    System.out.println("[MAIN] Đã cập nhật trạng thái token thành '02' khi đóng ứng dụng");
                } else {
                    System.out.println("[MAIN] Token trống. Không hủy Token.");
                }
            } catch (Exception ex) {
                System.out.println("[MAIN] Lỗi khi cập nhật trạng thái token: " + ex.getMessage());
            }
        }));

        try {
            // Đặt look and feel của ứng dụng
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Đặt tỷ lệ UI
            System.setProperty("sun.java2d.uiScale", "1.0");
            System.setProperty("sun.java2d.dpiaware", "true");

            // Lấy danh sách các font đã cài đặt
            Font defaultFont = getFont("Segoe UI");
            UIManager.put("Button.font", defaultFont);
            UIManager.put("Label.font", defaultFont);
            UIManager.put("TextField.font", defaultFont);
            UIManager.put("TextArea.font", defaultFont);
            UIManager.put("ComboBox.font", defaultFont);
            UIManager.put("Table.font", defaultFont);
            UIManager.put("TableHeader.font", defaultFont);
            UIManager.put("Menu.font", defaultFont);
            UIManager.put("MenuItem.font", defaultFont);
            UIManager.put("TabbedPane.font", defaultFont);
            UIManager.put("List.font", defaultFont);


        } catch (Exception e) {
            System.out.println("[MAIN] Lỗi: " + e.getMessage());
        }

        // Thiết lập cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // Hiển thị màn hình đăng nhập đầu tiên
        this.showAuthorizeScreen();
        this.setVisible(true);
    }

    private static Font getFont(String fontName) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();

        boolean isFontAvailable = false;

        // Kiểm tra sự tồn tại của JetBrains Mono
        for (String font : fontFamilies) {
            if (font.equalsIgnoreCase(fontName)) {
                isFontAvailable = true;
                break;
            }
        }

        // Nếu không có, chuyển sang fallback font "Segoe UI"
        if (!isFontAvailable) {
            fontName = "Segoe UI";
        }

        return new Font(fontName, Font.PLAIN, 12);
    }

    /// Chuyển đến màn hình đăng nhập. Tạo và hiển thị một màn hình đăng nhập mới
    public void showAuthorizeScreen() {
        // Tạo màn hình đăng nhập
        AuthorizeScreen authorizeScreen = new AuthorizeScreen();

        // Hiển thị màn hình đăng nhập
        this.setContentPane(authorizeScreen);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }

    /// Chuyển đến màn hình POS
    ///
    /// @param username Tên người dùng đã đăng nhập
    public void showPOSScreen(String username) {
        // Tạo màn hình POS
        POSScreen posScreen = new POSScreen(username);

        // Hiển thị màn hình POS
        this.setContentPane(posScreen);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
        this.revalidate();
        this.repaint();
    }
}