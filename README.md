# 🟢 NeoStain CSMS – Hệ Thống Quản Lý Cửa Hàng Tiện Lợi

NeoStain CSMS (Convenience Store Management System) là một ứng dụng Java tích hợp quản lý dữ liệu cửa hàng, nhân viên, kho hàng, tài khoản người dùng, và các báo cáo thống kê.

---

## 🚀 Yêu Cầu Hệ Thống

Để cài đặt và chạy NeoStain CSMS, bạn cần có:

* **Oracle Database 21c Enterprise Edition**
* **Java Runtime Environment (JRE) >= 17**
* **Java Development Kit (JDK) >= 17** *(chỉ cần nếu phát triển hoặc debug)*
* **Git** *(nếu muốn clone trực tiếp mã nguồn)*
* **IntelliJ IDEA Ultimate** *(khuyến nghị cho việc phát triển)*

### 🔶 Kiểm tra phiên bản Java

#### 🔹 Sau khi tải **Java Runtime Environment (JRE)** và **Java Development Kit (JDK)**, có thể kiểm tra phiên bản bằng lệnh dưới đây:

```powershell
java -version
```

---

## 🔧 Hướng Dẫn Cài Đặt

### 1️⃣ Cài đặt Oracle Database

* [Tải Oracle 21c](https://www.oracle.com/database/technologies/oracle21c-windows-downloads.html) và cài đặt theo hướng dẫn.
* Đảm bảo Oracle Listener hoạt động trên cổng `1521`.

### 2️⃣ Thiết Lập Cơ Sở Dữ Liệu

#### 🔹 Bước 1: Mở SQL\*Plus và kết nối với quyền SYSDBA

```powershell
SQLPLUS /AS SYSDBA
```

#### 🔹 Bước 2: Tạo Pluggable Database `NEOSTAIN_CSMS`

```sql
CREATE PLUGGABLE DATABASE NEOSTAIN_CSMS
  ADMIN USER CSMS_ADMIN IDENTIFIED BY 12345678 ROLES=(DBA)
  FILE_NAME_CONVERT=('PDBSEED', 'NEOSTAIN_CSMS');

ALTER PLUGGABLE DATABASE NEOSTAIN_CSMS OPEN;
ALTER PLUGGABLE DATABASE NEOSTAIN_CSMS SAVE STATE;
```

#### 🔹 Bước 3: Kết nối tới PDB

```sql
CONNECT CSMS_ADMIN/12345678@localhost:1521/NEOSTAIN_CSMS
```

#### 🔹 Bước 4: Tạo tablespace và cấp quyền

```sql
CREATE TABLESPACE CSMS_DATA 
  DATAFILE 'csms_data.dbf' SIZE 100M AUTOEXTEND ON;

ALTER USER CSMS_ADMIN DEFAULT TABLESPACE CSMS_DATA;
ALTER USER CSMS_ADMIN QUOTA UNLIMITED ON CSMS_DATA;
```

#### 🔹 Bước 5: Khởi tạo cơ sở dữ liệu

Chạy tuần tự các script SQL theo đúng thứ tự sau:

* `CREATE.sql` – tạo bảng và cấu trúc.
* `SEQUENCE_TRIGGER.sql` – tạo sequence và trigger.
* `PROCEDURE.sql` – tạo stored procedures.
* `FUNCTION.sql` – tạo functions.
* `SCHEDULE_JOBS.sql` – tạo job theo lịch.
* `INSERT.sql` – thêm dữ liệu mẫu.

🔁 **Nếu gặp lỗi:** Chạy `DROP.sql` để xóa toàn bộ cấu trúc và bắt đầu lại.

---

## ▶️ Hướng Dẫn Chạy Ứng Dụng (sử dụng JAR file)

* **File ứng dụng:** `neostain-csms.jar`

### Bước 1: Đảm bảo Java Runtime Environment (JRE >=17) đã được cài đặt.

### Bước 2: Chạy ứng dụng từ dòng lệnh

* Mở Terminal hoặc CMD và chạy lệnh sau:

```powershell
java -jar neostain-csms.jar
```

Ứng dụng sẽ khởi động và sẵn sàng hoạt động.

---

## 🌐 Chia Sẻ Cơ Sở Dữ Liệu Qua Mạng LAN (tùy chọn)

Để chia sẻ và truy cập CSDL từ máy khác:

* Cài đặt **Radmin VPN** và tạo mạng LAN.
* Đảm bảo Oracle Listener cho phép kết nối từ xa.
* Mở cổng `1521` trên Firewall.

---

## 🔒 Thông Tin Đăng Nhập Mặc Định

* **Mật khẩu mặc định cho tất cả tài khoản:**

  * Password: `Java@123`

---

## 📝 Phiên bản hiện tại: **NeoStain CSMS v1.0.1 June 2025 - Official Stable Release**
