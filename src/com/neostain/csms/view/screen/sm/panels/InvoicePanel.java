package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Invoice;
import com.neostain.csms.model.Payment;
import com.neostain.csms.model.ShiftReport;
import com.neostain.csms.model.Store;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class InvoicePanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final String[] invoiceColumns = {
            "Mã hóa đơn",
            "Ngày tạo hóa đơn",
            "Thành tiền",
            "Giảm giá",
            "Tổng cộng",
            "Phương thức thanh toán",
            "Trạng thái",
            "Thành viên",
            "Nhân viên",
            "Điểm sử dụng"
    };
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private final DecimalFormat decf = new DecimalFormat("#,###.00");
    private ScrollableTable invoiceTable;

    public InvoicePanel() {
        this.decf.setRoundingMode(RoundingMode.DOWN);
        initializeComponents();
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search tools in a titled border panel
        BorderedPanel toolWrapper = new BorderedPanel("Tìm kiếm hóa đơn");
        toolWrapper.setLayout(new BorderLayout(10, 10));
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.X_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Input fields
        JTextField invoiceIdField = new JTextField(10);
        JTextField customerIdField = new JTextField(10);
        JTextField employeeIdField = new JTextField(10);

        // 1. Xác định mặc định
        Date defaultFrom = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date defaultTo = new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();

        // 2. Tạo model/spinner
        SpinnerDateModel fromModel = new SpinnerDateModel(defaultFrom, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateFromSpinner = new JSpinner(fromModel);
        dateFromSpinner.setEditor(new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd"));

        SpinnerDateModel toModel = new SpinnerDateModel(defaultTo, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateToSpinner = new JSpinner(toModel);
        dateToSpinner.setEditor(new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd"));

        // Status dropdown
        String[] statuses = {"Tất cả trạng thái", "Chưa hoàn thành", "Đã hoàn thành", "Đang yêu cầu hủy", "Đã hủy"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);

        // Payment methods dropdown (load from DB)
        List<Payment> methods = serviceManager.getSaleService().getAllPayments();
        String[] methodItems = new String[methods.size() + 1];
        methodItems[0] = "Tất cả phương thức";
        for (int i = 0; i < methods.size(); i++) {
            methodItems[i + 1] = methods.get(i).getId();
        }
        JComboBox<String> paymentMethodBox = new JComboBox<>(methodItems);

        toolWrapper.add(new JLabel("Mã hóa đơn:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(invoiceIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Mã thành viên:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(customerIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Mã nhân viên:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(employeeIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Ngày tạo hóa đơn: Từ"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(dateFromSpinner);

        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(new JLabel("đến"));
        toolWrapper.add(Box.createHorizontalStrut(8));

        toolWrapper.add(dateToSpinner);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Trạng thái:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(statusBox);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Phương thức thanh toán:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(paymentMethodBox);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(Box.createHorizontalStrut(8));

        toolWrapper.add(searchBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(resetBtn);

        // Button listeners
        searchBtn.addActionListener(e -> {
            String id = invoiceIdField.getText().trim();
            String customerId = customerIdField.getText().trim();
            String employeeId = employeeIdField.getText().trim();
            String status = statuses[statusBox.getSelectedIndex()];
            String paymentMethod = methodItems[paymentMethodBox.getSelectedIndex()];
            Date fromDate = (Date) dateFromSpinner.getValue();
            Date toDate = (Date) dateToSpinner.getValue();
            String from = (fromDate != null) ? df2.format(fromDate) : "";
            String to = (toDate != null) ? df2.format(toDate) : "";

            List<Invoice> results = serviceManager.getSaleService()
                    .searchInvoices(id, customerId, employeeId, status, paymentMethod, from, to);

            if (results.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Thông báo", "Không có kết quả tìm kiếm!");
            } else {
                String[][] data = this.toTableData(results);
                invoiceTable.refreshData(data);
            }
        });

        resetBtn.addActionListener(e -> {
            // 1. Xoá bộ lọc text
            invoiceIdField.setText("");
            customerIdField.setText("");
            employeeIdField.setText("");
            statusBox.setSelectedIndex(0);
            paymentMethodBox.setSelectedIndex(0);

            // 2. Đưa spinner về mặc định
            dateFromSpinner.setValue(defaultFrom);
            dateToSpinner.setValue(defaultTo);

            // 3. Tìm kiếm lại (lần này từ–đến rộng nhất => load all)
            searchBtn.doClick();
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        // Initialize table with all invoice of current store
        String currentShiftReportId = serviceManager.getCurrentShiftId();
        ShiftReport shiftReport = serviceManager.getOperationService().getShiftReportById(currentShiftReportId);
        Store store = serviceManager.getManagementService().getStoreById(shiftReport.getStoreId());
        String currentStoreId = store.getId();

        List<Invoice> init = serviceManager.getSaleService().getInvoicesByStoreId(currentStoreId);
        String[][] initData = this.toTableData(init);

        // Setup table with actions (delete/update)
        java.util.List<ScrollableTable.ActionDefinition> actions = createActions();
        this.invoiceTable = new ScrollableTable(invoiceColumns, initData, actions);

        // ----- Invoice List Section -----
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
                    decf.format(m.getNetAmount()),
                    decf.format(m.getDiscount()),
                    decf.format(m.getTotalDue()),
                    m.getPaymentId() + " - " + serviceManager.getSaleService().getPaymentById(m.getPaymentId()).getName(),
                    m.getStatus(),
                    m.getMemberId(),
                    m.getEmployeeId(),
                    String.valueOf(m.getPointUsed())
            };
        }
        return data;
    }

    private List<ScrollableTable.ActionDefinition> createActions() {
        return List.of(
                new ScrollableTable.ActionDefinition("In", "In hóa đơn", (row, table) -> {
                    try {
                        String id = table.getValueAt(row, 0).toString();
                        Invoice invoice = serviceManager.getSaleService().getInvoiceById(id);
                        File invoiceFile = serviceManager.getPrintingService().printInvoice(invoice);

                        if (invoiceFile != null) {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.open(invoiceFile);
                        }
                    } catch (Exception e) {
                        System.out.println("Error printing invoice: " + e.getMessage());
                    }
                }),
                new ScrollableTable.ActionDefinition("Hủy", "Hủy hóa đơn", (row, table) -> {
                    String id = table.getValueAt(row, 0).toString();
                    boolean confirm = DialogFactory.showConfirmYesNoDialog(
                            this,
                            "Xác nhận hủy",
                            "Bạn có chắc muốn hủy hóa đơn " + id + "?"
                    );
                    if (!confirm) return;
                    if (serviceManager.getSaleService().cancelInvoice(id)) {
                        DialogFactory.showInfoDialog(this, "Thông báo", "Hóa đơn " + id + " đã bị hủy.");
                        searchBtn.doClick();
                    } else {
                        DialogFactory.showInfoDialog(this, "Thông báo", "Hóa đơn " + id + " đã bị hủy từ trước.");
                    }
                })
        );
    }
}