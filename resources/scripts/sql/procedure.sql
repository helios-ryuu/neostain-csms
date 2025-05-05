CREATE OR REPLACE PROCEDURE PRC_INITIATE_SHIFT(
    P_STORE_ID IN CSMS_ADMIN.SHIFT_REPORT.STORE_ID%type,
    P_EMPLOYEE_ID IN CSMS_ADMIN.SHIFT_REPORT.EMPLOYEE_ID%type,
    P_SHIFT_ID OUT CSMS_ADMIN.SHIFT_REPORT.SHIFT_REPORT_ID%type
)
AS
BEGIN
    INSERT INTO SHIFT_REPORT(STORE_ID, EMPLOYEE_ID)
    VALUES (P_STORE_ID, P_EMPLOYEE_ID)
    RETURNING SHIFT_REPORT_ID
        INTO P_SHIFT_ID;
END PRC_INITIATE_SHIFT;
/

CREATE OR REPLACE PROCEDURE PRC_END_SHIFT(
    P_SHIFT_ID IN SHIFT_REPORT.SHIFT_REPORT_ID%TYPE
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
    SELECT SHIFT_START_TIME
    INTO v_start
    FROM SHIFT_REPORT
    WHERE SHIFT_REPORT_ID = P_SHIFT_ID
      AND SHIFT_END_TIME IS NULL; -- đảm bảo chỉ chạy 1 lần

    UPDATE SHIFT_REPORT
    SET SHIFT_END_TIME = v_end
    WHERE SHIFT_REPORT_ID = P_SHIFT_ID;

    -- 2) Tính tổng doanh thu & số giao dịch
    SELECT SUM(CASE WHEN PAYMENT_ID = 'P001' THEN TOTAL_DUE ELSE 0 END),
           SUM(CASE WHEN PAYMENT_ID = 'P002' THEN TOTAL_DUE ELSE 0 END),
           SUM(CASE WHEN PAYMENT_ID = 'P003' THEN TOTAL_DUE ELSE 0 END)
    INTO v_cash, v_ewallet, v_bank
    FROM INVOICE
    WHERE INVOICE_CREATION_TIME BETWEEN v_start AND v_end
      AND INVOICE_STATUS = 'Đã hoàn thành';

    SELECT COUNT(*)
    INTO v_count
    FROM INVOICE
    WHERE INVOICE_CREATION_TIME BETWEEN v_start AND v_end;

    -- 3) Cập nhật vào báo cáo đóng ca
    UPDATE SHIFT_REPORT
    SET CASH_REVENUE      = NVL(v_cash, 0),
        EWALLET_REVENUE   = NVL(v_ewallet, 0),
        BANK_REVENUE      = NVL(v_bank, 0),
        TRANSACTION_COUNT = v_count
    WHERE SHIFT_REPORT_ID = P_SHIFT_ID;
END PRC_END_SHIFT;
/

CREATE OR REPLACE PROCEDURE PRC_INITIATE_INVOICE(
    P_STORE_ID IN CSMS_ADMIN.INVOICE.STORE_ID%type,
    P_MEMBER_ID IN CSMS_ADMIN.INVOICE.MEMBER_ID%type,
    P_PAYMENT_ID IN CSMS_ADMIN.INVOICE.PAYMENT_ID%type,
    P_EMPLOYEE_ID IN CSMS_ADMIN.INVOICE.EMPLOYEE_ID%type,
    P_INVOICE_ID OUT CSMS_ADMIN.INVOICE.INVOICE_ID%type
)
AS
BEGIN
    INSERT INTO INVOICE(STORE_ID, MEMBER_ID, PAYMENT_ID, EMPLOYEE_ID)
    VALUES (P_STORE_ID, P_MEMBER_ID, P_PAYMENT_ID, P_EMPLOYEE_ID)
    RETURNING INVOICE_ID
        INTO P_INVOICE_ID;
END PRC_INITIATE_INVOICE;
/

CREATE OR REPLACE PROCEDURE PRC_ADD_ITEM_TO_INVOICE(
    P_INVOICE_ID IN INVOICE_DETAIL.INVOICE_ID%TYPE,
    P_PRODUCT_ID IN INVOICE_DETAIL.PRODUCT_ID%TYPE,
    P_QUANTITY_SOLD IN INVOICE_DETAIL.QUANTITY_SOLD%TYPE
)
AS
    v_unit_price    PRODUCT.UNIT_PRICE%TYPE;
    v_promo_price   PRODUCT.UNIT_PRICE%TYPE;
    v_sets          NUMBER;
    v_free_qty      NUMBER;
    v_discount_rate PROMOTION.DISCOUNT_RATE%TYPE;
