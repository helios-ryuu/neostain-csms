package com.neostain.csms.view.screen;

import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Role;
import com.neostain.csms.service.ServiceManager;
import com.neostain.csms.view.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Màn hình đăng nhập của ứng dụng
 * Cung cấp giao diện cho người dùng nhập thông tin đăng nhập
 */
public class LoginScreen extends JPanel {
    // Service xử lý xác thực đăng nhập
    private final ServiceManager serviceManager;

    // Các thành phần giao diện
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;


    /**
     * Khởi tạo màn hình đăng nhập với các thành phần giao diện
     */
    public LoginScreen() {
        // Tạo Service Manager
        serviceManager = ServiceManager.getInstance();

        // Tạo các thành phần
        this.usernameField = new JTextField(20);
        this.passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Đăng nhập");
        this.statusLabel = new JLabel("Vui lòng đăng nhập");

        // Thiết lập border cho panel
        this.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Sử dụng GridBagLayout để bố trí các component
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Kích thước cố định cho cột label
        int labelFixedWidth = 100; // Điều chỉnh theo nhu cầu

        // Hàng 0: Label "Tên đăng nhập:" và TextField
        JLabel userLabel = new JLabel("Tên đăng nhập:");
        userLabel.setPreferredSize(new Dimension(labelFixedWidth, userLabel.getPreferredSize().height));
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        addComponent(userLabel, gbc, 0, 0, 1, 1);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        addComponent(usernameField, gbc, 1, 0, 2, 1);

        // Hàng 1: Label "Mật khẩu:" và PasswordField
        JLabel passLabel = new JLabel("Mật khẩu:");
        passLabel.setPreferredSize(new Dimension(labelFixedWidth, passLabel.getPreferredSize().height));
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        addComponent(passLabel, gbc, 0, 1, 1, 1);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        addComponent(passwordField, gbc, 1, 1, 2, 1);

        // Hàng 2: Nút "Đăng nhập" chiếm 1 cột và được căn giữa
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(loginButton, gbc, 0, 2, 1, 1);

        // Hàng 3: Status label chiếm 1 cột và được căn giữa
        addComponent(statusLabel, gbc, 1, 2, 1, 1);

        // Thêm xử lý sự kiện cho nút đăng nhập
        loginButton.addActionListener(e -> this.login());

        // Nhấn đăng nhập kho enter
        SwingUtilities.invokeLater(() -> {
            JRootPane rootPane = SwingUtilities.getRootPane(loginButton);
            if (rootPane != null) {
                rootPane.setDefaultButton(loginButton);
            }
        });

    }

    /**
     * Phương thức tiện ích để thêm component vào layout với GridBagConstraints.
     *
     * @param comp       Component cần thêm.
     * @param gbc        GridBagConstraints được cấu hình.
     * @param gridx      Vị trí cột.
     * @param gridy      Vị trí hàng.
     * @param gridwidth  Số cột mà component chiếm.
     * @param gridheight Số hàng mà component chiếm.
     */
    private void addComponent(Component comp, GridBagConstraints gbc,
                              int gridx, int gridy, int gridwidth, int gridheight) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        this.add(comp, gbc);
    }


    /**
     * Thực hiện đăng nhập với thông tin người dùng đã nhập
     */
    private void login() {
        String username = this.usernameField.getText();
        String password = new String(this.passwordField.getPassword());

        try {
            // Test
            if (username.equals("1") && password.equals("1")) {
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                mainFrame.showPOSScreen("Tester");
            } else if (this.serviceManager.getAuthService().authenticate(username, password)) {
                // Đăng nhập thành công
                this.statusLabel.setText("Đăng nhập thành công!");
                this.statusLabel.setForeground(Color.GREEN);
                this.passwordField.setText("");
                this.usernameField.setText("");

                // Khởi tạo đói tượng tin đăng nhập hiện tại
                Account account = this.serviceManager.getAccountService().getAccount(username);
                Employee employee = this.serviceManager.getEmployeeService().getEmployee(username);
                Role role = this.serviceManager.getRoleService().getRole(account.getRoleID());

                JOptionPane.showMessageDialog(this,
                        "Đăng nhập thành công!\nChào mừng, " + employee.getEmployeeId() + " - " + employee.getEmployeeName()
                                + "\nPhân quyền: " + role.getRoleId() + " - " + role.getRoleName(),
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                // Chuyển sang màn hình POSScreen
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                mainFrame.showPOSScreen(username);
            } else {
                // Đăng nhập thất bại
                JOptionPane.showMessageDialog(this,
                        "Đăng nhập không thành công!",
                        "Thành công",
                        JOptionPane.ERROR_MESSAGE);

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

