package com.neostain.csms.dao;

import com.neostain.csms.model.Member;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.util.exception.FieldValidationException;

import java.sql.*;
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
    public List<Member> search(String memberId, String phone, String email, String dateFrom, String dateTo) {
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM member WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (memberId != null) {
            sql.append(" AND ID = ?");
            params.add(memberId);
        }
        if (phone != null) {
            sql.append(" AND PHONE_NUMBER LIKE ?");
            params.add("%" + phone + "%");
        }
        if (email != null) {
            sql.append(" AND EMAIL LIKE ?");
            params.add("%" + email + "%");
        }
        if (dateFrom != null) {
            sql.append(" AND REGISTRATION_TIME >= ?");
            params.add(Date.valueOf(dateFrom));
        }
        if (dateTo != null) {
            sql.append(" AND REGISTRATION_TIME <= ?");
            params.add(Date.valueOf(dateTo));
        }
        sql.append(" AND IS_DELETED = 0 ORDER BY REGISTRATION_TIME DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<Member> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapResultSetToMember(rs));
            }
            return result;
        } catch (SQLException e) {
            LOGGER.severe("[SEARCH] Lỗi: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean create(String name, String phone, String email) throws DuplicateFieldException, FieldValidationException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_CREATE)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            int code = e.getErrorCode();
            String msg = e.getMessage().toUpperCase();
            if (code == 20030) {
                throw new FieldValidationException(
                        "email",
                        "Email không hợp lệ. Ví dụ hợp lệ: user@example.com"
                );
            }
            if (code == 20031 || code == 12899) {
                throw new FieldValidationException(
                        "phoneNumber",
                        "Số điện thoại phải gồm đúng 10 chữ số, không chứa ký tự khác."
                );
            }
            if (code == 1) {
                if (msg.contains("UK_MEMBER_PHONE_NUMBER")) {
                    throw new DuplicateFieldException("phoneNumber", "Số điện thoại đã tồn tại.");
                }
                if (msg.contains("UK_MEMBER_EMAIL")) {
                    throw new DuplicateFieldException("email", "Email đã tồn tại.");
                }
            }
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
    public boolean updatePhoneNumber(String id, String phoneNumber) throws DuplicateFieldException, FieldValidationException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_UPDATE_PHONE_NUMBER)) {
            ps.setString(1, phoneNumber);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            int code = e.getErrorCode();                 // ví dụ: -20020, -20021, 1…
            String msg = e.getMessage().toUpperCase();
            if (code == 20021 || code == 12899) {
                throw new FieldValidationException(
                        "phoneNumber",
                        "Số điện thoại phải gồm đúng 10 chữ số, không chứa ký tự khác."
                );
            }
            if (code == 1) {
                if (msg.contains("UK_MEMBER_PHONE_NUMBER")) {
                    throw new DuplicateFieldException("phoneNumber",
                            "Số điện thoại đã tồn tại trong hệ thống.");
                }
            }
            return false;
        }
    }

    @Override
    public boolean updateEmail(String id, String email) throws DuplicateFieldException, FieldValidationException {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_UPDATE_EMAIL)) {
            ps.setString(1, email);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            // 1) Lỗi từ trigger: format email/phone
            int code = e.getErrorCode();                 // ví dụ: -20020, -20021, 1…
            String msg = e.getMessage().toUpperCase();

            if (code == 20020 || code == 12899) {
                throw new FieldValidationException(
                        "email",
                        "Email không hợp lệ. Ví dụ hợp lệ: user@example.com"
                );
            }
            if (code == 1) {
                if (msg.contains("UK_MEMBER_EMAIL")) {
                    throw new DuplicateFieldException("email",
                            "Email đã được sử dụng bởi thành viên khác.");
                }
            }
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.MEMBER_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE] Lỗi: " + e.getMessage());
            return false;
        }
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getString("ID"),
                rs.getString("NAME"),
                rs.getString("PHONE_NUMBER"),
                rs.getString("EMAIL"),
                rs.getDate("REGISTRATION_TIME"),
                rs.getInt("LOYALTY_POINTS"),
                rs.getInt("IS_DELETED") == 1
        );
    }
}