BEGIN
    ----------------------------------------
    -- 1) Chèn hoặc cộng dồn sản phẩm chính
    ----------------------------------------
    UPDATE INVOICE_DETAIL
    SET QUANTITY_SOLD = QUANTITY_SOLD + P_QUANTITY_SOLD
    WHERE INVOICE_ID = P_INVOICE_ID
      AND PRODUCT_ID = P_PRODUCT_ID;
    IF SQL%ROWCOUNT = 0 THEN
        INSERT INTO INVOICE_DETAIL(INVOICE_ID, PRODUCT_ID, QUANTITY_SOLD)
        VALUES (P_INVOICE_ID, P_PRODUCT_ID, P_QUANTITY_SOLD);
    END IF;

    ----------------------------------------
    -- 2) Cập nhật NET_AMOUNT cho sản phẩm chính
    ----------------------------------------
    SELECT UNIT_PRICE
    INTO v_unit_price
    FROM PRODUCT
    WHERE PRODUCT_ID = P_PRODUCT_ID;

    UPDATE INVOICE
    SET NET_AMOUNT = NET_AMOUNT + v_unit_price * P_QUANTITY_SOLD
    WHERE INVOICE_ID = P_INVOICE_ID;

    ----------------------------------------
    -- 3) Xử lý khuyến mãi MUA X TẶNG Y
    ----------------------------------------
    FOR promo_rec IN (
        SELECT MINIMUM_PURCHASE_QUANTITY,
               PROMO_PRODUCT_ID,
               PROMO_PRODUCT_QUANTITY,
               DISCOUNT_RATE
        FROM PROMOTION
        WHERE PRODUCT_ID = P_PRODUCT_ID
          AND SYSDATE BETWEEN PROMOTION_START_TIME AND PROMOTION_END_TIME
        )
        LOOP
            -- số “bộ” đủ điều kiện
            v_sets := FLOOR(P_QUANTITY_SOLD / promo_rec.MINIMUM_PURCHASE_QUANTITY);
            IF v_sets > 0 THEN
                -- 3.1) Giảm giá trên phần mua (X)
                UPDATE INVOICE
                SET DISCOUNT = DISCOUNT
                    + v_unit_price
                                   * promo_rec.MINIMUM_PURCHASE_QUANTITY
                                   * v_sets
                                   * promo_rec.DISCOUNT_RATE
                WHERE INVOICE_ID = P_INVOICE_ID;

                -- 3.2) Xác định số lượng tặng
                v_free_qty := v_sets * promo_rec.PROMO_PRODUCT_QUANTITY;

                -- 3.3) Chèn hoặc cộng dồn hàng tặng vào detail
                UPDATE INVOICE_DETAIL
                SET QUANTITY_SOLD = QUANTITY_SOLD + v_free_qty
                WHERE INVOICE_ID = P_INVOICE_ID
                  AND PRODUCT_ID = promo_rec.PROMO_PRODUCT_ID;
                IF SQL%ROWCOUNT = 0 THEN
                    INSERT INTO INVOICE_DETAIL(INVOICE_ID, PRODUCT_ID, QUANTITY_SOLD)
                    VALUES (P_INVOICE_ID, promo_rec.PROMO_PRODUCT_ID, v_free_qty);
                END IF;

                -- 3.4) Cộng giá trị hàng tặng vào NET_AMOUNT và DISCOUNT
                SELECT UNIT_PRICE
                INTO v_promo_price
                FROM PRODUCT
                WHERE PRODUCT_ID = promo_rec.PROMO_PRODUCT_ID;

                UPDATE INVOICE
                SET NET_AMOUNT = NET_AMOUNT + v_promo_price * v_free_qty,
                    DISCOUNT   = DISCOUNT + v_promo_price * v_free_qty
                WHERE INVOICE_ID = P_INVOICE_ID;
            END IF;
        END LOOP;
END PRC_ADD_ITEM_TO_INVOICE;
/

CREATE OR REPLACE PROCEDURE PRC_CALC_TOTAL(
    P_INVOICE_ID IN INVOICE.INVOICE_ID%TYPE,
    P_POINT_USE IN NUMBER DEFAULT 0
)
AS
    V_INVOICE_ID INVOICE.INVOICE_ID%TYPE;
    V_INVOICE    INVOICE%ROWTYPE;
