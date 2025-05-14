package com.neostain.csms.service;

import com.neostain.csms.model.*;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.util.List;

public interface SaleService {
    // Invoice
    Invoice getInvoiceById(String id);

    List<Invoice> getInvoicesByStatus(String status);

    List<Invoice> getInvoicesByMemberId(String memberId);

    List<Invoice> getInvoicesByStoreId(String storeId);

    List<Invoice> searchInvoices(
            String id, String customerId, String employeeId,
            String status, String paymentMethod,
            String from, String to
    );

    String createInvoice(String storeId, String memberId, String paymentId, String employeeId, int pointUsed);

    boolean addItemToInvoice(String invoiceId, String productId, int quantity);

    boolean calculateInvoiceTotal(String invoiceId);

    boolean cancelInvoice(String invoiceId);

    // Invoice Detail
    List<InvoiceDetail> getInvoiceDetailsByInvoiceId(String invoiceId);

    // Product
    Product getProductById(String id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategoryId(String categoryId);

    boolean createProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProduct(String id);

    // Category
    Category getCategoryById(String id);

    List<Category> getAllCategories();

    boolean createCategory(Category category) throws DuplicateFieldException;

    boolean updateCategory(Category category);

    boolean deleteCategory(String id);

    // Promotion
    Promotion getPromotionById(String id);

    List<Promotion> getActivePromotions();

    boolean createPromotion(Promotion promotion);

    boolean updatePromotion(Promotion promotion);

    boolean deletePromotion(String id);

    // Point
    List<PointUpdateLog> getPointLogsByMember(String memberId);

    // Payment
    Payment getPaymentById(String id);

    List<Payment> getAllPayments();
}