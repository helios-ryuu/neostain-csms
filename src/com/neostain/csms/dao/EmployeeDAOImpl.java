package com.neostain.csms.dao;

import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Thực thi EmployeeDAO để quản lý thông tin nhân viên
 * Sử dụng các câu truy vấn SQL từ SQLQueries để dễ bảo trì
 */
public class EmployeeDAOImpl implements EmployeeDAO {
    private static final Logger LOGGER = Logger.getLogger(EmployeeDAOImpl.class.getName());
    private final Connection conn;

    public EmployeeDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Employee findById(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                } else {
                    LOGGER.warning("[FIND_BY_ID] Nhân viên không tồn tại: " + id);
                    return null;
                }
            }
        }
    }

    @Override
    public int create(Employee emp) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_CREATE)) {
            ps.setString(1, emp.getEmployeeId());
            ps.setString(2, emp.getEmployeeName());
            ps.setString(3, emp.getPosition());
            ps.setString(4, emp.getDepartmentId());
            ps.setDate(5, emp.getHireDate());
            ps.setString(6, emp.getEmail());
            ps.setString(7, emp.getPhoneNumber());
            ps.setString(8, emp.getAddress());
            ps.setBigDecimal(9, emp.getHourlyWage());
            ps.setBigDecimal(10, emp.getBaseSalary());
            ps.setString(11, emp.getEmployeeStatusId());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        }
    }

    /**
     * Ánh xạ ResultSet sang đối tượng Employee
     * Tách biệt logic đọc dữ liệu và tạo đối tượng để dễ bảo trì
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getString("EMPLOYEE_ID"),
                rs.getString("EMPLOYEE_NAME"),
                rs.getString("POSITION"),
                rs.getString("DEPARTMENT_ID"),
                rs.getDate("HIRE_DATE"),
                rs.getString("EMAIL"),
                rs.getString("PHONE_NUMBER"),
                rs.getString("ADDRESS"),
                rs.getBigDecimal("HOURLY_WAGE"),
                rs.getBigDecimal("BASE_SALARY"),
                rs.getString("EMPLOYEE_STATUS_ID")
        );
    }
}
