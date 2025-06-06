package com.neostain.csms.service;

import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Member;
import com.neostain.csms.model.Store;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.util.exception.FieldValidationException;

import java.util.List;

public interface ManagementService {
    // Employee
    Employee getEmployeeById(String id);

    List<Employee> getEmployeeByManagerId(String id);

    boolean createEmployee(Employee employee) throws DuplicateFieldException, FieldValidationException;

    void updateEmployee(Employee employee) throws FieldValidationException, DuplicateFieldException;

    // Member
    Member getMemberById(String id);

    Member getMemberByPhoneNumber(String phoneNumber);

    List<Member> searchMembers(
            String memberId,
            String phone,
            String email,
            String dateFrom,
            String dateTo
    );

    List<Member> getAllMembers();

    void createMember(String name, String phone, String email) throws DuplicateFieldException, FieldValidationException;

    void updateMember(Member member) throws DuplicateFieldException, FieldValidationException;

    void deleteMember(String id);

    // Store
    Store getStoreById(String id);

    Store getStoreByManagerId(String id);

    List<Store> getAllStores();

    boolean createStore(Store store);

    void updateStore(Store store);

    List<Employee> searchEmployees(String id, String managerId, String from, String to, String email, String phoneNumber, String status);
}
