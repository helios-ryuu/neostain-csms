package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Member;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.util.exception.FieldValidationException;
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

public class MemberPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final JButton searchBtn = new StandardButton(this, "Tìm kiếm");
    private final JButton resetBtn = new StandardButton(this, "Đặt lại");
    private final JButton registerBtn = new StandardButton(this, "Đăng ký thành viên");
    private final String[] memberColumns = {"Mã thành viên", "Tên", "Số điện thoại", "Email", "Ngày đăng ký", "Số điểm tích lũy"};
    private final SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    private ScrollableTable memberTable;

    public MemberPanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        // Create panels with GridBagLayout
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search tools in a titled border panel
        BorderedPanel toolWrapper = new BorderedPanel("Tìm kiếm thành viên");
        toolWrapper.setLayout(new BorderLayout(10, 10));
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.X_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Input fields
        JTextField idField = new JTextField(10);
        JTextField phoneField = new JTextField(10);
        JTextField emailField = new JTextField(12);

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

        toolWrapper.add(new JLabel("Mã thành viên:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(idField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Số điện thoại:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(phoneField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Email:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(emailField);
        toolWrapper.add(Box.createHorizontalStrut(15));

        toolWrapper.add(new JLabel("Ngày đăng kí: Từ"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(dateFromSpinner);

        toolWrapper.add(Box.createHorizontalStrut(8));
        toolWrapper.add(new JLabel("đến"));
        toolWrapper.add(Box.createHorizontalStrut(8));

        toolWrapper.add(dateToSpinner);
        toolWrapper.add(Box.createHorizontalStrut(15));
        toolWrapper.add(Box.createHorizontalStrut(8));

        toolWrapper.add(searchBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(resetBtn);
        toolWrapper.add(Box.createHorizontalStrut(5));
        toolWrapper.add(registerBtn);

        // Button listeners
        searchBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            Date fromDate = (Date) dateFromSpinner.getValue();
            Date toDate = (Date) dateToSpinner.getValue();
            String from = (fromDate != null) ? df2.format(fromDate) : "";
            String to = (toDate != null) ? df2.format(toDate) : "";

            List<Member> results = serviceManager.getManagementService()
                    .searchMembers(id, phone, email, from, to);

            if (results.isEmpty()) {
                DialogFactory.showErrorDialog(this, "Thông báo", "Không có kết quả tìm kiếm!");
            } else {
                String[][] data = this.toTableData(results);
                memberTable.refreshData(data);
            }
        });

        resetBtn.addActionListener(e -> {
            // 1. Xoá bộ lọc text
            idField.setText("");
            phoneField.setText("");
            emailField.setText("");

            // 2. Đưa spinner về mặc định
            dateFromSpinner.setValue(defaultFrom);
            dateToSpinner.setValue(defaultTo);

            // 3. Tìm kiếm lại (lần này từ–đến rộng nhất => load all)
            searchBtn.doClick();
        });

        registerBtn.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đăng ký thành viên", true);
            dialog.setLayout(new BorderLayout(10, 10));
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            JLabel titleLabel = new JLabel("Đăng ký thành viên mới", SwingConstants.CENTER);
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
            mainPanel.add(titleLabel, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            JLabel nameLabel = new JLabel("Nhập họ và tên:");
            JTextField nameField = new JTextField(20);
            JLabel phoneLabel = new JLabel("Nhập số điện thoại:");
            JTextField phoneFieldx = new JTextField(20);
            JLabel emailLabel = new JLabel("Nhập email:");
            JTextField emailFieldx = new JTextField(20);
            formPanel.add(nameLabel);
            formPanel.add(nameField);
            formPanel.add(phoneLabel);
            formPanel.add(phoneFieldx);
            formPanel.add(emailLabel);
            formPanel.add(emailFieldx);
            mainPanel.add(formPanel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton okBtn = new JButton("Đăng ký");
            JButton cancelBtn = new JButton("Hủy đăng ký");
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            dialog.add(mainPanel, BorderLayout.CENTER);

            cancelBtn.addActionListener(ev -> dialog.dispose());

            okBtn.addActionListener(ev -> {
                String name = nameField.getText().trim();
                String phone = phoneFieldx.getText().trim();
                String email = emailFieldx.getText().trim();
                if (name.isEmpty()) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi dữ liệu không hợp lệ", "Tên không được để trống");
                } else if (phone.isEmpty()) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi dữ liệu không hợp lệ", "Số điện thoại không được để trống");
                } else if (email.isEmpty()) {
                    DialogFactory.showErrorDialog(dialog, "Lỗi dữ liệu không hợp lệ", "Email không được để trống");
                } else {
                    // Step 1: Show confirmation dialog
                    JPanel infoPanel = new JPanel(new GridLayout(3, 2, 8, 8));
                    infoPanel.add(new JLabel("Họ và tên:"));
                    infoPanel.add(new JLabel(name));
                    infoPanel.add(new JLabel("Số điện thoại:"));
                    infoPanel.add(new JLabel(phone));
                    infoPanel.add(new JLabel("Email:"));
                    infoPanel.add(new JLabel(email));
                    int confirm = JOptionPane.showConfirmDialog(
                            dialog,
                            infoPanel,
                            "Bạn có xác nhận đăng ký thành viên với thông tin sau?",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (confirm == JOptionPane.OK_OPTION) {
                        try {
                            serviceManager.getManagementService().createMember(name, phone, email);
                            dialog.dispose();
                            resetBtn.doClick();
                            DialogFactory.showInfoDialog(this, "Thành công", "Đăng ký thành viên thành công!");
                        } catch (com.neostain.csms.util.exception.DuplicateFieldException dfe) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi trùng dữ liệu", dfe.getMessage());
                            if ("phoneNumber".equals(dfe.getFieldName())) {
                                phoneFieldx.requestFocus();
                            } else {
                                emailFieldx.requestFocus();
                            }
                        } catch (com.neostain.csms.util.exception.FieldValidationException fve) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi dữ liệu không hợp lệ", fve.getMessage());
                            if ("phoneNumber".equals(fve.getFieldName())) {
                                phoneFieldx.requestFocus();
                            } else {
                                emailFieldx.requestFocus();
                            }
                        } catch (Exception ex) {
                            DialogFactory.showErrorDialog(dialog, "Lỗi", "Lỗi không xác định: " + ex.getMessage());
                        }
                    }
                    // else: do nothing, stay in dialog
                }
            });

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        this.add(toolWrapper, BorderLayout.NORTH);

        // Initialize table with all members
        java.util.List<Member> init = serviceManager.getManagementService().getAllMembers();
        String[][] InitData = this.toTableData(init);

        // Setup table with actions (delete/update)
        java.util.List<ScrollableTable.ActionDefinition> actions = createActions();
        this.memberTable = new ScrollableTable(memberColumns, InitData, actions);

        // ----- Member List Section -----
        BorderedPanel memberTableBordered = new BorderedPanel("Danh sách thành viên");
        memberTableBordered.setLayout(new BorderLayout(10, 10));
        memberTableBordered.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        memberTableBordered.add(memberTable);
        this.add(memberTableBordered, BorderLayout.CENTER);
    }

    private String[][] toTableData(List<Member> list) {
        String[][] data = new String[list.size()][memberColumns.length];
        for (int i = 0; i < list.size(); i++) {
            Member m = list.get(i);
            data[i] = new String[]{
                    m.getId(),
                    m.getName(),
                    m.getPhoneNumber(),
                    m.getEmail(),
                    df1.format(m.getRegistrationDate()),
                    String.valueOf(m.getLoyaltyPoints())
            };
        }
        return data;
    }

    private List<ScrollableTable.ActionDefinition> createActions() {
        return List.of(
                new ScrollableTable.ActionDefinition("Xóa", "Xóa", (rowIndex, table) -> {
                    String memberId = table.getValueAt(rowIndex, 0).toString();
                    boolean confirm = DialogFactory.showConfirmYesNoDialog(
                            this,
                            "Xác nhận xóa",
                            "Bạn có chắc muốn xóa thành viên " + memberId + "?"
                    );

                    if (!confirm) return;
                    try {
                        serviceManager.getManagementService().deleteMember(memberId);
                        // remove dòng UI
                        resetBtn.doClick();
                        DialogFactory.showInfoDialog(this, "Thông báo",
                                "Xóa thành công thành viên " + memberId);
                    } catch (Exception ex) {
                        DialogFactory.showErrorDialog(this, "Lỗi",
                                "Không thể xóa: " + ex.getMessage());
                    }
                }),
                new ScrollableTable.ActionDefinition("Sửa", "Cập nhật", ((rowIndex, table) -> {
                    String id = table.getValueAt(rowIndex, 0).toString();
                    String name = table.getValueAt(rowIndex, 1).toString();
                    String phoneNumber = table.getValueAt(rowIndex, 2).toString();
                    String email = table.getValueAt(rowIndex, 3).toString();
                    String registrationDate = table.getValueAt(rowIndex, 4).toString();
                    int loyaltyPoints = Integer.parseInt(table.getValueAt(rowIndex, 5).toString());

                    // === BƯỚC 1: Hiện dialog sửa thông tin ===
                    JDialog dialog = new JDialog(
                            (Frame) SwingUtilities.getWindowAncestor(this),
                            "Cập nhật thông tin thành viên: " + id,
                            true
                    );

                    dialog.setLayout(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.insets = new Insets(8, 8, 8, 8);
                    gbc.fill = GridBagConstraints.HORIZONTAL;

                    // Form gồm 5 dòng x 2 cột
                    JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
                    // 1. Tên (editable)
                    form.add(new JLabel("Tên thành viên:"));
                    JTextField nameField = new JTextField(name, 20);
                    form.add(nameField);

                    // 2. SĐT (editable)
                    form.add(new JLabel("Số điện thoại:"));
                    JTextField phoneField = new JTextField(phoneNumber, 20);
                    form.add(phoneField);

                    // 3. Email (editable)
                    form.add(new JLabel("Email:"));
                    JTextField emailField = new JTextField(email, 20);
                    form.add(emailField);

                    // 4. Ngày đăng ký (readonly)
                    form.add(new JLabel("Ngày đăng ký:"));
                    JTextField dateField = new JTextField(registrationDate, 20);
                    dateField.setEditable(false);
                    form.add(dateField);

                    // 5. Điểm tích lũy (readonly)
                    form.add(new JLabel("Số điểm tích lũy:"));
                    JTextField pointsField = new JTextField(
                            String.valueOf(loyaltyPoints),
                            20
                    );
                    pointsField.setEditable(false);
                    form.add(pointsField);

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
                    Member m = serviceManager.getManagementService().getMemberById(id);
                    okBtn.addActionListener(ev -> {
                        // Lấy giá trị cũ
                        String oldName = m.getName();
                        String oldPhone = m.getPhoneNumber();
                        String oldEmail = m.getEmail();

                        // Lấy giá trị mới
                        String newName = nameField.getText().trim();
                        String newPhone = phoneField.getText().trim();
                        String newEmail = emailField.getText().trim();

                        // Đặt lại trên model
                        m.setName(newName);
                        m.setPhoneNumber(newPhone);
                        m.setEmail(newEmail);

                        try {
                            serviceManager.getManagementService().updateMember(m);
                            JPanel infoPanel = new JPanel(new GridLayout(4, 4, 8, 8));
                            infoPanel.add(new JLabel("Cập nhật thành viên: "));
                            infoPanel.add(new JLabel(m.getId()));
                            infoPanel.add(new JLabel());
                            infoPanel.add(new JLabel());
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
                            DialogFactory.showInfoDialog(
                                    dialog,
                                    "Cập nhật thành công!",
                                    infoPanel
                            );
                            // Làm mới bảng ngoài
                            List<Member> updated = serviceManager.getManagementService().getAllMembers();
                            String[][] newData = this.toTableData(updated);
                            memberTable.refreshData(newData);
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
                })),
                new ScrollableTable.ActionDefinition("Lịch sử tích điểm", "Lịch sử tích điểm", (rowIndex, table) -> {
                    String memberId = table.getValueAt(rowIndex, 0).toString();
                    java.util.List<com.neostain.csms.model.PointUpdateLog> logs = serviceManager.getSaleService().getPointLogsByMember(memberId);
                    String[] columns = {"Mã hóa đơn", "Số điểm thay đổi"};
                    Object[][] data = new Object[logs.size()][2];
                    for (int i = 0; i < logs.size(); i++) {
                        data[i][0] = logs.get(i).getInvoiceId();
                        data[i][1] = logs.get(i).getPointChange();
                    }
                    ScrollableTable tableLog = new ScrollableTable(columns, data, java.util.List.of());
                    JPanel panel = new JPanel(new BorderLayout(10, 10));
                    panel.add(tableLog, BorderLayout.CENTER);
                    JButton okBtn = new JButton("OK");
                    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    btnPanel.add(okBtn);
                    panel.add(btnPanel, BorderLayout.SOUTH);
                    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Lịch sử tích điểm", true);
                    dialog.setContentPane(panel);
                    dialog.setSize(400, 300);
                    dialog.setLocationRelativeTo(this);
                    okBtn.addActionListener(ev -> dialog.dispose());
                    dialog.setVisible(true);
                })
        );
    }
}
