package com.neostain.csms.view.component;

import com.neostain.csms.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;

public class PaginationPanel extends JPanel {
    private final JLabel pageInfoLabel;
    private final JButton prevButton;
    private final JButton nextButton;
    private final JComboBox<Integer> pageSizeSelector;
    private int currentPage = 1;
    private int totalPages = 1;
    private int pageSize = Constants.View.DEFAULT_PAGE_SIZE;
    private int totalItems = 0;
    private Consumer<Integer> pageChangeListener;
    private Consumer<Integer> pageSizeChangeListener;

    public PaginationPanel() {
        this(Constants.View.DEFAULT_PAGE_SIZE, 0);
    }

    /**
     * Creates a new pagination panel with specified page size and total items
     *
     * @param pageSize   Number of items per page
     * @param totalItems Total number of items
     */
    public PaginationPanel(int pageSize, int totalItems) {
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = calculateTotalPages();

        // Configure panel
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create page navigation buttons
        prevButton = new JButton(Constants.View.PAGINATION_PREV);
        prevButton.setEnabled(false); // Disabled by default on the first page
        prevButton.addActionListener(e -> goToPreviousPage());

        nextButton = new JButton(Constants.View.PAGINATION_NEXT);
        nextButton.setEnabled(totalPages > 1); // Enabled only if more than one page
        nextButton.addActionListener(e -> goToNextPage());

        // Create a page info label
        pageInfoLabel = new JLabel(formatPageInfo());

        // Create a page size selector
        JLabel pageSizeLabel = new JLabel("Items per page:");
        Integer[] pageSizeOptions = {5, 10, 20, 50, 100};
        pageSizeSelector = new JComboBox<>(pageSizeOptions);
        pageSizeSelector.setSelectedItem(pageSize);
        pageSizeSelector.addActionListener(e -> {
            try {
                int newPageSize = Integer.parseInt((String) Objects.requireNonNull(pageSizeSelector.getSelectedItem()));
                if (newPageSize != pageSize) {
                    setPageSize(newPageSize);
                }
            } catch (NullPointerException | NumberFormatException ex) {
                System.err.println("Invalid page size: " + ex.getMessage());
            }
        });

        // Add components to the panel
        add(prevButton);
        add(pageInfoLabel);
        add(nextButton);
        add(new JSeparator(JSeparator.VERTICAL));
        add(pageSizeLabel);
        add(pageSizeSelector);
    }

    /**
     * Goes to the previous page
     */
    public void goToPreviousPage() {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    }

    /**
     * Goes to the next page
     */
    public void goToNextPage() {
        if (currentPage < totalPages) {
            setCurrentPage(currentPage + 1);
        }
    }

    /**
     * Sets a listener for page change events
     *
     * @param listener Consumer that receives the new page number
     */
    public void setPageChangeListener(Consumer<Integer> listener) {
        this.pageChangeListener = listener;
    }

    /**
     * Sets a listener for page size change events
     *
     * @param listener Consumer that receives the new page size
     */
    public void setPageSizeChangeListener(Consumer<Integer> listener) {
        this.pageSizeChangeListener = listener;
    }

    /**
     * Gets the current page
     *
     * @return Current page number
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page
     *
     * @param page Page number to set
     */
    public void setCurrentPage(int page) {
        if (page >= 1 && page <= totalPages && page != currentPage) {
            int previousPage = currentPage;
            currentPage = page;
            updateControls();

            // Notify listener
            if (pageChangeListener != null) {
                pageChangeListener.accept(currentPage);
            }
        }
    }

    /**
     * Gets the total number of pages
     *
     * @return Total number of pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Gets the page size
     *
     * @return Number of items per page
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the page size
     *
     * @param pageSize Number of items per page
     */
    public void setPageSize(int pageSize) {
        if (pageSize > 0 && pageSize != this.pageSize) {
            this.pageSize = pageSize;
            int oldPage = currentPage;
            this.totalPages = calculateTotalPages();

            // Adjust the current page if needed
            if (currentPage > totalPages) {
                currentPage = Math.max(1, totalPages);
            }

            updateControls();

            // Notify listener
            if (pageSizeChangeListener != null) {
                pageSizeChangeListener.accept(pageSize);
            }

            // Notify page change listener if page changed
            if (oldPage != currentPage && pageChangeListener != null) {
                pageChangeListener.accept(currentPage);
            }
        }
    }

    /**
     * Gets the total number of items
     *
     * @return Total number of items
     */
    public int getTotalItems() {
        return totalItems;
    }

    /**
     * Sets the total number of items
     *
     * @param totalItems Total number of items
     */
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
        this.totalPages = calculateTotalPages();

        // Adjust the current page if needed
        if (currentPage > totalPages) {
            currentPage = Math.max(1, totalPages);
        }

        updateControls();
    }

    /**
     * Gets the start index for the current page
     *
     * @return Start index (0-based)
     */
    public int getPageStartIndex() {
        return (currentPage - 1) * pageSize;
    }

    /**
     * Gets the end index for the current page
     *
     * @return End index (0-based, exclusive)
     */
    public int getPageEndIndex() {
        return Math.min(getPageStartIndex() + pageSize, totalItems);
    }

    /**
     * Calculates the total number of pages
     *
     * @return Total number of pages
     */
    private int calculateTotalPages() {
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    /**
     * Updates the controls based on the current state
     */
    private void updateControls() {
        pageInfoLabel.setText(formatPageInfo());
        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage < totalPages);
    }

    /**
     * Formats the page info text
     *
     * @return Formatted page info text
     */
    private String formatPageInfo() {
        return String.format(Constants.View.PAGINATION_FORMAT, currentPage, Math.max(1, totalPages));
    }
}
