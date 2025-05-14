package com.neostain.csms.util;

/**
 * Class chứa tất cả các SQL query sử dụng trong ứng dụng
 * Giúp quản lý tập trung các câu truy vấn và dễ dàng chỉnh sửa khi cần
 */
public class SQLQueries {
    // Account queries
    public static final String ACCOUNT_FIND_BY_EMPLOYEE_ID = "SELECT * FROM ACCOUNT WHERE EMPLOYEE_ID = ? AND IS_DELETED = 0";
    public static final String ACCOUNT_FIND_BY_ID = "SELECT * FROM ACCOUNT WHERE ID = ? AND IS_DELETED = 0";
    public static final String ACCOUNT_FIND_BY_USERNAME = "SELECT * FROM ACCOUNT WHERE USERNAME = ? AND IS_DELETED = 0";
    public static final String ACCOUNT_FIND_BY_ROLE_ID = "SELECT * FROM ACCOUNT WHERE ROLE_ID = ? AND IS_DELETED = 0";
    public static final String ACCOUNT_FIND_BY_STATUS = "SELECT * FROM ACCOUNT WHERE STATUS = ? AND IS_DELETED = 0";
    public static final String ACCOUNT_FIND_ALL = "SELECT * FROM ACCOUNT WHERE IS_DELETED = 0";
    public static final String ACCOUNT_GET_PASSWORD_HASH = "SELECT PASSWORD_HASH FROM ACCOUNT WHERE USERNAME = ?";
    public static final String ACCOUNT_GET_ROLE_NAME =
            "SELECT r.NAME FROM ACCOUNT a JOIN ROLE r ON a.ROLE_ID = r.ID WHERE a.USERNAME = ?";

    public static final String ACCOUNT_CREATE = "INSERT INTO ACCOUNT(EMPLOYEE_ID, USERNAME, PASSWORD_HASH, ROLE_ID) VALUES(?, ?, ?, ?)";
    public static final String ACCOUNT_UPDATE_PASSWORD_HASH = "UPDATE ACCOUNT SET PASSWORD_HASH = ? WHERE USERNAME = ?";
    public static final String ACCOUNT_UPDATE_STATUS = "UPDATE ACCOUNT SET STATUS = ? WHERE USERNAME = ?";
    public static final String ACCOUNT_DELETE = "UPDATE ACCOUNT SET IS_DELETED = 1 WHERE USERNAME = ?";

    // Assignment queries (no IS_DELETED flag on ASSIGNMENT table)
    public static final String ASSIGNMENT_FIND_BY_ID = "SELECT * FROM ASSIGNMENT WHERE ID = ?";
    public static final String ASSIGNMENT_FIND_BY_EMPLOYEE_ID = "SELECT * FROM ASSIGNMENT WHERE EMPLOYEE_ID = ?";
    public static final String ASSIGNMENT_FIND_BY_STORE_ID = "SELECT * FROM ASSIGNMENT WHERE STORE_ID = ?";
    public static final String ASSIGNMENT_FIND_BY_RANGE =
            "SELECT * FROM ASSIGNMENT WHERE SYSTIMESTAMP BETWEEN START_TIME AND END_TIME AND EMPLOYEE_ID = ? AND STORE_ID = ?";
    public static final String ASSIGNMENT_FIND_ALL = "SELECT * FROM ASSIGNMENT";

    public static final String ASSIGNMENT_CREATE = "INSERT INTO ASSIGNMENT(EMPLOYEE_ID, STORE_ID, START_TIME, END_TIME) VALUES(?, ?, ?, ?)";
    public static final String ASSIGNMENT_UPDATE_START_TIME = "UPDATE ASSIGNMENT SET START_TIME = ? WHERE ID = ?";
    public static final String ASSIGNMENT_UPDATE_END_TIME = "UPDATE ASSIGNMENT SET END_TIME = ? WHERE ID = ?";
    public static final String ASSIGNMENT_DELETE = "DELETE FROM ASSIGNMENT WHERE ID = ?";

