package com.neostain.csms.service;

import com.neostain.csms.model.Member;
import com.neostain.csms.util.exception.DuplicateFieldException;

import java.util.List;

public interface MemberService {
    Member getMemberByPhoneNumber(String phoneNumber);

    Member getMemberById(String id);

    List<Member> getAllMembers();

    boolean updateMember(Member member) throws DuplicateFieldException;

    boolean add(Member member);

    boolean removeById(String id);

    boolean removeByPhoneNumber(String phoneNumber);
}
