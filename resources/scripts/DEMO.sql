DECLARE
    V_INVOICE_ID INVOICE.ID%TYPE;
    V_MEMBER_ID  MEMBER.ID%TYPE;
    V_EMPLOYEE_1 EMPLOYEE.ID%TYPE;
    V_EMPLOYEE_2 EMPLOYEE.ID%TYPE;
    V_EMPLOYEE_3 EMPLOYEE.ID%TYPE;
    V_SHIFT_ID   SHIFT_REPORT.ID%TYPE;
BEGIN
    SELECT ID
    INTO V_MEMBER_ID
    FROM MEMBER
    WHERE NAME = 'Tạ Việt Phương';

    SELECT ID
    INTO V_EMPLOYEE_1
    FROM EMPLOYEE
    WHERE NAME = 'Nguyễn Văn Nam';

    SELECT ID
    INTO V_EMPLOYEE_2
    FROM EMPLOYEE
    WHERE NAME = 'Võ Đức Tài';

    SELECT ID
    INTO V_EMPLOYEE_3
    FROM EMPLOYEE
    WHERE NAME = 'Thiều Đinh Nam Tài';

    PRC_INITIATE_SHIFT(P_STORE_ID => 'VN000001', P_EMPLOYEE_ID => V_EMPLOYEE_1, P_SHIFT_ID => V_SHIFT_ID);

    PRC_INITIATE_INVOICE(P_STORE_ID => 'VN000001', P_MEMBER_ID => NULL, P_PAYMENT_ID => 'P001',
                         P_EMPLOYEE_ID => V_EMPLOYEE_1, P_POINT_USED => 0, P_INVOICE_ID => V_INVOICE_ID);
    PRC_ADD_ITEM_TO_INVOICE(P_INVOICE_ID => V_INVOICE_ID, P_PRODUCT_ID => '0123456789010', P_QUANTITY_SOLD => 5);
    PRC_ADD_ITEM_TO_INVOICE(P_INVOICE_ID => V_INVOICE_ID, P_PRODUCT_ID => '0123456789009', P_QUANTITY_SOLD => 4);
    PRC_CALC_TOTAL(P_INVOICE_ID => V_INVOICE_ID);

    PRC_INITIATE_INVOICE(P_STORE_ID => 'VN000001', P_MEMBER_ID => V_MEMBER_ID, P_PAYMENT_ID => 'P002',
                         P_EMPLOYEE_ID => V_EMPLOYEE_2, P_POINT_USED => 0, P_INVOICE_ID => V_INVOICE_ID);
    PRC_ADD_ITEM_TO_INVOICE(P_INVOICE_ID => V_INVOICE_ID, P_PRODUCT_ID => '0123456789010', P_QUANTITY_SOLD => 5);
    PRC_ADD_ITEM_TO_INVOICE(P_INVOICE_ID => V_INVOICE_ID, P_PRODUCT_ID => '0123456789009', P_QUANTITY_SOLD => 4);
    PRC_ADD_ITEM_TO_INVOICE(P_INVOICE_ID => V_INVOICE_ID, P_PRODUCT_ID => '0123456789015', P_QUANTITY_SOLD => 4);
    PRC_CALC_TOTAL(P_INVOICE_ID => V_INVOICE_ID);

    PRC_INITIATE_INVOICE(P_STORE_ID => 'VN000001', P_MEMBER_ID => V_MEMBER_ID, P_PAYMENT_ID => 'P003',
                         P_EMPLOYEE_ID => V_EMPLOYEE_3, P_POINT_USED => 0, P_INVOICE_ID => V_INVOICE_ID);
    PRC_ADD_ITEM_TO_INVOICE(P_INVOICE_ID => V_INVOICE_ID, P_PRODUCT_ID => '0123456789010', P_QUANTITY_SOLD => 5);
    PRC_ADD_ITEM_TO_INVOICE(P_INVOICE_ID => V_INVOICE_ID, P_PRODUCT_ID => '0123456789023', P_QUANTITY_SOLD => 4);
    PRC_ADD_ITEM_TO_INVOICE(P_INVOICE_ID => V_INVOICE_ID, P_PRODUCT_ID => '0123456789015', P_QUANTITY_SOLD => 3);
    PRC_CALC_TOTAL(P_INVOICE_ID => V_INVOICE_ID);

    PRC_END_SHIFT(V_SHIFT_ID);

    UPDATE SHIFT_REPORT
    SET END_TIME = SYSTIMESTAMP + NUMTODSINTERVAL(6.5, 'HOUR')
    WHERE ID = V_SHIFT_ID;
END;
/

DECLARE
    -- 1) Lấy MEMBER_ID của Tạ Việt Phương
    V_MEMBER_ID MEMBER.ID%TYPE;
    -- 2) Mã cửa hàng cố định
    V_STORE_ID  STORE.ID%TYPE := 'VN000001';
