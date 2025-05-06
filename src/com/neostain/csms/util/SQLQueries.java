package com.neostain.csms.util;

/**
 * Class chứa tất cả các SQL query sử dụng trong ứng dụng
 * Giúp quản lý tập trung các câu truy vấn và dễ dàng chỉnh sửa khi cần
 */
public class SQLQueries {
    // Account queries
    public static final String ACCOUNT_FIND_BY_EMPLOYEE_ID = "SELECT * FROM ACCOUNT WHERE EMPLOYEE_ID = ?";
    public static final String ACCOUNT_FIND_BY_ID = "SELECT * FROM ACCOUNT WHERE ACCOUNT_ID = ?";
    public static final String ACCOUNT_FIND_BY_USERNAME = "SELECT * FROM ACCOUNT WHERE USERNAME = ?";
    public static final String ACCOUNT_FIND_BY_ROLE_ID = "SELECT * FROM ACCOUNT WHERE ROLE_ID = ?";
    public static final String ACCOUNT_FIND_BY_STATUS = "SELECT * FROM ACCOUNT WHERE ACCOUNT_STATUS = ?";
    public static final String ACCOUNT_FIND_ALL = "SELECT * FROM ACCOUNT";
    public static final String ACCOUNT_GET_PASSWORD_HASH = "SELECT PASSWORD_HASH FROM ACCOUNT WHERE USERNAME = ?";
    public static final String ACCOUNT_GET_ROLE_NAME = "SELECT r.ROLE_NAME FROM ACCOUNT a JOIN ROLE r ON a.ROLE_ID = r.ROLE_ID WHERE a.username = ?";

    public static final String ACCOUNT_CREATE = "INSERT INTO ACCOUNT(" +
            "EMPLOYEE_ID, " +
            "USERNAME, " +
            "PASSWORD_HASH, " +
            "ROLE_ID) VALUES (?,?,?,?)";
    public static final String ACCOUNT_UPDATE_PASSWORD_HASH = "UPDATE ACCOUNT SET PASSWORD_HASH = ? WHERE USERNAME = ?";
    public static final String ACCOUNT_UPDATE_STATUS = "UPDATE ACCOUNT SET PASSWORD_HASH = ? WHERE USERNAME = ?";
    public static final String ACCOUNT_DELETE = "DELETE FROM ACCOUNT WHERE USERNAME = ?";

    // Assignment queries
    public static final String ASSIGNMENT_FIND_BY_ID = "SELECT * FROM ASSIGNMENT WHERE ASSIGNMENT_ID = ?";
    public static final String ASSIGNMENT_FIND_BY_EMPLOYEE_ID = "SELECT * FROM ASSIGNMENT WHERE EMPLOYEE_ID = ?";
    public static final String ASSIGNMENT_FIND_BY_STORE_ID = "SELECT * FROM ASSIGNMENT WHERE STORE_ID = ?";
    public static final String ASSIGNMENT_FIND_BY_RANGE = "SELECT * FROM ASSIGNMENT WHERE SYSTIMESTAMP IS BETWEEN START_TIME AND END_TIME AND EMPLOYEE_ID = ? AND STORE_ID = ?";
    public static final String ASSIGNMENT_FIND_ALL = "SELECT * FROM ASSIGNMENT";

    public static final String ASSIGNMENT_CREATE = "INSERT INTO ASSIGNMENT(STORE_ID, EMPLOYEE_ID, START_TIME, END_TIME) VALUES(?,?,?,?)";
    public static final String ASSIGNMENT_UPDATE_START_TIME = "UPDATE ASSIGNMENT SET START_TIME = ? WHERE ASSIGNMENT_ID = ?";
    public static final String ASSIGNMENT_UPDATE_END_TIME = "UPDATE ASSIGNMENT SET END_TIME = ? WHERE ASSIGNMENT_ID = ?";
    public static final String ASSIGNMENT_DELETE = "DELETE FROM ASSIGNMENT WHERE ASSIGNMENT_ID = ?";

