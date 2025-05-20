package com.neostain.csms.dao;

import com.neostain.csms.model.Employee;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.util.exception.FieldValidationException;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeDAO {
    Employee findById(String id);

    List<Employee> findByManagerId(String id);

    List<Employee> findByName(String name);

    List<Employee> findByStatus(String status);

    List<Employee> findAll();

    boolean create(Employee employee) throws DuplicateFieldException, FieldValidationException;

    boolean updateManagerId(String id, String managerId) throws FieldValidationException;

    boolean updateName(String id, String name);

    boolean updateEmail(String id, String email) throws FieldValidationException, DuplicateFieldException;

    boolean updatePhoneNumber(String id, String phoneNumber) throws FieldValidationException, DuplicateFieldException;

    boolean updateAddress(String id, String address);

    boolean updateHourlyWage(String id, BigDecimal hourlyWage);

    boolean updateStatus(String id, String status);

    List<Employee> search(String id, String managerId, String from, String to, String email, String phoneNumber, String status);
}
