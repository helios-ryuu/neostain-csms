package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Role;
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

public class AccountPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final JButton createBtn = new StandardButton(this, "Tạo tài khoản");
    private final String[] accountColumns = {"Mã tài khoản", "Tên tài khoản", "Vai trò", "Ngày tạo tài khoản", "Trạng thái"};
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private final String[] statusOptions = {"TẤT CẢ TRẠNG THÁI", "ĐANG HOẠT ĐỘNG", "NGỪNG HOẠT ĐỘNG"};
    private ScrollableTable accountTable;
    private List<Role> roles;
    private List<Employee> employees;

    public AccountPanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Determine current manager and employees in store
        String currentUsername = serviceManager.getCurrentUsername();
        String currentManagerId = null;
        if (currentUsername != null) {
            var currentAccount = serviceManager.getAuthService().getAccountByUsername(currentUsername);
            if (currentAccount != null) {
                currentManagerId = currentAccount.getEmployeeId();
            }
        }
        // Employees managed by current manager (and self)
        List<Employee> managedEmployees = new ArrayList<>();
        if (currentManagerId != null) {
            managedEmployees.addAll(serviceManager.getManagementService().getEmployeeByManagerId(currentManagerId));
            Employee self = serviceManager.getManagementService().getEmployeeById(currentManagerId);
            if (self != null) managedEmployees.add(self);
        }
        Set<String> allowedEmployeeIds = new HashSet<>();
        for (var emp : managedEmployees) allowedEmployeeIds.add(emp.getId());
        roles = serviceManager.getAuthService().getAllRoles();
        employees = managedEmployees;

        // Toolbar in a bordered panel
        BorderedPanel toolWrapper = new BorderedPanel("Tìm kiếm tài khoản");
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.X_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JTextField accountIdField = new JTextField(10);
        JTextField employeeIdField = new JTextField(10);
        JTextField usernameField = new JTextField(10);

        // Role combobox
        assert roles != null;
        String[] roleItems = new String[roles.size() + 1];
        roleItems[0] = "TẤT CẢ VAI TRÒ";
        for (int i = 0; i < roles.size(); i++) {
            roleItems[i + 1] = roles.get(i).getId() + "-" + roles.get(i).getName();
        }
        JComboBox<String> roleBox = new JComboBox<>(roleItems);

        // Status combobox
        JComboBox<String> statusBox = new JComboBox<>(statusOptions);

        // Date spinners
        Date defaultFrom = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date defaultTo = new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();
        SpinnerDateModel fromModel = new SpinnerDateModel(defaultFrom, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateFromSpinner = new JSpinner(fromModel);
        dateFromSpinner.setEditor(new JSpinner.DateEditor(dateFromSpinner, "yyyy-MM-dd"));
        SpinnerDateModel toModel = new SpinnerDateModel(defaultTo, null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateToSpinner = new JSpinner(toModel);
        dateToSpinner.setEditor(new JSpinner.DateEditor(dateToSpinner, "yyyy-MM-dd"));

        toolWrapper.add(new JLabel("Mã tài khoản:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(accountIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Mã nhân viên:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(employeeIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Tên tài khoản:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(usernameField);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Vai trò:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(roleBox);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(new JLabel("Ngày tạo: Từ"));
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
        toolWrapper.add(searchBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(resetBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(createBtn);

        searchBtn.addActionListener(e -> {
            String id = accountIdField.getText().trim();
            String employeeId = employeeIdField.getText().trim();
            String username = usernameField.getText().trim();
            String roleId = roleBox.getSelectedIndex() > 0 ? roles.get(roleBox.getSelectedIndex() - 1).getId() : null;
            String status = statusBox.getSelectedIndex() > 0 ? statusOptions[statusBox.getSelectedIndex()] : null;
            Date fromDate = (Date) dateFromSpinner.getValue();
            Date toDate = (Date) dateToSpinner.getValue();
            String from = (fromDate != null) ? df2.format(fromDate) : "";
            String to = (toDate != null) ? df2.format(toDate) : "";
            List<Account> results = serviceManager.getAuthService().getAllAccounts();
            results = results.stream().filter(a ->
                    allowedEmployeeIds.contains(a.getEmployeeId()) &&
                            (id.isEmpty() || a.getId().equalsIgnoreCase(id)) &&
                            (employeeId.isEmpty() || a.getEmployeeId().equalsIgnoreCase(employeeId)) &&
                            (username.isEmpty() || a.getUsername().equalsIgnoreCase(username)) &&
                            (roleId == null || a.getRoleId().equalsIgnoreCase(roleId)) &&
                            (status == null || a.getStatus().equalsIgnoreCase(status)) &&
                            (from.isEmpty() || (a.getCreationTime() != null && df2.format(a.getCreationTime()).compareTo(from) >= 0)) &&
                            (to.isEmpty() || (a.getCreationTime() != null && df2.format(a.getCreationTime()).compareTo(to) <= 0))
            ).toList();
            if (results.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Thông báo", "Không có kết quả tìm kiếm!");
            } else {
                String[][] data = this.toTableData(results);
                accountTable.refreshData(data);
            }
        });

        resetBtn.addActionListener(e -> {
            accountIdField.setText("");
            employeeIdField.setText("");
            usernameField.setText("");
            roleBox.setSelectedIndex(0);
            statusBox.setSelectedIndex(0);
            dateFromSpinner.setValue(new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime());
            dateToSpinner.setValue(new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime());
            searchBtn.doClick();
        });

        createBtn.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo tài khoản", true);
            dialog.setLayout(new BorderLayout(10, 10));
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

            // Employee combobox
            JLabel empLabel = new JLabel("Chọn nhân viên:");
            JComboBox<String> empCombo = new JComboBox<>();
            Map<String, Employee> empMap = new HashMap<>();
            for (var emp : employees) {
                String display = emp.getId() + " - " + emp.getName();
                empCombo.addItem(display);
                empMap.put(display, emp);
            }

            // Username field
            JLabel usernameLabel = new JLabel("Tên tài khoản:");
            JTextField usernameFieldDialog = new JTextField(10);

            // Password field
            JLabel passwordLabel = new JLabel("Mật khẩu:");
            JPasswordField passwordField = new JPasswordField(10);

            // Role combobox
            JLabel roleLabel = new JLabel("Vai trò:");
            JComboBox<String> roleCombo = new JComboBox<>();
            Map<String, Role> roleMap = new HashMap<>();
            for (var role : roles) {
                String display = role.getId() + " - " + role.getName();
                roleCombo.addItem(display);
                roleMap.put(display, role);
            }

            formPanel.add(empLabel);
            formPanel.add(empCombo);
            formPanel.add(usernameLabel);
            formPanel.add(usernameFieldDialog);
            formPanel.add(passwordLabel);
            formPanel.add(passwordField);
            formPanel.add(roleLabel);
            formPanel.add(roleCombo);

            mainPanel.add(formPanel, BorderLayout.CENTER);
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton okBtn = new JButton("Tạo tài khoản");
            JButton cancelBtn = new JButton("Hủy");
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            dialog.add(mainPanel, BorderLayout.CENTER);

            cancelBtn.addActionListener(ev -> dialog.dispose());

            okBtn.addActionListener(ev -> {
                String empDisplay = (String) empCombo.getSelectedItem();
                Employee selectedEmp = empMap.get(empDisplay);
                if (selectedEmp == null) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng chọn nhân viên.");
                    return;
                }
                String username = usernameFieldDialog.getText().trim();
                if (username.isEmpty()) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập tên tài khoản.");
                    return;
                }
                String password = new String(passwordField.getPassword());
                if (password.isEmpty()) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng nhập mật khẩu.");
                    return;
                }
                String roleDisplay = (String) roleCombo.getSelectedItem();
                Role selectedRole = roleMap.get(roleDisplay);
                if (selectedRole == null) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng chọn vai trò.");
                    return;
                }
                try {
                    Account acc = new Account(null, selectedEmp.getId(), username, com.neostain.csms.util.PasswordUtils.hash(password), selectedRole.getId(), null, "ĐANG HOẠT ĐỘNG");
                    boolean ok = serviceManager.getAuthService().createAccount(acc);
                    if (!ok) {
                        DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo tài khoản. Vui lòng kiểm tra lại dữ liệu hoặc thử lại sau.");
                        return;
                    }
                    DialogFactory.showInfoDialog(dialog, "Tạo tài khoản thành công", "Tài khoản đã được tạo cho nhân viên: " + empDisplay + "\nTên tài khoản: " + username + "\nVai trò: " + roleDisplay);
                    searchBtn.doClick();
                    dialog.dispose();
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể tạo tài khoản: " + ex.getMessage());
                }
            });

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        List<Account> init = serviceManager.getAuthService().getAllAccounts();
        List<Account> filteredInit = init.stream().filter(a -> allowedEmployeeIds.contains(a.getEmployeeId())).toList();
        String[][] initData = this.toTableData(filteredInit);
        this.accountTable = new ScrollableTable(accountColumns, initData, java.util.List.of(
                new ScrollableTable.ActionDefinition("Cập nhật", "Cập nhật", (rowIndex, table) -> {
                    // Get account ID from table
                    String accId = (String) table.getValueAt(rowIndex, 0);
                    Account acc = serviceManager.getAuthService().getAllAccounts().stream()
                            .filter(a -> a.getId().equals(accId)).findFirst().orElse(null);
                    if (acc == null) {
                        DialogFactory.showErrorDialog(this, "Lỗi", "Không tìm thấy tài khoản.");
                        return;
                    }
                    Role role = roles.stream().filter(r -> r.getId().equals(acc.getRoleId())).findFirst().orElse(null);
                    // Show update dialog
                    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật tài khoản", true);
                    dialog.setLayout(new BorderLayout(10, 10));
                    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
                    JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

                    // Readonly fields
                    formPanel.add(new JLabel("Mã tài khoản:"));
                    JTextField idField = new JTextField(acc.getId());
                    idField.setEditable(false);
                    formPanel.add(idField);

                    formPanel.add(new JLabel("Tên tài khoản:"));
                    JTextField usernameFieldx = new JTextField(acc.getUsername());
                    usernameFieldx.setEditable(false);
                    formPanel.add(usernameFieldx);

                    formPanel.add(new JLabel("Vai trò:"));
                    JTextField roleField = new JTextField(role != null ? (role.getId() + "-" + role.getName()) : acc.getRoleId());
                    roleField.setEditable(false);
                    formPanel.add(roleField);

                    formPanel.add(new JLabel("Ngày tạo tài khoản:"));
                    JTextField creationField = new JTextField(acc.getCreationTime() != null ? df1.format(acc.getCreationTime()) : "");
                    creationField.setEditable(false);
                    formPanel.add(creationField);

                    formPanel.add(new JLabel("Trạng thái:"));
                    // Only show valid statuses, not the 'TẤT CẢ TRẠNG THÁI' option
                    String[] validStatusOptions = new String[statusOptions.length - 1];
                    System.arraycopy(statusOptions, 1, validStatusOptions, 0, statusOptions.length - 1);
                    JComboBox<String> statusCombo = new JComboBox<>(validStatusOptions);
                    // Set current status
                    for (int i = 0; i < validStatusOptions.length; i++) {
                        if (acc.getStatus() != null && acc.getStatus().equalsIgnoreCase(validStatusOptions[i])) {
                            statusCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                    formPanel.add(statusCombo);

                    mainPanel.add(formPanel, BorderLayout.CENTER);
                    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
                    JButton updateBtn = new JButton("Cập nhật");
                    JButton cancelBtn = new JButton("Hủy");
                    btnPanel.add(updateBtn);
                    btnPanel.add(cancelBtn);
                    mainPanel.add(btnPanel, BorderLayout.SOUTH);
                    dialog.add(mainPanel, BorderLayout.CENTER);

                    cancelBtn.addActionListener(ev -> dialog.dispose());
                    updateBtn.addActionListener(ev -> {
                        String newStatus = (String) statusCombo.getSelectedItem();
                        if (newStatus == null) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Vui lòng chọn trạng thái hợp lệ.");
                            return;
                        }
                        try {
                            boolean ok = serviceManager.getAuthService().updateAccountStatus(acc.getUsername(), newStatus);
                            if (!ok) {
                                DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể cập nhật trạng thái tài khoản. Vui lòng thử lại.");
                                return;
                            }
                            DialogFactory.showInfoDialog(dialog, "Cập nhật thành công", "Tài khoản đã được cập nhật:\nMã: " + acc.getId() + "\nTên: " + acc.getUsername() + "\nTrạng thái mới: " + newStatus);
                            searchBtn.doClick();
                            dialog.dispose();
                        } catch (Exception ex) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Không thể cập nhật trạng thái: " + ex.getMessage());
                        }
                    });
                    dialog.pack();
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                })
        ));

        BorderedPanel accountTableBordered = new BorderedPanel("Danh sách tài khoản");
        accountTableBordered.setLayout(new BorderLayout(10, 10));
        accountTableBordered.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        accountTableBordered.add(accountTable);
        this.add(accountTableBordered, BorderLayout.CENTER);
    }

    private String[][] toTableData(List<Account> list) {
        String[][] data = new String[list.size()][accountColumns.length];
        for (int i = 0; i < list.size(); i++) {
            Account a = list.get(i);
            Role role = roles.stream().filter(r -> r.getId().equals(a.getRoleId())).findFirst().orElse(null);
            data[i] = new String[]{
                    a.getId(),
                    a.getUsername(),
                    role != null ? (role.getId() + "-" + role.getName()) : a.getRoleId(),
                    a.getCreationTime() != null ? df1.format(a.getCreationTime()) : "",
                    a.getStatus()
            };
        }
        return data;
    }
}
