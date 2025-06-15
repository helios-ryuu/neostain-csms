----------------------------------------------------------------
-- 1. Doanh thu theo ngày
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_DAY_REVENUE(
    F_DATE IN INVOICE.CREATION_TIME%TYPE
) RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT SUM(TOTAL_DUE)
    INTO V_RESULT
    FROM INVOICE
    WHERE TRUNC(CREATION_TIME) = TRUNC(F_DATE);

    RETURN V_RESULT;
END FNC_DAY_REVENUE;
/
----------------------------------------------------------------
-- 2. Số hóa đơn theo ngày
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_DAY_INVOICE_COUNT(
    F_DATE IN INVOICE.CREATION_TIME%TYPE
) RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO V_RESULT
    FROM INVOICE
    WHERE TRUNC(CREATION_TIME) = TRUNC(F_DATE);

    RETURN V_RESULT;
END FNC_DAY_INVOICE_COUNT;
/
----------------------------------------------------------------
-- 3. Tổng thành viên
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_TOTAL_MEMBERS RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO V_RESULT
    FROM MEMBER
    WHERE IS_DELETED = 0;

    RETURN V_RESULT;
END FNC_TOTAL_MEMBERS;
/
----------------------------------------------------------------
-- 4. Tổng thành viên VIP (điểm > 1000)
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_TOTAL_VIP_MEMBERS RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO V_RESULT
    FROM MEMBER
    WHERE IS_DELETED = 0
      AND LOYALTY_POINTS > 1000;

    RETURN V_RESULT;
END FNC_TOTAL_VIP_MEMBERS;
/
----------------------------------------------------------------
-- 5. Tổng sản phẩm của cửa hàng
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_TOTAL_PRODUCTS(
    F_STORE_ID IN VARCHAR2
) RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    /* Giả sử bảng INVENTORY có cột STORE_ID */
    SELECT COUNT(*)
    INTO V_RESULT
    FROM INVENTORY
    WHERE STORE_ID = F_STORE_ID;

    RETURN V_RESULT;
END FNC_TOTAL_PRODUCTS;
/
----------------------------------------------------------------
-- 6. Tổng nhân viên của cửa hàng (theo STORE_ID hoặc BRANCH_ID)
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_TOTAL_EMPLOYEES(
    F_STORE_ID IN CHAR
) RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT COUNT(DISTINCT EMP_ID)
    INTO V_RESULT
    FROM (SELECT MANAGER_ID AS EMP_ID
          FROM STORE
          WHERE ID = F_STORE_ID
          UNION ALL
          SELECT ID AS EMP_ID
          FROM EMPLOYEE
          WHERE MANAGER_ID = (SELECT MANAGER_ID AS EMP_ID
                              FROM STORE
                              WHERE ID = F_STORE_ID));

    RETURN V_RESULT;
END FNC_TOTAL_EMPLOYEES;
/
----------------------------------------------------------------
-- 7. Số hóa đơn đã hủy
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_CANCELED_INVOICES RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO V_RESULT
    FROM INVOICE
    WHERE STATUS = N'ĐÃ HỦY';

    RETURN V_RESULT;
END FNC_CANCELED_INVOICES;
/
----------------------------------------------------------------
-- 8. Số hóa đơn chưa hoàn thành
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_UNCOMPLETED_INVOICES RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO V_RESULT
    FROM INVOICE
    WHERE STATUS = N'CHƯA HOÀN THÀNH';

    RETURN V_RESULT;
END FNC_UNCOMPLETED_INVOICES;
/
----------------------------------------------------------------
-- 9. Số hóa đơn 30 ngày gần nhất (đã hoàn thành)
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_TOTAL_INVOICES_LAST_30D RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO V_RESULT
    FROM INVOICE
    WHERE CREATION_TIME >= TRUNC(SYSDATE) - 30
      AND STATUS NOT IN (N'ĐÃ HỦY', N'CHƯA HOÀN THÀNH');

    RETURN V_RESULT;
END FNC_TOTAL_INVOICES_LAST_30D;
/
----------------------------------------------------------------
-- 10. Doanh thu 30 ngày gần nhất (đã hoàn thành)
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_TOTAL_REVENUE_LAST_30D RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT SUM(TOTAL_DUE)
    INTO V_RESULT
    FROM INVOICE
    WHERE CREATION_TIME >= TRUNC(SYSDATE) - 30
      AND STATUS NOT IN (N'ĐÃ HỦY', N'CHƯA HOÀN THÀNH');

    RETURN V_RESULT;
END FNC_TOTAL_REVENUE_LAST_30D;
/
----------------------------------------------------------------
-- 11. Số hóa đơn đang yêu cầu hủy
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION FNC_CANCEL_REQUESTED_INVOICES RETURN NUMBER
AS
    V_RESULT NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO V_RESULT
    FROM INVOICE
    WHERE STATUS = N'ĐANG YÊU CẦU HỦY';

    RETURN V_RESULT;
END FNC_CANCEL_REQUESTED_INVOICES;
/