    // Category queries
    public static final String CATEGORY_FIND_BY_ID = "SELECT * FROM CATEGORY WHERE CATEGORY_ID = ?";
    public static final String CATEGORY_FIND_BY_NAME = "SELECT * FROM CATEGORY WHERE CATEGORY_NAME = ?";
    public static final String CATEGORY_FIND_ALL = "SELECT * FROM CATEGORY";

    public static final String CATEGORY_CREATE = "INSERT INTO CATEGORY(CATEGORY_NAME) VALUES(?)";
    public static final String CATEGORY_UPDATE_NAME = "UPDATE CATEGORY SET CATEGORY_NAME = ? WHERE CATEGORY_ID = ?";
    public static final String CATEGORY_DELETE = "DELETE FROM CATEGORY WHERE CATEGORY_ID = ?";

    // Employee queries
    public static final String EMPLOYEE_FIND_BY_ID = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
    public static final String EMPLOYEE_FIND_BY_NAME = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_NAME = ?";
    public static final String EMPLOYEE_FIND_BY_POSITION = "SELECT * FROM EMPLOYEE WHERE POSITION = ?";
    public static final String EMPLOYEE_FIND_BY_STATUS = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_STATUS = ?";
    public static final String EMPLOYEE_FIND_ALL = "SELECT * FROM EMPLOYEE";

    public static final String EMPLOYEE_CREATE = "INSERT INTO EMPLOYEE(" +
            "EMPLOYEE_NAME, " +
            "POSITION, " +
            "EMAIL, " +
            "PHONE_NUMBER, " +
            "ADDRESS, " +
            "HOURLY_WAGE) VALUES (?,?,?,?,?,?)";
    public static final String EMPLOYEE_UPDATE_NAME = "UPDATE EMPLOYEE SET EMPLOYEE_NAME = ? WHERE EMPLOYEE_ID = ?";
    public static final String EMPLOYEE_UPDATE_POSITION = "UPDATE EMPLOYEE SET POSITION = ? WHERE EMPLOYEE_ID = ?";
    public static final String EMPLOYEE_UPDATE_EMAIL = "UPDATE EMPLOYEE SET EMAIL = ? WHERE EMPLOYEE_ID = ?";
    public static final String EMPLOYEE_UPDATE_PHONE_NUMBER = "UPDATE EMPLOYEE SET PHONE_NUMBER = ? WHERE EMPLOYEE_ID = ?";
    public static final String EMPLOYEE_UPDATE_ADDRESS = "UPDATE EMPLOYEE SET ADDRESS = ? WHERE EMPLOYEE_ID = ?";
    public static final String EMPLOYEE_UPDATE_HOURLY_WAGE = "UPDATE EMPLOYEE SET HOURLY_WAGE = ? WHERE EMPLOYEE_ID = ?";
    public static final String EMPLOYEE_UPDATE_STATUS = "UPDATE EMPLOYEE SET EMPLOYEE_STATUS = ? WHERE EMPLOYEE_ID = ?";
    public static final String EMPLOYEE_DELETE = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";

    // Invoice queries
    public static final String INVOICE_FIND_BY_ID = "SELECT * FROM INVOICE WHERE INVOICE_ID = ?";
    public static final String INVOICE_FIND_BY_STORE_ID = "SELECT * FROM INVOICE WHERE STORE_ID = ?";
    public static final String INVOICE_FIND_BY_MEMBER_ID = "SELECT * FROM INVOICE WHERE MEMBER_ID = ?";
    public static final String INVOICE_FIND_BY_PAYMENT_ID = "SELECT * FROM INVOICE WHERE PAYMENT_ID = ?";
    public static final String INVOICE_FIND_BY_STATUS = "SELECT * FROM INVOICE WHERE INVOICE_STATUS = ?";
    public static final String INVOICE_FIND_BY_EMPLOYEE_ID = "SELECT * FROM INVOICE WHERE EMPLOYEE_ID = ?";
    public static final String INVOICE_FIND_ALL = "SELECT * FROM INVOICE";

