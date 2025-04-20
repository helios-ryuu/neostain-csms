package com.neostain.csms.dao;

import com.neostain.csms.model.Cancellation;

import java.sql.SQLException;
import java.util.List;

public interface CancellationDAO {
    /**
     * Tìm thông tin hủy hàng theo ID
     *
     * @param id ID của thông tin hủy hàng
     * @return Đối tượng Cancellation hoặc null nếu không tìm thấy
     * @throws SQLException Nếu có lỗi truy vấn
     */
    Cancellation findById(String id) throws SQLException;

    /**
     * Tìm tất cả thông tin hủy hàng của sản phẩm
     *
     * @param productId ID của sản phẩm
     * @return Danh sách các thông tin hủy hàng
     * @throws SQLException Nếu có lỗi truy vấn
     */
    List<Cancellation> findByProductId(String productId) throws SQLException;

    /**
     * Tạo mới thông tin hủy hàng
     *
     * @param cancellation Thông tin hủy hàng cần tạo
     * @return Số bản ghi đã thêm (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int create(Cancellation cancellation) throws SQLException;

    /**
     * Cập nhật trạng thái của thông tin hủy hàng
     *
     * @param id        ID của thông tin hủy hàng
     * @param status_id Trạng thái mới
     * @return Số bản ghi đã cập nhật (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int updateStatus(String id, String status_id) throws SQLException;

    /**
     * Xóa thông tin hủy hàng
     *
     * @param id ID của thông tin hủy hàng
     * @return Số bản ghi đã xóa (thường là 1 nếu thành công)
     * @throws SQLException Nếu có lỗi truy vấn
     */
    int delete(String id) throws SQLException;
}