@echo off
setlocal

REM Usage: waitFor.bat [TIMEOUT_SECONDS]
REM Env optional: HUB_URL=http://host:port/status

set "TIMEOUT=%~1"
if "%TIMEOUT%"=="" set "TIMEOUT=90"

if "%HUB_URL%"=="" set "HUB_URL=http://localhost:4444/status"

set "ELAPSED=0"
set "SLEEP=5"
set "PS1=%TEMP%\_grid_status.ps1"

REM --- przygotuj minimalny skrypt PowerShell (bez nawiasów w CMD) ---
> "%PS1%" echo param([string]$Url)
>> "%PS1%" echo try {
>> "%PS1%" echo   $r = Invoke-RestMethod -Uri $Url -UseBasicParsing -TimeoutSec 3
>> "%PS1%" echo   if ($r.value.ready -eq $true) { exit 0 } else { exit 2 }
>> "%PS1%" echo } catch { exit 3 }

echo [%time:~0,8%] [INFO] Waiting for Selenium Grid (%HUB_URL%) to become ready (timeout: %TIMEOUT%s)...

:CHECK
powershell -NoProfile -ExecutionPolicy Bypass -File "%PS1%" "%HUB_URL%"
set "PSRC=%ERRORLEVEL%"

if "%PSRC%"=="0" (
  echo [%time:~0,8%] [OK] Selenium Grid is ready after %ELAPSED%s.
  del /q "%PS1%" >nul 2>&1
  exit /b 0
) else if "%PSRC%"=="2" (
  echo [%time:~0,8%] [INFO] Grid responded but not ready yet.
) else (
  echo [%time:~0,8%] [INFO] No response yet (endpoint unreachable).
)

set /a ELAPSED+=SLEEP
set /a REMAINING=TIMEOUT-ELAPSED

if %ELAPSED% GEQ %TIMEOUT% (
  echo [%time:~0,8%] [ERROR] Grid not ready within %TIMEOUT% seconds.
  del /q "%PS1%" >nul 2>&1
  exit /b 1
)

echo [%time:~0,8%] [INFO] Not ready yet... elapsed: %ELAPSED%s, remaining: %REMAINING%s
REM stabilny sleep w Jenkinsie:
powershell -NoProfile -Command "Start-Sleep -Seconds %SLEEP%" 1>nul 2>nul

goto CHECK
