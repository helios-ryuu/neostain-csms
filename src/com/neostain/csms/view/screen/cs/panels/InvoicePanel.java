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
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class InvoicePanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final String[] invoiceColumns = {
            "Mã hóa đơn", "Ngày tạo hóa đơn", "Thành tiền", "Giảm giá", "Tổng cộng", "Mã cửa hàng", "Mã thành viên", "Trạng thái", "Mã nhân viên"
    };
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private final DecimalFormat decf = new DecimalFormat("#,###");
    private final String currentStoreId;
    private final Set<String> storeEmployeeIds;
    private ScrollableTable invoiceTable;
    private POSPanel posPanel;
    private javax.swing.Timer lockCheckTimer;
    private boolean cancelActionEnabled = true;

    public InvoicePanel() {
        this.decf.setRoundingMode(RoundingMode.DOWN);
        // Determine current store and employees
        String currentShiftReportId = serviceManager.getCurrentShiftId();
        ShiftReport shiftReport = serviceManager.getOperationService().getShiftReportById(currentShiftReportId);
        Store store = serviceManager.getManagementService().getStoreById(shiftReport.getStoreId());
        this.currentStoreId = store.getId();
        List<Employee> employees = serviceManager.getManagementService().getEmployeeByManagerId(store.getManagerId());
        this.storeEmployeeIds = new HashSet<>();
        for (Employee emp : employees) storeEmployeeIds.add(emp.getId());
        storeEmployeeIds.add(store.getManagerId());
        initializeComponents();
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toolbar
        BorderedPanel toolWrapper = new BorderedPanel("Tìm kiếm hóa đơn");
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.Y_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField invoiceIdField = new JTextField(20);
        JTextField storeIdField = new JTextField(8);
        JTextField memberIdField = new JTextField(20);
        JTextField employeeIdField = new JTextField(12);

        Date defaultFrom = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date defaultTo = new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();
        SpinnerDateModel fromModel = new SpinnerDateModel(defaultFrom, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateFromSpinner = new JSpinner(fromModel);
        dateFromSpinner.setEditor(new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd"));
        SpinnerDateModel toModel = new SpinnerDateModel(defaultTo, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateToSpinner = new JSpinner(toModel);
        dateToSpinner.setEditor(new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd"));

        String[] statuses = {"TẤT CẢ TRẠNG THÁI", "CHƯA HOÀN THÀNH", "ĐÃ HOÀN THÀNH", "ĐANG YÊU CẦU HỦY", "ĐÃ HỦY"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);

        List<Payment> methods = serviceManager.getSaleService().getAllPayments();
        String[] methodItems = new String[methods.size() + 1];
        methodItems[0] = "TẤT CẢ PHƯƠNG THỨC";
        for (int i = 0; i < methods.size(); i++) {
            methodItems[i + 1] = methods.get(i).getId() + " - " + methods.get(i).getName();
        }
        JComboBox<String> paymentMethodBox = new JComboBox<>(methodItems);

        JPanel row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));
        row1.setOpaque(false);
        row1.add(new JLabel("Mã hóa đơn:"));
        row1.add(Box.createHorizontalStrut(8));
        row1.add(invoiceIdField);
        row1.add(Box.createHorizontalStrut(15));
        row1.add(new JLabel("Ngày tạo hóa đơn: Từ"));
        row1.add(Box.createHorizontalStrut(8));
        row1.add(dateFromSpinner);
        row1.add(Box.createHorizontalStrut(8));
        row1.add(new JLabel("đến"));
        row1.add(Box.createHorizontalStrut(8));
        row1.add(dateToSpinner);
        row1.add(Box.createHorizontalStrut(15));
        row1.add(new JLabel("Mã cửa hàng:"));
        row1.add(Box.createHorizontalStrut(8));
        row1.add(storeIdField);
        row1.add(Box.createHorizontalStrut(15));
        row1.add(new JLabel("Mã thành viên:"));
        row1.add(Box.createHorizontalStrut(8));
        row1.add(memberIdField);

        JPanel row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
        row2.setOpaque(false);
        row2.add(new JLabel("Phương thức thanh toán:"));
        row2.add(Box.createHorizontalStrut(8));
        row2.add(paymentMethodBox);
        row2.add(Box.createHorizontalStrut(15));
        row2.add(new JLabel("Trạng thái:"));
        row2.add(Box.createHorizontalStrut(8));
        row2.add(statusBox);
        row2.add(Box.createHorizontalStrut(15));
        row2.add(new JLabel("Mã nhân viên:"));
        row2.add(Box.createHorizontalStrut(8));
        row2.add(employeeIdField);
        row2.add(Box.createHorizontalStrut(15));
        row2.add(searchBtn);
        row2.add(Box.createHorizontalStrut(5));
        row2.add(resetBtn);

        toolWrapper.add(row1);
        toolWrapper.add(Box.createVerticalStrut(8));
        toolWrapper.add(row2);

        searchBtn.addActionListener(e -> {
            String id = invoiceIdField.getText().trim();
            String storeId = storeIdField.getText().trim();
            String memberId = memberIdField.getText().trim();
            String employeeId = employeeIdField.getText().trim();
            String status = statuses[statusBox.getSelectedIndex()];
            String paymentMethod = methodItems[paymentMethodBox.getSelectedIndex()];
            Date fromDate = (Date) dateFromSpinner.getValue();
            Date toDate = (Date) dateToSpinner.getValue();
            String from = (fromDate != null) ? df2.format(fromDate) : "";
            String to = (toDate != null) ? df2.format(toDate) : "";
            List<Invoice> results = serviceManager.getSaleService().searchInvoices(id, memberId, employeeId, status, paymentMethod, from, to);
            // Filter: only show invoices in this store or by employees of this store
            List<Invoice> filtered = new ArrayList<>();
            for (Invoice inv : results) {
                boolean inStore = inv.getStoreId() != null && inv.getStoreId().equals(currentStoreId);
                boolean byEmployee = inv.getEmployeeId() != null && storeEmployeeIds.contains(inv.getEmployeeId());
                boolean storeMatch = storeId.isEmpty() || (inv.getStoreId() != null && inv.getStoreId().equals(storeId));
                if ((inStore || byEmployee) && storeMatch) filtered.add(inv);
            }
            if (filtered.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Thông báo", "Không có kết quả tìm kiếm!");
            } else {
                String[][] data = toTableData(filtered);
                invoiceTable.refreshData(data);
            }
        });

        resetBtn.addActionListener(e -> {
            invoiceIdField.setText("");
            storeIdField.setText("");
            memberIdField.setText("");
            employeeIdField.setText("");
            statusBox.setSelectedIndex(0);
            paymentMethodBox.setSelectedIndex(0);
            dateFromSpinner.setValue(new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime());
            dateToSpinner.setValue(new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            searchBtn.doClick();
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        List<Invoice> init = serviceManager.getSaleService().getInvoicesByStoreId(currentStoreId);
        String[][] initData = toTableData(init);
        List<ScrollableTable.ActionDefinition> actions = createActions();
        this.invoiceTable = new ScrollableTable(invoiceColumns, initData, actions);

        BorderedPanel invoiceTableBordered = new BorderedPanel("Danh sách hóa đơn");
        invoiceTableBordered.setLayout(new BorderLayout(10, 10));
        invoiceTableBordered.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        invoiceTableBordered.add(invoiceTable);
        this.add(invoiceTableBordered, BorderLayout.CENTER);
    }

    private String[][] toTableData(List<Invoice> list) {
        String[][] data = new String[list.size()][invoiceColumns.length];
        for (int i = 0; i < list.size(); i++) {
            Invoice m = list.get(i);
            data[i] = new String[]{
                    m.getId(),
                    df1.format(m.getCreationTime()),
                    decf.format(m.getNetAmount()) + " VND",
                    decf.format(m.getDiscount()) + " VND",
                    decf.format(m.getTotalDue()) + " VND",
                    m.getStoreId(),
                    m.getMemberId(),
                    m.getStatus(),
                    m.getEmployeeId()
            };
        }
        return data;
    }

    private List<ScrollableTable.ActionDefinition> createActions() {
        return List.of(
                new ScrollableTable.ActionDefinition("In hóa đơn", "In", (row, table) -> {
                    try {
                        String id = table.getValueAt(row, 0).toString();
                        Invoice invoice = serviceManager.getSaleService().getInvoiceById(id);
                        File invoiceFile = serviceManager.getPrintingService().printInvoice(invoice);
                        if (invoiceFile != null) {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.open(invoiceFile);
                        }
                    } catch (Exception e) {
                        DialogFactory.showErrorDialog(this, "Lỗi", "Không thể in hóa đơn: " + e.getMessage());
                    }
                }),
                new ScrollableTable.ActionDefinition("Yêu cầu hủy", "Yêu cầu", (row, table) -> {
                    if (!cancelActionEnabled) {
                        DialogFactory.showWarningDialog(this, "Không thể yêu cầu hủy", "Không thể yêu cầu hủy hóa đơn khi đã sử dụng điểm. Hãy hủy tích điểm hoặc làm mới giỏ hàng để tiếp tục.");
                        return;
                    }
                    String id = table.getValueAt(row, 0).toString();
                    Invoice invoice = serviceManager.getSaleService().getInvoiceById(id);
                    if ("ĐANG YÊU CẦU HỦY".equals(invoice.getStatus())) {
                        DialogFactory.showInfoDialog(this, "Thông báo", "Hóa đơn đã được yêu cầu hủy.");
                        return;
                    }
                    if ("ĐÃ HỦY".equals(invoice.getStatus())) {
                        DialogFactory.showInfoDialog(this, "Thông báo", "Hóa đơn đã được hủy từ trước.");
                        return;
                    }
                    boolean confirm = DialogFactory.showConfirmYesNoDialog(
                            this,
                            "Xác nhận yêu cầu hủy",
                            "Bạn có chắc muốn yêu cầu hủy hóa đơn " + id + "?"
                    );
                    if (!confirm) return;
                    boolean updated = serviceManager.getSaleService().updateStatus(id, "ĐANG YÊU CẦU HỦY");
                    if (updated) {
                        DialogFactory.showInfoDialog(this, "Thông báo", "Đã gửi yêu cầu hủy hóa đơn " + id + ".");
                        searchBtn.doClick();
                    } else {
                        DialogFactory.showErrorDialog(this, "Lỗi", "Không thể cập nhật trạng thái hóa đơn.");
                    }
                }),
                new ScrollableTable.ActionDefinition("Chi tiết hóa đơn", "Chi tiết", (row, table) -> {
                    String id = table.getValueAt(row, 0).toString();
                    List<InvoiceDetail> details = serviceManager.getSaleService().getInvoiceDetailsByInvoiceId(id);
                    List<Product> allProducts = serviceManager.getSaleService().getAllProducts();
                    Map<String, Product> productMap = new HashMap<>();
                    for (Product p : allProducts) productMap.put(p.getId(), p);
                    // Build table data
                    String[] cols = {"Mã SP", "Tên SP", "SL", "Đơn giá"};
                    Object[][] data = new Object[details.size()][cols.length];
                    for (int i = 0; i < details.size(); i++) {
                        InvoiceDetail d = details.get(i);
                        Product p = productMap.get(d.getProductId());
                        String name = p != null ? p.getName() : d.getProductId();
                        if (d.getUnitPrice() != null && d.getUnitPrice().compareTo(java.math.BigDecimal.ZERO) == 0) {
                            name = "KM - " + name;
                        }
                        data[i][0] = d.getProductId();
                        data[i][1] = name;
                        data[i][2] = d.getQuantitySold();
                        data[i][3] = d.getUnitPrice() != null ? decf.format(d.getUnitPrice()) + " VND" : "";
                    }
                    ScrollableTable detailTable = new ScrollableTable(cols, data, List.of());
                    JPanel panel = new JPanel(new BorderLayout(10, 10));
                    panel.add(detailTable, BorderLayout.CENTER);
                    JButton okBtn = new JButton("OK");
                    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    btnPanel.add(okBtn);
                    panel.add(btnPanel, BorderLayout.SOUTH);
                    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn", true);
                    dialog.setContentPane(panel);
                    dialog.setSize(600, 400);
                    dialog.setLocationRelativeTo(this);
                    okBtn.addActionListener(ev -> dialog.dispose());
                    dialog.setVisible(true);
                })
        );
    }

    public void setPOSPanel(POSPanel posPanel) {
        this.posPanel = posPanel;
        if (lockCheckTimer != null) lockCheckTimer.stop();
        lockCheckTimer = new javax.swing.Timer(500, e -> updateCancelActionEnabled());
        lockCheckTimer.start();
    }

    private void updateCancelActionEnabled() {
        if (posPanel == null) return;
        boolean locked = posPanel.isPointsLocked();
        if (cancelActionEnabled == !locked) return;
        cancelActionEnabled = !locked;
        if (invoiceTable != null) {
            invoiceTable.getTable().repaint();
        }
    }
}
