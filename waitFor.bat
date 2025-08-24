@echo off
REM Usage: waitFor.bat [TIMEOUT_SECONDS]
REM Optional: set HUB_URL=http://host:port/status  (default: http://localhost:4444/status)
REM Optional: set SLEEP=5

set "TIMEOUT=%~1"
if "%TIMEOUT%"=="" set "TIMEOUT=90"

if "%HUB_URL%"=="" set "HUB_URL=http://localhost:4444/status"
if "%SLEEP%"=="" set "SLEEP=5"

set "ELAPSED=0"
set "TMP=%TEMP%\grid_status.json"

echo Waiting for Selenium Grid (%HUB_URL%) to be ready (timeout: %TIMEOUT%s)...

:LOOP
curl -sSf "%HUB_URL%" -o "%TMP%" 2>nul
if errorlevel 1 goto NOTREADY

findstr /i /c:"\"ready\": true" "%TMP%" >nul
if not errorlevel 1 goto READY

findstr /i /c:"\"ready\":true" "%TMP%" >nul
if not errorlevel 1 goto READY

:NOTREADY
set /a ELAPSED+=SLEEP
if %ELAPSED% GEQ %TIMEOUT% goto TIMEOUT

echo Not ready yet... elapsed: %ELAPSED%s / %TIMEOUT%s
powershell -NoProfile -Command "Start-Sleep -Seconds %SLEEP%" 1>nul 2>nul
goto LOOP

:READY
echo READY after %ELAPSED%s.
del /q "%TMP%" >nul 2>&1
exit /b 0

:TIMEOUT
echo ERROR: not ready within %TIMEOUT%s.
del /q "%TMP%" >nul 2>&1
exit /b 1
