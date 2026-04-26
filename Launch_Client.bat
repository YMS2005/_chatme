@echo off
REM ChatME Client Launcher
REM This script launches the ChatME client application

setlocal enabledelayedexpansion

REM Get the directory of this batch file
set SCRIPT_DIR=%~dp0

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in your PATH.
    echo Please install Java or add it to your system PATH.
    pause
    exit /b 1
)

REM Run the JAR file
echo Starting ChatME Client...
java -jar "%SCRIPT_DIR%launch\ChatME-Client.jar"

if %errorlevel% neq 0 (
    echo Error: Failed to start ChatME Client.
    pause
    exit /b 1
)

endlocal

