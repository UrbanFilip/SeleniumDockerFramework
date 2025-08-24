@echo off
setlocal

REM Usage: waitFor.bat [TIMEOUT_SECONDS]
REM Optional ENV: HUB_URL=http://host:port/status , SLEEP=5 , DEBUG=1

set "TIMEOUT=%~1"
if "%TIMEOUT%"=="" set "TIMEOUT=90"

if "%HUB_URL%"=="" set "HUB_URL=http://localhost:4444/status"
if "%SLEEP%"=="" set "SLEEP=5"

set "ELAPSED=0"
set "TMP=%TEMP%\grid_status.json"

echo [%time:~0,8%] [INFO] Waiting for Selenium Grid (%HUB_URL%) to become ready (timeout: %TIMEOUT%s)...

:CHECK
REM pobierz status; -s cicho, -S pokaże błąd na stderr (który zrzucamy), -f => błąd dla HTTP>=400
curl -sSf "%HUB_URL%" -o "%TMP%" 2>nul
if errorlevel 1 (
  echo [%time:~0,8%] [INFO] No response yet (endpoint unreachable).
) else (
  REM sprawdź "ready": true (ze spacją) albo "ready":true (bez spacji)
  findstr /i /c:"\"ready\": true" "%TMP%" >nul
  if %ERRORLEVEL%==0 goto READY
  findstr /i /c:"\"ready\":true" "%TMP%" >nul
  if %ERRORLEVEL%==0 goto READY

  if "%DEBUG%"=="1" (
    echo [%time:~0,8%] [DEBUG] First 200 chars of /status:
    type "%TMP%" | cmd /v:on /c set /p= & echo.
  )
  echo [%time:~0,8%] [INFO] Grid responded but not ready yet.
)

set /a ELAPSED+=SLEEP
set /a REMAINING=TIMEOUT-ELAPSED

if %ELAPSED% GEQ %TIMEOUT% (
  echo [%time:~0,8%] [ERROR] Grid not ready within %TIMEOUT% seconds.
  del /q "%TMP%" >nul 2>&1
  exit /b 1
)

echo [%time:~0,8%] [INFO] Not ready yet... elapsed: %ELAPSED%s, remaining: %REMAINING%s

REM stabilny sleep (w Jenkins bat timeout bywa kłopotliwy)
ping -n %SLEEP% 127.0.0.1 >nul
goto CHECK

:READY
echo [%time:~0,8%] [OK] Selenium Grid is ready after %ELAPSED%s.
del /q "%TMP%" >nul 2>&1
exit /b 0
