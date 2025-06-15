-- File: ss-tx-2.sql
-- CHƯƠNG 3: XỬ LÝ TRUY XUẤT ĐỒNG THỜI  (SESSION B)
-- Chú thích: TS = timestamp (thứ tự thực thi)
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
/*------------------------------------------------------------------*
 | 1. LOST UPDATE
 *------------------------------------------------------------------*/
-- 1.1 Demo LOST UPDATE
-- SESSION B
-- TS(2)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';
-- đọc dữ liệu cũ

-- TS(4)
UPDATE MEMBER
SET NAME = 'Dương Văn Lươn' -- B cập nhật tên
WHERE PHONE_NUMBER = '0941234500';
-- TS(6)
COMMIT;
-- commit TS(4)

-- TS(8)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';


/*------------------------------------------------------*
 | 1.2 Giải pháp LOST UPDATE
 *------------------------------------------------------*/
-- 1.2.1  Tăng mức CÔ LẬP dữ liệu
-- SESSION B
-- TS(2)
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
-- TS(4)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';
-- TS(6)
UPDATE MEMBER
SET NAME = 'Dương Văn Lươn'
WHERE PHONE_NUMBER = '0941234500';
-- có thể 0 rows affected
-- TS(8)
COMMIT;
-- hoặc ORA-08177 tuỳ thời điểm A commit

-- TS(10)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';

---------------------------------------------------------
-- 1.2.2  Pessimistic Locking
-- SESSION B
-- TS(2)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500'
    FOR UPDATE;
-- block tới khi A commit

-- TS(6)
UPDATE MEMBER
SET NAME = 'Dương Văn Lươn'
WHERE PHONE_NUMBER = '0941234500';
-- TS(7)
COMMIT;
-- TS(8)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';

---------------------------------------------------------
-- 1.2.3  Optimistic Locking
--   Đảm bảo cột VERSION đã tồn tại & khởi tạo (thao tác này chỉ cần chạy 1 lần)

-- SESSION B
-- TS(2)
SELECT ID, NAME, VERSION
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';

-- TS(4)
UPDATE MEMBER
SET NAME    = 'Đinh Linh La',
    VERSION = VERSION + 1
WHERE PHONE_NUMBER = '0941234500'
  AND VERSION = 0;
-- nếu A vừa tăng VERSION, câu này = 0 row
-- TS(7)
SELECT ID, NAME, VERSION
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';



/*------------------------------------------------------------------*
 | 2. NON-REPEATABLE READ
 *------------------------------------------------------------------*/
-- SESSION B
-- TS(2)
UPDATE PRODUCT
SET UNIT_PRICE = 3000000 -- B cập nhật giá
WHERE ID = '0123456789001';
-- TS(3)
COMMIT;


---------------------------------------------------------
-- 2.1  Giải pháp NON-REPEATABLE READ
-- SESSION B
-- TS(3)
UPDATE PRODUCT
SET UNIT_PRICE = 30000002
WHERE ID = '0123456789001';
-- TS(4)
COMMIT;



/*------------------------------------------------------------------*
 | 3. PHANTOM READ
 *------------------------------------------------------------------*/
-- SESSION B
-- TS(2)
INSERT INTO INVENTORY
    (PRODUCT_ID, STORE_ID, QUANTITY)
VALUES ('0123456789100', 'VN000002', 1);
-- thêm bản ghi
-- TS(3)
COMMIT;


---------------------------------------------------------
-- 3.1  Giải pháp PHANTOM READ
-- SESSION B
-- TS(3)
INSERT INTO INVENTORY
    (PRODUCT_ID, STORE_ID, QUANTITY)
VALUES ('0123456789100', 'VN000002', 1);
-- có thể block / ORA-08177
-- TS(4)
COMMIT;



/*------------------------------------------------------------------*
 | 4. DEADLOCK
 *------------------------------------------------------------------*/
-- SESSION B
-- TS(2)
UPDATE INVENTORY
SET QUANTITY = QUANTITY + 5
WHERE PRODUCT_ID = '0123456789002';
-- lock NL02

-- TS(3)
UPDATE INVENTORY
SET QUANTITY = QUANTITY + 5555555
WHERE PRODUCT_ID = '0123456789001';
-- chờ → DEADLOCK (ORA-00060)


/*------------------------------------------------------*
 | 4.2  Giải pháp DEADLOCK
 *------------------------------------------------------*/
-- 4.2.1  Thực hiện theo THỨ TỰ NHẤT QUÁN
--   (label sửa thành SESSION B cho khớp ngữ cảnh)
-- SESSION B
-- TS(2)
UPDATE INVENTORY
SET QUANTITY = QUANTITY + 5555555
WHERE PRODUCT_ID = '0123456789001';
-- TS(3)
UPDATE INVENTORY
SET QUANTITY = QUANTITY + 5
WHERE PRODUCT_ID = '0123456789002';
-- TS(6)
COMMIT;

---------------------------------------------------------
-- 4.2.2  Pessimistic Locking
-- SESSION B
-- TS(2)
SELECT ID, PRODUCT_ID, QUANTITY
FROM INVENTORY
WHERE PRODUCT_ID IN ('0123456789001', '0123456789002')
    FOR UPDATE;
-- block tới khi A commit

-- TS(4)
UPDATE INVENTORY
SET QUANTITY = 25
WHERE PRODUCT_ID = '0123456789002';
-- có thể không thực thi
-- TS(5)
UPDATE INVENTORY
SET QUANTITY = 333
WHERE PRODUCT_ID = '0123456789001';
-- TS(8)
SELECT ID, PRODUCT_ID, QUANTITY
FROM INVENTORY
WHERE PRODUCT_ID IN ('0123456789001', '0123456789002');
-- TS(9)
COMMIT;
