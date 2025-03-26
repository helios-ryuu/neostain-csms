package com.neostain.csms.service.impl;

import com.neostain.csms.model.Employee;
import com.neostain.csms.service.api.EmployeeService;
import com.neostain.csms.util.DatabaseUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class CSMSEmployeeService implements EmployeeService {
    private static final Logger LOGGER = Logger.getLogger(CSMSEmployeeService.class.getName());

    @Override
    public Employee getEmployee(String employeeID) {
        if (employeeID == null || employeeID.trim().isEmpty()) {
            LOGGER.warning("[EMPLOYEE.SERVICE.GET_EMPLOYEE] Tên đăng nhập trống");
        }
        String query = "SELECT * FROM EMPLOYEE e WHERE e.EMPLOYEE_ID = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    String EMPLOYEE_ID = rs.getString("EMPLOYEE_ID");
                    String EMPLOYEE_NAME = rs.getString("EMPLOYEE_NAME");
                    String POSITION = rs.getString("POSITION");
                    String DEPARTMENT_ID = rs.getString("DEPARTMENT_ID");
                    Date HIRE_DATE = rs.getDate("HIRE_DATE");
                    String EMAIL = rs.getString("EMAIL");
                    String PHONE_NUMBER = rs.getString("PHONE_NUMBER");
                    String ADDRESS = rs.getString("ADDRESS");
                    BigDecimal HOURLY_WAGE = rs.getBigDecimal("HOURLY_WAGE");
                    BigDecimal BASE_SALARY = rs.getBigDecimal("BASE_SALARY");
                    return new Employee(EMPLOYEE_ID, EMPLOYEE_NAME, POSITION, DEPARTMENT_ID, HIRE_DATE, EMAIL, PHONE_NUMBER, ADDRESS, HOURLY_WAGE, BASE_SALARY);
                } else {
                    LOGGER.severe("[EMPLOYEE.SERVICE.GET_EMPLOYEE] Nhân viên không tồn tại: " + employeeID);
                    return null;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("[EMPLOYEE.SERVICE.GET_EMPLOYEE] lỗi: " + e.getMessage());
            return null;
        }
    }
}
