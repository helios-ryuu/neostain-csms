package com.neostain.csms.dao.sql;

/**
 * Class chứa tất cả các SQL query sử dụng trong ứng dụng
 * Giúp quản lý tập trung các câu truy vấn và dễ dàng chỉnh sửa khi cần
 */
public class SQLQueries {
    // Account queries
    public static final String ACCOUNT_FIND_BY_USERNAME = "SELECT * FROM ACCOUNT WHERE USERNAME = ?";
    public static final String ACCOUNT_CREATE = "INSERT INTO ACCOUNT(EMPLOYEE_ID, USERNAME, PASSWORD_HASH, ROLE_ID, ACCOUNT_CREATION_TIME) VALUES(?,?,?,?,?)";
    public static final String ACCOUNT_UPDATE_PASSWORD = "UPDATE ACCOUNT SET PASSWORD_HASH = ? WHERE EMPLOYEE_ID = ?";
    public static final String ACCOUNT_DELETE = "DELETE FROM ACCOUNT WHERE USERNAME = ?";
    public static final String ACCOUNT_AUTH = "SELECT PASSWORD_HASH FROM ACCOUNT WHERE USERNAME = ?";
    public static final String ACCOUNT_GET_ROLE = "SELECT r.ROLE_NAME FROM ACCOUNT a JOIN ROLE r ON a.ROLE_ID = r.ROLE_ID WHERE a.username = ?";

    // Employee queries
    public static final String EMPLOYEE_FIND_BY_ID = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
    public static final String EMPLOYEE_CREATE = "INSERT INTO EMPLOYEE(" +
            "EMPLOYEE_ID, " +
            "EMPLOYEE_NAME, " +
            "POSITION, " +
            "DEPARTMENT_ID, " +
            "HIRE_DATE, " +
            "EMAIL, " +
            "PHONE_NUMBER, " +
            "ADDRESS, " +
            "HOURLY_WAGE, " +
            "BASE_SALARY. " +
            "EMPLOYEE_STATUS_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    public static final String EMPLOYEE_DELETE = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";

    // Token queries
    public static final String TOKEN_FIND_BY_VALUE = "SELECT * FROM TOKEN WHERE TOKEN_VALUE = ?";
    public static final String TOKEN_FIND_BY_USERNAME = "SELECT * FROM TOKEN WHERE USERNAME = ?";
    public static final String TOKEN_CREATE = "INSERT INTO TOKEN(USERNAME, TOKEN_VALUE, EXPIRES_AT, ISSUED_AT, TOKEN_STATUS_ID) VALUES(?,?,?,?,?)";
    public static final String TOKEN_UPDATE = "UPDATE TOKEN SET TOKEN_VALUE = ?, TOKEN_STATUS_ID = ? WHERE TOKEN_ID = ?";
    public static final String TOKEN_DELETE = "DELETE FROM TOKEN WHERE TOKEN_VALUE = ?";

    // Role queries
    public static final String ROLE_FIND_BY_ID = "SELECT * FROM ROLE WHERE ROLE_ID = ?";
    public static final String ROLE_CREATE = "INSERT INTO ROLE(ROLE_ID, ROLE_NAME) VALUES(?,?)";
    public static final String ROLE_DELETE = "DELETE FROM ROLE WHERE ROLE_ID = ?";

    // Cancellation queries
    public static final String CANCELLATION_FIND_BY_ID = "SELECT * FROM CANCELLATION WHERE CANCELLATION_ID = ?";
    public static final String CANCELLATION_FIND_BY_PRODUCT_ID = "SELECT * FROM CANCELLATION WHERE PRODUCT_ID = ? ORDER BY CANCELLATION_TIME DESC";
    public static final String CANCELLATION_CREATE = "INSERT INTO CANCELLATION (CANCELLATION_ID, PRODUCT_ID, CANCELLATION_TIME, CANCELLED_QUANTITY, DESCRIPTION, CANCELLATION_STATUS_ID) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String CANCELLATION_UPDATE_STATUS = "UPDATE CANCELLATION SET CANCELLATION_STATUS_ID = ? WHERE CANCELLATION_ID = ?";
    public static final String CANCELLATION_DELETE = "DELETE FROM CANCELLATION WHERE CANCELLATION_ID = ?";
}
