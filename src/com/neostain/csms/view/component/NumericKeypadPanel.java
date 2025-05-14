package com.neostain.csms.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumericKeypadPanel extends JPanel {
    private final JTextField targetField;

    public NumericKeypadPanel(JTextField targetField) {
        this.targetField = targetField;
        initializeKeypad();
    }

    private void initializeKeypad() {
        // 4 hàng, 3 cột: [1 2 3], [4 5 6], [7 8 9], [Clear 0 <-]
        this.setLayout(new GridLayout(4, 3, 5, 5));
        this.setBackground(Color.WHITE);

        // Tạo các nút số 1–9
        for (int i = 1; i <= 9; i++) {
            this.add(createNumberButton(String.valueOf(i)));
        }

        // Nút Clear
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> targetField.setText(""));
        this.add(btnClear);

        // Nút số 0
        this.add(createNumberButton("0"));

        // Nút Delete (Xóa một ký tự cuối)
        JButton btnDelete = new JButton("<-");
        btnDelete.addActionListener(e -> {
            String text = targetField.getText();
            if (!text.isEmpty()) {
                targetField.setText(text.substring(0, text.length() - 1));
            }
        });
        this.add(btnDelete);
    }

    private JButton createNumberButton(String digit) {
        JButton btn = new JButton(digit);
        btn.addActionListener(new NumberButtonListener());
        return btn;
    }

    // ActionListener cho các nút số
    private class NumberButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            targetField.setText(targetField.getText() + src.getText());
        }
    }
}
