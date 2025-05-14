package com.neostain.csms.dao;

import com.neostain.csms.model.Token;

import java.util.List;

public interface TokenDAO {
    Token findById(String id);

    List<Token> findByUsername(String username);

    Token findByValue(String value);

    boolean create(Token token);

    boolean updateValue(String id, String tokenValue);

    boolean updateStatus(String tokenValue, String tokenStatus);
}
