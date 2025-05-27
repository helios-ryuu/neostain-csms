package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Inventory;
import com.neostain.csms.model.InventoryTransaction;
import com.neostain.csms.model.Product;
import com.neostain.csms.model.Store;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.ScrollableTable;
import com.neostain.csms.view.component.StandardButton;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class WarehousePanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final String[] inventoryColumns = {"Sản phẩm", "Số lượng hiện tại", "Lần điều chỉnh gần nhất"};
    private final String[] transactionColumns = {"Sản phẩm", "Loại giao dịch", "Số lượng giao dịch", "Thời gian giao dịch"};
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private final String[] transactionTypes = {"TẤT CẢ", "NHẬP KHO", "BÁN HÀNG", "KHUYẾN MÃI", "ĐIỀU CHỈNH"};
    private final Store currentStore;
    private final Map<String, Product> productCache = new HashMap<>();
    private ScrollableTable inventoryTable;
    private ScrollableTable transactionTable;

    public WarehousePanel() {
        this.currentStore = getCurrentStore();
        initializeComponents();
    }

    private Store getCurrentStore() {
        String username = serviceManager.getCurrentUsername();
        String empId = serviceManager.getAuthService().getAccountByUsername(username).getEmployeeId();
        return serviceManager.getManagementService().getStoreByManagerId(empId);
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        topPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        topPanel.add(createInventorySearchPanel());
        topPanel.add(createTransactionSearchPanel());
        this.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        bottomPanel.add(createInventoryListPanel());
        bottomPanel.add(createTransactionListPanel());
        this.add(bottomPanel, BorderLayout.CENTER);
    }

    private JPanel createInventorySearchPanel() {
        BorderedPanel panel = new BorderedPanel("Tìm kiếm tồn kho");
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField productIdField = new JTextField(10);
        Date defaultFrom = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date defaultTo = new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();
        SpinnerDateModel fromModel = new SpinnerDateModel(defaultFrom, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateFromSpinner = new JSpinner(fromModel);
        dateFromSpinner.setEditor(new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd"));
        SpinnerDateModel toModel = new SpinnerDateModel(defaultTo, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateToSpinner = new JSpinner(toModel);
        dateToSpinner.setEditor(new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd"));
        JButton searchBtn = new StandardButton(this, "Tìm kiếm");
        JButton resetBtn = new StandardButton(this, "Đặt lại");
        JButton importBtn = new StandardButton(this, "Nhập hàng");
        JButton adjustBtn = new StandardButton(this, "Điều chỉnh");

        panel.add(new JLabel("Mã sản phẩm:"));
        panel.add(Box.createHorizontalStrut(8));
        panel.add(productIdField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Thời gian điều chỉnh: Từ"));
        panel.add(Box.createHorizontalStrut(8));
        panel.add(dateFromSpinner);
        panel.add(Box.createHorizontalStrut(8));
        panel.add(new JLabel("đến"));
        panel.add(Box.createHorizontalStrut(8));
        panel.add(dateToSpinner);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(searchBtn);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(resetBtn);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(importBtn);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(adjustBtn);

        searchBtn.addActionListener(e -> refreshInventoryTable(productIdField.getText().trim(), (Date) dateFromSpinner.getValue(), (Date) dateToSpinner.getValue()));
        resetBtn.addActionListener(e -> {
            productIdField.setText("");
            dateFromSpinner.setValue(new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime());
            dateToSpinner.setValue(new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            refreshInventoryTable("", defaultFrom, defaultTo);
        });
        importBtn.addActionListener(e -> DialogFactory.showInfoDialog(this, "Nhập hàng", "Chức năng đang phát triển..."));
        adjustBtn.addActionListener(e -> DialogFactory.showInfoDialog(this, "Điều chỉnh tồn kho", "Chức năng đang phát triển..."));

        return panel;
    }

    private JPanel createTransactionSearchPanel() {
        BorderedPanel panel = new BorderedPanel("Tìm kiếm giao dịch kho");
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField productIdField = new JTextField(10);
        JComboBox<String> typeCombo = new JComboBox<>(transactionTypes);
        Date defaultFrom = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date defaultTo = new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();
        SpinnerDateModel fromModel = new SpinnerDateModel(defaultFrom, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateFromSpinner = new JSpinner(fromModel);
        dateFromSpinner.setEditor(new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd"));
        SpinnerDateModel toModel = new SpinnerDateModel(defaultTo, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateToSpinner = new JSpinner(toModel);
        dateToSpinner.setEditor(new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd"));
        JButton searchBtn = new StandardButton(this, "Tìm kiếm");
        JButton resetBtn = new StandardButton(this, "Đặt lại");

        panel.add(new JLabel("Mã sản phẩm:"));
        panel.add(Box.createHorizontalStrut(8));
        panel.add(productIdField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Loại giao dịch:"));
        panel.add(Box.createHorizontalStrut(8));
        panel.add(typeCombo);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Thời gian giao dịch: Từ"));
        panel.add(Box.createHorizontalStrut(8));
        panel.add(dateFromSpinner);
        panel.add(Box.createHorizontalStrut(8));
        panel.add(new JLabel("đến"));
        panel.add(Box.createHorizontalStrut(8));
        panel.add(dateToSpinner);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(searchBtn);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(resetBtn);

        searchBtn.addActionListener(e -> refreshTransactionTable(productIdField.getText().trim(), (String) typeCombo.getSelectedItem(), (Date) dateFromSpinner.getValue(), (Date) dateToSpinner.getValue()));
        resetBtn.addActionListener(e -> {
            productIdField.setText("");
            typeCombo.setSelectedIndex(0);
            dateFromSpinner.setValue(new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime());
            dateToSpinner.setValue(new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            refreshTransactionTable("", "TẤT CẢ", defaultFrom, defaultTo);
        });

        return panel;
    }

    private JPanel createInventoryListPanel() {
        BorderedPanel panel = new BorderedPanel("Danh sách tồn kho");
        panel.setLayout(new BorderLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        inventoryTable = new ScrollableTable(inventoryColumns, new Object[0][inventoryColumns.length], List.of());
        panel.add(inventoryTable, BorderLayout.CENTER);
        refreshInventoryTable("", new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
        return panel;
    }

    private JPanel createTransactionListPanel() {
        BorderedPanel panel = new BorderedPanel("Danh sách giao dịch kho");
        panel.setLayout(new BorderLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        transactionTable = new ScrollableTable(transactionColumns, new Object[0][transactionColumns.length], List.of());
        panel.add(transactionTable, BorderLayout.CENTER);
        refreshTransactionTable("", "TẤT CẢ", new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
        return panel;
    }

    private void refreshInventoryTable(String productId, Date from, Date to) {
        List<Inventory> inventories = serviceManager.getSaleService().getInventoriesByStoreId(currentStore.getId());
        List<Object[]> rows = new ArrayList<>();
        for (Inventory inv : inventories) {
            if (!productId.isEmpty() && !inv.getProductId().equals(productId)) continue;
            if (from != null && inv.getModificationTime().before(from)) continue;
            if (to != null && inv.getModificationTime().after(to)) continue;
            Product p = getProduct(inv.getProductId());
            String prodDisplay = p == null ? inv.getProductId() : (p.getId() + " - " + p.getName());
            rows.add(new Object[]{prodDisplay, inv.getQuantity(), df1.format(inv.getModificationTime())});
        }
        inventoryTable.refreshData(rows.toArray(new Object[0][0]));
    }

    private void refreshTransactionTable(String productId, String type, Date from, Date to) {
        List<InventoryTransaction> txs = serviceManager.getSaleService().getInventoryTransactionsByStoreId(currentStore.getId());
        List<Object[]> rows = new ArrayList<>();
        for (InventoryTransaction tx : txs) {
            if (!productId.isEmpty() && !tx.getProductId().equals(productId)) continue;
            if (!"TẤT CẢ".equals(type) && !tx.getTransactionType().equals(type)) continue;
            if (from != null && tx.getCreationTime().before(from)) continue;
            if (to != null && tx.getCreationTime().after(to)) continue;
            Product p = getProduct(tx.getProductId());
            String prodDisplay = p == null ? tx.getProductId() : (p.getId() + " - " + p.getName());
            rows.add(new Object[]{prodDisplay, tx.getTransactionType(), tx.getQuantity(), df1.format(tx.getCreationTime())});
        }
        transactionTable.refreshData(rows.toArray(new Object[0][0]));
    }

    private Product getProduct(String productId) {
        if (productCache.containsKey(productId)) return productCache.get(productId);
        Product p = serviceManager.getSaleService().getProductById(productId);
        if (p != null) productCache.put(productId, p);
        return p;
    }
} 