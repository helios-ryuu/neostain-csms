package com.neostain.csms.view.component;

import com.neostain.csms.util.Constants;

import javax.swing.*;
import java.awt.*;

public class DashboardTile extends JPanel {
    private final JLabel valueLabel;

    public DashboardTile(String title, String value, Color color) {
        this.setLayout(new BorderLayout(5, 5));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, color),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.PLAIN, 12));

        valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.BOLD, 20));
        valueLabel.setForeground(color);

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(valueLabel, BorderLayout.CENTER);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }
}
