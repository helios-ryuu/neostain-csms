CREATE OR REPLACE PROCEDURE PRC_INITIATE_SHIFT(
    P_STORE_ID IN CSMS_ADMIN.SHIFT_REPORT.STORE_ID%type,
    P_EMPLOYEE_ID IN CSMS_ADMIN.SHIFT_REPORT.EMPLOYEE_ID%type,
    P_SHIFT_ID OUT CSMS_ADMIN.SHIFT_REPORT.ID%type
)
AS
BEGIN
    INSERT INTO SHIFT_REPORT(STORE_ID, EMPLOYEE_ID)
    VALUES (P_STORE_ID, P_EMPLOYEE_ID)
    RETURNING ID
        INTO P_SHIFT_ID;
END PRC_INITIATE_SHIFT;
/

CREATE OR REPLACE PROCEDURE PRC_END_SHIFT(
    P_SHIFT_ID IN SHIFT_REPORT.ID%TYPE
)
AS
    v_start   TIMESTAMP;
    v_end     TIMESTAMP := SYSTIMESTAMP;
    v_cash    NUMBER    := 0;
    v_ewallet NUMBER    := 0;
    v_bank    NUMBER    := 0;
    v_count   NUMBER    := 0;
BEGIN
    -- 1) Lấy và cập nhật END_TIME
    SELECT START_TIME
    INTO v_start
    FROM SHIFT_REPORT
    WHERE ID = P_SHIFT_ID
      AND END_TIME IS NULL; -- đảm bảo chỉ chạy 1 lần

    UPDATE SHIFT_REPORT
    SET END_TIME = v_end
    WHERE ID = P_SHIFT_ID;

    -- 2) Tính tổng doanh thu & số giao dịch
    SELECT SUM(CASE WHEN PAYMENT_ID = 'P001' THEN TOTAL_DUE ELSE 0 END),
           SUM(CASE WHEN PAYMENT_ID = 'P002' THEN TOTAL_DUE ELSE 0 END),
           SUM(CASE WHEN PAYMENT_ID = 'P003' THEN TOTAL_DUE ELSE 0 END)
    INTO v_cash, v_ewallet, v_bank
    FROM INVOICE
    WHERE CREATION_TIME BETWEEN v_start AND v_end
      AND STATUS = 'Đã hoàn thành';

    SELECT COUNT(*)
    INTO v_count
    FROM INVOICE
    WHERE CREATION_TIME BETWEEN v_start AND v_end;

    -- 3) Cập nhật vào báo cáo đóng ca
    UPDATE SHIFT_REPORT
    SET CASH_REVENUE      = NVL(v_cash, 0),
        EWALLET_REVENUE   = NVL(v_ewallet, 0),
        BANK_REVENUE      = NVL(v_bank, 0),
        TRANSACTION_COUNT = v_count
    WHERE ID = P_SHIFT_ID;
END PRC_END_SHIFT;
/

CREATE OR REPLACE PROCEDURE PRC_INITIATE_INVOICE(
    P_STORE_ID IN CSMS_ADMIN.INVOICE.STORE_ID%type,
    P_MEMBER_ID IN CSMS_ADMIN.INVOICE.MEMBER_ID%type,
    P_PAYMENT_ID IN CSMS_ADMIN.INVOICE.PAYMENT_ID%type,
    P_EMPLOYEE_ID IN CSMS_ADMIN.INVOICE.EMPLOYEE_ID%type,
    P_POINT_USED IN CSMS_ADMIN.INVOICE.POINT_USED%type DEFAULT 0,
    P_INVOICE_ID OUT CSMS_ADMIN.INVOICE.ID%type
)
AS
BEGIN
    INSERT INTO INVOICE(STORE_ID, MEMBER_ID, PAYMENT_ID, EMPLOYEE_ID, POINT_USED)
    VALUES (P_STORE_ID, P_MEMBER_ID, P_PAYMENT_ID, P_EMPLOYEE_ID, P_POINT_USED)
    RETURNING ID
        INTO P_INVOICE_ID;