    public static final String INVOICE_CREATE = "{CALL PRC_INITIATE_INVOICE(?,?,?,?,?)}"; // Mã cửa hàng, mã thành viên, mã phương thức thanh toán, mã nhân viên, mã hóa đơn (Output)
    public static final String INVOICE_ADD_ITEM = "{CALL PRC_ADD_ITEM_TO_INVOICE(?,?,?)}"; // Mã hóa đơn, mã sản phẩm, số lượng mua
    public static final String INVOICE_CALCULATE = "{CALL PRC_CALC_TOTAL(?,?)}"; // Nếu không tích điểm tham số thứ 2 sẽ bằng 0
    public static final String INVOICE_CANCEL = "{CALL PRC_CANCEL_INVOICE(?)}"; // Hóa đơn đã tích điểm không được hủy

    // Invoice Detail queries
    public static final String INVOICE_DETAIL_FIND_BY_INVOICE_ID = "SELECT * FROM INVOICE_DETAIL WHERE INVOICE_ID = ?";

    // Member queries
    public static final String MEMBER_FIND_BY_ID = "SELECT * FROM MEMBER WHERE MEMBER_ID = ?";
    public static final String MEMBER_FIND_BY_PHONE_NUMBER = "SELECT * FROM MEMBER WHERE PHONE_NUMBER = ?";
    public static final String MEMBER_FIND_BY_EMAIL = "SELECT * FROM MEMBER WHERE EMAIL = ?";
    public static final String MEMBER_FIND_ALL = "SELECT * FROM MEMBER";

    public static final String MEMBER_CREATE = "INSERT INTO MEMBER(STORE_ID, EMPLOYEE_ID) VALUES(?,?,?)";
    public static final String MEMBER_UPDATE_NAME = "UPDATE MEMBER SET MEMBER_NAME = ? WHERE MEMBER_ID = ?";
    public static final String MEMBER_UPDATE_PHONE_NUMBER = "UPDATE MEMBER SET PHONE_NUMBER = ? WHERE MEMBER_ID = ?";
    public static final String MEMBER_UPDATE_EMAIL = "UPDATE MEMBER SET EMAIL = ? WHERE MEMBER_ID = ?";
    public static final String MEMBER_DELETE_BY_ID = "DELETE FROM MEMBER WHERE MEMBER_ID = ?";
    public static final String MEMBER_DELETE_BY_PHONE_NUMBER = "DELETE FROM MEMBER WHERE PHONE_NUMBER = ?";

    // Paycheck queries
    public static final String PAYCHECK_FIND_BY_ID = "SELECT * FROM PAYCHECK WHERE PAYCHECK_ID = ?";
    public static final String PAYCHECK_FIND_BY_EMPLOYEE_ID = "SELECT * FROM PAYCHECK WHERE EMPLOYEE_ID = ?";
    public static final String PAYCHECK_FIND_ALL = "SELECT * FROM PAYCHECK";

    public static final String PAYCHECK_CREATE = "{CALL PRC_CALC_PAYCHECK(?,?,?,?,?)}";
    public static final String PAYCHECK_CREATE_FOR_ALL = "{CALL PRC_GENERATE_PAYCHECKS(?,?)}";

    // Payment queries
    public static final String PAYMENT_FIND_BY_ID = "SELECT * FROM PAYMENT WHERE PAYMENT_ID = ?";
    public static final String PAYMENT_FIND_BY_NAME = "SELECT * FROM PAYMENT WHERE PAYMENT_NAME = ?";
    public static final String PAYMENT_FIND_ALL = "SELECT * FROM PAYMENT";

    public static final String PAYMENT_CREATE = "INSERT INTO PAYMENT(PAYMENT_NAME) VALUES(?)";
    public static final String PAYMENT_UPDATE_NAME = "UPDATE PAYMENT SET PAYMENT_NAME = ? WHERE PAYMENT_ID = ?";
    public static final String PAYMENT_DELETE = "DELETE FROM PAYMENT WHERE PAYMENT_ID = ?";

