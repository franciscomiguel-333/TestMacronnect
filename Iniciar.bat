@echo off
title Instalador - Sistema de Ventas
color 0A

echo ============================================
echo   Sistema de Ventas - Instalacion Automatica
echo ============================================
echo.

docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Docker Desktop no esta corriendo.
    echo Abre Docker Desktop, espera a que inicie, y ejecuta este archivo de nuevo.
    pause
    exit /b 1
)

echo [OK] Docker detectado correctamente.
echo.
echo Este proceso construira la aplicacion y la base de datos.
echo Puede tardar varios minutos la primera vez, por favor espera...
echo.

start "" cmd /c "timeout /t 25 >nul && start http://localhost:8080/swagger-ui/"

docker-compose up --build

pause