END PRC_INITIATE_INVOICE;
/

CREATE OR REPLACE PROCEDURE PRC_ADD_ITEM_TO_INVOICE(
    P_INVOICE_ID IN INVOICE_DETAIL.INVOICE_ID%TYPE,
    P_PRODUCT_ID IN INVOICE_DETAIL.PRODUCT_ID%TYPE,
    P_QUANTITY_SOLD IN INVOICE_DETAIL.QUANTITY_SOLD%TYPE
)
AS
    v_unit_price PRODUCT.UNIT_PRICE%TYPE;
    v_sets       NUMBER;
    v_free_qty   NUMBER;
BEGIN
    -- 1) Lấy đơn giá gốc của sản phẩm chính
    SELECT UNIT_PRICE
    INTO v_unit_price
    FROM PRODUCT
    WHERE ID = P_PRODUCT_ID;

    -- 2) Chèn hoặc cộng dồn sản phẩm chính (unit price gốc)
    UPDATE INVOICE_DETAIL
    SET QUANTITY_SOLD = QUANTITY_SOLD + P_QUANTITY_SOLD
    WHERE INVOICE_ID = P_INVOICE_ID
      AND PRODUCT_ID = P_PRODUCT_ID;
    IF SQL%ROWCOUNT = 0 THEN
        INSERT INTO INVOICE_DETAIL(INVOICE_ID, PRODUCT_ID, QUANTITY_SOLD, UNIT_PRICE)
        VALUES (P_INVOICE_ID, P_PRODUCT_ID, P_QUANTITY_SOLD, v_unit_price);
    END IF;

    -- 3) Cập nhật NET_AMOUNT cho sản phẩm chính
    UPDATE INVOICE
    SET NET_AMOUNT = NVL(NET_AMOUNT, 0) + v_unit_price * P_QUANTITY_SOLD
    WHERE ID = P_INVOICE_ID;

    -- 4) Xử lý khuyến mãi MUA X TẶNG Y hoặc GIẢM %
    FOR promo_rec IN (
        SELECT MINIMUM_PURCHASE_QUANTITY,
               PROMO_PRODUCT_ID,
               PROMO_PRODUCT_QUANTITY,
               DISCOUNT_RATE
        FROM PROMOTION
        WHERE PRODUCT_ID = P_PRODUCT_ID
          AND SYSDATE BETWEEN START_TIME AND END_TIME
        )
        LOOP
            -- Tính số bộ đủ điều kiện
            v_sets := FLOOR(P_QUANTITY_SOLD / promo_rec.MINIMUM_PURCHASE_QUANTITY);
            IF v_sets > 0 THEN

                -- 4.1) Xử lý GIẢM % (nếu có rate > 0 và promo_rec.PROMO_PRODUCT_ID IS NULL)
                IF promo_rec.DISCOUNT_RATE > 0 AND promo_rec.PROMO_PRODUCT_ID IS NULL THEN
                    -- Cộng discount cho sản phẩm chính
                    UPDATE INVOICE
                    SET DISCOUNT = NVL(DISCOUNT, 0)
                        + v_unit_price
                                       * promo_rec.MINIMUM_PURCHASE_QUANTITY
                                       * v_sets
                                       * promo_rec.DISCOUNT_RATE
                    WHERE ID = P_INVOICE_ID;
                END IF;

                -- 4.2) Xử lý TẶNG Y (nếu PROMO_PRODUCT_ID không NULL)
                IF promo_rec.PROMO_PRODUCT_ID IS NOT NULL THEN
                    v_free_qty := v_sets * promo_rec.PROMO_PRODUCT_QUANTITY;

                    -- Chèn hoặc cộng dồn hàng tặng,
                    -- UNIT_PRICE = 0 để không cộng vào NET_AMOUNT
                    INSERT INTO INVOICE_DETAIL(INVOICE_ID, PRODUCT_ID, QUANTITY_SOLD, UNIT_PRICE)
                    VALUES (P_INVOICE_ID, promo_rec.PROMO_PRODUCT_ID,
                            v_free_qty, 0);

                    -- Giá trị hàng tặng KHÔNG được cộng vào NET_AMOUNT
                END IF;
            END IF;
        END LOOP;
