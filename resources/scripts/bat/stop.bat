@echo off
echo ================================
echo 1/4: Stopping Oracle VSS Writer Service
sc stop oraclevsswriterorcl
timeout /t 2 >nul

echo 2/4: Stopping Oracle Database Service
sc stop OracleServiceORCL
timeout /t 2 >nul

echo 3/4: Stopping Oracle TNS Listener
sc stop OracleOraDB21Home1TNSListener
timeout /t 2 >nul

echo 4/4: Stopping Oracle Recovery Service
sc stop oracleoradb21home1mtsrecoveryservice
timeout /t 2 >nul

echo.
echo ==> All Oracle service have stopped!
pause
