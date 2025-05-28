package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Invoice;
import com.neostain.csms.model.Payment;
import com.neostain.csms.model.ShiftReport;
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

public class ShiftReportPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final String[] columns = {
            "Mã báo cáo kết ca",
            "Thời gian bắt đầu ca",
            "Thời gian kết thúc ca",
            "Doanh thu Ví điện tử",
            "Doanh thu Ngân hàng",
            "Doanh thu Tiền mặt",
            "Số lượng giao dịch",
            "Mã cửa hàng",
            "Mã nhân viên"
    };
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private final DecimalFormat decf = new DecimalFormat("#,###.00");
    private ScrollableTable table;
    private List<Employee> managedEmployees;
    private Set<String> allowedStoreIds;
    private Set<String> allowedEmployeeIds;

    public ShiftReportPanel() {
        this.decf.setRoundingMode(RoundingMode.DOWN);
        initializeAccess();
        initializeComponents();
    }

    private void initializeAccess() {
        String username = serviceManager.getCurrentUsername();
        var account = serviceManager.getAuthService().getAccountByUsername(username);
        var employee = serviceManager.getManagementService().getEmployeeById(account.getEmployeeId());
        var store = serviceManager.getManagementService().getStoreByManagerId(employee.getId());
        allowedStoreIds = new HashSet<>();
        allowedStoreIds.add(store.getId());
        managedEmployees = new ArrayList<>();
        managedEmployees.addAll(serviceManager.getManagementService().getEmployeeByManagerId(employee.getId()));
        managedEmployees.add(employee);
        allowedEmployeeIds = new HashSet<>();
        for (var emp : managedEmployees) allowedEmployeeIds.add(emp.getId());
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toolbar
        BorderedPanel toolWrapper = new BorderedPanel("Tìm kiếm báo cáo kết ca");
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.X_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField reportIdField = new JTextField(10);
        JTextField storeIdField = new JTextField(10);
        JTextField employeeIdField = new JTextField(10);

        Date defaultFrom = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date defaultTo = new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();
        SpinnerDateModel fromModel = new SpinnerDateModel(defaultFrom, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateFromSpinner = new JSpinner(fromModel);
        dateFromSpinner.setEditor(new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd"));
        SpinnerDateModel toModel = new SpinnerDateModel(defaultTo, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateToSpinner = new JSpinner(toModel);
        dateToSpinner.setEditor(new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd"));

        toolWrapper.add(new JLabel("Mã báo cáo kết ca:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(reportIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Thời gian ca: Từ"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(dateFromSpinner);
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(new JLabel("đến"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(dateToSpinner);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Mã cửa hàng:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(storeIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Mã nhân viên:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(employeeIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(searchBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(resetBtn);

        searchBtn.addActionListener(e -> {
            String id = reportIdField.getText().trim();
            String storeId = storeIdField.getText().trim();
            String employeeId = employeeIdField.getText().trim();
            Date fromDate = (Date) dateFromSpinner.getValue();
            Date toDate = (Date) dateToSpinner.getValue();
            String from = (fromDate != null) ? df2.format(fromDate) : null;
            String to = (toDate != null) ? df2.format(toDate) : null;
            List<ShiftReport> all = serviceManager.getOperationService().getAllShiftReports();
            List<ShiftReport> filtered = new ArrayList<>();
            for (ShiftReport sr : all) {
                if (!id.isEmpty() && (sr.getId() == null || !sr.getId().contains(id))) continue;
                if (!storeId.isEmpty() && (sr.getStoreId() == null || !sr.getStoreId().equals(storeId))) continue;
                if (!employeeId.isEmpty() && (sr.getEmployeeId() == null || !sr.getEmployeeId().equals(employeeId)))
                    continue;
                if (from != null && sr.getStartTime() != null && sr.getStartTime().before(java.sql.Timestamp.valueOf(from + " 00:00:00")))
                    continue;
                if (to != null && sr.getEndTime() != null && sr.getEndTime().after(java.sql.Timestamp.valueOf(to + " 23:59:59")))
                    continue;
                if (!allowedStoreIds.contains(sr.getStoreId()) && !allowedEmployeeIds.contains(sr.getEmployeeId()))
                    continue;
                filtered.add(sr);
            }
            if (filtered.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Thông báo", "Không có kết quả tìm kiếm!");
            } else {
                table.refreshData(toTableData(filtered));
            }
        });

        resetBtn.addActionListener(e -> {
            reportIdField.setText("");
            storeIdField.setText("");
            employeeIdField.setText("");
            dateFromSpinner.setValue(new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime());
            dateToSpinner.setValue(new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            searchBtn.doClick();
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        List<ShiftReport> init = new ArrayList<>();
        for (ShiftReport sr : serviceManager.getOperationService().getAllShiftReports()) {
            if (allowedStoreIds.contains(sr.getStoreId()) || allowedEmployeeIds.contains(sr.getEmployeeId())) {
                init.add(sr);
            }
        }
        table = new ScrollableTable(columns, toTableData(init), createActions());
        BorderedPanel tablePanel = new BorderedPanel("Danh sách báo cáo kết ca");
        tablePanel.setLayout(new BorderLayout(10, 10));
        tablePanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        tablePanel.add(table);
        this.add(tablePanel, BorderLayout.CENTER);
    }

    private Object[][] toTableData(List<ShiftReport> list) {
        Object[][] data = new Object[list.size()][columns.length];
        for (int i = 0; i < list.size(); i++) {
            ShiftReport sr = list.get(i);
            data[i][0] = sr.getId();
            data[i][1] = sr.getStartTime() != null ? df1.format(sr.getStartTime()) : "";
            data[i][2] = sr.getEndTime() != null ? df1.format(sr.getEndTime()) : "";
            data[i][3] = sr.getEwalletRevenue() != null ? decf.format(sr.getEwalletRevenue()) : "0";
            data[i][4] = sr.getBankRevenue() != null ? decf.format(sr.getBankRevenue()) : "0";
            data[i][5] = sr.getCashRevenue() != null ? decf.format(sr.getCashRevenue()) : "0";
            data[i][6] = sr.getTransactionCount();
            data[i][7] = sr.getStoreId();
            data[i][8] = sr.getEmployeeId();
        }
        return data;
    }

    private List<ScrollableTable.ActionDefinition> createActions() {
        return List.of(
                new ScrollableTable.ActionDefinition("In báo cáo", "In", (row, tbl) -> {
                    String id = tbl.getValueAt(row, 0).toString();
                    ShiftReport sr = serviceManager.getOperationService().getShiftReportById(id);
                    if (sr.getEndTime() == null) {
                        DialogFactory.showErrorDialog(this, "Thông báo", "Ca làm chưa hoàn thành");
                        return;
                    }
                    File file = serviceManager.getPrintingService().printShiftReport(sr);
                    if (file != null) {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (Exception ex) {
                            DialogFactory.showErrorDialog(this, "Lỗi", "Không thể mở file: " + ex.getMessage());
                        }
                    }
                }),
                new ScrollableTable.ActionDefinition("Chi tiết ca làm", "Chi tiết", (row, tbl) -> {
                    String id = tbl.getValueAt(row, 0).toString();
                    ShiftReport sr = serviceManager.getOperationService().getShiftReportById(id);
                    showShiftDetailDialog(sr);
                })
        );
    }

    private void showShiftDetailDialog(ShiftReport sr) {
        // Lấy các hóa đơn trong khoảng thời gian ca làm
        List<Invoice> invoices = serviceManager.getSaleService().searchInvoices(
                null, null, null, "TẤT CẢ TRẠNG THÁI", "TẤT CẢ PHƯƠNG THỨC",
                sr.getStartTime() != null ? df2.format(sr.getStartTime()) : null,
                sr.getEndTime() != null ? df2.format(sr.getEndTime()) : null
        );
        // Lọc lại theo storeId và employeeId nếu cần
        List<Invoice> filtered = new ArrayList<>();
        for (Invoice inv : invoices) {
            if (inv.getStoreId() != null && inv.getStoreId().equals(sr.getStoreId()) &&
                    inv.getEmployeeId() != null && inv.getEmployeeId().equals(sr.getEmployeeId())) {
                filtered.add(inv);
            }
        }
        String[] cols = {"Mã hóa đơn", "Thời gian tạo hóa đơn", "Phương thức", "Trạng thái"};
        Object[][] data = new Object[filtered.size()][cols.length];
        for (int i = 0; i < filtered.size(); i++) {
            Invoice inv = filtered.get(i);
            Payment payment = serviceManager.getSaleService().getPaymentById(inv.getPaymentId());
            data[i][0] = inv.getId();
            data[i][1] = inv.getCreationTime() != null ? df1.format(inv.getCreationTime()) : "";
            data[i][2] = payment != null ? (payment.getId() + " - " + payment.getName()) : inv.getPaymentId();
            data[i][3] = inv.getStatus();
        }
        List<ScrollableTable.ActionDefinition> actions = List.of(
                new ScrollableTable.ActionDefinition("In hóa đơn", "In", (row, tbl) -> {
                    String invoiceId = tbl.getValueAt(row, 0).toString();
                    Invoice invoice = serviceManager.getSaleService().getInvoiceById(invoiceId);
                    File file = serviceManager.getPrintingService().printInvoice(invoice);
                    if (file != null) {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (Exception ex) {
                            DialogFactory.showErrorDialog(this, "Lỗi", "Không thể mở file: " + ex.getMessage());
                        }
                    }
                }),
                new ScrollableTable.ActionDefinition("Chi tiết", "Chi tiết", (row, tbl) -> {
                    String invoiceId = tbl.getValueAt(row, 0).toString();
                    List<com.neostain.csms.model.InvoiceDetail> details = serviceManager.getSaleService().getInvoiceDetailsByInvoiceId(invoiceId);
                    List<com.neostain.csms.model.Product> allProducts = serviceManager.getSaleService().getAllProducts();
                    Map<String, com.neostain.csms.model.Product> productMap = new HashMap<>();
                    for (com.neostain.csms.model.Product p : allProducts) productMap.put(p.getId(), p);
                    String[] detailCols = {"Mã SP", "Tên SP", "SL", "Đơn giá"};
                    Object[][] detailData = new Object[details.size()][detailCols.length];
                    for (int i = 0; i < details.size(); i++) {
                        var d = details.get(i);
                        var p = productMap.get(d.getProductId());
                        String name = p != null ? p.getName() : d.getProductId();
                        if (d.getUnitPrice() != null && d.getUnitPrice().compareTo(java.math.BigDecimal.ZERO) == 0) {
                            name = "KM - " + name;
                        }
                        detailData[i][0] = d.getProductId();
                        detailData[i][1] = name;
                        detailData[i][2] = d.getQuantitySold();
                        detailData[i][3] = d.getUnitPrice() != null ? decf.format(d.getUnitPrice()) : "";
                    }
                    ScrollableTable detailTable = new ScrollableTable(detailCols, detailData, List.of());
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
        ScrollableTable invoiceTable = new ScrollableTable(cols, data, actions);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(invoiceTable, BorderLayout.CENTER);
        JButton okBtn = new JButton("OK");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(okBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết ca làm", true);
        dialog.setContentPane(panel);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        okBtn.addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
    }
} 