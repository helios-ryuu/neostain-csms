package com.neostain.csms.service.api;

import com.neostain.csms.model.Account;

public interface AccountService {

    /// Lấy dữ liệu Account bằng ACCOUNT_ID từ bảng ACCOUNT
    ///
    /// @param accountID ACCOUNT_ID hiện tại
    /// @return Một thể hiện đối tượng Account
    Account getAccount(String accountID);
}
