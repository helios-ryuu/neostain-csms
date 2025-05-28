package com.neostain.csms.view.component;

import com.neostain.csms.util.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BorderedPanel extends JPanel {
    public BorderedPanel(String title) {
        this.setLayout(new BorderLayout(5, 5));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220), 1);
        Border marginBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        TitledBorder titledBorder = new TitledBorder(
                lineBorder,
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION
        );

        titledBorder.setTitleFont(Constants.View.BOLD_FONT);

        this.setBorder(BorderFactory.createCompoundBorder(titledBorder, marginBorder));
    }

    public void setTitle(String title) {
        Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220), 1);
        Border marginBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        TitledBorder titledBorder = new TitledBorder(
                lineBorder,
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION
        );
        this.setBorder(BorderFactory.createCompoundBorder(titledBorder, marginBorder));
        this.revalidate();
        this.repaint();
    }
}
