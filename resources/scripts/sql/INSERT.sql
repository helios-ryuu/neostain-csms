-- ================================ SAMPLE DATASET ================================ --
-- ================================
-- 1. ROLE (Vai trò)
-- ================================
INSERT ALL INTO ROLE (NAME)
VALUES ('Nhân viên quản lý cửa hàng')
INTO ROLE (NAME)
VALUES ('Nhân viên bán hàng')
SELECT *
FROM DUAL;

-- ====================================
-- 2. PAYMENT (Phương thúc thanh toán)
-- ====================================
INSERT ALL INTO PAYMENT (NAME)
VALUES ('Tiền mặt')
INTO PAYMENT (NAME)
VALUES ('Ví điện tử')
INTO PAYMENT (NAME)
VALUES ('Chuyển khoản ngân hàng')
SELECT *
FROM DUAL;

-- ============================
-- 3. CATEGORY (Danh mục)
-- ============================
INSERT ALL INTO CATEGORY (NAME)
VALUES ('Thức ăn nhanh')
INTO CATEGORY (NAME)
VALUES ('Quần áo')
INTO CATEGORY (NAME)
VALUES ('Đồ đóng hộp')
INTO CATEGORY (NAME)
VALUES ('Thức uống')
SELECT *
FROM DUAL;

-- ============================
-- 4. EMPLOYEE (Nhân viên)
-- ============================
INSERT INTO EMPLOYEE (NAME, EMAIL, PHONE_NUMBER, ADDRESS, HOURLY_WAGE)
VALUES ('Ngô Tiến Sỹ', '23521367@gm.uit.edu.vn', '0941866722', 'A005, UIT-VNUHCM', 25000);

INSERT INTO EMPLOYEE (NAME, EMAIL, PHONE_NUMBER, ADDRESS, HOURLY_WAGE)
VALUES ('Ngô Vĩnh Khang', '23529999@gm.uit.edu.vn', '0777880736', 'A006, UIT-VNUHCM', 25000);

INSERT ALL
    INTO EMPLOYEE (MANAGER_ID, NAME, EMAIL, PHONE_NUMBER, ADDRESS, HOURLY_WAGE)
