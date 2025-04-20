package com.neostain.csms;

import com.neostain.csms.dao.*;
import com.neostain.csms.service.*;
import com.neostain.csms.util.DatabaseUtils;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Quản lý tập trung các services cho ứng dụng.
 * Áp dụng quy tắc Single Responsibility.
 */
public class ServiceManager {
    private static final Logger LOGGER = Logger.getLogger(ServiceManager.class.getName());
    private static ServiceManager INSTANCE;

    // Registry for created services
    private final Map<Class<?>, Object> serviceRegistry = new HashMap<>();

    // Current authentication token
    private String currentToken = null;

    // Currently logged-in username
    private String currentUsername = null;

    // Flag to bypass authentication during the login process
    private boolean bypassAuth = false;

    private ServiceManager() {
        try {
            // Database connection
            Connection connection = DatabaseUtils.getConnection();

            // Register DAOs
            AccountDAO accountDAO = new AccountDAOImpl(connection);
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(connection);
            TokenDAO tokenDAO = new TokenDAOImpl(connection);
            RoleDAO roleDAO = new RoleDAOImpl(connection);
            CancellationDAO cancellationDAO = new CancellationDAOImpl(connection);

            // Register Services
            registerService(TokenService.class, new TokenServiceImpl(tokenDAO));
            registerService(AccountService.class, new AccountServiceImpl(accountDAO));
            registerService(EmployeeService.class, new EmployeeServiceImpl(employeeDAO));
            registerService(RoleService.class, new RoleServiceImpl(roleDAO));
            registerService(CancellationService.class, new CancellationServiceImpl(cancellationDAO));
            registerService(AuthService.class, new AuthServiceImpl());

            LOGGER.info("[INIT] Khởi tạo ServiceManager thành công");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[INIT] Lỗi trong khi khởi tạo dịch vụ", e);
            throw new RuntimeException("Không thể khởi tạo ServiceManager", e);
        }
    }

    /**
     * Singleton pattern for ServiceManager
     *
     * @return ServiceManager instance
     */
    public static ServiceManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceManager();
        }
        return INSTANCE;
    }

    private <T> void registerService(Class<T> serviceClass, T implementation) {
        serviceRegistry.put(serviceClass, implementation);
    }

    @SuppressWarnings("unchecked")
    private <T> T getServiceWithAuthCheck(Class<T> serviceClass, boolean requiresAuth) {
        // Skip authentication check if not required or if bypass is active
        if (!requiresAuth || bypassAuth) {
            return (T) serviceRegistry.get(serviceClass);
        }

        // Check authentication
        if (isLoggedIn()) {
            return (T) serviceRegistry.get(serviceClass);
        } else {
            String serviceName = serviceClass.getSimpleName();
            LOGGER.log(Level.WARNING, "[GET_" + serviceName + "] Access denied to " + serviceName);
            throw new SecurityException("Access denied to " + serviceName);
        }
    }

    /**
     * Public service getters with appropriate authentication requirements
     */
    public AuthService getAuthService() {
        // Auth service never requires authentication
        return getServiceWithAuthCheck(AuthService.class, false);
    }

    public TokenService getTokenService() {
        // Token service used for both auth and non-auth operations
        return getServiceWithAuthCheck(TokenService.class, false);
    }

    public AccountService getAccountService() {
        return getServiceWithAuthCheck(AccountService.class, true);
    }

    public EmployeeService getEmployeeService() {
        return getServiceWithAuthCheck(EmployeeService.class, true);
    }

    public RoleService getRoleService() {
        return getServiceWithAuthCheck(RoleService.class, true);
    }

    public CancellationService getCancellationService() {
        return getServiceWithAuthCheck(CancellationService.class, true);
    }

    /**
     * Token and session management
     */
    public String getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(String token) {
        this.currentToken = token;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    /**
     * Checks if a user is currently logged in by validating the token.
     *
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        if (StringUtils.isNullOrEmpty(currentToken)) {
            return false;
        }

        TokenService tokenService = getServiceWithAuthCheck(TokenService.class, false);
        return tokenService.validate(currentToken);
    }

    /**
     * Logs in a user and creates an authentication token.
     * Uses temporary auth bypass to allow service access during login.
     *
     * @param username Username
     * @param password Password
     * @return Token if login successful, null if failed
     */
    public String login(String username, String password) {
        try {
            // Enable bypass during a login process
            bypassAuth = true;

            // Get services without auth check
            AuthService authService = getAuthService();
            TokenService tokenService = getTokenService();

            // Authenticate user
            if (authService.authenticate(username, password)) {
                // Generate and store token
                String token = tokenService.generateToken(username);
                setCurrentToken(token);
                setCurrentUsername(username);

                LOGGER.info("[LOGIN] Đăng nhâp thành công cho người dùng: " + username);
                return token;
            }

            LOGGER.warning("[LOGIN] Đăng nhâp thất bại cho người dùng: " + username);
            return null;
        } finally {
            // Disable bypass after a login attempt completes
            bypassAuth = false;
        }
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        if (!StringUtils.isNullOrEmpty(currentToken)) {
            try {
                TokenService tokenService = getTokenService();
                tokenService.invalidateToken(currentToken);
                LOGGER.info("[LOGOUT] Người dùng đăng xuất: " + currentUsername);
            } catch (Exception e) {
                LOGGER.warning("[LOGOUT] Lỗi khi vô hiệu Token: " + e.getMessage());
            }
        }
        currentToken = null;
        currentUsername = null;
    }
}
