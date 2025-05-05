package com.neostain.csms.service;

import com.neostain.csms.ServiceManager;
import com.neostain.csms.dao.TokenDAO;
import com.neostain.csms.model.Token;
import com.neostain.csms.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import static com.neostain.csms.util.PasswordUtils.hash;

public class TokenServiceImpl implements TokenService {
    private final static Logger LOGGER = Logger.getLogger(TokenServiceImpl.class.getName());
    private final TokenDAO dao;

    public TokenServiceImpl(TokenDAO dao) {
        this.dao = dao;
    }

    @Override
    public boolean validate(String tokenValue) {
        Token token = this.getTokenByValue(tokenValue);
        if (token == null) {
            LOGGER.warning("[VALIDATE_TOKEN] Token không tồn tại");
            return false;
        }

        // Kiểm tra trạng thái token
        if (!"Có hiệu lực".equals(token.getTokenStatus())) {
            LOGGER.warning("[VALIDATE_TOKEN] Token không hợp lệ");
            return false;
        }

        // Kiểm tra thời hạn token
        Timestamp expiresAt = token.getExpiresAt();
        if (expiresAt == null || expiresAt.before(new Timestamp(System.currentTimeMillis()))) {
            LOGGER.warning("[VALIDATE_TOKEN] Token đã hết hạn");
            return false;
        }

        LOGGER.info("[VALIDATE_TOKEN] Token hợp lệ");
        return true;
    }

    @Override
    public void updateStatus(String tokenValue, String tokenStatus) {
        Token token = this.getTokenByValue(tokenValue);

        boolean affected = dao.updateStatus(tokenValue, tokenStatus);
        if (affected) {
            LOGGER.info("[UPDATE_STATUS] Cập nhật trạng thái <" + tokenStatus + "> cho token của user " + token.getUsername() + " thành công");
        } else {
            LOGGER.warning("[UPDATE_STATUS] Không tìm thấy token để cập nhật");
        }

    }

    @Override
    public Token getTokenByValue(String tokenValue) {
        if (StringUtils.isNullOrEmpty(tokenValue)) {
            LOGGER.warning("[GET_TOKEN_BY_VALUE] Giá trị token trống");
            return null;
        }

        try {
            return dao.findByValue(tokenValue);
        } catch (Exception e) {
            LOGGER.warning("[GET_TOKEN_BY_VALUE] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String generateToken(String username) {
        try {
            String tokenValue = hash(System.currentTimeMillis() / 1000 + String.valueOf(username));
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);

            Token token = new Token();
            token.setUsername(username);
            token.setTokenValue(tokenValue);
            token.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
            token.setExpiresAt(Timestamp.valueOf(expiresAt));
            token.setTokenStatus("Có hiệu lực");

            boolean affected = dao.create(token);
            if (affected) {
                LOGGER.info("[GENERATE_TOKEN] Tạo token thành công cho: " + username + "\nToken value: " + tokenValue
                        + "\nExpires at: " + expiresAt);
                return tokenValue;
            } else {
                LOGGER.severe("[GENERATE_TOKEN] Lỗi tạo token cho: " + username);
                return null;
            }
        } catch (Exception e) {
            LOGGER.severe("[GENERATE_TOKEN] Lỗi: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void invalidateToken(String currentToken) {
        ServiceManager.getInstance().getTokenService().updateStatus(currentToken, "Vô hiệu");
    }
}
