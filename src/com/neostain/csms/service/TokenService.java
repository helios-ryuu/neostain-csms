package com.neostain.csms.service;

import com.neostain.csms.model.Token;

public interface TokenService {
    Token getTokenByValue(String tokenValue);

    String generateToken(String username);

    boolean validate(String tokenValue);

    void updateStatus(String tokenValue, String tokenStatusID);

    void invalidateToken(String currentToken);
}