VALUES ((SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Ngô Tiến Sỹ'), 'Nguyễn Văn Nam', '23520982@gm.uit.edu.vn',
        '0987654321', 'B706, UIT-VNUHCM', 23000)
INTO EMPLOYEE (MANAGER_ID, NAME, EMAIL, PHONE_NUMBER, ADDRESS, HOURLY_WAGE)
VALUES ((SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Ngô Vĩnh Khang'), 'Võ Đức Tài', '23521381@gm.uit.edu.vn',
        '0123456789', 'C101, UIT-VNUHCM', 23000)
INTO EMPLOYEE (MANAGER_ID, NAME, EMAIL, PHONE_NUMBER, ADDRESS, HOURLY_WAGE)
VALUES ((SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Ngô Vĩnh Khang'), 'Thiều Đinh Nam Tài',
        '23521378@gm.uit.edu.vn', '0975312468', 'E201, UIT-VNUHCM', 23000)
SELECT *
FROM DUAL;

-- ============================
-- 5. PRODUCT (Hàng hoá)
-- ============================
-- Fast Food (CATEGORY_ID = 'C00001')
INSERT ALL INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789001', 'Bánh mì thịt', 30000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789002', 'Bánh mì chả', 35000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789003', 'Phở bò', 60000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789004', 'Phở gà', 55000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789005', 'Bún bò Huế', 65000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789006', 'Cơm tấm', 40000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789007', 'Xôi gà', 45000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789008', 'Hamburger', 70000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789009', 'Pizza', 80000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789010', 'Hot dog', 50000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789011', 'Gà rán', 75000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789012', 'Khoai tây chiên', 45000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789013', 'Xúc xích', 30000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789014', 'Gỏi cuốn', 40000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789015', 'Chả giò', 35000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789016', 'Mì xào', 55000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789017', 'Hủ tiếu', 60000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789018', 'Bánh cuốn', 40000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789019', 'Bánh hỏi', 45000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789020', 'Bánh xèo', 60000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789021', 'Bánh bao', 25000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789022', 'Bánh gối', 30000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789023', 'Xôi sườn', 50000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789024', 'Bún chả', 65000, 'C00001')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789025', 'Bánh tráng nướng', 40000, 'C00001')
SELECT *
FROM DUAL;

-- Clothing (CATEGORY_ID = 'C00002')
INSERT ALL INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789026', 'Áo thun', 150000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789027', 'Áo sơ mi', 250000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789028', 'Quần jean', 400000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789029', 'Quần tây', 350000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789030', 'Đầm', 450000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789031', 'Chân váy', 300000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789032', 'Áo khoác', 500000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789033', 'Áo len', 350000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789034', 'Quần short', 200000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789035', 'Áo ba lỗ', 100000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789036', 'Đồ bộ', 250000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789037', 'Khăn choàng', 200000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789038', 'Mũ lưỡi trai', 120000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789039', 'Giày thể thao', 600000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789040', 'Giày cao gót', 550000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789041', 'Dé bệt', 200000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789042', 'Sandal', 250000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789043', 'Túi xách', 400000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789044', 'Ví da', 350000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789045', 'Thắt lưng', 150000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789046', 'Áo hoodie', 400000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789047', 'Quần legging', 180000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789048', 'Áo vest', 600000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789049', 'Áo khoác jean', 450000, 'C00002')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789050', 'Áo choàng', 450000, 'C00002')
SELECT *
FROM DUAL;

-- Canned goods (CATEGORY_ID = 'C00003')
INSERT ALL INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789051', 'Sardine đóng hộp', 30000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789052', 'Thịt hộp', 40000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789053', 'Cá hộp', 35000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789054', 'Ngô đóng hộp', 20000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789055', 'Đậu đóng hộp', 18000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789056', 'Dưa hấu đóng hộp', 25000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789057', 'Bắp cải đóng hộp', 22000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789058', 'Nấm đóng hộp', 30000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789059', 'Xoài đóng hộp', 28000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789060', 'Đào đóng hộp', 30000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789061', 'Lạc đóng hộp', 20000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789062', 'Thịt heo đóng hộp', 45000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789063', 'Thịt gà đóng hộp', 42000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789064', 'Xốt cà chua đóng hộp', 15000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789065', 'Súp đóng hộp', 20000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789066', 'Cháo đóng hộp', 25000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789067', 'Rau muống đóng hộp', 22000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789068', 'Bí đỏ đóng hộp', 24000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789069', 'Đậu xanh đóng hộp', 18000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789070', 'Trái cây hỗn hợp đóng hộp', 35000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789071', 'Nước cốt dừa đóng hộp', 28000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789072', 'Cá ngừ đóng hộp', 40000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789073', 'Cá mòi đóng hộp', 30000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789074', 'Sườn heo đóng hộp', 50000, 'C00003')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789075', 'Xốt mayonnaise đóng hộp', 20000, 'C00003')
SELECT *
FROM DUAL;

-- Beverages (CATEGORY_ID = 'C00004')
INSERT ALL INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789076', 'Nước suối', 10000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789077', 'Nước ngọt Coca-Cola', 15000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789078', 'Nước ngọt Pepsi', 15000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789079', 'Trà xanh', 12000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789080', 'Trà sữa', 20000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789081', 'Cà phê đá', 18000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789082', 'Sinh tố bơ', 25000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789083', 'Sinh tố dâu', 25000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789084', 'Nước ép cam', 30000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789085', 'Nước ép táo', 30000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789086', 'Bia hơi', 12000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789087', 'Bia lon', 18000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789088', 'Rượu vang', 150000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789089', 'Sữa tươi', 18000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789090', 'Sữa chua uống', 20000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789091', 'Nước yến', 30000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789092', 'Nước dừa', 20000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789093', 'Nước mát', 12000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789094', 'Nước tăng lực', 20000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789095', 'Nước chanh dây', 20000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789096', 'Trà đá', 5000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789097', 'Trà chanh', 10000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789098', 'Trà ô long', 15000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789099', 'Trà thảo mộc', 18000, 'C00004')
INTO PRODUCT (ID, NAME, UNIT_PRICE, CATEGORY_ID)
VALUES ('0123456789100', 'Cà phê pha phin', 20000, 'C00004')
SELECT *
FROM DUAL;