BEGIN
    SELECT *
    INTO V_INVOICE
    FROM INVOICE
    WHERE INVOICE_ID = P_INVOICE_ID;

    IF V_INVOICE.MEMBER_ID IS NOT NULL AND V_INVOICE.NET_AMOUNT > 0 THEN
        INSERT INTO POINT_UPDATE_LOG (MEMBER_ID, INVOICE_ID, POINT_CHANGE)
        VALUES (V_INVOICE.MEMBER_ID, V_INVOICE.INVOICE_ID, TRUNC(V_INVOICE.NET_AMOUNT / 1000));

        IF P_POINT_USE > 0 THEN
            INSERT INTO POINT_UPDATE_LOG (MEMBER_ID, INVOICE_ID, POINT_CHANGE)
            VALUES (V_INVOICE.MEMBER_ID, V_INVOICE.INVOICE_ID, -P_POINT_USE);

            UPDATE INVOICE
            SET POINT_USED = P_POINT_USE
            WHERE INVOICE_ID = P_INVOICE_ID;
        END IF;
    END IF;

    UPDATE INVOICE
    SET DISCOUNT = DISCOUNT + P_POINT_USE * 40 -- Không được để sử dụng điểm vượt quá net amount
    WHERE INVOICE_ID = P_INVOICE_ID;

    UPDATE INVOICE
    SET TOTAL_DUE      = NET_AMOUNT - DISCOUNT,
        INVOICE_STATUS = 'Đã hoàn thành'
    WHERE INVOICE_ID = P_INVOICE_ID;
END PRC_CALC_TOTAL;
/

CREATE OR REPLACE PROCEDURE PRC_CANCEL_INVOICE(
    P_INVOICE_ID IN INVOICE.INVOICE_ID%TYPE
)
AS
    V_POINT_CHANGE NUMBER := 0;
    V_INVOICE      INVOICE%ROWTYPE;
BEGIN
    SELECT *
    INTO V_INVOICE
    FROM INVOICE
    WHERE INVOICE_ID = P_INVOICE_ID;

    UPDATE INVOICE
    SET INVOICE_STATUS = 'Đã hủy'
    WHERE INVOICE_ID = P_INVOICE_ID;

    FOR REC IN (
        SELECT POINT_CHANGE
        FROM POINT_UPDATE_LOG
        WHERE INVOICE_ID = P_INVOICE_ID)
        LOOP
            V_POINT_CHANGE := V_POINT_CHANGE + REC.POINT_CHANGE;
        END LOOP;

    INSERT INTO POINT_UPDATE_LOG (MEMBER_ID, INVOICE_ID, POINT_CHANGE)
    VALUES (V_INVOICE.MEMBER_ID, V_INVOICE.INVOICE_ID, -V_POINT_CHANGE);
END PRC_CANCEL_INVOICE;
/

CREATE OR REPLACE PROCEDURE PRC_CALC_PAYCHECK(
    P_EMP_ID IN EMPLOYEE.EMPLOYEE_ID%TYPE,
    P_DEDUCTIONS_ID IN PAYCHECK.DEDUCTIONS%TYPE DEFAULT 0,
    P_PERIOD_START IN TIMESTAMP,
    P_PERIOD_END IN TIMESTAMP,
    P_PAYCHECK_ID OUT PAYCHECK.PAYCHECK_ID%TYPE
)
AS
    v_hours      NUMBER; -- tổng số giờ (có decimal)
    v_wage       EMPLOYEE.HOURLY_WAGE%TYPE; -- lương theo giờ
    v_gross      PAYCHECK.GROSS_AMOUNT%TYPE; -- tổng trước khấu trừ
    v_deductions PAYCHECK.DEDUCTIONS%type := P_DEDUCTIONS_ID;
    v_net        PAYCHECK.NET_AMOUNT%TYPE; -- thực nhận (ở đây = v_gross vì không khấu trừ)
