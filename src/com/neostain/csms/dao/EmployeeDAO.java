package com.neostain.csms.dao;

import com.neostain.csms.model.Employee;

import java.sql.SQLException;

public interface EmployeeDAO {
    /**
     * Tìm nhân viên theo ID
     *
     * @param id ID của nhân viên
     * @return Đối tượng Employee hoặc null nếu không tìm thấy
     * @throws SQLException Nếu có lỗi truy vấn
     */
    Employee findById(String id) throws SQLException;

    /**
     * Tạo mới nhân viên
     *
     * @param employee Nhân viên cần tạo
     * @return Số bản ghi đã thêm (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int create(Employee employee) throws SQLException;

    /**
     * Xóa nhân viên
     *
     * @param id ID của nhân viên
     * @return Số bản ghi đã xóa (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int delete(String id) throws SQLException;
}