-- ====================================
-- 6. MEMBER (Khách hàng thành viên)
-- ====================================
INSERT ALL INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Vũ Minh Sang', '0941234567', 'sangvm@uit.edu.vn')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Nguyễn Văn A', '0941234500', 'nguyenvana00@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Trần Thị B', '0941234501', 'tranthib01@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Lê Văn C', '0941234502', 'levanc02@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Phạm Thị D', '0941234503', 'phamthid03@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Hoàng Văn E', '0941234504', 'hoangvane04@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Vũ Thị F', '0941234505', 'vuthif05@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Đặng Văn G', '0941234506', 'dangvang06@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Bùi Thị H', '0941234507', 'buithih07@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Đỗ Văn I', '0941234508', 'dovani08@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Phan Thị K', '0941234509', 'phanthik09@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Nguyễn Thị L', '0941234510', 'nguyenthil10@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Trần Văn M', '0941234511', 'tranvanm11@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Lê Thị N', '0941234512', 'lethin12@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Phạm Văn O', '0941234513', 'phamvano13@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Hoàng Thị P', '0941234514', 'hoangthip14@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Vũ Văn Q', '0941234515', 'vuvanq15@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Đặng Thị R', '0941234516', 'dangthir16@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Bùi Văn S', '0941234517', 'buivans17@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Đỗ Thị T', '0941234518', 'dothit18@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Phan Văn U', '0941234519', 'phanvanu19@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Nguyễn Văn V', '0941234520', 'nguyenvanv20@member.com')
INTO MEMBER (NAME, PHONE_NUMBER, EMAIL)
VALUES ('Trần Thị W', '0941234521', 'tranthiw21@member.com')
SELECT *
FROM DUAL;

-- ====================================
-- 7. PROMOTION (Khuyến mãi)
-- ====================================
INSERT ALL
-- Mua 2 tặng 1 cùng loại
    INTO PROMOTION (NAME, START_TIME, END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID,
                    PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE)
VALUES ('Khuyến mãi Mùa Xuân', TIMESTAMP '2025-05-15 00:00:00', TIMESTAMP '2025-06-30 23:59:59', '0123456789001', 3,
        '0123456789001', 1, 0)

-- Mua 2 tặng 1 khác loại
INTO PROMOTION (NAME, START_TIME, END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID,
                PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE)
VALUES ('Khuyến mãi Mùa Hạ', TIMESTAMP '2025-05-15 00:00:00', TIMESTAMP '2025-06-30 23:59:59', '0123456789002', 2,
        '0123456789009', 1, 0)

-- Mua 2 giảm 10%
INTO PROMOTION (NAME, START_TIME, END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID,
                PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE)
VALUES ('Khuyến mãi Mùa Thu', TIMESTAMP '2025-05-15 00:00:00', TIMESTAMP '2025-06-30 23:59:59', '0123456789003', 3,
        NULL, 0, 0.1)

-- Mua 3 giảm 20% tặng 1 khác loại
INTO PROMOTION (NAME, START_TIME, END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID,
                PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE)
VALUES ('Khuyến mãi Mùa Đông', TIMESTAMP '2025-05-15 00:00:00', TIMESTAMP '2025-06-30 23:59:59', '0123456789004', 3,
        '0123456789008', 2, 0.2)
SELECT *
FROM DUAL;

-- Promotion hết hạn
INSERT ALL
-- Mua 2 tặng 1 cùng loại
    INTO PROMOTION (NAME, START_TIME, END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID,
                    PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE)
VALUES ('Khuyến mãi Buổi sáng', TIMESTAMP '2025-05-15 00:00:00', TIMESTAMP '2025-05-30 23:59:59', '0123456789001', 2,
        '0123456789001', 1, 0)

-- Mua 2 tặng 1 khác loại
INTO PROMOTION (NAME, START_TIME, END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID,
                PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE)
VALUES ('Khuyến mãi Buổi trưa', TIMESTAMP '2025-05-15 00:00:00', TIMESTAMP '2025-05-30 23:59:59', '0123456789002', 2,
        '0123456789009', 1, 0)

-- Mua 2 giảm 10%
INTO PROMOTION (NAME, START_TIME, END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID,
                PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE)
