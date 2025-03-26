package com.neostain.csms.service;

import com.neostain.csms.service.api.AccountService;
import com.neostain.csms.service.api.AuthService;
import com.neostain.csms.service.api.EmployeeService;
import com.neostain.csms.service.api.RoleService;
import com.neostain.csms.service.impl.CSMSAccountService;
import com.neostain.csms.service.impl.CSMSAuthService;
import com.neostain.csms.service.impl.CSMSEmployeeService;
import com.neostain.csms.service.impl.CSMSRoleService;

public class ServiceManager {
    private static ServiceManager instance;

    private final AuthService authService;
    private final EmployeeService employeeService;
    private final AccountService accountService;
    private final RoleService roleService;

    private ServiceManager() {
        this.authService = new CSMSAuthService();
        this.employeeService = new CSMSEmployeeService();
        this.accountService = new CSMSAccountService();
        this.roleService = new CSMSRoleService();
    }

    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public RoleService getRoleService() {
        return roleService;
    }
}