BEGIN
    -- 2) Lấy lương giờ
    SELECT HOURLY_WAGE
    INTO v_wage
    FROM EMPLOYEE
    WHERE EMPLOYEE_ID = P_EMP_ID;

    -- 3) Tính tổng giờ làm:
    --    Sử dụng EXTRACT để lấy đầy đủ ngày/giờ/phút/giây từ INTERVAL
    SELECT NVL(SUM(
                       EXTRACT(DAY FROM (SHIFT_END_TIME - SHIFT_START_TIME)) * 24
                           + EXTRACT(HOUR FROM (SHIFT_END_TIME - SHIFT_START_TIME))
                           + EXTRACT(MINUTE FROM (SHIFT_END_TIME - SHIFT_START_TIME)) / 60
                           + EXTRACT(SECOND FROM (SHIFT_END_TIME - SHIFT_START_TIME)) / 3600
               ), 0)
    INTO v_hours
    FROM SHIFT_REPORT
    WHERE EMPLOYEE_ID = P_EMP_ID
      AND SHIFT_START_TIME >= P_PERIOD_START
      AND SHIFT_START_TIME < P_PERIOD_END
      AND SHIFT_END_TIME IS NOT NULL;

    -- 4) Tính gross và net
    v_gross := v_hours * v_wage;
    v_net := v_gross - v_deductions;
    -- nếu có khấu trừ, thay đổi v_net = v_gross - v_deductions

    -- 5) Lưu paycheck
    INSERT INTO PAYCHECK(EMPLOYEE_ID,
                         GROSS_AMOUNT,
                         DEDUCTIONS, -- nếu không khấu trừ, mặc định 0
                         NET_AMOUNT,
                         PAY_DATE)
    VALUES (P_EMP_ID,
            v_gross,
            v_deductions,
            v_net,
            SYSTIMESTAMP)
    RETURNING PAYCHECK_ID
        INTO P_PAYCHECK_ID;
END PRC_CALC_PAYCHECK;
/

CREATE OR REPLACE PROCEDURE PRC_GENERATE_PAYCHECKS(
    P_DEDUCTION_ID IN PAYCHECK.DEDUCTIONS%TYPE DEFAULT 0,
    P_PERIOD_START IN TIMESTAMP,
    P_PERIOD_END IN TIMESTAMP
)
AS
    CURSOR c_emp IS
        SELECT EMPLOYEE_ID
        FROM EMPLOYEE
        WHERE EMPLOYEE_STATUS = 'Đang hoạt động';
    v_emp_id EMPLOYEE.EMPLOYEE_ID%TYPE;
    v_pc_id  PAYCHECK.PAYCHECK_ID%TYPE;
BEGIN
    FOR r IN c_emp
        LOOP
            v_emp_id := r.EMPLOYEE_ID;
            -- Gọi từng nhân viên
            PRC_CALC_PAYCHECK(
                    P_EMP_ID => v_emp_id,
                    P_DEDUCTIONS_ID => P_DEDUCTION_ID,
                    P_PERIOD_START => P_PERIOD_START,
                    P_PERIOD_END => P_PERIOD_END,
                    P_PAYCHECK_ID => v_pc_id
            );
        END LOOP;
END PRC_GENERATE_PAYCHECKS;
/


