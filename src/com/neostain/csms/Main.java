package com.neostain.csms;

import com.neostain.csms.service.ServiceManager;
import com.neostain.csms.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

// Điểm khởi đầu của ứng dụng
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /// Phương thức khởi đầu của ứng dụng
    ///
    /// @param args Các tham số dòng lệnh (không sử dụng)
    public static void main(String[] args) {
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

            // Thêm shutdown hook để cập nhật trạng thái token khi đóng ứng dụng
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown hook đang chạy...");
                try {
                    String currentToken = ServiceManager.getInstance().getCurrentTokenValue();
                    if (currentToken != null && !currentToken.isEmpty()) {
                        ServiceManager.getInstance().getTokenService().updateTokenStatus(currentToken, "02");
                        LOGGER.info("[MAIN] Đã cập nhật trạng thái token thành '02' khi đóng ứng dụng");
                    } else {
                        LOGGER.info("[MAIN] Token trống");
                    }
                } catch (Exception ex) {
                    LOGGER.severe("[MAIN] Lỗi khi cập nhật trạng thái token: " + ex.getMessage());
                }
            }));
        } catch (Exception e) {
            LOGGER.severe("[MAIN] Lỗi: " + e.getMessage());
        }

        // Khởi động ứng dụng
        SwingUtilities.invokeLater(MainFrame::new);
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
}