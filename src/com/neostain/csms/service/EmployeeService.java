package com.neostain.csms.service;

import com.neostain.csms.model.Employee;

public interface EmployeeService {
    Employee getEmployeeById(String id);

    boolean add(Employee emp);

    boolean removeById(String id);
}