    // Category queries
    public static final String CATEGORY_FIND_BY_ID = "SELECT * FROM CATEGORY WHERE ID = ? AND IS_DELETED = 0";
    public static final String CATEGORY_FIND_BY_NAME = "SELECT * FROM CATEGORY WHERE NAME = ? AND IS_DELETED = 0";
    public static final String CATEGORY_FIND_ALL = "SELECT * FROM CATEGORY WHERE IS_DELETED = 0";

    public static final String CATEGORY_CREATE = "INSERT INTO CATEGORY(ID, NAME) VALUES(?, ?)";
    public static final String CATEGORY_UPDATE_NAME = "UPDATE CATEGORY SET NAME = ? WHERE ID = ?";
    public static final String CATEGORY_DELETE = "UPDATE CATEGORY SET IS_DELETED = 1 WHERE ID = ?";

    // Employee queries
    public static final String EMPLOYEE_FIND_BY_ID = "SELECT * FROM EMPLOYEE WHERE ID = ? AND IS_DELETED = 0";
    public static final String EMPLOYEE_FIND_BY_NAME = "SELECT * FROM EMPLOYEE WHERE NAME = ? AND IS_DELETED = 0";
    public static final String EMPLOYEE_FIND_BY_POSITION = "SELECT * FROM EMPLOYEE WHERE POSITION = ? AND IS_DELETED = 0";
    public static final String EMPLOYEE_FIND_BY_STATUS = "SELECT * FROM EMPLOYEE WHERE STATUS = ? AND IS_DELETED = 0";
    public static final String EMPLOYEE_FIND_ALL = "SELECT * FROM EMPLOYEE WHERE IS_DELETED = 0";

    public static final String EMPLOYEE_CREATE =
            "INSERT INTO EMPLOYEE(NAME, POSITION, EMAIL, PHONE_NUMBER, ADDRESS, HOURLY_WAGE) VALUES(?, ?, ?, ?, ?, ?)";
    public static final String EMPLOYEE_UPDATE_NAME = "UPDATE EMPLOYEE SET NAME = ? WHERE ID = ?";
    public static final String EMPLOYEE_UPDATE_POSITION = "UPDATE EMPLOYEE SET POSITION = ? WHERE ID = ?";
    public static final String EMPLOYEE_UPDATE_EMAIL = "UPDATE EMPLOYEE SET EMAIL = ? WHERE ID = ?";
    public static final String EMPLOYEE_UPDATE_PHONE_NUMBER = "UPDATE EMPLOYEE SET PHONE_NUMBER = ? WHERE ID = ?";
    public static final String EMPLOYEE_UPDATE_ADDRESS = "UPDATE EMPLOYEE SET ADDRESS = ? WHERE ID = ?";
    public static final String EMPLOYEE_UPDATE_HOURLY_WAGE = "UPDATE EMPLOYEE SET HOURLY_WAGE = ? WHERE ID = ?";
    public static final String EMPLOYEE_UPDATE_STATUS = "UPDATE EMPLOYEE SET STATUS = ? WHERE ID = ?";
    public static final String EMPLOYEE_DELETE = "UPDATE EMPLOYEE SET IS_DELETED = 1 WHERE ID = ?";

    // Invoice queries
    public static final String INVOICE_FIND_BY_ID = "SELECT * FROM INVOICE WHERE ID = ?";
    public static final String INVOICE_FIND_BY_STORE_ID = "SELECT * FROM INVOICE WHERE STORE_ID = ?";
    public static final String INVOICE_FIND_BY_MEMBER_ID = "SELECT * FROM INVOICE WHERE MEMBER_ID = ?";
    public static final String INVOICE_FIND_BY_PAYMENT_ID = "SELECT * FROM INVOICE WHERE PAYMENT_ID = ?";
    public static final String INVOICE_FIND_BY_STATUS = "SELECT * FROM INVOICE WHERE STATUS = ?";
    public static final String INVOICE_FIND_BY_EMPLOYEE_ID = "SELECT * FROM INVOICE WHERE EMPLOYEE_ID = ?";
    public static final String INVOICE_FIND_ALL = "SELECT * FROM INVOICE";

