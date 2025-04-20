package com.neostain.csms.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;

public class PasswordUtils {
    private static final Pattern COMPLEXITY =
            Pattern.compile("^(?=.{8,}$)(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).+$");

    private static final String HASH_ALGORITHM = "SHA-256";

    public static boolean isComplex(String pwd) {
        return pwd != null && COMPLEXITY.matcher(pwd).matches();
    }

    /**
     * Hash chuỗi
     *
     * @param input chuỗi cần hash
     * @return chuỗi hash dạng hex
     */
    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new SecurityException("Lỗi mã hóa", e);
        }
    }

    /**
     * Kiểm tra mật khẩu với hash
     */
    public static boolean verify(String input, String hash) {
        if (StringUtils.isNullOrEmpty(input) || StringUtils.isNullOrEmpty(hash)) {
            return false;
        }

        try {
            return hash.equals(hash(input));
        } catch (Exception e) {
            return false;
        }
    }
}
