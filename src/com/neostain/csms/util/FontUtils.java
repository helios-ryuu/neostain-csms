package com.neostain.csms.util;

import javax.swing.*;
import java.awt.*;

public class FontUtils {
    public static void setFont(String fontName) {
        try {
            // Đặt look and feel của ứng dụng
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Đặt font
            Font defaultFont = new Font(fontName, Font.PLAIN, 11);
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

        return new Font(fontName, Font.PLAIN, 11);
    }
}
