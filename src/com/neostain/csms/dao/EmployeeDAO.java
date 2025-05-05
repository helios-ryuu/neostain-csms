package com.neostain.csms.dao;

import com.neostain.csms.model.Employee;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeDAO {
    Employee findById(String id);

    List<Employee> findByName(String name);

    List<Employee> findByPosition(String position);

    List<Employee> findByStatus(String status);

    List<Employee> findAll();

    boolean create(Employee employee);

    boolean updateName(String id, String name);

    boolean updatePosition(String id, String position);

    boolean updateEmail(String id, String email);

    boolean updatePhoneNumber(String id, String phoneNumber);

    boolean updateAddress(String id, String address);

    boolean updateHourlyWage(String id, BigDecimal hourlyWage);

    boolean updateStatus(String id, String status);

    boolean delete(String id);
}