VALUES ('Khuyến mãi Buổi chiều', TIMESTAMP '2025-05-15 00:00:00', TIMESTAMP '2025-05-30 23:59:59', '0123456789003', 2,
        NULL, 0, 0.1)

-- Mua 3 giảm 20% tặng 1 khác loại
INTO PROMOTION (NAME, START_TIME, END_TIME, PRODUCT_ID, MINIMUM_PURCHASE_QUANTITY, PROMO_PRODUCT_ID,
                PROMO_PRODUCT_QUANTITY, DISCOUNT_RATE)
VALUES ('Khuyến mãi Buổi tối', TIMESTAMP '2025-05-15 00:00:00', TIMESTAMP '2025-05-30 23:59:59', '0123456789004', 2,
        '0123456789008', 1, 0.2)
SELECT *
FROM DUAL;

-- ============================
-- 8. STORE (Cửa hàng)
-- ============================
INSERT INTO STORE (NAME, ADDRESS, MANAGER_ID)
VALUES ('NeoStain UIT', 'Khu phố 6, P.Linh Trung, Tp.Thủ Đức, TP. Hồ Chí Minh',
        (SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Ngô Tiến Sỹ'));
INSERT INTO STORE (NAME, ADDRESS, MANAGER_ID)
VALUES ('NeoStain UEH', '59C Nguyễn Ðình Chiểu, phường Võ Thị Sáu, Quận 3, TP. Hồ Chí Minh',
        (SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Ngô Vĩnh Khang'));

-- ====================================
-- 9. INVENTORY_TRANSACTION (Giao dịch kho)
-- ====================================
BEGIN
    FOR REC IN (
        SELECT ID
        FROM PRODUCT
        )
        LOOP
            INSERT INTO INVENTORY_TRANSACTION (PRODUCT_ID, STORE_ID, TRANSACTION_TYPE, QUANTITY)
            VALUES (REC.ID, 'VN000001', 'NHẬP KHO', 30);
            INSERT INTO INVENTORY_TRANSACTION (PRODUCT_ID, STORE_ID, TRANSACTION_TYPE, QUANTITY)
            VALUES (REC.ID, 'VN000002', 'NHẬP KHO', 30);
        END LOOP;
END;
/

-- ====================================
-- 10. ACCOUNT (Tài khoản)
-- ====================================
INSERT INTO ACCOUNT (EMPLOYEE_ID, USERNAME, PASSWORD_HASH, ROLE_ID)
VALUES ((SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Ngô Tiến Sỹ'), 'iamsm1',
        'ebc7998126ef71e7d1b8e33ad262150833078b3bc1f493d0692599d80082a7dc', 'R001');
INSERT INTO ACCOUNT (EMPLOYEE_ID, USERNAME, PASSWORD_HASH, ROLE_ID)
VALUES ((SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Ngô Vĩnh Khang'), 'iamsm2',
        'ebc7998126ef71e7d1b8e33ad262150833078b3bc1f493d0692599d80082a7dc', 'R001');
INSERT INTO ACCOUNT (EMPLOYEE_ID, USERNAME, PASSWORD_HASH, ROLE_ID)
VALUES ((SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Nguyễn Văn Nam'), 'iamcs1',
        'ebc7998126ef71e7d1b8e33ad262150833078b3bc1f493d0692599d80082a7dc', 'R002');
INSERT INTO ACCOUNT (EMPLOYEE_ID, USERNAME, PASSWORD_HASH, ROLE_ID)
VALUES ((SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Võ Đức Tài'), 'iamcs2',
        'ebc7998126ef71e7d1b8e33ad262150833078b3bc1f493d0692599d80082a7dc', 'R002');
INSERT INTO ACCOUNT (EMPLOYEE_ID, USERNAME, PASSWORD_HASH, ROLE_ID)
VALUES ((SELECT ID FROM EMPLOYEE WHERE EMPLOYEE.NAME = 'Thiều Đinh Nam Tài'), 'iamcs3',
        'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'R002');

-- ====================================
-- 11. ASSIGNMENT (Phân công)
-- ====================================
BEGIN
    FOR REC IN (
        SELECT ID
        FROM EMPLOYEE
        )
        LOOP
            INSERT INTO ASSIGNMENT (EMPLOYEE_ID, STORE_ID, START_TIME, END_TIME)
            VALUES (REC.ID, 'VN000001', SYSTIMESTAMP, SYSTIMESTAMP + INTERVAL '8' HOUR);
        END LOOP;

    FOR REC IN (
        SELECT ID
        FROM EMPLOYEE
        )
        LOOP
            INSERT INTO ASSIGNMENT (EMPLOYEE_ID, STORE_ID, START_TIME, END_TIME)
            VALUES (REC.ID, 'VN000002', SYSTIMESTAMP, SYSTIMESTAMP + INTERVAL '8' HOUR);
        END LOOP;
END;
/

-- ====================================
-- 12, 13, 14, 15, 16: TOKEN, SHIFT_REPORT, INVOICE, INVOICE_DETAIL, POINT_UPDATE_LOG
-- ====================================
DECLARE
    V_USERNAME            VARCHAR2(50) := 'iamsm1';
    V_PASSWORD            VARCHAR2(50) := 'Java@123';
    V_STORE_ID            CHAR(8)      := 'VN000001';
    V_INPUT_PASSWORD_HASH VARCHAR2(64);
    V_USER_PASSWORD_HASH  VARCHAR2(64);
    V_EMPLOYEE_ID         CHAR(12);
    V_TOKEN_VALUE         VARCHAR2(64);
    V_INVOICE_ID          CHAR(20);
    V_MEMBER_ID           CHAR(20);
    V_PAYMENT_ID          CHAR(4);
    V_SHIFT_REPORT_ID     CHAR(30);
    V_PAYCHECK_ID         CHAR(24);
BEGIN
    SELECT RAWTOHEX(STANDARD_HASH(V_PASSWORD, 'SHA256'))
    INTO V_INPUT_PASSWORD_HASH
    FROM DUAL;

    SELECT PASSWORD_HASH
    INTO V_USER_PASSWORD_HASH
    FROM ACCOUNT
    WHERE USERNAME = V_USERNAME;

    IF (LOWER(V_INPUT_PASSWORD_HASH) = V_USER_PASSWORD_HASH) THEN
        -- LOGIN
        SELECT LOWER(RAWTOHEX(STANDARD_HASH(SYSTIMESTAMP || V_USERNAME, 'SHA256')))
        INTO V_TOKEN_VALUE
        FROM DUAL;

        INSERT INTO TOKEN(USERNAME, VALUE, EXPIRES_AT)
        VALUES (V_USERNAME, V_TOKEN_VALUE, SYSTIMESTAMP + INTERVAL '7' DAY);

        SELECT EMPLOYEE_ID
        INTO V_EMPLOYEE_ID
        FROM ACCOUNT
        WHERE USERNAME = V_USERNAME;

        PRC_INITIATE_SHIFT(V_STORE_ID, V_EMPLOYEE_ID, V_SHIFT_REPORT_ID);

        UPDATE EMPLOYEE
        SET STATUS = 'ĐANG HOẠT ĐỘNG'
        WHERE ID = V_EMPLOYEE_ID;

        UPDATE ACCOUNT
        SET STATUS = 'ĐANG HOẠT ĐỘNG'
        WHERE USERNAME = V_USERNAME;

        COMMIT;

        -- PAYMENT PROCESS
        SELECT ID
        INTO V_PAYMENT_ID
        FROM PAYMENT
        WHERE NAME = 'Tiền mặt';

        SELECT ID
        INTO V_MEMBER_ID
        FROM MEMBER
        WHERE NAME = 'Vũ Minh Sang';
        -- INVOICE 1
        PRC_INITIATE_INVOICE(V_STORE_ID,
                             V_MEMBER_ID,
                             V_PAYMENT_ID,
                             V_EMPLOYEE_ID,
                             0,
                             V_INVOICE_ID
        );
        PRC_ADD_ITEM_TO_INVOICE(V_INVOICE_ID, '0123456789001', 3);
        PRC_ADD_ITEM_TO_INVOICE(V_INVOICE_ID, '0123456789003', 3);
        PRC_ADD_GIFT_TO_INVOICE(V_INVOICE_ID, '0123456789001', 1);
        PRC_CALC_TOTAL(V_INVOICE_ID);
        COMMIT;

        -- INVOICE 2
        PRC_INITIATE_INVOICE(V_STORE_ID,
                             V_MEMBER_ID,
                             V_PAYMENT_ID,
                             V_EMPLOYEE_ID,
                             100,
                             V_INVOICE_ID
        );
        PRC_ADD_ITEM_TO_INVOICE(V_INVOICE_ID, '0123456789025', 3);
        PRC_ADD_ITEM_TO_INVOICE(V_INVOICE_ID, '0123456789003', 3);
        PRC_CALC_TOTAL(V_INVOICE_ID);
        COMMIT;

        -- INVOICE 3
        PRC_INITIATE_INVOICE(V_STORE_ID,
                             V_MEMBER_ID,
                             V_PAYMENT_ID,
                             V_EMPLOYEE_ID,
                             150,
                             V_INVOICE_ID
        );
        PRC_ADD_ITEM_TO_INVOICE(V_INVOICE_ID, '0123456789020', 3);
        PRC_ADD_ITEM_TO_INVOICE(V_INVOICE_ID, '0123456789003', 3);
        PRC_CALC_TOTAL(V_INVOICE_ID);
        COMMIT;

        -- LOGOUT
        PRC_END_SHIFT(V_SHIFT_REPORT_ID);

        UPDATE EMPLOYEE
        SET STATUS = 'TẠM NGỪNG HOẠT ĐỘNG'
        WHERE ID = V_EMPLOYEE_ID;

        UPDATE ACCOUNT
        SET STATUS = 'NGỪNG HOẠT ĐỘNG'
        WHERE USERNAME = V_USERNAME;

        UPDATE TOKEN
        SET STATUS = 'VÔ HIỆU'
        WHERE VALUE = V_TOKEN_VALUE;

        PRC_CALC_PAYCHECK(V_EMPLOYEE_ID,
                          0,
                          SYSTIMESTAMP - INTERVAL '15' DAY,
                          SYSTIMESTAMP + INTERVAL '15' DAY,
                          V_PAYCHECK_ID
        );
        COMMIT;
    END IF;

    -- DEMO
    BEGIN
        DBMS_OUTPUT.PUT_LINE('1. FNC_DAY_REVENUE                = ' ||
                             FNC_DAY_REVENUE(SYSDATE));

        DBMS_OUTPUT.PUT_LINE('2. FNC_DAY_INVOICE_COUNT          = ' ||
                             FNC_DAY_INVOICE_COUNT(SYSDATE));

        DBMS_OUTPUT.PUT_LINE('3. FNC_TOTAL_MEMBERS              = ' ||
                             FNC_TOTAL_MEMBERS);

        DBMS_OUTPUT.PUT_LINE('4. FNC_TOTAL_VIP_MEMBERS          = ' ||
                             FNC_TOTAL_VIP_MEMBERS);

        DBMS_OUTPUT.PUT_LINE('5. FNC_TOTAL_PRODUCTS             = ' ||
                             FNC_TOTAL_PRODUCTS(V_STORE_ID));

        DBMS_OUTPUT.PUT_LINE('6. FNC_TOTAL_EMPLOYEES            = ' ||
                             FNC_TOTAL_EMPLOYEES(V_STORE_ID));

        DBMS_OUTPUT.PUT_LINE('7. FNC_CANCELED_INVOICES          = ' ||
                             FNC_CANCELED_INVOICES);

        DBMS_OUTPUT.PUT_LINE('8. FNC_UNCOMPLETED_INVOICES       = ' ||
                             FNC_UNCOMPLETED_INVOICES);

        DBMS_OUTPUT.PUT_LINE('9. FNC_TOTAL_INVOICES_LAST_30D    = ' ||
                             FNC_TOTAL_INVOICES_LAST_30D);

        DBMS_OUTPUT.PUT_LINE('10. FNC_TOTAL_REVENUE_LAST_30D    = ' ||
                             FNC_TOTAL_REVENUE_LAST_30D);

        DBMS_OUTPUT.PUT_LINE('11. FNC_CANCEL_REQUESTED_INVOICES = ' ||
                             FNC_CANCEL_REQUESTED_INVOICES);
    END;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
END;
/