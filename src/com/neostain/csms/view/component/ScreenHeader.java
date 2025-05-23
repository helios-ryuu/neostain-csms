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
    public ScreenHeader(String employeeName, String roleName) {

        this.setLayout(new BorderLayout());
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_HEADER);
        this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        this.setPreferredSize(new Dimension(0, 60));

        // User info on the left
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.X_AXIS));
        userInfoPanel.setOpaque(false);
        userInfoPanel.add(Box.createHorizontalStrut(10)); // Add some padding

        JLabel userLabel = new JLabel(employeeName + " (" + roleName + ")");
        userLabel.setFont(Constants.View.DEFAULT_FONT);
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        userInfoPanel.add(userLabel);

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
    }
}
