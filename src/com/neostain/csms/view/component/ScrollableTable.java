package com.neostain.csms.view.component;

import com.neostain.csms.util.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ScrollableTable hỗ trợ tạo bảng có thể tùy chọn cột hành động:
 * - Không có nút action
 * - 1 hoặc nhiều nút action (Xóa, Cập nhật, hay bất kỳ logic tùy chỉnh)
 * Cung cấp phương thức refreshData để cập nhật dữ liệu mà vẫn giữ cột action.
 */
public class ScrollableTable extends JScrollPane {
    private final JTable table;
    private final String[] baseColumns;
    private final List<ActionDefinition> actions;

    /**
     * Tạo ScrollableTable với tùy chọn cột action.
     *
     * @param columnNames tên các cột dữ liệu (không gồm cột action)
     * @param data        dữ liệu ban đầu
     * @param actions     danh sách action (có thể rỗng để không có nút nào)
     */
    public ScrollableTable(String[] columnNames, Object[][] data, List<ActionDefinition> actions) {
        this.baseColumns = Arrays.copyOf(columnNames, columnNames.length);
        this.actions = new ArrayList<>(actions);
        this.table = new JTable();
        table.setRowHeight(28);
        table.setCellSelectionEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);
        refreshData(data);
        setViewportView(table);
        setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Cập nhật lại dữ liệu bảng, giữ nguyên các cột action.
     *
     * @param data mảng dữ liệu mới, mỗi row có baseColumns.length giá trị
     */
    public void refreshData(Object[][] data) {
        // Tổng số cột = baseColumns + số action
        int totalCols = baseColumns.length + actions.size();
        String[] cols = new String[totalCols];
        // copy tên cột dữ liệu
        System.arraycopy(baseColumns, 0, cols, 0, baseColumns.length);
        // thêm tên action columns
        for (int i = 0; i < actions.size(); i++) {
            cols[baseColumns.length + i] = actions.get(i).columnName;
        }

        // 3. Tạo safeData với đúng kích thước
        Object[][] safeData = new Object[data.length][totalCols];
        for (int i = 0; i < data.length; i++) {
            Object[] row = data[i];
            // Copy phần dữ liệu gốc (nếu có)
            for (int j = 0; j < baseColumns.length; j++) {
                if (row != null && j < row.length) {
                    safeData[i][j] = row[j];
                } else {
                    safeData[i][j] = null;  // điền null khi thiếu
                }
            }
            // Phần action columns để null (nút sẽ hiển thị dựa trên renderer/editor)
        }

        DefaultTableModel model = new DefaultTableModel(safeData, cols) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // chỉ cho edit ở các cột action
                return col >= baseColumns.length;
            }

            @Override
            public void setValueAt(Object aValue, int row, int col) {
                // 1) Bỏ qua nếu row hoặc col không hợp lệ
                if (row < 0 || row >= getRowCount()
                        || col < 0 || col >= getColumnCount()) {
                    return;
                }
                // 2) Chỉ lưu aValue khi là cột dữ liệu gốc
                if (col < baseColumns.length) {
                    super.setValueAt(aValue, row, col);
                }
                // Cột action hoàn toàn không gọi super -> không phát sinh setValueAt với row = -1
            }
        };
        table.setModel(model);

        // Cấu hình cho mỗi cột action
        for (int i = 0; i < actions.size(); i++) {
            ActionDefinition action = actions.get(i);
            int colIndex = baseColumns.length + i;
            try {
                table.getColumnModel().getColumn(colIndex)
                        .setCellRenderer(new ButtonRenderer(action.buttonLabel));
                table.getColumnModel().getColumn(colIndex)
                        .setCellEditor(new ButtonEditor(new JCheckBox(), action.buttonLabel, action.listener));
            } catch (IndexOutOfBoundsException ex) {
                // Nếu không tìm thấy cột, bỏ qua
            }
        }

        // Cột dữ liệu đầu tiên cố định width, có thể tuỳ chỉnh giống trước
        if (table.getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(260);
            table.getColumnModel().getColumn(0).setMinWidth(260);
            table.getColumnModel().getColumn(0).setMaxWidth(260);
        }
        // Set left alignment for all data columns (not action columns)
        for (int i = 0; i < baseColumns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new LeftCellRenderer());
        }
        // Center align header
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setFont(
                new Font(Constants.Font.DEFAULT_FONT_NAME, Font.BOLD, Constants.Font.DEFAULT_SIZE)
        );
    }

    /**
     * Lấy JTable gốc.
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Functional interface để bắt sự kiện action trên dòng.
     */
    public interface RowActionListener {
        /**
         * Gọi khi người dùng click nút ở dòng rowIndex.
         *
         * @param rowIndex chỉ số dòng
         * @param table    JTable gốc để thao tác model
         */
        void onAction(int rowIndex, JTable table);
    }

    /**
     * Mô tả một action (cột nút) trong bảng.
     */
    public static class ActionDefinition {
        public final String columnName;
        public final RowActionListener listener;
        public final String buttonLabel;

        /**
         * @param columnName  tên cột hiển thị
         * @param buttonLabel nhãn trên JButton
         * @param listener    callback khi click
         */
        public ActionDefinition(String columnName, String buttonLabel, RowActionListener listener) {
            this.columnName = columnName;
            this.buttonLabel = buttonLabel;
            this.listener = listener;
        }
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String label) {
            setText(label);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final RowActionListener listener;
        private final String label;
        private JTable tableRef;

        public ButtonEditor(JCheckBox checkBox, String label, RowActionListener listener) {
            super(checkBox);
            this.listener = listener;
            this.label = label;
            this.button = new JButton(label);
            this.button.setOpaque(true);
            this.button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.tableRef = table;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            // Lấy chỉ số dòng thực của cell đang edit
            int editingRow = tableRef.getEditingRow();
            if (editingRow >= 0 && editingRow < tableRef.getRowCount()) {
                listener.onAction(editingRow, tableRef);
            }
            return label;  // trả về label, dù model có ignore cũng không sao
        }
    }

    /**
     * Renderer to left-align cell content.
     */
    private static class LeftCellRenderer extends DefaultTableCellRenderer {
        public LeftCellRenderer() {
            setHorizontalAlignment(SwingConstants.LEFT);
        }
    }
}
