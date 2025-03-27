package com.neostain.csms.service.api;

import com.neostain.csms.model.Role;

public interface RoleService {

    /// Lấy dữ liệu Role bằng ROLE_ID từ bảng ROLE
    ///
    /// @param roleID ROLE_ID hiện tại
    /// @return Một thể hiện đối tượng Role
    Role getRole(String roleID);
}
