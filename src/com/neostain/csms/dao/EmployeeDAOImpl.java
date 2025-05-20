package com.neostain.csms.dao;

import com.neostain.csms.model.Employee;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.util.exception.FieldValidationException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public List<Employee> findByManagerId(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] ID nhân viên quản lý trống");
            return null;
        }

        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_FIND_BY_MANAGER_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
                return employees;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_MANAGER_ID] Lỗi: " + e.getMessage());
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
    public boolean create(Employee emp) throws DuplicateFieldException, FieldValidationException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_CREATE)) {
            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getPhoneNumber());
            ps.setString(4, emp.getAddress());
            ps.setBigDecimal(5, emp.getHourlyWage());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            int code = e.getErrorCode();                 // ví dụ: -20020, -20021, 1…
            String msg = e.getMessage().toUpperCase();   // để so sánh dễ hơn

            // 1) Lỗi từ trigger: format email/phone
            if (code == -20020) {
                throw new FieldValidationException(
                        "email",
                        "Email không hợp lệ. Ví dụ hợp lệ: user@example.com"
                );
            }
            if (code == -20021) {
                throw new FieldValidationException(
                        "phoneNumber",
                        "Số điện thoại phải gồm đúng 10 chữ số, không chứa ký tự khác."
                );
            }
            if (code == 1) {
                if (msg.contains("UK_MEMBER_PHONE_NUMBER")) {
                    throw new DuplicateFieldException("phoneNumber", "Số điện thoại đã tồn tại.");
                }
                if (msg.contains("UK_MEMBER_EMAIL")) {
                    throw new DuplicateFieldException("email", "Email đã tồn tại.");
                }
            }
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
    public boolean updateEmail(String id, String email) throws FieldValidationException, DuplicateFieldException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_UPDATE_EMAIL)) {
            ps.setString(1, email);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            int code = e.getErrorCode();                 // ví dụ: -20020, -20021, 1…
            String msg = e.getMessage().toUpperCase();   // để so sánh dễ hơn

            // 1) Lỗi từ trigger: format email/phone
            if (code == -20020) {
                throw new FieldValidationException(
                        "email",
                        "Email không hợp lệ. Ví dụ hợp lệ: user@example.com"
                );
            }
            if (code == 1) {
                if (msg.contains("UK_MEMBER_EMAIL")) {
                    throw new DuplicateFieldException("email", "Email đã tồn tại.");
                }
            }
            LOGGER.severe("[UPDATE_EMAIL] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePhoneNumber(String id, String phoneNumber) throws FieldValidationException, DuplicateFieldException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.EMPLOYEE_UPDATE_PHONE_NUMBER)) {
            ps.setString(1, phoneNumber);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            int code = e.getErrorCode();                 // ví dụ: -20020, -20021, 1…
            String msg = e.getMessage().toUpperCase();   // để so sánh dễ hơn

            // 1) Lỗi từ trigger: format email/phone
            if (code == -20021) {
                throw new FieldValidationException(
                        "phoneNumber",
                        "Số điện thoại phải gồm đúng 10 chữ số, không chứa ký tự khác."
                );
            }
            if (code == 1) {
                if (msg.contains("UK_MEMBER_PHONE_NUMBER")) {
                    throw new DuplicateFieldException("phoneNumber", "Số điện thoại đã tồn tại.");
                }
            }
            LOGGER.severe("[CREATE] Lỗi: " + e.getMessage());
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
    public boolean updateManagerId(String id, String managerId) throws FieldValidationException {
        try (CallableStatement cs = conn.prepareCall(SQLQueries.EMPLOYEE_CHANGE_MANAGER_ID)) {
            cs.setString(1, id);
            cs.setString(2, managerId);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            int code = e.getErrorCode();                 // ví dụ: -20020, -20021,

            if (code == 20051) {
                throw new FieldValidationException(
                        "managerId",
                        "Mã nhân viên quản lý này không phải là Nhân viên quản lý cửa hàng."
                );
            }
            if (code == 20053) {
                throw new FieldValidationException(
                        "managerId",
                        "Mã nhân viên quản lý không tồn tại"
                );
            } else {
                throw new FieldValidationException(
                        "managerId",
                        "Mã nhân viên quản lý không hợp lệ"
                );
            }
        } catch (Exception e) {
            LOGGER.severe("[UPDATE_MANAGER_ID] Lỗi: " + e.getMessage());
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
    public List<Employee> search(String id, String managerId, String from, String to, String email, String phoneNumber, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM EMPLOYEE WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (!StringUtils.isNullOrEmpty(id)) {
            sql.append(" AND ID=?");
            params.add(id);
        }
        if (!StringUtils.isNullOrEmpty(managerId)) {
            sql.append(" AND MANAGER_ID=?");
            params.add(managerId);
        }
        if (from != null) {
            sql.append(" AND HIRE_DATE >= ?");
            params.add(Date.valueOf(from));
        }
        if (to != null) {
            sql.append(" AND HIRE_DATE <= ?");
            params.add(Date.valueOf(to));
        }
        if (!StringUtils.isNullOrEmpty(email)) {
            sql.append(" AND EMAIL LIKE ?");
        }
        if (!StringUtils.isNullOrEmpty(phoneNumber)) {
            sql.append(" AND PHONE_NUMBER LIKE ?");
        }
        if (!Objects.equals(status, "TẤT CẢ TRẠNG THÁI")) {
            sql.append(" AND STATUS=?");
            params.add(status);
        }
        sql.append("ORDER BY HIRE_DATE DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<Employee> employees = new ArrayList<>();
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
            return employees;
        } catch (SQLException e) {
            LOGGER.severe("[SEARCH] Lỗi: " + e.getMessage());
            return List.of();
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getString("ID"),
                rs.getString("MANAGER_ID"),
                rs.getString("NAME"),
                rs.getTimestamp("HIRE_DATE"),
                rs.getString("EMAIL"),
                rs.getString("PHONE_NUMBER"),
                rs.getString("ADDRESS"),
                rs.getBigDecimal("HOURLY_WAGE"),
                rs.getString("STATUS")
        );
    }
}
