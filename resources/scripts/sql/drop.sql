-- 1. Drop tất cả các bảng cứng
BEGIN
    FOR tbl IN (
        SELECT table_name
        FROM user_tables
        WHERE table_name IN (
                             'PAYCHECK', 'TOKEN', 'POINT_UPDATE_LOG', 'INVOICE_DETAIL',
                             'SHIFT_REPORT', 'INVOICE', 'ACCOUNT', 'PROMOTION', 'MEMBER',
                             'PRODUCT', 'STORE', 'EMPLOYEE', 'CATEGORY', 'PAYMENT', 'ROLE'
            )
        )
        LOOP
            EXECUTE IMMEDIATE 'DROP TABLE ' || tbl.table_name || ' CASCADE CONSTRAINTS';
        END LOOP;
END;
/

-- 2. Purge recyclebin để đảm bảo xóa triệt để
PURGE recyclebin;

-- 3. Drop các đối tượng PL/SQL & schema-level khác
BEGIN
    FOR obj IN (
        SELECT object_name, object_type
        FROM user_objects
        WHERE object_type IN (
                              'SEQUENCE', 'TRIGGER', 'PROCEDURE', 'FUNCTION',
                              'PACKAGE', 'PACKAGE BODY', 'TYPE', 'TYPE BODY',
                              'VIEW', 'SYNONYM', 'MATERIALIZED VIEW'
            )
        )
        LOOP
            BEGIN
                EXECUTE IMMEDIATE
                    'DROP ' ||
                    CASE obj.object_type
                        WHEN 'SEQUENCE' THEN 'SEQUENCE '
                        WHEN 'TRIGGER' THEN 'TRIGGER '
                        WHEN 'PROCEDURE' THEN 'PROCEDURE '
                        WHEN 'FUNCTION' THEN 'FUNCTION '
                        WHEN 'PACKAGE BODY' THEN 'PACKAGE BODY '
                        WHEN 'PACKAGE' THEN 'PACKAGE '
                        WHEN 'TYPE BODY' THEN 'TYPE BODY '
                        WHEN 'TYPE' THEN 'TYPE '
                        WHEN 'VIEW' THEN 'VIEW '
                        WHEN 'MATERIALIZED VIEW' THEN 'MATERIALIZED VIEW '
                        WHEN 'SYNONYM' THEN 'SYNONYM '
                        END
                        || obj.object_name;
            EXCEPTION
                WHEN OTHERS THEN
                    NULL; -- Bỏ qua lỗi nếu object đã bị xóa
            END;
        END LOOP;
END;
/

-- 4. (Tuỳ chọn) Purge lại recyclebin để sạch hoàn toàn
PURGE recyclebin;

-- 5. Drop tất cả Scheduler Job trong schema
BEGIN
    FOR j IN (
        SELECT job_name
        FROM user_scheduler_jobs
        -- Nếu bạn chỉ muốn xóa job theo pattern, ví dụ JOB_RESET_%, thêm WHERE job_name LIKE 'JOB_RESET_%'
        )
        LOOP
            BEGIN
                DBMS_SCHEDULER.DROP_JOB(
                        job_name => j.job_name,
                        force => TRUE
                );
            EXCEPTION
                WHEN OTHERS THEN
                    NULL; -- Nếu job đang chạy hoặc không xóa được, bỏ qua
            END;
        END LOOP;
END;
/