END PRC_ADD_ITEM_TO_INVOICE;
/

CREATE OR REPLACE PROCEDURE PRC_CALC_TOTAL(
    P_INVOICE_ID IN INVOICE.ID%TYPE
)
AS
    V_NET_AMOUNT INVOICE.NET_AMOUNT%TYPE;
    V_MEMBER_ID  INVOICE.MEMBER_ID%TYPE;
    V_POINT_USED INVOICE.POINT_USED%TYPE;
BEGIN

    SELECT NET_AMOUNT, MEMBER_ID, POINT_USED
    INTO V_NET_AMOUNT, V_MEMBER_ID, V_POINT_USED
    FROM INVOICE
    WHERE ID = P_INVOICE_ID;


    IF V_MEMBER_ID IS NOT NULL AND V_NET_AMOUNT > 0 THEN
        INSERT INTO POINT_UPDATE_LOG (MEMBER_ID, INVOICE_ID, POINT_CHANGE)
        VALUES (V_MEMBER_ID, P_INVOICE_ID, TRUNC(V_NET_AMOUNT / 1000));

        IF V_POINT_USED > 0 THEN
            INSERT INTO POINT_UPDATE_LOG (MEMBER_ID, INVOICE_ID, POINT_CHANGE)
            VALUES (V_MEMBER_ID, P_INVOICE_ID, V_POINT_USED);
        END IF;
    END IF;

    UPDATE INVOICE
    SET DISCOUNT = DISCOUNT + V_POINT_USED * 40 -- Không được để sử dụng điểm vượt quá net amount
    WHERE ID = P_INVOICE_ID;

    UPDATE INVOICE
    SET TOTAL_DUE = NET_AMOUNT - DISCOUNT,
        STATUS    = 'Đã hoàn thành'
    WHERE ID = P_INVOICE_ID;
END PRC_CALC_TOTAL;
/

CREATE OR REPLACE PROCEDURE PRC_CANCEL_INVOICE(
    P_INVOICE_ID IN INVOICE.ID%TYPE
)
AS
    V_POINT_CHANGE NUMBER := 0;
    V_INVOICE      INVOICE%ROWTYPE;
BEGIN
    SELECT *
    INTO V_INVOICE
    FROM INVOICE
    WHERE ID = P_INVOICE_ID;

    UPDATE INVOICE
    SET STATUS = 'Đã hủy'
    WHERE ID = P_INVOICE_ID;

    IF V_INVOICE.MEMBER_ID IS NOT NULL THEN
        FOR REC IN (
            SELECT POINT_CHANGE
            FROM POINT_UPDATE_LOG
            WHERE INVOICE_ID = P_INVOICE_ID)
            LOOP
                V_POINT_CHANGE := V_POINT_CHANGE + REC.POINT_CHANGE;
            END LOOP;

        INSERT INTO POINT_UPDATE_LOG (MEMBER_ID, INVOICE_ID, POINT_CHANGE)
        VALUES (V_INVOICE.MEMBER_ID, V_INVOICE.ID, -V_POINT_CHANGE);
    END IF;
END PRC_CANCEL_INVOICE;
/

select *
from INVOICE
where ID = 'VN000001250515000001';

