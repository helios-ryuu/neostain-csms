package com.neostain.csms.view.screen;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.ViewManager;
import com.neostain.csms.model.*;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.util.ScreenType;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.view.MainFrame;
import com.neostain.csms.view.component.StandardButton;
import com.neostain.csms.view.component.StandardTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Màn hình xác thực của ứng dụng
 * Cung cấp giao diện cho người dùng đăng nhập và đăng ký
 */
public class LoginScreen extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(LoginScreen.class.getName());
    private static final ServiceManager serviceManager = ServiceManager.getInstance();

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> storeBox;
    private final String[] storeItems;
    private final JLabel statusLabel;

    /**
     * Khởi tạo màn hình xác thực với các thành phần giao diện
     */
    public LoginScreen() {
        this.usernameField = new JTextField(30);
        this.passwordField = new JPasswordField(30);

        // Payment methods dropdown (load from DB)
        List<Store> stores = serviceManager.getManagementService(false).getAllStores();
        this.storeItems = new String[stores.size()];
        for (int i = 0; i < stores.size(); i++) {
            storeItems[i] = stores.get(i).getId();
        }
        this.storeBox = new JComboBox<>(storeItems);
        this.statusLabel = new JLabel("Vui lòng đăng nhập");

        initializeComponents();
    }

    /**
     * Initialize the screen components
     */
    private void initializeComponents() {
        StandardTabbedPane tabbedPane = new StandardTabbedPane();
        tabbedPane.addTab("Đăng nhập", this.createLoginPanel());

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Thiết lập giao diện cho tab đăng nhập
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(
                Constants.View.CONTENT_INSETS.top,
                Constants.View.CONTENT_INSETS.left,
                Constants.View.CONTENT_INSETS.bottom,
                Constants.View.CONTENT_INSETS.right
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 18, 8, 18);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Tên đăng nhập"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbc);

        // Cửa hàng
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Mã cửa hàng"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(storeBox, gbc);


        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Mật khẩu"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);

        gbc.insets = new Insets(15, 15, 6, 15);

        // Login button
        JButton loginButton = new StandardButton(this, "Đăng nhập");
        loginButton.addActionListener(e -> this.login());

        gbc.gridx = 0;
        gbc.gridy = 3;
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
     * Thực hiện đăng nhập với thông tin người dùng đã nhập
     */
    private void login() {
        String username = this.usernameField.getText().trim();
        String password = new String(this.passwordField.getPassword());
        String storeId = this.storeItems[this.storeBox.getSelectedIndex()];

        // Validate input
        if (StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(storeId)) {
            this.statusLabel.setText("Vui lòng nhập tên đăng nhập, mã cửa hàng và mật khẩu");
            this.statusLabel.setForeground(Color.RED);
            DialogFactory.showErrorDialog(
                    this,
                    "Lỗi đăng nhập",
                    "Vui lòng nhập tên đăng nhập, mã cửa hàng và mật khẩu"
            );
            return;
        }

        try {
            // Sử dụng phương thức login của ServiceManager để xử lý đăng nhập và tạo token
            String token = serviceManager.login(username, password, storeId);

            if (token == null) {
                String errorMessage = "Sai tên đăng nhập hoặc mật khẩu hoặc không tìm thấy cửa hàng";
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
                this.storeBox.setSelectedIndex(0);
                this.statusLabel.setText("Đăng nhập và chấm công vào làm thành công!");
                this.statusLabel.setForeground(Color.GREEN);

                try {
                    // Khởi tạo đối tượng thông tin đăng nhập hiện tại
                    Account account = serviceManager.getAuthService().getAccountByUsername(currentUsername);
                    Employee employee = serviceManager.getManagementService().getEmployeeById(account.getEmployeeId());
                    Role role = serviceManager.getAuthService().getRoleById(account.getRoleId());
                    Store store = serviceManager.getManagementService().getStoreById(storeId);
                    ShiftReport shiftReport = serviceManager.getOperationService().getShiftReportById(serviceManager.getCurrentShiftId());

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String shiftStartTime = sdf.format(shiftReport.getStartTime());

                    // Hiển thị thông báo chào mừng
                    String message = "Đăng nhập thành công!\nChào mừng, " +
                            employee.getId() + " - " + employee.getName() +
                            "\nVai trò: " + role.getId() + " - " + role.getName() +
                            "\n\nĐã khởi động ca làm: " + shiftReport.getId() + " lúc " + shiftStartTime +
                            "\ntại cửa hàng: " + store.getId() + " - " + store.getName();
                    DialogFactory.showInfoDialog(
                            this,
                            "Thành công",
                            message
                    );

                    // Chuyển đến màn hình chức năng tương ứng
                    navigateToFunctionalScreen(username, role.getName());
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
