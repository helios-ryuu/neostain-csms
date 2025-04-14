package com.neostain.csms.view.component;

import com.neostain.csms.util.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuItemPanel extends JPanel {
    public MenuItemPanel(String text) {
        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(200, 40));
        this.setPreferredSize(new Dimension(200, 40));
        this.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        this.setBackground(ColorUtils.componentBackgroundWhite);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        this.add(label, BorderLayout.CENTER);

        // Thêm xử lý sự kiện hover
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(220, 220, 220));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(ColorUtils.componentBackgroundWhite);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Xử lý khi click vào menu item
                System.out.println("Clicked: " + text);
            }
        });
    }
}
