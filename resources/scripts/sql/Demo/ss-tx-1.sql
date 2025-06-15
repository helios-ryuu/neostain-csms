-- File: ss-tx-1.sql
-- CHƯƠNG 3: XỬ LÝ TRUY XUẤT ĐỒNG THỜI  (SESSION A)
-- Chú thích: TS = timestamp (thứ tự thực thi)
-- GỢI Ý: bật dòng sau để shell thoát ngay khi có ORA-08177/00060
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
/*------------------------------------------------------------------*
 | 1. LOST UPDATE
 *------------------------------------------------------------------*/
-- 1.1 Demo LOST UPDATE
-- SESSION A
-- TS(1)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';
-- đọc dữ liệu cũ

-- TS(3)
UPDATE MEMBER
SET NAME = 'Nguyễn Thị B' -- A cập nhật tên
WHERE PHONE_NUMBER = '0941234500';

-- TS(5)
COMMIT;
-- commit TS(3)

-- TS(7)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';


/*------------------------------------------------------*
 | 1.2 Giải pháp LOST UPDATE
 *------------------------------------------------------*/
-- 1.2.1  Tăng mức CÔ LẬP dữ liệu (SERIALIZABLE)
-- SESSION A
-- TS(1)
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
-- bắt đầu TX ở mức SERIALIZABLE
-- TS(3)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';
-- TS(5)
UPDATE MEMBER
SET NAME = 'Nguyễn Thị B'
WHERE PHONE_NUMBER = '0941234500';
-- TS(7)
COMMIT;
-- Nếu session B đã thay đổi, COMMIT này sẽ báo ORA-08177

-- TS(9)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';

---------------------------------------------------------
-- 1.2.2  Pessimistic Locking
-- SESSION A
-- TS(1)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500'
    FOR UPDATE;
-- giữ lock

-- TS(3)
UPDATE MEMBER
SET NAME = 'Nguyễn Thị B'
WHERE PHONE_NUMBER = '0941234500';
-- TS(4)
COMMIT;
-- TS(5)
SELECT ID, NAME
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';

---------------------------------------------------------
-- 1.2.3  Optimistic Locking
--   Thêm & khởi tạo cột VERSION cho toàn bộ bảng
ALTER TABLE MEMBER
    ADD VERSION NUMBER DEFAULT 0;
UPDATE MEMBER
SET VERSION = 0
WHERE VERSION IS NULL;
COMMIT;

-- SESSION A
-- TS(1)
SELECT ID, NAME, VERSION
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';

-- TS(3)
UPDATE MEMBER
SET NAME    = 'Đinh Linh La',
    VERSION = VERSION + 1
WHERE PHONE_NUMBER = '0941234500'
  AND VERSION = 0;
-- đảm bảo không bị ghi đè
-- TS(5)
COMMIT;

-- TS(6)
SELECT ID, NAME, VERSION
FROM MEMBER
WHERE PHONE_NUMBER = '0941234500';



/*------------------------------------------------------------------*
 | 2. NON-REPEATABLE READ
 *------------------------------------------------------------------*/
-- SESSION A
-- TS(1)
SELECT ID, UNIT_PRICE
FROM PRODUCT
WHERE ID = '0123456789001';
-- lần 1: giá cũ

-- (chờ SESSION B thay đổi giá)
-- TS(4)
SELECT ID, UNIT_PRICE
FROM PRODUCT
WHERE ID = '0123456789001';
-- lần 2: giá mới
-- TS(5)
COMMIT;


---------------------------------------------------------
-- 2.1  Giải pháp NON-REPEATABLE READ
-- SESSION A
-- TS(1)
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
-- TS(2)
SELECT ID, UNIT_PRICE
FROM PRODUCT
WHERE ID = '0123456789001';
-- giá cũ
-- TS(5)
SELECT ID, UNIT_PRICE
FROM PRODUCT
WHERE ID = '0123456789001';
-- vẫn giá cũ (đọc nhất quán)
-- TS(6)
COMMIT;



/*------------------------------------------------------------------*
 | 3. PHANTOM READ
 *------------------------------------------------------------------*/
-- SESSION A
-- TS(1)
SELECT *
FROM INVENTORY
WHERE PRODUCT_ID = '0123456789100';
-- lần 1: 2 bản ghi

-- (chờ SESSION B insert thêm)
-- TS(4)
SELECT *
FROM INVENTORY
WHERE PRODUCT_ID = '0123456789100';
-- lần 2: có bản ghi mới “bóng ma”
-- TS(5)
COMMIT;


---------------------------------------------------------
-- 3.1  Giải pháp PHANTOM READ
-- SESSION A
-- TS(1)
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
-- TS(2)
SELECT *
FROM INVENTORY
WHERE PRODUCT_ID = '0123456789100';
-- chỉ thấy 1 bản ghi
-- TS(5)
SELECT *
FROM INVENTORY
WHERE PRODUCT_ID = '0123456789100';
-- TS(6)
COMMIT;



/*------------------------------------------------------------------*
 | 4. DEADLOCK
 *------------------------------------------------------------------*/
-- SESSION A
-- TS(1)
UPDATE INVENTORY
SET QUANTITY = QUANTITY - 5
WHERE PRODUCT_ID = '0123456789001';
-- lock NL01

-- TS(4)
UPDATE INVENTORY
SET QUANTITY = QUANTITY - 2
WHERE PRODUCT_ID = '0123456789002';
-- chờ → DEADLOCK (ORA-00060)


/*------------------------------------------------------*
 | 4.2  Giải pháp DEADLOCK
 *------------------------------------------------------*/
-- 4.2.1  Thực hiện theo THỨ TỰ NHẤT QUÁN
--   Cập nhật hai hàng theo cùng thứ tự tại cả hai session
-- SESSION A
-- TS(1)
UPDATE INVENTORY
SET QUANTITY = QUANTITY - 5
WHERE PRODUCT_ID = '0123456789001';
-- TS(4)
UPDATE INVENTORY
SET QUANTITY = QUANTITY - 3
WHERE PRODUCT_ID = '0123456789002';
-- TS(5)
COMMIT;

---------------------------------------------------------
-- 4.2.2  Pessimistic Locking
-- SESSION A
-- TS(1)
SELECT ID, PRODUCT_ID, QUANTITY
FROM INVENTORY
WHERE PRODUCT_ID IN ('0123456789001', '0123456789002')
    FOR UPDATE;
-- giữ lock cả hai bản ghi
-- TS(3)
UPDATE INVENTORY
SET QUANTITY = 25
WHERE PRODUCT_ID = '0123456789001';
-- TS(6)
UPDATE INVENTORY
SET QUANTITY = 245
WHERE PRODUCT_ID = '0123456789002';
-- TS(7)
COMMIT;
