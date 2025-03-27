package com.neostain.csms.service.api;

import com.neostain.csms.model.Employee;

public interface EmployeeService {

    /// Lấy dữ liệu Employee bằng EMPLOYEE_ID từ bảng EMPLOYEE
    ///
    /// @param employeeID EMPLOYEE_ID hiện tại
    /// @return Một thể hiện đối tượng Employee
    Employee getEmployee(String employeeID);
}
