package com.neostain.csms.view.component;

import com.neostain.csms.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ScrollableContentTable extends JScrollPane {
    public ScrollableContentTable(String[] columnNames, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Không cho phép chỉnh sửa ô nào trong bảng
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);

        // Thiết lập cột 0 có kích thước cố định (300px)
        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(0).setMinWidth(300);
        table.getColumnModel().getColumn(0).setMaxWidth(300);

        // Cột 1 sẽ tự động lấp đầy phần còn lại
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

        // Đặt font in đậm cho tiêu đề cột
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.BOLD, Constants.Font.DEFAULT_SIZE));

        // Sử dụng setViewportView để thêm table vào JScrollPane
        this.setViewportView(table);
        this.setBorder(BorderFactory.createEmptyBorder());
    }
}
