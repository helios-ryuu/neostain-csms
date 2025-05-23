package com.neostain.csms.view.component;

import com.neostain.csms.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Standardized menu panels component used across the application
 * Provides consistent styling and behavior for menu panels
 */
public class StandardMenu extends BorderedPanel {

    private final List<ItemPanel> menuItems = new ArrayList<>();
    private final JPanel menuItemsContainer;
    private ItemPanel selectedItem = null;

    /**
     * Creates a standard menu panels with the specified title
     *
     * @param title Panel title
     */
    public StandardMenu(String title) {
        super(title);

        // Configure the panels với preferred và minimum size
        this.setPreferredSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 0));
        this.setMinimumSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 0));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create a container for menu items with BoxLayout
        menuItemsContainer = new JPanel();
        menuItemsContainer.setLayout(new BoxLayout(menuItemsContainer, BoxLayout.Y_AXIS));
        menuItemsContainer.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        menuItemsContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create a footer panels for logout button
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the main content panels that includes menu items and footer
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        contentPanel.add(menuItemsContainer, BorderLayout.CENTER);
        contentPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add scroll support
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add to the panels
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adds a menu item with the specified text and action
     *
     * @param text   Menu item text
     * @param action Action to perform when clicked
     */
    public void addMenuItem(String text, Consumer<ItemPanel> action) {
        ItemPanel menuItem = new ItemPanel(text);

        // Set up the click handler
        menuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Deselect a previously selected item
                if (selectedItem != null) {
                    selectedItem.setSelected(false);
                }

                // Select this item and execute the action
                menuItem.setSelected(true);
                selectedItem = menuItem;

                if (action != null) {
                    action.accept(menuItem);
                }
            }
        });

        // Add to the list and container
        menuItem.setMaximumSize(new Dimension(
                Constants.View.MENU_PANEL_WIDTH - 40, // Để lại 10px margin mỗi bên
                menuItem.getPreferredSize().height));

        menuItems.add(menuItem);
        menuItemsContainer.add(menuItem);

        // Add some vertical space
        menuItemsContainer.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    /**
     * Adds multiple menu items with their actions
     *
     * @param itemsWithActions Array of menu item texts and actions
     */
    public void addMenuItems(Object[][] itemsWithActions) {
        for (Object[] item : itemsWithActions) {
            String text = (String) item[0];
            @SuppressWarnings("unchecked")
            Consumer<ItemPanel> action = (Consumer<ItemPanel>) item[1];
            addMenuItem(text, action);
        }

        // Add vertical glue to push items to the top
        menuItemsContainer.add(Box.createVerticalGlue());
    }

    /**
     * Returns the list of menu items in this panels
     *
     * @return List of menu items
     */
    public List<ItemPanel> getMenuItems() {
        return new ArrayList<>(menuItems);
    }

    /**
     * Selects a menu item by its index
     *
     * @param index Index of the menu item to select
     */
    public void selectMenuItem(int index) {
        if (index >= 0 && index < menuItems.size()) {
            ItemPanel item = menuItems.get(index);

            // Deselect previous
            if (selectedItem != null) {
                selectedItem.setSelected(false);
            }

            // Select new item
            item.setSelected(true);
            selectedItem = item;
        }
    }

    /**
     * Clears all menu items from the panels
     */
    public void clearMenuItems() {
        menuItems.clear();
        menuItemsContainer.removeAll();
        selectedItem = null;
        menuItemsContainer.add(Box.createVerticalGlue());
        menuItemsContainer.revalidate();
        menuItemsContainer.repaint();
    }

    public static class ItemPanel extends JPanel {
        private final String text;

        public ItemPanel(String text) {
            this.text = text;
            this.setLayout(new BorderLayout());
            this.setMaximumSize(new Dimension(200, 40));
            this.setPreferredSize(new Dimension(200, 40));
            this.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

            JLabel label = new JLabel(text);
            label.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.PLAIN, Constants.Font.DEFAULT_SIZE));
            this.add(label, BorderLayout.CENTER);

            // Thêm xử lý sự kiện hover
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(Constants.Color.COMPONENT_BACKGROUND_SELECTED);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    // Xử lý khi click vào menu item
                    System.out.println("Clicked: " + text);
                }
            });
        }

        public void setSelected(boolean b) {
            if (b) {
                this.setBackground(Constants.Color.COMPONENT_BACKGROUND_SELECTED);
            } else {
                this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
            }
        }

        public String getText() {
            return text;
        }
    }
}
