package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Assignment;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.ScrollableTable;
import com.neostain.csms.view.component.StandardButton;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AssignmentPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final JButton createBtn = new StandardButton(this, "Tạo phân công");
    private final String[] columns = {"Mã phân công", "Nhân viên", "Cửa hàng", "Ngày bắt đầu", "Ngày kết thúc"};
    private final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private ScrollableTable table;

    public AssignmentPanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tool panel
        BorderedPanel toolPanel = new BorderedPanel("Tìm kiếm phân công");
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.X_AXIS));
        toolPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField assignmentIdField = new JTextField(10);
        JTextField employeeIdField = new JTextField(10);
        JTextField storeIdField = new JTextField(10);

        Date defaultFrom = new GregorianCalendar(1970, Calendar.JANUARY, 1, 0, 0, 0).getTime();
        Date defaultTo = new GregorianCalendar(2100, Calendar.JANUARY, 1, 0, 0, 0).getTime();
        SpinnerDateModel fromModel = new SpinnerDateModel(defaultFrom, null, null, Calendar.MINUTE);
        JSpinner fromSpinner = new JSpinner(fromModel);
        fromSpinner.setEditor(new JSpinner.DateEditor(fromSpinner, "yyyy-MM-dd HH:mm:ss"));
        SpinnerDateModel toModel = new SpinnerDateModel(defaultTo, null, null, Calendar.MINUTE);
        JSpinner toSpinner = new JSpinner(toModel);
        toSpinner.setEditor(new JSpinner.DateEditor(toSpinner, "yyyy-MM-dd HH:mm:ss"));

        toolPanel.add(new JLabel("Mã phân công:"));
        toolPanel.add(Box.createHorizontalStrut(8));
        toolPanel.add(assignmentIdField);
        toolPanel.add(Box.createHorizontalStrut(15));
        toolPanel.add(new JLabel("Mã nhân viên:"));
        toolPanel.add(Box.createHorizontalStrut(8));
        toolPanel.add(employeeIdField);
        toolPanel.add(Box.createHorizontalStrut(15));
        toolPanel.add(new JLabel("Mã cửa hàng:"));
        toolPanel.add(Box.createHorizontalStrut(8));
        toolPanel.add(storeIdField);
        toolPanel.add(Box.createHorizontalStrut(15));
        toolPanel.add(new JLabel("Từ:"));
        toolPanel.add(Box.createHorizontalStrut(8));
        toolPanel.add(fromSpinner);
        toolPanel.add(Box.createHorizontalStrut(8));
        toolPanel.add(new JLabel("đến"));
        toolPanel.add(Box.createHorizontalStrut(8));
        toolPanel.add(toSpinner);
        toolPanel.add(Box.createHorizontalStrut(15));
        toolPanel.add(searchBtn);
        toolPanel.add(Box.createHorizontalStrut(5));
        toolPanel.add(resetBtn);
        toolPanel.add(Box.createHorizontalStrut(5));
        toolPanel.add(createBtn);

        // Button listeners
        searchBtn.addActionListener(e -> {
            String assignmentId = assignmentIdField.getText().trim();
            String employeeId = employeeIdField.getText().trim();
            String storeId = storeIdField.getText().trim();
            Date fromDate = (Date) fromSpinner.getValue();
            Date toDate = (Date) toSpinner.getValue();
            String from = (fromDate != null) ? df2.format(fromDate) : null;
            String to = (toDate != null) ? df2.format(toDate) : null;
            List<Assignment> results = serviceManager.getOperationService().searchAssignments(
                    assignmentId, employeeId, storeId, from, to
            );
            if (toTableData(results).length == 0) {
                DialogFactory.showErrorDialog(this, "Thông báo", "Không có kết quả tìm kiếm!");
            } else {
                table.refreshData(toTableData(results));
            }
        });
        resetBtn.addActionListener(e -> {
            assignmentIdField.setText("");
            employeeIdField.setText("");
            storeIdField.setText("");
            fromSpinner.setValue(defaultFrom);
            toSpinner.setValue(defaultTo);
            table.refreshData(toTableData(serviceManager.getOperationService().getAllAssignments()));
        });
        createBtn.addActionListener(e -> {
            // Get current manager's employeeId
            String username = ServiceManager.getInstance().getCurrentUsername();
            String managerId = ServiceManager.getInstance().getAuthService().getAccountByUsername(username).getEmployeeId();
            java.util.List<com.neostain.csms.model.Employee> employees = ServiceManager.getInstance().getManagementService().getEmployeeByManagerId(managerId);
            java.util.List<com.neostain.csms.model.Store> stores = ServiceManager.getInstance().getManagementService().getAllStores();
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo phân công mới", true);
            dialog.setLayout(new BorderLayout(10, 10));
            JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
            // Employee combobox
            JLabel empLabel = new JLabel("Chọn mã nhân viên:");
            JComboBox<String> empCombo = new JComboBox<>();
            for (com.neostain.csms.model.Employee emp : employees) {
                empCombo.addItem(emp.getId() + " - " + emp.getName());
            }
            // Store combobox
            JLabel storeLabel = new JLabel("Chọn cửa hàng:");
            JComboBox<String> storeCombo = new JComboBox<>();
            for (com.neostain.csms.model.Store store : stores) {
                storeCombo.addItem(store.getId() + " - " + store.getName());
            }
            // Start/end time
            JLabel startLabel = new JLabel("Thời gian bắt đầu:");
            SpinnerDateModel startModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
            JSpinner startSpinner = new JSpinner(startModel);
            startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "dd-MM-yyyy HH:mm:ss"));
            JLabel endLabel = new JLabel("Thời gian kết thúc:");
            SpinnerDateModel endModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
            JSpinner endSpinner = new JSpinner(endModel);
            endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "dd-MM-yyyy HH:mm:ss"));
            // Add to form
            form.add(empLabel);
            form.add(empCombo);
            form.add(storeLabel);
            form.add(storeCombo);
            form.add(startLabel);
            form.add(startSpinner);
            form.add(endLabel);
            form.add(endSpinner);
            dialog.add(form, BorderLayout.CENTER);
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton okBtn = new JButton("Tạo phân công");
            JButton cancelBtn = new JButton("Hủy");
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);
            cancelBtn.addActionListener(ev -> dialog.dispose());
            okBtn.addActionListener(ev -> {
                int empIdx = empCombo.getSelectedIndex();
                int storeIdx = storeCombo.getSelectedIndex();
                if (empIdx < 0 || storeIdx < 0) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng chọn nhân viên và cửa hàng.");
                    return;
                }
                // Extract only the ID part from the selected item (before the first space or dash)
                String empItem = (String) empCombo.getSelectedItem();
                String storeItem = (String) storeCombo.getSelectedItem();
                String employeeId = empItem != null ? empItem.split(" - ", 2)[0] : "";
                String storeId = storeItem != null ? storeItem.split(" - ", 2)[0] : "";
                Date startDate = (Date) startSpinner.getValue();
                Date endDate = (Date) endSpinner.getValue();
                if (startDate.after(endDate)) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Thời gian bắt đầu không được sau thời gian kết thúc.");
                    return;
                }
                try {
                    java.sql.Timestamp startTs = new java.sql.Timestamp(startDate.getTime());
                    java.sql.Timestamp endTs = new java.sql.Timestamp(endDate.getTime());
                    com.neostain.csms.model.Assignment assignment = new com.neostain.csms.model.Assignment(null, employeeId, storeId, startTs, endTs);
                    boolean ok = ServiceManager.getInstance().getOperationService().createAssignment(assignment);
                    if (ok) {
                        table.refreshData(toTableData(ServiceManager.getInstance().getOperationService().getAllAssignments()));
                        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 8, 8));
                        infoPanel.add(new JLabel("Mã nhân viên:"));
                        infoPanel.add(new JLabel(employeeId));
                        infoPanel.add(new JLabel("Mã cửa hàng:"));
                        infoPanel.add(new JLabel(storeId));
                        infoPanel.add(new JLabel("Thời gian bắt đầu:"));
                        infoPanel.add(new JLabel(df.format(startDate)));
                        infoPanel.add(new JLabel("Thời gian kết thúc:"));
                        infoPanel.add(new JLabel(df.format(endDate)));
                        DialogFactory.showInfoDialog(dialog, "Tạo phân công thành công!", infoPanel);
                        dialog.dispose();
                    } else {
                        DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo phân công. Vui lòng kiểm tra lại dữ liệu.");
                    }
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", ex.getMessage());
                }
            });
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        this.add(toolPanel, BorderLayout.NORTH);

        // Table panel
        List<ScrollableTable.ActionDefinition> actions = List.of(
                new ScrollableTable.ActionDefinition("Xóa", "Xóa", (rowIndex, tbl) -> {
                    String assignmentId = tbl.getValueAt(rowIndex, 0).toString();
                    boolean confirm = DialogFactory.showConfirmYesNoDialog(
                            this,
                            "Xác nhận xóa",
                            "Bạn có chắc muốn xóa phân công " + assignmentId + "?"
                    );
                    if (!confirm) return;
                    boolean ok = serviceManager.getOperationService().deleteAssignment(assignmentId);
                    if (ok) {
                        table.refreshData(toTableData(serviceManager.getOperationService().getAllAssignments()));
                        DialogFactory.showInfoDialog(this, "Thông báo", "Xóa thành công phân công " + assignmentId);
                    } else {
                        DialogFactory.showErrorDialog(this, "Lỗi", "Không thể xóa phân công: " + assignmentId);
                    }
                }),
                new ScrollableTable.ActionDefinition("Sửa", "Cập nhật", (rowIndex, tbl) -> {
                    String assignmentId = tbl.getValueAt(rowIndex, 0).toString();
                    Assignment assignment = serviceManager.getOperationService().getAssignmentById(assignmentId);
                    if (assignment == null) {
                        DialogFactory.showErrorDialog(this, "Lỗi", "Không tìm thấy phân công.");
                        return;
                    }
                    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật phân công: " + assignmentId, true);
                    dialog.setLayout(new BorderLayout(10, 10));
                    JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
                    JLabel startLabel = new JLabel("Ngày bắt đầu:");
                    JLabel endLabel = new JLabel("Ngày kết thúc:");
                    SpinnerDateModel startModel = new SpinnerDateModel(assignment.getStartTime() != null ? assignment.getStartTime() : new Date(), null, null, Calendar.MINUTE);
                    JSpinner startSpinner = new JSpinner(startModel);
                    startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd HH:mm:ss"));
                    SpinnerDateModel endModel = new SpinnerDateModel(assignment.getEndTime() != null ? assignment.getEndTime() : new Date(), null, null, Calendar.MINUTE);
                    JSpinner endSpinner = new JSpinner(endModel);
                    endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd HH:mm:ss"));
                    form.add(startLabel);
                    form.add(startSpinner);
                    form.add(endLabel);
                    form.add(endSpinner);
                    dialog.add(form, BorderLayout.CENTER);
                    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
                    JButton okBtn = new JButton("Cập nhật");
                    JButton cancelBtn = new JButton("Hủy");
                    btnPanel.add(okBtn);
                    btnPanel.add(cancelBtn);
                    dialog.add(btnPanel, BorderLayout.SOUTH);
                    cancelBtn.addActionListener(ev -> dialog.dispose());
                    okBtn.addActionListener(ev -> {
                        Date newStart = (Date) startSpinner.getValue();
                        Date newEnd = (Date) endSpinner.getValue();
                        if (newStart.after(newEnd)) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Thời gian bắt đầu không được sau thời gian kết thúc.");
                            return;
                        }
                        try {
                            boolean a = serviceManager.getOperationService().updateAssignmentStartTime(assignmentId, df.format(newStart));
                            boolean b = serviceManager.getOperationService().updateAssignmentEndTime(assignmentId, df.format(newEnd));
                            if (a && b) {
                                table.refreshData(toTableData(serviceManager.getOperationService().getAllAssignments()));
                                DialogFactory.showInfoDialog(dialog, "Thành công", "Cập nhật phân công thành công!");
                                dialog.dispose();
                            } else {
                                DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể cập nhật phân công.");
                            }
                        } catch (Exception ex) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", ex.getMessage());
                        }
                    });
                    dialog.pack();
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                })
        );
        table = new ScrollableTable(columns, toTableData(serviceManager.getOperationService().getAllAssignments()), actions);
        BorderedPanel tablePanel = new BorderedPanel("Danh sách phân công");
        tablePanel.setLayout(new BorderLayout(10, 10));
        tablePanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        tablePanel.add(table);
        this.add(tablePanel, BorderLayout.CENTER);
    }

    private String getCurrentManagerId() {
        String username = ServiceManager.getInstance().getCurrentUsername();
        return ServiceManager.getInstance().getAuthService().getAccountByUsername(username).getEmployeeId();
    }

    private String[][] toTableData(List<Assignment> list) {
        String managerId = getCurrentManagerId();
        java.util.List<String[]> filtered = new java.util.ArrayList<>();
        for (Assignment a : list) {
            String empId = a.getEmployeeId();
            String empName;
            String empManagerId = null;
            try {
                var emp = ServiceManager.getInstance().getManagementService().getEmployeeById(empId);
                empName = emp.getName();
                empManagerId = emp.getManagerId();
            } catch (Exception e) {
                empName = "";
            }
            if (empManagerId == null || !empManagerId.equals(managerId)) continue;
            String storeId = a.getStoreId();
            String storeName;
            try {
                storeName = ServiceManager.getInstance().getManagementService().getStoreById(storeId).getName();
            } catch (Exception e) {
                storeName = "";
            }
            filtered.add(new String[]{
                    a.getId(),
                    empId + " - " + empName,
                    storeId + " - " + storeName,
                    a.getStartTime() != null ? df.format(a.getStartTime()) : "",
                    a.getEndTime() != null ? df.format(a.getEndTime()) : ""
            });
        }
        return filtered.toArray(new String[0][0]);
    }
} 