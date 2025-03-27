package com.neostain.csms;

import com.neostain.csms.view.MainFrame;

import javax.swing.*;

/// Điểm khởi đầu của ứng dụng
public class Main {

    /// Phương thức khởi đầu của ứng dụng
    ///
    /// @param args Các tham số dòng lệnh (không sử dụng)
    public static void main(String[] args) {

        // Khởi động ứng dụng
        SwingUtilities.invokeLater(MainFrame::new);
    }
}