-- 1) Vô hiệu hoá tất cả FK
BEGIN
    FOR rec IN (
        SELECT constraint_name, table_name
        FROM user_constraints
        WHERE constraint_type = 'R'
        )
        LOOP
            EXECUTE IMMEDIATE
                'ALTER TABLE ' || rec.table_name ||
                ' DISABLE CONSTRAINT ' || rec.constraint_name;
        END LOOP;
END;
/


-- 2) Truncate mọi bảng trong schema
BEGIN
    FOR t IN (
        SELECT table_name
        FROM user_tables
        )
        LOOP
            EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || t.table_name;
        END LOOP;
END;
/

-- 3) Kích hoạt lại tất cả FK
BEGIN
    FOR rec IN (
        SELECT constraint_name, table_name
        FROM user_constraints
        WHERE constraint_type = 'R'
        )
        LOOP
            EXECUTE IMMEDIATE
                'ALTER TABLE ' || rec.table_name ||
                ' ENABLE CONSTRAINT ' || rec.constraint_name;
        END LOOP;
END;
/
