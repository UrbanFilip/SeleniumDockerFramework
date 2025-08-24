@echo off

REM Usage: waitFor.bat [TIMEOUT_SECONDS]
REM Env (optional): HUB_URL=http://host:port/status

set TIMEOUT=%~1
if "%TIMEOUT%"=="" set TIMEOUT=90

if "%HUB_URL%"=="" set HUB_URL=http://localhost:4444/status

set ELAPSED=0
set SLEEP=5

echo [%time:~0,8%] [INFO] Waiting for Selenium Grid (%HUB_URL%) to become ready (timeout: %TIMEOUT%s)...

:CHECK
REM --- Call external ps1 script
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0\check_grid.ps1" -HubUrl "%HUB_URL%"
set PSRC=%ERRORLEVEL%

if %PSRC%==0 (
  echo [%time:~0,8%] [OK] Selenium Grid is ready after %ELAPSED%s.
  exit /b 0
) else (
  if %PSRC%==2 (
    echo [%time:~0,8%] [INFO] Grid responded but not ready yet.
  ) else (
    echo [%time:~0,8%] [INFO] No response yet (endpoint unreachable).
  )
)

set /a ELAPSED+=%SLEEP%
set /a REMAINING=TIMEOUT-ELAPSED

if %ELAPSED% GEQ %TIMEOUT% (
  echo [%time:~0,8%] [ERROR] Grid not ready within %TIMEOUT% seconds.
  exit /b 1
)

echo [%time:~0,8%] [INFO] Not ready yet... elapsed: %ELAPSED%s, remaining: %REMAINING%s
powershell -NoProfile -Command "Start-Sleep -Seconds %SLEEP%" 1>nul 2>nul
goto CHECK