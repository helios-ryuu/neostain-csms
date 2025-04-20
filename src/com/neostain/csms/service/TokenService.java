package com.neostain.csms.service;

import com.neostain.csms.model.Token;

public interface TokenService {
    Token getByValue(String tokenValue);

    String generateToken(String username);

    boolean validate(String tokenValue);

    void updateStatus(String tokenValue, String tokenStatusID);

    boolean remove(String tokenValue);

    void invalidateToken(String currentToken);
}
