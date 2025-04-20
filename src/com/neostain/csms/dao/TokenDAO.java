package com.neostain.csms.dao;

import com.neostain.csms.model.Token;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface định nghĩa các thao tác cơ bản với token xác thực
 * Theo mô hình DAO (Data Access Object)
 */
public interface TokenDAO {
    /**
     * Tìm token theo giá trị
     *
     * @param value Giá trị token
     * @return Đối tượng Token hoặc null nếu không tìm thấy
     * @throws SQLException Nếu có lỗi truy vấn
     */
    Token findByValue(String value) throws SQLException;

    /**
     * Tìm danh sách token theo ID tài khoản
     *
     * @param username ID của tài khoản
     * @return Danh sách các token thuộc về tài khoản
     * @throws SQLException Nếu có lỗi truy vấn
     */
    List<Token> findByUsername(String username) throws SQLException;

    /**
     * Tạo mới token
     *
     * @param token Token cần tạo
     * @return Số bản ghi đã thêm (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int create(Token token) throws SQLException;

    /**
     * Cập nhật token
     *
     * @param token Token cần cập nhật
     * @return Số bản ghi đã cập nhật (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int update(Token token) throws SQLException;

    /**
     * Xóa token
     *
     * @param value Giá trị token cần xóa
     * @return Số bản ghi đã xóa (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int delete(String value) throws SQLException;
}
