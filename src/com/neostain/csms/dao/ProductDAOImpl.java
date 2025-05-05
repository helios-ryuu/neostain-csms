package com.neostain.csms.dao;

import com.neostain.csms.model.Product;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

public class ProductDAOImpl implements ProductDAO {
    private static final Logger LOGGER = Logger.getLogger(ProductDAOImpl.class.getName());
    private final Connection conn;

    public ProductDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Product findById(String id) {
        return null;
    }

    @Override
    public List<Product> findByName(String name) {
        return List.of();
    }

    @Override
    public List<Product> findByCategoryId(String categoryID) {
        return List.of();
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public boolean create(Product product) {
        return false;
    }

    @Override
    public boolean updateName(String id, String name) {
        return false;
    }

    @Override
    public boolean updateUInitPrice(String id, int unitPrice) {
        return false;
    }

    @Override
    public boolean updateCategoryId(String id, String categoryId) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
