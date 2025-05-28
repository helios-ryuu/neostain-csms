package com.neostain.csms.dao;

import com.neostain.csms.model.Token;

public interface TokenDAO {
    Token findById(String id);

    Token findByValue(String value);

    boolean create(Token token);

    boolean updateStatus(String tokenValue, String tokenStatus);
}