BEGIN
    -- 1.1) Truy vấn ID
    SELECT ID
    INTO V_MEMBER_ID
    FROM MEMBER
    WHERE NAME = 'Tạ Việt Phương'
      AND ROWNUM = 1;

    DBMS_OUTPUT.PUT_LINE('== Hóa đơn của thành viên ' || V_MEMBER_ID || ' (Tạ Việt Phương) ==');
    -- 2) In toàn bộ hóa đơn
    FOR rec_inv IN (
        SELECT *
        FROM INVOICE
        WHERE MEMBER_ID = V_MEMBER_ID
        ORDER BY CREATION_TIME
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE(
                    '→ [' || rec_inv.ID || '] ' ||
                    'Thời gian: ' || TO_CHAR(rec_inv.CREATION_TIME, 'DD/MM/YYYY HH24:MI:SS') ||
                    ' | Net: ' || rec_inv.NET_AMOUNT ||
                    ' | Discount: ' || rec_inv.DISCOUNT ||
                    ' | Total Due: ' || rec_inv.TOTAL_DUE ||
                    ' | Status: ' || rec_inv.STATUS
            );

            -- 2.1) Chi tiết hóa đơn
            DBMS_OUTPUT.PUT_LINE('   -- Chi tiết hóa đơn:');
            FOR rec_d IN (
                SELECT PRODUCT_ID, QUANTITY_SOLD, UNIT_PRICE
                FROM INVOICE_DETAIL
                WHERE INVOICE_ID = rec_inv.ID
                )
                LOOP
                    DBMS_OUTPUT.PUT_LINE(
                            '      * Product: ' || rec_d.PRODUCT_ID ||
                            ' | Qty: ' || rec_d.QUANTITY_SOLD ||
                            ' | Price: ' || rec_d.UNIT_PRICE
                    );
                END LOOP;

            DBMS_OUTPUT.PUT_LINE('--------------------------------------------------');
        END LOOP;

    -- 3) In lịch sử thay đổi điểm
    DBMS_OUTPUT.PUT_LINE('== Lịch sử tích/trừ điểm của thành viên ==');
    FOR rec_pt IN (
        SELECT ID, INVOICE_ID, POINT_CHANGE
        FROM POINT_UPDATE_LOG
        WHERE MEMBER_ID = V_MEMBER_ID
        ORDER BY ID
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE(
                    '→ [' || rec_pt.ID || '] Invoice: ' || rec_pt.INVOICE_ID ||
                    ' | Point Change: ' || rec_pt.POINT_CHANGE
            );
        END LOOP;

    -- 4) In báo cáo ca tại cửa hàng VN000001
    DBMS_OUTPUT.PUT_LINE('== Báo cáo ca tại cửa hàng ' || V_STORE_ID || ' ==');
    FOR rec_sh IN (
        SELECT *
        FROM SHIFT_REPORT
        WHERE STORE_ID = V_STORE_ID
        ORDER BY START_TIME
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE(
                    '→ Shift ' || rec_sh.ID ||
                    ' | Start: ' || TO_CHAR(rec_sh.START_TIME, 'DD/MM/YYYY HH24:MI') ||
                    ' | End: ' || NVL(TO_CHAR(rec_sh.END_TIME, 'DD/MM/YYYY HH24:MI'), 'NULL') ||
                    ' | Cash: ' || rec_sh.CASH_REVENUE ||
                    ' | E-Wallet: ' || rec_sh.EWALLET_REVENUE ||
                    ' | Bank: ' || rec_sh.BANK_REVENUE ||
                    ' | Txns: ' || rec_sh.TRANSACTION_COUNT
            );
        END LOOP;

    -- 5) In tất cả paycheck
    DBMS_OUTPUT.PUT_LINE('== Danh sách tất cả Paycheck ==');
    FOR rec_pc IN (
        SELECT *
        FROM PAYCHECK
        ORDER BY PAY_DATE
        )
        LOOP
            DBMS_OUTPUT.PUT_LINE(
                    '→ Paycheck ' || rec_pc.ID ||
                    ' | Emp: ' || rec_pc.EMPLOYEE_ID ||
                    ' | PayDate: ' || TO_CHAR(rec_pc.PAY_DATE, 'DD/MM/YYYY') ||
                    ' | Gross: ' || rec_pc.GROSS_AMOUNT ||
                    ' | Deduct: ' || rec_pc.DEDUCTIONS ||
                    ' | Net: ' || rec_pc.NET_AMOUNT
            );
        END LOOP;

    -- 6) Kết thúc thành công
    DBMS_OUTPUT.PUT_LINE('== Demo hoàn thành ==');
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Lỗi demo: ' || SQLERRM);
END;
/