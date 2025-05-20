package com.neostain.csms.view.screen.cs.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Product;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.ScrollableTable;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PromotionProductPanel extends BorderedPanel {
    private final String[] columns = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng"};
    private final Map<String, PromoEntry> promoEntries = new LinkedHashMap<>();
    private final ServiceManager serviceManager = ServiceManager.getInstance();
    private final ScrollableTable table;
    private boolean deleteEnabled = true;
    private Runnable onPromoChanged;

    public PromotionProductPanel() {
        super("Sản phẩm khuyến mãi");
        this.setPreferredSize(new Dimension(0, 200));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        this.setMinimumSize(new Dimension(0, 200));
        this.setLayout(new BorderLayout(5, 5));
        List<ScrollableTable.ActionDefinition> actions = List.of(
                new ScrollableTable.ActionDefinition("Xóa", "Xóa", this::onDeletePromoProduct)
        );
        table = new ScrollableTable(columns, new Object[0][columns.length], actions);
        this.add(table, BorderLayout.CENTER);
    }

    public void addPromoProduct(Product product, int quantity) {
        promoEntries.compute(product.getId(), (k, v) -> v == null ? new PromoEntry(product, quantity) : new PromoEntry(product, v.quantity + quantity));
        refreshTable();
        if (onPromoChanged != null) onPromoChanged.run();
    }

    public void removePromoProduct(String productId, int quantity) {
        promoEntries.computeIfPresent(productId, (k, v) -> {
            int newQty = v.quantity - quantity;
            return newQty > 0 ? new PromoEntry(v.product, newQty) : null;
        });
        refreshTable();
        if (onPromoChanged != null) onPromoChanged.run();
    }

    public void clearAll() {
        promoEntries.clear();
        refreshTable();
        if (onPromoChanged != null) onPromoChanged.run();
    }

    public int getPromoProductQuantity(String productId) {
        PromoEntry entry = promoEntries.get(productId);
        return entry != null ? entry.quantity : 0;
    }

    private void refreshTable() {
        Object[][] data = new Object[promoEntries.size()][columns.length];
        int i = 0;
        for (PromoEntry entry : promoEntries.values()) {
            data[i][0] = entry.product.getId();
            data[i][1] = entry.product.getName();
            data[i][2] = entry.quantity;
            i++;
        }
        table.refreshData(data);
    }

    public void setDeleteEnabled(boolean enabled) {
        this.deleteEnabled = enabled;
        table.getTable().setEnabled(true); // Table itself stays enabled for scrolling
        table.repaint();
    }

    private void onDeletePromoProduct(int row, JTable tbl) {
        if (!deleteEnabled) return;
        String productId = tbl.getValueAt(row, 0).toString();
        promoEntries.remove(productId);
        refreshTable();
        if (onPromoChanged != null) onPromoChanged.run();
    }

    public void setOnPromoChanged(Runnable r) {
        this.onPromoChanged = r;
    }

    public Map<String, PromoEntry> getPromoEntries() {
        return Collections.unmodifiableMap(promoEntries);
    }

    public record PromoEntry(Product product, int quantity) {
    }
} 