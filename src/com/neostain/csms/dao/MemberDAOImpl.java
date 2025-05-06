package com.neostain.csms.dao;

import com.neostain.csms.model.Member;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MemberDAOImpl implements MemberDAO {
    private static final Logger LOGGER = Logger.getLogger(MemberDAOImpl.class.getName());
    private final Connection conn;

    public MemberDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Member findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] ID thành viên trống");
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                } else {
                    LOGGER.warning("[FIND_BY_ID] Thành vien không tồn tại: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Member findByPhoneNumber(String phoneNumber) {
        if (StringUtils.isNullOrEmpty(phoneNumber)) {
            LOGGER.warning("[FIND_BY_PHONE_NUMBER] Số điện thoại thành viên trống");
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_FIND_BY_PHONE_NUMBER)) {
            ps.setString(1, phoneNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                } else {
                    LOGGER.warning("[FIND_BY_PHONE_NUMBER] Thành vien không tồn tại: " + phoneNumber);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_PHONE_NUMBER] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Member findByEmail(String email) {
        if (StringUtils.isNullOrEmpty(email)) {
            LOGGER.warning("[FIND_BY_EMAIL] Email thành viên trống");
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_FIND_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                } else {
                    LOGGER.warning("[FIND_BY_EMAIL] Thành vien không tồn tại: " + email);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_EMAIL] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToMember(rs));
                }
                return members;
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean create(Member member) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_CREATE)) {
            ps.setString(1, member.getMemberName());
            ps.setString(2, member.getPhoneNumber());
            ps.setString(3, member.getEmail());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateName(String id, String name) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_UPDATE_NAME)) {
            ps.setString(1, name);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_NAME] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePhoneNumber(String id, String phoneNumber) throws DuplicateFieldException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_UPDATE_PHONE_NUMBER)) {
            ps.setString(1, phoneNumber);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                String msg = e.getMessage().toUpperCase();
                if (msg.contains("UK_MEMBER_PHONE_NUMBER")) {
                    throw new DuplicateFieldException("phoneNumber",
                            "Số điện thoại đã tồn tại trong hệ thống.");
                }
            }
            return false;
        }
    }

    @Override
    public boolean updateEmail(String id, String email) throws DuplicateFieldException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_UPDATE_EMAIL)) {
            ps.setString(1, email);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                String msg = e.getMessage().toUpperCase();
                if (msg.contains("UK_MEMBER_EMAIL")) {
                    throw new DuplicateFieldException("email",
                            "Email đã được sử dụng bởi thành viên khác.");
                }
            }
            return false;
        }
    }

    @Override
    public boolean deleteById(String id) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_DELETE_BY_ID)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE_BY_ID] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByPhoneNumber(String phoneNumber) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_DELETE_BY_PHONE_NUMBER)) {
            ps.setString(1, phoneNumber);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE_BY_PHONE_NUMBER] Lỗi: " + e.getMessage());
            return false;
        }
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getString("MEMBER_ID"),
                rs.getString("MEMBER_NAME"),
                rs.getString("PHONE_NUMBER"),
                rs.getString("EMAIL"),
                rs.getDate("REGISTRATION_TIME"),
                rs.getInt("LOYALTY_POINTS")
        );
    }
}
