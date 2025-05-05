package com.neostain.csms.dao;

import com.neostain.csms.model.Product;

import java.util.List;

public interface ProductDAO {
    Product findById(String id);

    List<Product> findByName(String name);

    List<Product> findByCategoryId(String categoryID);

    List<Product> findAll();

    boolean create(Product product);

    boolean updateName(String id, String name);

    boolean updateUInitPrice(String id, int unitPrice);

    boolean updateCategoryId(String id, String categoryId);

    boolean delete(String id);

}