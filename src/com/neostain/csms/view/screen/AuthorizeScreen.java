package com.neostain.csms.view.screen;

import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Role;
import com.neostain.csms.service.ServiceManager;
import com.neostain.csms.view.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Màn hình xác thực của ứng dụng
 * Cung cấp giao diện cho người dùng đăng nhập và đăng ký
 */
public class AuthorizeScreen extends JPanel {
    // Service xử lý xác thực đăng nhập
    private final ServiceManager serviceManager;

    // Các thành phần giao diện
    public JTabbedPane mainAuthorizePane;
    private final JPanel loginTabPanel;
    private final JPanel AboutTabPanel;

    // Các thành phần giao diện của tab đăng nhập
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;

    /**
     * Khởi tạo màn hình xác thực với các thành phần giao diện
     */
    public AuthorizeScreen() {
        // Tạo Service Manager
        serviceManager = ServiceManager.getInstance();

        // Khởi tạo các thành phần giao diện
        this.mainAuthorizePane = new JTabbedPane();
        this.loginTabPanel = new JPanel();
        this.AboutTabPanel = new JPanel();
        this.usernameField = new JTextField(30);
        this.passwordField = new JPasswordField(30);
        this.statusLabel = new JLabel("Vui lòng đăng nhập");

        // Thiết lập tab panel
        mainAuthorizePane.setTabPlacement(JTabbedPane.TOP);
        mainAuthorizePane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainAuthorizePane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        mainAuthorizePane.setFocusable(false);

        // Thiết lập tab đăng nhập
        setupLoginTab();

        // Thiết lập tab đăng ký
        setupAboutTab();

        // Thêm các tab vào mainAuthorizePane
        mainAuthorizePane.addTab("Đăng nhập", loginTabPanel);
        mainAuthorizePane.addTab("Thông tin dự án", AboutTabPanel);

        // Thêm mainAuthorizePane vào AuthorizeScreen
        this.setLayout(new BorderLayout());
        this.add(mainAuthorizePane, BorderLayout.CENTER);
    }

    /**
     * Thiết lập giao diện cho tab đăng nhập
     */
    private void setupLoginTab() {
        // Thiết lập border cho panel
        loginTabPanel.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
        loginTabPanel.setBackground(new Color(249, 249, 249));

        // Sử dụng GridBagLayout để bố trí các component
        loginTabPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Kích thước cố định cho cột label
        int labelFixedWidth = 100;

        // Hàng 0: Label "Tên đăng nhập:" và TextField
        JLabel userLabel = new JLabel("Tên đăng nhập:");
        userLabel.setPreferredSize(new Dimension(labelFixedWidth, userLabel.getPreferredSize().height));
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        addComponent(loginTabPanel, userLabel, gbc, 0, 0, 1, 1);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        addComponent(loginTabPanel, usernameField, gbc, 1, 0, 2, 1);

        // Hàng 1: Label "Mật khẩu:" và PasswordField
        JLabel passLabel = new JLabel("Mật khẩu:");
        passLabel.setPreferredSize(new Dimension(labelFixedWidth, passLabel.getPreferredSize().height));
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        addComponent(loginTabPanel, passLabel, gbc, 0, 1, 1, 1);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        addComponent(loginTabPanel, passwordField, gbc, 1, 1, 2, 1);

        // Hàng 2: Nút "Đăng nhập" và status label
        JButton loginButton = new JButton("Đăng nhập");
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(loginTabPanel, loginButton, gbc, 0, 2, 1, 1);
        addComponent(loginTabPanel, statusLabel, gbc, 2, 2, 1, 1);

        // Thêm xử lý sự kiện cho nút đăng nhập
        loginButton.addActionListener(e -> this.login());

        // Nhấn đăng nhập khi enter
        SwingUtilities.invokeLater(() -> {
            JRootPane rootPane = SwingUtilities.getRootPane(loginButton);
            if (rootPane != null) {
                rootPane.setDefaultButton(loginButton);
            }
        });
    }

