package com.neostain.csms.service;

import com.neostain.csms.dao.MemberDAO;
import com.neostain.csms.model.Member;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.util.List;
import java.util.logging.Logger;

public class MemberServiceImpl implements MemberService {
    private static final Logger LOGGER = Logger.getLogger(MemberServiceImpl.class.getName());
    private final MemberDAO dao;

    public MemberServiceImpl(MemberDAO dao) {
        this.dao = dao;
    }

    @Override
    public Member getMemberByPhoneNumber(String phoneNumber) {
        if (StringUtils.isNullOrEmpty(phoneNumber)) {
            LOGGER.warning("[GET_MEMBER_BY_PHONE_NUMBER] Số điện thoại thành viên trống");
            return null;
        }

        try {
            return dao.findByPhoneNumber(phoneNumber);
        } catch (Exception e) {
            LOGGER.warning("[GET_MEMBER_BY_PHONE_NUMBER] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Member getMemberById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[GET_MEMBER_BY_ID] ID thành viên trống");
            return null;
        }

        try {
            return dao.findById(id);
        } catch (Exception e) {
            LOGGER.warning("[GET_MEMBER_BY_PHONE_NUMBER] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Member> getAllMembers() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            LOGGER.warning("[GET_ALL_MEMBERS] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateMember(Member member) throws DuplicateFieldException {
        try {
            dao.updateName(member.getMemberId(), member.getMemberName());
            dao.updatePhoneNumber(member.getMemberId(), member.getPhoneNumber());
            dao.updateEmail(member.getMemberId(), member.getEmail());
            return true;
        } catch (DuplicateFieldException e) {
            // Chỉ “bóp” exception chuyên biệt xuống UI
            throw e;
        } catch (Exception e) {
            LOGGER.severe("[CHANGE_STORE_NAME] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean add(Member member) {
        try {
            dao.create(member);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[ADD] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[REMOVE_BY_ID] ID thành viên trống");
            return false;
        }
        try {
            Member member = dao.findById(id);
            if (member == null) {
                LOGGER.warning("[REMOVE_BY_ID] Thành viên không tồn tại: " + id);
                return false;
            }
            dao.deleteById(id);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[REMOVE_BY_ID] Lỗi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeByPhoneNumber(String phoneNumber) {
        if (StringUtils.isNullOrEmpty(phoneNumber)) {
            LOGGER.warning("[REMOVE_BY_PHONE_NUMBER] Số điện thoại thành viên trống");
            return false;
        }
        try {
            Member member = dao.findByPhoneNumber(phoneNumber);
            if (member == null) {
                LOGGER.warning("[REMOVE_BY_PHONE_NUMBER] Thành viên không tồn tại: " + phoneNumber);
                return false;
            }
            dao.deleteByPhoneNumber(phoneNumber);
            return true;
        } catch (Exception e) {
            LOGGER.severe("[REMOVE_BY_PHONE_NUMBER] Lỗi: " + e.getMessage());
            return false;
        }
    }
}
