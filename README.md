# Cài đặt môi trường phát triển

- Git
- Java Runtime Environment (JRE)
- Java Development Kit (JDK)
- IntelliJ IDEA Ultimate
- Oracle Enterprise Edition 21
- Radmin VPN (Nếu chia sẻ cơ sở dữ liệu cho các máy khác qua mạng cục bộ

## Thiết lập Pluggable Database

### Bước 1: Mở SQL Plus và kết nối tới user SYS

```powershell
PS C:\Users\admin> SQLPLUS

SQL*Plus: Release 21.0.0.0.0 - Production on Mon Mar 17 09:31:09 2025
Version 21.3.0.0.0

Copyright (c) 1982, 2021, Oracle.  All rights reserved.

Enter user-name: /AS SYSDBA

Connected to:
Oracle Database 21c Enterprise Edition Release 21.0.0.0.0 - Production
Version 21.3.0.0.0

SQL>
```

### Bước 2: Tạo một pluggable database, mở nó và giữ trạng thái luôn mở cho cơ sở dữ liệu

```powershell
SQL> CREATE PLUGGABLE DATABASE NEOSTAIN_CSMS
  2  ADMIN USER ADMIN_CSMS IDENTIFIED BY 12345678 ROLES=(DBA)
  3  FILE_NAME_CONVERT=('PDBSEED', 'NEOSTAIN_CSMS');

Pluggable database created.

SQL> ALTER PLUGGABLE DATABASE NEOSTAIN_CSMS OPEN;

Pluggable database altered.

SQL> ALTER PLUGGABLE DATABASE NEOSTAIN_CSMS SAVE STATE;

Pluggable database altered.
```

### Bước 3: Kết nối tới database vừa tạo

```powershell
SQL> CONNECT CSMS_ADMIN/12345678@LOCALHOST:1521/NEOSTAIN_CSMS
Connected.
```

### Bước 4: Tạo một tablespace và đặt làm mặc định

```powershell
SQL> CREATE TABLESPACE CSMS_DATA DATAFILE 'csms_data.dbf' SIZE 100M AUTOEXTEND ON;

Tablespace created.

SQL> ALTER USER CSMS_ADMIN DEFAULT TABLESPACE CSMS_DATA;

User altered.
```

### Bước 5: Cấp quota cho tablespace vào người dùng

```powershell
SQL> ALTER USER CSMS_ADMIN QUOTA UNLIMITED ON CSMS_DATA;

User altered.
```

### Bước 6: Lần lượt chạy CREATE.sql -> SEQUENCE_TRIGGER.sql -> INSERT.sql để tạo các bảng và thêm dữ liệu (Nếu xảy ra lỗi thì chạy file DROP.sql để xóa bảng)

### Bước 7: Chạy file Main.java tại src/com/neostain/csms/Main.java
