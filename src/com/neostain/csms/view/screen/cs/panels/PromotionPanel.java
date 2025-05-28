package com.neostain.csms.view.screen.cs.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Promotion;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.ScrollableTable;
import com.neostain.csms.view.component.StandardButton;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PromotionPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final String[] statusOptions = {"TẤT CẢ TÌNH TRẠNG", "ĐANG HIỆU LỰC", "KHÔNG CÒN HIỆU LỰC"};
    private final String[] promoColumns = {"Mã khuyến mãi", "Tên khuyến mãi", "Bắt đầu", "Kết thúc", "Mã SP KM", "SL tối thiểu", "Mã SP tặng", "SL tặng", "Giảm giá (%)"};
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private final POSPanel posPanel;
    private final Runnable switchToPOSTab;
    private ScrollableTable promoTable;
    private boolean addToCartEnabled = true;

    public PromotionPanel(POSPanel posPanel, Runnable switchToPOSTab) {
        this.posPanel = posPanel;
        this.switchToPOSTab = switchToPOSTab;
        initializeComponents();
        javax.swing.Timer timer = new javax.swing.Timer(500, e -> updateAddToCartEnabled());
        timer.start();
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toolbar
        BorderedPanel toolWrapper = new BorderedPanel("Tìm kiếm khuyến mãi");
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.X_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField promoIdField = new JTextField(10);
        JTextField productIdField = new JTextField(10);
        JTextField promoProductIdField = new JTextField(10);

        Date defaultFrom = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date defaultTo = new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();
        SpinnerDateModel fromModel = new SpinnerDateModel(defaultFrom, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateFromSpinner = new JSpinner(fromModel);
        dateFromSpinner.setEditor(new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd"));
        SpinnerDateModel toModel = new SpinnerDateModel(defaultTo, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateToSpinner = new JSpinner(toModel);
        dateToSpinner.setEditor(new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd"));

        JComboBox<String> statusBox = new JComboBox<>(statusOptions);

        toolWrapper.add(new JLabel("Mã khuyến mãi:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(promoIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Thời gian: Từ"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(dateFromSpinner);
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(new JLabel("đến"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(dateToSpinner);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Mã SP KM:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(productIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Mã SP tặng:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(promoProductIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Tình trạng:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(statusBox);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(searchBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(resetBtn);

        searchBtn.addActionListener(e -> {
            String promoId = promoIdField.getText().trim();
            String productId = productIdField.getText().trim();
            String promoProductId = promoProductIdField.getText().trim();
            String status = statusOptions[statusBox.getSelectedIndex()];
            Date fromDate = (Date) dateFromSpinner.getValue();
            Date toDate = (Date) dateToSpinner.getValue();
            String from = (fromDate != null) ? df2.format(fromDate) : "";
            String to = (toDate != null) ? df2.format(toDate) : "";
            Timestamp now = new Timestamp(System.currentTimeMillis());
            List<Promotion> results = serviceManager.getSaleService().getAllPromotions();
            results = results.stream().filter(p ->
                    (promoId.isEmpty() || p.getId().equalsIgnoreCase(promoId)) &&
                            (productId.isEmpty() || (p.getProductId() != null && p.getProductId().equalsIgnoreCase(productId))) &&
                            (promoProductId.isEmpty() || (p.getPromoProductId() != null && p.getPromoProductId().equalsIgnoreCase(promoProductId))) &&
                            (from.isEmpty() || (p.getStartTime() != null && df2.format(p.getStartTime()).compareTo(from) >= 0)) &&
                            (to.isEmpty() || (p.getEndTime() != null && df2.format(p.getEndTime()).compareTo(to) <= 0)) &&
                            (status.equals("TẤT CẢ TÌNH TRẠNG") ||
                                    (status.equals("ĐANG HIỆU LỰC") && p.getStartTime() != null && p.getEndTime() != null && now.after(p.getStartTime()) && now.before(p.getEndTime())) ||
                                    (status.equals("KHÔNG CÒN HIỆU LỰC") && (p.getEndTime() == null || now.after(p.getEndTime())))
                            )
            ).toList();
            if (results.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Thông báo", "Không có kết quả tìm kiếm!");
            } else {
                String[][] data = toTableData(results);
                promoTable.refreshData(data);
            }
        });

        resetBtn.addActionListener(e -> {
            promoIdField.setText("");
            productIdField.setText("");
            promoProductIdField.setText("");
            statusBox.setSelectedIndex(0);
            dateFromSpinner.setValue(defaultFrom);
            dateToSpinner.setValue(defaultTo);
            searchBtn.doClick();
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        List<Promotion> init = serviceManager.getSaleService().getAllPromotions();
        String[][] initData = toTableData(init);
        this.promoTable = new ScrollableTable(
                promoColumns,
                initData,
                java.util.List.of(
                        new ScrollableTable.ActionDefinition(
                                "Thêm vào giỏ hàng",
                                "Thêm",
                                (row, tbl) -> {
                                    if (!addToCartEnabled) return;
                                    String promoId = (String) tbl.getValueAt(row, 0);
                                    Promotion promo = serviceManager.getSaleService().getAllPromotions().stream()
                                            .filter(p -> p.getId().equals(promoId)).findFirst().orElse(null);
                                    if (promo == null) {
                                        DialogFactory.showErrorDialog(this, "Lỗi", "Không tìm thấy khuyến mãi.");
                                        return;
                                    }
                                    if (promo.getProductId() == null || promo.getMinimumPurchaseQuantity() <= 0) {
                                        DialogFactory.showErrorDialog(this, "Lỗi", "Khuyến mãi không hợp lệ.");
                                        return;
                                    }
                                    posPanel.addProductToCart(promo.getProductId(), promo.getMinimumPurchaseQuantity());
                                    if (switchToPOSTab != null) switchToPOSTab.run();
                                    String sb = "Mã khuyến mãi: " + promo.getId() + "\n" +
                                            "Tên khuyến mãi: " + promo.getName() + "\n" +
                                            "Thời gian: " +
                                            (promo.getStartTime() != null ? df1.format(promo.getStartTime()) : "") +
                                            " đến " +
                                            (promo.getEndTime() != null ? df1.format(promo.getEndTime()) : "") + "\n" +
                                            "Mã SP KM: " + promo.getProductId() + "\n" +
                                            "Số lượng tối thiểu: " + promo.getMinimumPurchaseQuantity() + "\n" +
                                            "Mã SP tặng: " + (promo.getPromoProductId() != null ? promo.getPromoProductId() : "-") + "\n" +
                                            "Số lượng tặng: " + promo.getPromoProductQuantity() + "\n" +
                                            "Giảm giá: " + (promo.getDiscountRate() != null ? promo.getDiscountRate().multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString() : "0") + " %";
                                    DialogFactory.showInfoDialog(this, "Thêm khuyến mãi thành công", sb);
                                }
                        )
                )
        );

        BorderedPanel promoTableBordered = new BorderedPanel("Danh sách khuyến mãi");
        promoTableBordered.setLayout(new BorderLayout(10, 10));
        promoTableBordered.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        promoTableBordered.add(promoTable);
        this.add(promoTableBordered, BorderLayout.CENTER);
    }

    private String[][] toTableData(List<Promotion> list) {
        String[][] data = new String[list.size()][promoColumns.length];
        for (int i = 0; i < list.size(); i++) {
            Promotion p = list.get(i);
            data[i] = new String[]{
                    p.getId(),
                    p.getName(),
                    p.getStartTime() != null ? df1.format(p.getStartTime()) : "",
                    p.getEndTime() != null ? df1.format(p.getEndTime()) : "",
                    p.getProductId(),
                    String.valueOf(p.getMinimumPurchaseQuantity()),
                    p.getPromoProductId(),
                    String.valueOf(p.getPromoProductQuantity()),
                    p.getDiscountRate() != null ? (p.getDiscountRate().multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString()) : "0"
            };
        }
        return data;
    }

    private void updateAddToCartEnabled() {
        boolean locked = posPanel.isPointsLocked();
        if (locked == addToCartEnabled) {
            addToCartEnabled = !locked;
            promoTable.getTable().setEnabled(addToCartEnabled);
            promoTable.repaint();
        }
    }
}
