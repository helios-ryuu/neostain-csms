package com.neostain.csms.view.screen.sm.panels;

import com.neostain.csms.util.Constants;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.ScrollableTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Panel for managing work shifts and employee schedules
 */
public class WorkShiftPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(WorkShiftPanel.class.getName());

    /**
     * Constructor for WorkShiftPanel
     */
    public WorkShiftPanel() {
        initializeComponents();
    }

    /**
     * Initialize all panels components
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create tabbed pane for different work shift management views
        JTabbedPane workShiftTabs = new JTabbedPane();

        // Create schedule panels
        JPanel schedulePanel = createSchedulePanel();

        // Create time tracking panels
        JPanel timeTrackingPanel = createTimeTrackingPanel();

        // Create leave management panels
        JPanel leavePanel = createLeavePanel();

        // Add panels to tabbed pane
        workShiftTabs.addTab("Lịch làm việc", schedulePanel);
        workShiftTabs.addTab("Chấm công", timeTrackingPanel);
        workShiftTabs.addTab("Quản lý nghỉ phép", leavePanel);

        // Add tabbed pane to main panels
        add(workShiftTabs, BorderLayout.CENTER);
    }

    /**
     * Creates the schedule management panels
     *
     * @return Configured schedule panels
     */
    private JPanel createSchedulePanel() {
        JPanel schedulePanel = new JPanel(new BorderLayout(10, 10));
        schedulePanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // TODO: Implement schedule management panels
        return schedulePanel;
    }

    /**
     * Creates the leave management panels
     *
     * @return Configured leave panels
     */
    private JPanel createLeavePanel() {
        JPanel leavePanel = new JPanel(new BorderLayout(10, 10));
        leavePanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create request form for leave
        BorderedPanel leaveFormPanel = createTitledPanel("Đơn xin nghỉ phép");
        leaveFormPanel.setPreferredSize(new Dimension(300, 0));

        // Add form components here
        // ...

        // Create buttons panels
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton saveButton = createStandardButton("Lưu thay đổi", null);
        JButton cancelButton = createStandardButton("Hủy bỏ", null);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        leaveFormPanel.add(buttonPanel);

        leavePanel.add(leaveFormPanel, BorderLayout.EAST);

        return leavePanel;
    }

    private JPanel createTimeTrackingPanel() {
        JPanel timeTrackingPanel = new JPanel(new BorderLayout(10, 10));
        timeTrackingPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create filter panels for time tracking
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

        JLabel employeeLabel = new JLabel("Nhân viên:");
        JComboBox<String> employeeCombo = new JComboBox<>(new String[]{
                "Tất cả nhân viên", "Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Phạm Thị D", "Hoàng Văn E"
        });

        JLabel fromDateLabel = new JLabel("Từ ngày:");
        JTextField fromDateField = new JTextField(10);
        fromDateField.putClientProperty("JTextField.placeholderText", "dd/mm/yyyy");

        JLabel toDateLabel = new JLabel("Đến ngày:");
        JTextField toDateField = new JTextField(10);
        toDateField.putClientProperty("JTextField.placeholderText", "dd/mm/yyyy");

        JButton filterButton = createStandardButton("Lọc", null);
        JButton exportButton = createStandardButton("Xuất báo cáo", null);

        filterPanel.add(employeeLabel);
        filterPanel.add(employeeCombo);
        filterPanel.add(fromDateLabel);
        filterPanel.add(fromDateField);
        filterPanel.add(toDateLabel);
        filterPanel.add(toDateField);
        filterPanel.add(filterButton);
        filterPanel.add(exportButton);

        timeTrackingPanel.add(filterPanel, BorderLayout.NORTH);

        // Create tabbed pane for different time tracking views
        JTabbedPane trackingTabs = new JTabbedPane();

        // Create summary panels
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create summary table with employee working hours
        String[] summaryColumns = {"Nhân viên", "Tổng giờ làm", "Giờ làm tháng này", "Giờ làm tuần này", "Giờ làm hôm nay"};
        Object[][] summaryData = {
                {"Nguyễn Văn A", "158.5", "42.0", "8.5", "0.0"},
                {"Trần Thị B", "162.0", "38.5", "10.0", "5.0"},
                {"Lê Văn C", "155.0", "40.0", "10.0", "5.0"},
                {"Phạm Thị D", "160.0", "41.5", "8.5", "0.0"},
                {"Hoàng Văn E", "156.5", "39.0", "8.0", "5.0"}
        };
        java.util.List<ScrollableTable.ActionDefinition> noActions = List.of();
        ScrollableTable summaryTable = new ScrollableTable(summaryColumns, summaryData, noActions);
        summaryPanel.add(summaryTable, BorderLayout.CENTER);

        // Create detailed panels
        JPanel detailedPanel = new JPanel(new BorderLayout());
        detailedPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Create detailed table with employee attendance records
        String[] detailedColumns = {"Nhân viên", "Ngày", "Ca làm việc", "Giờ vào", "Giờ ra", "Tổng giờ", "Ghi chú"};
        Object[][] detailedData = {
                {"Trần Thị B", "16/06/2023", "Ca sáng", "07:02", "12:05", "5.0", ""},
                {"Lê Văn C", "16/06/2023", "Ca sáng", "07:00", "12:03", "5.0", ""},
                {"Hoàng Văn E", "16/06/2023", "Ca sáng", "07:05", "12:10", "5.0", ""},
                {"Nguyễn Văn A", "15/06/2023", "Ca tối", "17:01", "22:02", "5.0", ""},
                {"Trần Thị B", "15/06/2023", "Ca chiều", "12:02", "17:05", "5.0", ""},
                {"Lê Văn C", "15/06/2023", "Ca chiều", "12:00", "17:00", "5.0", ""},
                {"Phạm Thị D", "15/06/2023", "Ca tối", "17:03", "22:05", "5.0", ""},
                {"Hoàng Văn E", "15/06/2023", "Ca tối", "17:05", "22:10", "5.0", ""},
                {"Nguyễn Văn A", "15/06/2023", "Ca sáng", "07:00", "12:05", "5.0", ""},
                {"Phạm Thị D", "15/06/2023", "Ca sáng", "07:02", "12:00", "5.0", ""}
        };

        ScrollableTable detailedTable = new ScrollableTable(detailedColumns, detailedData, List.of());
        detailedPanel.add(detailedTable, BorderLayout.CENTER);

        // Add panels to tabbed pane
        trackingTabs.addTab("Tổng hợp", summaryPanel);
        trackingTabs.addTab("Chi tiết", detailedPanel);

        timeTrackingPanel.add(trackingTabs, BorderLayout.CENTER);

        return timeTrackingPanel;
    }

    private JPanel createFormRow(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panel.setOpaque(false);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setPreferredSize(new Dimension(100, 20));
        labelComponent.setFont(new Font(labelComponent.getFont().getName(), Font.BOLD, 12));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setPreferredSize(new Dimension(150, 20));

        panel.add(labelComponent);
        panel.add(valueComponent);

        return panel;
    }

    private BorderedPanel createTitledPanel(String title) {
        BorderedPanel panel = new BorderedPanel(title);
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private JButton createStandardButton(String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }
}
