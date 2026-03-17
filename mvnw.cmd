@echo off
setlocal

set "__MVNW_CMD__="
for /f "usebackq delims=" %%I in (`powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0.mvn\wrapper\maven-wrapper.ps1" -ScriptName "%~nx0"`) do set "__MVNW_CMD__=%%I"

if not defined __MVNW_CMD__ (
  echo Cannot start Maven from wrapper 1>&2
  exit /b 1
)

"%__MVNW_CMD__%" %*