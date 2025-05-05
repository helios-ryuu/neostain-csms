package com.neostain.csms.view.screen.sm.dashboard;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Member;
import com.neostain.csms.model.Store;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.view.component.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

/**
 * Dashboard panel for the store manager screen
 */
public class DashboardPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(DashboardPanel.class.getName());
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    // Constants for card layout
    private static final String SALES_STATISTICS_PANEL = "salesStatisticsPanel";
    private static final String STORE_INFO_PANEL = "storeInfoPanel";
    private static final String MEMBER_PANEL = "memberPanel";
    private static final String EMPLOYEE_PANEL = "employeePanel";

    private final String username;
    private JLabel storeName;
    private JLabel storeAddress;
    private JLabel managerId;
    private JLabel managerName;
    // Panel components
    private BorderedPanel mainDashboardPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private ScrollableTable memberTable;

    /**
     * Creates a new dashboard panel
     */
    public DashboardPanel(String username) {
        this.username = username;
        initializeComponents();
    }

    private void initializeComponents() {
        // Create panel with GridBagLayout
        this.setLayout(new GridBagLayout());
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create a menu panel with a standardized component
        StandardMenu menuPanel = createMenuPanel();
        menuPanel.setPreferredSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 600));
        menuPanel.setMinimumSize(new Dimension(Constants.View.MENU_PANEL_WIDTH, 200));

        // Add menu panel to left side
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        this.add(menuPanel, gbc);

        // Create card layout for switching between panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create dashboard content panels
        JPanel salesStatisticsPanel = createSalesStatisticsPanel();
        JPanel storeInfoContentPanel = createStoreInfoContentPanel();
        JPanel memberContentPanel = createMemberContentPanel();
        JPanel employeeContentPanel = createEmployeeContentPanel();

        // Add panels to card layout
        cardPanel.add(salesStatisticsPanel, SALES_STATISTICS_PANEL);
        cardPanel.add(storeInfoContentPanel, STORE_INFO_PANEL);
        cardPanel.add(memberContentPanel, MEMBER_PANEL);
        cardPanel.add(employeeContentPanel, EMPLOYEE_PANEL);

        // Create overall dashboard panel
        mainDashboardPanel = new BorderedPanel("Thống kê bán hàng");
        mainDashboardPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        mainDashboardPanel.setLayout(new BorderLayout());
        mainDashboardPanel.add(cardPanel, BorderLayout.CENTER);

        // Set the preferred size for a dashboard panel
        mainDashboardPanel.setPreferredSize(new Dimension(800, 600));

        // Add a dashboard panel to the right side
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        this.add(mainDashboardPanel, gbc);

        // Show default panel (sales statistics)
        cardLayout.show(cardPanel, SALES_STATISTICS_PANEL);
    }

    private StandardMenu createMenuPanel() {
        StandardMenu menuPanel = new StandardMenu("Dashboard");
        menuPanel.addMenuItem("Thống kê bán hàng", item -> {
            LOGGER.info("Sales statistics selected");
            // Show sales statistics panel
            mainDashboardPanel.setTitle("Thống kê bán hàng");
            cardLayout.show(cardPanel, SALES_STATISTICS_PANEL);
        });

        menuPanel.addMenuItem("Thông tin cửa hàng", item -> {
            LOGGER.info("Store information selected");
            // Show store information panel
            mainDashboardPanel.setTitle("Thông tin cửa hàng");
            cardLayout.show(cardPanel, STORE_INFO_PANEL);
        });

        menuPanel.addMenuItem("Khách hàng thành viên", item -> {
            LOGGER.info("Member customers selected");
            // Show member customers panel
            mainDashboardPanel.setTitle("Quản lý thành viên");
            cardLayout.show(cardPanel, MEMBER_PANEL);
        });

        return menuPanel;
    }

    private JPanel createSalesStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create dashboard tiles panel
        JPanel tilesPanel = createTilesPanel();
        tilesPanel.setOpaque(false);
        tilesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(tilesPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTilesPanel() {
        JPanel tilesPanel = new JPanel(new GridLayout(2, 4, 10, 10));

        // Add dashboard tiles for the first row-sales statistics
        // TODO: Kết nối dữ liệu thống kê từ cơ sở dữ liệu
        tilesPanel.add(new DashboardTile("Tổng hoá đơn", "128", new Color(41, 128, 185)));
        tilesPanel.add(new DashboardTile("Hoá đơn hôm nay", "24", new Color(39, 174, 96)));
        tilesPanel.add(new DashboardTile("Doanh thu hôm nay", "5,280,000 VNĐ", new Color(211, 84, 0)));
        tilesPanel.add(new DashboardTile("Doanh thu tháng", "42,750,000 VNĐ", new Color(142, 68, 173)));

        // Add dashboard tiles for the second row-additional statistics
        tilesPanel.add(new DashboardTile("Sản phẩm bán chạy", "Cà phê", new Color(52, 152, 219)));
        tilesPanel.add(new DashboardTile("Khách hàng mới", "15", new Color(46, 204, 113)));
        tilesPanel.add(new DashboardTile("Tổng nhân viên", "12", new Color(230, 126, 34)));
        tilesPanel.add(new DashboardTile("Đơn hàng online", "8", new Color(155, 89, 182)));

        return tilesPanel;
    }

    private JPanel createStoreInfoContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create store information panel
        JPanel storeInfoPanel = createStoreInfoPanel();
        panel.add(storeInfoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStoreInfoPanel() {
        Account account = serviceManager.getAccountService().getAccountByUsername(this.username);
        Store store = serviceManager.getStoreService().getStoreByManagerId(account.getEmployeeId());
        Employee employee = serviceManager.getEmployeeService().getEmployeeById(store.getManagerId());

        JPanel storeInfoPanel = new JPanel(new BorderLayout());
        storeInfoPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Tool panel for store info actions
        JPanel storeToolPanel = new JPanel();

        storeToolPanel.setLayout(new BoxLayout(storeToolPanel, BoxLayout.Y_AXIS));
        storeToolPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JButton editStoreInfoButton = new StandardButton(this, "Cập nhật tên cửa hàng");
        editStoreInfoButton.addActionListener(e -> {
            // 1. Lấy tên cũ từ model
            String currentName = store.getStoreName();

            // 2. Tạo panel chứa label + field
            JTextField nameField = new JTextField(currentName, 25);
            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 8, 8));
            inputPanel.add(new JLabel("Tên cửa hàng hiện tại:"));
            inputPanel.add(new JLabel(currentName));
            inputPanel.add(new JLabel("Tên mới:"));
            inputPanel.add(nameField);

            // 3. Hiện dialog với 2 nút Update / Cancel
            int option = DialogFactory.showConfirmOkCancelDialog(
                    this,
                    "Cập nhật tên cửa hàng",
                    inputPanel
            );

            // 4. Xử lý khi nhấn OK
            if (option == JOptionPane.OK_OPTION) {
                String newName = nameField.getText().trim();
                if (StringUtils.isNullOrEmpty(newName)) {
                    DialogFactory.showErrorDialog(
                            this,
                            "Lỗi",
                            "Tên cửa hàng không được để trống!"
                    );
                    return;
                }

                try {
                    // 5. Gọi service để lưu thay đổi (giả sử có method updateStoreName)
                    serviceManager.getStoreService()
                            .changeStoreName(store.getStoreId(), newName);

                    // 6. Cập nhật UI ngay lập tức
                    store.setStoreName(newName);  // update model local
                    storeName.setText(newName);  // label hiển thị trên form

                    DialogFactory.showInfoDialog(
                            this,
                            "Thông báo",
                            "Cập nhật thành công!"
                    );
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(
                            this,
                            "Lỗi",
                            "Lỗi khi cập nhật: " + ex.getMessage()
                    );
                }
            }
        });

        JButton updateContactButton = new StandardButton(this, "Cập nhật địa chỉ cửa hàng");
        updateContactButton.addActionListener(e -> {
            // 1. Lấy địa chỉ cũ từ model
            String currentAddress = store.getStoreAddress();

            // 2. Tạo panel chứa label + field
            JTextField addressField = new JTextField(currentAddress, 25);
            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 8, 8));
            inputPanel.add(new JLabel("Địa chỉ cửa hàng hiện tại:"));
            inputPanel.add(new JLabel(currentAddress));
            inputPanel.add(new JLabel("Địa chỉ mới:"));
            inputPanel.add(addressField);

            // 3. Hiện dialog với 2 nút Update / Cancel
            int option = DialogFactory.showConfirmOkCancelDialog(
                    this,
                    "Cập nhật địa chỉ cửa hàng",
                    inputPanel
            );

            // 4. Xử lý khi nhấn OK
            if (option == JOptionPane.OK_OPTION) {
                String newAddress = addressField.getText().trim();
                if (StringUtils.isNullOrEmpty(newAddress)) {
                    DialogFactory.showErrorDialog(
                            this,
                            "Lỗi",
                            "Địa chỉ cửa hàng không được để trống!"
                    );
                    return;
                }

                try {
                    // 5. Gọi service để lưu thay đổi (giả sử có method updateStoreAddress)
                    serviceManager.getStoreService()
                            .changeStoreAddress(store.getStoreId(), newAddress);

                    // 6. Cập nhật UI ngay lập tức
                    store.setStoreAddress(newAddress);  // update model local
                    storeAddress.setText(newAddress);  // label hiển thị trên form

                    DialogFactory.showInfoDialog(
                            this,
                            "Thông báo",
                            "Cập nhật thành công!"
                    );
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(
                            this,
                            "Lỗi",
                            "Lỗi khi cập nhật: " + ex.getMessage()
                    );
                }
            }
        });

        editStoreInfoButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateContactButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        storeToolPanel.add(editStoreInfoButton);
        storeToolPanel.add(Box.createVerticalStrut(5));
        storeToolPanel.add(updateContactButton);
        storeToolPanel.add(Box.createVerticalStrut(5));

        storeInfoPanel.add(storeToolPanel, BorderLayout.WEST);

        JPanel storeInfoContent = new JPanel(new GridBagLayout());
        storeInfoContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        storeInfoContent.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Giữ reference cho các JLabel hiện thị dữ liệu
        this.storeName = new JLabel(store.getStoreName());
        this.storeAddress = new JLabel(store.getStoreAddress());
        this.managerId = new JLabel(store.getManagerId());
        this.managerName = new JLabel(employee.getEmployeeName());

        // Thêm lần lượt các row, chỉ với 1 dòng gọi helper mỗi row
        addRow(storeInfoContent, "Tên cửa hàng:", this.storeName, 0);
        addRow(storeInfoContent, "Địa chỉ:", this.storeAddress, 1);
        addRow(storeInfoContent, "Mã nhân viên quản lý cửa hàng:", this.managerId, 2);
        addRow(storeInfoContent, "Tên nhân viên quản lý cửa hàng:", this.managerName, 3);

        // Sau khi đã addRow(… row=0..3)
        int lastRow = 4;
        GridBagConstraints gbcFiller = new GridBagConstraints();
        gbcFiller.gridx = 0;
        gbcFiller.gridy = lastRow;
        gbcFiller.gridwidth = 2;           // lấp cả 2 cột
        gbcFiller.weighty = 1.0;         // chiếm toàn bộ không gian dư thừa
        gbcFiller.fill = GridBagConstraints.VERTICAL;
        storeInfoContent.add(Box.createVerticalGlue(), gbcFiller);

        storeInfoPanel.add(storeInfoContent, BorderLayout.CENTER);

        return storeInfoPanel;
    }

    /**
     * Tạo một GridBagConstraints đã config sẵn.
     */
    private GridBagConstraints makeGbc(int gridx, int gridy, double weightx) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 8, 8);
        return gbc;
    }

    /**
     * Thêm một dòng gồm label và component vào panel GridBagLayout.
     */
    private void addRow(JPanel panel, String labelText, JComponent dataComp, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.BOLD, Constants.Font.DEFAULT_SIZE));
        panel.add(label, makeGbc(0, row, 0.1));
        panel.add(dataComp, makeGbc(1, row, 0.9));
    }

    private JPanel createMemberContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create member panel
        JPanel memberPanel = createMemberPanel();
        panel.add(memberPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMemberPanel() {
        JPanel memberPanel = new JPanel(new BorderLayout());
        memberPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Tool panel for member actions
        JPanel memberToolPanel = new JPanel();
        memberToolPanel.setLayout(new BoxLayout(memberToolPanel, BoxLayout.Y_AXIS));
        memberToolPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JButton memberSearchWithIdButton = new StandardButton(this, "Tìm kiếm thành viên bằng ID");
        memberSearchWithIdButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(1, 2, 8, 8));

            // 1. Tạo field
            JTextField idField = new JTextField(20);
            inputPanel.add(new JLabel("Mã thành viên cần tìm:"));
            inputPanel.add(idField);

            int option = DialogFactory.showConfirmOkCancelDialog(
                    this,
                    "Tìm kiếm thành viên",
                    inputPanel
            );

            if (option == JOptionPane.OK_OPTION) {
                try {
                    Member result = serviceManager.getMemberService().getMemberById(idField.getText().trim());
                    if (result == null || result.getMemberId().isEmpty()) {
                        DialogFactory.showErrorDialog(this, "Thông báo", "Không tìm thấy kết quả nào!");
                    } else {
                        refreshTable(result);
                    }
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(this, "Lỗi", "Lỗi nhập liệu: " + ex.getMessage());
                }
            }
        });

        JButton memberSearchWithPhoneNumberButton = new StandardButton(this, "Tìm kiếm thành viên bằng số điện thoại");
        memberSearchWithPhoneNumberButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(1, 2, 8, 8));

            // 1. Tạo field
            JTextField phoneField = new JTextField(20);
            inputPanel.add(new JLabel("Số điện thoại thành viên cần tìm:"));
            inputPanel.add(phoneField);

            int option = DialogFactory.showConfirmOkCancelDialog(
                    this,
                    "Tìm kiếm thành viên",
                    inputPanel
            );

            if (option == JOptionPane.OK_OPTION) {
                try {
                    Member result = serviceManager.getMemberService().getMemberByPhoneNumber(phoneField.getText().trim());
                    if (result == null || result.getMemberId().isEmpty()) {
                        DialogFactory.showErrorDialog(this, "Thông báo", "Không tìm thấy kết quả nào!");
                    } else {
                        refreshTable(result);
                    }
                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(this, "Lỗi", "Lỗi nhập liệu: " + ex.getMessage());
                }
            }
        });

        JButton memberSearchAllButton = new StandardButton(this, "Liệt kê toàn bộ thành viên");
        memberSearchAllButton.addActionListener(e -> {
            try {
                List<Member> result = serviceManager.getMemberService().getAllMembers();
                if (result.isEmpty()) {
                    DialogFactory.showErrorDialog(this, "Thông báo", "Không tìm thấy kết quả nào!");
                } else {
                    refreshTable(result);
                }
            } catch (Exception ex) {
                DialogFactory.showErrorDialog(this, "Lỗi", "Lỗi nhập liệu: " + ex.getMessage());
            }
        });

        JButton memberUpdateButton = new StandardButton(this, "Cập nhật thông tin thành viên");
        memberUpdateButton.addActionListener(e -> {
            String memberId;         // sẽ lưu ID sau khi nhập đúng
            Member m;                // đối tượng tìm được

            // === BƯỚC 1: Nhập và kiểm tra ID ===
            while (true) {
                // Panel nhập ID
                JPanel idPanel = new JPanel(new GridLayout(1, 2, 8, 8));
                idPanel.add(new JLabel("ID thành viên:"));
                JTextField idField = new JTextField(20);
                idPanel.add(idField);

                int opt = DialogFactory.showConfirmOkCancelDialog(
                        this,
                        "Nhập ID thành viên cần cập nhật",
                        idPanel
                );
                if (opt != JOptionPane.OK_OPTION) {
                    // Nếu nhấn Hủy hoặc đóng dialog → thoát luôn
                    return;
                }

                memberId = idField.getText().trim();
                if (memberId.isEmpty()) {
                    DialogFactory.showErrorDialog(this, "Lỗi", "Vui lòng nhập ID!");
                    continue;   // lặp lại nhập ID
                }

                // Gọi service tìm Member
                m = serviceManager.getMemberService().getMemberById(memberId);
                if (m == null || m.getMemberId().isEmpty()) {
                    DialogFactory.showErrorDialog(
                            this,
                            "Không tìm thấy",
                            "Không tìm thấy thành viên với ID: " + memberId
                    );
                    continue;   // lặp lại nhập ID
                }
                // Nếu đến đây → đã tìm thấy, thoát vòng loop
                break;
            }

            // === BƯỚC 2: Hiện dialog sửa thông tin ===
            JDialog dialog = new JDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    "Cập nhật thành viên: " + m.getMemberId(),
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
            JTextField nameField = new JTextField(m.getMemberName(), 20);
            form.add(nameField);
            // 2. SĐT (editable)
            form.add(new JLabel("Số điện thoại:"));
            JTextField phoneField = new JTextField(m.getPhoneNumber(), 20);
            form.add(phoneField);
            // 3. Email (editable)
            form.add(new JLabel("Email:"));
            JTextField emailField = new JTextField(m.getEmail(), 20);
            form.add(emailField);
            // 4. Ngày đăng ký (readonly)
            form.add(new JLabel("Ngày đăng ký:"));
            JTextField dateField = new JTextField(
                    new SimpleDateFormat("dd/MM/yyyy").format(m.getRegistrationDate()),
                    20
            );
            dateField.setEditable(false);
            form.add(dateField);
            // 5. Điểm tích lũy (readonly)
            form.add(new JLabel("Số điểm tích lũy:"));
            JTextField pointsField = new JTextField(
                    String.valueOf(m.getLoyaltyPoints()),
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
            Member finalM = m;
            okBtn.addActionListener(ev -> {
                // Lấy giá trị cũ
                String oldName = finalM.getMemberName();
                String oldPhone = finalM.getPhoneNumber();
                String oldEmail = finalM.getEmail();

                // Lấy giá trị mới
                String newName = nameField.getText().trim();
                String newPhone = phoneField.getText().trim();
                String newEmail = emailField.getText().trim();

                // Đặt lại trên model
                finalM.setMemberName(newName);
                finalM.setPhoneNumber(newPhone);
                finalM.setEmail(newEmail);

                try {
                    serviceManager.getMemberService().updateMember(finalM);
                    JPanel infoPanel = new JPanel(new GridLayout(4, 4, 8, 8));
                    infoPanel.add(new JLabel("Cập nhật thành viên: "));
                    infoPanel.add(new JLabel(finalM.getMemberId()));
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
                    refreshTable(serviceManager.getMemberService().getAllMembers());
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
        });

        // Để các nút canh trái, set alignment
        memberSearchWithIdButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        memberSearchWithPhoneNumberButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        memberSearchAllButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        memberUpdateButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thêm nút (nó sẽ xếp dọc theo thứ tự)
        memberToolPanel.add(memberSearchWithIdButton);
        memberToolPanel.add(Box.createVerticalStrut(5));               // cách 5px
        memberToolPanel.add(memberSearchWithPhoneNumberButton);
        memberToolPanel.add(Box.createVerticalStrut(5));
        memberToolPanel.add(memberSearchAllButton);
        memberToolPanel.add(Box.createVerticalStrut(5));
        memberToolPanel.add(memberUpdateButton);

        memberPanel.add(memberToolPanel, BorderLayout.WEST);

        // Import sample member data
        String[] memberColumns = {"Mã thành viên", "Tên", "Số điện thoại", "Email", "Ngày đăng ký", "Số điểm tích lũy"};
        List<Member> members = serviceManager.getMemberService().getAllMembers();
        String[][] memberData = new String[members.size()][memberColumns.length];

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < members.size(); i++) {
            Member m = members.get(i);
            memberData[i][0] = m.getMemberId();
            memberData[i][1] = m.getMemberName();
            memberData[i][2] = m.getPhoneNumber();
            memberData[i][3] = m.getEmail();
            memberData[i][4] = df.format(m.getRegistrationDate());
            memberData[i][5] = String.valueOf(m.getLoyaltyPoints());
        }

        // Create a table with data
        this.memberTable = new ScrollableTable(memberColumns, memberData);
        memberPanel.add(memberTable, BorderLayout.CENTER);

        return memberPanel;
    }

    private JPanel createEmployeeContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create member panel
        JPanel employeePanel = createEmployeePanel();
        panel.add(employeePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createEmployeePanel() {
        JPanel employeePanel = new JPanel(new BorderLayout());
        employeePanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Tool panel for employee actions
        JPanel employeeToolPanel = new JPanel();
        employeeToolPanel.setLayout(new BoxLayout(employeeToolPanel, BoxLayout.Y_AXIS));
        employeePanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        return employeePanel;
    }

    private void refreshTable(List<Member> members) {
        String[] cols = {"Mã thành viên", "Tên", "Số điện thoại", "Email", "Ngày đăng ký", "Số điểm tích lũy"};
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String[][] data = new String[members.size()][cols.length];
        for (int i = 0; i < members.size(); i++) {
            Member m = members.get(i);
            data[i] = new String[]{
                    m.getMemberId(),
                    m.getMemberName(),
                    m.getPhoneNumber(),
                    m.getEmail(),
                    df.format(m.getRegistrationDate()),
                    String.valueOf(m.getLoyaltyPoints())
            };
        }
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        memberTable.getTable().setModel(model);
    }

    private void refreshTable(Member member) {
        String[] cols = {"Mã thành viên", "Tên", "Số điện thoại", "Email", "Ngày đăng ký", "Số điểm tích lũy"};
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String[][] data = new String[1][cols.length];
        data[0] = new String[]{
                member.getMemberId(),
                member.getMemberName(),
                member.getPhoneNumber(),
                member.getEmail(),
                df.format(member.getRegistrationDate()),
                String.valueOf(member.getLoyaltyPoints())
        };
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        memberTable.getTable().setModel(model);
    }
}
