package com.neostain.csms.dao;

import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.model.Cancellation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CancellationDAOImpl implements CancellationDAO {
    private static final Logger LOGGER = Logger.getLogger(CancellationDAOImpl.class.getName());
    private final Connection conn;

    public CancellationDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Cancellation findById(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CANCELLATION_FIND_BY_ID)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCancellation(rs);
            }
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm thông tin hủy hàng theo ID: " + id, e);
            throw e;
        }
    }

    @Override
    public List<Cancellation> findByProductId(String productId) throws SQLException {
        List<Cancellation> cancellations = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CANCELLATION_FIND_BY_PRODUCT_ID)) {
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cancellations.add(mapResultSetToCancellation(rs));
            }
            return cancellations;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm thông tin hủy hàng theo Product ID: " + productId, e);
            throw e;
        }
    }

    @Override
    public int create(Cancellation cancellation) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CANCELLATION_CREATE)) {
            ps.setString(1, cancellation.getCancellationID());
            ps.setString(2, cancellation.getProductID());
            ps.setDate(3, cancellation.getCancellationTime());
            ps.setInt(4, cancellation.getCancelledQuantity());
            ps.setString(5, cancellation.getDescription());
            ps.setString(6, cancellation.getCancellationStatus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tạo thông tin hủy hàng: " + cancellation.getCancellationID(), e);
            throw e;
        }
    }

    @Override
    public int updateStatus(String id, String status_id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CANCELLATION_UPDATE_STATUS)) {
            ps.setString(1, status_id);
            ps.setString(2, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái hủy hàng: " + id, e);
            throw e;
        }
    }

    @Override
    public int delete(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.CANCELLATION_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa thông tin hủy hàng: " + id, e);
            throw e;
        }
    }

    /**
     * Ánh xạ kết quả ResultSet sang đối tượng Cancellation
     *
     * @param rs ResultSet chứa dữ liệu từ database
     * @return Đối tượng Cancellation
     * @throws SQLException Nếu có lỗi khi truy cập dữ liệu
     */
    private Cancellation mapResultSetToCancellation(ResultSet rs) throws SQLException {
        return new Cancellation(
                rs.getString("CANCELLATION_ID"),
                rs.getString("PRODUCT_ID"),
                rs.getDate("CANCELLATION_TIME"),
                rs.getInt("CANCELLED_QUANTITY"),
                rs.getString("DESCRIPTION"),
                rs.getString("CANCELLATION_STATUS")
        );
    }
}