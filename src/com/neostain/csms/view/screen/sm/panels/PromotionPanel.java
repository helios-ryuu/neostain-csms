package com.neostain.csms.view.screen.sm.panels;

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
    private final JButton createBtn = new StandardButton(this, "Tạo khuyến mãi mới");
    private final String[] statusOptions = {"TẤT CẢ TÌNH TRẠNG", "ĐANG HIỆU LỰC", "KHÔNG CÒN HIỆU LỰC"};
    private final String[] promoColumns = {"Mã khuyến mãi", "Tên khuyến mãi", "Bắt đầu", "Kết thúc", "Mã SP KM", "SL tối thiểu", "Mã SP tặng", "SL tặng", "Giảm giá (%)"};
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private ScrollableTable promoTable;

    public PromotionPanel() {
        initializeComponents();
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
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(createBtn);

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
                String[][] data = this.toTableData(results);
                promoTable.refreshData(data);
            }
        });

        resetBtn.addActionListener(e -> {
            promoIdField.setText("");
            productIdField.setText("");
            promoProductIdField.setText("");
            statusBox.setSelectedIndex(0);
            dateFromSpinner.setValue(new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime());
            dateToSpinner.setValue(new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            searchBtn.doClick();
        });

        createBtn.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo khuyến mãi mới", true);
            dialog.setLayout(new BorderLayout(10, 10));
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

            // Fields
            JLabel nameLabel = new JLabel("Tên khuyến mãi:");
            JTextField nameField = new JTextField(20);

            JLabel startLabel = new JLabel("Thời gian bắt đầu:");
            SpinnerDateModel startModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
            JSpinner startSpinner = new JSpinner(startModel);
            startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd HH:mm:ss"));

            JLabel endLabel = new JLabel("Thời gian kết thúc:");
            SpinnerDateModel endModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
            JSpinner endSpinner = new JSpinner(endModel);
            endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd HH:mm:ss"));

            JLabel productIdLabel = new JLabel("Mã SP khuyến mãi:");
            JTextField productIdFieldx = new JTextField(15);

            JLabel minQtyLabel = new JLabel("Số lượng tối thiểu:");
            JFormattedTextField minQtyField = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
            minQtyField.setColumns(10);
            minQtyField.setValue(1);

            JLabel promoProductIdLabel = new JLabel("Mã SP tặng:");
            JTextField promoProductIdFieldx = new JTextField(15);

            JLabel promoQtyLabel = new JLabel("Số lượng tặng:");
            JFormattedTextField promoQtyField = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
            promoQtyField.setColumns(10);
            promoQtyField.setValue(0);

            JLabel discountLabel = new JLabel("Giảm giá (%):");
            JFormattedTextField discountField = new JFormattedTextField(java.text.NumberFormat.getNumberInstance());
            discountField.setColumns(10);
            discountField.setValue(0.0);

            // Add fields to form
            formPanel.add(nameLabel);
            formPanel.add(nameField);
            formPanel.add(startLabel);
            formPanel.add(startSpinner);
            formPanel.add(endLabel);
            formPanel.add(endSpinner);
            formPanel.add(productIdLabel);
            formPanel.add(productIdFieldx);
            formPanel.add(minQtyLabel);
            formPanel.add(minQtyField);
            formPanel.add(promoProductIdLabel);
            formPanel.add(promoProductIdFieldx);
            formPanel.add(promoQtyLabel);
            formPanel.add(promoQtyField);
            formPanel.add(discountLabel);
            formPanel.add(discountField);

            mainPanel.add(formPanel, BorderLayout.CENTER);
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton okBtn = new JButton("Tạo khuyến mãi");
            JButton cancelBtn = new JButton("Hủy");
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            dialog.add(mainPanel, BorderLayout.CENTER);

            // Logic: enable/disable promoQtyField and promoProductIdFieldx based on productIdFieldx
            Runnable updatePromoFieldsCreate = () -> {
                boolean hasPromoProduct = !productIdFieldx.getText().trim().isEmpty();
                promoProductIdFieldx.setEditable(hasPromoProduct);
                if (!hasPromoProduct) {
                    promoProductIdFieldx.setText("");
                    promoQtyField.setValue(0);
                    promoProductIdFieldx.setEditable(false);
                }
                boolean hasGift = hasPromoProduct && !promoProductIdFieldx.getText().trim().isEmpty();
                promoQtyField.setEnabled(hasGift);
                if (!hasGift) promoQtyField.setValue(0);
            };
            productIdFieldx.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                void update() {
                    updatePromoFieldsCreate.run();
                }

                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    update();
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    update();
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    update();
                }
            });
            promoProductIdFieldx.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                void update() {
                    updatePromoFieldsCreate.run();
                }

                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    update();
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    update();
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    update();
                }
            });
            updatePromoFieldsCreate.run();

            cancelBtn.addActionListener(ev -> dialog.dispose());
            okBtn.addActionListener(ev -> {
                String name = nameField.getText().trim();
                Date startDate = (Date) startSpinner.getValue();
                Date endDate = (Date) endSpinner.getValue();
                String productId = productIdFieldx.getText().trim();
                String minQtyStr = minQtyField.getText().replaceAll(",", "").trim();
                String promoProductId = promoProductIdFieldx.getText().trim();
                String promoQtyStr = promoQtyField.getText().replaceAll(",", "").trim();
                String discountStr = discountField.getText().replaceAll(",", "").trim();

                // Validation
                if (name.isEmpty()) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập tên khuyến mãi.");
                    return;
                }
                if (startDate == null || endDate == null) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập thời gian bắt đầu và kết thúc.");
                    return;
                }
                if (!endDate.after(startDate)) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Thời gian kết thúc phải sau thời gian bắt đầu.");
                    return;
                }
                if (productId.isEmpty()) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập mã sản phẩm khuyến mãi.");
                    return;
                }
                if (serviceManager.getSaleService().getProductById(productId) == null) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Mã sản phẩm khuyến mãi không tồn tại.");
                    return;
                }
                int minQty;
                try {
                    minQty = Integer.parseInt(minQtyStr);
                    if (minQty < 1) throw new NumberFormatException();
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Số lượng tối thiểu phải là số nguyên dương.");
                    return;
                }
                String promoProductIdVal = null;
                int promoQty;
                if (!promoProductId.isEmpty()) {
                    if (serviceManager.getSaleService().getProductById(promoProductId) == null) {
                        DialogFactory.showErrorDialog(dialog, "Lỗi", "Mã sản phẩm tặng không tồn tại.");
                        return;
                    }
                    promoProductIdVal = promoProductId;
                    try {
                        promoQty = Integer.parseInt(promoQtyStr);
                        if (promoQty < 1) throw new NumberFormatException();
                    } catch (Exception ex) {
                        DialogFactory.showErrorDialog(dialog, "Lỗi", "Số lượng tặng phải là số nguyên dương.");
                        return;
                    }
                } else {
                    promoQty = 0;
                }
                double discount;
                try {
                    double d = Double.parseDouble(discountStr);
                    if (d < 0 || d > 100) throw new NumberFormatException();
                    discount = d / 100.0;
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Giảm giá phải là số từ 0 đến 100.");
                    return;
                }
                Promotion promo = new Promotion(null, name, new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()), productId, minQty, promoProductIdVal, promoQty, java.math.BigDecimal.valueOf(discount), false);
                boolean ok = serviceManager.getSaleService().createPromotion(promo);
                if (!ok) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo khuyến mãi. Vui lòng thử lại.");
                    return;
                }
                String info = "Tên: " + name +
                        "\nThời gian: " + df1.format(startDate) + " đến " + df1.format(endDate) +
                        "\nMã SP KM: " + productId +
                        "\nSố lượng tối thiểu: " + minQty +
                        "\nMã SP tặng: " + (promoProductIdVal != null ? promoProductIdVal : "Không có") +
                        "\nSố lượng tặng: " + promoQty +
                        "\nGiảm giá: " + discountStr + "%\n";
                DialogFactory.showInfoDialog(dialog, "Tạo thành công", info);
                searchBtn.doClick();
                dialog.dispose();
            });
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        List<Promotion> init = serviceManager.getSaleService().getAllPromotions();
        String[][] initData = this.toTableData(init);
        java.util.List<ScrollableTable.ActionDefinition> actions = createActions();
        this.promoTable = new ScrollableTable(promoColumns, initData, actions);

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

    private java.util.List<ScrollableTable.ActionDefinition> createActions() {
        return java.util.List.of(
                new ScrollableTable.ActionDefinition("Xóa", "Xóa", (rowIndex, table) -> {
                    String promoId = (String) table.getValueAt(rowIndex, 0);
                    boolean confirm = DialogFactory.showConfirmYesNoDialog(this, "Xác nhận xóa", "Bạn có chắc chắn muốn xóa khuyến mãi này?");
                    if (!confirm) return;
                    boolean ok = serviceManager.getSaleService().deletePromotion(promoId);
                    if (!ok) {
                        DialogFactory.showErrorDialog(this, "Lỗi", "Không thể xóa khuyến mãi. Vui lòng thử lại.");
                        return;
                    }
                    DialogFactory.showInfoDialog(this, "Thành công", "Đã xóa khuyến mãi thành công.");
                    searchBtn.doClick();
                }),
                new ScrollableTable.ActionDefinition("Cập nhật", "Cập nhật", (rowIndex, table) -> {
                    String promoId = (String) table.getValueAt(rowIndex, 0);
                    Promotion promo = serviceManager.getSaleService().getPromotionById(promoId);
                    if (promo == null) {
                        DialogFactory.showErrorDialog(this, "Lỗi", "Không tìm thấy khuyến mãi.");
                        return;
                    }
                    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật khuyến mãi", true);
                    dialog.setLayout(new BorderLayout(10, 10));
                    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
                    JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

                    JLabel idLabel = new JLabel("Mã khuyến mãi:");
                    JTextField idField = new JTextField(promo.getId());
                    idField.setEditable(false);

                    JLabel nameLabel = new JLabel("Tên khuyến mãi:");
                    JTextField nameField = new JTextField(promo.getName());

                    JLabel startLabel = new JLabel("Thời gian bắt đầu:");
                    SpinnerDateModel startModel = new SpinnerDateModel(promo.getStartTime(), null, null, Calendar.MINUTE);
                    JSpinner startSpinner = new JSpinner(startModel);
                    startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd HH:mm:ss"));

                    JLabel endLabel = new JLabel("Thời gian kết thúc:");
                    SpinnerDateModel endModel = new SpinnerDateModel(promo.getEndTime(), null, null, Calendar.MINUTE);
                    JSpinner endSpinner = new JSpinner(endModel);
                    endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd HH:mm:ss"));

                    JLabel productIdLabel = new JLabel("Mã SP khuyến mãi:");
                    JTextField productIdFieldx = new JTextField(promo.getProductId());

                    JLabel minQtyLabel = new JLabel("Số lượng tối thiểu:");
                    JFormattedTextField minQtyField = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
                    minQtyField.setColumns(10);
                    minQtyField.setValue(promo.getMinimumPurchaseQuantity());

                    JLabel promoProductIdLabel = new JLabel("Mã SP tặng:");
                    JTextField promoProductIdFieldx = new JTextField(promo.getPromoProductId() != null ? promo.getPromoProductId() : "");

                    JLabel promoQtyLabel = new JLabel("Số lượng tặng:");
                    JFormattedTextField promoQtyField = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
                    promoQtyField.setColumns(10);
                    promoQtyField.setValue(promo.getPromoProductQuantity());

                    JLabel discountLabel = new JLabel("Giảm giá (%):");
                    JFormattedTextField discountField = new JFormattedTextField(java.text.NumberFormat.getNumberInstance());
                    discountField.setColumns(10);
                    discountField.setValue(promo.getDiscountRate() != null ? promo.getDiscountRate().multiply(java.math.BigDecimal.valueOf(100)).doubleValue() : 0.0);

                    formPanel.add(idLabel);
                    formPanel.add(idField);
                    formPanel.add(nameLabel);
                    formPanel.add(nameField);
                    formPanel.add(startLabel);
                    formPanel.add(startSpinner);
                    formPanel.add(endLabel);
                    formPanel.add(endSpinner);
                    formPanel.add(productIdLabel);
                    formPanel.add(productIdFieldx);
                    formPanel.add(minQtyLabel);
                    formPanel.add(minQtyField);
                    formPanel.add(promoProductIdLabel);
                    formPanel.add(promoProductIdFieldx);
                    formPanel.add(promoQtyLabel);
                    formPanel.add(promoQtyField);
                    formPanel.add(discountLabel);
                    formPanel.add(discountField);

                    mainPanel.add(formPanel, BorderLayout.CENTER);
                    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
                    JButton okBtn = new JButton("Cập nhật");
                    JButton cancelBtn = new JButton("Hủy");
                    btnPanel.add(okBtn);
                    btnPanel.add(cancelBtn);
                    mainPanel.add(btnPanel, BorderLayout.SOUTH);
                    dialog.add(mainPanel, BorderLayout.CENTER);

                    // Dynamic readonly/clearing logic for update dialog
                    Runnable updatePromoFieldsUpdate = () -> {
                        boolean hasPromoProduct = !productIdFieldx.getText().trim().isEmpty();
                        promoProductIdFieldx.setEditable(hasPromoProduct);
                        if (!hasPromoProduct) {
                            promoProductIdFieldx.setText("");
                            promoQtyField.setValue(0);
                            promoProductIdFieldx.setEditable(false);
                        }
                        boolean hasGift = hasPromoProduct && !promoProductIdFieldx.getText().trim().isEmpty();
                        promoQtyField.setEnabled(hasGift);
                        if (!hasGift) promoQtyField.setValue(0);
                    };
                    productIdFieldx.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                        void update() {
                            updatePromoFieldsUpdate.run();
                        }

                        public void insertUpdate(javax.swing.event.DocumentEvent e) {
                            update();
                        }

                        public void removeUpdate(javax.swing.event.DocumentEvent e) {
                            update();
                        }

                        public void changedUpdate(javax.swing.event.DocumentEvent e) {
                            update();
                        }
                    });
                    promoProductIdFieldx.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                        void update() {
                            updatePromoFieldsUpdate.run();
                        }

                        public void insertUpdate(javax.swing.event.DocumentEvent e) {
                            update();
                        }

                        public void removeUpdate(javax.swing.event.DocumentEvent e) {
                            update();
                        }

                        public void changedUpdate(javax.swing.event.DocumentEvent e) {
                            update();
                        }
                    });
                    updatePromoFieldsUpdate.run();

                    cancelBtn.addActionListener(ev -> dialog.dispose());
                    okBtn.addActionListener(ev -> {
                        String name = nameField.getText().trim();
                        Date startDate = (Date) startSpinner.getValue();
                        Date endDate = (Date) endSpinner.getValue();
                        String productId = productIdFieldx.getText().trim();
                        String minQtyStr = minQtyField.getText().replaceAll(",", "").trim();
                        String promoProductId = promoProductIdFieldx.getText().trim();
                        String promoQtyStr = promoQtyField.getText().replaceAll(",", "").trim();
                        String discountStr = discountField.getText().replaceAll(",", "").trim();

                        // Validation
                        if (name.isEmpty()) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập tên khuyến mãi.");
                            return;
                        }
                        if (startDate == null || endDate == null) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập thời gian bắt đầu và kết thúc.");
                            return;
                        }
                        if (!endDate.after(startDate)) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Thời gian kết thúc phải sau thời gian bắt đầu.");
                            return;
                        }
                        if (productId.isEmpty()) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập mã sản phẩm khuyến mãi.");
                            return;
                        }
                        if (serviceManager.getSaleService().getProductById(productId) == null) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Mã sản phẩm khuyến mãi không tồn tại.");
                            return;
                        }
                        int minQty;
                        try {
                            minQty = Integer.parseInt(minQtyStr);
                            if (minQty < 1) throw new NumberFormatException();
                        } catch (Exception ex) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Số lượng tối thiểu phải là số nguyên dương.");
                            return;
                        }
                        String promoProductIdVal = null;
                        int promoQty;
                        if (!promoProductId.isEmpty()) {
                            if (serviceManager.getSaleService().getProductById(promoProductId) == null) {
                                DialogFactory.showErrorDialog(dialog, "Lỗi", "Mã sản phẩm tặng không tồn tại.");
                                return;
                            }
                            promoProductIdVal = promoProductId;
                            try {
                                promoQty = Integer.parseInt(promoQtyStr);
                                if (promoQty < 1) throw new NumberFormatException();
                            } catch (Exception ex) {
                                DialogFactory.showErrorDialog(dialog, "Lỗi", "Số lượng tặng phải là số nguyên dương.");
                                return;
                            }
                        } else {
                            promoQty = 0;
                        }
                        double discount;
                        try {
                            double d = Double.parseDouble(discountStr);
                            if (d < 0 || d > 100) throw new NumberFormatException();
                            discount = d / 100.0;
                        } catch (Exception ex) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Giảm giá phải là số từ 0 đến 100.");
                            return;
                        }
                        Promotion updatedPromo = new Promotion(promo.getId(), name, new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()), productId, minQty, promoProductIdVal, promoQty, java.math.BigDecimal.valueOf(discount), false);
                        boolean ok = serviceManager.getSaleService().updatePromotion(updatedPromo);
                        if (!ok) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể cập nhật khuyến mãi. Vui lòng thử lại.");
                            return;
                        }
                        String info = "Tên: " + name +
                                "\nThời gian: " + df1.format(startDate) + " đến " + df1.format(endDate) +
                                "\nMã SP KM: " + productId +
                                "\nSố lượng tối thiểu: " + minQty +
                                "\nMã SP tặng: " + (promoProductIdVal != null ? promoProductIdVal : "Không có") +
                                "\nSố lượng tặng: " + promoQty +
                                "\nGiảm giá: " + discountStr + "%\n";
                        DialogFactory.showInfoDialog(dialog, "Cập nhật thành công", info);
                        searchBtn.doClick();
                        dialog.dispose();
                    });
                    dialog.pack();
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                })
        );
    }
} 