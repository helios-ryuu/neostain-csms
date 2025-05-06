package com.neostain.csms.view.screen;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.ViewManager;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Role;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.util.ScreenType;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.view.MainFrame;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.StandardButton;
import com.neostain.csms.view.component.StandardTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Màn hình xác thực của ứng dụng
 * Cung cấp giao diện cho người dùng đăng nhập và đăng ký
 */
public class LoginScreen extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(LoginScreen.class.getName());
    private static final ServiceManager serviceManager = ServiceManager.getInstance();

    // Các thành phần giao diện
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;

    /**
     * Khởi tạo màn hình xác thực với các thành phần giao diện
     */
    public LoginScreen() {
        super(); // Calls BaseScreen constructor
        // Initialize fields before parent class constructor is called
        this.usernameField = new JTextField(30);
        this.passwordField = new JPasswordField(30);
        this.statusLabel = new JLabel("Vui lòng đăng nhập");


        // Now call initializeContent which will call our initializeComponents() method
        initializeComponents();
    }

    private static BorderedPanel setupAuthorPanel() {
        BorderedPanel borderedPanel = new BorderedPanel("Tác giả");
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
        authorContent.setFont(Constants.View.DEFAULT_FONT);
        authorContent.setFocusable(false);
        authorContent.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        borderedPanel.add(authorContent, BorderLayout.CENTER);
        return borderedPanel;
    }

    /**
     * Initialize the screen components
     */
    private void initializeComponents() {
        // Khởi tạo các thành phần giao diện
        JTabbedPane mainAuthorizePane = this.createStandardTabbedPane();
        JPanel loginTabPanel = this.createLoginTabPanel();
        JPanel aboutTabPanel = this.createAboutTabPanel();

        // Thêm các tab vào mainAuthorizePane
        mainAuthorizePane.addTab("Đăng nhập", loginTabPanel);
        mainAuthorizePane.addTab("Thông tin dự án", aboutTabPanel);

        // Thêm mainAuthorizePane vào LoginScreen
        this.add(mainAuthorizePane, BorderLayout.CENTER);
    }

    private JTabbedPane createStandardTabbedPane() {
        return new StandardTabbedPane();
    }

    // Method removed as it's now in BaseScreen

    /**
     * Thiết lập giao diện cho tab đăng nhập
     */
    private JPanel createLoginTabPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(
                Constants.View.CONTENT_INSETS.top,
                Constants.View.CONTENT_INSETS.left,
                Constants.View.CONTENT_INSETS.bottom,
                Constants.View.CONTENT_INSETS.right
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 15, 6, 15);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Tên đăng nhập"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Mật khẩu"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new StandardButton(this, "Đăng nhập");
        loginButton.addActionListener(e -> this.login());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        panel.add(loginButton, gbc);

        // Status label
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);

        // Enter key = login
        SwingUtilities.invokeLater(() -> {
            JRootPane root = SwingUtilities.getRootPane(loginButton);
            if (root != null) root.setDefaultButton(loginButton);
        });

        return panel;
    }

    /**
     * Thiết lập giao diện cho tab thông tin dự án
     */
    private JPanel createAboutTabPanel() {
        JPanel aboutTabPanel = new JPanel();
        aboutTabPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        aboutTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        aboutTabPanel.setLayout(new GridBagLayout());

        BorderedPanel authorPanel = setupAuthorPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;  // Cho phép giãn ngang
        gbc.weighty = 1.0;  // Cho phép giãn dọc
        gbc.fill = GridBagConstraints.BOTH; // Giãn đầy cả hai chiều
        aboutTabPanel.add(authorPanel, gbc);

        return aboutTabPanel;
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
     * @param anchor     Vị trí neo component trong ô lưới.
     * @param fill       Cách thức điền đầy không gian được cấp phát.
     * @param weightx    Trọng số phân bổ không gian dư theo chiều ngang.
     * @param weighty    Trọng số phân bổ không gian dư theo chiều dọc.
     */
    public void addComponent(JPanel panel, Component comp, GridBagConstraints gbc,
                             int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, double weightx, double weighty) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        panel.add(comp, gbc);
    }

    /**
     * Thực hiện đăng nhập với thông tin người dùng đã nhập
     */
    private void login() {
        String username = this.usernameField.getText().trim();
        String password = new String(this.passwordField.getPassword());

        // Validate input
        if (StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password)) {
            this.statusLabel.setText("Vui lòng nhập tên đăng nhập và mật khẩu");
            this.statusLabel.setForeground(Color.RED);
            DialogFactory.showErrorDialog(
                    this,
                    "Lỗi đăng nhập",
                    "Vui lòng nhập tên đăng nhập và mật khẩu"
            );
            return;
        }

        try {
            // Sử dụng phương thức login của ServiceManager để xử lý đăng nhập và tạo token
            String token = serviceManager.login(username, password);

            if (token == null) {
                String errorMessage = "Sai tên đăng nhập hoặc mật khẩu";
                this.statusLabel.setText(errorMessage);
                this.statusLabel.setForeground(Color.RED);
                DialogFactory.showErrorDialog(
                        this,
                        "Lỗi đăng nhập",
                        errorMessage
                );
            } else {
                // Login successful - process the login
                // Lấy thông tin người dùng từ token
                String currentUsername = serviceManager.getCurrentUsername();

                // Reset input fields
                this.passwordField.setText("");
                this.usernameField.setText("");
                this.statusLabel.setText("Đăng nhập thành công!");
                this.statusLabel.setForeground(Color.GREEN);

                try {
                    // Khởi tạo đối tượng thông tin đăng nhập hiện tại
                    Account account = serviceManager.getAuthService().getAccountByUsername(currentUsername);
                    Employee employee = serviceManager.getEmployeeService().getEmployeeById(account.getEmployeeId());
                    Role role = serviceManager.getAuthService().getRoleById(account.getRoleId());

                    // Hiển thị thông báo chào mừng
                    String message = "Đăng nhập thành công!\nChào mừng, " +
                            employee.getEmployeeId() + " - " + employee.getEmployeeName() +
                            "\nPhân quyền: " + role.getRoleId() + " - " + role.getRoleName();
                    DialogFactory.showInfoDialog(
                            this,
                            "Thành công",
                            message
                    );

                    // Chuyển đến màn hình chức năng tương ứng
                    navigateToFunctionalScreen(username, role.getRoleName());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Lỗi sau khi đăng nhập: " + e.getMessage(), e);
                    DialogFactory.showErrorDialog(
                            this,
                            "Lỗi hệ thống",
                            "Đăng nhập thành công nhưng không thể chuyển màn hình"
                    );
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Lỗi đăng nhập: " + ex.getMessage(), ex);

            // Sử dụng thông báo lỗi trực tiếp
            String errorMessage = "Có lỗi xảy ra khi đăng nhập, vui lòng thử lại";
            this.statusLabel.setText(errorMessage);
            this.statusLabel.setForeground(Color.RED);
            DialogFactory.showErrorDialog(
                    this,
                    "Lỗi đăng nhập",
                    errorMessage
            );
        }
    }

    /**
     * Chuyển đến màn hình chức năng dựa trên vai trò người dùng
     */
    private void navigateToFunctionalScreen(String username, String roleName) {
        MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);

        switch (roleName) {
            case "Nhân viên quản lý cửa hàng":
                ViewManager.getInstance(mainFrame).switchScreen(ScreenType.STORE_MANAGER, username);
                break;
            case "Nhân viên bán hàng":
                ViewManager.getInstance(mainFrame).switchScreen(ScreenType.POS, username);
                break;
            default:
                LOGGER.warning("Không tìm thấy màn hình phù hợp cho vai trò: " + roleName);
                break;
        }
    }
}
