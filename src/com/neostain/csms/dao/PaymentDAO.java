package com.neostain.csms.dao;

import com.neostain.csms.model.Payment;

import java.util.List;

public interface PaymentDAO {
    Payment findById(String id);

    Payment findByName(String name);

    List<Payment> findAll();
}
