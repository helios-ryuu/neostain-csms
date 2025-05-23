package com.neostain.csms.view.screen.cs.panels;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.model.*;
import com.neostain.csms.util.Constants;
import com.neostain.csms.util.DialogFactory;
import com.neostain.csms.view.component.BorderedPanel;
import com.neostain.csms.view.component.NumericKeypadPanel;
import com.neostain.csms.view.component.ScrollableTable;
import com.neostain.csms.view.component.StandardButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POSPanel extends JPanel {
    private static final ServiceManager serviceManager = ServiceManager.getInstance();
    // --- CONSTANTS ---
    private static final Font BOLD_FONT = new Font(Constants.Font.DEFAULT_FONT_NAME, Font.BOLD, Constants.Font.DEFAULT_SIZE);
    private static final String TITLE_CART = "Giỏ hàng";
    private static final String TITLE_INFO = "Thông tin";
    private static final String TITLE_PAYMENT = "Thanh toán";
    private static final String TITLE_MEMBER = "Thành viên";
    private static final String TITLE_MEMBER_INFO = "Thông tin thành viên";
    private static final String BTN_ADD = "Thêm";
    private static final String BTN_REFRESH = "Làm mới giỏ hàng";
    private static final String BTN_CASH = "Thanh toán tiền mặt";
    private static final String BTN_EWALLET = "Thanh toán ví điện tử";
    private static final String BTN_BANK = "Thanh toán ngân hàng";
    private static final String BTN_EARN = "Tích điểm";
    private static final String BTN_USE = "Sử dụng điểm";
    private static final String BTN_CANCEL = "Hủy tích điểm";
    private static final String BTN_CANCEL_PAYMENT = "Hủy thanh toán";
    private static final String TITLE_SUCCESS = "Thanh toán thành công";
    private static final Color SUCCESS_COLOR = new Color(0, 128, 0);
    private final String[] cartColumns = {"Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "SL", "Giảm giá", "Thành tiền"};
    private final String username;
    private final java.util.Map<String, CartItem> cartItems = new java.util.LinkedHashMap<>();
    private final Map<String, Integer> promoProgress = new HashMap<>();
    private final Map<String, Integer> promoGiftCounts = new HashMap<>();
    private ScrollableTable cartTable;
    private JLabel lblType, lblQty, lblTotal, lblPointUsed, lblGrand, lblPointEarned;
    private Member currentMember = null;
    private JPanel memberInfoPanel;
    private JLabel lblMemberId, lblMemberName, lblMemberPhone, lblMemberEmail, lblMemberReg, lblMemberPoints;
    private JButton scanBtn;
    private JTextField scanField;
    private JButton refreshCartBtn;
    private boolean pointsLocked = false;
    private int usedPoints = 0;
    private List<ScrollableTable.ActionDefinition> cartActions;
    private JButton earnBtn, useBtn, cancelBtn;
    private JLabel lblDiscount;
    private PromotionProductPanel promoPanel;
    private java.math.BigDecimal totalDiscount = java.math.BigDecimal.ZERO;
    private JButton cashBtn, eWalletBtn, bankBtn;

    public POSPanel() {
        this.username = serviceManager.getCurrentUsername();
        initializeComponents();
    }

    private void initializeComponents() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.insets = new Insets(5, 5, 5, 5);

        // --- 1. Giỏ hàng (Cart) Panel ---
        BorderedPanel cartPanel = new BorderedPanel(TITLE_CART);
        cartPanel.setLayout(new BorderLayout(8, 8));
        cartPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);

        // Tool wrapper (top of a cart panel)
        JPanel toolWrapper = new JPanel();
        toolWrapper.setLayout(new BoxLayout(toolWrapper, BoxLayout.X_AXIS));
        toolWrapper.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        toolWrapper.add(new JLabel("Quét mã sản phẩm:"));
        toolWrapper.add(Box.createHorizontalStrut(8));
        scanField = new JTextField(30);
        toolWrapper.add(scanField);
        toolWrapper.add(Box.createHorizontalStrut(8));
        scanBtn = new StandardButton(this, BTN_ADD);
        scanBtn.addActionListener(e -> onAddProduct());
        toolWrapper.add(scanBtn);
        scanField.addActionListener(e -> scanBtn.doClick());
        cartPanel.add(toolWrapper, BorderLayout.NORTH);

        // Cart table
        cartActions = List.of(
                new ScrollableTable.ActionDefinition("Xóa", "Xóa", this::onDeleteProduct)
        );
        this.cartTable = new ScrollableTable(cartColumns, new Object[0][cartColumns.length], cartActions);
        JTable table = cartTable.getTable();
        table.getColumnModel().getColumn(3).setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            String productId = tbl.getValueAt(row, 0).toString();
            CartItem item = cartItems.get(productId);
            return new JLabel(item != null ? String.valueOf(item.quantity) : "1", SwingConstants.CENTER);
        });
        cartPanel.add(cartTable, BorderLayout.CENTER);

        // --- 1.5. Promotion Product Panel ---
        promoPanel = new PromotionProductPanel();
        promoPanel.setOnPromoChanged(this::updateInfoPanel);

        // --- 2. Info Panel ---
        BorderedPanel infoPanel = new BorderedPanel(TITLE_INFO);
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setMinimumSize(new Dimension(350, 0));
        infoPanel.setMaximumSize(new Dimension(350, 1000));
        infoPanel.setPreferredSize(new Dimension(350, 180));
        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(10, 20, 10, 20);
        gbcInfo.anchor = GridBagConstraints.WEST;
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 0;
        gbcInfo.gridwidth = 1;
        gbcInfo.weightx = 1;
        gbcInfo.fill = GridBagConstraints.HORIZONTAL;
        Font boldFont = new Font(Constants.Font.DEFAULT_FONT_NAME, Font.BOLD, Constants.Font.DEFAULT_SIZE);
        JLabel prodType = new JLabel("Số loại sản phẩm:");
        prodType.setFont(boldFont);
        infoPanel.add(prodType, gbcInfo);
        gbcInfo.gridx = 1;
        lblType = new JLabel("0");
        infoPanel.add(lblType, gbcInfo);
        gbcInfo.gridx = 0;
        gbcInfo.gridy++;
        JLabel qty = new JLabel("Số lượng:");
        qty.setFont(boldFont);
        infoPanel.add(qty, gbcInfo);
        gbcInfo.gridx = 1;
        lblQty = new JLabel("0");
        infoPanel.add(lblQty, gbcInfo);
        gbcInfo.gridx = 0;
        gbcInfo.gridy++;
        JLabel totalLbl = new JLabel("Thành tiền:");
        totalLbl.setFont(boldFont);
        infoPanel.add(totalLbl, gbcInfo);
        gbcInfo.gridx = 1;
        lblTotal = new JLabel("0");
        infoPanel.add(lblTotal, gbcInfo);
        gbcInfo.gridx = 0;
        gbcInfo.gridy++;
        JLabel pointUsedLbl = new JLabel("Số điểm sử dụng:");
        pointUsedLbl.setFont(boldFont);
        infoPanel.add(pointUsedLbl, gbcInfo);
        gbcInfo.gridx = 1;
        lblPointUsed = new JLabel("0");
        infoPanel.add(lblPointUsed, gbcInfo);
        gbcInfo.gridx = 0;
        gbcInfo.gridy++;
        JLabel discountLbl = new JLabel("Giảm giá:");
        discountLbl.setFont(boldFont);
        infoPanel.add(discountLbl, gbcInfo);
        gbcInfo.gridx = 1;
        lblDiscount = new JLabel("0");
        infoPanel.add(lblDiscount, gbcInfo);
        gbcInfo.gridx = 0;
        gbcInfo.gridy++;
        gbcInfo.gridwidth = 2;
        infoPanel.add(new JSeparator(), gbcInfo);
        gbcInfo.gridy++;
        gbcInfo.gridwidth = 1;
        JLabel grandLbl = new JLabel("Tổng cộng:");
        grandLbl.setFont(boldFont);
        infoPanel.add(grandLbl, gbcInfo);
        gbcInfo.gridx = 1;
        lblGrand = new JLabel("0");
        infoPanel.add(lblGrand, gbcInfo);
        gbcInfo.gridx = 0;
        gbcInfo.gridy++;
        JLabel pointEarnedLbl = new JLabel("Số điểm tích được:");
        pointEarnedLbl.setFont(boldFont);
        infoPanel.add(pointEarnedLbl, gbcInfo);
        gbcInfo.gridx = 1;
        lblPointEarned = new JLabel("0");
        infoPanel.add(lblPointEarned, gbcInfo);

        // Add a refresh cart button to an info panel
        gbcInfo.gridx = 0;
        gbcInfo.gridy++;
        gbcInfo.gridwidth = 2;
        refreshCartBtn = new StandardButton(this, BTN_REFRESH);
        refreshCartBtn.addActionListener(e -> onRefreshCart());
        infoPanel.add(refreshCartBtn, gbcInfo);

        // --- 3. Payment Panel ---
        BorderedPanel paymentPanel = new BorderedPanel(TITLE_PAYMENT);
        paymentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcPay = new GridBagConstraints();
        gbcPay.insets = new Insets(4, 4, 4, 4);
        gbcPay.gridx = 0;
        gbcPay.gridy = 0;
        gbcPay.fill = GridBagConstraints.HORIZONTAL;
        gbcPay.weightx = 1;
        cashBtn = new StandardButton(this, BTN_CASH);
        cashBtn.addActionListener(e -> onCashPayment());
        paymentPanel.add(cashBtn, gbcPay);
        gbcPay.gridy++;
        eWalletBtn = new StandardButton(this, BTN_EWALLET);
        eWalletBtn.addActionListener(e -> onEWalletPayment());
        paymentPanel.add(eWalletBtn, gbcPay);
        gbcPay.gridy++;
        bankBtn = new StandardButton(this, BTN_BANK);
        bankBtn.addActionListener(e -> onBankPayment());
        paymentPanel.add(bankBtn, gbcPay);
        // Disable payment buttons at startup
        cashBtn.setEnabled(false);
        eWalletBtn.setEnabled(false);
        bankBtn.setEnabled(false);

        // --- 4. Member Panel ---
        BorderedPanel memberPanel = new BorderedPanel(TITLE_MEMBER);
        memberPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcMem = new GridBagConstraints();
        gbcMem.insets = new Insets(4, 4, 4, 4);
        gbcMem.gridx = 0;
        gbcMem.gridy = 0;
        gbcMem.fill = GridBagConstraints.HORIZONTAL;
        gbcMem.weightx = 1;
        earnBtn = new StandardButton(this, BTN_EARN);
        earnBtn.addActionListener(e -> onEarnPoints());
        memberPanel.add(earnBtn, gbcMem);
        gbcMem.gridy++;
        useBtn = new StandardButton(this, BTN_USE);
        useBtn.addActionListener(e -> onUsePoints());
        memberPanel.add(useBtn, gbcMem);
        gbcMem.gridy++;
        cancelBtn = new StandardButton(this, BTN_CANCEL);
        cancelBtn.addActionListener(e -> onCancelPoints());
        memberPanel.add(cancelBtn, gbcMem);

        // --- 5. Member Info Panel ---
        BorderedPanel memberInfoPanelWrap = new BorderedPanel(TITLE_MEMBER_INFO);
        memberInfoPanel = new JPanel(new GridBagLayout());
        memberInfoPanel.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        GridBagConstraints gbcMinf = new GridBagConstraints();
        gbcMinf.insets = new Insets(2, 0, 2, 200);
        gbcMinf.anchor = GridBagConstraints.WEST;
        gbcMinf.gridx = 0;
        gbcMinf.gridy = 0;
        gbcMinf.gridwidth = 1;
        gbcMinf.weightx = 0;
        gbcMinf.fill = GridBagConstraints.HORIZONTAL;
        memberInfoPanel.add(labelBold("Mã thành viên:", boldFont), gbcMinf);
        gbcMinf.gridx = 1;
        lblMemberId = new JLabel("-");
        memberInfoPanel.add(lblMemberId, gbcMinf);
        gbcMinf.gridx = 0;
        gbcMinf.gridy++;
        memberInfoPanel.add(labelBold("Tên thành viên:", boldFont), gbcMinf);
        gbcMinf.gridx = 1;
        lblMemberName = new JLabel("-");
        memberInfoPanel.add(lblMemberName, gbcMinf);
        gbcMinf.gridx = 0;
        gbcMinf.gridy++;
        memberInfoPanel.add(labelBold("Số điện thoại:", boldFont), gbcMinf);
        gbcMinf.gridx = 1;
        lblMemberPhone = new JLabel("-");
        memberInfoPanel.add(lblMemberPhone, gbcMinf);
        gbcMinf.gridx = 0;
        gbcMinf.gridy++;
        memberInfoPanel.add(labelBold("Email:", boldFont), gbcMinf);
        gbcMinf.gridx = 1;
        lblMemberEmail = new JLabel("-");
        memberInfoPanel.add(lblMemberEmail, gbcMinf);
        gbcMinf.gridx = 0;
        gbcMinf.gridy++;
        memberInfoPanel.add(labelBold("Ngày đăng kí:", boldFont), gbcMinf);
        gbcMinf.gridx = 1;
        lblMemberReg = new JLabel("-");
        memberInfoPanel.add(lblMemberReg, gbcMinf);
        gbcMinf.gridx = 0;
        gbcMinf.gridy++;
        memberInfoPanel.add(labelBold("Số điểm tích lũy:", boldFont), gbcMinf);
        gbcMinf.gridx = 1;
        lblMemberPoints = new JLabel("-");
        memberInfoPanel.add(lblMemberPoints, gbcMinf);
        memberInfoPanelWrap.add(memberInfoPanel);
        memberInfoPanelWrap.setPreferredSize(new Dimension(260, 180));

        // --- Layout: Left column (cart + 3 panels below + promo panel) ---
        JPanel leftColumn = new JPanel(new GridBagLayout());
        leftColumn.setBackground(Constants.Color.COMPONENT_BACKGROUND_WHITE);
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.fill = GridBagConstraints.BOTH;
        gbcLeft.insets = new Insets(0, 5, 0, 5);

        // a) cartPanel
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.gridwidth = 3;
        gbcLeft.weightx = 1.0;
        gbcLeft.weighty = 1.0;
        leftColumn.add(cartPanel, gbcLeft);

        // e) promoPanel (cùng hàng, expand ngang còn lại)
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 1;
        gbcLeft.gridwidth = 3;
        gbcLeft.weightx = 1.0;
        gbcLeft.weighty = 0.0;
        leftColumn.add(promoPanel, gbcLeft);

        // b) paymentPanel
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 2;
        gbcLeft.gridwidth = 1;
        gbcLeft.weightx = 0.0;
        gbcLeft.weighty = 0.0;
        leftColumn.add(paymentPanel, gbcLeft);


        // c) memberPanel
        gbcLeft.gridx = 1;
        gbcLeft.gridy = 2;
        gbcLeft.gridwidth = 1;
        gbcLeft.weightx = 0.0;
        gbcLeft.weighty = 0.0;
        leftColumn.add(memberPanel, gbcLeft);


        // d) memberInfoPanelWrap
        gbcLeft.gridx = 2;
        gbcLeft.gridy = 2;
        gbcLeft.gridwidth = 1;
        gbcLeft.weightx = 1.0;
        gbcLeft.weighty = 0.0;
        leftColumn.add(memberInfoPanelWrap, gbcLeft);

        // --- Add leftColumn and infoPanel to POSPanel ---
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.weightx = 1.0;
        gbcMain.weighty = 1.0;
        this.add(leftColumn, gbcMain);
        gbcMain.gridx = 1;
        gbcMain.gridy = 0;
        gbcMain.gridheight = 2;
        gbcMain.weightx = 0.0;
        gbcMain.weighty = 1.0;
        this.add(infoPanel, gbcMain);

        // Đổi màu nút làm mới giỏ hàng thành đỏ
        refreshCartBtn.setForeground(Color.RED);
    }

    private void updateMemberInfoPanel() {
        if (currentMember == null) return;
        lblMemberId.setText(currentMember.getId());
        lblMemberName.setText(currentMember.getName());
        lblMemberPhone.setText(currentMember.getPhoneNumber());
        lblMemberEmail.setText(currentMember.getEmail());
        lblMemberReg.setText(currentMember.getRegistrationDate().toString());
        lblMemberPoints.setText(String.valueOf(currentMember.getLoyaltyPoints()));
    }

    private void clearMemberInfoPanel() {
        lblMemberId.setText("-");
        lblMemberName.setText("-");
        lblMemberPhone.setText("-");
        lblMemberEmail.setText("-");
        lblMemberReg.setText("-");
        lblMemberPoints.setText("-");
    }

    private void refreshCartTable() {
        refreshCartTable(false);
    }

    private void refreshCartTable(boolean skipPromoDialog) {
        // 1. Prepare cart data
        java.util.List<CartItem> cartList = new java.util.ArrayList<>(cartItems.values());
        Object[][] data = new Object[cartList.size()][cartColumns.length];
        int i = 0, totalQty = 0;
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        totalDiscount = java.math.BigDecimal.ZERO;
        List<com.neostain.csms.model.Promotion> activePromos = ServiceManager.getInstance().getSaleService().getActivePromotions();
        // Fill cart table data
        for (CartItem item : cartList) {
            Product p = item.product;
            int quantity = item.quantity;
            java.math.BigDecimal line = p.getUnitPrice().multiply(java.math.BigDecimal.valueOf(quantity));
            java.math.BigDecimal lineDiscount = java.math.BigDecimal.ZERO;
            // Tìm khuyến mãi giảm giá áp dụng cho sản phẩm này
            for (Promotion promo : activePromos) {
                if (promo.getProductId() != null && promo.getProductId().equals(p.getId())
                        && promo.getDiscountRate() != null && promo.getDiscountRate().compareTo(java.math.BigDecimal.ZERO) > 0) {
                    int sets = quantity / promo.getMinimumPurchaseQuantity();
                    if (sets > 0) {
                        java.math.BigDecimal discount = p.getUnitPrice()
                                .multiply(java.math.BigDecimal.valueOf(promo.getMinimumPurchaseQuantity()))
                                .multiply(java.math.BigDecimal.valueOf(sets))
                                .multiply(promo.getDiscountRate());
                        lineDiscount = lineDiscount.add(discount);
                    }
                }
            }
            data[i][0] = p.getId();
            data[i][1] = p.getName();
            data[i][2] = p.getUnitPrice().setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND";
            data[i][3] = quantity;
            data[i][4] = lineDiscount.compareTo(java.math.BigDecimal.ZERO) > 0 ? lineDiscount.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND" : "0 VND";
            data[i][5] = line.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND";
            total = total.add(line);
            totalDiscount = totalDiscount.add(lineDiscount);
            totalQty += quantity;
            i++;
        }
        cartTable.refreshData(data);
        updateInfoPanel();
        // Enable/disable payment buttons
        boolean hasProduct = !cartItems.isEmpty();
        cashBtn.setEnabled(hasProduct);
        eWalletBtn.setEnabled(hasProduct);
        bankBtn.setEnabled(hasProduct);
        int typeCount = cartItems.size();
        java.math.BigDecimal grand;
        int earned = 0, pointUsed;
        // 2. Add points discount (but do NOT accumulate)
        pointUsed = (pointsLocked && usedPoints > 0) ? usedPoints : 0;
        java.math.BigDecimal pointDiscount = java.math.BigDecimal.valueOf(pointUsed * 40L);
        java.math.BigDecimal totalDiscountFinal = totalDiscount.add(pointDiscount);
        grand = total.subtract(totalDiscountFinal);
        if (currentMember != null)
            earned = total.divide(java.math.BigDecimal.valueOf(1000), java.math.RoundingMode.DOWN).intValue();
        lblType.setText(String.valueOf(typeCount));
        lblQty.setText(String.valueOf(totalQty));
        lblTotal.setText(total.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND");
        lblPointUsed.setText(String.valueOf(pointUsed));
        lblDiscount.setText(totalDiscount.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND");
        lblGrand.setText(grand.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND");
        lblPointEarned.setText(String.valueOf(earned));
    }

    private void onAddProduct() {
        if (pointsLocked) return;
        String productId = scanField.getText().trim();
        if (productId.isEmpty()) return;
        Product product = ServiceManager.getInstance().getSaleService().getProductById(productId);
        if (product == null) {
            DialogFactory.showErrorDialog(this, "Lỗi", "Không tìm thấy sản phẩm với mã: " + productId);
            return;
        }
        cartItems.compute(productId, (k, v) -> v == null ? new CartItem(product, 1) : new CartItem(product, v.quantity + 1));
        // --- Promotion logic: rolling counter ---
        List<Promotion> activePromos = ServiceManager.getInstance().getSaleService().getActivePromotions();
        for (Promotion promo : activePromos) {
            if (promo.getProductId() != null && promo.getProductId().equals(productId)) {
                int progress = promoProgress.getOrDefault(promo.getId(), 0) + 1;
                int sets = 0;
                while (progress >= promo.getMinimumPurchaseQuantity()) {
                    sets++;
                    progress -= promo.getMinimumPurchaseQuantity();
                }
                promoProgress.put(promo.getId(), progress);
                if (sets > 0) {
                    // Add gifts
                    if (promo.getPromoProductId() != null && promo.getPromoProductQuantity() > 0) {
                        Product promoProduct = ServiceManager.getInstance().getSaleService().getProductById(promo.getPromoProductId());
                        if (promoProduct != null) {
                            promoPanel.addPromoProduct(promoProduct, sets * promo.getPromoProductQuantity());
                        }
                    }
                    // Track the number of sets for discount
                    promoGiftCounts.put(promo.getId(), promoGiftCounts.getOrDefault(promo.getId(), 0) + sets);
                }
            }
        }
        refreshCartTable();
        scanField.setText("");
        updateInfoPanel();
    }

    private void onDeleteProduct(int row, JTable table) {
        if (pointsLocked) return;
        String productId = table.getValueAt(row, 0).toString();
        CartItem item = cartItems.get(productId);
        int qty = item != null ? item.quantity : 1;
        cartItems.remove(productId);
        // --- Promotion logic: rolling counter ---
        List<Promotion> activePromos = ServiceManager.getInstance().getSaleService().getActivePromotions();
        for (Promotion promo : activePromos) {
            if (promo.getProductId() != null && promo.getProductId().equals(productId)) {
                int progress = promoProgress.getOrDefault(promo.getId(), 0);
                int total = progress + promoGiftCounts.getOrDefault(promo.getId(), 0) * promo.getMinimumPurchaseQuantity();
                int newTotal = Math.max(0, total - qty);
                int newSets = newTotal / promo.getMinimumPurchaseQuantity();
                int oldSets = promoGiftCounts.getOrDefault(promo.getId(), 0);
                int setsToRemove = oldSets - newSets;
                // Remove gifts if needed
                if (setsToRemove > 0 && promo.getPromoProductId() != null && promo.getPromoProductQuantity() > 0) {
                    Product promoProduct = ServiceManager.getInstance().getSaleService().getProductById(promo.getPromoProductId());
                    if (promoProduct != null) {
                        promoPanel.removePromoProduct(promoProduct.getId(), setsToRemove * promo.getPromoProductQuantity());
                    }
                }
                promoGiftCounts.put(promo.getId(), newSets);
                promoProgress.put(promo.getId(), newTotal % promo.getMinimumPurchaseQuantity());
            }
        }
        refreshCartTable(true);
        updateInfoPanel();
    }

    private java.math.BigDecimal getCartTotal() {
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (CartItem item : cartItems.values()) {
            total = total.add(item.product.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.quantity)));
        }
        return total;
    }

    // --- Event Handlers ---
    private void onEarnPoints() {
        if (pointsLocked) return;
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Nhập số điện thoại thành viên:"));
        JTextField phoneField = new JTextField(15);
        inputPanel.add(phoneField);
        int res = DialogFactory.showConfirmOkCancelDialog(this, "Tích điểm thành viên", inputPanel);
        if (res == JOptionPane.OK_OPTION) {
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) return;
            Member m = ServiceManager.getInstance().getManagementService().getMemberByPhoneNumber(phone);
            if (m == null) {
                DialogFactory.showErrorDialog(this, "Không tìm thấy", "Không tìm thấy thành viên với số điện thoại: " + phone);
                return;
            }
            JPanel memberInfoPanel = new JPanel(new GridLayout(0, 1));
            memberInfoPanel.add(new JLabel("Mã: " + m.getId()));
            memberInfoPanel.add(new JLabel("Tên: " + m.getName()));
            memberInfoPanel.add(new JLabel("SĐT: " + m.getPhoneNumber()));
            memberInfoPanel.add(new JLabel("Email: " + m.getEmail()));
            memberInfoPanel.add(new JLabel("Ngày đăng kí: " + m.getRegistrationDate()));
            memberInfoPanel.add(new JLabel("Điểm tích lũy: " + m.getLoyaltyPoints()));
            boolean confirm = DialogFactory.showConfirmYesNoDialog(this, "Xác nhận thành viên", memberInfoPanel);
            if (confirm) {
                currentMember = m;
                updateMemberInfoPanel();
                refreshCartTable();
            }
        }
    }

    private void onUsePoints() {
        if (pointsLocked) return;
        if (currentMember == null) {
            DialogFactory.showErrorDialog(this, "Không có thành viên", "Không có thông tin thành viên.\nHãy nhập thông tin thành viên bằng nút Tích điểm trong menu Thành viên");
            return;
        }
        int res = DialogFactory.showConfirmOkCancelDialog(this, "Xác nhận tiến hành sử dụng điểm", "Hệ thống chuẩn bị tiến vào quá trình sử dụng điểm. Sau khi xác nhận sử dụng điểm sẽ không thể thêm sản phẩm mới vào giỏ hàng. Nhấn OK để tiếp tục.");
        if (res != JOptionPane.OK_OPTION) return;
        java.math.BigDecimal total = getCartTotal();
        int memberPoints = currentMember.getLoyaltyPoints();
        int usablePoints = total.divide(java.math.BigDecimal.valueOf(40), java.math.RoundingMode.DOWN).intValue();
        if (usablePoints > memberPoints) usablePoints = memberPoints;
        JPanel info = new JPanel(new GridLayout(0, 1));
        info.add(new JLabel("Mã thành viên: " + currentMember.getId()));
        info.add(new JLabel("Tên thành viên: " + currentMember.getName()));
        info.add(new JLabel("Số điểm hiện có: " + memberPoints));
        info.add(new JLabel("Thành tiền của hóa đơn: " + total));
        JLabel status = new JLabel();
        info.add(status);
        if (memberPoints < 25) {
            status.setText("Không thể sử dụng điểm (Số điểm dưới 25 điểm)");
            status.setForeground(Color.RED);
            Object[] options = {"Quay về trang thanh toán"};
            JOptionPane.showOptionDialog(this, info, "Không thể sử dụng điểm", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        } else if (usablePoints == 0) {
            status.setText("Không thể sử dụng điểm (Giá trị hóa đơn bằng 0)");
            status.setForeground(Color.RED);
            Object[] options = {"Quay về trang thanh toán"};
            JOptionPane.showOptionDialog(this, info, "Không thể sử dụng điểm", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        } else {
            status.setText("Có thể sử dụng " + usablePoints + " điểm");
            status.setForeground(new Color(0, 128, 0));
            Object[] options = {"Xác nhận sử dụng điểm", "Hủy sử dụng điểm"};
            int choice = JOptionPane.showOptionDialog(this, info, "Xác nhận sử dụng điểm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == JOptionPane.OK_OPTION) {
                pointsLocked = true;
                usedPoints = usablePoints;
                scanField.setEnabled(false);
                scanBtn.setEnabled(false);
                earnBtn.setEnabled(false);
                useBtn.setEnabled(false);
                promoPanel.setDeleteEnabled(false);
                refreshCartTable();
            }
        }
    }

    private void onCancelPoints() {
        int res = DialogFactory.showConfirmOkCancelDialog(this, "Xác nhận", "Bạn có chắc chắn muốn hủy tích điểm và xóa thông tin thành viên?");
        if (res == JOptionPane.OK_OPTION) {
            currentMember = null;
            clearMemberInfoPanel();
            pointsLocked = false;
            usedPoints = 0;
            scanField.setEnabled(true);
            scanBtn.setEnabled(true);
            earnBtn.setEnabled(true);
            useBtn.setEnabled(true);
            promoPanel.setDeleteEnabled(true);
            refreshCartTable();
            updateInfoPanel();
        }
    }

    private void onRefreshCart() {
        int res = DialogFactory.showConfirmOkCancelDialog(this, "Xác nhận làm mới giỏ hàng", "Chuẩn bị tiến hành làm mới giỏ hàng. Hành động này sẽ xóa bỏ tất cả sản phẩm đã quét được và xóa bỏ thông tin khách hàng. Nhấn xác nhận để tiến hành.");
        if (res == JOptionPane.OK_OPTION) {
            cartItems.clear();
            promoProgress.clear();
            promoGiftCounts.clear();
            promoPanel.clearAll();
            promoPanel.setDeleteEnabled(true);
            currentMember = null;
            clearMemberInfoPanel();
            pointsLocked = false;
            usedPoints = 0;
            scanField.setEnabled(true);
            scanBtn.setEnabled(true);
            earnBtn.setEnabled(true);
            useBtn.setEnabled(true);
            refreshCartTable();
            DialogFactory.showInfoDialog(this, "Làm mới giỏ hàng", "Làm mới giỏ hàng thành công");
            updateInfoPanel();
            cashBtn.setEnabled(false);
            eWalletBtn.setEnabled(false);
            bankBtn.setEnabled(false);
        }
    }

    private void onRefreshCart(boolean noConfirm) {
        if (noConfirm) {
            cartItems.clear();
            promoProgress.clear();
            promoGiftCounts.clear();
            promoPanel.clearAll();
            promoPanel.setDeleteEnabled(true);
            currentMember = null;
            clearMemberInfoPanel();
            pointsLocked = false;
            usedPoints = 0;
            scanField.setEnabled(true);
            scanBtn.setEnabled(true);
            earnBtn.setEnabled(true);
            useBtn.setEnabled(true);
            refreshCartTable();
            updateInfoPanel();
            cashBtn.setEnabled(false);
            eWalletBtn.setEnabled(false);
            bankBtn.setEnabled(false);
        }
    }

    private void updateInfoPanel() {
        int promoTypeCount = promoPanel.getPromoEntries().size();
        int promoQty = promoPanel.getPromoEntries().values().stream().mapToInt(PromotionProductPanel.PromoEntry::quantity).sum();
        int typeCountFinal = cartItems.size() + promoTypeCount;
        int totalQtyFinal = cartItems.values().stream().mapToInt(item -> item.quantity).sum() + promoQty;
        lblType.setText(String.valueOf(typeCountFinal));
        lblQty.setText(String.valueOf(totalQtyFinal));
    }

    private JLabel labelBold(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private void onCashPayment() {
        if (!pointsLocked) {
            int confirm = DialogFactory.showConfirmOkCancelDialog(this, "Xác nhận thanh toán", "Bạn chưa sử dụng điểm. Tiếp tục thanh toán?");
            if (confirm != JOptionPane.OK_OPTION) return;
        }
        // --- INVENTORY CHECK LOGIC ---
        Account account = serviceManager.getAuthService().getAccountByUsername(username);
        Employee employee = serviceManager.getManagementService().getEmployeeById(account.getEmployeeId());
        Store store = serviceManager.getManagementService().getStoreByManagerId(employee.getManagerId());
        String storeId = store.getId();
        // Build productId -> required quantity (cart + promo)
        java.util.Map<String, Integer> required = new java.util.HashMap<>();
        for (CartItem item : cartItems.values()) {
            required.put(item.product.getId(), required.getOrDefault(item.product.getId(), 0) + item.quantity);
        }
        for (PromotionProductPanel.PromoEntry promoEntry : promoPanel.getPromoEntries().values()) {
            required.put(promoEntry.product().getId(), required.getOrDefault(promoEntry.product().getId(), 0) + promoEntry.quantity());
        }
        java.util.List<Object[]> insufficient = new java.util.ArrayList<>();
        for (var entry : required.entrySet()) {
            String pid = entry.getKey();
            int reqQty = entry.getValue();
            int invQty = 0;
            var invs = serviceManager.getSaleService().getInventoriesByProductId(pid);
            for (var inv : invs) {
                if (storeId.equals(inv.getStoreId())) {
                    invQty = inv.getQuantity();
                    break;
                }
            }
            if (reqQty > invQty) {
                Product p = serviceManager.getSaleService().getProductById(pid);
                insufficient.add(new Object[]{pid, p != null ? p.getName() : "", "Thiếu " + (reqQty - invQty) + " sản phẩm"});
            }
        }
        if (!insufficient.isEmpty()) {
            // Show warning dialog with ScrollableTable
            String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Tình trạng"};
            Object[][] data = insufficient.toArray(new Object[0][]);
            ScrollableTable table = new ScrollableTable(cols, data, java.util.List.of());
            DialogFactory.showWarningDialog(this, "Thông báo tồn kho", table);
            return;
        }
        // --- END INVENTORY CHECK LOGIC ---
        java.math.BigDecimal totalDue = getPanelGrandTotal();
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tiến hành thanh toán bằng tiền mặt", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel lblTotal = new JLabel("Số tiền phải trả:");
        dialog.add(lblTotal, gbc);
        gbc.gridx = 1;
        JLabel total = new JLabel(totalDue.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND");
        dialog.add(total, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblGiven = new JLabel("Số tiền khách đưa:");
        dialog.add(lblGiven, gbc);
        gbc.gridx = 1;
        JPanel givenPanel = new JPanel(new BorderLayout(5, 0));
        JTextField givenField = new JTextField(12);
        JButton btnExactCash = new JButton("Đủ tiền mặt");
        givenPanel.add(givenField, BorderLayout.CENTER);
        givenPanel.add(btnExactCash, BorderLayout.EAST);
        dialog.add(givenPanel, gbc);
        // Focus vào field nhập số tiền khi mở dialog
        SwingUtilities.invokeLater(givenField::requestFocusInWindow);
        // Chỉ cho nhập số
        givenField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                }
            }
        });
        // Đủ tiền mặt button logic
        btnExactCash.addActionListener(ev -> {
            givenField.setText(totalDue.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString());
        });
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblChange = new JLabel("Số tiền trả lại khách:");
        dialog.add(lblChange, gbc);
        gbc.gridx = 1;
        JLabel changeField = new JLabel();
        dialog.add(changeField, gbc);
        // Numeric keypad
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        NumericKeypadPanel keypad = new NumericKeypadPanel(givenField);
        dialog.add(keypad, gbc);
        // Button panel
        gbc.gridy++;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton confirmBtn = new JButton("Xác nhận thanh toán");
        JButton backBtn = new JButton("Trở lại giỏ hàng");
        btnPanel.add(confirmBtn);
        btnPanel.add(backBtn);
        dialog.add(btnPanel, gbc);
        confirmBtn.setEnabled(false);
        // Tính tiền trả lại khi nhập số tiền khách đưa
        givenField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            void update() {
                try {
                    String text = givenField.getText().replaceAll("[^0-9]", "");
                    java.math.BigDecimal given = text.isEmpty() ? java.math.BigDecimal.ZERO : new java.math.BigDecimal(text);
                    java.math.BigDecimal change = given.subtract(totalDue);
                    if (text.isEmpty() || change.compareTo(java.math.BigDecimal.ZERO) < 0) {
                        changeField.setText("0 VND");
                        confirmBtn.setEnabled(false);
                    } else {
                        changeField.setText(change.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND");
                        confirmBtn.setEnabled(true);
                    }
                } catch (Exception ex) {
                    changeField.setText("0 VND");
                    confirmBtn.setEnabled(false);
                }
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }
        });
        // Nút trở lại
        backBtn.addActionListener(ev -> dialog.dispose());
        // Nút xác nhận thanh toán
        confirmBtn.addActionListener(ev -> {
            String givenStr = givenField.getText().replaceAll("[^0-9]", "");
            java.math.BigDecimal given = givenStr.isEmpty() ? java.math.BigDecimal.ZERO : new java.math.BigDecimal(givenStr);
            // Dialog xác nhận
            JDialog confirmDialog = new JDialog(dialog, "Xác nhận thanh toán", true);
            confirmDialog.setLayout(new BorderLayout(10, 10));
            JPanel msgPanel = new JPanel(new GridLayout(0, 1));
            msgPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            msgPanel.add(new JLabel("Xác nhận đã nhận " + given.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND và thanh toán " + totalDue.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND cho hóa đơn này?"));
            confirmDialog.add(msgPanel, BorderLayout.CENTER);
            JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton okBtn = new JButton("Xác nhận thanh toán");
            JButton cancelBtn = new JButton("Hủy thanh toán");
            btns.add(okBtn);
            btns.add(cancelBtn);
            confirmDialog.add(btns, BorderLayout.SOUTH);
            okBtn.addActionListener(e2 -> {
                String invoiceId = serviceManager.getSaleService().createInvoice(store.getId(), (currentMember != null ? currentMember.getId() : null), "P001", employee.getId(), usedPoints);
                for (CartItem item : cartItems.values()) {
                    serviceManager.getSaleService().addItemToInvoice(invoiceId, item.product.getId(), item.quantity);
                }
                for (PromotionProductPanel.PromoEntry promoEntry : promoPanel.getPromoEntries().values()) {
                    serviceManager.getSaleService().addGiftToInvoice(invoiceId, promoEntry.product().getId(), promoEntry.quantity());
                }
                boolean invoiceCalculated = serviceManager.getSaleService().calculateInvoiceTotal(invoiceId);
                if (invoiceCalculated) {
                    Invoice invoice = serviceManager.getSaleService().getInvoiceById(invoiceId);
                    File invoiceFile = serviceManager.getPrintingService().printInvoice(invoice);
                    String fileName = "Invoice_" + invoiceId;

                    DialogFactory.showInfoDialog(
                            this,
                            "Thông báo",
                            "Thanh toán thành công cho hóa đơn " + invoiceId +
                                    "\nĐã in hóa đơn: " + invoiceId +
                                    "\ntại cửa hàng: " + store.getId() + " - " + store.getName() +
                                    "\n\nHóa đơn đã được lưu vào: reports/" + fileName + ".pdf"
                    );

                    if (invoiceFile != null) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(invoiceFile);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    confirmDialog.dispose();
                    dialog.dispose();
                    onRefreshCart(true);
                }
            });
            cancelBtn.addActionListener(e2 -> confirmDialog.dispose());
            confirmDialog.pack();
            confirmDialog.setLocationRelativeTo(dialog);
            confirmDialog.setVisible(true);
        });
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private java.math.BigDecimal getPanelGrandTotal() {
        // Lấy tổng cộng từ panel thông tin (lblGrand), loại bỏ VND và làm tròn
        String text = lblGrand.getText().replace(" VND", "").replaceAll("[^0-9-]", "");
        if (text.isEmpty()) return java.math.BigDecimal.ZERO;
        return new java.math.BigDecimal(text);
    }

    private void onEWalletPayment() {
        if (!pointsLocked) {
            int confirm = DialogFactory.showConfirmOkCancelDialog(this, "Xác nhận thanh toán", "Bạn chưa sử dụng điểm. Tiếp tục thanh toán?");
            if (confirm != JOptionPane.OK_OPTION) return;
        }
        int confirm = DialogFactory.showConfirmOkCancelDialog(this, "Xác nhận thanh toán", "Xác nhận thanh toán bằng ví điện tử?");
        if (confirm != JOptionPane.OK_OPTION) return;
        java.math.BigDecimal totalDue = getPanelGrandTotal();
        // --- Chọn phương thức ví điện tử ---
        JDialog methodDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn phương thức thanh toán", true);
        methodDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitle = new JLabel("Chọn phương thức thanh toán");
        lblTitle.setFont(new Font(Constants.Font.DEFAULT_FONT_NAME, Font.BOLD, 14));
        methodDialog.add(lblTitle, gbc);
        gbc.gridy++;
        ButtonGroup group = new ButtonGroup();
        JRadioButton rbMomo = new JRadioButton("Thanh toán ví điện tử MOMO", true);
        JRadioButton rbZalo = new JRadioButton("Thanh toán ví điện tử ZALOPAY");
        rbMomo.setFont(Constants.View.DEFAULT_FONT);
        rbZalo.setFont(Constants.View.DEFAULT_FONT);
        group.add(rbMomo);
        group.add(rbZalo);
        gbc.gridwidth = 2;
        methodDialog.add(rbMomo, gbc);
        gbc.gridy++;
        methodDialog.add(rbZalo, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton btnConfirm = new JButton("Xác nhận phương thức");
        JButton btnCancel = new JButton("Hủy thanh toán");
        gbc.gridx = 0;
        methodDialog.add(btnConfirm, gbc);
        gbc.gridx = 1;
        methodDialog.add(btnCancel, gbc);
        btnCancel.addActionListener(ev -> methodDialog.dispose());
        btnConfirm.addActionListener(ev -> {
            methodDialog.dispose();
            String method = rbMomo.isSelected() ? "MOMO" : "ZALOPAY";
            showEWalletQRDialog(totalDue, method);
        });
        methodDialog.pack();
        methodDialog.setLocationRelativeTo(this);
        methodDialog.setVisible(true);
    }

    private void showEWalletQRDialog(java.math.BigDecimal totalDue, String method) {
        String qrData = method.equals("MOMO")
                ? "00020101021138620010A00000072701320006970454011899MM23329M470844320208QRIBFTTA53037045802VN62420515MOMOW2W470844320819THANH TOAN NEOSTAIN63042A1D"
                : "00020101021126400010vn.zalopay0115EvuIH1jpvXIFYlQ020300238620010A00000072701320006970454011899ZP24144M251173630208QRIBFTTA5204739953037045802VN62230819THANH TOAN NEOSTAIN63041917";
        String methodName = method.equals("MOMO") ? "ví điện tử MOMO" : "ví điện tử ZALOPAY";
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thanh toán " + methodName, true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTotal = new JLabel("Số tiền: " + totalDue.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND");
        dialog.add(lblTotal, gbc);
        gbc.gridy++;
        JLabel qrLabel = new JLabel();
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qrLabel.setPreferredSize(new Dimension(200, 200));
        try {
            java.awt.Image qrImg = generateQRCode(qrData, 200, 200);
            qrLabel.setIcon(new ImageIcon(qrImg));
        } catch (Exception ex) {
            qrLabel.setText("[Không thể tạo mã QR]");
        }
        dialog.add(qrLabel, gbc);
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton btnCancel = new JButton("Hủy thanh toán");
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, gbc);
        btnCancel.addActionListener(ev -> dialog.dispose());
        // Key listener for Ctrl+Shift+Alt+P
        dialog.addKeyListener(new java.awt.event.KeyAdapter() {
            boolean paid = false;

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (!paid && e.isControlDown() && e.isShiftDown() && e.isAltDown() && e.getKeyCode() == java.awt.event.KeyEvent.VK_P) {
                    paid = true;
                    dialog.dispose();
                    String message = "Đã thanh toán thành công!\nSố tiền: " + totalDue.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND bằng phương thức " + methodName;
                    showSuccessDialog(message, () -> processPayment(method.equals("MOMO") ? "P002" : "P004"));
                }
            }
        });
        dialog.setFocusable(true);
        dialog.setFocusTraversalKeysEnabled(false);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Helper: generate QR code image
    private java.awt.Image generateQRCode(String data, int width, int height) {
        // Use ZXing or similar library if available, else fallback to blank
        try {
            com.google.zxing.qrcode.QRCodeWriter qrWriter = new com.google.zxing.qrcode.QRCodeWriter();
            com.google.zxing.common.BitMatrix bitMatrix = qrWriter.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, width, height);
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }
            return image;
        } catch (Throwable t) {
            return new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        }
    }

    // Helper: process payment and create invoice (reuse logic from cash)
    private void processPayment(String paymentId) {
        Account account = serviceManager.getAuthService().getAccountByUsername(username);
        Employee employee = serviceManager.getManagementService().getEmployeeById(account.getEmployeeId());
        Store store = serviceManager.getManagementService().getStoreByManagerId(employee.getManagerId());
        String invoiceId = serviceManager.getSaleService().createInvoice(store.getId(), (currentMember != null ? currentMember.getId() : null), paymentId, employee.getId(), usedPoints);
        for (CartItem item : cartItems.values()) {
            serviceManager.getSaleService().addItemToInvoice(invoiceId, item.product.getId(), item.quantity);
        }
        for (PromotionProductPanel.PromoEntry promoEntry : promoPanel.getPromoEntries().values()) {
            serviceManager.getSaleService().addGiftToInvoice(invoiceId, promoEntry.product().getId(), promoEntry.quantity());
        }
        boolean invoiceCalculated = serviceManager.getSaleService().calculateInvoiceTotal(invoiceId);
        if (invoiceCalculated) {
            Invoice invoice = serviceManager.getSaleService().getInvoiceById(invoiceId);
            File invoiceFile = serviceManager.getPrintingService().printInvoice(invoice);
            String fileName = "Invoice_" + invoiceId;
            DialogFactory.showInfoDialog(
                    this,
                    "Thông báo",
                    "Thanh toán thành công cho hóa đơn " + invoiceId +
                            "\nĐã in hóa đơn: " + invoiceId +
                            "\ntại cửa hàng: " + store.getId() + " - " + store.getName() +
                            "\n\nHóa đơn đã được lưu vào: reports/" + fileName + ".pdf"
            );
            if (invoiceFile != null) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.open(invoiceFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            onRefreshCart(true);
        }
    }

    private void onBankPayment() {
        if (!pointsLocked) {
            int confirm = DialogFactory.showConfirmOkCancelDialog(this, "Xác nhận thanh toán", "Bạn chưa sử dụng điểm. Tiếp tục thanh toán?");
            if (confirm != JOptionPane.OK_OPTION) return;
        }
        int confirm = DialogFactory.showConfirmOkCancelDialog(this, "Xác nhận thanh toán", "Xác nhận thanh toán bằng ngân hàng?");
        if (confirm != JOptionPane.OK_OPTION) return;
        java.math.BigDecimal totalDue = getPanelGrandTotal();
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thanh toán ngân hàng", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTotal = new JLabel("Số tiền: " + totalDue.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND");
        dialog.add(lblTotal, gbc);
        gbc.gridy++;
        JLabel lblInstr = new JLabel("Vui lòng đặt thẻ ngân hàng hoặc thiết bị vào đầu đọc.");
        dialog.add(lblInstr, gbc);
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton cancelBtn = new JButton("Hủy thanh toán");
        btnPanel.add(cancelBtn);
        dialog.add(btnPanel, gbc);
        cancelBtn.addActionListener(ev -> dialog.dispose());
        // Key listener for Ctrl+Shift+Alt+P
        dialog.addKeyListener(new java.awt.event.KeyAdapter() {
            boolean paid = false;

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (!paid && e.isControlDown() && e.isShiftDown() && e.isAltDown() && e.getKeyCode() == java.awt.event.KeyEvent.VK_P) {
                    paid = true;
                    dialog.dispose();
                    String message = "Đã thanh toán thành công!\nSố tiền: " + totalDue.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + " VND bằng phương thức ngân hàng";
                    showSuccessDialog(message, () -> processPayment("P003"));
                }
            }
        });
        dialog.setFocusable(true);
        dialog.setFocusTraversalKeysEnabled(false);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // --- UI HELPERS ---
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        if (font != null) label.setFont(font);
        return label;
    }

    private void addLabelValue(GridBagConstraints gbc, JPanel panel, String label, JLabel valueLabel, Font font) {
        JLabel lbl = createLabel(label, font);
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(valueLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void showSuccessDialog(String message, Runnable onClose) {
        JDialog infoDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), TITLE_SUCCESS, true);
        infoDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        JLabel success = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        success.setForeground(SUCCESS_COLOR);
        success.setFont(success.getFont().deriveFont(Font.BOLD));
        infoDialog.add(success, gbc);
        infoDialog.pack();
        infoDialog.setLocationRelativeTo(this);
        new javax.swing.Timer(3000, ev -> {
            infoDialog.dispose();
            if (onClose != null) onClose.run();
        }) {{
            setRepeats(false);
        }}.start();
        infoDialog.setVisible(true);
    }

    private static class CartItem {
        Product product;
        int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }
}