@echo off
REM Usage: waitFor.bat [TIMEOUT_SECONDS]
REM Optional ENV: HUB_URL=http://host:port/status , SLEEP=5 , DEBUG=1

setlocal

set "TIMEOUT=%~1"
if "%TIMEOUT%"=="" set "TIMEOUT=90"

if "%HUB_URL%"=="" set "HUB_URL=http://localhost:4444/status"
if "%SLEEP%"=="" set "SLEEP=5"

set "ELAPSED=0"
set "TMP=%TEMP%\grid_status.json"

echo Waiting for Selenium Grid (%HUB_URL%) to be ready (timeout: %TIMEOUT%s)...

:LOOP
REM pobierz status (cicho), HTTP błąd nie zrywa procesu, bo zależy nam na treści
curl -s "%HUB_URL%" -o "%TMP%" 2>nul
if errorlevel 1 goto NOTREADY

REM SZUKAJ: "ready":[ ]*true  (działa dla "ready": true i "ready":true i z wieloma spacjami)
findstr /R /I /C:"\"ready\":[ ]*true" "%TMP%" >nul
if not errorlevel 1 goto READY

if "%DEBUG%"=="1" (
  echo --- /status (first 300 chars) ---
  for /f "usebackq tokens=* delims=" %%L in ("%TMP%") do (
    echo %%L
    goto :AFTERDUMP
  )
  :AFTERDUMP
  echo -------------------------------
)

:NOTREADY
set /a ELAPSED+=SLEEP
if %ELAPSED% GEQ %TIMEOUT% goto TIMEOUT

echo Not ready yet... elapsed: %ELAPSED%s / %TIMEOUT%s
REM stabilny sleep bez PowerShella
set /a P=%SLEEP%+1
ping -n %P% 127.0.0.1 >nul
goto LOOP

:READY
echo READY after %ELAPSED%s.
del /q "%TMP%" >nul 2>&1
exit /b 0

:TIMEOUT
echo ERROR: not ready within %TIMEOUT%s.
if "%DEBUG%"=="1" (
  echo --- final /status content ---
  type "%TMP%"
  echo -----------------------------
)
del /q "%TMP%" >nul 2>&1
exit /b 1
