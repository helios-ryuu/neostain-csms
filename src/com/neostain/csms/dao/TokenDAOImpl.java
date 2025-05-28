package com.neostain.csms.dao;

import com.neostain.csms.model.Token;
import com.neostain.csms.util.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Thực thi TokenDAO để quản lý các token xác thực
 * Sử dụng các câu truy vấn SQL từ SQLQueries để dễ bảo trì
 */
public class TokenDAOImpl implements TokenDAO {
    private static final Logger LOGGER = Logger.getLogger(TokenDAOImpl.class.getName());
    private final Connection conn;

    public TokenDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Token findByValue(String value) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.TOKEN_FIND_BY_VALUE)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToToken(rs);
                } else {
                    LOGGER.warning("[FIND_BY_VALUE] Token không tồn tại: " + value);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_VALUE] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Token findById(String id) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.TOKEN_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToToken(rs);
                } else {
                    LOGGER.warning("[FIND_BY_ID] Token không tồn tại: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean create(Token token) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.TOKEN_CREATE)) {
            ps.setString(1, token.getUsername());
            ps.setString(2, token.getValue());
            ps.setTimestamp(3, token.getExpiresAt());
            ps.setTimestamp(4, token.getIssuedAt());
            ps.setString(5, token.getStatus());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStatus(String tokenValue, String tokenStatus) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.TOKEN_UPDATE_STATUS)) {
            ps.setString(1, tokenStatus);
            ps.setString(2, tokenValue);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_STATUS] Lỗi: " + e.getMessage());
            return false;
        }
    }

    private Token mapResultSetToToken(ResultSet rs) throws SQLException {
        return new Token(
                rs.getString("ID"),
                rs.getString("USERNAME"),
                rs.getString("VALUE"),
                rs.getTimestamp("EXPIRES_AT"),
                rs.getTimestamp("ISSUED_AT"),
                rs.getString("STATUS")
        );
    }
}
