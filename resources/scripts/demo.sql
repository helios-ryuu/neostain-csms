-- Demo
DECLARE
    V_INVOICE_ID_1 INVOICE.ID%TYPE;
    V_INVOICE_ID_2 INVOICE.ID%TYPE;
    V_SHIFT_ID     SHIFT_REPORT.ID%TYPE;
    V_POINT_GAINED MEMBER.LOYALTY_POINTS%TYPE;
    V_MEMBER_ID    MEMBER.ID%TYPE             := '09412345672505180001';
    V_STORE_ID     SHIFT_REPORT.STORE_ID%TYPE := 'VN000001';
    V_EMPLOYEE_ID  EMPLOYEE.ID%TYPE           := '250518000001';
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