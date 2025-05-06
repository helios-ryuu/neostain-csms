package com.neostain.csms.dao;

import com.neostain.csms.model.Employee;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public Employee findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] ID nhân viên trống");
            return null;
        }

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
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Employee> findByName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            LOGGER.warning("[FIND_BY_NAME] Tên nhân viên trống");
            return null;
        }

        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_FIND_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
                return employees;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_NAME] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Employee> findByPosition(String position) {
        if (StringUtils.isNullOrEmpty(position)) {
            LOGGER.warning("[FIND_BY_POSITION] Vị trí nhân viên trống");
            return null;
        }

        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_FIND_BY_POSITION)) {
            ps.setString(1, position);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
                return employees;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_POSITION] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Employee> findByStatus(String status) {
        if (StringUtils.isNullOrEmpty(status)) {
            LOGGER.warning("[FIND_BY_STATUS] Trạng thái nhân viên trống");
            return null;
        }

        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_FIND_BY_STATUS)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
                return employees;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_STATUS] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
                return employees;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean create(Employee emp) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_CREATE)) {
            ps.setString(1, emp.getEmployeeName());
            ps.setString(2, emp.getPosition());
            ps.setString(3, emp.getEmail());
            ps.setString(4, emp.getPhoneNumber());
            ps.setString(5, emp.getAddress());
            ps.setBigDecimal(6, emp.getHourlyWage());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateName(String id, String name) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_UPDATE_NAME)) {
            ps.setString(1, name);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_NAME] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePosition(String id, String position) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_UPDATE_POSITION)) {
            ps.setString(1, position);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_POSITION] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEmail(String id, String email) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_UPDATE_EMAIL)) {
            ps.setString(1, email);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_EMAIL] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePhoneNumber(String id, String phoneNumber) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_UPDATE_PHONE_NUMBER)) {
            ps.setString(1, phoneNumber);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_PHONE_NUMBER] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateAddress(String id, String address) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_UPDATE_ADDRESS)) {
            ps.setString(1, address);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_ADDRESS] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateHourlyWage(String id, BigDecimal hourlyWage) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_UPDATE_HOURLY_WAGE)) {
            ps.setBigDecimal(1, hourlyWage);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_HOURLY_WAGE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStatus(String id, String status) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_UPDATE_STATUS)) {
            ps.setString(1, status);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_STATUS] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getString("EMPLOYEE_ID"),
                rs.getString("EMPLOYEE_NAME"),
                rs.getString("POSITION"),
                rs.getTimestamp("HIRE_DATE"),
                rs.getString("EMAIL"),
                rs.getString("PHONE_NUMBER"),
                rs.getString("ADDRESS"),
                rs.getBigDecimal("HOURLY_WAGE"),
                rs.getString("EMPLOYEE_STATUS")
        );
    }
}
