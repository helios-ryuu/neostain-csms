package com.neostain.csms;

import com.neostain.csms.view.MainFrame;

import javax.swing.*;

/**
 * Entry point of the application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