CREATE OR REPLACE PROCEDURE PRC_CALC_PAYCHECK(
    P_EMP_ID IN EMPLOYEE.ID%TYPE,
    P_DEDUCTIONS IN PAYCHECK.DEDUCTIONS%TYPE DEFAULT 0,
    P_PERIOD_START IN TIMESTAMP,
    P_PERIOD_END IN TIMESTAMP,
    P_PAYCHECK_ID OUT PAYCHECK.ID%TYPE
)
AS
    v_hours       NUMBER; -- tổng số giờ làm việc
    v_night_hours NUMBER; -- số giờ trong khung 22:00–06:00
    v_wage        EMPLOYEE.HOURLY_WAGE%TYPE;
    v_gross       PAYCHECK.GROSS_AMOUNT%TYPE;
    v_deductions  PAYCHECK.DEDUCTIONS%TYPE := P_DEDUCTIONS;
    v_net         PAYCHECK.NET_AMOUNT%TYPE;
BEGIN
    -- 1) Lấy lương giờ
    SELECT HOURLY_WAGE
    INTO v_wage
    FROM EMPLOYEE
    WHERE ID = P_EMP_ID;

    -- 2a) Tính tổng giờ làm trong ca và kỳ
    SELECT NVL(SUM(
                       (EXTRACT(DAY FROM (o_end - o_start)) * 86400
                           + EXTRACT(HOUR FROM (o_end - o_start)) * 3600
                           + EXTRACT(MINUTE FROM (o_end - o_start)) * 60
                           + EXTRACT(SECOND FROM (o_end - o_start)))
               ) / 3600, 0)
    INTO V_HOURS
    FROM (SELECT GREATEST(SR.START_TIME, A.START_TIME, P_PERIOD_START) AS O_START,
                 LEAST(SR.END_TIME, A.END_TIME, P_PERIOD_END)          AS O_END
          FROM SHIFT_REPORT SR
                   JOIN ASSIGNMENT A
                        ON SR.EMPLOYEE_ID = A.EMPLOYEE_ID
                            AND SR.START_TIME < A.END_TIME
                            AND SR.END_TIME > A.START_TIME
          WHERE SR.EMPLOYEE_ID = P_EMP_ID
            AND SR.START_TIME < P_PERIOD_END
            AND SR.END_TIME > P_PERIOD_START) OVL
    WHERE OVL.O_END > OVL.O_START;

    -- 2b) Tính giờ đêm (22:00–24:00 và 00:00–06:00)
    SELECT NVL(SUM(n1 + n2), 0)
    INTO v_night_hours
    FROM (SELECT
              -- phần 22→24 của mỗi khoảng
              (EXTRACT(DAY FROM CASE
                                    WHEN o_start < TRUNC(o_start) + 1
                                        AND o_end > TRUNC(o_start) + INTERVAL '22' HOUR
                                        THEN LEAST(o_end, TRUNC(o_start) + 1)
                                        - GREATEST(o_start, TRUNC(o_start) + INTERVAL '22' HOUR)
                                    ELSE NUMTODSINTERVAL(0, 'SECOND') END)
                   * 86400
                  + EXTRACT(HOUR FROM CASE
                                          WHEN o_start < TRUNC(o_start) + 1
                                              AND o_end > TRUNC(o_start) + INTERVAL '22' HOUR
                                              THEN LEAST(o_end, TRUNC(o_start) + 1)
                                              - GREATEST(o_start, TRUNC(o_start) + INTERVAL '22' HOUR)
                                          ELSE NUMTODSINTERVAL(0, 'SECOND') END)
                   * 3600
                  + EXTRACT(MINUTE FROM CASE
                                            WHEN o_start < TRUNC(o_start) + 1
                                                AND o_end > TRUNC(o_start) + INTERVAL '22' HOUR
                                                THEN LEAST(o_end, TRUNC(o_start) + 1)
                                                - GREATEST(o_start, TRUNC(o_start) + INTERVAL '22' HOUR)
                                            ELSE NUMTODSINTERVAL(0, 'SECOND') END)
                   * 60
                  + EXTRACT(SECOND FROM CASE
                                            WHEN o_start < TRUNC(o_start) + 1
                                                AND o_end > TRUNC(o_start) + INTERVAL '22' HOUR
                                                THEN LEAST(o_end, TRUNC(o_start) + 1)
                                                - GREATEST(o_start, TRUNC(o_start) + INTERVAL '22' HOUR)
                                            ELSE NUMTODSINTERVAL(0, 'SECOND') END)
                  ) / 3600 AS n1,
              -- phần 00→06 của mỗi khoảng
              (EXTRACT(DAY FROM CASE
                                    WHEN o_start < TRUNC(o_end) + INTERVAL '06' HOUR
                                        AND o_end > TRUNC(o_end)
                                        THEN LEAST(o_end, TRUNC(o_end) + INTERVAL '06' HOUR)
                                        - GREATEST(o_start, TRUNC(o_end))
                                    ELSE NUMTODSINTERVAL(0, 'SECOND') END)
                   * 86400
                  + EXTRACT(HOUR FROM CASE
                                          WHEN o_start < TRUNC(o_end) + INTERVAL '06' HOUR
                                              AND o_end > TRUNC(o_end)
                                              THEN LEAST(o_end, TRUNC(o_end) + INTERVAL '06' HOUR)
                                              - GREATEST(o_start, TRUNC(o_end))
                                          ELSE NUMTODSINTERVAL(0, 'SECOND') END)
                   * 3600
                  + EXTRACT(MINUTE FROM CASE
                                            WHEN o_start < TRUNC(o_end) + INTERVAL '06' HOUR
                                                AND o_end > TRUNC(o_end)
                                                THEN LEAST(o_end, TRUNC(o_end) + INTERVAL '06' HOUR)
                                                - GREATEST(o_start, TRUNC(o_end))
                                            ELSE NUMTODSINTERVAL(0, 'SECOND') END)
                   * 60
                  + EXTRACT(SECOND FROM CASE
                                            WHEN o_start < TRUNC(o_end) + INTERVAL '06' HOUR
                                                AND o_end > TRUNC(o_end)
                                                THEN LEAST(o_end, TRUNC(o_end) + INTERVAL '06' HOUR)
                                                - GREATEST(o_start, TRUNC(o_end))
                                            ELSE NUMTODSINTERVAL(0, 'SECOND') END)
                  ) / 3600 AS n2
          FROM (SELECT GREATEST(SR.START_TIME, A.START_TIME, P_PERIOD_START) AS O_START,
                       LEAST(SR.END_TIME, A.END_TIME, P_PERIOD_END)          AS O_END
                FROM SHIFT_REPORT SR
                         JOIN ASSIGNMENT A
                              ON SR.EMPLOYEE_ID = A.EMPLOYEE_ID
                                  AND SR.START_TIME < A.END_TIME
                                  AND SR.END_TIME > A.START_TIME
                WHERE SR.EMPLOYEE_ID = P_EMP_ID
                  AND SR.START_TIME < P_PERIOD_END
                  AND SR.END_TIME > P_PERIOD_START) OV2
          WHERE OV2.O_END > OV2.O_START);

    -- 3) Tính gross và net
    v_gross := (v_hours - v_night_hours) * v_wage
        + v_night_hours * v_wage * 1.3;
    v_net := v_gross - v_deductions;

    -- 4) Lưu paycheck
    INSERT INTO PAYCHECK(EMPLOYEE_ID, GROSS_AMOUNT, DEDUCTIONS, NET_AMOUNT, PAY_DATE)
    VALUES (P_EMP_ID, v_gross, v_deductions, v_net, SYSTIMESTAMP)
    RETURNING ID INTO P_PAYCHECK_ID;
