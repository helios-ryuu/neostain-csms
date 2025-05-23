package com.neostain.csms.service;

import com.neostain.csms.dao.*;
import com.neostain.csms.model.*;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class SaleServiceImpl implements SaleService {
    private final InvoiceDAO invoiceDAO;
    private final InvoiceDetailDAO invoiceDetailDAO;
    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    private final PromotionDAO promotionDAO;
    private final PointUpdateLogDAO pointUpdateLogDAO;
    private final PaymentDAO paymentDAO;
    private final InventoryTransactionDAO inventoryTransactionDAO;
    private final InventoryDAO inventoryDAO;

    public SaleServiceImpl(InvoiceDAO invoiceDAO, InvoiceDetailDAO invoiceDetailDAO, ProductDAO productDAO, CategoryDAO categoryDAO, PromotionDAO promotionDAO, PointUpdateLogDAO pointUpdateLogDAO, PaymentDAO paymentDAO, InventoryTransactionDAO inventoryTransactionDAO, InventoryDAO inventoryDAO) {
        this.invoiceDAO = invoiceDAO;
        this.invoiceDetailDAO = invoiceDetailDAO;
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
        this.promotionDAO = promotionDAO;
        this.pointUpdateLogDAO = pointUpdateLogDAO;
        this.paymentDAO = paymentDAO;
        this.inventoryTransactionDAO = inventoryTransactionDAO;
        this.inventoryDAO = inventoryDAO;
    }

    // Invoice
    @Override
    public Invoice getInvoiceById(String id) {
        return invoiceDAO.findById(id);
    }

    @Override
    public List<Invoice> getInvoicesByStatus(String status) {
        return invoiceDAO.findByStatus(status);
    }

    @Override
    public List<Invoice> getInvoicesByMemberId(String memberId) {
        return invoiceDAO.findByMemberId(memberId);
    }

    @Override
    public List<Invoice> getInvoicesByStoreId(String storeId) {
        return invoiceDAO.findByStoreId(storeId);
    }

    @Override
    public List<Invoice> searchInvoices(
            String id, String customerId, String employeeId,
            String status, String paymentMethod,
            String from, String to
    ) {
        // chuyển null/empty về null để DAO dễ xử lý
        if (id != null && id.isBlank()) id = null;
        if (customerId != null && customerId.isBlank()) customerId = null;
        if (employeeId != null && employeeId.isBlank()) employeeId = null;
        if (status != null && status.isBlank()) status = null;
        if (paymentMethod != null && paymentMethod.isBlank()) paymentMethod = null;
        if (from != null && from.isBlank()) from = null;
        if (to != null && to.isBlank()) to = null;

        return invoiceDAO.search(id, customerId, employeeId, status, paymentMethod, from, to);
    }

    @Override
    public String createInvoice(String storeId, String memberId, String paymentId, String employeeId, int pointUsed) {
        return invoiceDAO.create(storeId, memberId, paymentId, employeeId, pointUsed);
    }

    @Override
    public boolean addItemToInvoice(String invoiceId, String productId, int quantity) {
        return invoiceDAO.addItem(invoiceId, productId, quantity);
    }

    @Override
    public boolean addGiftToInvoice(String invoiceId, String productId, int quantity) {
        return invoiceDAO.addGift(invoiceId, productId, quantity);
    }

    @Override
    public boolean calculateInvoiceTotal(String invoiceId) {
        return invoiceDAO.calculateTotal(invoiceId);
    }

    @Override
    public boolean cancelInvoice(String invoiceId) {
        return invoiceDAO.cancel(invoiceId);
    }

    @Override
    public List<InvoiceDetail> getInvoiceDetailsByInvoiceId(String invoiceId) {
        return invoiceDetailDAO.findByInvoiceId(invoiceId);
    }

    // Product
    @Override
    public Product getProductById(String id) {
        return productDAO.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryId(String categoryId) {
        return productDAO.findByCategoryId(categoryId);
    }

    @Override
    public boolean createProduct(Product product) {
        return productDAO.create(product);
    }

    @Override
    public boolean updateProduct(Product product) {
        return productDAO.updateName(product.getId(), product.getName());
    }

    @Override
    public boolean deleteProduct(String id) {
        return productDAO.delete(id);
    }

    // Category
    @Override
    public Category getCategoryById(String id) {
        return categoryDAO.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    @Override
    public boolean createCategory(Category category) throws DuplicateFieldException {
        return categoryDAO.create(category);
    }

    @Override
    public boolean updateCategory(Category category) {
        return categoryDAO.updateName(category.getId(), category.getName());
    }

    @Override
    public boolean deleteCategory(String id) {
        return categoryDAO.delete(id);
    }

    // Promotion
    @Override
    public Promotion getPromotionById(String id) {
        return promotionDAO.findById(id);
    }

    @Override
    public List<Promotion> getActivePromotions() {
        Timestamp now = Timestamp.from(Instant.now());
        return promotionDAO.findAll().stream()
                .filter(p -> p.getStartTime().before(now) && p.getEndTime().after(now))
                .collect(Collectors.toList());
    }

    @Override
    public boolean createPromotion(Promotion promotion) {
        return promotionDAO.create(promotion);
    }

    @Override
    public boolean updatePromotion(Promotion promotion) {
        return promotionDAO.updateName(promotion.getId(), promotion.getName());
    }

    @Override
    public boolean deletePromotion(String id) {
        return promotionDAO.delete(id);
    }

    // Point
    @Override
    public List<PointUpdateLog> getPointLogsByMember(String memberId) {
        return pointUpdateLogDAO.findByMemberId(memberId);
    }

    @Override
    public Payment getPaymentById(String id) {
        return paymentDAO.findById(id);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentDAO.findAll();
    }

    @Override
    public InventoryTransaction getInventoryTransactionById(String id) {
        return inventoryTransactionDAO.findById(id);
    }

    @Override
    public List<InventoryTransaction> getInventoryTransactionsByProductId(String productId) {
        return inventoryTransactionDAO.findByProductId(productId);
    }

    @Override
    public List<InventoryTransaction> getInventoryTransactionsByStoreId(String storeId) {
        return inventoryTransactionDAO.findByStoreId(storeId);
    }

    @Override
    public List<InventoryTransaction> getAllInventoryTransactions() {
        return inventoryTransactionDAO.findAll();
    }

    @Override
    public Inventory getInventoryById(String id) {
        return inventoryDAO.findById(id);
    }

    @Override
    public List<Inventory> getInventoriesByProductId(String productId) {
        return inventoryDAO.findByProductId(productId);
    }

    @Override
    public List<Inventory> getInventoriesByStoreId(String storeId) {
        return inventoryDAO.findByStoreId(storeId);
    }

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryDAO.findAll();
    }
} 