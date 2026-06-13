#!/bin/bash
# Start script for Render deployment
# This script is executed to start the application on Render

set -e  # Exit on error

echo "=========================================="
echo "Starting Pahappa Attendance System"
echo "=========================================="

# Display environment info
echo "Environment: ${SPRING_PROFILES_ACTIVE:-default}"
echo "Port: ${PORT:-8080}"
echo "Java Options: ${JAVA_TOOL_OPTIONS}"

# Check if WAR file exists
if [ ! -f "target/user-auth-app-1.0.0.war" ]; then
    echo "ERROR: WAR file not found!"
    echo "Please ensure the build completed successfully."
    exit 1
fi

# Start the Spring Boot application
echo "=========================================="
echo "Launching Spring Boot Application..."
echo "=========================================="

exec java \
    -Dserver.port=${PORT:-8080} \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-production} \
    ${JAVA_TOOL_OPTIONS} \
    -jar target/user-auth-app-1.0.0.war

# Made with Bob