END PRC_CALC_PAYCHECK;
/

CREATE OR REPLACE PROCEDURE PRC_GENERATE_PAYCHECKS(
    P_DEDUCTIONS IN PAYCHECK.DEDUCTIONS%TYPE DEFAULT 0, -- số nhiều
    P_PERIOD_START IN TIMESTAMP,
    P_PERIOD_END IN TIMESTAMP
)
AS
    CURSOR c_emp IS
        SELECT ID
        FROM EMPLOYEE
        WHERE STATUS = 'Đang hoạt động';
    v_pc_id PAYCHECK.ID%TYPE;
BEGIN
    FOR r IN c_emp
        LOOP
            PRC_CALC_PAYCHECK(
                    P_EMP_ID => r.ID,
                    P_DEDUCTIONS => P_DEDUCTIONS,
                    P_PERIOD_START => P_PERIOD_START,
                    P_PERIOD_END => P_PERIOD_END,
                    P_PAYCHECK_ID => v_pc_id
            );
        END LOOP;
EXCEPTION
    WHEN OTHERS THEN
        RAISE; -- hoặc log thêm DBMS_OUTPUT.PUT_LINE(SQLERRM);
END PRC_GENERATE_PAYCHECKS;
/

-- Demo qui trình thanh toán
DECLARE
    V_INVOICE_ID_1 INVOICE.ID%TYPE;
    V_INVOICE_ID_2 INVOICE.ID%TYPE;
    V_SHIFT_ID     SHIFT_REPORT.ID%TYPE;
    V_POINT_GAINED MEMBER.LOYALTY_POINTS%TYPE;
    V_MEMBER_ID    MEMBER.ID%TYPE             := '09412345672505150001';
    V_STORE_ID     SHIFT_REPORT.STORE_ID%TYPE := 'VN000001';
    V_EMPLOYEE_ID  EMPLOYEE.ID%TYPE           := '250515000001';