    public static final String INVOICE_CREATE = "{CALL PRC_INITIATE_INVOICE(?,?,?,?,?,?)}"; // (STORE_ID, MEMBER_ID, PAYMENT_ID, EMPLOYEE_ID, POINT_USED, OUT INVOICE_ID)
    public static final String INVOICE_ADD_ITEM = "{CALL PRC_ADD_ITEM_TO_INVOICE(?,?,?)}"; // (INVOICE_ID, PRODUCT_ID, QUANTITY_SOLD)
    public static final String INVOICE_CALCULATE = "{CALL PRC_CALC_TOTAL(?)}"; // (INVOICE_ID)
    public static final String INVOICE_CANCEL = "{CALL PRC_CANCEL_INVOICE(?)}"; // (INVOICE_ID)

    // Invoice Detail queries
    public static final String INVOICE_DETAIL_FIND_BY_INVOICE_ID = "SELECT * FROM INVOICE_DETAIL WHERE INVOICE_ID = ?";

    // Member queries
    public static final String MEMBER_FIND_BY_ID = "SELECT * FROM MEMBER WHERE ID = ? AND IS_DELETED = 0";
    public static final String MEMBER_FIND_BY_PHONE_NUMBER = "SELECT * FROM MEMBER WHERE PHONE_NUMBER = ? AND IS_DELETED = 0";
    public static final String MEMBER_FIND_BY_EMAIL = "SELECT * FROM MEMBER WHERE EMAIL = ? AND IS_DELETED = 0";
    public static final String MEMBER_FIND_ALL = "SELECT * FROM MEMBER WHERE IS_DELETED = 0";

    public static final String MEMBER_CREATE = "INSERT INTO MEMBER(NAME, PHONE_NUMBER, EMAIL, REGISTRATION_TIME, LOYALTY_POINTS) VALUES(?, ?, ?, SYSTIMESTAMP, 0)";
    public static final String MEMBER_UPDATE_NAME = "UPDATE MEMBER SET NAME = ? WHERE ID = ?";
    public static final String MEMBER_UPDATE_PHONE_NUMBER = "UPDATE MEMBER SET PHONE_NUMBER = ? WHERE ID = ?";
    public static final String MEMBER_UPDATE_EMAIL = "UPDATE MEMBER SET EMAIL = ? WHERE ID = ?";
    public static final String MEMBER_DELETE = "UPDATE MEMBER SET IS_DELETED = 1 WHERE ID = ?";

    // Paycheck queries
    public static final String PAYCHECK_FIND_BY_ID = "SELECT * FROM PAYCHECK WHERE ID = ?";
    public static final String PAYCHECK_FIND_BY_EMPLOYEE_ID = "SELECT * FROM PAYCHECK WHERE EMPLOYEE_ID = ?";
    public static final String PAYCHECK_FIND_ALL = "SELECT * FROM PAYCHECK";

    public static final String PAYCHECK_CREATE = "{CALL PRC_CALC_PAYCHECK(?,?,?,?,?)}"; // (EMPLOYEE_ID, GROSS_AMOUNT, DEDUCTIONS, OUT NET_AMOUNT, OUT PC_ID)
    public static final String PAYCHECK_CREATE_FOR_ALL = "{CALL PRC_GENERATE_PAYCHECKS(?,?,?)}"; // (YEAR, MONTH, BATCH_ID)

    // Payment method queries
    public static final String PAYMENT_FIND_BY_ID = "SELECT * FROM PAYMENT WHERE ID = ? AND IS_DELETED = 0";
    public static final String PAYMENT_FIND_BY_NAME = "SELECT * FROM PAYMENT WHERE NAME = ? AND IS_DELETED = 0";
    public static final String PAYMENT_FIND_ALL = "SELECT * FROM PAYMENT WHERE IS_DELETED = 0";

