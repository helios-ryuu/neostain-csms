package com.neostain.csms.dao;

import com.neostain.csms.model.Member;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.util.List;

public interface MemberDAO {
    Member findById(String id);

    Member findByPhoneNumber(String phoneNumber);

    Member findByEmail(String email);

    List<Member> findAll();

    boolean create(Member member);

    boolean updateName(String id, String name);

    boolean updatePhoneNumber(String id, String phoneNumber) throws DuplicateFieldException;

    boolean updateEmail(String id, String email) throws DuplicateFieldException;

    boolean deleteById(String id);

    boolean deleteByPhoneNumber(String phoneNumber);
}
