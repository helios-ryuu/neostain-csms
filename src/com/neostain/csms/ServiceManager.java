package com.neostain.csms;

import com.neostain.csms.dao.*;
import com.neostain.csms.service.*;
import com.neostain.csms.util.DatabaseUtils;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Quản lý tập trung các services cho ứng dụng.
 * Áp dụng quy tắc Single Responsibility.
 */
public class ServiceManager {
    private static final Logger LOGGER = Logger.getLogger(ServiceManager.class.getName());
    private static volatile ServiceManager INSTANCE;

    // Registry for created services
    private final Map<Class<?>, Object> registry = new ConcurrentHashMap<>();

    // Current authentication token
    private String currentToken = null;

    // Currently logged-in username
    private String currentUsername = null;

    private ServiceManager() {
        try {
            // Database connection
            Connection connection = DatabaseUtils.getConnection();

            // Register DAOs
            AccountDAO accountDAO = new AccountDAOImpl(connection);
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(connection);
            MemberDAO memberDAO = new MemberDAOImpl(connection);
            TokenDAO tokenDAO = new TokenDAOImpl(connection);
            StoreDAO storeDAO = new StoreDAOImpl(connection);
            RoleDAO roleDAO = new RoleDAOImpl(connection);

            // Register Services
            register(EmployeeService.class, new EmployeeServiceImpl(employeeDAO));
            register(MemberService.class, new MemberServiceImpl(memberDAO));
            register(StoreService.class, new StoreServiceImpl(storeDAO));

            register(AuthService.class, new AuthServiceImpl(tokenDAO, accountDAO, roleDAO));

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
            synchronized (ServiceManager.class) {
                if (INSTANCE == null) INSTANCE = new ServiceManager();
            }
        }
        return INSTANCE;
    }

    private <T> void register(Class<T> serviceClass, T implementation) {
        registry.put(serviceClass, implementation);
    }

    private <T> T getServiceWithAuthCheck(Class<T> serviceClass, boolean requiresAuth) {
        // Kiểm tra authentication nếu cần
        if (requiresAuth && !isLoggedIn()) {
            String serviceName = serviceClass.getSimpleName();
            LOGGER.log(Level.WARNING, "[GET_" + serviceName + "] Access denied to " + serviceName);
            throw new SecurityException("Access denied to " + serviceName);
        }

        // Lấy service từ registry
        Object svc = registry.get(serviceClass);
        if (svc == null) {
            throw new IllegalStateException("Service " + serviceClass.getSimpleName() + " chưa được khởi tạo!");
        }

        // Type‑safe cast
        return serviceClass.cast(svc);
    }

    // Auth service never requires authentication
    public AuthService getAuthService() {
        return getServiceWithAuthCheck(AuthService.class, false);
    }

    public EmployeeService getEmployeeService() {
        return getServiceWithAuthCheck(EmployeeService.class, true);
    }

    public MemberService getMemberService() {
        return getServiceWithAuthCheck(MemberService.class, true);
    }

    public StoreService getStoreService() {
        return getServiceWithAuthCheck(StoreService.class, true);
    }

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

        AuthService authService = getServiceWithAuthCheck(AuthService.class, false);
        return authService.validate(currentToken);
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
        // Get services without auth check
        AuthService authService = getAuthService();

        // Authenticate user
        if (authService.authenticate(username, password)) {
            // Generate and store token
            String token = authService.generateToken(username);
            setCurrentToken(token);
            setCurrentUsername(username);

            LOGGER.info("[LOGIN] Đăng nhâp thành công cho người dùng: " + username);
            return token;
        }

        LOGGER.warning("[LOGIN] Đăng nhâp thất bại cho người dùng: " + username);
        return null;
    }

    // TODO: loại bỏ phân ca, thêm logic đóng ca làm việc khi tổng kết ca làm

    /**
     * Logs out the current user.
     */
    public void logout() {
        if (!StringUtils.isNullOrEmpty(currentToken)) {
            try {
                AuthService authService = getServiceWithAuthCheck(AuthService.class, false);
                authService.invalidateToken(currentToken);
                LOGGER.info("[LOGOUT] Người dùng đăng xuất: " + currentUsername);
            } catch (Exception e) {
                LOGGER.warning("[LOGOUT] Lỗi khi vô hiệu Token: " + e.getMessage());
            }
        }
        currentToken = null;
        currentUsername = null;
    }
}
