package com.neostain.csms;

import com.neostain.csms.dao.*;
import com.neostain.csms.model.Account;
import com.neostain.csms.model.Employee;
import com.neostain.csms.model.Store;
import com.neostain.csms.service.*;
import com.neostain.csms.util.DatabaseUtils;
import com.neostain.csms.util.StringUtils;
import com.neostain.csms.util.exception.DuplicateFieldException;
import com.neostain.csms.util.exception.FieldValidationException;

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

    // Current shift id
    private String currentShiftId = null;

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
            InvoiceDAO invoiceDAO = new InvoiceDAOImpl(connection);
            InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAOImpl(connection);
            PaymentDAO paymentDAO = new PaymentDAOImpl(connection);
            ProductDAO productDAO = new ProductDAOImpl(connection);
            CategoryDAO categoryDAO = new CategoryDAOImpl(connection);
            PromotionDAO promotionDAO = new PromotionDAOImpl(connection);
            AssignmentDAO assignmentDAO = new AssignmentDAOImpl(connection);
            ShiftReportDAO shiftReportDAO = new ShiftReportDAOImpl(connection);
            PaycheckDAO paycheckDAO = new PaycheckDAOImpl(connection);
            PointUpdateLogDAO pointUpdateLogDAO = new PointUpdateLogDAOImpl(connection);
            InventoryTransactionDAO inventoryTransactionDAO = new InventoryTransactionDAOImpl(connection);
            InventoryDAO inventoryDAO = new InventoryDAOImpl(connection);

            // Register Services
            register(ManagementService.class, new ManagementServiceImpl(employeeDAO, memberDAO, storeDAO));
            register(SaleService.class, new SaleServiceImpl(invoiceDAO, invoiceDetailDAO, productDAO, categoryDAO, promotionDAO, pointUpdateLogDAO, paymentDAO, inventoryTransactionDAO, inventoryDAO));
            register(OperationService.class, new OperationServiceImpl(assignmentDAO, shiftReportDAO, paycheckDAO));
            register(AuthService.class, new AuthServiceImpl(tokenDAO, accountDAO, roleDAO, employeeDAO, storeDAO));
            register(PrintingService.class, new PrintingServiceImpl());
            register(StatisticService.class, new StatisticServiceImpl());

            LOGGER.info("[INIT] ServiceManager initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[INIT] Error initializing ServiceManager", e);
            throw new RuntimeException("Cannot initialize ServiceManager", e);
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
        if (requiresAuth && isLoggedIn()) {
            String serviceName = serviceClass.getSimpleName();
            LOGGER.log(Level.WARNING, "[GET_" + serviceName + "] Access denied to " + serviceName);
            throw new SecurityException("Access denied to " + serviceName);
        }

        // Lấy service từ registry
        Object svc = registry.get(serviceClass);
        if (svc == null) {
            throw new IllegalStateException("Service " + serviceClass.getSimpleName() + " not initialized!");
        }

        // Type‑safe cast
        return serviceClass.cast(svc);
    }

    // Auth service never requires authentication
    public AuthService getAuthService() {
        return getServiceWithAuthCheck(AuthService.class, false);
    }

    public ManagementService getManagementService() {
        return getServiceWithAuthCheck(ManagementService.class, true);
    }

    public ManagementService getManagementService(boolean requiresAuth) {
        return getServiceWithAuthCheck(ManagementService.class, requiresAuth);
    }

    public SaleService getSaleService() {
        return getServiceWithAuthCheck(SaleService.class, true);
    }

    public OperationService getOperationService() {
        return getServiceWithAuthCheck(OperationService.class, true);
    }

    public PrintingService getPrintingService() {
        return getServiceWithAuthCheck(PrintingService.class, true);
    }

    public StatisticService getStatisticService() {
        return getServiceWithAuthCheck(StatisticService.class, true);
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

    public String getCurrentShiftId() {
        return currentShiftId;
    }

    public void setCurrentShiftId(String shiftId) {
        this.currentShiftId = shiftId;
    }

    /**
     * Checks if a user is currently logged in by validating the token.
     *
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        if (StringUtils.isNullOrEmpty(currentToken)) {
            return true;
        }

        AuthService authService = getServiceWithAuthCheck(AuthService.class, false);
        return !authService.validate(currentToken);
    }

    /**
     * Logs in a user and creates an authentication token.
     * Uses temporary auth bypass to allow service access during login.
     *
     * @param username Username
     * @param password Password
     * @return Token if login successful, null if failed
     */
    public String login(String username, String password, String storeId) throws FieldValidationException, DuplicateFieldException {
        // Get services without auth check
        AuthService authService = getAuthService();
        ManagementService managementService = getServiceWithAuthCheck(ManagementService.class, false);
        OperationService operationService = getServiceWithAuthCheck(OperationService.class, false);
        Store store = managementService.getStoreById(storeId);

        // Authenticate user
        if (authService.authenticate(username, password, storeId)) {
            String token = authService.generateToken(username);
            Account account = authService.getAccountByUsername(username);

            String shiftReportId = operationService.createShiftReport(store.getId(), account.getEmployeeId());
            authService.updateAccountStatus(username, "ĐANG HOẠT ĐỘNG");
            Employee employee = managementService.getEmployeeById(account.getEmployeeId());
            employee.setStatus("ĐANG HOẠT ĐỘNG");
            managementService.updateEmployee(employee);

            setCurrentShiftId(shiftReportId);
            setCurrentToken(token);
            setCurrentUsername(username);

            LOGGER.info("[LOGIN] Login successful for user: " + username);
            return token;
        }

        LOGGER.warning("[LOGIN] Login failed for user: " + username);
        return null;
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        if (!StringUtils.isNullOrEmpty(currentToken)) {
            try {
                AuthService authService = getServiceWithAuthCheck(AuthService.class, false);
                authService.invalidateToken(currentToken);
                LOGGER.info("[LOGOUT] User logged out: " + currentUsername);
            } catch (Exception e) {
                LOGGER.warning("[LOGOUT] Error invalidating token: " + e.getMessage());
            }
        }
        currentToken = null;
        currentUsername = null;
    }
}
