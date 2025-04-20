package com.neostain.csms;

import com.neostain.csms.view.MainFrame;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Entry point of the application.
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Main method that starts the application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
