@echo off
REM ============================================================================
REM Use Local Maven Installation
REM This script uses the Maven installation in the project directory
REM ============================================================================

set LOCAL_MAVEN=%~dp0apache-maven-3.9.16
set MAVEN_BIN=%LOCAL_MAVEN%\bin\mvn.cmd

echo.
echo ============================================================================
echo Using Local Maven Installation
echo ============================================================================
echo.
echo Maven Location: %LOCAL_MAVEN%
echo.

REM Check if local Maven exists
if not exist "%MAVEN_BIN%" (
    echo ERROR: Maven not found at: %LOCAL_MAVEN%
    echo.
    echo Please verify the Maven directory exists in the project folder.
    pause
    exit /b 1
)

REM Check Java installation
java -version >nul 2>&1
if %errorLevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher from: https://adoptium.net/
    pause
    exit /b 1
)

echo Java Version:
java -version
echo.

REM Run Maven with provided arguments
if "%~1"=="" (
    echo Usage: use-local-maven.bat [maven-command]
    echo.
    echo Examples:
    echo   use-local-maven.bat clean install
    echo   use-local-maven.bat spring-boot:run
    echo   use-local-maven.bat --version
    echo.
    pause
    exit /b 0
)

echo Running: %MAVEN_BIN% %*
echo.
echo ============================================================================
echo.

"%MAVEN_BIN%" %*

echo.
echo ============================================================================
echo Maven command completed
echo ============================================================================
echo.

pause

@REM Made with Bob
