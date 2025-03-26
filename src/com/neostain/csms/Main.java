package com.neostain.csms;

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
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

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