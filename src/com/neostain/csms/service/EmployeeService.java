package com.neostain.csms.service;

import com.neostain.csms.model.Employee;

public interface EmployeeService {
    Employee getById(String id);

    boolean add(Employee emp);

    boolean remove(String id);
}
