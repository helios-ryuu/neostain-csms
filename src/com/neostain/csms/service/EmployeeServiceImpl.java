package com.neostain.csms.service;

import com.neostain.csms.dao.EmployeeDAO;
import com.neostain.csms.model.Employee;
import com.neostain.csms.util.StringUtils;

import java.util.logging.Logger;

public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class.getName());
    private final EmployeeDAO dao;

    public EmployeeServiceImpl(EmployeeDAO dao) {
        this.dao = dao;
    }

    @Override
    public Employee getEmployeeById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[GET_EMPLOYEE_BY_ID] ID nhân viên trống");
            return null;
        }

        try {
            return dao.findById(id);
        } catch (Exception e) {
            LOGGER.warning("[GET_EMPLOYEE_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean add(Employee emp) {
        try {
            dao.create(emp);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[ADD] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[REMOVE_BY_ID] ID nhân viên trống");
            return false;
        }
        try {
            Employee emp = dao.findById(id);
            if (emp == null) {
                LOGGER.warning("[REMOVE_BY_ID] Nhân viên không tồn tại: " + id);
                return false;
            }
            if ("Nhân viên quản trị cơ sở dữ liệu".equalsIgnoreCase(emp.getPosition())) {
                LOGGER.warning("[REMOVE_BY_ID] Không được xóa nhân viên quản trị cơ sở dữ liệu");
                return false;
            }
            dao.delete(id);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[REMOVE_BY_ID] Lỗi: " + e.getMessage());
            return false;
        }
    }
}
