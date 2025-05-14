package com.neostain.csms.service;

import com.neostain.csms.dao.EmployeeDAO;
import com.neostain.csms.dao.MemberDAO;
import com.neostain.csms.dao.StoreDAO;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Member;
import com.neostain.csms.model.Store;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.util.exception.FieldValidationException;

import java.util.List;

public class ManagementServiceImpl implements ManagementService {
    private final EmployeeDAO employeeDAO;
    private final MemberDAO memberDAO;
    private final StoreDAO storeDAO;

    public ManagementServiceImpl(EmployeeDAO employeeDAO, MemberDAO memberDAO, StoreDAO storeDAO) {
        this.employeeDAO = employeeDAO;
        this.memberDAO = memberDAO;
        this.storeDAO = storeDAO;
    }

    // Employee
    @Override
    public Employee getEmployeeById(String id) {
        return employeeDAO.findById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    @Override
    public boolean createEmployee(Employee employee) throws DuplicateFieldException {
        return employeeDAO.create(employee);
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        return employeeDAO.updateName(employee.getId(), employee.getName());
    }

    @Override
    public boolean deleteEmployee(String id) {
        return employeeDAO.delete(id);
    }

    // Member
    @Override
    public Member getMemberById(String id) {
        return memberDAO.findById(id);
    }

    @Override
    public Member getMemberByPhoneNumber(String phoneNumber) {
        return memberDAO.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<Member> searchMembers(
            String memberId, String phone, String email,
            String dateFrom, String dateTo) {
        // chuyển null/empty về null để DAO dễ xử lý
        if (memberId != null && memberId.isBlank()) memberId = null;
        if (phone != null && phone.isBlank()) phone = null;
        if (email != null && email.isBlank()) email = null;
        if (dateFrom != null && dateFrom.isBlank()) dateFrom = null;
        if (dateTo != null && dateTo.isBlank()) dateTo = null;

        return memberDAO.search(memberId, phone, email, dateFrom, dateTo);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    @Override
    public boolean createMember(Member member) throws DuplicateFieldException, FieldValidationException {
        return memberDAO.create(member);
    }

    @Override
    public boolean updateMember(Member member) throws DuplicateFieldException, FieldValidationException {
        memberDAO.updateName(member.getId(), member.getName());
        memberDAO.updatePhoneNumber(member.getId(), member.getPhoneNumber());
        memberDAO.updateEmail(member.getId(), member.getEmail());

        return memberDAO.updateName(member.getId(), member.getName());
    }

    @Override
    public boolean deleteMember(String id) {
        return memberDAO.delete(id);
    }

    // Store
    @Override
    public Store getStoreById(String id) {
        return storeDAO.findById(id);
    }

    @Override
    public Store getStoreByManagerId(String id) {
        return storeDAO.findByManagerId(id);
    }

    @Override
    public List<Store> getAllStores() {
        return storeDAO.findAll();
    }

    @Override
    public boolean createStore(Store store) {
        return storeDAO.create(store);
    }

    @Override
    public boolean updateStore(Store store) {
        return storeDAO.updateStoreName(store.getId(), store.getName());
    }
} 