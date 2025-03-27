package com.neostain.csms.service;

import com.neostain.csms.service.api.*;
import com.neostain.csms.service.impl.*;

import javax.naming.NoPermissionException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class ServiceManager {
    private static final Object LOCK = new Object();
    private final AuthService authService;
    private final EmployeeService employeeService;
    private final AccountService accountService;
    private final RoleService roleService;
    private final TokenService tokenService;
    private String tokenValue = "";

    private ServiceManager() {
        this.authService = new CSMSAuthService();
        this.employeeService = new CSMSEmployeeService();
        this.accountService = new CSMSAccountService();
        this.roleService = new CSMSRoleService();
        this.tokenService = new CSMSTokenService();
    }

    private static final class InstanceHolder {
        private static final ServiceManager instance = new ServiceManager();
    }

    public static ServiceManager getInstance() {
        return InstanceHolder.instance;
    }

    /// Băm chuỗi đầu vào sử dụng thuật toán SHA-256.
    ///
    /// @param input Chuỗi cần băm.
    /// @return Chuỗi băm dưới dạng hex.
    /// @throws SecurityException Nếu có lỗi trong quá trình mã hóa.
    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
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

    public AuthService getAuthService() {
        return authService;
    }

    public EmployeeService getEmployeeService() throws NoPermissionException {
        // Xác thực token hiện tại trước khi thao tác
        if (validateCurrentToken()) {
            throw new NoPermissionException("Hành động không được xác thực hoặc Token đã hết hạn.");
        }
        return employeeService;
    }

    public AccountService getAccountService() throws NoPermissionException {
        // Xác thực token hiện tại trước khi thao tác
        if (validateCurrentToken()) {
            throw new NoPermissionException("Hành động không được xác thực hoặc Token đã hết hạn.");
        }
        return accountService;
    }

    public RoleService getRoleService() throws NoPermissionException {
        // Xác thực token hiện tại trước khi thao tác
        if (validateCurrentToken()) {
            throw new NoPermissionException("Hành động không được xác thực hoặc Token đã hết hạn.");
        }
        return roleService;
    }

    public TokenService getTokenService() throws NoPermissionException {
        // Xác thực token hiện tại trước khi thao tác
        if (validateCurrentToken()) {
            throw new NoPermissionException("Hành động không được xác thực hoặc Token đã hết hạn.");
        }
        return tokenService;
    }

    public String getCurrentTokenValue() {
        return tokenValue;
    }

    public void setCurrentTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    /// Kiểm tra tính hợp lệ của token hiện tại
    ///
    /// @return false nếu token còn hạn sử dụng và hợp lệ, true nếu ngược lại
    private boolean validateCurrentToken() {
        boolean result = false;
        if (tokenValue != null && !tokenValue.trim().isEmpty()) {
            result = tokenService.validateToken(tokenValue);
        }
        return !result;
    }
}