    /**
     * Thiết lập giao diện cho tab thông tin dự án
     */
    private void setupAboutTab() {
        // Thiết lập cho panel "AboutTabPanel"
        AboutTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        AboutTabPanel.setBackground(new Color(249, 249, 249));
        AboutTabPanel.setLayout(new GridBagLayout());

        // -- Tạo panel chứa nội dung, đặt border với màu xám nhạt --
        JPanel boxPanel = new JPanel(new BorderLayout(5, 5));
        boxPanel.setBackground(new Color(249, 249, 249));

        // Tạo một LineBorder màu xám nhạt
        Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220), 1);

        // Tạo TitledBorder từ lineBorder
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                lineBorder,
                "Tác giả",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION
        );

        // Tạo border padding để tăng khoảng cách bên trong
        Border marginBorder = BorderFactory.createEmptyBorder(4, 8, 4, 8);

        // Kết hợp TitledBorder và padding thành 1 border duy nhất
        boxPanel.setBorder(BorderFactory.createCompoundBorder(titledBorder, marginBorder));

        // Tạo thêm label hiển thị thông tin khác (ví dụ: developingLabel)
        JLabel developingLabel = new JLabel(
                "<html>" +
                        "Copyright © 2025 NeoStain<br><br>" +
                        "Phần mềm tạo ra bởi thành viên của nhóm phát triển:<br>" +
                        "Ngô Tiến Sỹ<br>" +
                        "Nguyễn Văn Nam<br>" +
                        "Võ Đức Tài<br>" +
                        "Thiều Đinh Nam Tài<br><br>" +
                        "Mọi nội dung, dữ liệu và mã nguồn chỉ được sử dụng cho mục đích học tập và nghiên cứu." +
                        "</html>"
        );
        // Thêm label thứ hai vào dưới label thứ nhất (có thể cho vào BorderLayout.SOUTH, hoặc tạo JPanel con)
        boxPanel.add(developingLabel, BorderLayout.CENTER);

        // -- Thêm boxPanel vào AboutTabPanel với GridBagConstraints để fill toàn bộ --
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;  // Cho phép giãn ngang
        gbc.weighty = 1.0;  // Cho phép giãn dọc
        gbc.fill = GridBagConstraints.BOTH; // Giãn đầy cả hai chiều
        AboutTabPanel.add(boxPanel, gbc);
    }


    /**
     * Phương thức tiện ích để thêm component vào layout với GridBagConstraints.
     *
     * @param panel      Panel chứa component.
     * @param comp       Component cần thêm.
     * @param gbc        GridBagConstraints được cấu hình.
     * @param gridx      Vị trí cột.
     * @param gridy      Vị trí hàng.
     * @param gridwidth  Số cột mà component chiếm.
     * @param gridheight Số hàng mà component chiếm.
     */
    private void addComponent(JPanel panel, Component comp, GridBagConstraints gbc,
                            int gridx, int gridy, int gridwidth, int gridheight) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        panel.add(comp, gbc);
    }

    /**
     * Thực hiện đăng nhập với thông tin người dùng đã nhập
     */
    private void login() {
        String username = this.usernameField.getText();
        String password = new String(this.passwordField.getPassword());

        try {
            if (this.serviceManager.getAuthService().authenticate(username, password)) {
                // Đăng nhập thành công
                this.statusLabel.setText("Đăng nhập thành công!");
                this.statusLabel.setForeground(Color.GREEN);
                this.passwordField.setText("");
                this.usernameField.setText("");

                // Khởi tạo đối tượng tin đăng nhập hiện tại
                Account account = this.serviceManager.getAccountService().getAccount(username);
                Employee employee = this.serviceManager.getEmployeeService().getEmployee(username);
                Role role = this.serviceManager.getRoleService().getRole(account.getRoleID());

                JOptionPane optionPane = new JOptionPane(
                        "Đăng nhập thành công!\nChào mừng, " + employee.getEmployeeId() + " - " + employee.getEmployeeName() +
                                "\nPhân quyền: " + role.getRoleId() + " - " + role.getRoleName(),
                        JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog("Thành công");
                dialog.setFocusableWindowState(false);
                dialog.setVisible(true);

                // Chuyển sang màn hình POSScreen
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                mainFrame.showPOSScreen(username);
            } else {
                // Đăng nhập thất bại
                JOptionPane optionPane = new JOptionPane(
                        "Đăng nhập không thành công!",
                        JOptionPane.ERROR_MESSAGE);
                JDialog dialog = optionPane.createDialog("Lỗi");
                dialog.setFocusableWindowState(false);
                dialog.setVisible(true);

                this.statusLabel.setText("Sai tên đăng nhập hoặc mật khẩu");
                this.statusLabel.setForeground(Color.RED);
            }
        } catch (Exception ex) {
            // Hiển thị lỗi
            this.statusLabel.setText("Lỗi: " + ex.getMessage());
            this.statusLabel.setForeground(Color.RED);
        }
    }
}
