package com.neostain.csms.view.screen;

import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Role;
import com.neostain.csms.service.ServiceManager;
import com.neostain.csms.util.ColorUtils;
import com.neostain.csms.view.MainFrame;
import com.neostain.csms.view.component.TitledBorderPanel;
import com.neostain.csms.view.manager.ScreenManager;

import javax.swing.*;
import java.awt.*;

/**
 * Màn hình xác thực của ứng dụng
 * Cung cấp giao diện cho người dùng đăng nhập và đăng ký
 */
public class LoginScreen extends JPanel {
    // Service xử lý
    private final ServiceManager serviceManager;

    // Các thành phần giao diện
    private final JTextField usernameField = new JTextField(30);
    private final JPasswordField passwordField = new JPasswordField(30);
    private final JLabel statusLabel = new JLabel("Vui lòng đăng nhập");

    /**
     * Khởi tạo màn hình xác thực với các thành phần giao diện
     */
    public LoginScreen() {
        // Thiết lập màn hình
        this.setLayout(new BorderLayout());

        // Tạo Service Manager
        this.serviceManager = ServiceManager.getInstance();

        // Khởi tạo các thành phần giao diện
        JTabbedPane mainAuthorizePane = this.createMainAuthorizePane();
        JPanel loginTabPanel = this.createLoginTabPanel();
        JPanel aboutTabPanel = this.createAboutTabPanel();

        // Thêm các tab vào mainAuthorizePane
        mainAuthorizePane.addTab("Đăng nhập", loginTabPanel);
        mainAuthorizePane.addTab("Thông tin dự án", aboutTabPanel);

        // Thêm mainAuthorizePane vào LoginScreen
        this.add(mainAuthorizePane, BorderLayout.CENTER);
    }

    private JTabbedPane createMainAuthorizePane() {
        JTabbedPane mainAuthorizePane = new JTabbedPane();
        mainAuthorizePane.setTabPlacement(JTabbedPane.TOP);
        mainAuthorizePane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainAuthorizePane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        mainAuthorizePane.setFocusable(false);

        return mainAuthorizePane;
    }

    /**
     * Thiết lập giao diện cho tab đăng nhập
     */
    private JPanel createLoginTabPanel() {
        JPanel loginTabPanel = new JPanel();
        loginTabPanel.setBackground(ColorUtils.componentBackgroundWhite);
        loginTabPanel.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));

        loginTabPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Kích thước cố định cho cột label
        int labelFixedWidth = 100;

        // Hàng 0: Label "Tên đăng nhập:" và TextField
        JLabel userLabel = new JLabel("Tên đăng nhập:");
        userLabel.setPreferredSize(new Dimension(labelFixedWidth, userLabel.getPreferredSize().height));
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(loginTabPanel, userLabel, gbc, 0, 0, 1, 1);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        addComponent(loginTabPanel, usernameField, gbc, 1, 0, 2, 1);

        // Hàng 1: Label "Mật khẩu:" và PasswordField
        JLabel passLabel = new JLabel("Mật khẩu:");
        passLabel.setPreferredSize(new Dimension(labelFixedWidth, passLabel.getPreferredSize().height));
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
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

        return loginTabPanel;
    }

    /**
     * Thiết lập giao diện cho tab thông tin dự án
     */
    private JPanel createAboutTabPanel() {
        JPanel aboutTabPanel = new JPanel();
        aboutTabPanel.setBackground(ColorUtils.componentBackgroundWhite);
        aboutTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        aboutTabPanel.setLayout(new GridBagLayout());

        TitledBorderPanel authorPanel = setupAuthorPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;  // Cho phép giãn ngang
        gbc.weighty = 1.0;  // Cho phép giãn dọc
        gbc.fill = GridBagConstraints.BOTH; // Giãn đầy cả hai chiều
        aboutTabPanel.add(authorPanel, gbc);

        return aboutTabPanel;
    }

    private static TitledBorderPanel setupAuthorPanel() {
        TitledBorderPanel titledBorderPanel = new TitledBorderPanel("Tác giả");
        JTextArea authorContent = new JTextArea(
                """
                        Copyright © 2025 NeoStain
                        Phần mềm tạo ra bởi thành viên của nhóm phát triển:
                        Ngô Tiến Sỹ
                        Nguyễn Văn Nam
                        Võ Đức Tài
                        Thiều Đinh Nam Tài
                        
                        Mọi nội dung, dữ liệu và mã nguồn chỉ được sử dụng cho mục đích học tập và nghiên cứu."""
        );
        authorContent.setEditable(false);
        authorContent.setOpaque(false);
        authorContent.setFont(new JLabel().getFont());
        authorContent.setFocusable(false);
        authorContent.setBackground(ColorUtils.componentBackgroundWhite);
        titledBorderPanel.add(authorContent, BorderLayout.CENTER);
        return titledBorderPanel;
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

                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);

                switch (role.getRoleName()) {
                    case "Quản lý cửa hàng":
                        ScreenManager.getInstance(mainFrame).switchScreen(ScreenType.STORE_MANAGER, username);
                        break;
                    case "Nhân viên bán hàng":
                        ScreenManager.getInstance(mainFrame).switchScreen(ScreenType.POS, username);
                        break;
                    default:
                        break;
                }
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
