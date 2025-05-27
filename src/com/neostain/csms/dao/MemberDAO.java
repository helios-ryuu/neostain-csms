package com.neostain.csms.dao;

import com.neostain.csms.model.Member;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.util.exception.FieldValidationException;

import java.util.List;

public interface MemberDAO {
    Member findById(String id);

    Member findByPhoneNumber(String phoneNumber);

    Member findByEmail(String email);

    List<Member> findAll();

    List<Member> search(String memberId, String phone, String email,
                        String dateFrom, String dateTo);

    boolean create(String name, String phone, String email) throws DuplicateFieldException, FieldValidationException;

    boolean updateName(String id, String name);

    boolean updatePhoneNumber(String id, String phoneNumber) throws DuplicateFieldException, FieldValidationException;

    boolean updateEmail(String id, String email) throws DuplicateFieldException, FieldValidationException;

    boolean delete(String id);
}
