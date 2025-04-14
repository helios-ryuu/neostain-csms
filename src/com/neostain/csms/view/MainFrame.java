package com.neostain.csms.view;

import com.neostain.csms.service.ServiceManager;
import com.neostain.csms.util.FontUtils;
import com.neostain.csms.view.manager.ScreenManager;
import com.neostain.csms.view.screen.ScreenType;

import javax.swing.*;

/// Cửa sổ chính của ứng dụng, quản lý hiển thị các màn hình khác nhau
public class MainFrame extends JFrame {

    /// Khởi tạo cửa sổ chính với các thiết lập cơ bản
    public MainFrame() {

        // Đặt tiêu đề cửa sổ
        super("NeoStain Convenience Store Management System");

        // Đặt tỷ lệ UI
        System.setProperty("sun.java2d.uiScale", "1.0");
        System.setProperty("sun.java2d.dpiaware", "true");

        // Đặt Font
        FontUtils.setFont("Segoe UI");

        ScreenManager screenManager = ScreenManager.getInstance(this);
        screenManager.switchScreen(ScreenType.LOGIN);
        this.setVisible(true);

        // Thiết lập cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.setupShutdownHook();
    }

    private void setupShutdownHook() {
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
    }
}