-- TODO: Quy trình thanh toán
DECLARE
    V_INVOICE_ID_1 INVOICE.INVOICE_ID%TYPE;
    V_INVOICE_ID_2 INVOICE.INVOICE_ID%TYPE;
    V_SHIFT_ID     SHIFT_REPORT.SHIFT_REPORT_ID%TYPE;
    V_POINT_GAINED MEMBER.LOYALTY_POINTS%TYPE;
    V_MEMBER_ID    MEMBER.MEMBER_ID%TYPE      := '09412345672505050001';
    V_STORE_ID     SHIFT_REPORT.STORE_ID%TYPE := 'VN000001';
    V_EMPLOYEE_ID  EMPLOYEE.EMPLOYEE_ID%TYPE  := '250505000001';
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
            P_INVOICE_ID => V_INVOICE_ID_1,
            P_POINT_USE => 0
    );

    SELECT LOYALTY_POINTS
    INTO V_POINT_GAINED
    FROM MEMBER
             JOIN INVOICE ON MEMBER.MEMBER_ID = INVOICE.MEMBER_ID
    WHERE INVOICE_ID = V_INVOICE_ID_1;

    PRC_INITIATE_INVOICE(
            P_STORE_ID => V_STORE_ID,
            P_MEMBER_ID => V_MEMBER_ID,
            P_PAYMENT_ID => 'P001',
            P_EMPLOYEE_ID => V_EMPLOYEE_ID,
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
    PRC_CALC_TOTAL(
            P_INVOICE_ID => V_INVOICE_ID_2,
            P_POINT_USE => V_POINT_GAINED
    );

    PRC_CANCEL_INVOICE(V_INVOICE_ID_1);

    PRC_END_SHIFT(
            P_SHIFT_ID => V_SHIFT_ID
    );

    -- DEV
    UPDATE SHIFT_REPORT
    SET SHIFT_END_TIME = SYSTIMESTAMP
        + NUMTODSINTERVAL(6.5, 'HOUR')
    WHERE SHIFT_REPORT_ID = V_SHIFT_ID;

    PRC_GENERATE_PAYCHECKS(
            P_PERIOD_START => TIMESTAMP '2025-05-01 00:00:00',
            P_PERIOD_END => SYSTIMESTAMP
    );

    DBMS_OUTPUT.PUT_LINE('== Các hóa đơn của thành viên: ' || V_MEMBER_ID || ' ==');
    FOR REC IN (
        SELECT *
        FROM INVOICE
        WHERE MEMBER_ID = V_MEMBER_ID
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE('Mã hóa đơn: ' || REC.INVOICE_ID);
            DBMS_OUTPUT.PUT_LINE('Thời gian tạo hóa đơn: ' || REC.INVOICE_CREATION_TIME);
            DBMS_OUTPUT.PUT_LINE('Thành tiền: ' || REC.NET_AMOUNT);
            DBMS_OUTPUT.PUT_LINE('Giảm giá: ' || REC.DISCOUNT);
            DBMS_OUTPUT.PUT_LINE('Tổng cộng: ' || REC.TOTAL_DUE);
            DBMS_OUTPUT.PUT_LINE('Trạng thái hóa đơn: ' || REC.INVOICE_STATUS);
            DBMS_OUTPUT.PUT_LINE('Mã thành viên: ' || REC.MEMBER_ID);
            DBMS_OUTPUT.PUT_LINE('Mã cửa hàng: ' || REC.STORE_ID);
            DBMS_OUTPUT.PUT_LINE('Mã phương thức thanh toán: ' || REC.PAYMENT_ID);
            DBMS_OUTPUT.PUT_LINE('Mã nhân viên: ' || REC.EMPLOYEE_ID);
            DBMS_OUTPUT.PUT_LINE('Số điểm sử dụng: ' || REC.POINT_USED);
            DBMS_OUTPUT.PUT_LINE('===============================================================');

            DBMS_OUTPUT.PUT_LINE('== Chi tiết hóa đơn ' || REC.INVOICE_ID || ' ==');
            FOR REC1 IN (
                SELECT *
                FROM INVOICE_DETAIL
                WHERE INVOICE_ID = REC.INVOICE_ID
                )
                LOOP
                    DBMS_OUTPUT.PUT_LINE('Mã sản phẩm: ' || REC1.PRODUCT_ID || ' - Số lượng: ' || REC1.QUANTITY_SOLD);
                END LOOP;
            DBMS_OUTPUT.PUT_LINE('===============================================================');

            DBMS_OUTPUT.PUT_LINE('== Thành viên ' || REC.MEMBER_ID || ' của hóa đơn trên' || ' ==');
            FOR REC1 IN (
                SELECT *
                FROM MEMBER
                WHERE MEMBER_ID = REC.MEMBER_ID
                )
                LOOP
                    DBMS_OUTPUT.PUT_LINE('Tên: ' || REC1.MEMBER_NAME);
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
            DBMS_OUTPUT.PUT_LINE('Mã cập nhật điểm: ' || REC.POINT_UPDATE_LOG_ID || ' - Mã hóa đơn: ' ||
                                 REC.INVOICE_ID || 'Số điểm thay đổi: ' ||
                                 REC.POINT_CHANGE);
        END LOOP;
    DBMS_OUTPUT.PUT_LINE('===============================================================');

    DBMS_OUTPUT.PUT_LINE('== Báo cáo kết ca hiện tại: ' || V_SHIFT_ID || ' ==');
    FOR REC IN (
        SELECT *
        FROM SHIFT_REPORT
        WHERE SHIFT_REPORT_ID = V_SHIFT_ID
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE('Thời gian bắt đầu ca: ' || REC.SHIFT_START_TIME);
            DBMS_OUTPUT.PUT_LINE('Thời gian kết thúc ca: ' || REC.SHIFT_END_TIME);
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
            DBMS_OUTPUT.PUT_LINE('Mã phiếu lương: ' || REC.PAYCHECK_ID);
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
