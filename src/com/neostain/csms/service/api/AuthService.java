package com.neostain.csms.service.api;

public interface AuthService {

    /// Xác thực thông tin đăng nhập của người dùng.
    ///
    /// @param username Tên đăng nhập của người dùng.
    /// @param password Mật khẩu của người dùng.
    /// @return true nếu xác thực thành công, false nếu thất bại.
    boolean authenticate(String username, String password);

    /// Kiểm tra phân quyền của người dùng dựa trên tên đăng nhập và vai trò.
    ///
    /// @param username Tên đăng nhập của người dùng.
    /// @param role     Vai trò cần kiểm tra.
    /// @return true nếu người dùng có vai trò tương ứng, false nếu không.
    boolean isAuthorized(String username, String role);

    /// Tạo token mới cho người dùng dựa trên tên đăng nhập và lưu vào cơ sở dữ liệu.
    ///
    /// @param accountID Tên đăng nhập của người dùng.
    /// @return Chuỗi token nếu tạo thành công, ngược lại trả về null.
    String generateToken(String accountID);
}