package com.neostain.csms.service.api;

import com.neostain.csms.model.Token;

public interface TokenService {

    /// Lấy dữ liệu Token bằng TOKEN_ID từ bảng TOKEN
    ///
    /// @param tokenValue TOKEN_VALUE hiện tại
    /// @return Một thể hiện đối tượng Token
    Token getToken(String tokenValue);

    /// Kiểm tra tính hợp lệ của token
    ///
    /// @param tokenValue TOKEN_VALUE cần kiểm tra
    /// @return true nếu token còn hạn sử dụng và hợp lệ, false nếu ngược lại
    boolean validateToken(String tokenValue);

    /// Cập nhật trạng thái của token
    ///
    /// @param tokenValue    TOKEN_VALUE cần cập nhật
    /// @param tokenStatusID Trạng thái mới của token
    /// @return true nếu cập nhật thành công, false nếu ngược lại
    boolean updateTokenStatus(String tokenValue, String tokenStatusID);
}
