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
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    @Override
    public boolean createEmployee(Employee employee) throws DuplicateFieldException, FieldValidationException {
        return employeeDAO.create(employee);
    }

    @Override
    public boolean updateEmployee(Employee employee) throws FieldValidationException, DuplicateFieldException {
        boolean z = employeeDAO.updateManagerId(employee.getId(), employee.getManagerId());
        boolean a = employeeDAO.updateName(employee.getId(), employee.getName());
        boolean b = employeeDAO.updateEmail(employee.getId(), employee.getEmail());
        boolean c = employeeDAO.updatePhoneNumber(employee.getId(), employee.getPhoneNumber());
        boolean d = employeeDAO.updateAddress(employee.getId(), employee.getAddress());
        boolean e = employeeDAO.updateHourlyWage(employee.getId(), employee.getHourlyWage());
        boolean f = employeeDAO.updateStatus(employee.getId(), employee.getStatus());

        return z && a && b && c && d && e && f;
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
    public boolean createMember(String name, String phone, String email) throws DuplicateFieldException, FieldValidationException {
        return memberDAO.create(name, phone, email);
    }

    @Override
    public boolean updateMember(Member member) throws DuplicateFieldException, FieldValidationException {
        boolean a = memberDAO.updateName(member.getId(), member.getName());
        boolean b = memberDAO.updatePhoneNumber(member.getId(), member.getPhoneNumber());
        boolean c = memberDAO.updateEmail(member.getId(), member.getEmail());

        return a && b && c;
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

    @Override
    public List<Employee> searchEmployees(String id, String managerId, String from, String to, String email, String phoneNumber, String status) {
        if (id != null && id.isBlank()) id = null;
        if (managerId != null && managerId.isBlank()) managerId = null;
        if (from != null && from.isBlank()) from = null;
        if (to != null && to.isBlank()) to = null;
        if (email != null && email.isBlank()) email = null;
        if (phoneNumber != null && phoneNumber.isBlank()) phoneNumber = null;
        if (status != null && status.isBlank()) status = null;

        return employeeDAO.search(id, managerId, from, to, email, phoneNumber, status);
    }
} 