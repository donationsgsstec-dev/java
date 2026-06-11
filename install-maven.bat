@echo off
REM ============================================================================
REM Maven Installation Script for Windows
REM This script downloads and installs Apache Maven automatically
REM ============================================================================

echo.
echo ============================================================================
echo Maven Installation Script for Windows
echo ============================================================================
echo.

REM Check if running as administrator
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo WARNING: Not running as administrator. Installation may fail.
    echo Please right-click this script and select "Run as administrator"
    echo.
    pause
)

REM Set Maven version
set MAVEN_VERSION=3.9.6
set MAVEN_DOWNLOAD_URL=https://dlcdn.apache.org/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip
set INSTALL_DIR=C:\Program Files\Apache\Maven
set MAVEN_HOME=%INSTALL_DIR%\apache-maven-%MAVEN_VERSION%

echo Step 1: Checking Java installation...
echo ----------------------------------------

REM Check if Java is installed
java -version >nul 2>&1
if %errorLevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo.
    echo Please install Java 17 or higher first:
    echo 1. Download from: https://adoptium.net/
    echo 2. Install Java
    echo 3. Run this script again
    echo.
    pause
    exit /b 1
)

echo Java is installed:
java -version
echo.

echo Step 2: Checking if Maven is already installed...
echo ----------------------------------------

where mvn.cmd >nul 2>&1
if %errorLevel% equ 0 (
    echo Maven is already installed:
    mvn.cmd --version
    echo.
    echo Do you want to reinstall Maven? (Y/N)
    set /p REINSTALL=
    if /i not "%REINSTALL%"=="Y" (
        echo Installation cancelled.
        pause
        exit /b 0
    )
)

echo Step 3: Creating installation directory...
echo ----------------------------------------

if not exist "%INSTALL_DIR%" (
    mkdir "%INSTALL_DIR%"
    if %errorLevel% neq 0 (
        echo ERROR: Failed to create directory: %INSTALL_DIR%
        echo Please run this script as administrator
        pause
        exit /b 1
    )
    echo Created: %INSTALL_DIR%
) else (
    echo Directory already exists: %INSTALL_DIR%
)
echo.

echo Step 4: Downloading Maven %MAVEN_VERSION%...
echo ----------------------------------------
echo This may take a few minutes depending on your internet connection...
echo.

REM Download Maven using PowerShell
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%MAVEN_DOWNLOAD_URL%' -OutFile '%TEMP%\maven.zip'}"

if %errorLevel% neq 0 (
    echo ERROR: Failed to download Maven
    echo Please check your internet connection and try again
    pause
    exit /b 1
)

echo Download completed successfully!
echo.

echo Step 5: Extracting Maven...
echo ----------------------------------------

REM Extract using PowerShell
powershell -Command "& {Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%INSTALL_DIR%' -Force}"

if %errorLevel% neq 0 (
    echo ERROR: Failed to extract Maven
    pause
    exit /b 1
)

echo Extraction completed successfully!
echo.

echo Step 6: Setting up environment variables...
echo ----------------------------------------

REM Set MAVEN_HOME environment variable
setx MAVEN_HOME "%MAVEN_HOME%" /M >nul 2>&1
if %errorLevel% neq 0 (
    echo WARNING: Failed to set MAVEN_HOME system variable
    echo You may need to set it manually
) else (
    echo Set MAVEN_HOME=%MAVEN_HOME%
)

REM Add Maven to PATH
for /f "tokens=2*" %%a in ('reg query "HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment" /v Path 2^>nul') do set "CURRENT_PATH=%%b"

echo %CURRENT_PATH% | find /i "%MAVEN_HOME%\bin" >nul
if %errorLevel% neq 0 (
    setx PATH "%CURRENT_PATH%;%MAVEN_HOME%\bin" /M >nul 2>&1
    if %errorLevel% neq 0 (
        echo WARNING: Failed to add Maven to system PATH
        echo You may need to add it manually
    ) else (
        echo Added Maven to system PATH
    )
) else (
    echo Maven is already in system PATH
)

echo.

echo Step 7: Cleaning up temporary files...
echo ----------------------------------------

del "%TEMP%\maven.zip" >nul 2>&1
echo Temporary files cleaned up
echo.

echo ============================================================================
echo Maven Installation Completed Successfully!
echo ============================================================================
echo.
echo Maven has been installed to: %MAVEN_HOME%
echo.
echo IMPORTANT: You need to restart your command prompt or PowerShell
echo for the changes to take effect.
echo.
echo After restarting, verify the installation by running:
echo   mvn --version
echo.
echo To use Maven in this project, run:
echo   mvn.cmd clean install
echo   mvn.cmd spring-boot:run
echo.
echo Or use the provided batch script:
echo   run-maven.bat clean install
echo   run-maven.bat spring-boot:run
echo.

pause

REM Open a new command prompt with updated environment
echo Opening a new command prompt with updated environment...
start cmd /k "echo Maven installation complete! && echo. && echo Verify installation: && mvn --version && echo. && cd /d %~dp0"

exit /b 0

@REM Made with Bob
