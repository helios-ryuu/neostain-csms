package com.neostain.csms.view.component;

import com.neostain.csms.ViewManager;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class LogoutButton extends StandardButton {
    private static final Logger LOGGER = Logger.getLogger(LogoutButton.class.getName());

    public LogoutButton(Component parent, String text) {
        super(parent, text);
        this.parent = parent;
        this.setText(text);
        this.setFocusPainted(false);
        this.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.PLAIN, Constants.Font.DEFAULT_SIZE));
        this.setFocusable(false);

        this.addActionListener(e -> event());
    }

    public void event() {
        boolean choice = DialogFactory.showConfirmYesNoDialog(
                this.parent,
                "Xác nhận đăng xuất",
                "Bạn có chắc chắn muốn đăng xuất khỏi hệ thống?"
        );
        if (choice) {
            try {
                // Get the current MainFrame instance instead of creating a new one
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this.parent);
                ViewManager.getInstance(mainFrame).logout();

            } catch (Exception e) {
                LOGGER.severe("Error during logout: " + e.getMessage());
                DialogFactory.showErrorDialog(
                        this.parent,
                        "Lỗi",
                        "Có lỗi xảy ra khi đăng xuất: " + e.getMessage()
                );
            }
        }
    }
}
