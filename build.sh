#!/bin/bash
# Build script for Render deployment
# This script is executed during the build phase on Render

set -e  # Exit on error

echo "=========================================="
echo "Starting Render Build Process"
echo "=========================================="

# Display Java version
echo "Java Version:"
java -version

# Display Maven version
echo "Maven Version:"
./mvnw -version

# Clean and build the project
echo "=========================================="
echo "Building Spring Boot Application..."
echo "=========================================="
./mvnw clean package -DskipTests

# Verify the WAR file was created
if [ -f "target/user-auth-app-1.0.0.war" ]; then
    echo "=========================================="
    echo "Build Successful!"
    echo "WAR file created: target/user-auth-app-1.0.0.war"
    echo "=========================================="
else
    echo "=========================================="
    echo "Build Failed: WAR file not found"
    echo "=========================================="
    exit 1
fi

# Display build artifacts
echo "Build artifacts:"
ls -lh target/*.war

echo "=========================================="
echo "Build Complete - Ready for Deployment"
echo "=========================================="

# Made with Bob
