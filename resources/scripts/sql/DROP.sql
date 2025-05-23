-- THIS FILE IS FINALIZED AND SEALED.
-- DO NOT MODIFY THIS FILE.
-- [SEALED BY HELIOS 23/5/2025] --

-- 1. DROP ALL TABLES
BEGIN
    FOR TBL IN (
        SELECT TABLE_NAME
        FROM USER_TABLES
        WHERE TABLE_NAME IN (
                             'INVENTORY_TRANSACTION', 'INVENTORY', 'ASSIGNMENT', 'PAYCHECK', 'TOKEN',
                             'POINT_UPDATE_LOG', 'INVOICE_DETAIL',
                             'SHIFT_REPORT', 'INVOICE', 'ACCOUNT', 'PROMOTION', 'MEMBER',
                             'PRODUCT', 'STORE', 'EMPLOYEE', 'CATEGORY', 'PAYMENT', 'ROLE'
            )
        )
        LOOP
            EXECUTE IMMEDIATE 'DROP TABLE ' || TBL.TABLE_NAME || ' CASCADE CONSTRAINTS';
        END LOOP;
END;
/

-- 2. PURGE RECYCLEBIN TO DELETE ALL OBJECTS COMPLETELY
PURGE RECYCLEBIN;

-- 3. DROP ALL OBJECTS PL/SQL & OTHERS OF SCHEMA-LEVEL
BEGIN
    FOR OBJ IN (
        SELECT OBJECT_NAME, OBJECT_TYPE
        FROM USER_OBJECTS
        WHERE OBJECT_TYPE IN (
                              'SEQUENCE', 'TRIGGER', 'PROCEDURE', 'FUNCTION',
                              'PACKAGE', 'PACKAGE BODY', 'TYPE', 'TYPE BODY',
                              'VIEW', 'SYNONYM', 'MATERIALIZED VIEW'
            )
        )
        LOOP
            BEGIN
                EXECUTE IMMEDIATE
                    'DROP ' ||
                    CASE OBJ.OBJECT_TYPE
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
                        || OBJ.OBJECT_NAME;
            EXCEPTION
                WHEN OTHERS THEN
                    NULL; -- SKIP ERRORS IF OBJECT HAS BEEN DROPPED
            END;
        END LOOP;
END;
/

-- 4. PURGE RECYCLEBIN TO DELETE ALL OBJECTS COMPLETELY
PURGE RECYCLEBIN;

-- 5. DROP ALL SCHEDULER JOB IN SCHEMA
BEGIN
    FOR J IN (
        SELECT JOB_NAME
        FROM USER_SCHEDULER_JOBS
        )
        LOOP
            BEGIN
                DBMS_SCHEDULER.DROP_JOB(
                        JOB_NAME => J.JOB_NAME,
                        FORCE => TRUE
                );
            EXCEPTION
                WHEN OTHERS THEN
                    NULL;
            END;
        END LOOP;
END;
/

-- 6. PURGE RECYCLEBIN TO DELETE ALL OBJECTS COMPLETELY
PURGE RECYCLEBIN;