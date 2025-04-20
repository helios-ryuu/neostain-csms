package com.neostain.csms.dao;

import com.neostain.csms.model.Account;

import java.sql.SQLException;

/**
 * Interface định nghĩa các thao tác cơ bản với tài khoản người dùng
 * Theo mô hình DAO (Data Access Object)
 */
public interface AccountDAO {
    /**
     * Tìm tài khoản theo ID
     *
     * @param username ID của tài khoản
     * @return Đối tượng Account hoặc null nếu không tìm thấy
     * @throws SQLException Nếu có lỗi truy vấn
     */
    Account findByUsername(String username) throws SQLException;

    /**
     * Tạo mới tài khoản
     *
     * @param acc Tài khoản cần tạo
     * @return Số bản ghi đã thêm (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int create(Account acc) throws SQLException;

    /**
     * Cập nhật mật khẩu
     *
     * @param username Tên người dùng
     * @param newHash  Mật khẩu đã hash mới
     * @return true nếu cập nhật thành công, ngược lại false
     * @throws SQLException Nếu có lỗi truy vấn
     */
    boolean updatePassword(String username, String newHash) throws SQLException;

    /**
     * Xóa tài khoản
     *
     * @param username Tên người dùng
     * @return Số bản ghi đã xóa (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int delete(String username) throws SQLException;
}
