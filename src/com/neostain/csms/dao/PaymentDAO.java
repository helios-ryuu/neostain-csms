package com.neostain.csms.dao;

import com.neostain.csms.model.Payment;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.util.List;

public interface PaymentDAO {
    Payment findById(String id);

    Payment findByName(String name);

    List<Payment> findAll();

    boolean create(Payment payment) throws DuplicateFieldException;

    boolean updateName(String id, String name) throws DuplicateFieldException;

    boolean delete(String id);
}
