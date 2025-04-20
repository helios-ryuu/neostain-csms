package com.neostain.csms.dao;

import com.neostain.csms.dao.sql.SQLQueries;
import com.neostain.csms.model.Token;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public Token findByValue(String value) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.TOKEN_FIND_BY_VALUE)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Token(
                            rs.getInt("TOKEN_ID"),
                            rs.getString("USERNAME"),
                            rs.getString("TOKEN_VALUE"),
                            rs.getTimestamp("EXPIRES_AT"),
                            rs.getTimestamp("ISSUED_AT"),
                            rs.getString("TOKEN_STATUS_ID")
                    );
                } else {
                    LOGGER.warning("[FIND_BY_VALUE] Token không tồn tại: " + value);
                    return null;
                }
            }
        }
    }

    @Override
    public List<Token> findByUsername(String username) throws SQLException {
        List<Token> tokens = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.TOKEN_FIND_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tokens.add(new Token(
                            rs.getInt("TOKEN_ID"),
                            rs.getString("USERNAME"),
                            rs.getString("TOKEN_VALUE"),
                            rs.getTimestamp("EXPIRES_AT"),
                            rs.getTimestamp("ISSUED_AT"),
                            rs.getString("TOKEN_STATUS_ID")
                    ));
                }
                return tokens;
            }
        }
    }

    @Override
    public int create(Token token) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.TOKEN_CREATE)) {
            ps.setString(1, token.getUsername());
            ps.setString(2, token.getTokenValue());
            ps.setTimestamp(3, token.getExpiresAt());
            ps.setTimestamp(4, token.getIssuedAt());
            ps.setString(5, token.getTokenStatusID());
            return ps.executeUpdate();
        }
    }

    @Override
    public int update(Token token) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.TOKEN_UPDATE)) {
            ps.setString(1, token.getTokenValue());
            ps.setString(2, token.getTokenStatusID());
            ps.setInt(3, token.getTokenID());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(String value) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.TOKEN_DELETE)) {
            ps.setString(1, value);
            return ps.executeUpdate();
        }
    }
}