    public static final String PAYMENT_CREATE = "INSERT INTO PAYMENT(NAME) VALUES(?)";
    public static final String PAYMENT_UPDATE_NAME = "UPDATE PAYMENT SET NAME = ? WHERE ID = ?";
    public static final String PAYMENT_DELETE = "UPDATE PAYMENT SET IS_DELETED = 1 WHERE ID = ?";

    // Point update log queries
    public static final String POINT_UPDATE_LOG_FIND_BY_ID = "SELECT * FROM POINT_UPDATE_LOG WHERE ID = ?";
    public static final String POINT_UPDATE_LOG_FIND_BY_MEMBER_ID = "SELECT * FROM POINT_UPDATE_LOG WHERE MEMBER_ID = ?";
    public static final String POINT_UPDATE_LOG_FIND_BY_INVOICE_ID = "SELECT * FROM POINT_UPDATE_LOG WHERE INVOICE_ID = ?";

    // Product queries
    public static final String PRODUCT_FIND_BY_ID = "SELECT * FROM PRODUCT WHERE ID = ? AND IS_DELETED = 0";
    public static final String PRODUCT_FIND_BY_NAME = "SELECT * FROM PRODUCT WHERE NAME = ? AND IS_DELETED = 0";
    public static final String PRODUCT_FIND_BY_CATEGORY_ID = "SELECT * FROM PRODUCT WHERE CATEGORY_ID = ? AND IS_DELETED = 0";
    public static final String PRODUCT_FIND_ALL = "SELECT * FROM PRODUCT WHERE IS_DELETED = 0";

    public static final String PRODUCT_CREATE = "INSERT INTO PRODUCT(ID, NAME, UNIT_PRICE, CATEGORY_ID) VALUES(?, ?, ?, ?)";
    public static final String PRODUCT_UPDATE_NAME = "UPDATE PRODUCT SET NAME = ? WHERE ID = ?";
    public static final String PRODUCT_UPDATE_UNIT_PRICE = "UPDATE PRODUCT SET UNIT_PRICE = ? WHERE ID = ?";
    public static final String PRODUCT_UPDATE_CATEGORY_ID = "UPDATE PRODUCT SET CATEGORY_ID = ? WHERE ID = ?";
    public static final String PRODUCT_DELETE = "UPDATE PRODUCT SET IS_DELETED = 1 WHERE ID = ?";

    // Promotion queries
    public static final String PROMOTION_FIND_BY_ID = "SELECT * FROM PROMOTION WHERE ID = ? AND IS_DELETED = 0";
    public static final String PROMOTION_FIND_BY_NAME = "SELECT * FROM PROMOTION WHERE NAME = ? AND IS_DELETED = 0";
    public static final String PROMOTION_FIND_BY_PRODUCT_ID = "SELECT * FROM PROMOTION WHERE PRODUCT_ID = ? AND IS_DELETED = 0";
    public static final String PROMOTION_FIND_ALL = "SELECT * FROM PROMOTION WHERE IS_DELETED = 0";

    public static final String PROMOTION_CREATE =
            "INSERT INTO PROMOTION(NAME, START_TIME, END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID, PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String PROMOTION_UPDATE_NAME = "UPDATE PROMOTION SET NAME = ? WHERE ID = ?";
    public static final String PROMOTION_UPDATE_START_TIME = "UPDATE PROMOTION SET START_TIME = ? WHERE ID = ?";
    public static final String PROMOTION_UPDATE_END_TIME = "UPDATE PROMOTION SET END_TIME = ? WHERE ID = ?";
    public static final String PROMOTION_UPDATE_PRODUCT_ID = "UPDATE PROMOTION SET PRODUCT_ID = ? WHERE ID = ?";
    public static final String PROMOTION_UPDATE_MINIMUM_PURCHASE_QUANTITY = "UPDATE PROMOTION SET MINIMUM_PURCHASE_QUANTITY = ? WHERE ID = ?";
    public static final String PROMOTION_UPDATE_PROMO_PRODUCT_ID = "UPDATE PROMOTION SET PROMO_PRODUCT_ID = ? WHERE ID = ?";
    public static final String PROMOTION_UPDATE_PROMO_PRODUCT_QUANTITY = "UPDATE PROMOTION SET PROMO_PRODUCT_QUANTITY = ? WHERE ID = ?";
    public static final String PROMOTION_UPDATE_DISCOUNT_RATE = "UPDATE PROMOTION SET DISCOUNT_RATE = ? WHERE ID = ?";
    public static final String PROMOTION_DELETE = "UPDATE PROMOTION SET IS_DELETED = 1 WHERE ID = ?";

