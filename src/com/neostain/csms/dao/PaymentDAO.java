package com.neostain.csms.dao;

import com.neostain.csms.model.Payment;

public interface PaymentDAO {
    Payment findById(String id);

    Payment findByName(String name);

    Payment findAll();

    boolean create(Payment payment);

    boolean updateName(String id, String name);

    boolean delete(String id);
}