    // Point update log queries
    public static final String POINT_UPDATE_LOG_FIND_BY_ID = "SELECT * FROM POINT_UPDATE_LOG WHERE POINT_UPDATE_LOG_ID = ?";
    public static final String POINT_UPDATE_LOG_FIND_BY_MEMBER_ID = "SELECT * FROM POINT_UPDATE_LOG WHERE MEMBER_ID = ?";
    public static final String POINT_UPDATE_LOG_FIND_BY_INVOICE_ID = "SELECT * FROM POINT_UPDATE_LOG WHERE INVOICE_ID = ?";

    // Product queries
    public static final String PRODUCT_FIND_BY_ID = "SELECT * FROM PRODUCT WHERE PRODUCT_ID = ?";
    public static final String PRODUCT_FIND_BY_NAME = "SELECT * FROM PRODUCT WHERE PRODUCT_NAME = ?";
    public static final String PRODUCT_FIND_BY_CATEGORY_ID = "SELECT * FROM PRODUCT WHERE CATEGORY_ID = ?";
    public static final String PRODUCT_FIND_ALL = "SELECT * FROM PRODUCT";

    public static final String PRODUCT_CREATE = "INSERT INTO PRODUCT(PRODUCT_ID, PRODUCT_NAME, UNIT_PRICE, CATEGORY_ID) VALUES(?,?,?,?)";
    public static final String PRODUCT_UPDATE_NAME = "UPDATE PRODUCT SET PRODUCT_NAME = ? WHERE PRODUCT_ID = ?";
    public static final String PRODUCT_UPDATE_UNIT_PRICE = "UPDATE PRODUCT SET UNIT_PRICE = ? WHERE PRODUCT_ID = ?";
    public static final String PRODUCT_UPDATE_CATEGORY_ID = "UPDATE PRODUCT SET CATEGORY_ID = ? WHERE PRODUCT_ID = ?";
    public static final String PRODUCT_DELETE = "DELETE FROM PRODUCT WHERE PRODUCT_ID = ?";

    // Promotion queries
    public static final String PROMOTION_FIND_BY_ID = "SELECT * FROM PROMOTION WHERE PROMOTION_ID = ?";
    public static final String PROMOTION_FIND_BY_NAME = "SELECT * FROM PROMOTION WHERE PROMOTION_NAME = ?";
    public static final String PROMOTION_FIND_RANGE = "SELECT * FROM PROMOTION WHERE SYSTIMESTAMP IS BETWEEN ? AND ?";
    public static final String PROMOTION_FIND_BY_PRODUCT_ID = "SELECT * FROM PROMOTION WHERE PRODUCT_ID = ?";
    public static final String PROMOTION_FIND_ALL = "SELECT * FROM PROMOTION";

    public static final String PROMOTION_CREATE = "INSERT INTO PROMOTION(PROMOTION_NAME, PROMOTION_START_TIME, PROMOTION_END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID, PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE) VALUES(?,?,?,?,?,?,?,?)";
    public static final String PROMOTION_UPDATE_NAME = "UPDATE PROMOTION SET PROMOTION_NAME = ? WHERE PROMOTION_ID = ?";
    public static final String PROMOTION_UPDATE_START_TIME = "UPDATE PROMOTION SET PROMOTION_START_TIME = ? WHERE PROMOTION_ID = ?";
    public static final String PROMOTION_UPDATE_END_TIME = "UPDATE PROMOTION SET PROMOTION_END_TIME = ? WHERE PROMOTION_ID = ?";
    public static final String PROMOTION_UPDATE_PRODUCT_ID = "UPDATE PROMOTION SET PRODUCT_ID = ? WHERE PROMOTION_ID = ?";
    public static final String PROMOTION_UPDATE_MINIMUM_PURCHASE_QUANTITY = "UPDATE PROMOTION SET MINIMUM_PURCHASE_QUANTITY = ? WHERE PROMOTION_ID = ?";
    public static final String PROMOTION_UPDATE_PROMO_PRODUCT_ID = "UPDATE PROMOTION SET PROMO_PRODUCT_ID = ? WHERE PROMOTION_ID = ?";
    public static final String PROMOTION_UPDATE_PROMO_PRODUCT_QUANTITY = "UPDATE PROMOTION SET PROMO_PRODUCT_QUANTITY = ? WHERE PROMOTION_ID = ?";
    public static final String PROMOTION_UPDATE_DISCOUNT_RATE = "UPDATE PROMOTION SET DISCOUNT_RATE = ? WHERE PROMOTION_ID = ?";