    // Role queries
    public static final String ROLE_FIND_BY_ID = "SELECT * FROM ROLE WHERE ID = ? AND IS_DELETED = 0";
    public static final String ROLE_FIND_BY_NAME = "SELECT * FROM ROLE WHERE NAME = ? AND IS_DELETED = 0";
    public static final String ROLE_FIND_ALL = "SELECT * FROM ROLE WHERE IS_DELETED = 0";

    public static final String ROLE_CREATE = "INSERT INTO ROLE(NAME) VALUES(?)";
    public static final String ROLE_UPDATE_NAME = "UPDATE ROLE SET NAME = ? WHERE ID = ?";
    public static final String ROLE_DELETE = "UPDATE ROLE SET IS_DELETED = 1 WHERE ID = ?";

    // Shift report queries
    public static final String SHIFT_REPORT_FIND_BY_ID = "SELECT * FROM SHIFT_REPORT WHERE ID = ?";
    public static final String SHIFT_REPORT_FIND_BY_STORE_ID = "SELECT * FROM SHIFT_REPORT WHERE STORE_ID = ?";
    public static final String SHIFT_REPORT_FIND_BY_EMPLOYEE_ID = "SELECT * FROM SHIFT_REPORT WHERE EMPLOYEE_ID = ?";
    public static final String SHIFT_REPORT_FIND_ALL = "SELECT * FROM SHIFT_REPORT";

    public static final String SHIFT_REPORT_CREATE = "{CALL PRC_INITIATE_SHIFT(?,?,?)}"; // (STORE_ID, EMPLOYEE_ID, OUT ID)
    public static final String SHIFT_REPORT_CLOSE = "{CALL PRC_END_SHIFT(?)}"; // (SHIFT_REPORT_ID)

    // Store queries
    public static final String STORE_FIND_BY_ID = "SELECT * FROM STORE WHERE ID = ?";
    public static final String STORE_FIND_BY_MANAGER_ID = "SELECT * FROM STORE WHERE MANAGER_ID = ?";
    public static final String STORE_FIND_ALL = "SELECT * FROM STORE";

    public static final String STORE_CREATE = "INSERT INTO STORE(NAME, ADDRESS, MANAGER_ID) VALUES(?, ?, ?, ?)";
    public static final String STORE_UPDATE_NAME = "UPDATE STORE SET NAME = ? WHERE ID = ?";
    public static final String STORE_UPDATE_ADDRESS = "UPDATE STORE SET ADDRESS = ? WHERE ID = ?";

    // Token queries
    public static final String TOKEN_FIND_BY_ID = "SELECT * FROM TOKEN WHERE ID = ?";
    public static final String TOKEN_FIND_BY_USERNAME = "SELECT * FROM TOKEN WHERE USERNAME = ?";
    public static final String TOKEN_FIND_BY_VALUE = "SELECT * FROM TOKEN WHERE VALUE = ?";

    public static final String TOKEN_CREATE = "INSERT INTO TOKEN(USERNAME, VALUE, EXPIRES_AT, ISSUED_AT, STATUS) VALUES(?, ?, ?, ?, ?)";
    public static final String TOKEN_UPDATE_VALUE = "UPDATE TOKEN SET VALUE = ? WHERE ID = ?";
    public static final String TOKEN_UPDATE_STATUS = "UPDATE TOKEN SET STATUS = ? WHERE VALUE = ?";
}