package com.neostain.csms.view.screen.cs.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.*;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.ScrollableTable;
import com.neostain.csms.view.component.StandardButton;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final POSPanel posPanel;
    private final Runnable switchToPOSTab;
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final String[] columns = {"Mã sản phẩm", "Tên sản phẩm", "Tồn kho", "Đơn giá", "Danh mục"};
    private ScrollableTable table;
    private String currentStoreId;
    private List<Category> categories;
    private JComboBox<String> categoryBox;
    private JTextField productIdField;

    public ProductPanel(POSPanel posPanel, Runnable switchToPOSTab) {
        this.posPanel = posPanel;
        this.switchToPOSTab = switchToPOSTab;
        initializeComponents();
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Determine current store
        String currentShiftReportId = serviceManager.getCurrentShiftId();
        ShiftReport shiftReport = serviceManager.getOperationService().getShiftReportById(currentShiftReportId);
        Store store = serviceManager.getManagementService().getStoreById(shiftReport.getStoreId());
        this.currentStoreId = store.getId();

        // Toolbar
        BorderedPanel toolWrapper = new BorderedPanel("Tìm kiếm sản phẩm");
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.X_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        productIdField = new JTextField(12);
        categories = serviceManager.getSaleService().getAllCategories();
        String[] categoryItems = new String[categories.size() + 1];
        categoryItems[0] = "TẤT CẢ DANH MỤC";
        for (int i = 0; i < categories.size(); i++) {
            categoryItems[i + 1] = categories.get(i).getId() + " - " + categories.get(i).getName();
        }
        categoryBox = new JComboBox<>(categoryItems);

        toolWrapper.add(new JLabel("Mã sản phẩm:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(productIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Danh mục:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(categoryBox);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(searchBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(resetBtn);

        searchBtn.addActionListener(e -> searchProducts());
        resetBtn.addActionListener(e -> {
            productIdField.setText("");
            categoryBox.setSelectedIndex(0);
            searchProducts();
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        List<Object[]> initData = getInventoryTableData(null, null);
        table = new ScrollableTable(columns, initData.toArray(new Object[0][]), List.of(
                new ScrollableTable.ActionDefinition("Thêm vào giỏ hàng", "Thêm", this::onAddToCart)
        ));
        BorderedPanel tablePanel = new BorderedPanel("Danh sách tồn kho");
        tablePanel.setLayout(new BorderLayout(10, 10));
        tablePanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        tablePanel.add(table);
        this.add(tablePanel, BorderLayout.CENTER);
    }

    private void searchProducts() {
        String productId = productIdField.getText().trim();
        int catIdx = categoryBox.getSelectedIndex();
        String categoryId = (catIdx > 0) ? categories.get(catIdx - 1).getId() : null;
        List<Object[]> data = getInventoryTableData(productId, categoryId);
        table.refreshData(data.toArray(new Object[0][]));
    }

    private List<Object[]> getInventoryTableData(String productId, String categoryId) {
        List<Inventory> inventories = serviceManager.getSaleService().getInventoriesByStoreId(currentStoreId);
        List<Product> allProducts = serviceManager.getSaleService().getAllProducts();
        Map<String, Product> productMap = new HashMap<>();
        for (Product p : allProducts) productMap.put(p.getId(), p);
        Map<String, Category> categoryMap = new HashMap<>();
        for (Category c : categories) categoryMap.put(c.getId(), c);
        DecimalFormat decf = new DecimalFormat("#,###");
        List<Object[]> data = new ArrayList<>();
        for (Inventory inv : inventories) {
            Product p = productMap.get(inv.getProductId());
            if (p == null) continue;
            if (productId != null && !productId.isEmpty() && !p.getId().equalsIgnoreCase(productId)) continue;
            if (categoryId != null && !categoryId.isEmpty() && !categoryId.equals(p.getCategoryId())) continue;
            Category cat = categoryMap.get(p.getCategoryId());
            String catStr = cat != null ? cat.getId() + " - " + cat.getName() : "";
            data.add(new Object[]{
                    p.getId(),
                    p.getName(),
                    inv.getQuantity(),
                    p.getUnitPrice() != null ? decf.format(p.getUnitPrice()) + " VND" : "",
                    catStr
            });
        }
        return data;
    }

    private void onAddToCart(int row, JTable tbl) {
        String productId = (String) tbl.getValueAt(row, 0);
        String name = (String) tbl.getValueAt(row, 1);
        int stock = Integer.parseInt(tbl.getValueAt(row, 2).toString());
        String price = (String) tbl.getValueAt(row, 3);
        String category = (String) tbl.getValueAt(row, 4);
        if (stock <= 0) {
            DialogFactory.showWarningDialog(this, "Hết hàng", "Sản phẩm đã hết hàng trong kho.");
            return;
        }
        if (posPanel.isPointsLocked()) {
            DialogFactory.showWarningDialog(this, "Không thể thêm", "Không thể thêm sản phẩm khi đã sử dụng điểm. Hãy hủy tích điểm hoặc làm mới giỏ hàng để tiếp tục.");
            return;
        }
        // Show dialog for quantity input
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm sản phẩm vào giỏ hàng", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(new JLabel("Mã sản phẩm:"), gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField(productId);
        idField.setEditable(false);
        idField.setColumns(12);
        dialog.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        dialog.add(new JLabel("Số lượng tồn kho hiện tại:"), gbc);
        gbc.gridx = 1;
        JTextField stockField = new JTextField(String.valueOf(stock));
        stockField.setEditable(false);
        stockField.setColumns(6);
        dialog.add(stockField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        dialog.add(new JLabel("Nhập số lượng:"), gbc);
        gbc.gridx = 1;
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JTextField qtyField = new JTextField("1", 4);
        qtyField.setHorizontalAlignment(JTextField.RIGHT);
        JButton btnDec = new JButton("-");
        JButton btnInc = new JButton("+");
        btnDec.addActionListener(ev -> {
            int val = Integer.parseInt(qtyField.getText());
            if (val > 1) qtyField.setText(String.valueOf(val - 1));
        });
        btnInc.addActionListener(ev -> {
            int val = Integer.parseInt(qtyField.getText());
            if (val < stock) qtyField.setText(String.valueOf(val + 1));
        });
        qtyPanel.add(btnDec);
        qtyPanel.add(qtyField);
        qtyPanel.add(btnInc);
        dialog.add(qtyPanel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtn = new JButton("Thêm sản phẩm");
        JButton cancelBtn = new JButton("Hủy");
        btnPanel.add(addBtn);
        btnPanel.add(cancelBtn);
        dialog.add(btnPanel, gbc);
        addBtn.addActionListener(ev -> {
            int qty = Integer.parseInt(qtyField.getText());
            if (qty < 1 || qty > stock) {
                DialogFactory.showWarningDialog(dialog, "Số lượng không hợp lệ", "Vui lòng nhập số lượng từ 1 đến " + stock);
                return;
            }
            posPanel.addProductToCart(productId, qty);
            if (switchToPOSTab != null) switchToPOSTab.run();
            dialog.dispose();
            String sb = "Mã sản phẩm: " + productId + "\n" +
                    "Tên sản phẩm: " + name + "\n" +
                    "Số lượng vừa thêm: " + qty + "\n" +
                    "Đơn giá: " + price + "\n" +
                    "Danh mục: " + category;
            DialogFactory.showInfoDialog(this, "Thêm sản phẩm thành công", sb);
        });
        cancelBtn.addActionListener(ev -> dialog.dispose());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
