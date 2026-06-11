@echo off
REM Batch script to run Maven commands on Windows
REM This bypasses any Python mvn package conflicts

REM Check if Maven is installed
where mvn.cmd >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/download.cgi
    echo And add it to your system PATH
    pause
    exit /b 1
)

REM Run Maven with the provided arguments
echo Running Maven command: mvn %*
mvn.cmd %*

pause

@REM Made with Bob
