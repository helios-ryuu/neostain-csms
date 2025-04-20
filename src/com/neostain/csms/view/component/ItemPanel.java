package com.neostain.csms.view.component;

import com.neostain.csms.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemPanel extends JPanel {
    private final String text;

    public ItemPanel(String text) {
        this.text = text;
        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(200, 40));
        this.setPreferredSize(new Dimension(200, 40));
        this.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JLabel label = new JLabel(text);
        label.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.PLAIN, Constants.Font.DEFAULT_SIZE));
        this.add(label, BorderLayout.CENTER);

        // Thêm xử lý sự kiện hover
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Constants.Color.COMPONENT_BACKGROUND_SELECTED);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Xử lý khi click vào menu item
                System.out.println("Clicked: " + text);
            }
        });
    }

    public void setSelected(boolean b) {
        if (b) {
            this.setBackground(Constants.Color.COMPONENT_BACKGROUND_SELECTED);
        } else {
            this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        }
    }

    public String getText() {
        return text;
    }
}