    public static final String PROMOTION_DELETE = "DELETE FROM PROMOTION WHERE PROMOTION_ID = ?";

    // Role queries
    public static final String ROLE_FIND_BY_ID = "SELECT * FROM ROLE WHERE ROLE_ID = ?";
    public static final String ROLE_FIND_BY_NAME = "SELECT * FROM ROLE WHERE ROLE_NAME = ?";
    public static final String ROLE_FIND_ALL = "SELECT * FROM ROLE";

    public static final String ROLE_CREATE = "INSERT INTO ROLE(ROLE_NAME) VALUES(?)";
    public static final String ROLE_UPDATE_NAME = "UPDATE ROLE SET ROLE_NAME = ? WHERE ROLE_ID = ?";
    public static final String ROLE_DELETE = "DELETE FROM ROLE WHERE ROLE_ID = ?";

    // Shift report queries
    public static final String SHIFT_REPORT_FIND_BY_ID = "SELECT * FROM SHIFT_REPORT WHERE SHIFT_REPORT_ID = ?";
    public static final String SHIFT_REPORT_FIND_BY_STORE_ID = "SELECT * FROM SHIFT_REPORT WHERE STORE_ID = ?";
    public static final String SHIFT_REPORT_FIND_BY_EMPLOYEE_ID = "SELECT * FROM SHIFT_REPORT WHERE EMPLOYEE_ID = ?";
    public static final String SHIFT_REPORT_FIND_ALL = "SELECT * FROM SHIFT_REPORT";

    public static final String SHIFT_REPORT_CREATE = "{CALL PRC_INITIATE_SHIFT(?,?,?)}";
    public static final String SHIFT_REPORT_CLOSE = "{CALL PRC_INITIATE_SHIFT(?)}";

    // Store queries
    public static final String STORE_FIND_BY_ID = "SELECT * FROM STORE WHERE STORE_ID = ?";
    public static final String STORE_FIND_BY_MANAGER_ID = "SELECT * FROM STORE WHERE MANAGER_ID = ?";
    public static final String STORE_FIND_ALL = "SELECT * FROM STORE";

    public static final String STORE_CREATE = "INSERT INTO STORE(STORE_NAME, ADDRESS, MANAGER_ID) VALUES(?,?,?)";
    public static final String STORE_UPDATE_STORE_NAME = "UPDATE STORE SET STORE_NAME = ? WHERE STORE_ID = ?";
    public static final String STORE_UPDATE_ADDRESS = "UPDATE STORE SET ADDRESS = ? WHERE STORE_ID = ?";


    // Token queries
    public static final String TOKEN_FIND_BY_ID = "SELECT * FROM TOKEN WHERE TOKEN_ID = ?";
    public static final String TOKEN_FIND_BY_USERNAME = "SELECT * FROM TOKEN WHERE USERNAME = ?";
    public static final String TOKEN_FIND_BY_VALUE = "SELECT * FROM TOKEN WHERE TOKEN_VALUE = ?";

    public static final String TOKEN_CREATE = "INSERT INTO TOKEN(USERNAME, TOKEN_VALUE, EXPIRES_AT, ISSUED_AT, TOKEN_STATUS) VALUES(?,?,?,?,?)";
    public static final String TOKEN_UPDATE_VALUE = "UPDATE TOKEN SET TOKEN_VALUE = ? WHERE TOKEN_ID = ?";
    public static final String TOKEN_UPDATE_STATUS = "UPDATE TOKEN SET TOKEN_STATUS = ? WHERE TOKEN_ID = ?";
}
