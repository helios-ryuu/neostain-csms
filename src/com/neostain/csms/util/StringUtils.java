package com.neostain.csms.util;

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
}