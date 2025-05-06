package com.neostain.csms.view.component;

import com.neostain.csms.util.Constants;

import javax.swing.*;
import java.awt.*;

public class StandardButton extends JButton {
    public Component parent;

    public StandardButton(Component component, String text) {
        this.parent = component;
        this.setText(text);
        this.setFocusPainted(false);
        this.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.PLAIN, Constants.Font.DEFAULT_SIZE));
        this.setFocusable(false);
    }
}
