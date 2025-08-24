@echo off
setlocal enabledelayedexpansion

REM Usage: waitFor.bat [TIMEOUT_SECONDS]
set TIMEOUT=%~1
if "%TIMEOUT%"=="" set TIMEOUT=90

set ELAPSED=0
echo [%time:~0,8%] [INFO] Waiting for Selenium Grid to become ready (timeout: %TIMEOUT%s)...

:CHECK
curl -s http://localhost:4444/status | findstr /i "\"ready\":true" >nul
if %ERRORLEVEL%==0 (
    echo [%time:~0,8%] [OK] Selenium Grid is ready after %ELAPSED%s.
    exit /b 0
)

set /a ELAPSED+=5
set /a REMAINING=TIMEOUT-ELAPSED

if %ELAPSED% GEQ %TIMEOUT% (
    echo [%time:~0,8%] [ERROR] Grid not ready within %TIMEOUT% seconds.
    exit /b 1
)

echo [%time:~0,8%] [INFO] Not ready yet... elapsed: %ELAPSED%s, remaining: %REMAINING%s
timeout /t 5 /nobreak >nul
goto CHECK
