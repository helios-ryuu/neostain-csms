package com.neostain.csms;

import com.neostain.csms.view.MainFrame;

import javax.swing.*;

// Điểm khởi đầu của ứng dụng
public class Main {
    public static void main(String[] args) {
        // Khởi động ứng dụng trong luồng Swing
        SwingUtilities.invokeLater(MainFrame::new);
    }
}