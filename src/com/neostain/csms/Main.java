package com.neostain.csms;

import com.neostain.csms.view.MainFrame;

import javax.swing.*;

/**
 * Main application entry point
 */
public class Main {
    public static void main(String[] args) {
        // Start application in Swing thread
        SwingUtilities.invokeLater(MainFrame::new);
    }
}