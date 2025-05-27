package com.neostain.csms.view.component;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.ViewManager;
import com.neostain.csms.model.ShiftReport;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ScreenHeader extends JPanel {
    private final JLabel clockLabel = new JLabel();
    private final Timer timer;

    public ScreenHeader(String employeeId, String employeeName, String storeId, String storeName) {

        this.setLayout(new BorderLayout());
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_HEADER);
        this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        this.setPreferredSize(new Dimension(0, 60));

        // User info on the left
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.X_AXIS));
        userInfoPanel.setOpaque(false);
        userInfoPanel.add(Box.createHorizontalStrut(10)); // Add some padding

        JLabel empIdLabel = new JLabel("Nhân viên: " + employeeId + " - " + employeeName + "    ");
        empIdLabel.setFont(new Font(Constants.View.DEFAULT_FONT.getName(), Font.BOLD, Constants.View.DEFAULT_FONT.getSize()));
        empIdLabel.setForeground(Color.WHITE);
        empIdLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        userInfoPanel.add(empIdLabel);
        userInfoPanel.add(Box.createHorizontalStrut(15));

        JLabel storeLabel = new JLabel("Cửa hàng: " + storeId + " - " + storeName + "    ");
        storeLabel.setFont(new Font(Constants.View.DEFAULT_FONT.getName(), Font.BOLD, Constants.View.DEFAULT_FONT.getSize()));
        storeLabel.setForeground(Color.WHITE);
        storeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        userInfoPanel.add(storeLabel);
        userInfoPanel.add(Box.createHorizontalStrut(15));

        clockLabel.setFont(new Font(Constants.View.DEFAULT_FONT.getName(), Font.BOLD, Constants.View.DEFAULT_FONT.getSize()));
        clockLabel.setForeground(Color.YELLOW);
        clockLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        userInfoPanel.add(clockLabel);
        userInfoPanel.add(Box.createHorizontalStrut(15));

        // Actions on the right
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setOpaque(false);

        JButton logoutButton = new StandardButton(this, "Kết ca");
        logoutButton.setFont(Constants.View.DEFAULT_FONT);
        logoutButton.setForeground(Color.RED);
        logoutButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        logoutButton.addActionListener(e -> {
            boolean choice = DialogFactory.showConfirmYesNoDialog(
                    this,
                    "Xác nhận kết ca",
                    "Bạn có chắc chắn muốn kết ca và đăng xuất khỏi hệ thống?"
            );
            if (choice) {
                try {
                    ServiceManager serviceManager = ServiceManager.getInstance();
                    String currentShiftId = serviceManager.getCurrentShiftId();

                    // Get the current MainFrame instance instead of creating a new one
                    MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                    boolean isShiftReportClosed = serviceManager.getOperationService().closeShiftReport(currentShiftId);
                    if (isShiftReportClosed) {
                        ShiftReport shiftReport = serviceManager.getOperationService().getShiftReportById(currentShiftId);
                        File file = serviceManager.getPrintingService().printShiftReport(shiftReport);
                        if (file != null) {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.open(file);
                        }
                    }
                    ViewManager.getInstance(mainFrame).logout();

                } catch (Exception ex) {
                    DialogFactory.showErrorDialog(
                            this,
                            "Lỗi",
                            "Có lỗi xảy ra khi kết ca và đăng xuất: " + ex.getMessage()
                    );
                }
            }
        });

        actionsPanel.add(logoutButton);
        actionsPanel.add(Box.createHorizontalStrut(10)); // Add some padding

        // Container panels with vertical centering
        JPanel leftContainer = new JPanel(new GridBagLayout());
        leftContainer.setOpaque(false);
        leftContainer.add(userInfoPanel);

        JPanel rightContainer = new JPanel(new GridBagLayout());
        rightContainer.setOpaque(false);
        rightContainer.add(actionsPanel);

        // Add panels to header
        this.add(leftContainer, BorderLayout.WEST);
        this.add(rightContainer, BorderLayout.EAST);

        // Start clock
        timer = new Timer(1000, e -> updateClock());
        timer.setRepeats(true);
        timer.start();
        updateClock();
    }

    private void updateClock() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        clockLabel.setText(sdf.format(new java.util.Date()));
    }
}