BEGIN
    PRC_INITIATE_SHIFT(
            P_STORE_ID => V_STORE_ID,
            P_EMPLOYEE_ID => V_EMPLOYEE_ID,
            P_SHIFT_ID => V_SHIFT_ID
    );

    PRC_INITIATE_INVOICE(
            P_STORE_ID => V_STORE_ID,
            P_MEMBER_ID => V_MEMBER_ID,
            P_PAYMENT_ID => 'P001',
            P_EMPLOYEE_ID => V_EMPLOYEE_ID,
            P_POINT_USED => 0,
            P_INVOICE_ID => V_INVOICE_ID_1
    );
    PRC_ADD_ITEM_TO_INVOICE(
            P_INVOICE_ID => V_INVOICE_ID_1,
            P_PRODUCT_ID => '0123456789023',
            P_QUANTITY_SOLD => 1
    );
    PRC_ADD_ITEM_TO_INVOICE(
            P_INVOICE_ID => V_INVOICE_ID_1,
            P_PRODUCT_ID => '0123456789023',
            P_QUANTITY_SOLD => 1
    );
    PRC_ADD_ITEM_TO_INVOICE(
            P_INVOICE_ID => V_INVOICE_ID_1,
            P_PRODUCT_ID => '0123456789023',
            P_QUANTITY_SOLD => 2
    );
    PRC_ADD_ITEM_TO_INVOICE(
            P_INVOICE_ID => V_INVOICE_ID_1,
            P_PRODUCT_ID => '0123456789025',
            P_QUANTITY_SOLD => 2
    );
    PRC_CALC_TOTAL(
            P_INVOICE_ID => V_INVOICE_ID_1
    );

    SELECT LOYALTY_POINTS
    INTO V_POINT_GAINED
    FROM MEMBER
             JOIN INVOICE ON MEMBER.ID = INVOICE.MEMBER_ID
    WHERE INVOICE.ID = V_INVOICE_ID_1;

    PRC_INITIATE_INVOICE(
            P_STORE_ID => V_STORE_ID,
            P_MEMBER_ID => V_MEMBER_ID,
            P_PAYMENT_ID => 'P001',
            P_EMPLOYEE_ID => V_EMPLOYEE_ID,
            P_POINT_USED => V_POINT_GAINED,
            P_INVOICE_ID => V_INVOICE_ID_2
    );

    PRC_ADD_ITEM_TO_INVOICE(
            P_INVOICE_ID => V_INVOICE_ID_2,
            P_PRODUCT_ID => '0123456789023',
            P_QUANTITY_SOLD => 1
    );
    PRC_ADD_ITEM_TO_INVOICE(
            P_INVOICE_ID => V_INVOICE_ID_2,
            P_PRODUCT_ID => '0123456789023',
            P_QUANTITY_SOLD => 1
    );

    PRC_CALC_TOTAL(P_INVOICE_ID => V_INVOICE_ID_2);

    PRC_CANCEL_INVOICE(V_INVOICE_ID_1);

    PRC_END_SHIFT(P_SHIFT_ID => V_SHIFT_ID);

    -- DEV
    UPDATE SHIFT_REPORT
    SET END_TIME = SYSTIMESTAMP
        + NUMTODSINTERVAL(6.5, 'HOUR')
    WHERE ID = V_SHIFT_ID;

    PRC_GENERATE_PAYCHECKS(
            P_DEDUCTIONS => 0,
            P_PERIOD_START => TIMESTAMP '2025-05-01 00:00:00',
            P_PERIOD_END => TIMESTAMP '2025-05-31 00:00:00'
    );

    DBMS_OUTPUT.PUT_LINE('== Các hóa đơn của thành viên: ' || V_MEMBER_ID || ' ==');
    FOR REC IN (
        SELECT *
        FROM INVOICE
        WHERE MEMBER_ID = V_MEMBER_ID
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE('Mã hóa đơn: ' || REC.ID);
            DBMS_OUTPUT.PUT_LINE('Thời gian tạo hóa đơn: ' || REC.CREATION_TIME);
            DBMS_OUTPUT.PUT_LINE('Thành tiền: ' || REC.NET_AMOUNT);
            DBMS_OUTPUT.PUT_LINE('Giảm giá: ' || REC.DISCOUNT);
            DBMS_OUTPUT.PUT_LINE('Tổng cộng: ' || REC.TOTAL_DUE);
            DBMS_OUTPUT.PUT_LINE('Trạng thái hóa đơn: ' || REC.STATUS);
            DBMS_OUTPUT.PUT_LINE('Mã thành viên: ' || REC.MEMBER_ID);
            DBMS_OUTPUT.PUT_LINE('Mã cửa hàng: ' || REC.STORE_ID);
            DBMS_OUTPUT.PUT_LINE('Mã phương thức thanh toán: ' || REC.PAYMENT_ID);
            DBMS_OUTPUT.PUT_LINE('Mã nhân viên: ' || REC.EMPLOYEE_ID);
            DBMS_OUTPUT.PUT_LINE('Số điểm sử dụng: ' || REC.POINT_USED);
            DBMS_OUTPUT.PUT_LINE('===============================================================');

            DBMS_OUTPUT.PUT_LINE('== Chi tiết hóa đơn ' || REC.ID || ' ==');
            FOR REC1 IN (
                SELECT *
                FROM INVOICE_DETAIL
                WHERE INVOICE_ID = REC.ID
                )
                LOOP
                    DBMS_OUTPUT.PUT_LINE('Mã sản phẩm: ' || REC1.PRODUCT_ID || ' - Số lượng: ' || REC1.QUANTITY_SOLD);
                END LOOP;
            DBMS_OUTPUT.PUT_LINE('===============================================================');

            DBMS_OUTPUT.PUT_LINE('== Thành viên ' || REC.MEMBER_ID || ' của hóa đơn trên' || ' ==');
            FOR REC1 IN (
                SELECT *
                FROM MEMBER
                WHERE ID = REC.MEMBER_ID
                )
                LOOP
                    DBMS_OUTPUT.PUT_LINE('Tên: ' || REC1.NAME);
                    DBMS_OUTPUT.PUT_LINE('Số điện thoại: ' || REC1.PHONE_NUMBER);
                    DBMS_OUTPUT.PUT_LINE('Email: ' || REC1.EMAIL);
                    DBMS_OUTPUT.PUT_LINE('Ngày đăng kí thành viên: ' || REC1.REGISTRATION_TIME);
                    DBMS_OUTPUT.PUT_LINE('Số điểm tích lũy: ' || REC1.LOYALTY_POINTS);
                END LOOP;
            DBMS_OUTPUT.PUT_LINE('===============================================================');
        END LOOP;

    DBMS_OUTPUT.PUT_LINE('== Lịch sử tích điểm thành viên ' || V_MEMBER_ID || ' ==');
    FOR REC IN (
        SELECT *
        FROM POINT_UPDATE_LOG
        WHERE MEMBER_ID = V_MEMBER_ID
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE('Mã cập nhật điểm: ' || REC.ID || ' - Mã hóa đơn: ' ||
                                 REC.INVOICE_ID || 'Số điểm thay đổi: ' ||
                                 REC.POINT_CHANGE);
        END LOOP;
    DBMS_OUTPUT.PUT_LINE('===============================================================');

    DBMS_OUTPUT.PUT_LINE('== Báo cáo kết ca hiện tại: ' || V_SHIFT_ID || ' ==');
    FOR REC IN (
        SELECT *
        FROM SHIFT_REPORT
        WHERE ID = V_SHIFT_ID
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE('Thời gian bắt đầu ca: ' || REC.START_TIME);
            DBMS_OUTPUT.PUT_LINE('Thời gian kết thúc ca: ' || REC.END_TIME);
            DBMS_OUTPUT.PUT_LINE('Tổng ví điện tử: ' || REC.EWALLET_REVENUE);
            DBMS_OUTPUT.PUT_LINE('Tổng tiền mặt: ' || REC.CASH_REVENUE);
            DBMS_OUTPUT.PUT_LINE('Tổng chuyển khoản ngân hàng: ' || REC.BANK_REVENUE);
            DBMS_OUTPUT.PUT_LINE('Tổng cộng => ' || TO_CHAR(REC.EWALLET_REVENUE + REC.CASH_REVENUE + REC.BANK_REVENUE));
            DBMS_OUTPUT.PUT_LINE('Tổng số lượng giao dịch: ' || REC.TRANSACTION_COUNT);
            DBMS_OUTPUT.PUT_LINE('Mã cửa hàng: ' || REC.STORE_ID);
            DBMS_OUTPUT.PUT_LINE('Mã nhân viên: ' || REC.EMPLOYEE_ID);
        END LOOP;
    DBMS_OUTPUT.PUT_LINE('===============================================================');

    DBMS_OUTPUT.PUT_LINE('== Phiếu tính lương: các ca làm từ 0h 1/5/2025 tới hiện tại ==');
    FOR REC IN (
        SELECT *
        FROM PAYCHECK
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE('Mã phiếu lương: ' || REC.ID);
            DBMS_OUTPUT.PUT_LINE('Mã nhân viên: ' || REC.EMPLOYEE_ID);
            DBMS_OUTPUT.PUT_LINE('Ngày trả lương: ' || REC.PAY_DATE);
            DBMS_OUTPUT.PUT_LINE('Tổng trước thuế, phụ cấp, khấu trừ: ' || REC.GROSS_AMOUNT);
            DBMS_OUTPUT.PUT_LINE('Các khoản thuế, phụ cấp, khấu trừ:  ' || REC.DEDUCTIONS);
            DBMS_OUTPUT.PUT_LINE('Tổng thực nhận:  ' || REC.NET_AMOUNT);
            DBMS_OUTPUT.PUT_LINE('===============================================================');
        END LOOP;

    -- Nếu không có lỗi, thì commit
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        -- Nếu có lỗi xảy ra, rollback lại toàn bộ
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Có lỗi xảy ra: ' || SQLERRM);
END;
/