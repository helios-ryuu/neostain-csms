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
        // Tạo các dịch vụ
        this.serviceManager = ServiceManager.getInstance();

        // Tạo các thành phần
        this.usernameField = new JTextField(20);
        this.passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Đăng nhập");
        this.statusLabel = new JLabel("Vui lòng đăng nhập");

        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Thiết lập bố cục
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Padding giữa các component
        gbc.insets = new Insets(15, 15, 15, 15);

        // Co giãn theo chiều ngang
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Hàng 0: Label "Tên đăng nhập:" và TextField
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        this.add(new JLabel("Tên đăng nhập:"), gbc);

        gbc.gridx = 1;
        this.add(usernameField, gbc);

        // Hàng 1: Label "Mật khẩu:" và PasswordField
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1;
        this.add(passwordField, gbc);

        // Hàng 2: Nút Đăng nhập chiếm 2 cột
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;  // Span 2 cột
        this.add(loginButton, gbc);

        // Hàng 3: Status label chiếm 2 cột
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;  // Span 2 cột
        this.add(statusLabel, gbc);

        // Thêm xử lý sự kiện cho nút đăng nhập
        loginButton.addActionListener(e -> this.login());
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

                // Thông tin đăng nhập hiện tại
                Account account = this.serviceManager.getAccountService().getAccount(username);
                Employee employee = this.serviceManager.getEmployeeService().getEmployee(username);
                Role role = this.serviceManager.getRoleService().getRole(account.getRoleID());

                this.serviceManager.getAuthService().generateToken(account.getAccountID());

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

