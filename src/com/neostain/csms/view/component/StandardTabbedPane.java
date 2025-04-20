package com.neostain.csms.view.component;

import com.neostain.csms.util.Constants;

import javax.swing.*;

/**
 * A standardized JTabbedPane component with consistent styling and behavior
 * This component ensures UI consistency across the application
 */
public class StandardTabbedPane extends JTabbedPane {

    /**
     * Default constructor that sets up standard styling
     */
    public StandardTabbedPane() {
        this.tabPlacement = JTabbedPane.TOP;
        initialize();
    }

    /**
     * Initialize the tabbed pane with standard styling
     */
    private void initialize() {
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setFocusable(false);

        // Set standard font for consistent look
        setFont(UIManager.getFont("TabbedPane.font"));

        // Ensure a proper background for consistency
        setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
    }
}
