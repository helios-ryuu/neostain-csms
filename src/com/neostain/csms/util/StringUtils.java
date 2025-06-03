package com.neostain.csms.util;

import java.sql.SQLException;

public class StringUtils {
    /**
     * Kiểm tra xem một chuỗi có null hoặc rỗng hay không.
     *
     * @param str Chuỗi cần kiểm tra
     * @return true nếu chuỗi là null hoặc rỗng, ngược lại false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String fetchMessage(SQLException e) {// 1. Lấy toàn bộ chuỗi lỗi do e.getMessage() trả về
        String fullMsg = e.getMessage();

        // 2. Tách thành các dòng theo dấu ngắt dòng (\n hoặc \r\n)
        String[] lines = fullMsg.split("\\r?\\n");

        // 3. Dòng đầu tiên thường là ORA-20001: ...
        String ora20001Line = lines.length > 0 ? lines[0] : fullMsg;

        // 4. (Tuỳ chọn) Cắt chỉ phần sau “ORA-20001: ” nếu muốn lấy mỗi “DA CO PAYCHECK ...”
        String prefix = "ORA-20001:";
        String onlyMessage;
        if (ora20001Line.startsWith(prefix)) {
            onlyMessage = ora20001Line.substring(prefix.length()).trim();
        } else {
            onlyMessage = ora20001Line;
        }
        return onlyMessage;
    }
}