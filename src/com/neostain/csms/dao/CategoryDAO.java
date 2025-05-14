package com.neostain.csms.dao;

import com.neostain.csms.model.Category;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.util.List;

public interface CategoryDAO {
    Category findById(String id);

    Category findByName(String name);

    List<Category> findAll();

    boolean create(Category category) throws DuplicateFieldException;

    boolean updateName(String id, String name);

    boolean delete(String id);
}
