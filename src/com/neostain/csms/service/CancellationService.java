package com.neostain.csms.service;

import com.neostain.csms.model.Cancellation;

import java.util.List;

public interface CancellationService {
    /**
     * Lấy thông tin hủy hàng theo ID
     *
     * @param id ID của thông tin hủy hàng
     * @return Đối tượng Cancellation hoặc null nếu không tìm thấy
     */
    Cancellation getById(String id);

    /**
     * Lấy tất cả thông tin hủy hàng của sản phẩm
     *
     * @param productId ID của sản phẩm
     * @return Danh sách các thông tin hủy hàng
     */
    List<Cancellation> getByProductId(String productId);

    /**
     * Tạo mới thông tin hủy hàng
     *
     * @param cancellation Thông tin hủy hàng cần tạo
     * @return true nếu thành công, false nếu thất bại
     */
    boolean create(Cancellation cancellation);

    /**
     * Cập nhật trạng thái hủy hàng
     *
     * @param id     ID của thông tin hủy hàng
     * @param status Trạng thái mới
     * @return true nếu thành công, false nếu thất bại
     */
    boolean updateStatus(String id, String status);

    /**
     * Xóa thông tin hủy hàng
     *
     * @param id ID của thông tin hủy hàng
     * @return true nếu thành công, false nếu thất bại
     */
    boolean remove(String id);
}
