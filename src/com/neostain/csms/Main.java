package com.neostain.csms;

import com.neostain.csms.view.MainFrame;

import javax.swing.*;

/**
 * Điểm đầu vào của ứng dụng
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
