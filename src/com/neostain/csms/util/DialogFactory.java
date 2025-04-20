package com.neostain.csms.util;

import javax.swing.*;
import java.awt.*;

public class DialogFactory {
    /**
     * Displays an error dialog with the specified message and title.
     * The dialog is created and shown in the Event Dispatch Thread.
     *
     * @param parent  the parent component for the dialog
     * @param message the error message to display
     * @param title   the title for the error dialog
     */
    public static void showErrorDialog(Component parent, String title, String message) {
        // Tìm Window (Frame) cha gốc từ component parent
        Window window = parent instanceof Window ?
                (Window) parent :
                SwingUtilities.getWindowAncestor(parent);

        JOptionPane.showMessageDialog(
                window,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Shows a confirmation dialog with the specified message and title.
     * The dialog is created and shown in the Event Dispatch Thread.
     *
     * @param parent  the parent component for the dialog
     * @param message the confirmation message to display
     * @param title   the title for the confirmation dialog
     */
    public static void showInfoDialog(Component parent, String message, String title) {
        // Tìm Window (Frame) cha gốc từ component parent
        Window window = parent instanceof Window ?
                (Window) parent :
                SwingUtilities.getWindowAncestor(parent);

        JOptionPane.showMessageDialog(
                window,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Shows a warning dialog with the specified message and title.
     * The dialog is created and shown in the Event Dispatch Thread.
     *
     * @param parent  the parent component for the dialog
     * @param message the warning message to display
     * @param title   the title for the warning dialog
     */
    public static void showWarningDialog(Component parent, String message, String title) {
        // Tìm Window (Frame) cha gốc từ component parent
        Window window = parent instanceof Window ?
                (Window) parent :
                SwingUtilities.getWindowAncestor(parent);

        JOptionPane.showMessageDialog(
                window,
                message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Shows a confirmation dialog with the specified message and title.
     * The dialog is created and shown in the Event Dispatch Thread.
     *
     * @param parent  the parent component for the dialog
     * @param message the confirmation message to display
     * @param title   the title for the confirmation dialog
     * @return true if the user confirmed the dialog, false otherwise
     */
    public static boolean showConfirmDialog(Component parent, String message, String title) {
        // Tìm Window (Frame) cha gốc từ component parent
        Window window = parent instanceof Window ?
                (Window) parent :
                SwingUtilities.getWindowAncestor(parent);

        // Hiển thị hộp thoại xác nhận và đợi kết quả
        int choice = JOptionPane.showConfirmDialog(
                window,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // Trả về true nếu người dùng chọn YES
        return choice == JOptionPane.YES_OPTION;
    }
}