@echo off
REM Script para compilar y ejecutar la interfaz gráfica del Metro Client (Windows)
REM Uso: run_gui.bat

echo =========================================
echo   Metro Telemetry Client - GUI Launcher
echo =========================================
echo.

REM Verificar Java
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Java no esta instalado
    echo Por favor instale Java JDK 8 o superior
    pause
    exit /b 1
)

where javac >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: javac no esta disponible
    echo Por favor instale Java JDK 8 o superior
    pause
    exit /b 1
)

echo Compilando MetroClientGUI.java...
javac MetroClientGUI.java

if %errorlevel% neq 0 (
    echo ERROR en compilacion
    pause
    exit /b 1
)

echo Compilacion exitosa
echo.
echo Iniciando interfaz grafica...
echo.

REM Ejecutar la GUI
javaw MetroClientGUI

echo.
echo GUI cerrada
pause
