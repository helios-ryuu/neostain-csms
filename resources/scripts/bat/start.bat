@echo off
REM → Thiết lập OEM code page nếu muốn chữ tiếng Việt hiển thị tốt:
chcp 65001 >nul

echo ================================
echo 1/4: Starting Oracle MTS Recovery Service
sc start OracleOraDb21Home1MTSRecoveryService
timeout /t 2 >nul

echo 2/4: Starting Oracle TNS Listener
sc start OracleOraDb21Home1TNSListener
timeout /t 2 >nul

echo 3/4: Starting Oracle Database Service
sc start OracleServiceORCL
timeout /t 2 >nul

echo 4/4: Starting Oracle VSS Writer Service
sc start OracleVssWriterORCL
timeout /t 2 >nul

echo.
echo ==> All Oracle service has started!
pause
