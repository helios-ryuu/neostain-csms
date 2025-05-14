package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Store;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.DashboardTile;
import com.neostain.csms.view.component.StandardButton;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard panels for the store manager screen
 */
public class DashboardPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    private final String username;

    // UI components for store info
    private JLabel storeName;
    private JLabel storeAddress;
    private JLabel managerId;
    private JLabel managerName;

    public DashboardPanel() {
        this.username = serviceManager.getCurrentUsername();
        initializeComponents();
    }

    private void initializeComponents() {
        // Use BorderLayout to stack panels vertically
        this.setLayout(new BorderLayout(0, 10));
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Top: Sales statistics panel (expands)
        JPanel salesPanel = createSalesStatisticsPanel();
        // Let width expand, height initial hint
        salesPanel.setPreferredSize(new Dimension(0, 400));
        this.add(salesPanel, BorderLayout.CENTER);

        // Bottom: Store info panel (fixed height)
        JPanel storeInfoPanel = createStoreInfoContentPanel();
        // Fixed height to fit buttons and info
        storeInfoPanel.setPreferredSize(new Dimension(0, 200));
        this.add(storeInfoPanel, BorderLayout.SOUTH);
    }

    /**
     * Builds the sales statistics panel.
     */
    private JPanel createSalesStatisticsPanel() {
        JPanel panel = new BorderedPanel("Thống kê bán hàng");
        panel.setLayout(new BorderLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Tiles grid
        JPanel tiles = new JPanel(new GridLayout(3, 4, 10, 10));
        tiles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tiles.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // TODO: load real data
        tiles.add(new DashboardTile("Tổng hoá đơn của cửa hàng", "128", new Color(41, 128, 185)));
        tiles.add(new DashboardTile("Hoá đơn hôm nay", "24", new Color(39, 174, 96)));
        tiles.add(new DashboardTile("Doanh thu hôm nay", "5,280,000 VNĐ", new Color(211, 84, 0)));
        tiles.add(new DashboardTile("Doanh thu 30 ngày gần nhất", "42,750,000 VNĐ", new Color(142, 68, 173)));

        tiles.add(new DashboardTile("Sản phẩm bán chạy", "Cà phê", new Color(52, 152, 219)));
        tiles.add(new DashboardTile("Khách hàng mới", "15", new Color(46, 204, 113)));
        tiles.add(new DashboardTile("Tổng nhân viên", "12", new Color(230, 126, 34)));
        tiles.add(new DashboardTile("Tỉ lệ hủy hóa đơn", "8%", new Color(155, 89, 182)));

        tiles.add(new DashboardTile("Sản phẩm bán chạy", "Cà phê", new Color(52, 152, 219)));
        tiles.add(new DashboardTile("Khách hàng mới", "15", new Color(46, 204, 113)));
        tiles.add(new DashboardTile("Tổng nhân viên", "12", new Color(230, 126, 34)));
        tiles.add(new DashboardTile("Tỉ lệ hủy hóa đơn", "8%", new Color(155, 89, 182)));

        panel.add(tiles, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStoreInfoContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create store information panels
        JPanel storeInfoPanel = createStoreInfoPanel();
        panel.add(storeInfoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStoreInfoPanel() {
        // Fetch data
        Account account = serviceManager.getAuthService().getAccountByUsername(username);
        Store store = serviceManager.getManagementService().getStoreByManagerId(account.getEmployeeId());
        Employee employee = serviceManager.getManagementService().getEmployeeById(store.getManagerId());

        // Container with border and title
        JPanel container = new BorderedPanel("Thông tin cửa hàng");
        container.setLayout(new BorderLayout());
        container.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Left: Action buttons
        JPanel tools = new JPanel();
        tools.setLayout(new BoxLayout(tools, BoxLayout.Y_AXIS));
        tools.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        JButton editNameBtn = new StandardButton(this, "Cập nhật tên cửa hàng");
        editNameBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        editNameBtn.addActionListener(e -> {
            // 1. Lấy tên cũ từ model
            String currentName = store.getName();

            // 2. Tạo panels chứa label + field
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
                    store.setName(newName);
                    serviceManager.getManagementService()
                            .updateStore(store);

                    // 6. Cập nhật UI ngay lập tức
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

        JButton editAddrBtn = new StandardButton(this, "Cập nhật địa chỉ cửa hàng");
        editAddrBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        editAddrBtn.addActionListener(e -> {
            // 1. Lấy địa chỉ cũ từ model
            String currentAddress = store.getStoreAddress();

            // 2. Tạo panels chứa label + field
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
                    store.setStoreAddress(newAddress);
                    serviceManager.getManagementService()
                            .updateStore(store);

                    // 6. Cập nhật UI ngay lập tức
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

        tools.add(editNameBtn);
        tools.add(Box.createVerticalStrut(5));
        tools.add(editAddrBtn);
        tools.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        container.add(tools, BorderLayout.WEST);

        // Center: Info fields
        JPanel infoGrid = new JPanel(new GridBagLayout());
        infoGrid.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        infoGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        storeName = new JLabel(store.getName());
        storeAddress = new JLabel(store.getStoreAddress());
        managerId = new JLabel(store.getManagerId());
        managerName = new JLabel(employee.getName());

        addRow(infoGrid, "Tên cửa hàng:", storeName, 0);
        addRow(infoGrid, "Địa chỉ:", storeAddress, 1);
        addRow(infoGrid, "Mã nhân viên quản lý:", managerId, 2);
        addRow(infoGrid, "Tên nhân viên quản lý:", managerName, 3);

        container.add(infoGrid, BorderLayout.CENTER);
        return container;
    }

    // GridBag helper
    private void addRow(JPanel panel, String text, JComponent comp, int row) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.BOLD, Constants.Font.DEFAULT_SIZE));
        GridBagConstraints gbc1 = makeGbc(0, row, 0.1);
        GridBagConstraints gbc2 = makeGbc(1, row, 0.9);
        panel.add(label, gbc1);
        panel.add(comp, gbc2);
    }

    private GridBagConstraints makeGbc(int x, int y, double w) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = w;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);
        return gbc;
    }
}
