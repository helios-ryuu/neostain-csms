package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Paycheck;
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

public class PaycheckPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final JButton createBtn = new StandardButton(this, "Tạo phiếu lương");
    private final JButton createBatchBtn = new StandardButton(this, "Tạo phiếu lương hàng loạt");
    private final String[] paycheckColumns = {
            "Mã phiếu lương",
            "Nhân viên",
            "Lương gross",
            "Khấu trừ",
            "Lương net",
            "Ngày tạo phiếu lương",
            "Kỳ trả lương"
    };
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private final DecimalFormat decf = new DecimalFormat("#,###.00");
    private ScrollableTable paycheckTable;

    public PaycheckPanel() {
        this.decf.setRoundingMode(RoundingMode.DOWN);
        initializeComponents();
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toolbar in a bordered panel
        BorderedPanel toolWrapper = new BorderedPanel("Tìm kiếm phiếu lương");
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.X_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField paycheckIdField = new JTextField(10);
        JTextField employeeIdField = new JTextField(10);

        Date defaultFrom = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date defaultTo = new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();

        SpinnerDateModel fromModel = new SpinnerDateModel(defaultFrom, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateFromSpinner = new JSpinner(fromModel);
        dateFromSpinner.setEditor(new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd"));

        SpinnerDateModel toModel = new SpinnerDateModel(defaultTo, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateToSpinner = new JSpinner(toModel);
        dateToSpinner.setEditor(new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd"));

        toolWrapper.add(new JLabel("Mã phiếu lương:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(paycheckIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Mã nhân viên:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(employeeIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Ngày trả lương: Từ"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(dateFromSpinner);
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(new JLabel("đến"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(dateToSpinner);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(searchBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(resetBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(createBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(createBatchBtn);

        searchBtn.addActionListener(e -> {
            String id = paycheckIdField.getText().trim();
            String employeeId = employeeIdField.getText().trim();
            Date fromDate = (Date) dateFromSpinner.getValue();
            Date toDate = (Date) dateToSpinner.getValue();
            String from = (fromDate != null) ? df2.format(fromDate) : "";
            String to = (toDate != null) ? df2.format(toDate) : "";
            List<Paycheck> results = serviceManager.getOperationService().getAllPaychecks();
            // Filter in-memory for now, or replace with DAO search if available
            results = results.stream().filter(p ->
                    (id.isEmpty() || p.getId().equalsIgnoreCase(id)) &&
                            (employeeId.isEmpty() || p.getEmployeeId().equalsIgnoreCase(employeeId)) &&
                            (from.isEmpty() || (p.getPayDate() != null && df2.format(p.getPayDate()).compareTo(from) >= 0)) &&
                            (to.isEmpty() || (p.getPayDate() != null && df2.format(p.getPayDate()).compareTo(to) <= 0))
            ).toList();
            if (results.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Thông báo", "Không có kết quả tìm kiếm!");
            } else {
                String[][] data = this.toTableData(results);
                paycheckTable.refreshData(data);
            }
        });

        resetBtn.addActionListener(e -> {
            paycheckIdField.setText("");
            employeeIdField.setText("");
            dateFromSpinner.setValue(defaultFrom);
            dateToSpinner.setValue(defaultTo);
            searchBtn.doClick();
        });

        createBtn.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo phiếu lương", true);
            dialog.setLayout(new BorderLayout(10, 10));
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

            // Get current store employees
            String username = serviceManager.getCurrentUsername();
            if (username == null) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không xác định được người dùng hiện tại.");
                return;
            }
            var account = serviceManager.getAuthService().getAccountByUsername(username);
            if (account == null) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không xác định được tài khoản người dùng.");
                return;
            }
            var employee = serviceManager.getManagementService().getEmployeeById(account.getEmployeeId());
            if (employee == null) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không xác định được nhân viên hiện tại.");
                return;
            }
            var store = serviceManager.getManagementService().getStoreByManagerId(employee.getId());
            if (store == null) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không xác định được cửa hàng hiện tại.");
                return;
            }
            java.util.List<com.neostain.csms.model.Employee> employees = serviceManager.getManagementService().getEmployeeByManagerId(store.getManagerId());
            if (employees == null || employees.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không có nhân viên nào trong cửa hàng này.");
                return;
            }

            // Employee combobox
            JLabel empLabel = new JLabel("Chọn nhân viên:");
            JComboBox<String> empCombo = new JComboBox<>();
            java.util.Map<String, com.neostain.csms.model.Employee> empMap = new java.util.HashMap<>();
            for (var emp : employees) {
                String display = emp.getId() + " - " + emp.getName();
                empCombo.addItem(display);
                empMap.put(display, emp);
            }

            // Deduction field (numeric only)
            JLabel deductionLabel = new JLabel("Khấu trừ:");
            JTextField deductionField = new JTextField(10);
            deductionField.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    String text = ((JTextField) input).getText();
                    return text.matches("\\d+(\\.\\d{1,2})?");
                }
            });
            deductionField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != '.' && c != '\b') {
                        e.consume();
                    }
                }
            });

            // Date spinners
            JLabel startLabel = new JLabel("Thời gian bắt đầu:");
            JLabel endLabel = new JLabel("Thời gian kết thúc:");
            java.util.Date now = new java.util.Date();
            SpinnerDateModel startModel = new SpinnerDateModel(now, null, null, Calendar.DAY_OF_MONTH);
            JSpinner startSpinner = new JSpinner(startModel);
            startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd"));
            SpinnerDateModel endModel = new SpinnerDateModel(now, null, null, Calendar.DAY_OF_MONTH);
            JSpinner endSpinner = new JSpinner(endModel);
            endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd"));

            formPanel.add(empLabel);
            formPanel.add(empCombo);
            formPanel.add(deductionLabel);
            formPanel.add(deductionField);
            formPanel.add(startLabel);
            formPanel.add(startSpinner);
            formPanel.add(endLabel);
            formPanel.add(endSpinner);

            mainPanel.add(formPanel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton okBtn = new JButton("Tạo phiếu lương");
            JButton cancelBtn = new JButton("Hủy");
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            dialog.add(mainPanel, BorderLayout.CENTER);

            cancelBtn.addActionListener(ev -> dialog.dispose());

            okBtn.addActionListener(ev -> {
                String empDisplay = (String) empCombo.getSelectedItem();
                com.neostain.csms.model.Employee selectedEmp = empMap.get(empDisplay);
                if (selectedEmp == null) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng chọn nhân viên.");
                    return;
                }
                String deductionStr = deductionField.getText().trim();
                if (deductionStr.isEmpty() || !deductionStr.matches("\\d+(\\.\\d{1,2})?")) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập số tiền khấu trừ hợp lệ.");
                    return;
                }
                java.math.BigDecimal deduction = new java.math.BigDecimal(deductionStr);
                java.util.Date startDate = (java.util.Date) startSpinner.getValue();
                java.util.Date endDate = (java.util.Date) endSpinner.getValue();
                if (!endDate.after(startDate)) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Thời gian kết thúc phải sau thời gian bắt đầu.");
                    return;
                }
                try {
                    java.sql.Timestamp periodStart = new java.sql.Timestamp(startDate.getTime());
                    java.sql.Timestamp periodEnd = new java.sql.Timestamp(endDate.getTime());
                    String paycheckId = serviceManager.getOperationService().generatePaycheck(selectedEmp.getId(), deduction, periodStart, periodEnd);
                    if (paycheckId == null || paycheckId.isEmpty()) {
                        DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo phiếu lương. Vui lòng kiểm tra lại dữ liệu hoặc thử lại sau.");
                        return;
                    }
                    Paycheck created = serviceManager.getOperationService().getPaycheckById(paycheckId.trim());
                    if (created == null) {
                        DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể lấy thông tin phiếu lương vừa tạo.");
                        return;
                    }
                    Employee emp = serviceManager.getManagementService().getEmployeeById(created.getEmployeeId());
                    String info = "Mã phiếu lương: " + created.getId() + "\n" +
                            "Nhân viên: " + (emp != null ? (emp.getId() + " - " + emp.getName()) : created.getEmployeeId()) + "\n" +
                            "Kỳ trả lương: " +
                            (created.getPeriodStart() != null ? df2.format(created.getPeriodStart()) : "") +
                            " - " +
                            (created.getPeriodEnd() != null ? df2.format(created.getPeriodEnd()) : "") + "\n" +
                            "Ngày trả lương: " +
                            (created.getPayDate() != null ? df1.format(created.getPayDate()) : "") + "\n" +
                            "Lương gross: " + (created.getGrossAmount() != null ? decf.format(created.getGrossAmount()) : "") + "\n" +
                            "Khấu trừ: " + (created.getDeductions() != null ? decf.format(created.getDeductions()) : "") + "\n" +
                            "Lương net: " + (created.getNetAmount() != null ? decf.format(created.getNetAmount()) : "") + "\n";
                    DialogFactory.showInfoDialog(dialog, "Tạo phiếu lương thành công", info);
                    // Refresh table
                    searchBtn.doClick();
                    dialog.dispose();
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo phiếu lương: " + ex.getMessage());
                }
            });

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        createBatchBtn.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo phiếu lương hàng loạt", true);
            dialog.setLayout(new BorderLayout(10, 10));
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

            // Get current store employees
            String username = serviceManager.getCurrentUsername();
            if (username == null) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không xác định được người dùng hiện tại.");
                return;
            }
            var account = serviceManager.getAuthService().getAccountByUsername(username);
            if (account == null) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không xác định được tài khoản người dùng.");
                return;
            }
            var employee = serviceManager.getManagementService().getEmployeeById(account.getEmployeeId());
            if (employee == null) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không xác định được nhân viên hiện tại.");
                return;
            }
            var store = serviceManager.getManagementService().getStoreByManagerId(employee.getId());
            if (store == null) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không xác định được cửa hàng hiện tại.");
                return;
            }
            java.util.List<com.neostain.csms.model.Employee> employees = serviceManager.getManagementService().getEmployeeByManagerId(store.getManagerId());
            if (employees == null || employees.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Không có nhân viên nào trong cửa hàng này.");
                return;
            }

            // Deduction field (numeric only)
            JLabel deductionLabel = new JLabel("Khấu trừ:");
            JTextField deductionField = new JTextField(10);
            deductionField.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    String text = ((JTextField) input).getText();
                    return text.matches("\\d+(\\.\\d{1,2})?");
                }
            });
            deductionField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != '.' && c != '\b') {
                        e.consume();
                    }
                }
            });

            // Date spinners
            JLabel startLabel = new JLabel("Thời gian bắt đầu:");
            JLabel endLabel = new JLabel("Thời gian kết thúc:");
            java.util.Date now = new java.util.Date();
            SpinnerDateModel startModel = new SpinnerDateModel(now, null, null, Calendar.DAY_OF_MONTH);
            JSpinner startSpinner = new JSpinner(startModel);
            startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd"));
            SpinnerDateModel endModel = new SpinnerDateModel(now, null, null, Calendar.DAY_OF_MONTH);
            JSpinner endSpinner = new JSpinner(endModel);
            endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd"));

            formPanel.add(deductionLabel);
            formPanel.add(deductionField);
            formPanel.add(startLabel);
            formPanel.add(startSpinner);
            formPanel.add(endLabel);
            formPanel.add(endSpinner);

            mainPanel.add(formPanel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton okBtn = new JButton("Tạo phiếu lương hàng loạt");
            JButton cancelBtn = new JButton("Hủy");
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            dialog.add(mainPanel, BorderLayout.CENTER);

            cancelBtn.addActionListener(ev -> dialog.dispose());

            okBtn.addActionListener(ev -> {
                String deductionStr = deductionField.getText().trim();
                if (deductionStr.isEmpty() || !deductionStr.matches("\\d+(\\.\\d{1,2})?")) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập số tiền khấu trừ hợp lệ.");
                    return;
                }
                java.math.BigDecimal deduction = new java.math.BigDecimal(deductionStr);
                java.util.Date startDate = (java.util.Date) startSpinner.getValue();
                java.util.Date endDate = (java.util.Date) endSpinner.getValue();
                if (!endDate.after(startDate)) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Thời gian kết thúc phải sau thời gian bắt đầu.");
                    return;
                }
                try {
                    java.sql.Timestamp periodStart = new java.sql.Timestamp(startDate.getTime());
                    java.sql.Timestamp periodEnd = new java.sql.Timestamp(endDate.getTime());
                    java.util.List<String[]> tableRows = new java.util.ArrayList<>();
                    int successCount = 0;
                    for (var emp : employees) {
                        String paycheckId = serviceManager.getOperationService().generatePaycheck(emp.getId(), deduction, periodStart, periodEnd);
                        if (paycheckId != null && !paycheckId.trim().isEmpty()) {
                            Paycheck created = serviceManager.getOperationService().getPaycheckById(paycheckId.trim());
                            if (created != null) {
                                tableRows.add(new String[]{
                                        created.getId(),
                                        emp.getId() + " - " + emp.getName(),
                                        (created.getPeriodStart() != null ? df2.format(created.getPeriodStart()) : "") + " - " + (created.getPeriodEnd() != null ? df2.format(created.getPeriodEnd()) : ""),
                                        created.getPayDate() != null ? df1.format(created.getPayDate()) : "",
                                        created.getGrossAmount() != null ? decf.format(created.getGrossAmount()) : "",
                                        created.getDeductions() != null ? decf.format(created.getDeductions()) : "",
                                        created.getNetAmount() != null ? decf.format(created.getNetAmount()) : "",
                                        "In phiếu lương"
                                });
                                successCount++;
                            }
                        }
                    }
                    if (successCount == 0) {
                        DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo phiếu lương cho bất kỳ nhân viên nào. Vui lòng kiểm tra lại dữ liệu hoặc thử lại sau.");
                        return;
                    }
                    // Show result in a scrollable table
                    String[] columns = {"Mã phiếu lương", "Nhân viên", "Kỳ trả lương", "Ngày trả lương", "Lương gross", "Khấu trừ", "Lương net", "In phiếu lương"};
                    String[][] data = tableRows.toArray(new String[0][]);
                    ScrollableTable resultTable = new ScrollableTable(columns, data, java.util.List.of());
                    resultTable.setPreferredSize(new java.awt.Dimension(1200, 300));
                    JOptionPane.showMessageDialog(dialog, resultTable, "Tạo phiếu lương hàng loạt thành công", JOptionPane.INFORMATION_MESSAGE);
                    // Refresh table
                    searchBtn.doClick();
                    dialog.dispose();
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo phiếu lương hàng loạt: " + ex.getMessage());
                }
            });

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        List<Paycheck> init = serviceManager.getOperationService().getAllPaychecks();
        String[][] initData = this.toTableData(init);
        java.util.List<ScrollableTable.ActionDefinition> actions = createActions();
        this.paycheckTable = new ScrollableTable(paycheckColumns, initData, actions);

        BorderedPanel paycheckTableBordered = new BorderedPanel("Danh sách phiếu lương");
        paycheckTableBordered.setLayout(new BorderLayout(10, 10));
        paycheckTableBordered.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        paycheckTableBordered.add(paycheckTable);
        this.add(paycheckTableBordered, BorderLayout.CENTER);
    }

    private String[][] toTableData(List<Paycheck> list) {
        String[][] data = new String[list.size()][paycheckColumns.length];
        for (int i = 0; i < list.size(); i++) {
            Paycheck p = list.get(i);
            Employee emp = serviceManager.getManagementService().getEmployeeById(p.getEmployeeId());
            String empStr = emp != null ? (emp.getId() + " - " + emp.getName()) : p.getEmployeeId();
            String period = (p.getPeriodStart() != null && p.getPeriodEnd() != null)
                    ? (df2.format(p.getPeriodStart()) + " - " + df2.format(p.getPeriodEnd())) : "";
            data[i] = new String[]{
                    p.getId(),
                    empStr,
                    p.getGrossAmount() != null ? decf.format(p.getGrossAmount()) : "",
                    p.getDeductions() != null ? decf.format(p.getDeductions()) : "",
                    p.getNetAmount() != null ? decf.format(p.getNetAmount()) : "",
                    p.getPayDate() != null ? df1.format(p.getPayDate()) : "",
                    period
            };
        }
        return data;
    }

    private java.util.List<ScrollableTable.ActionDefinition> createActions() {
        return java.util.List.of(
                new ScrollableTable.ActionDefinition("In phiếu lương", "In", (rowIndex, table) -> {
                    String paycheckId = (String) table.getValueAt(rowIndex, 0);
                    Paycheck paycheck = serviceManager.getOperationService().getPaycheckById(paycheckId);
                    if (paycheck == null) {
                        DialogFactory.showErrorDialog(this, "Lỗi", "Không tìm thấy phiếu lương.");
                        return;
                    }
                    try {
                        File file = serviceManager.getPrintingService().printPaycheck(paycheck);
                        if (file != null && file.exists()) {
                            Desktop.getDesktop().open(file);
                        } else {
                            DialogFactory.showErrorDialog(this, "Lỗi", "Không thể tạo file phiếu lương.");
                        }
                    } catch (Exception ex) {
                        DialogFactory.showErrorDialog(this, "Lỗi", "Không thể in phiếu lương: " + ex.getMessage());
                    }
                })
        );
    }
} 