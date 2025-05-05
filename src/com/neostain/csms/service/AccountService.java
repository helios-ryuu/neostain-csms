package com.neostain.csms.service;

import com.neostain.csms.model.Account;

public interface AccountService {
    Account getAccountByUsername(String username);

    boolean register(Account acc);

    boolean changePassword(String username, String newHash);

    boolean removeByUsername(String username);
}
