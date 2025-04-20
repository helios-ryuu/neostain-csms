package com.neostain.csms.view.screen.cs;

import com.neostain.csms.ViewManager;
import com.neostain.csms.util.ScreenType;
import com.neostain.csms.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CashierStaffScreen extends JPanel {

    public CashierStaffScreen(String username) {
        setLayout(new BorderLayout());
        add(createTopPanel(username), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createStatusPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel(String username) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(0, 80));
        panel.setBackground(Color.YELLOW);

        panel.add(new JLabel("Người dùng: " + username));
        panel.add(Box.createHorizontalGlue());

        JButton logoutButton = createLogoutButton();
        panel.add(logoutButton);

        return panel;
    }

    private JButton createLogoutButton() {
        JButton button = new JButton("Đăng xuất");
        button.setBackground(Color.WHITE);

        // Xử lý click chuột
        button.addActionListener(e -> {
            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
            ViewManager.getInstance(mainFrame).switchScreen(ScreenType.LOGIN, null);
        });

        // Hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createMenuPanel(), BorderLayout.WEST);
        panel.add(createProductPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBackground(Color.ORANGE);
        // Thêm các thành phần menu tại đây
        return panel;
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // Thêm các thành phần sản phẩm tại đây
        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(0, 30));
        panel.setBackground(Color.BLUE);
        // Thêm các thành phần trạng thái tại đây
        return panel;
    }
}