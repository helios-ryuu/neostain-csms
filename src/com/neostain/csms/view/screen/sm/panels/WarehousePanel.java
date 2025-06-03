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
    private final String[] inventoryColumns = {"Sản phẩm", "Số lượng hiện tại", "Đơn giá", "Danh mục", "Lần điều chỉnh gần nhất"};
    private final String[] transactionColumns = {"Sản phẩm", "Loại giao dịch", "Số lượng giao dịch", "Thời gian giao dịch"};
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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

        // 1. Panel chính sử dụng GridBagLayout
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // 2. Tạo GridBagConstraints chung (sẽ tái sử dụng, chỉ thay đổi gridx và weightx)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1.0;
        gbc.gridy = 0;                 // duy nhất một hàng (row = 0)
        gbc.insets = new Insets(0, 0, 0, 10); // khoảng cách 10px giữa 2 panel

        // 3. Thêm createInventoryListPanel() - chiếm 70% chiều ngang
        gbc.gridx = 0;                 // cột 0
        gbc.weightx = 0.7;             // tỉ lệ 70%
        gbc.fill = GridBagConstraints.BOTH; // phóng to theo cả hai chiều
        JPanel inventoryPanel = createInventoryListPanel();
        bottomPanel.add(inventoryPanel, gbc);

        // 4. Thêm createTransactionListPanel() - chiếm 30% chiều ngang
        gbc.gridx = 1;                 // cột 1
        gbc.weightx = 0.3;             // tỉ lệ 30%
        gbc.insets = new Insets(0, 0, 0, 0); // không cần khoảng cách bên phải
        JPanel transactionPanel = createTransactionListPanel();
        bottomPanel.add(transactionPanel, gbc);
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
        JButton importNewBtn = new StandardButton(this, "Nhập hàng mới");
        JButton adjustBtn = new StandardButton(this, "Điều chỉnh");
        JButton createCategoryBtn = new StandardButton(this, "Tạo danh mục mới");

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
        panel.add(importNewBtn);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(adjustBtn);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(createCategoryBtn);

        searchBtn.addActionListener(e -> refreshInventoryTable(productIdField.getText().trim(), (Date) dateFromSpinner.getValue(), (Date) dateToSpinner.getValue()));
        resetBtn.addActionListener(e -> {
            productIdField.setText("");
            dateFromSpinner.setValue(new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime());
            dateToSpinner.setValue(new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            refreshInventoryTable("", defaultFrom, defaultTo);
        });
        importBtn.addActionListener(e -> showImportDialog());
        importNewBtn.addActionListener(e -> showImportNewProductDialog());
        adjustBtn.addActionListener(e -> showAdjustDialog());
        createCategoryBtn.addActionListener(e -> showCreateCategoryDialog());

        productIdField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });

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

        productIdField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });

        return panel;
    }

    private JPanel createInventoryListPanel() {
        BorderedPanel panel = new BorderedPanel("Danh sách tồn kho");
        panel.setLayout(new BorderLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        java.util.List<ScrollableTable.ActionDefinition> actions = java.util.List.of(
                new ScrollableTable.ActionDefinition("Điều chỉnh thông tin", "Điều chỉnh thông tin", this::onUpdateProductInfo)
        );
        inventoryTable = new ScrollableTable(inventoryColumns, new Object[0][inventoryColumns.length], actions);
        panel.add(inventoryTable, BorderLayout.CENTER);
        refreshInventoryTable("", new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
        return panel;
    }

    private void onUpdateProductInfo(int row, JTable tbl) {
        String prodDisplay = tbl.getValueAt(row, 0).toString();
        String productId = prodDisplay.split(" - ")[0];
        Product product = getProduct(productId);
        if (product == null) {
            DialogFactory.showErrorDialog(this, "Lỗi", "Không tìm thấy sản phẩm.");
            return;
        }
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Điều chỉnh thông tin sản phẩm", true);
        dialog.setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel prodLabel = new JLabel("Sản phẩm:");
        JTextField prodField = new JTextField(product.getId() + " - " + product.getName());
        prodField.setEditable(false);
        JLabel priceLabel = new JLabel("Đơn giá:");
        JFormattedTextField priceField = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
        priceField.setColumns(10);
        priceField.setValue(product.getUnitPrice().intValue());
        JLabel catLabel = new JLabel("Danh mục:");
        java.util.List<com.neostain.csms.model.Category> categories = serviceManager.getSaleService().getAllCategories();
        String[] catItems = new String[categories.size()];
        int selectedIdx = 0;
        for (int i = 0; i < categories.size(); i++) {
            catItems[i] = categories.get(i).getId() + " - " + categories.get(i).getName();
            if (categories.get(i).getId().equals(product.getCategoryId())) selectedIdx = i;
        }
        JComboBox<String> catBox = new JComboBox<>(catItems);
        catBox.setSelectedIndex(selectedIdx);
        formPanel.add(prodLabel);
        formPanel.add(prodField);
        formPanel.add(priceLabel);
        formPanel.add(priceField);
        formPanel.add(catLabel);
        formPanel.add(catBox);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton okBtn = new JButton("Cập nhật");
        JButton cancelBtn = new JButton("Hủy");
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        priceField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        cancelBtn.addActionListener(ev -> dialog.dispose());
        okBtn.addActionListener(ev -> {
            int price;
            try {
                price = Integer.parseInt(priceField.getText().replaceAll(",", "").trim());
            } catch (Exception ex) {
                price = 1;
            }
            if (price <= 0) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Đơn giá phải lớn hơn 0.");
                return;
            }
            int catIdx = catBox.getSelectedIndex();
            String newCatId = categories.get(catIdx).getId();
            boolean priceChanged = !product.getUnitPrice().equals(new java.math.BigDecimal(price));
            boolean catChanged = !product.getCategoryId().equals(newCatId);
            boolean ok = true;
            if (priceChanged) {
                ok = serviceManager.getSaleService().updateProductUnitPrice(productId, price);
            }
            if (ok && catChanged) {
                ok = serviceManager.getSaleService().updateProduct(new com.neostain.csms.model.Product(productId, product.getName(), new java.math.BigDecimal(price), newCatId, false));
            }
            if (!ok) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể cập nhật thông tin sản phẩm. Vui lòng thử lại.");
                return;
            }
            String info = "Mã sản phẩm: " + productId +
                    "\nTên sản phẩm: " + product.getName() +
                    "\nĐơn giá mới: " + price +
                    "\nDanh mục mới: " + catItems[catIdx];
            DialogFactory.showInfoDialog(dialog, "Cập nhật thông tin thành công", info);
            productCache.clear();
            refreshInventoryTable("", new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            dialog.dispose();
        });
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
            String unitPrice = p == null ? "" : (p.getUnitPrice() != null ? p.getUnitPrice().toPlainString() : "");
            String catDisplay = "";
            if (p != null) {
                com.neostain.csms.model.Category cat = serviceManager.getSaleService().getCategoryById(p.getCategoryId());
                catDisplay = cat != null ? (cat.getId() + " - " + cat.getName()) : p.getCategoryId();
            }
            rows.add(new Object[]{prodDisplay, inv.getQuantity(), unitPrice, catDisplay, df1.format(inv.getModificationTime())});
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

    private void showImportDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nhập hàng", true);
        dialog.setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel productIdLabel = new JLabel("Nhập mã sản phẩm:");
        JTextField productIdField = new JTextField(15);
        JLabel storeLabel = new JLabel("Cửa hàng:");
        JTextField storeField = new JTextField(currentStore.getId() + " - " + currentStore.getName());
        storeField.setEditable(false);
        JLabel typeLabel = new JLabel("Loại giao dịch:");
        JTextField typeField = new JTextField("NHẬP KHO");
        typeField.setEditable(false);
        JLabel qtyLabel = new JLabel("Số lượng:");
        JFormattedTextField qtyField = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
        qtyField.setColumns(10);
        qtyField.setValue(1);

        // Ngăn nhập ký tự không phải số (chỉ nhận 0-9)
        qtyField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        qtyField.addPropertyChangeListener("value", evt -> {
            Object v = qtyField.getValue();
            if (v == null || (v instanceof Number && ((Number) v).intValue() < 1)) qtyField.setValue(1);
        });

        formPanel.add(productIdLabel);
        formPanel.add(productIdField);
        formPanel.add(storeLabel);
        formPanel.add(storeField);
        formPanel.add(typeLabel);
        formPanel.add(typeField);
        formPanel.add(qtyLabel);
        formPanel.add(qtyField);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton okBtn = new JButton("Nhập hàng");
        JButton cancelBtn = new JButton("Hủy");
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);

        // Only allow digits in productIdField
        productIdField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str != null && str.matches("\\d+")) super.insertString(offs, str, a);
            }
        });
        // Only allow positive numbers in qtyField
        qtyField.addPropertyChangeListener("value", evt -> {
            Object v = qtyField.getValue();
            if (v == null || (v instanceof Number && ((Number) v).intValue() <= 0)) qtyField.setValue(1);
        });

        cancelBtn.addActionListener(ev -> dialog.dispose());
        okBtn.addActionListener(ev -> {
            String productId = productIdField.getText().trim();
            int qty;
            try {
                qty = Integer.parseInt(qtyField.getText().replaceAll(",", "").trim());
            } catch (Exception ex) {
                qty = 1;
            }
            if (productId.isEmpty()) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập mã sản phẩm.");
                return;
            }
            if (serviceManager.getSaleService().getProductById(productId) == null) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Mã sản phẩm không tồn tại.");
                return;
            }
            if (qty < 1) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Số lượng nhập phải lớn hơn hoặc bằng 1.");
                return;
            }
            InventoryTransaction tx = new InventoryTransaction();
            tx.setProductId(productId);
            tx.setStoreId(currentStore.getId());
            tx.setTransactionType("NHẬP KHO");
            tx.setQuantity(qty);
            boolean ok = serviceManager.getSaleService().createInventoryTransaction(tx);
            if (!ok) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể nhập hàng. Vui lòng thử lại.");
                return;
            }
            String info = "Mã sản phẩm: " + productId +
                    "\nCửa hàng: " + currentStore.getId() + " - " + currentStore.getName() +
                    "\nLoại giao dịch: NHẬP KHO" +
                    "\nSố lượng: " + qty;
            DialogFactory.showInfoDialog(dialog, "Nhập hàng thành công", info);
            refreshInventoryTable("", new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            dialog.dispose();
        });
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAdjustDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Điều chỉnh tồn kho", true);
        dialog.setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel productIdLabel = new JLabel("Nhập mã sản phẩm:");
        JTextField productIdField = new JTextField(15);
        JLabel storeLabel = new JLabel("Cửa hàng:");
        JTextField storeField = new JTextField(currentStore.getId() + " - " + currentStore.getName());
        storeField.setEditable(false);
        JLabel typeLabel = new JLabel("Loại giao dịch:");
        JTextField typeField = new JTextField("ĐIỀU CHỈNH");
        typeField.setEditable(false);
        JLabel qtyLabel = new JLabel("Số lượng:");
        JFormattedTextField qtyField = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
        qtyField.setColumns(10);
        qtyField.setValue(1);

        formPanel.add(productIdLabel);
        formPanel.add(productIdField);
        formPanel.add(storeLabel);
        formPanel.add(storeField);
        formPanel.add(typeLabel);
        formPanel.add(typeField);
        formPanel.add(qtyLabel);
        formPanel.add(qtyField);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton okBtn = new JButton("Điều chỉnh");
        JButton cancelBtn = new JButton("Hủy");
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);

        // Only allow digits in productIdField
        productIdField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str != null && str.matches("\\d+")) super.insertString(offs, str, a);
            }
        });
        // Allow negative/positive numbers in qtyField, but not zero
        qtyField.addPropertyChangeListener("value", evt -> {
            Object v = qtyField.getValue();
            if (v == null || (v instanceof Number && ((Number) v).intValue() == 0)) qtyField.setValue(1);
        });

        cancelBtn.addActionListener(ev -> dialog.dispose());
        okBtn.addActionListener(ev -> {
            String productId = productIdField.getText().trim();
            int qty;
            try {
                qty = Integer.parseInt(qtyField.getText().replaceAll(",", "").trim());
            } catch (Exception ex) {
                qty = 1;
            }
            if (productId.isEmpty()) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập mã sản phẩm.");
                return;
            }
            if (serviceManager.getSaleService().getProductById(productId) == null) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Mã sản phẩm không tồn tại.");
                return;
            }
            if (qty == 0) qty = 1;
            InventoryTransaction tx = new InventoryTransaction();
            tx.setProductId(productId);
            tx.setStoreId(currentStore.getId());
            tx.setTransactionType("ĐIỀU CHỈNH");
            tx.setQuantity(qty);
            boolean ok = serviceManager.getSaleService().createInventoryTransaction(tx);
            if (!ok) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể điều chỉnh tồn kho. Vui lòng thử lại.");
                return;
            }
            String info = "Mã sản phẩm: " + productId +
                    "\nCửa hàng: " + currentStore.getId() + " - " + currentStore.getName() +
                    "\nLoại giao dịch: ĐIỀU CHỈNH" +
                    "\nSố lượng: " + qty;
            DialogFactory.showInfoDialog(dialog, "Điều chỉnh thành công", info);
            refreshInventoryTable("", new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            dialog.dispose();
        });
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showImportNewProductDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nhập hàng mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel productIdLabel = new JLabel("Mã sản phẩm:");
        JTextField productIdField = new JTextField(15);
        JLabel nameLabel = new JLabel("Tên sản phẩm:");
        JTextField nameField = new JTextField(30);
        JLabel priceLabel = new JLabel("Đơn giá:");
        JFormattedTextField priceField = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
        priceField.setColumns(10);
        priceField.setValue(1);
        JLabel categoryLabel = new JLabel("Danh mục:");
        java.util.List<com.neostain.csms.model.Category> categories = serviceManager.getSaleService().getAllCategories();
        String[] categoryItems = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryItems[i] = categories.get(i).getId() + " - " + categories.get(i).getName();
        }
        JComboBox<String> categoryBox = new JComboBox<>(categoryItems);

        formPanel.add(productIdLabel);
        formPanel.add(productIdField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(priceLabel);
        formPanel.add(priceField);
        formPanel.add(categoryLabel);
        formPanel.add(categoryBox);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton okBtn = new JButton("Nhập hàng mới");
        JButton cancelBtn = new JButton("Hủy");
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);

        // Only allow exactly 13 digits in productIdField
        productIdField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str != null && str.matches("\\d+") && (getLength() + str.length() <= 13))
                    super.insertString(offs, str, a);
            }
        });
        // Only allow positive numbers in priceField
        priceField.addPropertyChangeListener("value", evt -> {
            Object v = priceField.getValue();
            if (v == null || (v instanceof Number && ((Number) v).intValue() <= 0)) priceField.setValue(1);
        });

        cancelBtn.addActionListener(ev -> dialog.dispose());
        okBtn.addActionListener(ev -> {
            String productId = productIdField.getText().trim();
            String name = nameField.getText().trim();
            int price;
            try {
                price = Integer.parseInt(priceField.getText().replaceAll(",", "").trim());
            } catch (Exception ex) {
                price = 1;
            }
            int catIdx = categoryBox.getSelectedIndex();
            String categoryId = catIdx >= 0 ? categories.get(catIdx).getId() : null;
            if (productId.isEmpty()) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập mã sản phẩm.");
                return;
            }
            if (!productId.matches("\\d{13}")) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Mã sản phẩm phải gồm đúng 13 ký tự số.");
                return;
            }
            if (name.isEmpty()) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập tên sản phẩm.");
                return;
            }
            if (price <= 0) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Đơn giá phải lớn hơn 0.");
                return;
            }
            if (categoryId == null) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng chọn danh mục.");
                return;
            }
            if (serviceManager.getSaleService().getProductById(productId) != null) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Mã sản phẩm đã tồn tại.");
                return;
            }
            com.neostain.csms.model.Product product = new com.neostain.csms.model.Product(productId, name, new java.math.BigDecimal(price), categoryId, false);
            boolean ok = serviceManager.getSaleService().createProduct(product);
            if (!ok) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo sản phẩm mới. Vui lòng thử lại.");
                return;
            }
            String info = "Mã sản phẩm: " + productId +
                    "\nTên sản phẩm: " + name +
                    "\nĐơn giá: " + price +
                    "\nDanh mục: " + categoryItems[catIdx];
            DialogFactory.showInfoDialog(dialog, "Nhập hàng mới thành công", info);
            refreshInventoryTable("", new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            dialog.dispose();
        });
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Hiển thị dialog tạo danh mục mới
     */
    private void showCreateCategoryDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo danh mục mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JLabel nameLabel = new JLabel("Nhập tên danh mục mới:");
        JTextField nameField = new JTextField(30);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton createBtn = new JButton("Tạo danh mục");
        JButton cancelBtn = new JButton("Hủy");
        btnPanel.add(createBtn);
        btnPanel.add(cancelBtn);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        // Action: Hủy
        cancelBtn.addActionListener(ev -> dialog.dispose());
        // Action: Tạo danh mục
        createBtn.addActionListener(ev -> {
            String name = nameField.getText().trim();
            if (com.neostain.csms.util.StringUtils.isNullOrEmpty(name)) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập tên danh mục.");
                return;
            }
            // Check duplicate (case-insensitive, not deleted)
            java.util.List<com.neostain.csms.model.Category> categories = serviceManager.getSaleService().getAllCategories();
            boolean exists = categories.stream().anyMatch(c -> !c.isDeleted() && c.getName().trim().equalsIgnoreCase(name));
            if (exists) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Tên danh mục đã tồn tại.");
                return;
            }
            // Create category (id=null for trigger, isDeleted=false)
            com.neostain.csms.model.Category newCat = new com.neostain.csms.model.Category(null, name, false);
            try {
                boolean ok = serviceManager.getSaleService().createCategory(newCat);
                if (!ok) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo danh mục mới. Vui lòng thử lại.");
                    return;
                }
                // Lấy lại danh mục vừa tạo (tìm theo tên, lấy bản ghi mới nhất)
                categories = serviceManager.getSaleService().getAllCategories();
                com.neostain.csms.model.Category created = categories.stream()
                        .filter(c -> !c.isDeleted() && c.getName().trim().equalsIgnoreCase(name))
                        .findFirst().orElse(null);
                String info = created == null ? ("Tên: " + name) : ("Mã: " + created.getId() + "\nTên: " + created.getName());
                DialogFactory.showInfoDialog(dialog, "Tạo danh mục thành công", info);
                dialog.dispose();
            } catch (com.neostain.csms.util.exception.DuplicateFieldException ex) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", "Tên danh mục đã tồn tại.");
            } catch (Exception ex) {
                DialogFactory.showErrorDialog(dialog, "Lỗi", ex.getMessage());
            }
        });
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
} 