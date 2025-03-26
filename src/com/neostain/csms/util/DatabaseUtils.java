package com.neostain.csms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/// Lớp tiện ích cung cấp các phương thức để kết nối và tương tác với cơ sở dữ liệu
public class DatabaseUtils {

    /// Thông tin cấu hình kết nối đến cơ sở dữ liệu Oracle
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/NEOSTAIN_CSMS";
    private static final String DB_USER = "CSMS_ADMIN";
    private static final String DB_PASSWORD = "12345678";

    /// Tạo và trả về kết nối đến cơ sở dữ liệu
    ///
    /// @return Đối tượng Connection đã được khởi tạo
    /// @throws SQLException Nếu không thể kết nối đến cơ sở dữ liệu
    public static Connection getConnection() throws SQLException {
        try {
            // Đăng ký driver Oracle JDBC
            Class.forName("oracle.jdbc.OracleDriver");
            // Tạo kết nối mới đến cơ sở dữ liệu
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy driver cơ sở dữ liệu: " + e.getMessage());
        }
    }
}
