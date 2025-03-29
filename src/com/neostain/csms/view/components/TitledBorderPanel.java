package com.neostain.csms.view.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TitledBorderPanel extends JPanel {

    public TitledBorderPanel(String title) {
        this.setLayout(new BorderLayout(5, 5));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(new Color(249, 249, 249));

        Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220), 1);
        Border marginBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        TitledBorder titledBorder = new TitledBorder(
                lineBorder,
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION
        );

        this.setBorder(BorderFactory.createCompoundBorder(titledBorder, marginBorder));
    }
}
