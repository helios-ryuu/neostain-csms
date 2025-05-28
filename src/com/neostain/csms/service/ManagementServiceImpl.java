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
    public List<Employee> getEmployeeByManagerId(String id) {
        return employeeDAO.findByManagerId(id);
    }

    @Override
    public boolean createEmployee(Employee employee) throws DuplicateFieldException, FieldValidationException {
        return employeeDAO.create(employee);
    }

    @Override
    public void updateEmployee(Employee employee) throws FieldValidationException, DuplicateFieldException {
        employeeDAO.updateManagerId(employee.getId(), employee.getManagerId());
        employeeDAO.updateName(employee.getId(), employee.getName());
        employeeDAO.updateEmail(employee.getId(), employee.getEmail());
        employeeDAO.updatePhoneNumber(employee.getId(), employee.getPhoneNumber());
        employeeDAO.updateAddress(employee.getId(), employee.getAddress());
        employeeDAO.updateHourlyWage(employee.getId(), employee.getHourlyWage());
        employeeDAO.updateStatus(employee.getId(), employee.getStatus());
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
    public void createMember(String name, String phone, String email) throws DuplicateFieldException, FieldValidationException {
        memberDAO.create(name, phone, email);
    }

    @Override
    public void updateMember(Member member) throws DuplicateFieldException, FieldValidationException {
        memberDAO.updateName(member.getId(), member.getName());
        memberDAO.updatePhoneNumber(member.getId(), member.getPhoneNumber());
        memberDAO.updateEmail(member.getId(), member.getEmail());
    }

    @Override
    public void deleteMember(String id) {
        memberDAO.delete(id);
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
    public void updateStore(Store store) {
        storeDAO.updateStoreName(store.getId(), store.getName());
        storeDAO.updateStoreAddress(store.getId(), store.getStoreAddress());
    }

    @Override
    public List<Employee> searchEmployees(String id, String managerId, String from, String to, String email, String phoneNumber, String status) {
        return employeeDAO.search(id, managerId, from, to, email, phoneNumber, status);
    }
} 