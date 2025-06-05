package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.util.exception.FieldValidationException;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.ScrollableTable;
import com.neostain.csms.view.component.StandardButton;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class EmployeePanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final JButton addEmployeeBtn = new StandardButton(this, "Thêm nhân viên");
    private final String[] employeeColumns = {
            "Mã nhân viên",
            "Tên nhân viên",
            "Ngày đăng ký",
            "Email",
            "Số điện thoại",
            "Địa chỉ",
            "Lương theo giờ",
            "Trạng thái"
    };
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private final Account account = serviceManager.getAuthService().getAccountByUsername(serviceManager.getCurrentUsername());
    private ScrollableTable employeeTable;

    public EmployeePanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search tools in a titled border panel
        BorderedPanel toolWrapper = new BorderedPanel("Tìm kiếm nhân viên");
        toolWrapper.setLayout(new BorderLayout(10, 10));
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.X_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Input fields
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

        JTextField emailField = new JTextField(10);
        JTextField phoneNumberField = new JTextField(10);
        // Status dropdown
        String[] statuses = {"TẤT CẢ TRẠNG THÁI", "ĐANG HOẠT ĐỘNG", "TẠM NGỪNG HOẠT ĐỘNG", "ĐÃ NGHỈ VIỆC"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);

        toolWrapper.add(new JLabel("Mã nhân viên"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(employeeIdField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Ngày đăng ký: Từ"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(dateFromSpinner);

        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(new JLabel("đến"));
        toolWrapper.add(Box.createHorizontalStrut(8));

        toolWrapper.add(dateToSpinner);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Email:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(emailField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Số điện thoại:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(phoneNumberField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Trạng thái:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(statusBox);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(Box.createHorizontalStrut(8));

        toolWrapper.add(searchBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(resetBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(addEmployeeBtn);

        // Button listeners
        searchBtn.addActionListener(e -> {
            String id = employeeIdField.getText().trim();
            Date fromDate = (Date) dateFromSpinner.getValue();
            Date toDate = (Date) dateToSpinner.getValue();
            String from = (fromDate != null) ? df2.format(fromDate) : "";
            String to = (toDate != null) ? df2.format(toDate) : "";
            String email = emailField.getText().trim();
            String phoneNumber = phoneNumberField.getText().trim();
            String status = statuses[statusBox.getSelectedIndex()];


            List<Employee> results = serviceManager.getManagementService()
                    .searchEmployees(id, account.getEmployeeId(), from, to, email, phoneNumber, status);

            if (results.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Thông báo", "Không có kết quả tìm kiếm!");
            } else {
                String[][] data = this.toTableData(results);
                employeeTable.refreshData(data);
            }
        });

        resetBtn.addActionListener(e -> {
            // 1. Xoá bộ lọc text
            employeeIdField.setText("");

            // 2. Đưa spinner về mặc định
            dateFromSpinner.setValue(defaultFrom);
            dateToSpinner.setValue(defaultTo);


            emailField.setText("");
            phoneNumberField.setText("");
            statusBox.setSelectedIndex(0);

            // 3. Tìm kiếm lại (lần này từ–đến rộng nhất => load all)
            searchBtn.doClick();
        });

        addEmployeeBtn.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm nhân viên", true);
            dialog.setLayout(new BorderLayout(10, 10));
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            JLabel titleLabel = new JLabel("Thêm nhân viên mới", SwingConstants.CENTER);
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
            mainPanel.add(titleLabel, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
            JLabel managerIdLabel = new JLabel("Mã quản lý:");
            JTextField managerIdField = new JTextField(account.getEmployeeId());
            managerIdField.setEditable(false);
            JLabel nameLabel = new JLabel("Nhập họ và tên:");
            JTextField nameField = new JTextField(20);
            JLabel emailLabel = new JLabel("Nhập email:");
            JTextField emailFieldx = new JTextField(20);
            JLabel phoneLabel = new JLabel("Nhập số điện thoại:");
            JTextField phoneFieldx = new JTextField(20);
            JLabel addressLabel = new JLabel("Nhập địa chỉ:");
            JTextField addressField = new JTextField(20);
            JLabel wageLabel = new JLabel("Nhập lương theo giờ (đ/h):");
            JTextField wageField = new JTextField(20);
            JLabel roleLabel = new JLabel("Nhập vai trò:");
            java.util.List<com.neostain.csms.model.Role> roles = serviceManager.getAuthService().getAllRoles();
            JComboBox<String> roleCombo = new JComboBox<>(roles.stream().map(r -> r.getId() + " - " + r.getName()).toArray(String[]::new));
            // Add listener to roleCombo to handle managerId field
            roleCombo.addActionListener(ev -> {
                String selectedRole = roles.get(roleCombo.getSelectedIndex()).getName().toLowerCase();
                if (selectedRole.contains("quản lý")) {
                    managerIdField.setText("");
                    managerIdField.setEditable(false);
                } else {
                    managerIdField.setText(account.getEmployeeId());
                    managerIdField.setEditable(false);
                }
            });
            formPanel.add(managerIdLabel);
            formPanel.add(managerIdField);
            formPanel.add(nameLabel);
            formPanel.add(nameField);
            formPanel.add(emailLabel);
            formPanel.add(emailFieldx);
            formPanel.add(phoneLabel);
            formPanel.add(phoneFieldx);
            formPanel.add(addressLabel);
            formPanel.add(addressField);
            formPanel.add(wageLabel);
            formPanel.add(wageField);
            formPanel.add(roleLabel);
            formPanel.add(roleCombo);
            mainPanel.add(formPanel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton okBtn = new JButton("Thêm nhân viên");
            JButton cancelBtn = new JButton("Hủy");
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            dialog.add(mainPanel, BorderLayout.CENTER);

            cancelBtn.addActionListener(ev -> dialog.dispose());

            okBtn.addActionListener(ev -> {
                String managerId = managerIdField.getText().trim();
                String name = nameField.getText().trim();
                String email = emailFieldx.getText().trim();
                String phone = phoneFieldx.getText().trim();
                String address = addressField.getText().trim();
                String wageStr = wageField.getText().trim();
                String roleId = roles.get(roleCombo.getSelectedIndex()).getId();
                java.math.BigDecimal wage;
                try {
                    wage = new java.math.BigDecimal(wageStr);
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Lương theo giờ không hợp lệ!");
                    wageField.requestFocus();
                    return;
                }
                com.neostain.csms.model.Employee emp = new com.neostain.csms.model.Employee(
                        null, managerId, name, null, email, phone, address, wage, "ĐANG HOẠT ĐỘNG"
                );
                try {
                    boolean created = serviceManager.getManagementService().createEmployee(emp);
                    if (!created) throw new Exception("Không thể thêm nhân viên.");
                    // Lấy lại nhân viên vừa tạo bằng phone (unique)
                    com.neostain.csms.model.Employee newEmp = serviceManager.getManagementService().getEmployeeById(
                            serviceManager.getManagementService().getEmployeeByManagerId(managerId).stream()
                                    .filter(e1 -> e1.getPhoneNumber().equals(phone)).findFirst().orElseThrow().getId()
                    );
                    // Tạo account
                    String passwordHash = com.neostain.csms.util.PasswordUtils.hash("Java@123");
                    java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
                    com.neostain.csms.model.Account acc = new com.neostain.csms.model.Account(
                            null, newEmp.getId(), phone, passwordHash, roleId, now, "ĐANG HOẠT ĐỘNG"
                    );
                    boolean accCreated = serviceManager.getAuthService().createAccount(acc);
                    if (!accCreated) throw new Exception("Không thể tạo tài khoản cho nhân viên.");
                    // Hiện dialog thông tin nhân viên và tài khoản
                    JPanel infoPanel = new JPanel(new GridLayout(0, 2, 8, 8));
                    infoPanel.add(new JLabel("Mã nhân viên:"));
                    infoPanel.add(new JLabel(newEmp.getId()));
                    infoPanel.add(new JLabel("Tên nhân viên:"));
                    infoPanel.add(new JLabel(newEmp.getName()));
                    infoPanel.add(new JLabel("Email:"));
                    infoPanel.add(new JLabel(newEmp.getEmail()));
                    infoPanel.add(new JLabel("Số điện thoại:"));
                    infoPanel.add(new JLabel(newEmp.getPhoneNumber()));
                    infoPanel.add(new JLabel("Địa chỉ:"));
                    infoPanel.add(new JLabel(newEmp.getAddress()));
                    infoPanel.add(new JLabel("Lương theo giờ:"));
                    infoPanel.add(new JLabel(newEmp.getHourlyWage().toString()));
                    infoPanel.add(new JLabel("Trạng thái:"));
                    infoPanel.add(new JLabel(newEmp.getStatus()));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel("Đã tạo cho nhân viên 1 tài khoản mặc định:"));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel("Tên tài khoản:"));
                    infoPanel.add(new JLabel(phone));
                    infoPanel.add(new JLabel("Mật khẩu:"));
                    infoPanel.add(new JLabel("Java@123"));
                    infoPanel.add(new JLabel("Vai trò:"));
                    infoPanel.add(new JLabel(roleId + " - " + roles.get(roleCombo.getSelectedIndex()).getName()));
                    JOptionPane.showMessageDialog(dialog, infoPanel, "Thông tin nhân viên mới", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    resetBtn.doClick();
                } catch (DuplicateFieldException dfe) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi trùng dữ liệu", dfe.getMessage());
                    if ("phoneNumber".equals(dfe.getFieldName())) {
                        phoneFieldx.requestFocus();
                    } else {
                        emailFieldx.requestFocus();
                    }
                } catch (FieldValidationException fve) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi dữ liệu không hợp lệ", fve.getMessage());
                    if ("phoneNumber".equals(fve.getFieldName())) {
                        phoneFieldx.requestFocus();
                    } else {
                        emailFieldx.requestFocus();
                    }
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi", "Lỗi không xác định: " + ex.getMessage());
                }
            });

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        // Initialize table with all employees of the current store manager
        List<Employee> init = serviceManager.getManagementService().getEmployeeByManagerId(account.getEmployeeId());
        String[][] initData = this.toTableData(init);

        // Setup table with actions (delete/update)
        java.util.List<ScrollableTable.ActionDefinition> actions = createActions();
        this.employeeTable = new ScrollableTable(employeeColumns, initData, actions);

        // ----- Invoice List Section -----
        BorderedPanel employeeTableBordered = new BorderedPanel("Danh sách nhân viên");
        employeeTableBordered.setLayout(new BorderLayout(10, 10));
        employeeTableBordered.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        employeeTableBordered.add(employeeTable);
        this.add(employeeTableBordered, BorderLayout.CENTER);
    }

    private String[][] toTableData(List<Employee> list) {
        String[][] data = new String[list.size()][employeeColumns.length];
        for (int i = 0; i < list.size(); i++) {
            Employee m = list.get(i);
            data[i] = new String[]{
                    m.getId(),
                    m.getName(),
                    df1.format(m.getHireDate()),
                    m.getEmail(),
                    m.getPhoneNumber(),
                    m.getAddress(),
                    String.valueOf(m.getHourlyWage()),
                    m.getStatus()
            };
        }
        return data;
    }

    private List<ScrollableTable.ActionDefinition> createActions() {
        return List.of(
                new ScrollableTable.ActionDefinition("Tiến hành nghỉ việc", "Tiến hành", (row, table) -> {
                    String id = table.getValueAt(row, 0).toString();
                    String name = table.getValueAt(row, 1).toString();
                    boolean confirm = DialogFactory.showConfirmYesNoDialog(
                            this,
                            "Xác nhận nghỉ việc",
                            "Bạn có chắc chắn muốn chuyển trạng thái nhân viên " + name + " (" + id + ") sang Nghỉ việc không?"
                    );
                    if (confirm) {
                        try {
                            // Update employee status to "ĐÃ NGHỈ VIỆC"
                            Employee emp = serviceManager.getManagementService().getEmployeeById(id);
                            if (emp != null) {
                                emp.setStatus("ĐÃ NGHỈ VIỆC");
                                serviceManager.getManagementService().updateEmployee(emp);
                                DialogFactory.showInfoDialog(this, "Thành công", "Đã chuyển trạng thái nhân viên " + name + " sang Nghỉ việc.");
                                // Refresh the table
                                List<Employee> results = serviceManager.getManagementService().getEmployeeByManagerId(account.getEmployeeId());
                                String[][] data = this.toTableData(results);
                                employeeTable.refreshData(data);
                            } else {
                                DialogFactory.showErrorDialog(this, "Lỗi", "Không tìm thấy thông tin nhân viên.");
                            }
                        } catch (Exception ex) {
                            DialogFactory.showErrorDialog(this, "Lỗi", "Không thể cập nhật trạng thái nhân viên: " + ex.getMessage());
                        }
                    }
                }),
                new ScrollableTable.ActionDefinition("Cập nhật", "Cập nhật thông tin", (row, table) -> {
                    String id = table.getValueAt(row, 0).toString();
                    String managerId = serviceManager.getManagementService().getEmployeeById(id).getManagerId();
                    String name = table.getValueAt(row, 1).toString();
                    String hireDate = table.getValueAt(row, 2).toString();
                    String email = table.getValueAt(row, 3).toString();
                    String phoneNumber = table.getValueAt(row, 4).toString();
                    String address = table.getValueAt(row, 5).toString();
                    String hourlyWage = table.getValueAt(row, 6).toString();

                    // === BƯỚC 1: Hiện dialog sửa thông tin ===
                    JDialog dialog = new JDialog(
                            (Frame) SwingUtilities.getWindowAncestor(this),
                            "Cập nhật thông tin nhân viên: " + id,
                            true
                    );

                    dialog.setLayout(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.insets = new Insets(8, 8, 8, 8);
                    gbc.fill = GridBagConstraints.HORIZONTAL;

                    // Form gồm 9 dòng x 2 cột
                    JPanel form = new JPanel(new GridLayout(8, 2, 8, 8));

                    // 1. Mã nhân viên quản lý (editable)
                    form.add(new JLabel("Mã nhân viên quản lý:"));
                    JTextField managerIdField = new JTextField(managerId, 10);
                    form.add(managerIdField);

                    // 2. Tên (editable)
                    form.add(new JLabel("Tên nhân viên"));
                    JTextField nameField = new JTextField(name, 10);
                    form.add(nameField);

                    // 3. Ngày đăng ký (readonly)
                    form.add(new JLabel("Ngày đăng ký:"));
                    JTextField dateField = new JTextField(hireDate, 20);
                    dateField.setEditable(false);
                    form.add(dateField);

                    // 4. SĐT (editable)
                    form.add(new JLabel("Số điện thoại:"));
                    JTextField phoneField = new JTextField(phoneNumber, 20);
                    form.add(phoneField);

                    // 5. Email (editable)
                    form.add(new JLabel("Email:"));
                    JTextField emailField = new JTextField(email, 20);
                    form.add(emailField);

                    // 6. Địa chỉ (editable)
                    form.add(new JLabel("Địa chỉ:"));
                    JTextField addressField = new JTextField(address, 20);
                    form.add(addressField);

                    // 7. Lương (editable)
                    form.add(new JLabel("Lương (đ/giờ):"));
                    JTextField hourlyWageField = new JTextField(hourlyWage, 20);
                    form.add(hourlyWageField);

                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.gridwidth = 2;
                    dialog.add(form, gbc);

                    // Nút Cập nhật / Hủy
                    JPanel btnP = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
                    JButton okBtn = new JButton("Cập nhật");
                    JButton cancelBtn = new JButton("Hủy");
                    btnP.add(okBtn);
                    btnP.add(cancelBtn);

                    gbc.gridy = 1;
                    dialog.add(btnP, gbc);

                    // Hủy → đóng dialog
                    cancelBtn.addActionListener(ev -> dialog.dispose());

                    // Xử lý Cập nhật
                    Employee e = serviceManager.getManagementService().getEmployeeById(id);

                    okBtn.addActionListener(ev -> {
                        // Lấy giá trị cũ
                        String oldManagerId = e.getManagerId();
                        String oldName = e.getName();
                        String oldPhone = e.getPhoneNumber();
                        String oldEmail = e.getEmail();
                        String oldAddress = e.getAddress();
                        String oldHourlyWage = e.getHourlyWage().toString();

                        // Lấy giá trị mới
                        String newManagerId = managerIdField.getText().trim();
                        String newName = nameField.getText().trim();
                        String newPhone = phoneField.getText().trim();
                        String newEmail = emailField.getText().trim();
                        String newAddress = addressField.getText().trim();
                        String newHourlyWage = hourlyWageField.getText().trim();

                        // Đặt lại trên model
                        e.setManagerId(newManagerId);
                        e.setName(newName);
                        e.setPhoneNumber(newPhone);
                        e.setEmail(newEmail);
                        e.setAddress(newAddress);
                        e.setHourlyWage(new BigDecimal(newHourlyWage).setScale(2, RoundingMode.DOWN));

                        try {
                            serviceManager.getManagementService().updateEmployee(e);
                            JPanel infoPanel = new JPanel(new GridLayout(0, 4, 8, 8));
                            infoPanel.add(new JLabel("Cập nhật nhân viên: "));
                            infoPanel.add(new JLabel(e.getId()));
                            infoPanel.add(new JLabel());
                            infoPanel.add(new JLabel());
                            infoPanel.add(new JLabel("Mã nhân viên quản lý: "));
                            infoPanel.add(new JLabel(oldManagerId));
                            infoPanel.add(new JLabel("          >>>"));
                            infoPanel.add(new JLabel(newManagerId));
                            infoPanel.add(new JLabel("Tên: "));
                            infoPanel.add(new JLabel(oldName));
                            infoPanel.add(new JLabel("          >>>"));
                            infoPanel.add(new JLabel(newName));
                            infoPanel.add(new JLabel("Số điện thoại: "));
                            infoPanel.add(new JLabel(oldPhone));
                            infoPanel.add(new JLabel("          >>>"));
                            infoPanel.add(new JLabel(newPhone));
                            infoPanel.add(new JLabel("Email: "));
                            infoPanel.add(new JLabel(oldEmail));
                            infoPanel.add(new JLabel("          >>>"));
                            infoPanel.add(new JLabel(newEmail));
                            infoPanel.add(new JLabel("Địa chỉ: "));
                            infoPanel.add(new JLabel(oldAddress));
                            infoPanel.add(new JLabel("          >>>"));
                            infoPanel.add(new JLabel(newAddress));
                            infoPanel.add(new JLabel("Lương (đ/giờ): "));
                            infoPanel.add(new JLabel(oldHourlyWage));
                            infoPanel.add(new JLabel("          >>>"));
                            infoPanel.add(new JLabel(newHourlyWage));
                            DialogFactory.showInfoDialog(
                                    dialog,
                                    "Cập nhật thành công!",
                                    infoPanel
                            );
                            // Làm mới bảng ngoài
                            List<Employee> results = serviceManager.getManagementService().getEmployeeByManagerId(account.getEmployeeId());
                            String[][] data = this.toTableData(results);
                            employeeTable.refreshData(data);
                            dialog.dispose();
                        } catch (DuplicateFieldException dfe) {
                            // Bắt lỗi trùng số/email, giữ dialog mở
                            DialogFactory.showErrorDialog(
                                    dialog,
                                    "Lỗi trùng dữ liệu",
                                    dfe.getMessage()
                            );
                            if ("phoneNumber".equals(dfe.getFieldName())) {
                                phoneField.requestFocus();
                            } else {
                                emailField.requestFocus();
                            }
                        } catch (FieldValidationException fve) {
                            DialogFactory.showErrorDialog(
                                    dialog,
                                    "Lỗi dữ liệu không hợp lệ",
                                    fve.getMessage()
                            );
                            if ("phoneNumber".equals(fve.getFieldName())) {
                                phoneField.requestFocus();
                            } else {
                                emailField.requestFocus();
                            }
                        } catch (Exception ex) {
                            DialogFactory.showErrorDialog(
                                    dialog,
                                    "Lỗi",
                                    "Lỗi không xác định: " + ex.getMessage()
                            );
                            dialog.dispose();
                        }
                    });

                    dialog.pack();
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                }));
    }
}
