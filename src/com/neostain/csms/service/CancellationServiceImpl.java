package com.neostain.csms.service;

import com.neostain.csms.dao.CancellationDAO;
import com.neostain.csms.model.Cancellation;
import com.neostain.csms.util.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CancellationServiceImpl implements CancellationService {
    private static final Logger LOGGER = Logger.getLogger(CancellationServiceImpl.class.getName());
    private final CancellationDAO cancellationDAO;

    public CancellationServiceImpl(CancellationDAO cancellationDAO) {
        this.cancellationDAO = cancellationDAO;
    }

    @Override
    public Cancellation getById(String id) {
        if (id == null || id.trim().isEmpty()) {
            LOGGER.warning("ID yêu cầu hủy hàng trống");
            return null;
        }

        try {
            return cancellationDAO.findById(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông tin hủy hàng: " + id, e);
            return null;
        }
    }

    @Override
    public List<Cancellation> getByProductId(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            LOGGER.warning("ID sản phẩm trống");
            return new ArrayList<>();
        }

        try {
            return cancellationDAO.findByProductId(productId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách hủy hàng theo sản phẩm: " + productId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean create(Cancellation cancellation) {
        if (cancellation == null) {
            LOGGER.warning("Thông tin hủy hàng rỗng");
            return false;
        }

        try {
            int result = cancellationDAO.create(cancellation);
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tạo thông tin hủy hàng", e);
            return false;
        }
    }

    @Override
    public boolean updateStatus(String id, String status_id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("ID yêu cầu hủy hàng trống");
            return false;
        }

        if (status_id == null || status_id.trim().isEmpty()) {
            LOGGER.warning("Trạng thái hủy hàng không hợp lệ");
            return false;
        }

        try {
            int result = cancellationDAO.updateStatus(id, status_id);
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái hủy hàng: " + status_id, e);
            return false;
        }
    }

    @Override
    public boolean remove(String id) {
        if (id == null || id.trim().isEmpty()) {
            LOGGER.warning("ID yêu cầu hủy hàng trống");
            return false;
        }

        try {
            int result = cancellationDAO.delete(id);
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa thông tin hủy hàng: " + id, e);
            return false;
        }
    }
}
