package com.neostain.csms.dao;

import com.neostain.csms.model.Role;

import java.sql.SQLException;

public interface RoleDAO {
    /**
     * Tìm vai trò theo ID
     *
     * @param id ID của vai trò
     * @return Đối tượng Role hoặc null nếu không tìm thấy
     * @throws SQLException Nếu có lỗi truy vấn
     */
    Role findById(String id) throws SQLException;

    /**
     * Tạo mới vai trò
     *
     * @param role Vai trò cần tạo
     * @return Số bản ghi đã thêm (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int create(Role role) throws SQLException;

    /**
     * Xóa vai trò
     *
     * @param id ID của vai trò
     * @return Số bản ghi đã xóa (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int delete(String id) throws SQLException;
}
