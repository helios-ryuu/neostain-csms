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
        this.setFont(Constants.View.DEFAULT_FONT);
        this.setFocusable(false);
    }
}
